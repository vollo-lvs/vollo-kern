package nl.vollo.kern.repository

import nl.vollo.kern.model.Medewerker
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MedewerkerRepository : JpaRepository<Medewerker, Long>