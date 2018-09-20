package nl.vollo.kern.repository

import nl.vollo.kern.model.Leerling
import org.springframework.data.jpa.repository.JpaRepository

interface LeerlingRepository : JpaRepository<Leerling, Long>
