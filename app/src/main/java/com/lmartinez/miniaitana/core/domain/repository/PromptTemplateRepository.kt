package com.lmartinez.miniaitana.core.domain.repository

import com.lmartinez.miniaitana.core.domain.model.PromptTemplate
import kotlinx.coroutines.flow.Flow

interface PromptTemplateRepository {
    fun getPromptTemplates(): Flow<List<PromptTemplate>>
    suspend fun createPromptTemplate(template: PromptTemplate): Long
    suspend fun updatePromptTemplate(template: PromptTemplate)
    suspend fun deletePromptTemplate(id: Long)
}
