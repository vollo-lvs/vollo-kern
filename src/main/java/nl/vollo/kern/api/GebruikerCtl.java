package nl.vollo.kern.api;

import nl.vollo.kern.model.Gebruiker;
import nl.vollo.kern.security.CookieService;
import nl.vollo.kern.security.GebruikerAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/gebruiker")
final class GebruikerCtl {

    @Autowired
    private GebruikerAuthenticationService authentication;

    @Autowired
    private CookieService cookieService;

    @GetMapping("/ingelogd")
    Gebruiker getIngelogdeGebruiker(@AuthenticationPrincipal final Gebruiker gebruiker) {
        return gebruiker;
    }

    @PostMapping("/uitloggen")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void logout(@AuthenticationPrincipal final Gebruiker gebruiker, HttpServletRequest request, HttpServletResponse response) {
        authentication.logout(gebruiker);
        response.addCookie(cookieService.verwijderCookie(request));
    }
}