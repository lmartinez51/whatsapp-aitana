package com.lmartinez.miniaitana.core.data.repository

import com.lmartinez.miniaitana.core.data.db.dao.ConversationLogDao
import com.lmartinez.miniaitana.core.data.db.mapper.toDomain
import com.lmartinez.miniaitana.core.data.db.mapper.toEntity
import com.lmartinez.miniaitana.core.domain.model.ConversationMessage
import com.lmartinez.miniaitana.core.domain.repository.ConversationLogRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ConversationLogRepositoryImpl @Inject constructor(
    private val dao: ConversationLogDao,
) : ConversationLogRepository {

    override fun getMessagesForContact(normalizedName: String): Flow<List<ConversationMessage>> =
        dao.getMessagesForContact(normalizedName).map { list -> list.map { it.toDomain() } }

    override fun getRecentMessagesForContact(
        normalizedName: String,
        limit: Int
    ): Flow<List<ConversationMessage>> =
        dao.getRecentMessagesForContact(normalizedName, limit.coerceAtLeast(0))
            .map { list -> list.map { it.toDomain() } }

    override fun getRecentMessages(limit: Int): Flow<List<ConversationMessage>> =
        dao.getRecentMessages(limit).map { list -> list.map { it.toDomain() } }

    override suspend fun insertMessage(message: ConversationMessage) =
        dao.insert(message.toEntity())

    override suspend fun deleteMessagesForContact(normalizedName: String) =
        dao.deleteForContact(normalizedName)

    override suspend fun clearAll() = dao.clearAll()
}
