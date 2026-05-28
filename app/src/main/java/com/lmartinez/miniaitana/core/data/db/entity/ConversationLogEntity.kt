package com.lmartinez.miniaitana.core.data.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "conversation_logs",
    indices = [Index(value = ["contactNormalizedName"])]
)
data class ConversationLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val contactNormalizedName: String,
    val userText: String,
    val aiReply: String,
    val timestamp: Long,
)
