package nl.vollo.kern.api

import io.swagger.annotations.Api
import mu.KotlinLogging
import nl.vollo.kern.model.Gebruiker
import nl.vollo.kern.model.Notitie
import nl.vollo.kern.repository.LeerlingRepository
import nl.vollo.kern.repository.NotitieRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.*

private val log = KotlinLogging.logger {}

@Api(value = "Notitie")
@RestController()
@RequestMapping("/notitie")
@PreAuthorize("hasRole('MEDEWERKER')")
class NotitieCtl {

    @Autowired
    private lateinit var notitieRepository: NotitieRepository

    @Autowired
    private lateinit var leerlingRepository: LeerlingRepository

    @GetMapping(path = ["/leerling/{leerlingId}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getLeerlingNotities(@PathVariable(name = "leerlingId") leerlingId: Long,
                            @AuthenticationPrincipal gebruiker: Gebruiker): ResponseEntity<List<Notitie>> {
        return if (gebruiker.medewerker != null) {
            ResponseEntity.ok(notitieRepository.findByLeerlingIdVoorMedewerker(leerlingId, gebruiker.medewerker))
        } else {
            ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        }
    }

    @PostMapping(
            path = ["/leerling/{leerlingId}"],
            consumes = [MediaType.APPLICATION_JSON_VALUE],
            produces = [MediaType.APPLICATION_JSON_VALUE])
    fun opslaanNotitie(
            @PathVariable(name = "leerlingId") leerlingId: Long,
            @RequestBody notitie: Notitie,
            @AuthenticationPrincipal gebruiker: Gebruiker): ResponseEntity<Notitie> {
        if (gebruiker.medewerker == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        }
        // TODO Check dat leerling in groep van medewerker zit?
        return leerlingRepository.findById(leerlingId)
                .map { leerling ->
                    notitie.leerling = leerling
                    notitie.medewerker = gebruiker.medewerker
                    notitie.datum = Date()
                    val saved = notitieRepository.save(notitie)
                    ResponseEntity.ok(saved)
                }
                .orElse(ResponseEntity.notFound().build())
    }

}