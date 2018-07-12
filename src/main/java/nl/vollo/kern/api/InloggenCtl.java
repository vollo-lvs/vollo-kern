package nl.vollo.kern.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.*;
import lombok.experimental.FieldDefaults;
import nl.vollo.kern.repository.GebruikerRepository;
import nl.vollo.kern.security.GebruikerAuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;
import static nl.vollo.kern.rest.RestUtils.errorHeader;

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
    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<InloggenResponse> inloggen(@RequestBody @Valid InloggenRequest data) {
        return authentication
                .login(data.getGebruikersnaam(), data.getWachtwoord())
                .map(InloggenResponse::new)
                .map(token -> new ResponseEntity<>(token, HttpStatus.OK))
                .orElse(new ResponseEntity<>(errorHeader("Invalid login and/or password"), HttpStatus.UNAUTHORIZED));
    }

    @Getter
    @Setter
    static class InloggenRequest {
        @NotBlank
        private String gebruikersnaam;

        @NotBlank
        private String wachtwoord;
    }

    @Value
    static class InloggenResponse {
        private String token;
    }
}
