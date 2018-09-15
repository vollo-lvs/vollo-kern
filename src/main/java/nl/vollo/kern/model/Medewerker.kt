package nl.vollo.kern.model;

import javax.persistence.*
import javax.xml.bind.annotation.XmlRootElement

@Entity
@Table(name = "medewerkers")
@XmlRootElement
data class Medewerker(
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vollo_seq")
        @SequenceGenerator(name = "vollo_seq", sequenceName = "vollo_seq", allocationSize = 1)
        @Column(name = "id", updatable = false, nullable = false)
        override val id: Long?,

        @Version
        @Column(name = "version")
        override val version: Int,

        @Column(nullable = false)
        val voornaam: String,

        @Column
        val tussenvoegsel: String?,

        @Column(nullable = false)
        val achternaam: String

) : DomainObject(DomainEntity.medewerker, id, version)
