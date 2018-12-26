package nl.vollo.kern.repository

import nl.vollo.kern.model.Medewerker
import nl.vollo.kern.model.Notitie
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface NotitieRepository : JpaRepository<Notitie, Long> {

    @Query("""
        select n
        from   Notitie n
        where  n.leerling.id = :leerlingId
        and    (  n.notitieNiveau <> nl.vollo.kern.model.enums.NotitieNiveau.PERSOONLIJK
               or n.medewerker = :medewerker)
        """)
    fun findByLeerlingIdVoorMedewerker(
            @Param("leerlingId") leerlingId: Long,
            @Param("medewerker") medewerker: Medewerker): List<Notitie>

}