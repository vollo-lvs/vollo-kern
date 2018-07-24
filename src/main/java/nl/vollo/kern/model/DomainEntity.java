package nl.vollo.kern.model;

import lombok.Getter;

import java.util.List;

import static java.util.Arrays.asList;

@Getter
public enum DomainEntity {
    gebruiker(Gebruiker.class),
    groep(Groep.class),
    groepLeerling(GroepLeerling.class),
    groepMedewerker(GroepMedewerker.class),
    inschrijving(Inschrijving.class),
    leerling(Leerling.class),
    medewerker(Medewerker.class),
    ouder(Ouder.class),
    ouderLeerling(OuderLeerling.class),
    school(School.class),
    score(Score.class),
    toets(Toets.class);

    static {
        leerling.setAllowedEntities(DomainEntity.groepLeerling, DomainEntity.inschrijving)
                .setAllowedFields("voornamen", "achternaam");
        medewerker.setAllowedEntities(DomainEntity.groepMedewerker);
    }

    private Class<? extends DomainObject> domainClass;
    private List<DomainEntity> allowedEntities;
    private List<String> allowedFields;

    <T extends DomainObject> DomainEntity(Class<T> domainClass) {
        this.domainClass = domainClass;
    }

    public boolean isFieldAllowed(String field) {
        return allowedFields.contains(field);
    }

    public boolean isEntityAllowed(DomainEntity entity) {
        return allowedEntities.contains(entity);
    }

    private DomainEntity setAllowedEntities(DomainEntity... allowedEntities) {
        this.allowedEntities = asList(allowedEntities);
        return this;
    }

    private DomainEntity setAllowedFields(String... allowedFields) {
        this.allowedFields = asList(allowedFields);
        return this;
    }

}
