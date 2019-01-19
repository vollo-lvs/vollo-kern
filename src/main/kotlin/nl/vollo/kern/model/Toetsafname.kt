package nl.vollo.kern.model;

import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "toetsafnames")
class Toetsafname(

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vollo_seq")
        @SequenceGenerator(name = "vollo_seq", sequenceName = "vollo_seq", allocationSize = 1)
        @Column(name = "id", updatable = false, nullable = false)
        override val id: Long? = null,

        @Version
        @Column(name = "version")
        override val version: Int = 0,

        @Column(name = "omschrijving")
        val omschrijving: String? = null,

        @ManyToOne(targetEntity = Toets::class)
        @JoinColumn(name = "toets_id", foreignKey = ForeignKey(name = "tan_tts_fk"))
        val toets: Toets,

        @Column(name = "datum")
        val datum: LocalDate? = null

) : DomainObject() {
    override val _type: DomainEntity get() = DomainEntity.TOETSAFNAME
}
