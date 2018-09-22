package nl.vollo.kern.model;

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
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
interface DomainObject : Serializable, Comparable<DomainObject> {

    val _type: DomainEntity

    val id: Long?

    val version: Int

    override fun compareTo(other: DomainObject) = id!!.compareTo(other.id!!)

}
