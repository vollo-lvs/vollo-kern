package nl.vollo.kern.model;

import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "groep_medewerkers")
class GroepMedewerker(

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vollo_seq")
        @SequenceGenerator(name = "vollo_seq", sequenceName = "vollo_seq", allocationSize = 1)
        @Column(name = "id", updatable = false, nullable = false)
        override val id: Long? = null,

        @Version
        @Column(name = "version")
        override val version: Int = 0,

        @ManyToOne(targetEntity = Groep::class)
        @JoinColumn(name = "groep_id", foreignKey = ForeignKey(name = "gmw_grp_fk"))
        val groep: Groep,

        @ManyToOne(targetEntity = Medewerker::class)
        @JoinColumn(name = "medewerker_id", foreignKey = ForeignKey(name = "gmw_mdw_fk"))
        val medewerker: Medewerker,

        @Column(name = "datum_begin", nullable = false)
        val datumBegin: LocalDate,

        @Column(name = "datum_einde")
        val datumEinde: LocalDate? = null

) : DomainObject() {
    override val _type: DomainEntity get() = DomainEntity.GROEP_MEDEWERKER
}
