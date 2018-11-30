package nl.vollo.kern.repository

import nl.vollo.kern.model.Ouder
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface OuderRepository : JpaRepository<Ouder, Long> {

    @Query("select o from Ouder o "
            + "where o in ( "
            + "  select ol.ouder from OuderLeerling ol "
            + "  where ol.leerling.id = :id "
            + ")"
    )
    fun findAllByLeerlingId(@Param("id") id: Long): List<Ouder>
}