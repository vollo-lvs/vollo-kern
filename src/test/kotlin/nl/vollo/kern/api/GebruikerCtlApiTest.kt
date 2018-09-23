package nl.vollo.kern.api

import nl.vollo.kern.security.CookieService
import nl.vollo.kern.test.ApiTestService
import nl.vollo.kern.test.isTextNode
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.startsWith
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class GebruikerCtlApiTest {

    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Autowired
    private lateinit var apiTestService: ApiTestService

    @BeforeAll
    fun beforeAll() {
        apiTestService.init(port, restTemplate).genererenTestdata()
    }

    @Test
    fun `geeft ingelogde gebruiker terug na inloggen`() {
        val headers = apiTestService.inloggen()
        val response: ResponseEntity<String> = restTemplate.exchange(
                apiTestService.testUrl("/gebruiker/ingelogd"), HttpMethod.GET,
                HttpEntity<Void>(headers))

        assertThat(response.statusCode, equalTo(HttpStatus.OK))
        val json = apiTestService.objectMapper.readTree(response.body)
        assertThat(json["gebruikersnaam"], isTextNode("m0"))
        assertThat(json["rollen"], isTextNode("ROLE_GEBRUIKER"))
        assertThat(json["_type"], isTextNode("GEBRUIKER"))
        assertThat(json["medewerker"]["voornaam"], isTextNode("Gerharda"))
        assertThat(json["medewerker"]["achternaam"], isTextNode("Hoef"))
        assertThat(json["medewerker"]["_type"], isTextNode("MEDEWERKER"))
    }

    @Test
    fun `geeft geen ingelogde gebruiker indien niet ingelogd`() {
        val response: ResponseEntity<String> = restTemplate.exchange(
                apiTestService.testUrl("/gebruiker/ingelogd"), HttpMethod.GET)

        assertThat(response.statusCode, equalTo(HttpStatus.UNAUTHORIZED))
    }

    @Test
    fun `uitloggen logt gebruiker uit en geeft lege cookie terug`() {
        val headers = apiTestService.inloggen()
        val response: ResponseEntity<String> = restTemplate.exchange(
                apiTestService.testUrl("/gebruiker/uitloggen"), HttpMethod.POST,
                HttpEntity<Void>(headers))

        assertThat(response.statusCode, equalTo(HttpStatus.NO_CONTENT))
        val cookie: String? = response.headers.getFirst("Set-Cookie")
        assertThat(cookie, startsWith("${CookieService.VOLLO_TOKEN_NAME}=;"))
    }

}