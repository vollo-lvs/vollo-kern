package nl.vollo.kern.events.listeners

import nl.vollo.events.testdata.LeerlingFotoVerkregen
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class OnLeerlingFotoVerkregen {

    @KafkaListener(topics = [LeerlingFotoVerkregen.TOPIC])
    fun receive(event: ConsumerRecord<String, LeerlingFotoVerkregen>) {
        println("message: ${event.value().id}")
    }

}