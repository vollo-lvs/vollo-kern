package nl.vollo.kern.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import nl.vollo.kern.model.DomainEntity;
import nl.vollo.kern.model.DomainObject;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.Enumeration;
import java.util.Objects;

@Api(value = "crud")
@Stateless
@Path("/crud")
@Log4j2
public class CrudCtl {

    @PersistenceContext(unitName = "vollo-kern-persistence-unit")
    private EntityManager em;

    @ApiOperation(value = "")
    @GET
    @Path("/{entity}")
    @Produces("application/json")
    public Response getEntityAll(@PathParam("entity") DomainEntity entity) {
        TypedQuery<? extends DomainObject> query = em.createQuery(
                "select x from " + entity.getDomainClass().getSimpleName() + " x",
                entity.getDomainClass());
        return Response.ok(query.getResultList()).build();
    }

    @ApiOperation(value = "")
    @GET
    @Path("/{entity}/filter")
    @Produces("application/json")
    public Response getEntityFiltered(@PathParam("entity") DomainEntity entity,
                                      @Context HttpServletRequest request) {
        StringBuilder sql = new StringBuilder("select x from " + entity.getDomainClass().getSimpleName() + " x ");
        int i = 0;
        for (Enumeration<String> en = request.getParameterNames(); en.hasMoreElements(); i++) {
            String field = en.nextElement();
            if (!entity.isFieldAllowed(field)) {
                return Response.status(Response.Status.NOT_ACCEPTABLE).build();
            }
            sql.append(i == 0 ? "where" : "and").append(" x.").append(field).append(" = :value").append(i).append(" ");
        }

        TypedQuery<? extends DomainObject> query = em.createQuery(sql.toString(), entity.getDomainClass());
        i = 0;
        for (Enumeration<String> en = request.getParameterNames(); en.hasMoreElements(); i++) {
            String field = en.nextElement();
            query.setParameter("value" + i, request.getParameter(field));
        }
        return Response.ok(query.getResultList()).build();
    }

    @ApiOperation(value = "")
    @GET
    @Path("/{entity}/{id}/{subentity}")
    @Produces("application/json")
    public Response getSubEntity(@PathParam("entity") DomainEntity entity,
                                 @PathParam("id") Long id,
                                 @PathParam("subentity") DomainEntity subentity) {
        if (!entity.isEntityAllowed(subentity)) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }
        TypedQuery<? extends DomainObject> query = em.createQuery(
                "select x from " + subentity.getDomainClass().getSimpleName() + " x "
                        + "where x." + entity.name() + ".id = :id",
                subentity.getDomainClass());
        query.setParameter("id", id);
        return Response.ok(query.getResultList()).build();
    }

    @ApiOperation(value = "")
    @GET
    @Path("/{entity}/{id}")
    @Produces("application/json")
    public Response getEntityOne(@PathParam("entity") DomainEntity entity, @PathParam("id") Long id) {
        Object object = em.find(entity.getDomainClass(), id);
        return (object == null ? Response.status(Response.Status.NOT_FOUND) : Response.ok(object)).build();
    }

    @ApiOperation(value = "")
    @POST
    @Path("/{entity}")
    @Consumes("application/json")
    @Produces("application/json")
    public Response post(@PathParam("entity") DomainEntity entity, DomainObject object) {
        em.persist(object);
        return Response.status(Response.Status.CREATED).entity(object).build();
    }

    @ApiOperation(value = "")
    @PUT
    @Path("/{entity}/{id}")
    @Consumes("application/json")
    @Produces("application/json")
    public Response put(@PathParam("entity") DomainEntity entity, @PathParam("id") Long id, DomainObject object) {
        if (id == null || !Objects.equals(id, object.getId())) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        DomainObject updated = em.merge(object);
        return Response.ok(updated).build();
    }

    @ApiOperation(value = "")
    @DELETE
    @Path("/{entity}/{id}")
    public Response delete(@PathParam("entity") DomainEntity entity, @PathParam("id") Long id) {
        if (id == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        DomainObject object = em.find(entity.getDomainClass(), id);
        if (object == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        em.remove(object);
        return Response.status(Response.Status.OK).build();
    }

}
