package nl.vollo.kern.repository;

import nl.vollo.kern.model.Leerling;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeerlingRepository extends JpaRepository<Leerling, Long> {
}
