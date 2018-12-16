package nl.vollo.kern.events.listeners

import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import java.util.*

@Component
class OnLeerlingFotoVerkregen {

    @KafkaListener(topics = ["nl.vollo.testdata.LeerlingFotoVerkregen"], groupId = "volloKern")
    fun receive(message: Any) {
        println("message: ${message}")
    }

}