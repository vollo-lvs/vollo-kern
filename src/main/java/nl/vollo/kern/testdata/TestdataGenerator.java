package nl.vollo.kern.testdata;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;
import nl.vollo.kern.model.Adres;
import nl.vollo.kern.model.School;
import nl.vollo.kern.repository.SchoolRepository;

@Component
@Log4j2
public class TestdataGenerator implements CommandLineRunner {

    private long aantalScholen = 100L;

    private Random random;

    List<String> schoolnamen;
    List<String> straatnamen;
    List<String> plaatsnamen;

    @Autowired
    SchoolRepository schoolRepository;

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
        genererenScholen(null, aantalScholen);
    }

    private void genererenScholen(School hoortBij, long aantalScholen) {
        for (int i = 0; i < aantalScholen; i++) {
            genererenSchool(hoortBij);
        }
    }

    private void genererenSchool(School hoortBij) {
        School s = new School();
        s.setNaam(random(schoolnamen));
        s.setAdres(randomAdres());
        s.setHoortBij(hoortBij);
        schoolRepository.save(s);
        if (kans(0.1)) {
            genererenScholen(s, randomInt(1, 5));
        }
    }

    private Adres randomAdres() {
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

    private List<String> inlezenTestdata(String bestandsnaam) {
        try {
            String lijstStr = IOUtils.resourceToString("/testdata/" + bestandsnaam + ".txt", null);
            return Arrays.asList(lijstStr.split("\\n"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
