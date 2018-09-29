package nl.vollo.kern.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import nl.vollo.kern.model.*;
import nl.vollo.kern.repository.GroepRepository;
import nl.vollo.kern.repository.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.Produces;
import java.util.*;

@Api(value = "MijnGroepen")
@RestController
@RequestMapping("/mijn-groepen")
public class MijnGroepenCtl {

    @Autowired
    private GroepRepository groepRepository;

    @Autowired
    private ScoreRepository scoreRepository;

    @ApiOperation(value = "Haal de groepen van de ingelogde medewerker op.")
    @GetMapping(produces = "application/json")
    @PreAuthorize("hasRole('GEBRUIKER')")
    public List<Groep> getMijnGroepen(@AuthenticationPrincipal final Gebruiker gebruiker) {
        return groepRepository.findByMedewerker(gebruiker.getMedewerker(), new Date());
    }

    @ApiOperation(value = "Haal de leerlingen van een groep van de ingelogde medewerker op.")
    @GetMapping(value = "/{id}/leerlingen", produces = "application/json")
    @Produces("application/json")
    @PreAuthorize("hasRole('GEBRUIKER')")
    public GroepView getGroepLeerlingen(@ApiParam("ID van een groep") @PathVariable("id") Long id) {
        GroepView groepView = new GroepView();
        groepRepository.getGroepLeerlingen(id, new Date()).forEach(leerling -> {
            List<Score> scores = scoreRepository.findAllByLeerling(leerling);
            groepView.addLeerling(leerling, scores);
        });
        return groepView;
    }

    public static class GroepView {
        private List<LeerlingView> leerlingen = new ArrayList<>();
        private Set<Toetsafname> toetsen = new TreeSet<>();

        void addLeerling(Leerling leerling, List<Score> scores) {
            LeerlingView leerlingView = new LeerlingView(leerling);
            scores.forEach(score -> {
                toetsen.add(score.getToetsafname());
                leerlingView.addScore(score);
            });
            leerlingen.add(leerlingView);
        }

        public List<LeerlingView> getLeerlingen() {
            return leerlingen;
        }

        public Set<Toetsafname> getToetsen() {
            return toetsen;
        }
    }

    @JsonIgnoreProperties({"leerling", "adres", "inschrijvingen"})
    public static class LeerlingView {
        private Leerling leerling;

        private Map<Long, Object> scores = new TreeMap<>();

        public LeerlingView(Leerling leerling) {
            this.leerling = leerling;
        }

        void addScore(Score score) {
            this.scores.put(score.getToetsafname().getId(), score.getCijferScore());
        }

        public Map<Long, Object> getScores() {
            return scores;
        }

        public Long getId() {
            return leerling.getId();
        }

        public String getVoornamen() {
            return leerling.getVoornamen();
        }

        public String getRoepnaam() {
            return leerling.getRoepnaam();
        }

        public String getTussenvoegsel() {
            return leerling.getTussenvoegsel();
        }

        public String getAchternaam() {
            return leerling.getAchternaam();
        }

        public Date getGeboortedatum() {
            return leerling.getGeboortedatum();
        }

        public Geslacht getGeslacht() {
            return leerling.getGeslacht();
        }

        public Adres getAdres() {
            return leerling.getAdres();
        }

        public List<Inschrijving> getInschrijvingen() {
            return leerling.getInschrijvingen();
        }
    }
}
