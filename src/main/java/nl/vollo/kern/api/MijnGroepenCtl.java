package nl.vollo.kern.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j2;
import nl.vollo.kern.model.Groep;
import nl.vollo.kern.model.Leerling;
import nl.vollo.kern.repository.GroepRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.Produces;
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
    public List<Groep> getMijnGroepen() {
        return groepRepository.findAll();
    }

    @ApiOperation(value = "Haal de leerlingen van een groep van de ingelogde medewerker op.")
    @GetMapping(value = "/{id}/leerlingen", produces = "application/json")
    @Produces("application/json")
    public List<Leerling> getGroepLeerlingen(
            @ApiParam("ID van een groep")
            @PathVariable("id") Long id) {
        return groepRepository.getGroepLeerlingen(id);
    }

}
