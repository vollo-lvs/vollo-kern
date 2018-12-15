package nl.vollo.kern.repository

import nl.vollo.kern.model.Toetsafname
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ToetsafnameRepository : JpaRepository<Toetsafname, Long>
