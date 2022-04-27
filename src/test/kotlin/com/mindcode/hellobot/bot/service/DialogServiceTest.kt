package com.mindcode.hellobot.bot.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class DialogServiceTest {

    private lateinit var dialogService: DialogService

    @BeforeEach
    fun setUp() {
        dialogService = DialogService()
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
    }
}
