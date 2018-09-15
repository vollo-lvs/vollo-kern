package nl.vollo.kern.model;

import javax.persistence.*

@Entity
@Table(name = "ouder_leerlingen")
data class OuderLeerling(
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vollo_seq")
        @SequenceGenerator(name = "vollo_seq", sequenceName = "vollo_seq", allocationSize = 1)
        @Column(name = "id", updatable = false, nullable = false)
        override val id: Long?,

        @Version
        @Column(name = "version")
        override val version: Int,

        @ManyToOne(targetEntity = Ouder::class)
        @JoinColumn(name = "ouder_id", foreignKey = ForeignKey(name = "oll_oud_fk"))
        val ouder: Ouder,

        @ManyToOne(targetEntity = Leerling::class)
        @JoinColumn(name = "leerling_id", foreignKey = ForeignKey(name = "oll_llg_fk"))
        val leerling: Leerling
) : DomainObject(DomainEntity.ouderLeerling, id, version)
