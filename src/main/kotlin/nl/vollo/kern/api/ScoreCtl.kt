package nl.vollo.kern.api;

import io.swagger.annotations.Api
import nl.vollo.kern.api.view.ScoreView
import nl.vollo.kern.model.Gebruiker
import nl.vollo.kern.repository.LeerlingRepository
import nl.vollo.kern.repository.ScoreRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@Api(value = "Score")
@RestController
@RequestMapping("/score")
@PreAuthorize("hasRole('MEDEWERKER')")
class ScoreCtl {

    @Autowired
    private lateinit var leerlingRepository: LeerlingRepository

    @Autowired
    private lateinit var scoreRepository: ScoreRepository

    @GetMapping()
    fun getScores(
            @AuthenticationPrincipal gebruiker: Gebruiker): ResponseEntity<List<ScoreView>> {
        val datumBegin = LocalDate.now()
                .withMonth(9)
                .withDayOfMonth(1)
                .minusYears(if (LocalDate.now().monthValue < 9) 1 else 0)
        val datumEinde = datumBegin.plusYears(1).minusDays(1)
        return ResponseEntity.ok(
                scoreRepository.findAllByMedewerkerIdInPeriode(gebruiker.medewerker!!, datumBegin, datumEinde)
                        .map(::ScoreView)
        )
    }
}
