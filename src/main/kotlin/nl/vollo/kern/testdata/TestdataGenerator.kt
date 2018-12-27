package nl.vollo.kern.testdata

import mu.KotlinLogging
import nl.vollo.kern.model.*
import nl.vollo.kern.model.enums.Geslacht
import nl.vollo.kern.model.enums.SoortScore
import nl.vollo.kern.repository.*
import org.apache.commons.io.IOUtils
import org.apache.commons.lang3.ArrayUtils
import org.apache.commons.lang3.time.DateUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.io.IOException
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.PersistenceException

private val log = KotlinLogging.logger {}

@Component
class TestdataGenerator : CommandLineRunner {
    private var aantalScholen: Int = 0
    private var aantalGroepen: Int = 0
    private var aantalLeerlingenMin: Int = 0
    private var aantalLeerlingenMax: Int = 0

    private val datumBeginSchooljaar = calcBeginSchooljaar()

    lateinit var random: Random

    private lateinit var schoolnamen: List<String>
    private lateinit var straatnamen: List<String>
    private lateinit var plaatsnamen: List<String>
    private lateinit var achternamen: List<String>
    private lateinit var meisjesnamen: List<String>
    private lateinit var jongensnamen: List<String>
    private lateinit var tussenvoegsels: List<String>

    private lateinit var toetsafnames: List<Toetsafname>

    @PersistenceContext
    lateinit var em: EntityManager

    @Autowired
    lateinit var schoolRepository: SchoolRepository

    @Autowired
    lateinit var groepRepository: GroepRepository

    @Autowired
    lateinit var groepLeerlingRepository: GroepLeerlingRepository

    @Autowired
    lateinit var leerlingRepository: LeerlingRepository

    @Autowired
    lateinit var inschrijvingRepository: InschrijvingRepository

    @Autowired
    lateinit var medewerkerRepository: MedewerkerRepository

    @Autowired
    lateinit var gebruikerRepository: GebruikerRepository

    @Autowired
    lateinit var groepMedewerkerRepository: GroepMedewerkerRepository

    @Autowired
    lateinit var scoreRepository: ScoreRepository

    @Autowired
    lateinit var toetsRepository: ToetsRepository

    @Autowired
    lateinit var toetsafnameRepository: ToetsafnameRepository

    @Autowired
    lateinit var notitieRepository: NotitieRepository

    @Autowired
    lateinit var ouderRepository: OuderRepository

    @Autowired
    lateinit var ouderLeerlingRepository: OuderLeerlingRepository

    @Throws(Exception::class)
    override fun run(vararg args: String) {
        if (ArrayUtils.contains(args, "--genereer-testdata")) {
            log.info("Testdata genereren")
            genereren()
        }
    }

    fun genereren(
            aantalScholen: Int = 3,
            aantalGroepen: Int = 8,
            aantalLeerlingenMin: Int = 15,
            aantalLeerlingenMax: Int = 32
    ) {
        this.aantalScholen = aantalScholen
        this.aantalGroepen = aantalGroepen
        this.aantalLeerlingenMin = aantalLeerlingenMin
        this.aantalLeerlingenMax = aantalLeerlingenMax
        try {
            // TODO betere oplossing voor deze hack
            em.createNativeQuery("alter sequence vollo_seq restart").resultList
        } catch (e: PersistenceException) {
        }

        truncateTables()
        random = Random(1L)
        genererenToetsen()
        schoolnamen = inlezenTestdata("schoolnamen")
        straatnamen = inlezenTestdata("straatnamen")
        plaatsnamen = inlezenTestdata("plaatsnamen")
        achternamen = inlezenTestdata("achternamen")
        meisjesnamen = inlezenTestdata("meisjesnamen")
        jongensnamen = inlezenTestdata("jongensnamen")
        tussenvoegsels = inlezenTestdata("tussenvoegsels")
        genererenScholen(null, aantalScholen)
        genererenOuders()
    }

    private fun genererenScholen(hoortBij: School?, aantalScholen: Int) {
        for (i in 0 until aantalScholen) {
            val school = genererenSchool(hoortBij)
            if (hoortBij == null) {
                log.info { "Bijbehorende scholen ${school.bijbehorendeScholen}" }
                val medewerker = genererenMedewerker(i)
                koppelenMedewerkerAanSchool(medewerker, school)
            }
        }
    }

    private fun genererenSchool(hoortBij: School?): School {
        val schoolPlaatsnamen = random(plaatsnamen, randomInt(1, 4))
        val s = School(
                naam = random(schoolnamen),
                adres = randomAdres(schoolPlaatsnamen),
                hoortBij = hoortBij)
        schoolRepository.save(s)
        log.info { "Gegenereerd school ${s.id}" }

        hoortBij?.bijbehorendeScholen?.add(s)
        if (hoortBij == null && kans(0.5)) {
            genererenScholen(s, 2)
        } else {
            genererenGroepen(s, schoolPlaatsnamen)
        }
        return s
    }

    private fun genererenGroepen(school: School, schoolPlaatsnamen: List<String>) {
        for (i in 1..aantalGroepen) {
            val g = Groep(
                    school = school,
                    niveau = i,
                    naam = i.toString()
            )
            groepRepository.save(g)
            log.info { "Gegenereerd groep ${g.id}" }
            school.groepen.add(g)
            genererenGroepLeerlingen(g, schoolPlaatsnamen)
        }
    }

    private fun genererenGroepLeerlingen(g: Groep, schoolPlaatsnamen: List<String>): List<GroepLeerling> {
        val leerlingen = IntRange(0, randomInt(aantalLeerlingenMin, aantalLeerlingenMax)).map {
            genererenLeerling(g, schoolPlaatsnamen)
        }
        leerlingRepository.saveAll(leerlingen)
        val groepLeerlingen = leerlingen.map {
            GroepLeerling(
                    groep = g,
                    datumBegin = datumBeginSchooljaar,
                    leerling = it
            )
        }
        groepLeerlingRepository.saveAll(groepLeerlingen)
        g.groepLeerlingen.addAll(groepLeerlingen)
        leerlingen.forEach {
            koppelLeerlingAanVoorgaandeGroepen(g, it)
        }
        val inschrijvingen = leerlingen.map {
            genererenInschrijving(it, g.school, g)
        }
        inschrijvingRepository.saveAll(inschrijvingen)
        inschrijvingen.forEach {
            it.leerling.inschrijvingen.add(it)
            g.school.inschrijvingen.add(it)
        }
        leerlingen.forEach {
            genererenScores(it)
        }

        return groepLeerlingen
    }

    private fun koppelLeerlingAanVoorgaandeGroepen(laatsteGroep: Groep, leerling: Leerling) {
        val groepLeerlingen: Sequence<GroepLeerling> = laatsteGroep.school.groepen
                .asSequence()
                .filter { groep -> groep.niveau < laatsteGroep.niveau }
                .map { groep ->
                    val datumBegin = DateUtils.addYears(datumBeginSchooljaar, groep.niveau - laatsteGroep.niveau)
                    val datumEinde = DateUtils.addDays(DateUtils.addYears(datumBegin, 1), -1)
                    GroepLeerling(
                            groep = groep,
                            datumBegin = datumBegin,
                            datumEinde = datumEinde,
                            leerling = leerling
                    )
                }

        groepLeerlingRepository.saveAll(groepLeerlingen.asIterable())

        groepLeerlingen
                .forEach { gl ->
                    gl.groep.groepLeerlingen.add(gl)
                }
    }

    private fun genererenLeerling(groep: Groep, schoolPlaatsnamen: List<String>): Leerling {
        val geslacht = if (kans(0.45)) Geslacht.VROUW else if (kans(0.9)) Geslacht.MAN else Geslacht.OVERIG
        val geslachtVoorNaam = if (geslacht === Geslacht.OVERIG) {
            if (kans(0.5)) Geslacht.VROUW else Geslacht.MAN
        } else geslacht
        val roepnaam = if (geslachtVoorNaam === Geslacht.VROUW) random(meisjesnamen) else random(jongensnamen)
        val l = Leerling(
                achternaam = random(achternamen),
                adres = randomAdres(schoolPlaatsnamen),
                geboortedatum = randomGeboortedatum(groep.niveau),
                geslacht = geslacht,
                roepnaam = roepnaam,
                voornamen = if (geslachtVoorNaam === Geslacht.VROUW) randomVoornamen(roepnaam, meisjesnamen) else randomVoornamen(roepnaam, jongensnamen),
                tussenvoegsel = if (kans(0.2)) random(tussenvoegsels) else null
        )
        // TODO uitgeschreven leerlingen
        return l
    }

    private fun genererenScores(leerling: Leerling) {
        val scores = toetsafnames.map { toetsafname ->
            Score(
                    leerling = leerling,
                    toetsafname = toetsafname,
                    cijferScore = genererenScore(toetsafname.toets.soortScore)
            )
        }
        scoreRepository.saveAll(scores)
    }

    private fun genererenScore(soortScore: SoortScore): BigDecimal? {
        if (soortScore === SoortScore.CIJFER_1_10) {
            return BigDecimal(random.nextDouble() * 9 + 1).setScale(1, RoundingMode.HALF_UP)
        } else if (soortScore === SoortScore.SCORE_500) {
            return BigDecimal(randomInt(200, 800)).setScale(0, RoundingMode.UP)
        }
        return null
    }

    private fun genererenToetsen() {
        toetsafnames = createToetsafnames("Centraal taal", SoortScore.SCORE_500, "Centraal") +
                createToetsafnames("Centraal rekenen", SoortScore.SCORE_500, "Centraal") +
                createToetsafnames("Dictee", SoortScore.CIJFER_1_10, "School") +
                createToetsafnames("Hoofdrekenen", SoortScore.CIJFER_1_10, "School") +
                createToetsafnames("Grammatica", SoortScore.CIJFER_1_10, "School")
    }

    private fun createToetsafnames(omschrijving: String, soortScore: SoortScore, soort: String): List<Toetsafname> {
        val toets = Toets(
                omschrijving = omschrijving,
                soortScore = soortScore,
                soort = soort
        )
        toetsRepository.save(toets)
        return IntRange(1, randomInt(1, 5)).map {
            val toetsafname = Toetsafname(
                    toets = toets,
                    datum = DateUtils.addDays(
                            DateUtils.addMonths(datumBeginSchooljaar, randomInt(1, 6)),
                            randomInt(1, 27))
            )
            toetsafnameRepository.save(toetsafname)
            toetsafname
        }
    }

    private fun genererenInschrijving(leerling: Leerling, school: School, laatsteGroep: Groep): Inschrijving {
        val datum = DateUtils.addYears(datumBeginSchooljaar, 1 - laatsteGroep.niveau)
        return Inschrijving(
                datumInschrijving = datum,
                leerling = leerling,
                school = school)
    }

    private fun koppelenMedewerkerAanSchool(medewerker: Medewerker, school: School) {
        val d = Date(0L)
        school.groepen.asSequence()
                .map {
                    GroepMedewerker(
                            groep = it,
                            medewerker = medewerker,
                            datumBegin = d)
                }
                .forEach { gm ->
                    log.info { "Koppel medewerker ${gm.medewerker.id} aan groep ${gm.groep.id}" }
                    groepMedewerkerRepository.save(gm)
                }
        school.bijbehorendeScholen.forEach { s -> koppelenMedewerkerAanSchool(medewerker, s) }
    }

    private fun genererenMedewerker(i: Int): Medewerker {
        val m = Medewerker(
                voornaam = random(if (kans(0.5)) jongensnamen else meisjesnamen),
                tussenvoegsel = if (kans(0.2)) random(tussenvoegsels) else null,
                achternaam = random(achternamen)
        )
        medewerkerRepository.save(m)
        val g = Gebruiker(
                gebruikersnaam = "m$i",
                wachtwoord = "m$i",
                medewerker = m,
                rollen = "ROLE_GEBRUIKER,ROLE_MEDEWERKER")
        gebruikerRepository.save(g)
        return m
    }

    private fun randomOuder(geslacht: Geslacht, leerling: Leerling) = Ouder(
            voornaam = random(if (geslacht == Geslacht.MAN) jongensnamen else meisjesnamen),
            tussenvoegsel = leerling.tussenvoegsel,
            achternaam = leerling.achternaam,
            geboortedatum = randomGeboortedatumOuder(),
            geslacht = geslacht,
            adres = leerling.adres)

    private fun genererenOuders() {
        log.info { "${Date()} Genereren ouders" }
        val vaders = hashMapOf<Leerling, Ouder>()
        val moeders = hashMapOf<Leerling, Ouder>()
        val leerlingen = leerlingRepository.findAll()
        leerlingen.filter { kans(0.5) }
                .forEach { leerling: Leerling ->
                    val vader = randomOuder(Geslacht.MAN, leerling)
                    val moeder = randomOuder(Geslacht.VROUW, leerling)
                    vaders[leerling] = vader
                    moeders[leerling] = moeder
                }
        var leerlingenMetOuders = vaders.keys.toList()
        leerlingen.filter { !vaders.containsKey(it) && !moeders.containsKey(it) }
                .filter { kans(0.5) }
                .forEach { leerling ->
                    val sibling = leerlingenMetOuders[randomInt(0, leerlingenMetOuders.size - 1)]
                    vaders[leerling] = vaders[sibling]!!
                    moeders[leerling] = moeders[sibling]!!
                }
        leerlingenMetOuders = vaders.keys.toList()
        leerlingen.filter { !vaders.containsKey(it) && !moeders.containsKey(it) }
                .filter { kans(0.5) }
                .forEach { leerling ->
                    val vader = randomOuder(Geslacht.MAN, leerling)
                    vaders[leerling] = vader
                }
        leerlingenMetOuders = vaders.keys.toList()
        leerlingen.filter { !vaders.containsKey(it) && !moeders.containsKey(it) }
                .forEach { leerling ->
                    val moeder = randomOuder(Geslacht.VROUW, leerling)
                    moeders[leerling] = moeder
                }

        ouderRepository.saveAll(vaders.values)
        ouderRepository.saveAll(moeders.values)
        ouderLeerlingRepository.saveAll(moeders.map { entry ->
            OuderLeerling(ouder = entry.value, leerling = entry.key)
        })
        ouderLeerlingRepository.saveAll(vaders.map { entry ->
            OuderLeerling(ouder = entry.value, leerling = entry.key)
        })
        log.info { "${Date()} Ouders gegenereerd" }
    }

    private fun randomGeboortedatum(niveau: Int): Date {
        return DateUtils.addDays(
                DateUtils.addMonths(
                        DateUtils.addYears(Date(), -3 - niveau),
                        -randomInt(0, 10)),
                -randomInt(0, 27)
        )
    }

    private fun randomGeboortedatumOuder(): Date {
        return DateUtils.addDays(
                DateUtils.addMonths(
                        DateUtils.addYears(Date(), -randomInt(20, 40)),
                        -randomInt(0, 10)),
                -randomInt(0, 27)
        )
    }

    private fun randomVoornamen(roepnaam: String, namen: List<String>): String {
        val s = StringBuilder(roepnaam)
        for (i in 0 until randomInt(0, 4)) {
            s.append(" ")
            s.append(random(namen))
        }
        return s.toString()
    }

    private fun randomAdres(plaatsnamenSelectie: List<String> = plaatsnamen): Adres {
        return Adres(
                straat = random(straatnamen),
                huisnummer = randomInt(1, 200).toString(),
                toevoeging = if (kans(0.2)) randomToevoeging() else null,
                postcode = randomPostcode(),
                plaats = random(plaatsnamenSelectie),
                land = "Nederland")
    }

    private fun kans(k: Double): Boolean {
        return random.nextDouble() < k
    }

    private fun randomToevoeging(): String {
        return if (kans(0.33))
            randomUppercase().toString()
        else
            if (kans(0.5))
                randomLowercase().toString()
            else
                randomInt(1, 100).toString()
    }

    private fun randomPostcode(): String {
        return String.format("%d%d%d%d%c%c",
                randomInt(1, 9),
                randomInt(0, 9),
                randomInt(0, 9),
                randomInt(0, 9),
                randomUppercase(),
                randomUppercase()
        )
    }

    private fun randomUppercase(): Char {
        return randomInt(65, 90).toChar()
    }

    private fun randomLowercase(): Char {
        return randomInt(97, 122).toChar()
    }

    private fun randomInt(vanaf: Int, totEnMet: Int): Int {
        return random.nextInt(totEnMet - vanaf) + vanaf
    }

    private fun random(items: List<String>): String {
        return items[random.nextInt(items.size)]
    }

    private fun random(items: List<String>, aantal: Int): List<String> {
        val l = ArrayList<String>(aantal)
        while (l.size < aantal) {
            val item = random(items)
            if (!l.contains(item)) {
                l.add(item)
            }
        }
        return l
    }

    private fun inlezenTestdata(bestandsnaam: String): List<String> {
        try {
            val lijstStr = IOUtils.resourceToString("/testdata/$bestandsnaam.txt", null)
            return lijstStr.split("\\n".toRegex())
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    private fun calcBeginSchooljaar(): Date {
        var d = DateUtils.setDays(
                DateUtils.setMonths(Date(), Calendar.SEPTEMBER),
                1)
        if (d.after(Date())) {
            d = DateUtils.addYears(d, -1)
        }
        return d
    }

    private fun truncateTables() {
        ouderLeerlingRepository.deleteAllInBatch()
        ouderRepository.deleteAllInBatch()
        notitieRepository.deleteAllInBatch()
        scoreRepository.deleteAllInBatch()
        toetsafnameRepository.deleteAllInBatch()
        toetsRepository.deleteAllInBatch()
        groepLeerlingRepository.deleteAllInBatch()
        groepMedewerkerRepository.deleteAllInBatch()
        gebruikerRepository.deleteAllInBatch()
        medewerkerRepository.deleteAllInBatch()
        inschrijvingRepository.deleteAllInBatch()
        leerlingRepository.deleteAllInBatch()
        groepRepository.deleteAllInBatch()
        schoolRepository.deleteAllInBatch()
    }

}
