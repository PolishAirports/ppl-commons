package pl.ppl.common.infrastructure.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specifications
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import javax.persistence.EntityManager
import javax.persistence.EntityNotFoundException
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

@ContextConfiguration(classes = RepositoriesConfig)
class FetchableJpaRepositorySpec extends Specification {

    @Autowired
    CustomerRepository customerRepository

    @Autowired
    EntityManager entityManager

    def setup() {
        customerRepository.deleteAll()
    }

    def 'should fetch existing entity'() {
        given:
            Long customerId = 1L
            Customer customer = new Customer(id: customerId, name: 'test')
            customerRepository.save(customer)
        when:
          Customer retrivedCustomer = customerRepository.fetchById(customerId)
        then:
          customerRepository.fetchById(customerId).id == retrivedCustomer.id
          !isLoaded(retrivedCustomer.nicknames)
    }

    def 'should fail to fetch non-existient entity'() {
        given:
            Long nonExistientCustomerId = 99L
        when:
            customerRepository.fetchById(nonExistientCustomerId)
        then:
            def ex = thrown(JpaObjectRetrievalFailureException)
            ex.message.startsWith "Customer entity with id=$nonExistientCustomerId does not exist"
            ex.cause.class == EntityNotFoundException
    }

    def 'should return customer with match name'() {
        given:
          Customer customer = new Customer(id: 1L, name: 'test')
          customerRepository.save(customer)
          Customer customer2 = new Customer(id: 2L, name: 'michal')
          customer2.nicknames = ["test"]
          customerRepository.save(customer2)
        when:
          List<Customer> customers = customerRepository.findAll(new org.springframework.data.jpa.domain.Specification() {
              @Override
              Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                  return cb.like(root.get("name"), "michal");
              }
          }, EntityGraph.EntityGraphType.FETCH, "Customer.withNicknames")
        then:
          customers.size() == 1
          customers[0].name == 'michal'
          isLoaded(customers[0].nicknames)
    }

    def 'should return customer with match name with paged result'() {
        given:
          Customer customer = new Customer(id: 1L, name: 'test1')
          customer.nicknames = ["test"]
          customerRepository.save(customer)
          Customer customer2 = new Customer(id: 2L, name: 'test2')
          customer2.nicknames = ["test"]
          customerRepository.save(customer2)
          Customer customer3 = new Customer(id: 3L, name: 'michal')
          customer3.nicknames = ["test"]
          customerRepository.save(customer3)
        when:
          Page<Customer> customers = customerRepository.findAll(new org.springframework.data.jpa.domain.Specification() {
              @Override
              Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                  return cb.like(root.get("name"), "test%");
              }
          }, new PageRequest(0, 1, new Sort("name")), EntityGraph.EntityGraphType.FETCH, "Customer.withNicknames")
        then:
          customers.getTotalElements() == 2
          customers.getTotalPages() == 2
          customers.getContent().size() == 1
          customers[0].name == 'test1'
          isLoaded(customers[0].nicknames)
    }

    def 'should return customer with match name with sorted result'() {
        given:
          Customer customer = new Customer(id: 1L, name: 'test1')
          customer.nicknames = ["test"]
          customerRepository.save(customer)
          Customer customer2 = new Customer(id: 2L, name: 'test2')
          customer2.nicknames = ["test"]
          customerRepository.save(customer2)
          Customer customer3 = new Customer(id: 3L, name: 'michal')
          customer3.nicknames = ["test"]
          customerRepository.save(customer3)
        when:
          List<Customer> customers = customerRepository.findAll(new org.springframework.data.jpa.domain.Specification() {
              @Override
              Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                  return cb.like(root.get("name"), "test%");
              }
          }, new Sort("name"), EntityGraph.EntityGraphType.FETCH, "Customer.withNicknames")
        then:
          customers[0].name == 'test1'
          isLoaded(customers[0].nicknames)
          customers[1].name == 'test2'
          isLoaded(customers[1].nicknames)
    }

    def 'should return one customer with match name '() {
        given:
          Customer customer = new Customer(id: 1L, name: 'test1')
          customer.nicknames = ["test"]
          customerRepository.save(customer)
          Customer customer2 = new Customer(id: 2L, name: 'test2')
          customer2.nicknames = ["test"]
          customerRepository.save(customer2)
          Customer customer3 = new Customer(id: 3L, name: 'michal')
          customer3.nicknames = ["test"]
          customerRepository.save(customer3)
        when:
          Customer oneCustomer = customerRepository.findOne(new org.springframework.data.jpa.domain.Specification() {
              @Override
              Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                  return cb.like(root.get("name"), "michal%");
              }
          }, EntityGraph.EntityGraphType.FETCH, "Customer.withNicknames")
        then:
          oneCustomer.name == 'michal'
          isLoaded(oneCustomer.nicknames)
    }

    def 'should fetch existing entity without initialized collection'() {
        given:
          Customer customer = new Customer(id: 1L, name: 'test')
          customerRepository.save(customer)
        when:
          Customer retriedCustomer = customerRepository.findOne(1L)
        then:
          !isLoaded(retriedCustomer.nicknames)
    }

    private boolean isLoaded(Collection<String> collection) {
        entityManager.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(collection)
    }


}