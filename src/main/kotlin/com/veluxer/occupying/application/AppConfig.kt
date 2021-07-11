package com.veluxer.occupying.application

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(KorailProperties::class)
class AppConfig
