package com.mindcode.hellobot

import com.mindcode.hellobot.configuration.HelloBotConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(HelloBotConfiguration::class)
class HellobotApplication

fun main(args: Array<String>) {
    runApplication<HellobotApplication>(*args)
}
