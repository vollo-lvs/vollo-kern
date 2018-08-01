package nl.vollo.kern.testdata;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;
import nl.vollo.kern.model.Adres;
import nl.vollo.kern.model.Geslacht;
import nl.vollo.kern.model.Groep;
import nl.vollo.kern.model.GroepLeerling;
import nl.vollo.kern.model.Leerling;
import nl.vollo.kern.model.School;
import nl.vollo.kern.repository.GroepLeerlingRepository;
import nl.vollo.kern.repository.GroepRepository;
import nl.vollo.kern.repository.LeerlingRepository;
import nl.vollo.kern.repository.SchoolRepository;

@Component
@Log4j2
public class TestdataGenerator implements CommandLineRunner {

    private long aantalScholen = 100L;
    private Date datumBeginSchooljaar = calcBeginSchooljaar();

    private Random random;

    List<String> schoolnamen;
    List<String> straatnamen;
    List<String> plaatsnamen;
    List<String> achternamen;
    List<String> meisjesnamen;
    List<String> jongensnamen;
    List<String> tussenvoegsels;

    @Autowired
    SchoolRepository schoolRepository;

    @Autowired
    GroepRepository groepRepository;

    @Autowired
    GroepLeerlingRepository groepLeerlingRepository;

    @Autowired
    LeerlingRepository leerlingRepository;

	@Override
	public void run(String... args) throws Exception {
		if (ArrayUtils.contains(args, "--genereer-testdata")) {
            log.info("Testdata genereren");
            genereren();
        }
	}

    public void genereren() {
        random = new Random(1L);
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
            genererenSchool(hoortBij);
        }
    }

    private void genererenSchool(School hoortBij) {
        List<String> schoolPlaatsnamen = random(plaatsnamen, randomInt(1, 4));
        School s = new School();
        s.setNaam(random(schoolnamen));
        s.setAdres(randomAdres(schoolPlaatsnamen));
        s.setHoortBij(hoortBij);
        schoolRepository.save(s);
        if (kans(0.1)) {
            genererenScholen(s, randomInt(1, 5));
        } else {
            genererenGroepen(s, schoolPlaatsnamen);
        }
    }

    private void genererenGroepen(School school, List<String> schoolPlaatsnamen) {
        for (int i = 1; i <= 8; i++) {
            Groep g = new Groep();
            g.setSchool(school);
            g.setNiveau(i);
            g.setNaam(String.valueOf(i));
            groepRepository.save(g);
            genererenGroepLeerlingen(g, schoolPlaatsnamen);
        }
    }

    private List<GroepLeerling> genererenGroepLeerlingen(Groep g, List<String> schoolPlaatsnamen) {
        List<GroepLeerling> groepLeerlingen = new ArrayList<>();
        for (int i = 0; i < randomInt(15, 32); i++) {
            GroepLeerling gl = new GroepLeerling();
            gl.setGroep(g);
            gl.setDatumBegin(datumBeginSchooljaar);
            gl.setLeerling(genererenLeerling(g, schoolPlaatsnamen));
            groepLeerlingRepository.save(gl);
            // TODO leerlingen koppelen aan voorgaande groepen
        }
		return groepLeerlingen;
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
        return l;
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
}
