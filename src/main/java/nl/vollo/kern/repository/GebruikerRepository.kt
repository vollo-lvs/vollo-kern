package nl.vollo.kern.repository

import nl.vollo.kern.model.Gebruiker
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface GebruikerRepository : JpaRepository<Gebruiker, Long> {
    fun findByGebruikersnaam(gebruikersnaam: String): Optional<Gebruiker>
}
