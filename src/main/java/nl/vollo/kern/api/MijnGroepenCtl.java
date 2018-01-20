package nl.vollo.kern.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j2;
import nl.vollo.kern.model.Groep;
import nl.vollo.kern.model.Leerling;
import nl.vollo.kern.service.MijnGroepenSvc;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.List;

@Api(value = "MijnGroepen")
@Stateless
@Path("/mijn-groepen")
@Log4j2
public class MijnGroepenCtl {

    @Inject
    MijnGroepenSvc mijnGroepenSvc;

    @ApiOperation(value = "Haal de groepen van de ingelogde medewerker op.")
    @GET
    @Produces("application/json")
    public List<Groep> getMijnGroepen() {
        return mijnGroepenSvc.getMijnGroepen();
    }

    @ApiOperation(value = "Haal de leerlingen van een groep van de ingelogde medewerker op.")
    @GET
    @Path("/{id}/leerlingen")
    @Produces("application/json")
    public List<Leerling> getGroepLeerlingen(
            @ApiParam("ID van een groep")
            @PathParam("id") Long id) {
        return mijnGroepenSvc.getGroepLeerlingen(id);
    }


}