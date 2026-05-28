package com.lmartinez.miniaitana.core.domain.model

/** Domain model for a single Auto-Pilot exchange logged per contact. */
data class ConversationMessage(
    val id: Long = 0,
    /** Matches [Contact.normalizedName]. */
    val contactNormalizedName: String,
    val userText: String,
    val aiReply: String,
    val timestamp: Long = System.currentTimeMillis(),
)
