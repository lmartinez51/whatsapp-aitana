package com.lmartinez.miniaitana.core.domain.usecase

import com.lmartinez.miniaitana.core.domain.model.AppConfig
import com.lmartinez.miniaitana.core.domain.repository.ConfigRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ConfigUseCase @Inject constructor(
    private val configRepository: ConfigRepository
) {
    fun getConfig(): Flow<AppConfig> = configRepository.getAppConfig()

    suspend fun updateModelPath(path: String) {
        configRepository.updateModelFilePath(path)
    }

    suspend fun updateSystemPrompt(prompt: String) {
        configRepository.updateSystemPrompt(prompt)
    }

    suspend fun updateMaxHistoryExchanges(limit: Int) {
        configRepository.updateMaxHistoryExchanges(limit)
    }

    suspend fun setServiceEnabled(enabled: Boolean) {
        configRepository.setServiceEnabled(enabled)
    }
}
