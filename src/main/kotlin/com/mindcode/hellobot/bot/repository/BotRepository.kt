package com.mindcode.hellobot.bot.repository

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.mindcode.hellobot.configuration.HelloBotConfiguration
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.File
import javax.annotation.PostConstruct

@Service
class BotRepository(
    private val objectMapper: ObjectMapper,
    private val helloBotConfiguration: HelloBotConfiguration
) {

    companion object {
        private const val usersFileName = "users.json"
    }

    private val log = LoggerFactory.getLogger(this::class.java)

    private val usersFile = File("""${helloBotConfiguration.persistPath}/$usersFileName""")
    private val userChatIds: MutableMap<String, Long> by lazy(::loadUsers)

    @PostConstruct
    fun init() {
        log.info("Initializing bot repository")
        File(helloBotConfiguration.persistPath).mkdirs()
    }

    private fun loadUsers(): MutableMap<String, Long> =
        try {
            objectMapper
                .readValue<MutableMap<String, Long>>(usersFile)
                .also {
                    log.info("Users loaded from file")
                }
        } catch (e: Exception) {
            mutableMapOf<String, Long>()
                .also {
                    log.info("Users not loaded: $e")
                }
        }

    fun saveUser(userName: String, chatId: Long) {
        synchronized(userChatIds) {
            userChatIds[userName] = chatId
            persistUsersToFile()
        }
    }

    private fun persistUsersToFile() {
        usersFile.writeText(objectMapper.writeValueAsString(userChatIds))
    }

    fun findChatIdByUserName(userName: String): Long? = userChatIds[userName]

}
