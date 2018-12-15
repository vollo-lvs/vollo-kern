package nl.vollo.kern.repository

import nl.vollo.kern.model.School
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SchoolRepository : JpaRepository<School, Long>