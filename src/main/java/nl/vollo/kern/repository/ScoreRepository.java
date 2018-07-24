package nl.vollo.kern.repository;

import nl.vollo.kern.model.Score;
import nl.vollo.kern.model.Leerling;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScoreRepository extends JpaRepository<Score, Long> {

    List<Score> findAllByLeerling(Leerling leerling);

}
