package nl.vollo.kern.model

import com.fasterxml.jackson.annotation.JsonIgnore
import java.util.*
import javax.persistence.*
import javax.xml.bind.annotation.XmlRootElement

@Entity
@Table(name = "leerlingen")
@XmlRootElement
data class Leerling(
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vollo_seq")
        @SequenceGenerator(name = "vollo_seq", sequenceName = "vollo_seq", allocationSize = 1)
        @Column(name = "id", updatable = false, nullable = false)
        override val id: Long? = null,

        @Version
        @Column(name = "version")
        override val version: Int = 0,

        @Column(nullable = false)
        val voornamen: String,

        @Column(nullable = false)
        val roepnaam: String,

        @Column
        val tussenvoegsel: String?,

        @Column(nullable = false)
        val achternaam: String,

        @Column(nullable = false)
        @Temporal(TemporalType.DATE)
        val geboortedatum: Date,

        @Enumerated(EnumType.STRING)
        val geslacht: Geslacht?,

        @Embedded
        val adres: Adres?,

        @JsonIgnore
        @OneToMany(mappedBy = "leerling", targetEntity = Inschrijving::class, fetch = FetchType.LAZY)
        @OrderBy("datumInschrijving")
        val inschrijvingen: List<Inschrijving> = ArrayList()
) : DomainObject(DomainEntity.leerling, id, version)
