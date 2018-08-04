package nl.vollo.kern.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import nl.vollo.kern.model.Inschrijving;

public interface InschrijvingRepository extends JpaRepository<Inschrijving, Long> {
}