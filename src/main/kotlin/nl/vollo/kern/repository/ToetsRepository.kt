package nl.vollo.kern.repository

import nl.vollo.kern.model.Toets
import org.springframework.data.jpa.repository.JpaRepository

interface ToetsRepository : JpaRepository<Toets, Long>