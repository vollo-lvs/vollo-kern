package nl.vollo.kern.events.listeners

import mu.KotlinLogging
import nl.vollo.events.testdata.LeerlingFotoVerkregen
import nl.vollo.kern.repository.LeerlingRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

private val log = KotlinLogging.logger {}

@Component
class OnLeerlingFotoVerkregen {

    @Autowired
    private lateinit var leerlingRepository: LeerlingRepository

    @KafkaListener(topics = [LeerlingFotoVerkregen.TOPIC])
    fun receive(event: LeerlingFotoVerkregen) {
        log.info("id: ${event.id}")
        if (event.id != null) {
            leerlingRepository
                    .findById(event.id!!)
                    .ifPresent {
                        it.foto = event.foto
                        leerlingRepository.save(it)
                    }
        }
    }

}