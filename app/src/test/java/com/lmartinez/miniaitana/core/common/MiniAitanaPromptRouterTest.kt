package com.lmartinez.miniaitana.core.common

import com.lmartinez.miniaitana.core.domain.model.ConversationMessage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MiniAitanaPromptRouterTest {
    @Test
    fun modeForTreatsLorenzoSenderAsDirectBoss() {
        assertEquals(
            MiniAitanaPromptRouter.ReplyMode.DirectToLorenzo,
            MiniAitanaPromptRouter.modeFor("Lorenzo Martinez")
        )
    }

    @Test
    fun buildSystemInstructionForLorenzoContainsOnlyDirectRole() {
        val instruction = MiniAitanaPromptRouter.buildSystemInstruction(
            basePrompt = "Base persona.",
            sender = "Lorenzo"
        )

        assertTrue(instruction.contains("## DYNAMIC ROUTER DIRECTIVE"))
        assertTrue(instruction.contains("SENDER: Lorenzo (your employer)"))
        assertTrue(instruction.contains("theatrical exasperation"))
        assertFalse(instruction.contains("SENDER: Not Lorenzo"))
        assertFalse(instruction.contains("one excuse for why Lorenzo cannot respond"))
    }

    @Test
    fun buildSystemInstructionForOtherSenderContainsOnlyProxyRole() {
        val instruction = MiniAitanaPromptRouter.buildSystemInstruction(
            basePrompt = "Base persona.",
            sender = "Ana"
        )

        assertTrue(instruction.contains("## DYNAMIC ROUTER DIRECTIVE"))
        assertTrue(instruction.contains("SENDER: Not Lorenzo"))
        assertTrue(instruction.contains("one excuse for why Lorenzo cannot respond right now"))
        assertFalse(instruction.contains("SENDER: Lorenzo (your employer)"))
        assertFalse(instruction.contains("theatrical exasperation"))
    }

    @Test
    fun buildSystemInstructionSeparatesBasePromptAndDynamicDirectiveWithBlankLine() {
        val instruction = MiniAitanaPromptRouter.buildSystemInstruction(
            basePrompt = "Base persona.",
            sender = "Ana"
        )

        assertTrue(instruction.contains("Base persona.\n\n## DYNAMIC ROUTER DIRECTIVE"))
    }

    @Test
    fun buildSystemInstructionStripsLegacyCaseRoutingBlock() {
        val legacyPrompt = """
            Persona stays.

            ## Who is writing to Lorenzo?

            ### CASE A - The sender IS Lorenzo
            Old direct route.

            ### CASE B - The sender is ANYONE ELSE
            Old proxy route.

            ## Formatting rules
            Reply text only.
        """.trimIndent()

        val instruction = MiniAitanaPromptRouter.buildSystemInstruction(
            basePrompt = legacyPrompt,
            sender = "Ana"
        )

        assertTrue(instruction.contains("Persona stays."))
        assertTrue(instruction.contains("Reply text only."))
        assertFalse(instruction.contains("Old direct route."))
        assertFalse(instruction.contains("Old proxy route."))
    }

    @Test
    fun buildRuntimePromptFlattensHistoryAndCurrentMessage() {
        val prompt = MiniAitanaPromptRouter.buildRuntimePrompt(
            sender = "Ana",
            message = "Puedes confirmar?\nPorfa",
            historyExchanges = listOf(
                ConversationMessage(
                    contactNormalizedName = "ana",
                    userText = "Hola\nAitana",
                    aiReply = "Que paso?",
                    timestamp = 1L
                )
            )
        )

        assertTrue(prompt.contains("Recent conversation with this sender:"))
        assertTrue(prompt.contains("Sender: Hola Aitana"))
        assertTrue(prompt.contains("Aitana: Que paso?"))
        assertTrue(prompt.contains("Sender name: Ana"))
        assertTrue(prompt.contains("Message: Puedes confirmar? Porfa"))
        assertTrue(prompt.endsWith("Reply only with Aitana's WhatsApp text:"))
    }
}
