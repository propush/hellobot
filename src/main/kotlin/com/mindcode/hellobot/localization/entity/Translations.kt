package com.mindcode.hellobot.localization.entity

data class Translations(
    val localizedMessages: Map<String, LocalizedMessages>
)

data class LocalizedMessages(
    val messages: Map<String, String>
)
