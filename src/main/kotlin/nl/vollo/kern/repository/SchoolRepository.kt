package nl.vollo.kern.repository

import nl.vollo.kern.model.School
import org.springframework.data.jpa.repository.JpaRepository

interface SchoolRepository : JpaRepository<School, Long>