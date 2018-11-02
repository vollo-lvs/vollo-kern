package nl.vollo.kern.api;

import com.fasterxml.jackson.databind.JsonNode;
import nl.vollo.kern.security.CookieService;
import nl.vollo.kern.test.ApiTestService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static nl.vollo.kern.test.JsonNodeIsEqualToTextMatcher.isTextNode;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.startsWith;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("GebruikerCtlApi")
class GebruikerCtlIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ApiTestService apiTestService;

    @BeforeAll
    void beforeAll() {
        apiTestService.init(port, restTemplate).genererenTestdata();
    }

    @Test
    @DisplayName("geeft ingelogde gebruiker terug na inloggen")
    void inloggen() throws IOException {
        var headers = apiTestService.inloggen();
        var response = restTemplate.exchange(
                apiTestService.testUrl("/gebruiker/ingelogd"), HttpMethod.GET,
                new HttpEntity<Void>(headers), String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        var json = apiTestService.getObjectMapper().readTree(response.getBody());
        assertThat(json.get("gebruikersnaam"), isTextNode("m0"));
        assertThat(json.get("rollen"), isTextNode("ROLE_GEBRUIKER"));
        assertThat(json.get("_type"), isTextNode("GEBRUIKER"));
        JsonNode medewerker = json.get("medewerker");
        assertThat(medewerker.get("voornaam"), isTextNode("Kwaku"));
        assertThat(medewerker.get("achternaam"), isTextNode("Oudejans"));
        assertThat(medewerker.get("_type"), isTextNode("MEDEWERKER"));
    }

    @Test
    @DisplayName("geeft geen ingelogde gebruiker indien niet ingelogd")
    void nietIngelogd() {
        var response = restTemplate.exchange(
                apiTestService.testUrl("/gebruiker/ingelogd"), HttpMethod.GET, null, String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
    }

    @Test
    @DisplayName("uitloggen logt gebruiker uit en geeft lege cookie terug")
    void uitloggen() {
        var headers = apiTestService.inloggen();
        var response = restTemplate.exchange(
                apiTestService.testUrl("/gebruiker/uitloggen"), HttpMethod.POST,
                new HttpEntity<Void>(headers), String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.NO_CONTENT));
        var cookie = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        assertThat(cookie, startsWith(CookieService.VOLLO_TOKEN_NAME + "=;"));
    }
}
