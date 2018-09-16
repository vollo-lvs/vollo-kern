package nl.vollo.kern.model;

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.io.Serializable

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "_type", visible = true)
@JsonSubTypes(
        JsonSubTypes.Type(name = "leerling", value = Leerling::class),
        JsonSubTypes.Type(name = "groep", value = Groep::class),
        JsonSubTypes.Type(name = "groepMedewerker", value = GroepMedewerker::class),
        JsonSubTypes.Type(name = "groepLeerling", value = GroepLeerling::class),
        JsonSubTypes.Type(name = "school", value = School::class),
        JsonSubTypes.Type(name = "medewerker", value = Medewerker::class),
        JsonSubTypes.Type(name = "inschrijving", value = Inschrijving::class),
        JsonSubTypes.Type(name = "gebruiker", value = Gebruiker::class)
)
open class DomainObject(
        @JsonProperty
        val _type: DomainEntity?,

        open val id: Long?,

        open val version: Int
) : Serializable, Comparable<DomainObject> {

    // Nodig voor Hibernate
    constructor() : this(null, null, 0) {}

    override fun compareTo(other: DomainObject) = id!!.compareTo(other.id!!)
}
