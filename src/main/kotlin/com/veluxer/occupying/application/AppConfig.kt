package com.veluxer.occupying.application

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import java.time.Duration

@Configuration
@EnableConfigurationProperties(KorailProperties::class)
class AppConfig {

    @Bean
    fun korailClient(korailProperties: KorailProperties): WebClient {
        return WebClient.builder()
            .baseUrl(korailProperties.host)
            .defaultUriVariables(mapOf("Device" to "AD"))
            .clientConnector(
                ReactorClientHttpConnector(
                    HttpClient.create().responseTimeout(Duration.ofMillis(korailProperties.timeout))
                )
            )
            .build()
    }
}
