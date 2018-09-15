package nl.vollo.kern.model;

import kotlin.reflect.KClass

enum class DomainEntity(val domainClass: KClass<out DomainObject>) {
    gebruiker(Gebruiker::class),
    groep(Groep::class),
    groepLeerling(GroepLeerling::class),
    groepMedewerker(GroepMedewerker::class),
    inschrijving(Inschrijving::class),
    leerling(Leerling::class),
    medewerker(Medewerker::class),
    ouder(Ouder::class),
    ouderLeerling(OuderLeerling::class),
    school(School::class),
    score(Score::class),
    toets(Toets::class),
    toetsafname(Toetsafname::class)
}
