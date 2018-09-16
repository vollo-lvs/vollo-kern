package nl.vollo.kern;

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/apidocs")
class ApidocsCtl {

    private val log = LoggerFactory.getLogger(ApidocsCtl::class.java)

    @GetMapping
    fun home(model: Model): String {
        log.info("serving apidocs")
        return "/apidocs/index.html"
    }

}