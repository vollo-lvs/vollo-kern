package nl.vollo.kern.repository

import nl.vollo.kern.model.Leerling
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LeerlingRepository : JpaRepository<Leerling, Long>
