package nl.vollo.kern.api

import io.swagger.annotations.Api
import nl.vollo.kern.api.view.LeerlingHistorieRecord
import nl.vollo.kern.repository.GroepLeerlingRepository
import nl.vollo.kern.repository.GroepRepository
import nl.vollo.kern.repository.InschrijvingRepository
import nl.vollo.kern.repository.LeerlingRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@Api(value = "LeerlingHistorie")
@RestController()
class LeerlingHistorieCtl {

    @Autowired
    private lateinit var groepRepository: GroepRepository

    @Autowired
    private lateinit var groepLeerlingRepository: GroepLeerlingRepository

    @Autowired
    private lateinit var leerlingRepository: LeerlingRepository

    @Autowired
    private lateinit var inschrijvingRepository: InschrijvingRepository

    @GetMapping(path = ["/leerling/{leerlingId}/historie"], produces = ["application/json"])
    fun getLeerlingHistorie(@PathVariable("leerlingId") leerlingId: Long): ResponseEntity<List<LeerlingHistorieRecord>> {
        return leerlingRepository.findById(leerlingId)
                .map {
                    val groepen = groepLeerlingRepository.findAllByLeerlingId(leerlingId)
                    val inschrijvingen = inschrijvingRepository.findAllByLeerlingId(leerlingId)
                    ResponseEntity.ok(
                            groepen.map(::LeerlingHistorieRecord) +
                            inschrijvingen.map(::LeerlingHistorieRecord)
                    )
                }.orElse(ResponseEntity.notFound().build())
    }

}