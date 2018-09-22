package nl.vollo.kern.model;

import java.math.BigDecimal
import javax.persistence.*

@Entity
@Table(name = "scores")
data class Score(

        @Transient
        override val _type: DomainEntity = DomainEntity.SCORE,

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vollo_seq")
        @SequenceGenerator(name = "vollo_seq", sequenceName = "vollo_seq", allocationSize = 1)
        @Column(name = "id", updatable = false, nullable = false)
        override val id: Long? = null,

        @Version
        @Column(name = "version")
        override val version: Int = 0,

        @ManyToOne(targetEntity = Leerling::class)
        @JoinColumn(name = "leerling_id", foreignKey = ForeignKey(name = "sco_llg_fk"))
        val leerling: Leerling,

        @ManyToOne(targetEntity = Toetsafname::class)
        @JoinColumn(name = "toetsafname_id", foreignKey = ForeignKey(name = "sco_tan_fk"))
        val toetsafname: Toetsafname,

        @Column(name = "letter_score")
        val letterScore: String? = null,

        @Column(name = "cijfer_score")
        val cijferScore: BigDecimal? = null

) : DomainObject(_type, id, version)
