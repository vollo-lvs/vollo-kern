package nl.vollo.kern.api;

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import nl.vollo.kern.repository.GebruikerRepository
import nl.vollo.kern.rest.RestUtils.errorHeader
import nl.vollo.kern.security.GebruikerAuthenticationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid

@RestController
@Api(value = "Gebruiker")
@RequestMapping("/public/inloggen")
class InloggenCtl {
    @Autowired
    private lateinit var authentication: GebruikerAuthenticationService

    @Autowired
    private lateinit var gebruikerRepository: GebruikerRepository

    @ApiOperation(value = "Inloggen.")
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun inloggen(@RequestBody @Valid data: InloggenRequest, request: HttpServletRequest, response: HttpServletResponse): ResponseEntity<Void> {
        // TODO logger
        System.out.println("un=" + data.gebruikersnaam + ", ww=" + data.wachtwoord);
        return authentication
                .login(data.gebruikersnaam, data.wachtwoord)
                .map { InloggenResponse(it) }
                .map { token ->
                    response.addCookie(nieuweCookie(request, token.token));
                    ResponseEntity<Void>(HttpStatus.NO_CONTENT)
                }
                .orElseGet {
                    response.addCookie(verwijderCookie(request));
                    ResponseEntity(errorHeader("Invalid login and/or password"), HttpStatus.UNAUTHORIZED);
                }
    }

    private fun nieuweCookie(request: HttpServletRequest, token: String): Cookie {
        val cookie: Cookie = Cookie("vollo_token", token)
        cookie.isHttpOnly = true
        cookie.secure = request.isSecure
        cookie.path = "/"
        cookie.maxAge = -1
        return cookie;
    }

    private fun verwijderCookie(request: HttpServletRequest): Cookie {
        val cookie: Cookie = Cookie("vollo_token", "")
        cookie.isHttpOnly = true
        cookie.secure = request.isSecure
        cookie.path = "/"
        cookie.maxAge = 0
        return cookie;
    }

    data class InloggenRequest(val gebruikersnaam: String, val wachtwoord: String)

    data class InloggenResponse(val token: String)
}
