package com.mindcode.hellobot.localization.service

import com.mindcode.hellobot.localization.exception.LocalizationNotFoundException
import com.mindcode.hellobot.localization.message.Messages
import com.mindcode.hellobot.localization.repository.LocalizationRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
class LocalizationService(private val localizationRepository: LocalizationRepository) {

    companion object {
        private const val DEFAULT_LANGUAGE_CODE = "en"
    }

    private val log = LoggerFactory.getLogger(this::class.java)

    @Throws(LocalizationNotFoundException::class)
    fun getLocalizedMessage(message: Messages, languageCode: String?): String {
        log.debug("Getting localized message for language: $languageCode, message code: $message")
        val lcLanguageCode = languageCode?.lowercase(Locale.getDefault()) ?: DEFAULT_LANGUAGE_CODE
        val realLanguageCode =
            if (localizationRepository.languageDefined(lcLanguageCode)) lcLanguageCode else DEFAULT_LANGUAGE_CODE
        return localizationRepository
            .getLocalizedMessage(message, realLanguageCode)
            ?: throw LocalizationNotFoundException("Localization not found for language: $languageCode, message code: $message")
    }

}
