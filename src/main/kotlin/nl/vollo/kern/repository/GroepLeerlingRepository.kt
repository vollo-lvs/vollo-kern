package nl.vollo.kern.repository

import nl.vollo.kern.model.GroepLeerling
import org.springframework.data.jpa.repository.JpaRepository

interface GroepLeerlingRepository : JpaRepository<GroepLeerling, Long>