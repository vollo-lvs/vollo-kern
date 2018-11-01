package nl.vollo.kern.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.vollo.kern.api.InloggenCtl;
import nl.vollo.kern.testdata.TestdataGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;


@Service
public class ApiTestService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private int port;

    private TestRestTemplate restTemplate;

    @Autowired
    private TestdataGenerator testdataGenerator;

    public ApiTestService init(int port, TestRestTemplate restTemplate) {
        this.port = port;
        this.restTemplate = restTemplate;
        return this;
    }

    public void genererenTestdata() {
        testdataGenerator.genereren(1, 1, 3, 5);
    }

    public HttpHeaders inloggen() {
        var inloggenRequest = new InloggenCtl.InloggenRequest("m0", "m0");
        var result = restTemplate.postForEntity(testUrl("/public/inloggen"), inloggenRequest, Void.class);
        assertThat("Fout bij inloggen", result.getStatusCode(), is(HttpStatus.NO_CONTENT));
        var cookie = result.getHeaders().getFirst("Set-Cookie");
        assertThat("Geen cookie ontvangen bij inloggen", cookie, notNullValue());
        assertThat("Geen token ontvangen bij inloggen", cookie.startsWith("vollo_token"), is(true));

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.COOKIE, cookie);
        return headers;
    }

    public String testUrl(String path) {
        return String.format("http://localhost:%s%s", port, path);
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
