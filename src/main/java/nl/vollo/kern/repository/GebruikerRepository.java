package nl.vollo.kern.repository;

import nl.vollo.kern.model.Gebruiker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GebruikerRepository extends JpaRepository<Gebruiker, Long> {
    Optional<Gebruiker> findByGebruikersnaam(String gebruikersnaam);
}
