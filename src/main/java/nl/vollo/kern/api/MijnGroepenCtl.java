package nl.vollo.kern.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j2;
import nl.vollo.kern.model.Groep;
import nl.vollo.kern.model.Leerling;
import nl.vollo.kern.repository.GroepRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.Produces;
import java.security.Principal;
import java.util.List;

@Api(value = "MijnGroepen")
@RestController
@RequestMapping("/mijn-groepen")
@Log4j2
public class MijnGroepenCtl {

    @Autowired
    GroepRepository groepRepository;

    @ApiOperation(value = "Haal de groepen van de ingelogde medewerker op.")
    @GetMapping(produces = "application/json")
    @PreAuthorize("isFullyAuthenticated() AND hasRole('GEBRUIKER')")
    public List<Groep> getMijnGroepen(Principal principal) {
        System.out.println(principal.getName());
        SecurityContext context = SecurityContextHolder.getContext();
        System.out.println(context.getAuthentication().getAuthorities());
        return groepRepository.findAll();
    }

    @ApiOperation(value = "Haal de leerlingen van een groep van de ingelogde medewerker op.")
    @GetMapping(value = "/{id}/leerlingen", produces = "application/json")
    @Produces("application/json")
    @PreAuthorize("hasRole('USER')")
    public List<Leerling> getGroepLeerlingen(
            @ApiParam("ID van een groep")
            @PathVariable("id") Long id) {
        return groepRepository.getGroepLeerlingen(id);
    }

}
