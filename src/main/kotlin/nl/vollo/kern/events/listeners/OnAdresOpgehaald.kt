package nl.vollo.kern.events.listeners

import mu.KotlinLogging
import nl.vollo.events.bag.AdresOpgehaald
import nl.vollo.kern.model.Adres
import nl.vollo.kern.model.DomainEntity
import nl.vollo.kern.repository.LeerlingRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

private val log = KotlinLogging.logger {}

@Component
class OnAdresOpgehaald {

    @Autowired
    private lateinit var leerlingRepository: LeerlingRepository

    @KafkaListener(topics = [AdresOpgehaald.TOPIC])
    fun receive(event: AdresOpgehaald) {
        log.info("${event.domainEntity} id: ${event.domainId} ${event.openbareRuimte} ${event.huisnummer}, ${event.postcode} ${event.woonplaats}")
        if (DomainEntity.valueOf(event.domainEntity) == DomainEntity.LEERLING) {
            leerlingRepository
                    .findById(event.domainId)
                    .ifPresent { leerling ->
                        leerling.adres = Adres(
                                straat = event.openbareRuimte,
                                huisnummer = event.huisnummer,
                                toevoeging = leerling.adres?.toevoeging,
                                postcode = event.postcode,
                                plaats = event.woonplaats,
                                longitude = event.lon,
                                latitude = event.lat
                        )
                        leerlingRepository.save(leerling)
                    }
        }
    }

}