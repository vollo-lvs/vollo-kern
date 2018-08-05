package nl.vollo.kern;

import nl.vollo.kern.testdata.TestdataGenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class VolloBoot {

	@Autowired
	TestdataGenerator testdataGenerator;

	public static void main(String[] args) {
		SpringApplication.run(VolloBoot.class, args);
	}

	@PostMapping("/public/testdata")
	void genererenTestdata() {
		testdataGenerator.genereren();
	}
}
