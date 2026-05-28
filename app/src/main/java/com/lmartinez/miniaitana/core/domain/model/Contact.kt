package com.lmartinez.miniaitana.core.domain.model

/**
 * Domain model for a WhatsApp contact in the Auto-Pilot whitelist.
 * @param normalizedName Lowercased, trimmed, emoji-free key for whitelist lookups.
 */
data class Contact(
    val id: Long = 0,
    val normalizedName: String,
    val displayName: String,
    val isWhitelisted: Boolean = false,
)
