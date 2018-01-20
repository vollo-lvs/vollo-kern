package nl.vollo.kern.service;

import nl.vollo.kern.model.Groep;
import nl.vollo.kern.model.Leerling;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class MijnGroepenSvc {

    @Resource
    SessionContext ctx;

    @PersistenceContext(unitName = "vollo-kern-persistence-unit")
    private EntityManager em;

    public List<Groep> getMijnGroepen() {
        TypedQuery<Groep> query = em.createQuery("select g from Groep g", Groep.class);
        return query.getResultList();
    }

    public List<Leerling> getGroepLeerlingen(Long groepId) {
        TypedQuery<Leerling> query = em.createQuery("select l from Leerling l " +
                "where l in (" +
                "  select gl.leerling from GroepLeerling gl " +
                "  where gl.groep.id = :groepId)", Leerling.class);
        query.setParameter("groepId", groepId);
        return query.getResultList();
    }
}
