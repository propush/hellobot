package com.mindcode.hellobot.localization.service

import com.mindcode.hellobot.localization.exception.LocalizationNotFoundException
import com.mindcode.hellobot.localization.message.Messages
import com.mindcode.hellobot.localization.repository.LocalizationRepository
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.mockito.kotlin.*

internal class LocalizationServiceTest {

    private lateinit var localizationRepository: LocalizationRepository
    private lateinit var localizationService: LocalizationService

    @BeforeEach
    fun setUp() {
        localizationRepository = mock {
            on {
                getLocalizedMessage(
                    any(), eq("ru")
                )
            } doAnswer { invocationOnMock ->
                invocationOnMock.getArgument<Messages>(0).name + "_" + invocationOnMock.getArgument(1)
            }
            on {
                getLocalizedMessage(
                    any(), eq("en")
                )
            } doAnswer { invocationOnMock ->
                invocationOnMock.getArgument<Messages>(0).name + "_" + invocationOnMock.getArgument(1)
            }
            on {
                getLocalizedMessage(
                    eq(Messages.UNDEFINED), any()
                )
            } doReturn null
            on { languageDefined(eq("ru")) } doReturn true
            on { languageDefined(eq("en")) } doReturn true
            on { languageDefined(eq("fr")) } doReturn false
        }
        localizationService = LocalizationService(localizationRepository)
    }

    @Test
    fun getLocalizedMessage() {
        assertEquals(
            "WELCOME_USER_ru",
            localizationService.getLocalizedMessage(Messages.WELCOME_USER, "ru")
        )
    }

    @Test
    fun getLocalizedMessageNotFound() {
        assertThrows(LocalizationNotFoundException::class.java) {
            localizationService.getLocalizedMessage(Messages.UNDEFINED, "ru")
        }
    }

    @Test
    fun getLocalizedMessageUndefinedLanguage() {
        assertEquals(
            "WELCOME_USER_en",
            localizationService.getLocalizedMessage(Messages.WELCOME_USER, "en")
        )
    }
}
