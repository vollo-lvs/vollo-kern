package nl.vollo.kern.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "_type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Leerling.class, name = "leerling"),
        @JsonSubTypes.Type(value = Groep.class, name = "groep"),
        @JsonSubTypes.Type(value = GroepMedewerker.class, name = "groepMedewerker"),
        @JsonSubTypes.Type(value = GroepLeerling.class, name = "groepLeerling"),
        @JsonSubTypes.Type(value = School.class, name = "school"),
        @JsonSubTypes.Type(value = Medewerker.class, name = "medewerker"),
        @JsonSubTypes.Type(value = Inschrijving.class, name = "inschrijving"),
})
public abstract class DomainObject implements Serializable {
    @JsonProperty
    private DomainEntity _type;

    DomainObject(DomainEntity domainEntity) {
        this._type = domainEntity;
    }

    public abstract Long getId();

    public abstract int getVersion();

    public abstract void setVersion(int version);
}
