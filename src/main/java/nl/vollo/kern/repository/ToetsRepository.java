package nl.vollo.kern.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import nl.vollo.kern.model.Toets;

public interface ToetsRepository extends JpaRepository<Toets, Long> {}