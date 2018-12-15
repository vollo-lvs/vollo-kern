package nl.vollo.kern.repository

import nl.vollo.kern.model.Toets
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ToetsRepository : JpaRepository<Toets, Long>