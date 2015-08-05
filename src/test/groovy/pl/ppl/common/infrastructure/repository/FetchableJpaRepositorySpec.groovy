package pl.ppl.common.infrastructure.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import javax.persistence.EntityNotFoundException

@ContextConfiguration(classes = RepositoriesConfig)
class FetchableJpaRepositorySpec extends Specification {

    @Autowired
    CustomerRepository customerRepository

    def 'should fetch existing entity'() {
        given:
            Long customerId = 1L
            Customer customer = new Customer(id: customerId)
            customerRepository.save(customer)
        expect:
            customerRepository.fetchById(customerId) == customer
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
}