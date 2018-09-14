package nl.vollo.kern.model;

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "inschrijvingen")
data class Inschrijving(
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vollo_seq")
        @SequenceGenerator(name = "vollo_seq", sequenceName = "vollo_seq", allocationSize = 1)
        @Column(name = "id", updatable = false, nullable = false)
        override val id: Long?,

        @Version
        @Column(name = "version")
        override val version: Int = 0,

        @ManyToOne(targetEntity = Leerling::class)
        @JoinColumn(name = "leerling_id", foreignKey = ForeignKey(name = "ins_llg_fk"))
        val leerling: Leerling,

        @Column(name = "datum_inschrijving", nullable = false)
        @Temporal(TemporalType.DATE)
        val datumInschrijving: Date,

        @Column(name = "datum_uitschrijving")
        @Temporal(TemporalType.DATE)
        val datumUitschrijving: Date?,

        @ManyToOne(targetEntity = School::class)
        @JoinColumn(name = "school_id", foreignKey = ForeignKey(name = "ins_scl_fk"), nullable = false)
        val school: School

) : DomainObject(DomainEntity.inschrijving, id, version)
