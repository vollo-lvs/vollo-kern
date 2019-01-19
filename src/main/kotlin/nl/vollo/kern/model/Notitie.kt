package nl.vollo.kern.model

import nl.vollo.kern.model.enums.NotitieNiveau
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotNull

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
        @NotNull
        var tekst: String? = null,

        @Column(nullable = false)
        @NotNull
        var datum: LocalDateTime? = null,

        @Column(name = "notitie_niveau", nullable = false)
        @NotNull
        @Enumerated(EnumType.STRING)
        var notitieNiveau: NotitieNiveau? = null,

        @ManyToOne(targetEntity = Leerling::class)
        @JoinColumn(name = "leerling_id", foreignKey = ForeignKey(name = "nti_llg_fk"))
        @NotNull
        var leerling: Leerling? = null,

        @ManyToOne(targetEntity = Medewerker::class)
        @JoinColumn(name = "medewerker_id", foreignKey = ForeignKey(name = "nti_mdw_fk"))
        @NotNull
        var medewerker: Medewerker? = null

) : DomainObject() {
    override val _type: DomainEntity get() = DomainEntity.NOTITIE
}
