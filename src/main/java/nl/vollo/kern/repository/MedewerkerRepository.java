package nl.vollo.kern.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import nl.vollo.kern.model.Medewerker;

public interface MedewerkerRepository extends JpaRepository<Medewerker, Long> {}