package com.lmartinez.miniaitana.service.engine

import android.content.Context
import com.google.ai.edge.litertlm.Backend
import com.google.ai.edge.litertlm.Contents
import com.google.ai.edge.litertlm.Conversation
import com.google.ai.edge.litertlm.ConversationConfig
import com.google.ai.edge.litertlm.Engine
import com.google.ai.edge.litertlm.EngineConfig
import com.google.ai.edge.litertlm.ExperimentalApi
import com.google.ai.edge.litertlm.SamplerConfig
import com.lmartinez.miniaitana.core.common.AutoPilotReplySanitizer
import com.lmartinez.miniaitana.core.common.MiniAitanaPromptRouter
import com.lmartinez.miniaitana.core.domain.model.AppConfig
import com.lmartinez.miniaitana.core.domain.model.ConversationMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.Closeable
import java.io.File

class MiniAitanaLiteRtEngine(
    private val context: Context,
    private val lease: AiEngineLeaseManager.Lease
) : Closeable {
    private var engine: Engine? = null
    private var conversation: Conversation? = null

    @OptIn(ExperimentalApi::class)
    suspend fun generateReply(
        config: AppConfig,
        sender: String,
        message: String,
        historyExchanges: List<ConversationMessage>
    ): String = withContext(Dispatchers.IO) {
        if (!lease.isValid) {
            throw IllegalStateException("AI engine lease is not valid.")
        }

        val modelFile = File(config.modelFilePath)
        if (!modelFile.exists()) {
            throw IllegalStateException("Configured model file does not exist: ${config.modelFilePath}")
        }

        try {
            val engineConfig = EngineConfig(
                modelPath = config.modelFilePath,
                backend = Backend.CPU(),
                audioBackend = Backend.CPU(),
                visionBackend = Backend.CPU(),
                cacheDir = context.cacheDir.path
            )
            val loadedEngine = Engine(engineConfig)
            engine = loadedEngine
            loadedEngine.initialize()
            lease.heartbeat()

            val configForConversation = ConversationConfig(
                systemInstruction = Contents.of(
                    MiniAitanaPromptRouter.buildSystemInstruction(
                        basePrompt = config.systemPrompt,
                        sender = sender
                    )
                ),
                samplerConfig = SamplerConfig(
                    topK = 40,
                    topP = 0.85,
                    temperature = 0.7
                ),
                tools = emptyList()
            )
            val activeConversation = loadedEngine.createConversation(configForConversation)
            conversation = activeConversation

            val buffer = StringBuilder()
            activeConversation.sendMessageAsync(
                MiniAitanaPromptRouter.buildRuntimePrompt(
                    sender = sender,
                    message = message,
                    historyExchanges = historyExchanges
                )
            )
                .collect { token ->
                    buffer.append(token.toString())
                }

            return@withContext AutoPilotReplySanitizer.clean(buffer.toString())
        } finally {
            try {
                conversation?.close()
            } finally {
                conversation = null
            }
        }
    }

    override fun close() {
        try {
            conversation?.close()
        } catch (_: Exception) {
        } finally {
            conversation = null
        }

        try {
            engine?.close()
        } finally {
            engine = null
            lease.close()
        }
    }
}
