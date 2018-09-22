package nl.vollo.kern.model;

import com.fasterxml.jackson.annotation.JsonIgnore
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "scholen")
data class School(

        @Transient
        override val _type: DomainEntity = DomainEntity.SCHOOL,

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vollo_seq")
        @SequenceGenerator(name = "vollo_seq", sequenceName = "vollo_seq", allocationSize = 1)
        @Column(name = "id", updatable = false, nullable = false)
        override val id: Long? = null,

        @Version
        @Column(name = "version")
        override val version: Int = 0,

        @Column(nullable = false)
        val naam: String,

        @Embedded
        val adres: Adres? = null,

        @ManyToOne(targetEntity = School::class)
        @JoinColumn(name = "hoort_bij_school_id", foreignKey = ForeignKey(name = "scl_scl_fk"))
        val hoortBij: School? = null,

        @JsonIgnore
        @OneToMany(mappedBy = "hoortBij", targetEntity = School::class, fetch = FetchType.LAZY)
        val bijbehorendeScholen: MutableList<School> = ArrayList(),

        @JsonIgnore
        @OneToMany(mappedBy = "school", targetEntity = Groep::class, fetch = FetchType.LAZY)
        val groepen: MutableList<Groep> = ArrayList(),

        @JsonIgnore
        @OneToMany(mappedBy = "school", targetEntity = Inschrijving::class, fetch = FetchType.LAZY)
        val inschrijvingen: MutableList<Inschrijving> = ArrayList()

) : DomainObject(_type, id, version)
