package nl.vollo.kern.security

import com.google.common.collect.ImmutableMap
import nl.vollo.kern.model.Gebruiker
import nl.vollo.kern.repository.GebruikerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
internal class TokenAuthenticationService : GebruikerAuthenticationService {

    @Autowired
    private lateinit var tokens: JWTTokenService

    @Autowired
    private lateinit var gebruikers: GebruikerRepository

    override fun login(gebruikersnaam: String, wachtwoord: String): Optional<String> {
        return gebruikers
                .findByGebruikersnaam(gebruikersnaam)
                .filter { gebruiker -> wachtwoord == gebruiker.password }
                .map { tokens.expiring(ImmutableMap.of("gebruikersnaam", gebruikersnaam)) }
    }

    override fun findByToken(token: String): Optional<Gebruiker> {
        // TODO Kan null opleveren?
        return Optional
                .of(tokens.verify(token))
                .map { map -> map["gebruikersnaam"] }
                .flatMap { gebruikers.findByGebruikersnaam(it!!) }
    }

    override fun logout(gebruiker: Gebruiker) {
        // Nothing to do
    }
}
