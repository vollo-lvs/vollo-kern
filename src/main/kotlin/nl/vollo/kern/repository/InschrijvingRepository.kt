package nl.vollo.kern.repository

import nl.vollo.kern.model.Inschrijving
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface InschrijvingRepository : JpaRepository<Inschrijving, Long>