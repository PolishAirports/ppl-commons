package pl.ppl.common.infrastructure.repository

import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface CustomerRepository extends FetchableJpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {
}
