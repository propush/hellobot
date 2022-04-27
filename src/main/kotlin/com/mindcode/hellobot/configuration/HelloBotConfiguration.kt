package com.mindcode.hellobot.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "bot")
data class HelloBotConfiguration(
    var persistPath: String = "",
    var token: String = "",
    var admins: MutableSet<String>,
)
