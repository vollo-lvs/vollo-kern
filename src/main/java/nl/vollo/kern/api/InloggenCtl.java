package nl.vollo.kern.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import nl.vollo.kern.repository.GebruikerRepository;
import nl.vollo.kern.security.CookieService;
import nl.vollo.kern.security.GebruikerAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static lombok.AccessLevel.PRIVATE;
import static nl.vollo.kern.rest.RestUtils.errorHeader;

@RestController
@Api(value = "Gebruiker")
@RequestMapping("/public/inloggen")
@FieldDefaults(level = PRIVATE)
final class InloggenCtl {

    @Autowired
    GebruikerAuthenticationService authentication;

    @Autowired
    GebruikerRepository gebruikerRepository;

    @Autowired
    CookieService cookieService;

    @ApiOperation(value = "Inloggen.")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<Void> inloggen(@RequestBody @Valid InloggenRequest data, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("un="+data.getGebruikersnaam()+", ww=" + data.getWachtwoord());
        return authentication
                .login(data.getGebruikersnaam(), data.getWachtwoord())
                .map(InloggenResponse::new)
                .map(token -> {
                    response.addCookie(cookieService.nieuweCookie(request, token.getToken()));
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
                })
                .orElseGet(() -> {
                    response.addCookie(cookieService.verwijderCookie(request));
                    return new ResponseEntity<>(errorHeader("Invalid login and/or password"), HttpStatus.UNAUTHORIZED);
                });
    }

    @Getter
    @Setter
    static class InloggenRequest {
        private String gebruikersnaam;

        private String wachtwoord;
    }

    @Value
    static class InloggenResponse {
        private String token;
    }
}
