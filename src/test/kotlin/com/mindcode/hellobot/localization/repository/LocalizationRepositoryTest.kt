package com.mindcode.hellobot.localization.repository

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.mindcode.hellobot.localization.message.Messages
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.mockito.kotlin.*

internal class LocalizationRepositoryTest {

    private lateinit var localizationRepository: LocalizationRepository

    @BeforeEach
    fun setUp() {
        localizationRepository = LocalizationRepository(ObjectMapper().registerKotlinModule()).apply { init() }
    }

    @Test
    fun getLocalizedMessage() {
        assertEquals(
            "Hello, admin!",
            localizationRepository.getLocalizedMessage(Messages.WELCOME_ADMIN, "en")
        )
    }

    @Test
    fun getLocalizedMessageNotFound() {
        assertNull(
            localizationRepository.getLocalizedMessage(Messages.WELCOME_USER, "nonexistent")
        )
    }

    @Test
    fun languageDefined() {
        assertTrue(localizationRepository.languageDefined("ru"))
        assertTrue(localizationRepository.languageDefined("en"))
        assertFalse(localizationRepository.languageDefined("nonexistent"))
    }
}
