package nl.vollo.kern.model;

import javax.persistence.*

@Entity
@Table(name = "toetsen")
data class Toets(
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vollo_seq")
        @SequenceGenerator(name = "vollo_seq", sequenceName = "vollo_seq", allocationSize = 1)
        @Column(name = "id", updatable = false, nullable = false)
        override val id: Long?,

        @Version
        @Column(name = "version")
        override val version: Int,

        // TODO optioneel koppelen aan school

        // TODO waarden?
        @Column(name = "soort")
        val soort: String?,

        @Column(name = "soort_score")
        @Enumerated(EnumType.STRING)
        val soortScore: SoortScore,

        @Column(name = "omschrijving")
        val omschrijving: String?

) : DomainObject(DomainEntity.toets, id, version)
