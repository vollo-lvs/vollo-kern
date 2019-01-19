package nl.vollo.kern.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import nl.vollo.kern.model.*
import nl.vollo.kern.repository.GroepRepository
import nl.vollo.kern.repository.ScoreRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.util.*

@Api(value = "MijnGroepen")
@RestController
@RequestMapping("/mijn-groepen")
class MijnGroepenCtl {

    @Autowired
    private lateinit var groepRepository: GroepRepository

    @Autowired
    private lateinit var scoreRepository: ScoreRepository

    @ApiOperation(value = "Haal de groepen van de ingelogde medewerker op.")
    @GetMapping(produces = ["application/json"])
    @PreAuthorize("hasRole('GEBRUIKER')")
    fun getMijnGroepen(@AuthenticationPrincipal gebruiker: Gebruiker): List<Groep> {
        return groepRepository.findByMedewerker(gebruiker.medewerker!!, LocalDate.now())
    }

    @ApiOperation(value = "Haal de leerlingen van een groep van de ingelogde medewerker op.")
    @GetMapping(value = ["/{id}/leerlingen"], produces = ["application/json"])
    @PreAuthorize("hasRole('GEBRUIKER')")
    fun getGroepLeerlingen(@ApiParam("ID van een groep") @PathVariable("id") id: Long): GroepView {
        val groepView = GroepView()
        groepRepository.getGroepLeerlingen(id, LocalDate.now()).forEach { leerling ->
            val scores = scoreRepository.findAllByLeerling(leerling)
            groepView.addLeerling(leerling, scores)
        }
        return groepView
    }

    data class GroepView(
        val leerlingen: MutableList<LeerlingView> = ArrayList(),
        val toetsen: MutableSet<Toetsafname> = TreeSet()
    ) {
        fun addLeerling(leerling: Leerling, scores: List<Score>) {
            val leerlingView = LeerlingView(leerling)
            scores.forEach { score ->
                toetsen.add(score.toetsafname)
                leerlingView.addScore(score)
            }
            leerlingen.add(leerlingView)
        }
    }

    @JsonIgnoreProperties("adres", "inschrijvingen")
    data class LeerlingView(
        val leerling: Leerling,
        val scores: MutableMap<Long, Any> = TreeMap()
    ) {
        fun addScore(score: Score) {
            this.scores[score.toetsafname.id!!] = score.cijferScore!!
        }
    }
}
