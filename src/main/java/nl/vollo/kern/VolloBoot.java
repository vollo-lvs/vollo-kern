package nl.vollo.kern;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
public class VolloBoot {

	public static void main(String[] args) {
		SpringApplication.run(VolloBoot.class, args);
	}

	@RequestMapping("/")
	String home() {
		return "Hello world";
	}

	@RequestMapping("/hello2")
	String hello() {
		return "Goodbye";
	}
}
