package com.mindcode.hellobot.bot.service

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.message
import com.github.kotlintelegrambot.dispatcher.telegramError
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.Message
import com.github.kotlintelegrambot.entities.User
import com.github.kotlintelegrambot.extensions.filters.Filter
import com.mindcode.hellobot.bot.repository.BotRepository
import com.mindcode.hellobot.configuration.HelloBotConfiguration
import com.mindcode.hellobot.localization.exception.LocalizationNotFoundException
import org.slf4j.LoggerFactory.getLogger
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct
import kotlin.system.exitProcess

@Service
@Profile("!test")
class HelloBotService(
    private val helloBotConfiguration: HelloBotConfiguration,
    private val botRepository: BotRepository,
    private val dialogService: DialogService
) {

    private val log = getLogger(this::class.java)

    private lateinit var bot: Bot

    @PostConstruct
    fun init() {
        bot = initBot()
    }

    private fun initBot(): Bot {
        log.debug("Initializing bot")
        checkConfiguration()
        return bot {
            token = helloBotConfiguration.token
            dispatch {

                command("start") {
                    log.debug("Received start command")
                    tryPersistAdminUser(message.from)
                    if (userIsAdmin(message.from)) {
                        sendAdminWelcomeMessage(bot, message.from)
                    } else {
                        sendUserWelcomeMessage(bot, message.from)
                    }
                }

                message(Filter.Command.not()) {
                    log.debug("Received message: $message")
                    if (isAdminResponse(message)) {
                        sendMessageToOriginalSender(bot, message)
                    }
                    sendMessageToAdmins(bot, message)
                }

                telegramError {
                    log.error("Telegram error: ${error.getErrorMessage()}")
                    exitProcess(1)
                }

            }
        }.apply {
            startPolling()
            log.info("Bot initialized")
        }
    }

    private fun sendUserWelcomeMessage(bot: Bot, user: User?) {
        log.debug("Sending user welcome message to $user")
        if (user == null) {
            log.error("User is null")
            return
        }
        try {
            sendMessage(bot, user, dialogService.getUserWelcomeMessage(user))
        } catch (e: LocalizationNotFoundException) {
            log.error("Localization not found: ${e.message}")
        }
    }

    private fun sendAdminWelcomeMessage(bot: Bot, user: User?) {
        log.debug("Sending admin welcome message to $user")
        if (user == null) {
            log.error("User is null")
            return
        }
        try {
            sendMessage(bot, user, dialogService.getAdminWelcomeMessage(user))
        } catch (e: LocalizationNotFoundException) {
            log.error("Localization not found: ${e.message}")
        }
    }

    private fun isAdminResponse(message: Message): Boolean =
        message.replyToMessage != null && userIsAdmin(message.from)

    private fun userIsAdmin(user: User?): Boolean =
        user?.username != null && helloBotConfiguration.admins.contains(user.username)

    private fun sendMessageToOriginalSender(bot: Bot, message: Message) {
        val originalSenderId = try {
            dialogService.getOriginalSenderId(message)
        } catch (e: Exception) {
            log.error("Original sender is null, not sending")
            return
        }
        val text = message.text
        if (text.isNullOrEmpty()) {
            log.warn("Received empty response message, not sending")
            return
        }
        log.info("Sending message to original sender $originalSenderId: $text")
        bot.sendMessage(ChatId.fromId(originalSenderId), text)
    }

    private fun tryPersistAdminUser(user: User?) {
        if (user == null) {
            log.error("User is null")
            return
        }
        if (user.username.isNullOrBlank()) {
            log.error("User has no username")
            return
        }
        val userName = user.username!!
        if (userIsAdmin(user)) {
            botRepository.saveUser(userName, user.id)
            log.info("Admin user $userName saved as ${user.id}")
        } else {
            log.info("User $userName is not admin, not saving")
        }
    }

    private fun sendMessageToAdmins(bot: Bot, message: Message) {
        helloBotConfiguration
            .admins
            .mapNotNull(::chatIdByUserName)
            .forEach {
                sendMessage(
                    bot,
                    ChatId.fromId(it),
                    "New message from [${message.from?.id}] ${dialogService.getUserName(message.from)} ${
                        dialogService.composeToName(message)
                    }: ${message.text}"
                )
            }
    }

    private fun chatIdByUserName(userName: String): Long? =
        botRepository.findChatIdByUserName(userName)

    private fun sendMessage(bot: Bot, chatId: ChatId, text: String) {
        log.info("Sending message to $chatId: $text")
        bot.sendMessage(chatId = chatId, text = text)
    }

    private fun sendMessage(bot: Bot, user: User, text: String) {
        log.info("Sending message to $user: $text")
        sendMessage(bot, ChatId.fromId(user.id), text)
    }

    private fun checkConfiguration() {
        log.debug("Configuration: $helloBotConfiguration")
        if (helloBotConfiguration.token.isBlank()) {
            log.error("Token is not set")
            throw IllegalArgumentException("bot.token is not set")
        }
        if (helloBotConfiguration.admins.isEmpty()) {
            log.error("Admins are not set")
            throw IllegalArgumentException("bot.admins are not set")
        }
    }
}
