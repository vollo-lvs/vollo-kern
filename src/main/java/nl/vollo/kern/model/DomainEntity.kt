package nl.vollo.kern.model;

import kotlin.reflect.KClass

enum class DomainEntity(val domainClass: KClass<out DomainObject>) {
    GEBRUIKER(Gebruiker::class),
    GROEP(Groep::class),
    GROEP_LEERLING(GroepLeerling::class),
    GROEP_MEDEWERKER(GroepMedewerker::class),
    INSCHRIJVING(Inschrijving::class),
    LEERLING(Leerling::class),
    MEDEWERKER(Medewerker::class),
    OUDER(Ouder::class),
    OUDER_LEERLING(OuderLeerling::class),
    SCHOOL(School::class),
    SCORE(Score::class),
    TOETS(Toets::class),
    TOETSAFNAME(Toetsafname::class)
}
