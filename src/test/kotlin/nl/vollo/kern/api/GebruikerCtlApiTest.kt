package nl.vollo.kern.api

import com.fasterxml.jackson.databind.ObjectMapper
import nl.vollo.kern.test.isTextNode
import nl.vollo.kern.testdata.TestdataGenerator
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.*
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class GebruikerCtlApiTest {

    @LocalServerPort
    private var port: Int = 0

    private val objectMapper = ObjectMapper()

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Autowired
    private lateinit var testdataGenerator: TestdataGenerator

    @BeforeAll
    fun beforeAll() {
        testdataGenerator.genereren(
                aantalScholen = 1,
                aantalGroepen = 1,
                aantalLeerlingenMin = 3,
                aantalLeerlingenMax = 5
        )
    }

    @Test
    fun getIngelogdeGebruiker() {
        val token = inloggen()
        val headers = HttpHeaders().apply { set("Cookie", token) }
        val gebruiker: ResponseEntity<String> = restTemplate.exchange(testUrl("/gebruiker/ingelogd"), HttpMethod.GET,
                HttpEntity<Void>(headers))
        assertThat(gebruiker.statusCode, equalTo(HttpStatus.OK))
        println(gebruiker.body)
        val json = objectMapper.readTree(gebruiker.body)
        assertThat(json["gebruikersnaam"], isTextNode("m0"))
        assertThat(json["rollen"], isTextNode("ROLE_GEBRUIKER"))
        assertThat(json["_type"], isTextNode("GEBRUIKER"))
        assertThat(json["medewerker"]["voornaam"], isTextNode("Gerharda"))
        assertThat(json["medewerker"]["achternaam"], isTextNode("Hoef"))
        assertThat(json["medewerker"]["_type"], isTextNode("MEDEWERKER"))
    }

    @Test
    @Disabled
    fun logout() {
    }

    fun inloggen(): String {
        val inloggenRequest = InloggenCtl.InloggenRequest("m0", "m0")
        val result: ResponseEntity<Void> = restTemplate.postForEntity(testUrl("/public/inloggen"), inloggenRequest)
        assertThat("Fout bij inloggen", result.statusCode, equalTo(HttpStatus.NO_CONTENT))
        val cookie: String? = result.headers.getFirst("Set-Cookie")
        assertThat("Geen cookie ontvangen bij inloggen", cookie, CoreMatchers.notNullValue())
        assertThat("Geen token ontvangen bij inloggen", cookie!!.startsWith("vollo_token"), CoreMatchers.equalTo(true))
        return cookie
    }

    fun testUrl(path: String) = "http://localhost:$port$path"
}