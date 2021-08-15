package com.veluxer.occupying

import com.veluxer.occupying.application.TrainTicketBox
import com.veluxer.occupying.domain.Agent
import io.mockk.mockk
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class TestAppConfig {
    @Bean
    fun korail(): Agent = mockk()

    @Bean
    fun srt(): Agent = mockk()

    @Bean
    fun trainTicketBox(korail: Agent, srt: Agent) = TrainTicketBox(korail, srt)
}
