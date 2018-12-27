package nl.vollo.kern.repository

import nl.vollo.kern.model.Inschrijving
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface InschrijvingRepository : JpaRepository<Inschrijving, Long> {

    @Query("select i from Inschrijving i where i.leerling.id = :leerlingId")
    fun findAllByLeerlingId(@Param("leerlingId") leerlingId: Long): List<Inschrijving>

}