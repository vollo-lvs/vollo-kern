package nl.vollo.kern.model;

import com.fasterxml.jackson.annotation.JsonIgnore
import java.util.ArrayList
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.ForeignKey
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.SequenceGenerator
import javax.persistence.Table
import javax.persistence.Version

@Entity
@Table(name = "scholen")
class School(

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vollo_seq")
        @SequenceGenerator(name = "vollo_seq", sequenceName = "vollo_seq", allocationSize = 1)
        @Column(name = "id", updatable = false, nullable = false)
        override val id: Long? = null,

        @Version
        @Column(name = "version")
        override val version: Int = 0,

        @Column(nullable = false)
        var naam: String,

        @Embedded
        var adres: Adres? = null,

        @ManyToOne(targetEntity = School::class)
        @JoinColumn(name = "hoort_bij_school_id", foreignKey = ForeignKey(name = "scl_scl_fk"))
        var hoortBij: School? = null,

        @JsonIgnore
        @OneToMany(mappedBy = "hoortBij", targetEntity = School::class, fetch = FetchType.LAZY)
        val bijbehorendeScholen: MutableList<School> = ArrayList(),

        @JsonIgnore
        @OneToMany(mappedBy = "school", targetEntity = Groep::class, fetch = FetchType.LAZY)
        val groepen: MutableList<Groep> = ArrayList(),

        @JsonIgnore
        @OneToMany(mappedBy = "school", targetEntity = Inschrijving::class, fetch = FetchType.LAZY)
        val inschrijvingen: MutableList<Inschrijving> = ArrayList()

) : DomainObject() {
    override val _type: DomainEntity get() = DomainEntity.SCHOOL

    override fun toString(): String {
        return "School(id=$id, naam='$naam', adres=$adres, hoortBij=${hoortBij?.id})"
    }
}
