package nl.vollo.kern.api;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GebruikerCtlApiTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

//    @Autowired
//    private ApiTestService apiTestService;




}
