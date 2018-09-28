package nl.vollo.kern.model;

import lombok.Getter;

import java.util.List;

import static java.util.Arrays.asList;

@Getter
public enum DomainEntity {
    GEBRUIKER(Gebruiker.class),
    GROEP(Groep.class),
    GROEP_LEERLING(GroepLeerling.class),
    GROEP_MEDEWERKER(GroepMedewerker.class),
    INSCHRIJVING(Inschrijving.class),
    LEERLING(Leerling.class),
    MEDEWERKER(Medewerker.class),
    OUDER(Ouder.class),
    OUDER_LEERLING(OuderLeerling.class),
    SCHOOL(School.class),
    SCORE(Score.class),
    TOETS(Toets.class),
    TOETSAFNAME(Toetsafname.class);

    static {
        LEERLING.setAllowedEntities(DomainEntity.GROEP_LEERLING, DomainEntity.INSCHRIJVING)
                .setAllowedFields("voornamen", "achternaam");
        MEDEWERKER.setAllowedEntities(DomainEntity.GROEP_MEDEWERKER);
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
