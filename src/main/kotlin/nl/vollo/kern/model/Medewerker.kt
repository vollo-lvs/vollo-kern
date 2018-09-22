package nl.vollo.kern.model;

import javax.persistence.*

@Entity
@Table(name = "medewerkers")
data class Medewerker(

        @Transient
        override val _type: DomainEntity = DomainEntity.MEDEWERKER,

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

) : DomainObject(_type, id, version)
