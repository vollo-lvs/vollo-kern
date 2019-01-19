package nl.vollo.kern.model;

import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "inschrijvingen")
class Inschrijving(

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vollo_seq")
        @SequenceGenerator(name = "vollo_seq", sequenceName = "vollo_seq", allocationSize = 1)
        @Column(name = "id", updatable = false, nullable = false)
        override val id: Long? = null,

        @Version
        @Column(name = "version")
        override val version: Int = 0,

        @ManyToOne(targetEntity = Leerling::class)
        @JoinColumn(name = "leerling_id", foreignKey = ForeignKey(name = "ins_llg_fk"))
        val leerling: Leerling,

        @Column(name = "datum_inschrijving", nullable = false)
        val datumInschrijving: LocalDate,

        @Column(name = "datum_uitschrijving")
        val datumUitschrijving: LocalDate? = null,

        @ManyToOne(targetEntity = School::class)
        @JoinColumn(name = "school_id", foreignKey = ForeignKey(name = "ins_scl_fk"), nullable = false)
        val school: School

) : DomainObject() {
    override val _type: DomainEntity get() = DomainEntity.INSCHRIJVING
}
