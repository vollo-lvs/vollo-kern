package nl.vollo.kern.test

import com.fasterxml.jackson.databind.ObjectMapper
import nl.vollo.kern.api.InloggenCtl
import nl.vollo.kern.testdata.TestdataGenerator
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class ApiTestService {

    val objectMapper = ObjectMapper()

    private var port: Int = 0

    private lateinit var restTemplate: TestRestTemplate

    @Autowired
    private lateinit var testdataGenerator: TestdataGenerator

    fun init(port: Int, restTemplate: TestRestTemplate): ApiTestService {
        this.port = port
        this.restTemplate = restTemplate
        return this
    }

    fun genererenTestdata() {
        println("apiTestService.genereren, port=$port")
        testdataGenerator.genereren(
                aantalScholen = 1,
                aantalGroepen = 1,
                aantalLeerlingenMin = 3,
                aantalLeerlingenMax = 5
        )
    }

    fun inloggen(): HttpHeaders {
        val inloggenRequest = InloggenCtl.InloggenRequest("m0", "m0")
        val result: ResponseEntity<Void> = restTemplate.postForEntity(testUrl("/public/inloggen"), inloggenRequest)
        MatcherAssert.assertThat("Fout bij inloggen", result.statusCode, CoreMatchers.equalTo(HttpStatus.NO_CONTENT))
        val cookie: String? = result.headers.getFirst("Set-Cookie")
        MatcherAssert.assertThat("Geen cookie ontvangen bij inloggen", cookie, CoreMatchers.notNullValue())
        MatcherAssert.assertThat("Geen token ontvangen bij inloggen", cookie!!.startsWith("vollo_token"), CoreMatchers.equalTo(true))
        return HttpHeaders().apply { set("Cookie", cookie) }
    }

    fun testUrl(path: String) = "http://localhost:$port$path"

}