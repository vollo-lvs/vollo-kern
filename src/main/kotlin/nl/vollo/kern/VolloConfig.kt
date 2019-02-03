package nl.vollo.kern

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.time.Duration

@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "vollo.kern")
class VolloConfig {
    val jwt = Jwt()

    class Jwt {
        var issuer: String = "vollo"
        var expiration: Duration = Duration.ofDays(1)
        var clockSkew: Duration = Duration.ofMinutes(5)
        var secret: String = "secret"
    }

    @Bean
    fun restTemplate(builder: RestTemplateBuilder): RestTemplate {
        return builder.build()
    }

}
