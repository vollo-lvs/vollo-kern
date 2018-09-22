package nl.vollo.kern.api

import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class GebruikerCtlTest {

    @LocalServerPort
    private var port: Int = 0

    private val objectMapper = ObjectMapper()

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Test
    fun getIngelogdeGebruiker() {
        val token = inloggen()
        val headers = HttpHeaders().apply { set("Cookie", token) }
        val gebruiker: ResponseEntity<String> = restTemplate.exchange("http://localhost:$port/gebruiker/ingelogd", HttpMethod.GET,
                HttpEntity<Void>(headers))
        println(gebruiker)
    }

    @Test
    @Disabled
    fun logout() {
    }

    fun inloggen(): String {
        val inloggenRequest = InloggenCtl.InloggenRequest("m0", "m0")
        val result: ResponseEntity<Void> = restTemplate.postForEntity("http://localhost:$port/public/inloggen", inloggenRequest)
        val cookie: String? = result.headers.getFirst("Set-Cookie")
        assertThat(cookie, CoreMatchers.notNullValue())
        assertThat(cookie!!.startsWith("vollo_token"), CoreMatchers.equalTo(true))
        val token = cookie.substringBefore(";")
        println(token)
        return cookie
    }
}