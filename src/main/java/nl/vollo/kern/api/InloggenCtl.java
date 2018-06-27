package nl.vollo.kern.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import nl.vollo.kern.repository.GebruikerRepository;
import nl.vollo.kern.security.GebruikerAuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@RestController
@Api(value = "Gebruiker")
@RequestMapping("/public/inloggen")
@FieldDefaults(level = PRIVATE, makeFinal = true)
@AllArgsConstructor(access = PACKAGE)
final class InloggenCtl {
    @NonNull
    GebruikerAuthenticationService authentication;
    @NonNull
    GebruikerRepository gebruikerRepository;

    @ApiOperation(value = "Inloggen.")
    @PostMapping()
    ResponseEntity<String> inloggen(
            @RequestParam("gebruikersnaam") final String gebruikersnaam,
            @RequestParam("wachtwoord") final String wachtwoord) {
        return authentication
                .login(gebruikersnaam, wachtwoord)
                .map(token -> new ResponseEntity<>(token, HttpStatus.OK))
                .orElse(new ResponseEntity<>("invalid login and/or password", HttpStatus.NOT_FOUND));
    }
}
