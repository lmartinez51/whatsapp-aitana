package com.lmartinez.miniaitana.core.domain.repository

import com.lmartinez.miniaitana.core.domain.model.ConversationMessage
import kotlinx.coroutines.flow.Flow

interface ConversationLogRepository {
    fun getMessagesForContact(normalizedName: String): Flow<List<ConversationMessage>>
    fun getRecentMessagesForContact(
        normalizedName: String,
        limit: Int
    ): Flow<List<ConversationMessage>>
    fun getRecentMessages(limit: Int = 50): Flow<List<ConversationMessage>>
    suspend fun insertMessage(message: ConversationMessage)
    suspend fun deleteMessagesForContact(normalizedName: String)
    suspend fun clearAll()
}
