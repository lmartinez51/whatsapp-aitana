package com.lmartinez.miniaitana.core.data.db.dao

import androidx.room.*
import com.lmartinez.miniaitana.core.data.db.entity.ConversationLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ConversationLogDao {

    @Query("""
        SELECT * FROM conversation_logs
        WHERE contactNormalizedName = :normalizedName
        ORDER BY timestamp DESC
    """)
    fun getMessagesForContact(normalizedName: String): Flow<List<ConversationLogEntity>>

    @Query("""
        SELECT * FROM conversation_logs
        WHERE contactNormalizedName = :normalizedName
        ORDER BY timestamp DESC
        LIMIT :limit
    """)
    fun getRecentMessagesForContact(
        normalizedName: String,
        limit: Int
    ): Flow<List<ConversationLogEntity>>

    @Query("SELECT * FROM conversation_logs ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentMessages(limit: Int): Flow<List<ConversationLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(message: ConversationLogEntity)

    @Query("DELETE FROM conversation_logs WHERE contactNormalizedName = :normalizedName")
    suspend fun deleteForContact(normalizedName: String)

    @Query("DELETE FROM conversation_logs")
    suspend fun clearAll()
}
