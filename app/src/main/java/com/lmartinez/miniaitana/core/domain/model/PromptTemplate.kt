package com.lmartinez.miniaitana.core.domain.model

/**
 * Persisted reusable system prompt owned by the Prompt Vault.
 */
data class PromptTemplate(
    val id: Long = 0,
    val title: String,
    val content: String,
)
