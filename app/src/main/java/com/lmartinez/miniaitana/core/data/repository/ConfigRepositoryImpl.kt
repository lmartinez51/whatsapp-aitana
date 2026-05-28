package com.lmartinez.miniaitana.core.data.repository

import android.content.ComponentName
import android.content.Context
import android.service.quicksettings.TileService
import com.lmartinez.miniaitana.core.data.datastore.AppConfigDataStore
import com.lmartinez.miniaitana.core.domain.model.AppConfig
import com.lmartinez.miniaitana.core.domain.repository.ConfigRepository
import com.lmartinez.miniaitana.service.engine.AutoPilotTileService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ConfigRepositoryImpl @Inject constructor(
    private val dataStore: AppConfigDataStore,
    @ApplicationContext private val context: Context,
) : ConfigRepository {

    override fun getAppConfig(): Flow<AppConfig> = dataStore.appConfigFlow

    override suspend fun updateSystemPrompt(prompt: String) =
        dataStore.updateSystemPrompt(prompt)

    override suspend fun updateModelFilePath(path: String) =
        dataStore.updateModelFilePath(path)

    override suspend fun updateInferenceThreads(threads: Int) =
        dataStore.updateInferenceThreads(threads)

    override suspend fun updateMaxReplyTokens(tokens: Int) =
        dataStore.updateMaxReplyTokens(tokens)

    override suspend fun updateMaxHistoryExchanges(limit: Int) =
        dataStore.updateMaxHistoryExchanges(limit)

    override suspend fun setServiceEnabled(enabled: Boolean) {
        dataStore.setServiceEnabled(enabled)
        TileService.requestListeningState(
            context,
            ComponentName(context, AutoPilotTileService::class.java)
        )
    }

    override suspend fun setEngineOwner(owner: String) =
        dataStore.setEngineOwner(owner)

    override suspend fun clearEngineOwner() =
        dataStore.clearEngineOwner()
}
