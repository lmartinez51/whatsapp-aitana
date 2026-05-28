package com.lmartinez.miniaitana.core.domain.usecase

import com.lmartinez.miniaitana.core.domain.model.PromptTemplate
import com.lmartinez.miniaitana.core.domain.repository.PromptTemplateRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PromptTemplateUseCase @Inject constructor(
    private val promptTemplateRepository: PromptTemplateRepository,
) {
    fun getTemplates(): Flow<List<PromptTemplate>> =
        promptTemplateRepository.getPromptTemplates()

    suspend fun createTemplate(title: String, content: String) {
        val normalizedTitle = title.trim()
        if (normalizedTitle.isBlank() || content.isBlank()) return

        promptTemplateRepository.createPromptTemplate(
            PromptTemplate(
                title = normalizedTitle,
                content = content,
            )
        )
    }

    suspend fun updateTemplate(template: PromptTemplate) {
        val normalizedTitle = template.title.trim()
        if (template.id <= 0 || normalizedTitle.isBlank() || template.content.isBlank()) return

        promptTemplateRepository.updatePromptTemplate(
            template.copy(title = normalizedTitle)
        )
    }

    suspend fun deleteTemplate(id: Long) {
        if (id <= 0) return
        promptTemplateRepository.deletePromptTemplate(id)
    }
}
