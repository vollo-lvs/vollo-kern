package nl.vollo.kern.events.listeners

import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Component
import java.util.*
import javax.jms.Message

@Component
class OnLeerlingFotoVerkregen {

    @JmsListener(destination = "nl.vollo.kern.LeerlingFotoVerkregen", containerFactory = "volloJmsFactory")
    fun receive(message: Message) {
        println("message from ${Date(message.jmsTimestamp)}: ${message}")
    }

}