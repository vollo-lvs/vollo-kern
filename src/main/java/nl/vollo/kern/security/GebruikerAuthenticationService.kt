package nl.vollo.kern.security

import nl.vollo.kern.model.Gebruiker
import java.util.*

interface GebruikerAuthenticationService {

    /**
     * Logs in with the given `gebruikersnaam` and `wachtwoord`.
     *
     * @param gebruikersnaam
     * @param wachtwoord
     * @return an [Optional] of a user when login succeeds
     */
    fun login(gebruikersnaam: String, wachtwoord: String): Optional<String>

    /**
     * Finds a user by its dao-key.
     *
     * @param token user dao key
     * @return
     */
    fun findByToken(token: String): Optional<Gebruiker>

    /**
     * Logs out the given input `user`.
     *
     * @param gebruiker the user to logout
     */
    fun logout(gebruiker: Gebruiker)
}