package nl.vollo.kern.model;

import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.xml.bind.annotation.XmlRootElement

@Entity
@Table(name = "ouders")
@XmlRootElement
data class Ouder(
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vollo_seq")
        @SequenceGenerator(name = "vollo_seq", sequenceName = "vollo_seq", allocationSize = 1)
        @Column(name = "id", updatable = false, nullable = false)
        override val id: Long? = null,

        @Version
        @Column(name = "version")
        override val version: Int = 0,

        @Column(nullable = false)
        @NotNull
        val voornaam: String,

        @Column
        val tussenvoegsel: String? = null,

        @Column(nullable = false)
        @NotNull
        val achternaam: String,

        @Column(nullable = false)
        @Temporal(TemporalType.DATE)
        @NotNull
        val geboortedatum: Date,

        @Enumerated(EnumType.STRING)
        val geslacht: Geslacht? = null,

        @Embedded
        val adres: Adres? = null

) : DomainObject(DomainEntity.OUDER, id, version)

