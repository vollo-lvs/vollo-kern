package nl.vollo.kern.events

import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jms.annotation.EnableJms
import org.springframework.jms.config.DefaultJmsListenerContainerFactory
import org.springframework.jms.config.JmsListenerContainerFactory
import org.springframework.jms.core.JmsTemplate
import org.springframework.jms.support.converter.MappingJackson2MessageConverter
import org.springframework.jms.support.converter.MessageConverter
import org.springframework.jms.support.converter.MessageType
import javax.jms.ConnectionFactory
import javax.jms.DeliveryMode

@Configuration
@EnableJms
class EventConfig {

    @Bean
    fun volloJmsFactory(connectionFactory: ConnectionFactory,
                  configurer: DefaultJmsListenerContainerFactoryConfigurer
    ): JmsListenerContainerFactory<*> {
        return DefaultJmsListenerContainerFactory().apply {
            configurer.configure(this, connectionFactory);
            this.setPubSubDomain(true)
            this.setClientId("vollo-kern")
            this.setSubscriptionDurable(true)
        }
    }

    @Bean
    fun jacksonJmsMessageConverter(): MessageConverter {
        return MappingJackson2MessageConverter().apply {
            this.setTargetType(MessageType.TEXT)
            this.setTypeIdPropertyName("_type")
        }
    }

    @Bean
    fun jmsTemplate(connectionFactory: ConnectionFactory): JmsTemplate {
        return JmsTemplate(connectionFactory).apply {
            this.setDeliveryPersistent(true)
            this.deliveryMode = DeliveryMode.PERSISTENT
            this.isPubSubDomain = true
        }
    }
}