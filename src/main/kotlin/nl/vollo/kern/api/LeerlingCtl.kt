package nl.vollo.kern.api

import io.swagger.annotations.Api
import mu.KotlinLogging
import nl.vollo.events.EventService
import nl.vollo.events.kern.LeerlingOpgehaald
import nl.vollo.kern.model.Groep
import nl.vollo.kern.model.Leerling
import nl.vollo.kern.model.Ouder
import nl.vollo.kern.model.Score
import nl.vollo.kern.repository.GroepRepository
import nl.vollo.kern.repository.LeerlingRepository
import nl.vollo.kern.repository.OuderRepository
import nl.vollo.kern.repository.ScoreRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

private val log = KotlinLogging.logger {}

@Api(value = "Leerling")
@RestController()
@RequestMapping("/leerling")
class LeerlingCtl {

    @Autowired
    private lateinit var leerlingRepository: LeerlingRepository

    @Autowired
    private lateinit var scoreRepository: ScoreRepository

    @Autowired
    private lateinit var ouderRepository: OuderRepository

    @Autowired
    private lateinit var groepRepository: GroepRepository

    @Autowired
    private lateinit var eventService: EventService

    @GetMapping(produces = ["application/json"])
    fun listAll(): List<Leerling> = leerlingRepository.findAll()

    @PostMapping(consumes = ["application/json"])
    fun create(entity: Leerling): ResponseEntity<Leerling> {
        val saved = leerlingRepository.save(entity)
        return ResponseEntity(saved, HttpStatus.CREATED)
    }

    @DeleteMapping(value = ["/{id:[0-9][0-9]*}"])
    fun deleteById(@PathVariable("id") id: Long): ResponseEntity<Any> {
        return leerlingRepository
                .findById(id)
                .map {
                    leerlingRepository.deleteById(id)
                    ResponseEntity<Any>(HttpStatus.NO_CONTENT)
                }
                .orElse(ResponseEntity(HttpStatus.NOT_FOUND))
    }

    @GetMapping(value = ["/{id:[0-9][0-9]*}"], produces = ["application/json"])
    fun findById(@PathVariable("id") id: Long): ResponseEntity<Leerling> {
        return leerlingRepository
                .findById(id)
                .map { eventService.send(LeerlingOpgehaald().apply { this.id = it.id; this.geslacht = it.geslacht.toString() }); it }
                .map { ResponseEntity(it, HttpStatus.OK) }
                .orElse(ResponseEntity(HttpStatus.NOT_FOUND))
    }

    @GetMapping(value = ["/{id:[0-9][0-9]*}/scores"], produces = ["application/json"])
    fun findScoresById(@PathVariable("id") id: Long): ResponseEntity<List<Score>> {
        return ResponseEntity(scoreRepository.findAllByLeerlingId(id), HttpStatus.OK)
    }

    @GetMapping(value = ["/{id:[0-9][0-9]*}/ouders"], produces = ["application/json"])
    fun findOudersById(@PathVariable("id") id: Long): ResponseEntity<List<Ouder>> {
        return ResponseEntity(ouderRepository.findAllByLeerlingId(id), HttpStatus.OK)
    }

    @GetMapping(value = ["/{id:[0-9][0-9]*}/groepen"], produces = ["application/json"])
    fun findGroepenById(@PathVariable("id") id: Long): ResponseEntity<List<Groep>> {
        return ResponseEntity(groepRepository.findAllByLeerlingId(id), HttpStatus.OK)
    }

    @PutMapping(value = ["/{id:[0-9][0-9]*}"], consumes = ["application/json"])
    fun update(@PathVariable("id") id: Long, entity: Leerling): ResponseEntity<Any> {
        if (id != entity.id) {
            return ResponseEntity(HttpStatus.CONFLICT)
        }
        return leerlingRepository
                .findById(id)
                .map {
                    leerlingRepository.save(entity)
                    ResponseEntity<Any>(HttpStatus.NO_CONTENT)
                }
                .orElse(ResponseEntity(HttpStatus.NOT_FOUND))
    }

}
