package com.lmartinez.miniaitana.core.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.lmartinez.miniaitana.core.domain.model.AppConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_config")

@Singleton
class AppConfigDataStore @Inject constructor(private val context: Context) {

    private object Keys {
        val SYSTEM_PROMPT     = stringPreferencesKey("system_prompt")
        val MODEL_FILE_PATH   = stringPreferencesKey("model_file_path")
        val INFERENCE_THREADS = intPreferencesKey("inference_threads")
        val MAX_REPLY_TOKENS  = intPreferencesKey("max_reply_tokens")
        val MAX_HISTORY_EXCHANGES = intPreferencesKey("max_history_exchanges")
        val SERVICE_ENABLED   = booleanPreferencesKey("service_enabled")
        val ENGINE_OWNER      = stringPreferencesKey("engine_owner")
    }

    val appConfigFlow: Flow<AppConfig> = context.dataStore.data
        .catch { cause ->
            if (cause is IOException) emit(emptyPreferences()) else throw cause
        }
        .map { prefs ->
            AppConfig(
                systemPrompt     = prefs[Keys.SYSTEM_PROMPT]     ?: AppConfig.DEFAULT_SYSTEM_PROMPT,
                modelFilePath    = prefs[Keys.MODEL_FILE_PATH]   ?: "",
                inferenceThreads = prefs[Keys.INFERENCE_THREADS] ?: 4,
                maxReplyTokens   = prefs[Keys.MAX_REPLY_TOKENS]  ?: 256,
                maxHistoryExchanges = (
                    prefs[Keys.MAX_HISTORY_EXCHANGES]
                        ?: AppConfig.DEFAULT_MAX_HISTORY_EXCHANGES
                    ).coerceAtLeast(0),
                serviceEnabled   = prefs[Keys.SERVICE_ENABLED]   ?: false,
                engineOwner      = prefs[Keys.ENGINE_OWNER]      ?: "none",
            )
        }

    suspend fun updateSystemPrompt(prompt: String) = edit { it[Keys.SYSTEM_PROMPT] = prompt }
    suspend fun updateModelFilePath(path: String) = edit { it[Keys.MODEL_FILE_PATH] = path }
    suspend fun updateInferenceThreads(threads: Int) = edit { it[Keys.INFERENCE_THREADS] = threads }
    suspend fun updateMaxReplyTokens(tokens: Int) = edit { it[Keys.MAX_REPLY_TOKENS] = tokens }
    suspend fun updateMaxHistoryExchanges(limit: Int) =
        edit { it[Keys.MAX_HISTORY_EXCHANGES] = limit.coerceAtLeast(0) }
    suspend fun setServiceEnabled(enabled: Boolean) = edit { it[Keys.SERVICE_ENABLED] = enabled }
    
    suspend fun setEngineOwner(owner: String) = edit { it[Keys.ENGINE_OWNER] = owner }
    suspend fun clearEngineOwner() = edit { it[Keys.ENGINE_OWNER] = "none" }

    private suspend fun edit(transform: suspend (MutablePreferences) -> Unit) {
        context.dataStore.edit(transform)
    }
}
