package nl.vollo.kern;

import mu.KotlinLogging
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

private val log = KotlinLogging.logger {}

@Controller
@RequestMapping("/apidocs")
class ApidocsCtl {

    @GetMapping
    fun home(model: Model): String {
        log.info("serving apidocs")
        return "/apidocs/index.html"
    }

}