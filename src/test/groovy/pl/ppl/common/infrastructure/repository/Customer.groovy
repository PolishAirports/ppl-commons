package pl.ppl.common.infrastructure.repository

import groovy.transform.ToString

import javax.persistence.*

@Entity
//@EqualsAndHashCode
@NamedEntityGraph(name = "Customer.withNicknames",
        attributeNodes = @NamedAttributeNode("nicknames"))
@ToString
class Customer {

    @Id
    Long id

    String name

    @ElementCollection
    Collection<String> nicknames;
}
