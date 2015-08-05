package pl.ppl.common.infrastructure.repository

import groovy.transform.EqualsAndHashCode

import javax.persistence.Entity
import javax.persistence.Id

@Entity
@EqualsAndHashCode
class Customer {

    @Id
    Long id
}
