package nl.vollo.kern

import nl.vollo.kern.testdata.TestdataGenerator

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@SpringBootApplication
class VolloBoot {

    @Autowired
    private lateinit var testdataGenerator: TestdataGenerator

    @PostMapping("/public/testdata")
    fun genererenTestdata() {
        testdataGenerator.genereren()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(VolloBoot::class.java, *args)
        }
    }
}
