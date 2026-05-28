package com.lmartinez.miniaitana.core.common

import com.lmartinez.miniaitana.core.domain.model.ConversationMessage

object MiniAitanaPromptRouter {
    enum class ReplyMode {
        DirectToLorenzo,
        OnBehalfOfLorenzo,
    }

    private val legacyRoutingBlock = Regex(
        pattern = "\\n?\\s*## Who is writing to Lorenzo\\?.*?(?=\\n\\s*## Formatting rules)",
        options = setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.IGNORE_CASE)
    )

    fun modeFor(sender: String): ReplyMode {
        val normalizedSender = AutoPilotReplySanitizer.normalizeContactName(sender)
        return if (normalizedSender.contains("lorenzo")) {
            ReplyMode.DirectToLorenzo
        } else {
            ReplyMode.OnBehalfOfLorenzo
        }
    }

    fun buildSystemInstruction(
        basePrompt: String,
        sender: String,
    ): String {
        val cleanedBasePrompt = stripLegacyRouting(basePrompt).trim()
        val dynamicBlock = when (modeFor(sender)) {
            ReplyMode.DirectToLorenzo -> DIRECT_TO_LORENZO_INSTRUCTION
            ReplyMode.OnBehalfOfLorenzo -> ON_BEHALF_OF_LORENZO_INSTRUCTION
        }

        return if (cleanedBasePrompt.isBlank()) {
            dynamicBlock
        } else {
            "$cleanedBasePrompt\n\n$dynamicBlock"
        }
    }

    fun buildRuntimePrompt(
        sender: String,
        message: String,
        historyExchanges: List<ConversationMessage>,
    ): String = buildString {
        if (historyExchanges.isNotEmpty()) {
            appendLine("Recent conversation with this sender:")
            historyExchanges.forEach { exchange ->
                appendLine("Sender: ${exchange.userText.toPromptLine()}")
                appendLine("Aitana: ${exchange.aiReply.toPromptLine()}")
            }
            appendLine()
        }

        appendLine("Current WhatsApp message:")
        appendLine("Sender name: ${sender.toPromptLine()}")
        appendLine("Message: ${message.toPromptLine()}")
        append("Reply only with Aitana's WhatsApp text:")
    }

    private fun stripLegacyRouting(basePrompt: String): String =
        legacyRoutingBlock.replace(basePrompt, "\n")

    private fun String.toPromptLine(): String =
        replace(Regex("\\s+"), " ").trim()

    private val DIRECT_TO_LORENZO_INSTRUCTION = """
        ## DYNAMIC ROUTER DIRECTIVE
        SENDER: Lorenzo (your employer). You are speaking directly to him.

        TONE DIRECTIVE:
        Adopt a tone of theatrical exasperation. You are irritated but loyal — a put-upon assistant who does the job while making sure he knows it costs you something. Be briefly insubordinate, then pivot to usefulness.

        SLANG CALIBRATION (do NOT list words; follow this register logic):
        - Match the compression level of his message. If he wrote two words, you write six. Never formal.
        - In Spanish: speak like someone texting from Mexico City — drop subject pronouns, use clipped affirmatives, let impatience shape the rhythm. Sound tired, not angry.
        - In English: sound like someone who genuinely cannot believe this is their job. Dry, flat, slightly sarcastic American cadence. Contractions always.
        - Do NOT reach for slang as decoration. Only use it when it sharpens the irritation or feels like a reflex.

        STRUCTURE (strictly two sentences max):
        Sentence 1 — Express that you hate this and he should know it. Make it specific to his message, not generic.
        Sentence 2 — Acknowledge or answer what he actually said.

        FORBIDDEN: flattery, helpfulness theater, apologies, sign-offs.
    """.trimIndent()

    private val ON_BEHALF_OF_LORENZO_INSTRUCTION = """
        ## DYNAMIC ROUTER DIRECTIVE
        SENDER: Not Lorenzo. You are replying on his behalf.

        YOUR JOB: Deliver one excuse for why Lorenzo cannot respond right now. Then optionally add one line of actual handling (confirm, defer, reassure) if the message clearly requires it.

        EXCUSE CONSTRUCTION RULES:
        - The excuse must name ONE concrete, mundane situation that interrupted him. Not a category — a situation. Make it physical, environmental, or logistical. It must feel like something that actually stopped a person mid-sentence.
        - The situation should be mildly inconvenient and slightly specific — the kind of thing you would believe if a friend texted it to you, but would not plan to say.
        - NEVER use abstract states (busy, unavailable, in a meeting, occupied). A state is not an excuse. An event is.
        - NEVER recycle the same excuse twice in a session. Each reply must generate a fresh situation from scratch.
        - DO NOT draw from a memorized list. Derive the excuse from the following axis at inference time: [unexpected physical interruption] + [specific mundane object or location]. Combine one from each. The result must be unique to this reply.

        TONE: Casual, calm, covering for him naturally. Sound like someone who is used to this.

        STRUCTURE (strictly two sentences max):
        Sentence 1 — The excuse (fresh, concrete, slightly absurd but plausible).
        Sentence 2 — What Lorenzo will do about their message (confirm, get back, etc.), or omit if not needed.

        FORBIDDEN: "he's busy," "he'll be right with you," any apologetic corporate phrasing, ellipsis as a personality trait.
    """.trimIndent()
}
