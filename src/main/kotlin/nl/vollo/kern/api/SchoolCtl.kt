package nl.vollo.kern.api

import nl.vollo.kern.model.Gebruiker
import nl.vollo.kern.model.School
import nl.vollo.kern.repository.SchoolRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.HttpClientErrorException

@RestController
@RequestMapping("/school")
@PreAuthorize("hasRole('MEDEWERKER')")
class SchoolCtl {

    @Autowired
    private lateinit var schoolRepository: SchoolRepository

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getScholen(): List<School> =
        schoolRepository.findAll()

    @GetMapping("/{schoolId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getSchool(@PathVariable("schoolId") schoolId: Long): ResponseEntity<School> =
        schoolRepository.findById(schoolId)
            .map { ResponseEntity.ok(it) }
            .orElse(ResponseEntity.notFound().build())

    @PostMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE])
    fun opslaanSchool(
        @RequestBody gewijzigdeSchool: School,
        @AuthenticationPrincipal gebruiker: Gebruiker
    ): ResponseEntity<School> {
        gewijzigdeSchool.hoortBij = gewijzigdeSchool.hoortBij?.id?.let { id ->
            schoolRepository
                .findById(id)
                .orElseThrow { HttpClientErrorException(HttpStatus.NOT_FOUND) }
        }

        return if (gewijzigdeSchool.id == null) {
            ResponseEntity.ok(schoolRepository.save(gewijzigdeSchool))
        } else {
            schoolRepository.findById(gewijzigdeSchool.id)
                .map { school ->
                    school.naam = gewijzigdeSchool.naam
                    school.adres = gewijzigdeSchool.adres
                    ResponseEntity.ok(schoolRepository.save(school))
                }.orElse(ResponseEntity.notFound().build())
        }
    }

}