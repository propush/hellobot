package com.mindcode.hellobot.bot.service

import com.github.kotlintelegrambot.entities.User
import com.mindcode.hellobot.localization.message.Messages
import com.mindcode.hellobot.localization.service.LocalizationService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

internal class DialogServiceTest {

    private lateinit var dialogService: DialogService
    private lateinit var localizationService: LocalizationService

    @BeforeEach
    fun setUp() {
        localizationService = mock {
            on {
                getLocalizedMessage(
                    any(),
                    any()
                )
            } doAnswer { invocationOnMock ->
                invocationOnMock.getArgument<Messages>(0).name +
                        "_" +
                        invocationOnMock.getArgument(1)
            }
        }
        dialogService = DialogService(localizationService)
    }

    @Test
    fun extractIdFromOriginalText() {
        assertEquals(
            290705193L,
            dialogService.extractIdFromOriginalText("New message from [290705193] Sergey Poziturin (@pushkin_kukushkin) : 1")
        )
        assertEquals(
            290705193L,
            dialogService.extractIdFromOriginalText("New message from [290705193] pushkin_kukushkin : 1")
        )
        assertEquals(
            290705193L,
            dialogService.extractIdFromOriginalText("New message from [290705193] Sergey Poziturin (@pushkin_kukushkin) : 1:1")
        )
        assertEquals(
            290705193L,
            dialogService.extractIdFromOriginalText("New message from [290705193] pushkin_kukushkin : 1:1")
        )
    }

    @Test
    fun extractNameFromOriginalText() {
        assertEquals(
            "Sergey Poziturin (@pushkin_kukushkin)",
            dialogService.extractNameFromOriginalText("New message from [290705193] Sergey Poziturin (@pushkin_kukushkin) : 1")
        )
        assertEquals(
            "pushkin_kukushkin",
            dialogService.extractNameFromOriginalText("New message from [290705193] pushkin_kukushkin : 1")
        )
        assertEquals(
            "Sergey Poziturin (@pushkin_kukushkin)",
            dialogService.extractNameFromOriginalText("New message from [290705193] Sergey Poziturin (@pushkin_kukushkin) : 1:1")
        )
        assertEquals(
            "pushkin_kukushkin",
            dialogService.extractNameFromOriginalText("New message from [290705193] pushkin_kukushkin : 1:1")
        )
    }

    @Test
    fun getAdminWelcomeMessage() {
        val user = User(
            id = 1L,
            isBot = false,
            firstName = "Test",
            lastName = "Test",
            languageCode = "ru"
        )
        assertEquals(
            "${Messages.WELCOME_ADMIN}_ru",
            dialogService.getAdminWelcomeMessage(user)
        )
    }

    @Test
    fun getUserWelcomeMessage() {
        val user = User(
            id = 1L,
            isBot = false,
            firstName = "Test",
            lastName = "Test",
            languageCode = "ru"
        )
        assertEquals(
            "${Messages.WELCOME_USER}_ru",
            dialogService.getUserWelcomeMessage(user)
        )
    }
}
