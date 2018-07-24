package nl.vollo.kern.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.Value;
import lombok.experimental.Delegate;
import lombok.extern.log4j.Log4j2;
import nl.vollo.kern.model.Adres;
import nl.vollo.kern.model.Groep;
import nl.vollo.kern.model.Inschrijving;
import nl.vollo.kern.model.Leerling;
import nl.vollo.kern.model.Score;
import nl.vollo.kern.model.Toets;
import nl.vollo.kern.repository.GroepRepository;
import nl.vollo.kern.repository.ScoreRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.Produces;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Api(value = "MijnGroepen")
@RestController
@RequestMapping("/mijn-groepen")
@Log4j2
public class MijnGroepenCtl {

    @Autowired
    GroepRepository groepRepository;

    @Autowired
    ScoreRepository scoreRepository;

    @ApiOperation(value = "Haal de groepen van de ingelogde medewerker op.")
    @GetMapping(produces = "application/json")
    @PreAuthorize("hasRole('GEBRUIKER')")
    public List<Groep> getMijnGroepen(Principal principal) {
        System.out.println(principal.getName());
        SecurityContext context = SecurityContextHolder.getContext();
        System.out.println(context.getAuthentication().getAuthorities());
        return groepRepository.findAll();
    }

    @ApiOperation(value = "Haal de leerlingen van een groep van de ingelogde medewerker op.")
    @GetMapping(value = "/{id}/leerlingen", produces = "application/json")
    @Produces("application/json")
    @PreAuthorize("hasRole('GEBRUIKER')")
    public GroepView getGroepLeerlingen(@ApiParam("ID van een groep") @PathVariable("id") Long id) {
        GroepView groepView = new GroepView();
        groepRepository.getGroepLeerlingen(id).forEach(leerling -> {
            List<Score> scores = scoreRepository.findAllByLeerling(leerling);
            groepView.addLeerling(leerling, scores);
        });
        return groepView;
    }

    @Value
    public static class GroepView {
        private List<LeerlingView> leerlingen = new ArrayList<>();
        private Set<Toets> toetsen = new TreeSet<>();

        void addLeerling(Leerling leerling, List<Score> scores) {
            LeerlingView leerlingView = new LeerlingView(leerling);
            scores.forEach(score -> {
                toetsen.add(score.getToets());
                leerlingView.addScore(score);
            });
            leerlingen.add(leerlingView);
        }
    }

    @Value
    @JsonIgnoreProperties({"leerling", "adres", "inschrijvingen"})
    public static class LeerlingView {
        @Delegate
        private Leerling leerling;

        private Map<Long, Object> scores = new TreeMap<>();

        void addScore(Score score) {
            this.scores.put(score.getToets().getId(), score.getCijferScore());
        }
    }
}
