package nl.vollo.kern.model;

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "groep_medewerkers")
data class GroepMedewerker(
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vollo_seq")
        @SequenceGenerator(name = "vollo_seq", sequenceName = "vollo_seq", allocationSize = 1)
        @Column(name = "id", updatable = false, nullable = false)
        override val id: Long?,

        @Version
        @Column(name = "version")
        override val version: Int,

        @ManyToOne(targetEntity = Groep::class)
        @JoinColumn(name = "groep_id", foreignKey = ForeignKey(name = "gmw_grp_fk"))
        val groep: Groep,

        @ManyToOne(targetEntity = Medewerker::class)
        @JoinColumn(name = "medewerker_id", foreignKey = ForeignKey(name = "gmw_mdw_fk"))
        val medewerker: Medewerker,

        @Column(name = "datum_begin", nullable = false)
        @Temporal(TemporalType.DATE)
        val datumBegin: Date,

        @Column(name = "datum_einde")
        @Temporal(TemporalType.DATE)
        val datumEinde: Date?

) : DomainObject(DomainEntity.groepMedewerker, id, version)
