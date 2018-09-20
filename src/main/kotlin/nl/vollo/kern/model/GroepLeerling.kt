package nl.vollo.kern.model;

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "groep_leerlingen")
data class GroepLeerling(
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vollo_seq")
        @SequenceGenerator(name = "vollo_seq", sequenceName = "vollo_seq", allocationSize = 1)
        @Column(name = "id", updatable = false, nullable = false)
        override val id: Long? = null,

        @Version
        @Column(name = "version")
        override val version: Int = 0,

        @ManyToOne(targetEntity = Groep::class)
        @JoinColumn(name = "groep_id", foreignKey = ForeignKey(name = "gll_grp_fk"))
        val groep: Groep,

        @ManyToOne(targetEntity = Leerling::class)
        @JoinColumn(name = "leerling_id", foreignKey = ForeignKey(name = "gll_llg_fk"))
        val leerling: Leerling,

        @Column(name = "datum_begin", nullable = false)
        @Temporal(TemporalType.DATE)
        val datumBegin: Date,

        @Column(name = "datum_einde")
        @Temporal(TemporalType.DATE)
        val datumEinde: Date? = null

) : DomainObject(DomainEntity.GROEP_LEERLING, id, version)
