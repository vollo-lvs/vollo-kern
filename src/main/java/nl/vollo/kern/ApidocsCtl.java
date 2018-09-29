package nl.vollo.kern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/apidocs")
public class ApidocsCtl {

    private static final Logger log = LogManager.getLogger(ApidocsCtl.class);

    @GetMapping
    public String home(Model model) {
        log.info("serving apidocs");
        return "/apidocs/index.html";
    }

}