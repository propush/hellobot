package com.mindcode.hellobot.configuration

import com.mindcode.hellobot.bot.service.HelloBotService
import org.mockito.kotlin.mock
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class HelloBotServiceConfiguration {

    @Bean
    fun helloBotService() = mock<HelloBotService> {}

}
