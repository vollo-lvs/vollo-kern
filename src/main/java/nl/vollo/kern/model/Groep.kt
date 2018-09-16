package nl.vollo.kern.model;

import com.fasterxml.jackson.annotation.JsonIgnore
import java.util.*
import javax.persistence.*
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

@Entity
@Table(name = "groepen")
data class Groep(
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
        val naam: String,

        @Column(nullable = false)
        @Min(1L)
        @Max(8L)
        @NotNull
        val niveau: Int,

        @JsonIgnore
        @OneToMany(mappedBy = "groep", targetEntity = GroepLeerling::class, fetch = FetchType.LAZY)
        val groepLeerlingen: MutableList<GroepLeerling> = ArrayList(),

        @JsonIgnore
        @OneToMany(mappedBy = "groep", targetEntity = GroepMedewerker::class, fetch = FetchType.LAZY)
        val groepMedewerkers: MutableList<GroepMedewerker> = ArrayList(),

        @ManyToOne(targetEntity = School::class)
        @JoinColumn(name = "school_id", foreignKey = ForeignKey(name = "grp_scl_fk"), nullable = false)
        val school: School

) : DomainObject(DomainEntity.groep, id, version)
