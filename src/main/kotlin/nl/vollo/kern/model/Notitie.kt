package nl.vollo.kern.model

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "notities")
class Notitie(
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vollo_seq")
        @SequenceGenerator(name = "vollo_seq", sequenceName = "vollo_seq", allocationSize = 1)
        @Column(name = "id", updatable = false, nullable = false)
        override val id: Long? = null,

        @Version
        @Column(name = "version")
        override val version: Int = 0,

        @Column(nullable = false)
        var tekst: String? = null,

        @Column(nullable = false)
        @Temporal(TemporalType.TIMESTAMP)
        var datum: Date? = null,

        @ManyToOne(targetEntity = Leerling::class)
        @JoinColumn(name = "leerling_id", foreignKey = ForeignKey(name = "nti_llg_fk"))
        var leerling: Leerling? = null,

        @ManyToOne(targetEntity = Medewerker::class)
        @JoinColumn(name = "medewerker_id", foreignKey = ForeignKey(name = "nti_mdw_fk"))
        var medewerker: Medewerker? = null

) : DomainObject() {
    override val _type: DomainEntity get() = DomainEntity.NOTITIE
}
