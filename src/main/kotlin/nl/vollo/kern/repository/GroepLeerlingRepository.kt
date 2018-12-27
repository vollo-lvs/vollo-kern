package nl.vollo.kern.repository

import nl.vollo.kern.model.GroepLeerling
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface GroepLeerlingRepository : JpaRepository<GroepLeerling, Long> {

    @Query("select gl from GroepLeerling gl where gl.leerling.id = :leerlingId")
    fun findAllByLeerlingId(@Param("leerlingId") leerlingId: Long): List<GroepLeerling>
}