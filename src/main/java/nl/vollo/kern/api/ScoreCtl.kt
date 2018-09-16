package nl.vollo.kern.api;

import io.swagger.annotations.Api
import nl.vollo.kern.model.Score
import nl.vollo.kern.repository.LeerlingRepository
import nl.vollo.kern.repository.ScoreRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Api(value = "Score")
@RestController
@RequestMapping("/score")
class ScoreCtl {

    @Autowired
    private lateinit var leerlingRepository: LeerlingRepository

    @Autowired
    private lateinit var scoreRepository: ScoreRepository

    @GetMapping()
    fun getScores(@RequestParam(name = "leerlingId") leerlingId: Long): ResponseEntity<List<Score>> {
        return leerlingRepository.findById(leerlingId)
                .map { leerling -> scoreRepository.findAllByLeerling(leerling) }
                .map { ResponseEntity.ok(it) }
                .orElse(ResponseEntity.notFound().build())
    }
}
