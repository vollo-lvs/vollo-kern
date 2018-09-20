package nl.vollo.kern.repository

import nl.vollo.kern.model.GroepMedewerker
import org.springframework.data.jpa.repository.JpaRepository

interface GroepMedewerkerRepository : JpaRepository<GroepMedewerker, Long>