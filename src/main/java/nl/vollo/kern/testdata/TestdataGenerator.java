package nl.vollo.kern.testdata;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import nl.vollo.kern.model.*;
import nl.vollo.kern.repository.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Component
@Log4j2
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TestdataGenerator implements CommandLineRunner {

    private int aantalScholen = 3;
    private int aantalGroepen = 0;
    private int aantalLeerlingenMin = 0;
    private int aantalLeerlingenMax = 0;

    private Date datumBeginSchooljaar = calcBeginSchooljaar();

    private Random random;

    List<String> schoolnamen;
    List<String> straatnamen;
    List<String> plaatsnamen;
    List<String> achternamen;
    List<String> meisjesnamen;
    List<String> jongensnamen;
    List<String> tussenvoegsels;

    List<Toetsafname> toetsen;

    @PersistenceContext
    EntityManager em;

    @Autowired
    SchoolRepository schoolRepository;

    @Autowired
    GroepRepository groepRepository;

    @Autowired
    GroepLeerlingRepository groepLeerlingRepository;

    @Autowired
    LeerlingRepository leerlingRepository;

    @Autowired
    InschrijvingRepository inschrijvingRepository;

    @Autowired
    MedewerkerRepository medewerkerRepository;

    @Autowired
    GebruikerRepository gebruikerRepository;

    @Autowired
    GroepMedewerkerRepository groepMedewerkerRepository;

    @Autowired
    ScoreRepository scoreRepository;

    @Autowired
    ToetsRepository toetsRepository;

    @Autowired
    ToetsafnameRepository toetsafnameRepository;

	@Override
	public void run(String... args) throws Exception {
		if (ArrayUtils.contains(args, "--genereer-testdata")) {
            log.info("Testdata genereren");
            genereren();
        }
	}

    public void genereren() {
        genereren(3, 8, 15, 32);
    }

    public void genereren(int aantalScholen, int aantalGroepen,
                          int aantalLeerlingenMin, int aantalLeerlingenMax) {
        this.aantalScholen = aantalScholen;
        this.aantalGroepen = aantalGroepen;
        this.aantalLeerlingenMin = aantalLeerlingenMin;
        this.aantalLeerlingenMax = aantalLeerlingenMax;

        try {
            em.createNativeQuery("alter sequence vollo_seq restart").getResultList();
        } catch (PersistenceException e) {}
        truncateTables();
        random = new Random(1L);
        genererenToetsen();
        schoolnamen = inlezenTestdata("schoolnamen");
        straatnamen = inlezenTestdata("straatnamen");
        plaatsnamen = inlezenTestdata("plaatsnamen");
        achternamen = inlezenTestdata("achternamen");
        meisjesnamen = inlezenTestdata("meisjesnamen");
        jongensnamen = inlezenTestdata("jongensnamen");
        tussenvoegsels = inlezenTestdata("tussenvoegsels");
        genererenScholen(null, aantalScholen);
    }

    private void genererenScholen(School hoortBij, long aantalScholen) {
        for (int i = 0; i < aantalScholen; i++) {
            School school = genererenSchool(hoortBij);
            if (hoortBij == null) {
                log.info("Bijbehorende scholen {}", school.getBijbehorendeScholen());
                Medewerker medewerker = genererenMedewerker(i);
                koppelenMedewerkerAanSchool(medewerker, school);
            }
        }
    }

	private School genererenSchool(School hoortBij) {
        List<String> schoolPlaatsnamen = random(plaatsnamen, randomInt(1, 4));
        School s = new School();
        s.setNaam(random(schoolnamen));
        s.setAdres(randomAdres(schoolPlaatsnamen));
        s.setHoortBij(hoortBij);
        schoolRepository.save(s);
        log.info("Gegenereerd school {}", s.getId());

        if (hoortBij != null) {
            hoortBij.getBijbehorendeScholen().add(s);
        }
        if (hoortBij == null && kans(0.5)) {
            genererenScholen(s, 2);
        } else {
            genererenGroepen(s, schoolPlaatsnamen);
        }
        return s;
    }

    private void genererenGroepen(School school, List<String> schoolPlaatsnamen) {
        for (int i = 1; i <= aantalGroepen; i++) {
            Groep g = new Groep();
            g.setSchool(school);
            g.setNiveau(i);
            g.setNaam(String.valueOf(i));
            groepRepository.save(g);
            log.info("Gegenereerd groep {}", g.getId());
            school.getGroepen().add(g);
            genererenGroepLeerlingen(g, schoolPlaatsnamen);
        }
    }

    private List<GroepLeerling> genererenGroepLeerlingen(Groep g, List<String> schoolPlaatsnamen) {
        List<GroepLeerling> groepLeerlingen = new ArrayList<>();
        for (int i = 0; i < randomInt(aantalLeerlingenMin, aantalLeerlingenMax); i++) {
            Leerling leerling = genererenLeerling(g, schoolPlaatsnamen);
            GroepLeerling gl = new GroepLeerling();
            gl.setGroep(g);
            gl.setDatumBegin(datumBeginSchooljaar);
            gl.setLeerling(leerling);
            groepLeerlingRepository.save(gl);
            g.getGroepLeerlingen().add(gl);
            koppelLeerlingAanVoorgaandeGroepen(g, leerling);
        }
		return groepLeerlingen;
    }

    private void koppelLeerlingAanVoorgaandeGroepen(Groep laatsteGroep, Leerling leerling) {
        laatsteGroep.getSchool().getGroepen().stream()
                .filter(groep -> groep.getNiveau() < laatsteGroep.getNiveau())
                .map(groep -> {
                    Date datumBegin = DateUtils.addYears(datumBeginSchooljaar, groep.getNiveau() - laatsteGroep.getNiveau());
                    Date datumEinde = DateUtils.addDays(DateUtils.addYears(datumBegin, 1), -1);
                    GroepLeerling gl = new GroepLeerling();
                    gl.setGroep(groep);
                    gl.setDatumBegin(datumBegin);
                    gl.setDatumEinde(datumEinde);
                    gl.setLeerling(leerling);
                    return gl;
                })
                .forEach(gl -> {
                    groepLeerlingRepository.save(gl);
                    gl.getGroep().getGroepLeerlingen().add(gl);
                });
	}

	private Leerling genererenLeerling(Groep groep, List<String> schoolPlaatsnamen) {
        Leerling l = new Leerling();
        l.setAchternaam(random(achternamen));
        l.setAdres(randomAdres(schoolPlaatsnamen));
        l.setGeboortedatum(randomGeboortedatum(groep.getNiveau()));
        l.setGeslacht(kans(0.45) ? Geslacht.VROUW : (kans(0.9) ? Geslacht.MAN : Geslacht.OVERIG));
        Geslacht geslachtVoorNaam = l.getGeslacht() == Geslacht.OVERIG ? (kans(0.5) ? Geslacht.VROUW : Geslacht.MAN) : l.getGeslacht();
        if (geslachtVoorNaam == Geslacht.VROUW) {
            l.setRoepnaam(random(meisjesnamen));
            l.setVoornamen(randomVoornamen(l.getRoepnaam(), meisjesnamen));
        } else if (geslachtVoorNaam == Geslacht.MAN) {
            l.setRoepnaam(random(jongensnamen));
            l.setVoornamen(randomVoornamen(l.getRoepnaam(), jongensnamen));
        }
        if (kans(0.2)) {
            l.setTussenvoegsel(random(tussenvoegsels));
        }
        leerlingRepository.save(l);
        genererenInschrijving(l, groep.getSchool(), groep);
        genererenScores(l);
        // TODO uitgeschreven leerlingen
        return l;
    }

    private void genererenScores(Leerling leerling) {
	    toetsen.forEach(toetsafname -> {
            Score score = new Score();
            score.setLeerling(leerling);
            score.setToetsafname(toetsafname);
            score.setCijferScore(genererenScore(toetsafname.getToets().getSoortScore()));
            scoreRepository.save(score);
        });
    }

    private BigDecimal genererenScore(SoortScore soortScore) {
	    if (soortScore == SoortScore.CIJFER_1_10) {
	        return new BigDecimal(random.nextDouble() * 9 + 1).setScale(1, RoundingMode.HALF_UP);
        } else if (soortScore == SoortScore.SCORE_500) {
	        return new BigDecimal(randomInt(200, 800)).setScale(0, RoundingMode.UP);
        }
        return null;
    }

    private void genererenToetsen() {
        toetsen = Arrays.asList(
                createToets("Centraal taal", SoortScore.SCORE_500, "Centraal"),
                createToets("Centraal rekenen", SoortScore.SCORE_500, "Centraal"),
                createToets("Dictee", SoortScore.CIJFER_1_10, "School"),
                createToets("Hoofdrekenen", SoortScore.CIJFER_1_10, "School"),
                createToets("Grammatica", SoortScore.CIJFER_1_10, "School")
        );
    }

    private Toetsafname createToets(String omschrijving, SoortScore soortScore, String soort) {
        Toets toets = new Toets();
        toets.setOmschrijving(omschrijving);
        toets.setSoortScore(soortScore);
        toets.setSoort(soort);
        toetsRepository.save(toets);
        Toetsafname toetsafname = new Toetsafname();
        toetsafname.setToets(toets);
        toetsafname.setDatum(
                DateUtils.addDays(
                        DateUtils.addMonths(datumBeginSchooljaar, randomInt(1, 6)),
                        randomInt(1, 27)));
        toetsafnameRepository.save(toetsafname);
        return toetsafname;
    }

    private void genererenInschrijving(Leerling leerling, School school, Groep laatsteGroep) {
        Date datum = DateUtils.addYears(datumBeginSchooljaar, 1 - laatsteGroep.getNiveau());
        Inschrijving i = new Inschrijving();
        i.setDatumInschrijving(datum);
        i.setLeerling(leerling);
        i.setSchool(school);
        inschrijvingRepository.save(i);
        leerling.getInschrijvingen().add(i);
        school.getInschrijvingen().add(i);
    }

    private void koppelenMedewerkerAanSchool(Medewerker medewerker, School school) {
        final Date d = new Date(0L);
        school.getGroepen().stream()
                .map(g -> {
                    GroepMedewerker gm = new GroepMedewerker();
                    gm.setGroep(g);
                    gm.setMedewerker(medewerker);
                    gm.setDatumBegin(d);
                    return gm;
                })
                .forEach(gm -> {
                    log.info("Koppel medewerker {} aan groep {}", gm.getMedewerker().getId(), gm.getGroep().getId());
                    groepMedewerkerRepository.save(gm);
                });
        school.getBijbehorendeScholen().forEach(s -> koppelenMedewerkerAanSchool(medewerker, s));
	}

	private Medewerker genererenMedewerker(int i) {
        Medewerker m = new Medewerker();
        m.setVoornaam(random(kans(0.5) ? jongensnamen : meisjesnamen));
        if (kans(0.2)) {
            m.setTussenvoegsel(random(tussenvoegsels));
        }
        m.setAchternaam(random(achternamen));
        medewerkerRepository.save(m);
        Gebruiker g = new Gebruiker();
        g.setGebruikersnaam("m" + i);
        g.setWachtwoord("m" + i);
        g.setMedewerker(m);
        g.setRollen("ROLE_GEBRUIKER");
        gebruikerRepository.save(g);
		return m;
	}

    private Date randomGeboortedatum(int niveau) {
        return DateUtils.addDays(
                DateUtils.addMonths(
                        DateUtils.addYears(new Date(), -3 - niveau),
                        -randomInt(0, 10)),
                -randomInt(0, 27)
        );
    }

    private String randomVoornamen(String roepnaam, List<String> namen) {
        StringBuilder s = new StringBuilder(roepnaam);
        for (int i = 0; i < randomInt(0, 4); i++) {
            s.append(" ");
            s.append(random(namen));
        }
        return s.toString();
    }

    private Adres randomAdres() {
        return randomAdres(plaatsnamen);
    }

    private Adres randomAdres(List<String> plaatsnamen) {
        Adres a = new Adres();
        a.setStraat(random(straatnamen));
        a.setHuisnummer(String.valueOf(randomInt(1, 200)));
        if (kans(0.2)) {
            a.setToevoeging(randomToevoeging());
        }
        a.setPostcode(randomPostcode());
        a.setPlaats(random(plaatsnamen));
        a.setLand("Nederland");
        return a;
    }

    private boolean kans(double k) {
        return random.nextDouble() < k;
    }

    private String randomToevoeging() {
        return kans(0.33)
                ? String.valueOf(randomUppercase())
                : (kans(0.5)
                        ? String.valueOf(randomLowercase())
                        : String.valueOf(randomInt(1, 100)));
    }

    private String randomPostcode() {
        return String.format("%d%d%d%d%c%c",
                randomInt(1, 9),
                randomInt(0, 9),
                randomInt(0, 9),
                randomInt(0, 9),
                randomUppercase(),
                randomUppercase()
        );
    }

    private char randomUppercase() {
        return (char) randomInt(65, 90);
    }

    private char randomLowercase() {
        return (char) randomInt(97, 122);
    }

    private int randomInt(int vanaf, int totEnMet) {
        return random.nextInt(totEnMet - vanaf) + vanaf;
    }

    private String random(List<String> items) {
        return items.get(random.nextInt(items.size()));
    }

    private List<String> random(List<String> items, int aantal) {
        List<String> l = new ArrayList<>(aantal);
        while (l.size() < aantal) {
            String item = random(items);
            if (!l.contains(item)) {
                l.add(item);
            }
        }
        return l;
    }

    private List<String> inlezenTestdata(String bestandsnaam) {
        try {
            String lijstStr = IOUtils.resourceToString("/testdata/" + bestandsnaam + ".txt", null);
            return Arrays.asList(lijstStr.split("\\n"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Date calcBeginSchooljaar() {
        Date d = DateUtils.setDays(
                DateUtils.setMonths(new Date(), Calendar.SEPTEMBER),
                1);
        if (d.after(new Date())) {
            d = DateUtils.addYears(d, -1);
        }
        return d;
    }

    private void truncateTables() {
	    scoreRepository.deleteAllInBatch();
	    toetsafnameRepository.deleteAllInBatch();
	    toetsRepository.deleteAllInBatch();
        groepLeerlingRepository.deleteAllInBatch();
        groepMedewerkerRepository.deleteAllInBatch();
        gebruikerRepository.deleteAllInBatch();
        medewerkerRepository.deleteAllInBatch();
        inschrijvingRepository.deleteAllInBatch();
        leerlingRepository.deleteAllInBatch();
        groepRepository.deleteAllInBatch();
        schoolRepository.deleteAllInBatch();
    }

}
