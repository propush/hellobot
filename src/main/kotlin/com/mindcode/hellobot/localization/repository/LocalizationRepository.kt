package com.mindcode.hellobot.localization.repository

import com.fasterxml.jackson.databind.ObjectMapper
import com.mindcode.hellobot.localization.entity.Translations
import com.mindcode.hellobot.localization.message.Messages
import org.slf4j.LoggerFactory
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

@Service
class LocalizationRepository(private val objectMapper: ObjectMapper) {

    private val log = LoggerFactory.getLogger(this::class.java)

    private lateinit var translations: Translations

    companion object {
        private const val MESSAGE_FILE = "messages/messages.json"
    }

    @PostConstruct
    fun init() {
        log.info("Reading translations")
        translations = objectMapper.readValue(
            ClassPathResource(MESSAGE_FILE).inputStream,
            Translations::class.java
        )
    }

    fun getLocalizedMessage(message: Messages, languageCode: String): String? =
        translations.localizedMessages[languageCode]?.messages?.get(message.name)

    fun languageDefined(languageCode: String): Boolean = translations.localizedMessages.containsKey(languageCode)

}
