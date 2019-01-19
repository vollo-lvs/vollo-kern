package nl.vollo.kern.repository

import nl.vollo.kern.model.Leerling
import nl.vollo.kern.model.Medewerker
import nl.vollo.kern.model.Score
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface ScoreRepository : JpaRepository<Score, Long> {

    fun findAllByLeerling(leerling: Leerling): List<Score>

    @Query("select s from Score s where s.leerling.id = :id")
    fun findAllByLeerlingId(@Param("id") id: Long): List<Score>

    @Query("""
        select s from Score s where s.leerling in (
            select gl.leerling from GroepLeerling gl
            where gl.groep in (
                select gm.groep from GroepMedewerker gm
                where gm.medewerker = :medewerker
            )
            and gl.datumBegin >= :datumBegin
            and (gl.datumEinde is null or gl.datumEinde <= :datumEinde)
        )
    """)
    fun findAllByMedewerkerIdInPeriode(
            @Param("medewerker") medewerker: Medewerker,
            @Param("datumBegin") datumBegin: LocalDate,
            @Param("datumEinde") datumEinde: LocalDate
    ): List<Score>

}
