package nl.vollo.kern.model;

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import org.apache.commons.lang3.builder.ToStringBuilder
import java.io.Serializable

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "_type", visible = true)
@JsonSubTypes(
        JsonSubTypes.Type(name = "LEERLING", value = Leerling::class),
        JsonSubTypes.Type(name = "GROEP", value = Groep::class),
        JsonSubTypes.Type(name = "GROEP_MEDEWERKER", value = GroepMedewerker::class),
        JsonSubTypes.Type(name = "GROEP_LEERLING", value = GroepLeerling::class),
        JsonSubTypes.Type(name = "SCHOOL", value = School::class),
        JsonSubTypes.Type(name = "MEDEWERKER", value = Medewerker::class),
        JsonSubTypes.Type(name = "INSCHRIJVING", value = Inschrijving::class),
        JsonSubTypes.Type(name = "GEBRUIKER", value = Gebruiker::class)
)
abstract class DomainObject : Serializable, Comparable<DomainObject> {

    abstract val _type: DomainEntity

    abstract val id: Long?

    abstract val version: Int

    override fun compareTo(other: DomainObject) = id!!.compareTo(other.id!!)

    override fun equals(other: Any?) = when {
        this === other -> true
        other == null || javaClass != other.javaClass -> false
        else -> if (id != null) id == (other as DomainObject).id else (other as DomainObject).id == null
    }

    override fun hashCode() = if (id != null) id!!.hashCode() else 0

    override fun toString() = "$javaClass[id=$id]"

}
