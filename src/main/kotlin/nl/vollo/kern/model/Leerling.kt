package nl.vollo.kern.model

import com.fasterxml.jackson.annotation.JsonIgnore
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "leerlingen")
class Leerling(

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vollo_seq")
        @SequenceGenerator(name = "vollo_seq", sequenceName = "vollo_seq", allocationSize = 1)
        @Column(name = "id", updatable = false, nullable = false)
        override val id: Long? = null,

        @Version
        @Column(name = "version")
        override val version: Int = 0,

        @Column(nullable = false)
        var voornamen: String,

        @Column(nullable = false)
        var roepnaam: String,

        @Column
        var tussenvoegsel: String? = null,

        @Column(nullable = false)
        var achternaam: String,

        @Column(nullable = false)
        @Temporal(TemporalType.DATE)
        var geboortedatum: Date,

        @Enumerated(EnumType.STRING)
        var geslacht: Geslacht? = null,

        @Embedded
        var adres: Adres? = null,

        @JsonIgnore
        @OneToMany(mappedBy = "leerling", targetEntity = Inschrijving::class, fetch = FetchType.LAZY)
        @OrderBy("datumInschrijving")
        var inschrijvingen: MutableList<Inschrijving> = ArrayList()
) : DomainObject() {
    override val _type: DomainEntity get() = DomainEntity.LEERLING
}
