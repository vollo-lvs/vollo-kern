package nl.vollo.kern.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import nl.vollo.kern.annotation.DomainType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.util.Objects;

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
public abstract class DomainObject implements Serializable, Comparable<DomainObject> {
    static final long serialVersionUID = -1L;

    @JsonProperty
    private DomainEntity _type;

    DomainObject() {
        assert this.getClass().isAnnotationPresent(DomainType.class) : "DomainObject moet annotatie DomainType hebben";

        this._type = this.getClass().getAnnotation(DomainType.class).value();
    }
    private Long id;
    public abstract Long getId();

    public abstract int getVersion();

    public abstract void setVersion(int version);

    public int compareTo(@NonNull DomainObject other) {
        return getId().compareTo(other.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DomainObject)) {
            return false;
        }
        return Objects.equals(getId(), ((DomainObject) o).getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("_type", _type)
                .append("id", id)
                .toString();
    }
}
