package nl.vollo.kern.repository

import nl.vollo.kern.model.Gebruiker
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface GebruikerRepository : JpaRepository<Gebruiker, Long> {
    fun findByGebruikersnaam(gebruikersnaam: String): Optional<Gebruiker>
}
