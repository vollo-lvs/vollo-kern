package nl.vollo.kern.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import nl.vollo.kern.model.School;

public interface SchoolRepository extends JpaRepository<School, Long> {

}