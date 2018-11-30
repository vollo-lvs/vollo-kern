package nl.vollo.kern.model;

import javax.persistence.*

@Entity
@Table(name = "toetsen")
class Toets(

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vollo_seq")
        @SequenceGenerator(name = "vollo_seq", sequenceName = "vollo_seq", allocationSize = 1)
        @Column(name = "id", updatable = false, nullable = false)
        override val id: Long? = null,

        @Version
        @Column(name = "version")
        override val version: Int = 0,

        // TODO optioneel koppelen aan school

        // TODO waarden?
        @Column(name = "soort")
        val soort: String? = null,

        @Column(name = "soort_score")
        @Enumerated(EnumType.STRING)
        val soortScore: SoortScore,

        @Column(name = "omschrijving")
        val omschrijving: String? = null

) : DomainObject() {
    override val _type: DomainEntity get() = DomainEntity.TOETS
}
