package nl.vollo.kern.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.extern.log4j.Log4j2;
import nl.vollo.kern.annotation.DomainType;

import java.io.Serializable;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "_type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Leerling.class, name = "LEERLING"),
        @JsonSubTypes.Type(value = Groep.class, name = "GROEP"),
        @JsonSubTypes.Type(value = GroepMedewerker.class, name = "GROEP_MEDEWERKER"),
        @JsonSubTypes.Type(value = GroepLeerling.class, name = "GROEP_LEERLING"),
        @JsonSubTypes.Type(value = School.class, name = "SCHOOL"),
        @JsonSubTypes.Type(value = Medewerker.class, name = "MEDEWERKER"),
        @JsonSubTypes.Type(value = Inschrijving.class, name = "INSCHRIJVING"),
        @JsonSubTypes.Type(value = Gebruiker.class, name = "GEBRUIKER")
})
@Log4j2
public abstract class DomainObject implements Serializable, Comparable<DomainObject> {
    static final long serialVersionUID = -1L;

    @JsonProperty
    private DomainEntity _type;

    DomainObject() {
        assert this.getClass().isAnnotationPresent(DomainType.class) : "DomainObject moet annotatie DomainType hebben";

        this._type = this.getClass().getAnnotation(DomainType.class).value();
    }

    public abstract Long getId();

    public abstract int getVersion();

    public abstract void setVersion(int version);

    public int compareTo(DomainObject other) {
        return getId().compareTo(other.getId());
    }
}
