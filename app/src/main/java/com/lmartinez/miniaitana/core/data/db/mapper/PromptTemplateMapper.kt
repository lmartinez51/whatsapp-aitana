package com.lmartinez.miniaitana.core.data.db.mapper

import com.lmartinez.miniaitana.core.data.db.entity.PromptTemplateEntity
import com.lmartinez.miniaitana.core.domain.model.PromptTemplate

fun PromptTemplateEntity.toDomain(): PromptTemplate = PromptTemplate(
    id = id,
    title = title,
    content = content,
)

fun PromptTemplate.toEntity(): PromptTemplateEntity = PromptTemplateEntity(
    id = id,
    title = title,
    content = content,
)
