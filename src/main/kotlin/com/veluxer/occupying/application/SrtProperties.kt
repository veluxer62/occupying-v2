package com.veluxer.occupying.application

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("agent.srt")
data class SrtProperties(
    val host: String,
    val timeout: Long,
)
