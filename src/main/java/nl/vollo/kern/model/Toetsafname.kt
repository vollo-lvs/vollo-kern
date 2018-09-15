package nl.vollo.kern.model;

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "toetsafnames")
data class Toetsafname(
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vollo_seq")
        @SequenceGenerator(name = "vollo_seq", sequenceName = "vollo_seq", allocationSize = 1)
        @Column(name = "id", updatable = false, nullable = false)
        override val id: Long?,

        @Version
        @Column(name = "version")
        override val version: Int,

        @Column(name = "omschrijving")
        val omschrijving: String?,

        @ManyToOne(targetEntity = Toets::class)
        @JoinColumn(name = "toets_id", foreignKey = ForeignKey(name = "tan_tts_fk"))
        val toets: Toets,

        @Column(name = "datum")
        @Temporal(TemporalType.DATE)
        val datum: Date?
) : DomainObject(DomainEntity.toetsafname, id, version)
