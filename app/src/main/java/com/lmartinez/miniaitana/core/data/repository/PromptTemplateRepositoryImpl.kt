package com.lmartinez.miniaitana.core.data.repository

import com.lmartinez.miniaitana.core.data.db.dao.PromptTemplateDao
import com.lmartinez.miniaitana.core.data.db.mapper.toDomain
import com.lmartinez.miniaitana.core.data.db.mapper.toEntity
import com.lmartinez.miniaitana.core.domain.model.PromptTemplate
import com.lmartinez.miniaitana.core.domain.repository.PromptTemplateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PromptTemplateRepositoryImpl @Inject constructor(
    private val dao: PromptTemplateDao,
) : PromptTemplateRepository {

    override fun getPromptTemplates(): Flow<List<PromptTemplate>> =
        dao.getAllTemplates().map { list -> list.map { it.toDomain() } }

    override suspend fun createPromptTemplate(template: PromptTemplate): Long =
        withContext(Dispatchers.IO) {
            dao.insert(template.toEntity())
        }

    override suspend fun updatePromptTemplate(template: PromptTemplate) {
        withContext(Dispatchers.IO) {
            dao.update(template.toEntity())
        }
    }

    override suspend fun deletePromptTemplate(id: Long) {
        withContext(Dispatchers.IO) {
            dao.deleteById(id)
        }
    }
}
