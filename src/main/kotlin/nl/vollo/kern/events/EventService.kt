package nl.vollo.kern.events

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jms.core.JmsTemplate
import org.springframework.stereotype.Service

@Service
class EventService {

    @Autowired
    private lateinit var jmsTemplate: JmsTemplate;

    fun send(event: LeerlingOpgehaald) {
        jmsTemplate.convertAndSend("nl.vollo.kern." + event.name, event.body)
    }
}