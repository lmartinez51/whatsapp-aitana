package com.lmartinez.miniaitana.core.data.db.mapper

import com.lmartinez.miniaitana.core.data.db.entity.ConversationLogEntity
import com.lmartinez.miniaitana.core.domain.model.ConversationMessage

fun ConversationLogEntity.toDomain(): ConversationMessage = ConversationMessage(
    id                    = id,
    contactNormalizedName = contactNormalizedName,
    userText              = userText,
    aiReply               = aiReply,
    timestamp             = timestamp,
)

fun ConversationMessage.toEntity(): ConversationLogEntity = ConversationLogEntity(
    id                    = id,
    contactNormalizedName = contactNormalizedName,
    userText              = userText,
    aiReply               = aiReply,
    timestamp             = timestamp,
)
