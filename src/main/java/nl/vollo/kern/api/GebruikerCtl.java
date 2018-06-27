package nl.vollo.kern.api;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import nl.vollo.kern.model.Gebruiker;
import nl.vollo.kern.security.GebruikerAuthenticationService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@RestController
@RequestMapping("/gebruiker")
@FieldDefaults(level = PRIVATE, makeFinal = true)
@AllArgsConstructor(access = PACKAGE)
final class GebruikerCtl {
    @NonNull
    GebruikerAuthenticationService authentication;

    @GetMapping("/ingelogd")
    Gebruiker getIngelogdeGebruiker(@AuthenticationPrincipal final Gebruiker gebruiker) {
        return gebruiker;
    }

    @PostMapping("/uitloggen")
    void logout(@AuthenticationPrincipal final Gebruiker gebruiker) {
        authentication.logout(gebruiker);
    }
}