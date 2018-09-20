package nl.vollo.kern.api;

import nl.vollo.kern.model.Gebruiker
import nl.vollo.kern.security.GebruikerAuthenticationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/gebruiker")
class GebruikerCtl {
    @Autowired
    lateinit var authentication: GebruikerAuthenticationService

    @GetMapping("/ingelogd")
    fun getIngelogdeGebruiker(@AuthenticationPrincipal gebruiker: Gebruiker): Gebruiker {
        return gebruiker;
    }

    @PostMapping("/uitloggen")
    fun logout(@AuthenticationPrincipal gebruiker: Gebruiker) {
        authentication.logout(gebruiker);
    }
}