package nl.vollo.kern.model;

import com.fasterxml.jackson.annotation.JsonIgnore
import java.util.*
import javax.persistence.*
import javax.xml.bind.annotation.XmlRootElement

@Entity
@Table(name = "scholen")
@XmlRootElement
data class School(
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vollo_seq")
        @SequenceGenerator(name = "vollo_seq", sequenceName = "vollo_seq", allocationSize = 1)
        @Column(name = "id", updatable = false, nullable = false)
        override val id: Long?,

        @Version
        @Column(name = "version")
        override val version: Int,

        @Column(nullable = false)
        val naam: String,

        @Embedded
        val adres: Adres?,

        @ManyToOne(targetEntity = School::class)
        @JoinColumn(name = "hoort_bij_school_id", foreignKey = ForeignKey(name = "scl_scl_fk"))
        val hoortBij: School?,

        @JsonIgnore
        @OneToMany(mappedBy = "hoortBij", targetEntity = School::class, fetch = FetchType.LAZY)
        val bijbehorendeScholen: List<School> = ArrayList(),

        @JsonIgnore
        @OneToMany(mappedBy = "school", targetEntity = Groep::class, fetch = FetchType.LAZY)
        val groepen: List<Groep> = ArrayList(),

        @JsonIgnore
        @OneToMany(mappedBy = "school", targetEntity = Inschrijving::class, fetch = FetchType.LAZY)
        val inschrijvingen: List<Inschrijving> = ArrayList()

) : DomainObject(DomainEntity.school, id, version)
