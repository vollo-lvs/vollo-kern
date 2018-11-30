package nl.vollo.kern.repository

import nl.vollo.kern.model.Leerling
import nl.vollo.kern.model.Score
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ScoreRepository : JpaRepository<Score, Long> {

    fun findAllByLeerling(leerling: Leerling): List<Score>

    @Query("select s from Score s where s.leerling.id = :id")
    fun findAllByLeerlingId(@Param("id") id: Long): List<Score>;

}
