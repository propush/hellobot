package com.mindcode.hellobot.bot.repository

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.mindcode.hellobot.configuration.HelloBotConfiguration
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

internal class BotRepositoryTest {

    companion object {
        private const val tmpPath = "/tmp/hellobottest/"
    }

    private lateinit var botRepository: BotRepository

    @BeforeEach
    fun setUp() {
        botRepository = BotRepository(
            ObjectMapper().registerKotlinModule(),
            HelloBotConfiguration(
                tmpPath,
                "token",
                mutableSetOf("admin1", "admin2")
            )
        ).apply(BotRepository::init)
    }

    @AfterEach
    fun tearDown() {
        File(tmpPath).deleteRecursively()
    }

    @Test
    fun saveUser() {
        botRepository.saveUser("user0", 0L)
        botRepository.saveUser("user1", 1L)
        assertEquals(
            """{"user0":0,"user1":1}""",
            File("/tmp/hellobottest/users.json").readText()
        )
    }

    @Test
    fun findChatIdByUserName() {
        botRepository.saveUser("user2", 2L)
        assertEquals(2L, botRepository.findChatIdByUserName("user2"))
    }

    @Test
    fun findChatIdByUserNameNonexistent() {
        assertNull(botRepository.findChatIdByUserName("nonexistent"))
    }
}
