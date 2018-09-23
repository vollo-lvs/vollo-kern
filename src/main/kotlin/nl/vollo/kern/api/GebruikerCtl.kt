package nl.vollo.kern.api;

import nl.vollo.kern.model.Gebruiker
import nl.vollo.kern.security.CookieService
import nl.vollo.kern.security.GebruikerAuthenticationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/gebruiker")
class GebruikerCtl {

    @Autowired
    private lateinit var authentication: GebruikerAuthenticationService

    @Autowired
    private lateinit var cookieService: CookieService

    @GetMapping("/ingelogd")
    fun getIngelogdeGebruiker(@AuthenticationPrincipal gebruiker: Gebruiker): Gebruiker {
        return gebruiker;
    }

    @PostMapping("/uitloggen")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun logout(@AuthenticationPrincipal gebruiker: Gebruiker, request: HttpServletRequest, response: HttpServletResponse) {
        authentication.logout(gebruiker);
        response.addCookie(cookieService.verwijderCookie(request));
    }
}