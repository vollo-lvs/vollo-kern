package nl.vollo.kern.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import java.util.*

@Component
internal class TokenAuthenticationProvider : AbstractUserDetailsAuthenticationProvider() {

    @Autowired
    private lateinit var auth: GebruikerAuthenticationService

    override fun additionalAuthenticationChecks(d: UserDetails, auth: UsernamePasswordAuthenticationToken) {
        // Nothing to do
    }

    override fun retrieveUser(username: String, authentication: UsernamePasswordAuthenticationToken): UserDetails {
        val token = authentication.credentials
        return Optional
                .ofNullable(token)
                .map(Any::toString)
                .flatMap(auth::findByToken)
                .orElseThrow { UsernameNotFoundException("Cannot find user with authentication token=$token") }
    }
}