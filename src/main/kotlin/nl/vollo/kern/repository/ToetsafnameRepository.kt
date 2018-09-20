package nl.vollo.kern.repository

import nl.vollo.kern.model.Toetsafname
import org.springframework.data.jpa.repository.JpaRepository

interface ToetsafnameRepository : JpaRepository<Toetsafname, Long>
