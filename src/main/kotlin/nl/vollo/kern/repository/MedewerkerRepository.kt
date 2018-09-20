package nl.vollo.kern.repository

import nl.vollo.kern.model.Medewerker
import org.springframework.data.jpa.repository.JpaRepository

interface MedewerkerRepository : JpaRepository<Medewerker, Long>