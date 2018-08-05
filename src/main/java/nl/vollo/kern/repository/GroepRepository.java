package nl.vollo.kern.repository;

import nl.vollo.kern.model.Groep;
import nl.vollo.kern.model.Leerling;
import nl.vollo.kern.model.Medewerker;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface GroepRepository extends JpaRepository<Groep, Long> {

    @Query("select l from Leerling l " +
            "where l in (" +
            "  select gl.leerling from GroepLeerling gl " +
            "  where gl.groep.id = :groepId " +
            "  and gl.datumBegin <= :peildatum " +
            "  and (gl.datumEinde is null or gl.datumEinde >= :peildatum) " +
            ") " +
            "order by l.achternaam, l.roepnaam")
    List<Leerling> getGroepLeerlingen(
            @Param("groepId") Long id,
            @Param("peildatum") Date peildatum);

    @Query("select g from Groep g "
            + "where g in ( "
            + "  select gm.groep from GroepMedewerker gm "
            + "  where gm.medewerker = :medewerker "
            + "  and gm.datumBegin <= :peildatum "
            + "  and (gm.datumEinde is null or gm.datumEinde >= :peildatum) "
            + ")")
    List<Groep> findByMedewerker(
            @Param("medewerker") Medewerker medewerker,
            @Param("peildatum") Date peildatum);
}
