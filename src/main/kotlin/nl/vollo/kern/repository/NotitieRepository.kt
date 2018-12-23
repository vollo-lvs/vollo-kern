package nl.vollo.kern.repository

import nl.vollo.kern.model.Notitie
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface NotitieRepository : JpaRepository<Notitie, Long> {

    @Query("select n from Notitie n where n.leerling.id = :leerlingId")
    fun findByLeerlingId(@Param("leerlingId") leerlingId: Long): List<Notitie>

}