package com.veluxer.occupying.application

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.util.MimeType
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import java.time.Duration
import java.util.TimeZone
import javax.annotation.PostConstruct

@Configuration
@EnableConfigurationProperties(KorailProperties::class)
class AppConfig {

    @PostConstruct
    fun init() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"))
    }

    @Bean
    fun korailClient(korailProperties: KorailProperties): WebClient {
        return WebClient.builder()
            .baseUrl(korailProperties.host)
            .clientConnector(
                ReactorClientHttpConnector(
                    HttpClient.create().responseTimeout(Duration.ofMillis(korailProperties.timeout))
                )
            )
            .codecs {
                it.defaultCodecs()
                    .jackson2JsonDecoder(Jackson2JsonDecoder(jacksonObjectMapper(), MimeType.valueOf("text/plain")))
            }
            .build()
    }
}
