package nl.vollo.kern;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/apidocs")
@Log4j2
public class ApidocsCtl {

    @GetMapping
    public String home(Model model) {
        log.info("serving apidocs");
        return "/apidocs/index.html";
    }

}