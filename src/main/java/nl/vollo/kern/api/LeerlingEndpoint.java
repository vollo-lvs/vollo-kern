package nl.vollo.kern.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import nl.vollo.kern.model.Geslacht;
import nl.vollo.kern.model.Leerling;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.time.DateUtils;

import javax.ejb.Stateless;
import javax.persistence.*;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import java.util.Date;
import java.util.List;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

/**
 * 
 */
@Api(value = "Leerling")
@Stateless
@Path("/leerling")
@Log4j2
public class LeerlingEndpoint {
    @PersistenceContext(unitName = "vollo-kern-persistence-unit")
	private EntityManager em;

	@ApiOperation(value = "Maak en retourneer een voorbeeldleerling met willekeurig data")
	@GET
    @Path("/sample")
    @Produces("application/json")
    public Response sampleLeerling() {
        Leerling leerling = new Leerling();
        leerling.setAchternaam(randomAlphabetic(40));
        leerling.setVoornamen(randomAlphabetic(40));
        leerling.setRoepnaam(randomAlphabetic(20));
        leerling.setGeboortedatum(DateUtils.addDays(new Date(), -RandomUtils.nextInt(365 * 6, 365 * 12)));
        if (Math.random() > .67) {
            leerling.setGeslacht(Geslacht.MAN);
        } else if (Math.random() > .5) {
            leerling.setGeslacht(Geslacht.VROUW);
        } else {
            leerling.setGeslacht(Geslacht.OVERIG);
        }
        em.persist(leerling);
        log.info("Leerling aangemaakt: {}", leerling);
        return Response.ok(leerling).build();
    }

	@POST
	@Consumes("application/json")
	public Response create(Leerling entity) {
		em.persist(entity);
		return Response.created(
				UriBuilder.fromResource(LeerlingEndpoint.class)
						.path(String.valueOf(entity.getId())).build()).build();
	}

	@DELETE
	@Path("/{id:[0-9][0-9]*}")
	public Response deleteById(@PathParam("id") Long id) {
		Leerling entity = em.find(Leerling.class, id);
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		em.remove(entity);
		return Response.noContent().build();
	}

	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces("application/json")
	public Response findById(@PathParam("id") Long id) {
		TypedQuery<Leerling> findByIdQuery = em
				.createQuery(
						"SELECT DISTINCT l FROM Leerling l WHERE l.id = :entityId ORDER BY l.id",
						Leerling.class);
		findByIdQuery.setParameter("entityId", id);
		Leerling entity;
		try {
			entity = findByIdQuery.getSingleResult();
		} catch (NoResultException nre) {
			entity = null;
		}
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.ok(entity).build();
	}

	@GET
	@Produces("application/json")
	public List<Leerling> listAll(@QueryParam("start") Integer startPosition,
			@QueryParam("max") Integer maxResult) {
		TypedQuery<Leerling> findAllQuery = em.createQuery(
				"SELECT DISTINCT l FROM Leerling l ORDER BY l.id",
				Leerling.class);
		if (startPosition != null) {
			findAllQuery.setFirstResult(startPosition);
		}
		if (maxResult != null) {
			findAllQuery.setMaxResults(maxResult);
		}
		final List<Leerling> results = findAllQuery.getResultList();
		return results;
	}

	@PUT
	@Path("/{id:[0-9][0-9]*}")
	@Consumes("application/json")
	public Response update(@PathParam("id") Long id, Leerling entity) {
		if (entity == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		if (id == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		if (!id.equals(entity.getId())) {
			return Response.status(Status.CONFLICT).entity(entity).build();
		}
		if (em.find(Leerling.class, id) == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		try {
			entity = em.merge(entity);
		} catch (OptimisticLockException e) {
			return Response.status(Response.Status.CONFLICT)
					.entity(e.getEntity()).build();
		}

		return Response.noContent().build();
	}
}
