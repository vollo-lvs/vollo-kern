package nl.vollo.kern.repository

import nl.vollo.kern.model.Leerling
import nl.vollo.kern.model.Score
import org.springframework.data.jpa.repository.JpaRepository

interface ScoreRepository : JpaRepository<Score, Long> {

    fun findAllByLeerling(leerling: Leerling): List<Score>

}
