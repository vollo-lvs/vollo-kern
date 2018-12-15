package nl.vollo.kern.repository

import nl.vollo.kern.model.GroepLeerling
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GroepLeerlingRepository : JpaRepository<GroepLeerling, Long>