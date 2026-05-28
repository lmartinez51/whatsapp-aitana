package com.lmartinez.miniaitana.core.domain.repository

import com.lmartinez.miniaitana.core.domain.model.AppConfig
import kotlinx.coroutines.flow.Flow

interface ConfigRepository {
    fun getAppConfig(): Flow<AppConfig>
    suspend fun updateSystemPrompt(prompt: String)
    suspend fun updateModelFilePath(path: String)
    suspend fun updateInferenceThreads(threads: Int)
    suspend fun updateMaxReplyTokens(tokens: Int)
    suspend fun updateMaxHistoryExchanges(limit: Int)
    suspend fun setServiceEnabled(enabled: Boolean)
    suspend fun setEngineOwner(owner: String)
    suspend fun clearEngineOwner()
}
