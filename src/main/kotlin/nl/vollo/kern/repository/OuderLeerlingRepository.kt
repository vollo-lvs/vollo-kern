package nl.vollo.kern.repository

import nl.vollo.kern.model.OuderLeerling
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OuderLeerlingRepository : JpaRepository<OuderLeerling, Long>