package com.veluxer.occupying.application

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("agent.korail")
data class KorailProperties(
    val host: String,
    val contextPath: String,
    val timeout: Long,
)
