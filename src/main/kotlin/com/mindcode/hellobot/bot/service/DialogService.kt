package com.mindcode.hellobot.bot.service

import com.github.kotlintelegrambot.entities.Message
import com.github.kotlintelegrambot.entities.User
import org.springframework.stereotype.Service

@Service
class DialogService {

    fun extractIdFromOriginalText(originalText: String) =
        originalText
            .replaceBefore('[', "")
            .substring(1)
            .substringBefore(']')
            .toLong(10)

    fun extractNameFromOriginalText(originalText: String): String =
        originalText.substringAfter(']').substringBeforeLast(':').trim()

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

}
