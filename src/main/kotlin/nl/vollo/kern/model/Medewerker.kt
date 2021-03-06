package nl.vollo.kern.model;

import javax.persistence.*

@Entity
@Table(name = "medewerkers")
class Medewerker(

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vollo_seq")
        @SequenceGenerator(name = "vollo_seq", sequenceName = "vollo_seq", allocationSize = 1)
        @Column(name = "id", updatable = false, nullable = false)
        override val id: Long? = null,

        @Version
        @Column(name = "version")
        override val version: Int = 0,

        @Column(nullable = false)
        val voornaam: String,

        @Column
        val tussenvoegsel: String? = null,

        @Column(nullable = false)
        val achternaam: String

) : DomainObject() {
    override val _type: DomainEntity get() = DomainEntity.MEDEWERKER
}
