package com.mindcode.hellobot.bot.service

import com.github.kotlintelegrambot.entities.Message
import com.github.kotlintelegrambot.entities.User
import com.mindcode.hellobot.localization.message.Messages
import com.mindcode.hellobot.localization.service.LocalizationService
import org.springframework.stereotype.Service

@Service
class DialogService(private val localizationService: LocalizationService) {

    fun extractIdFromOriginalText(originalText: String) =
        originalText
            .replaceBefore('[', "")
            .substring(1)
            .substringBefore(']')
            .toLong(10)

    fun extractNameFromOriginalText(originalText: String): String =
        originalText.substringAfter(']').substringBefore(':').trim()

    fun composeToName(message: Message): String {
        val originalText = message.replyToMessage?.text ?: return ""
        return "to [${extractIdFromOriginalText(originalText)}] ${extractNameFromOriginalText(originalText)}"
    }

    fun getUserName(user: User?): String =
        (getRealName(user) ?: "Unknown") + " (@${user?.username ?: "Unknown"})"

    fun getRealName(user: User?): String? {
        val firstName = user?.firstName
        val lastName = user?.lastName
        if (firstName.isNullOrEmpty() && lastName.isNullOrEmpty()) {
            return null
        }
        return "$firstName $lastName"
    }

    fun getOriginalSenderId(message: Message): Long {
        val originalText = message.replyToMessage?.text ?: throw IllegalArgumentException("Message is not a reply")
        return extractIdFromOriginalText(originalText)
    }

    fun getAdminWelcomeMessage(user: User): String {
        val languageCode = getUserLanguageCode(user)
        return localizationService.getLocalizedMessage(Messages.WELCOME_ADMIN, languageCode)
    }

    fun getUserWelcomeMessage(user: User): String {
        val languageCode = getUserLanguageCode(user)
        return localizationService.getLocalizedMessage(Messages.WELCOME_USER, languageCode)
    }

    private fun getUserLanguageCode(user: User) = user.languageCode

}
