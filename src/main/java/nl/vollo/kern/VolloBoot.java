package nl.vollo.kern;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
@Log4j2
public class VolloBoot {

	public static void main(String[] args) {
		SpringApplication.run(VolloBoot.class, args);
	}

	@RequestMapping("/")
	String home() {
		log.info("GET /");
		return "Hello world";
	}

	@RequestMapping("/hello2")
	String hello() {
		return "Goodbye";
	}
}
