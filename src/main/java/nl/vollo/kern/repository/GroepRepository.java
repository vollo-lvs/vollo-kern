package nl.vollo.kern.repository;

import nl.vollo.kern.model.Groep;
import nl.vollo.kern.model.Leerling;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GroepRepository extends JpaRepository<Groep, Long> {

    @Query("select l from Leerling l " +
            "where l in (" +
            "  select gl.leerling from GroepLeerling gl " +
            "  where gl.groep.id = :groepId)")
    List<Leerling> getGroepLeerlingen(@Param("groepId") Long id);
}
