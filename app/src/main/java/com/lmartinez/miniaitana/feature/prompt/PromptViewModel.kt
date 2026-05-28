package com.lmartinez.miniaitana.feature.prompt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lmartinez.miniaitana.core.domain.model.AppConfig
import com.lmartinez.miniaitana.core.domain.model.PromptTemplate
import com.lmartinez.miniaitana.core.domain.usecase.ConfigUseCase
import com.lmartinez.miniaitana.core.domain.usecase.PromptTemplateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PromptViewModel @Inject constructor(
    private val configUseCase: ConfigUseCase,
    private val promptTemplateUseCase: PromptTemplateUseCase,
) : ViewModel() {

    val systemPrompt = configUseCase.getConfig()
        .map { it.systemPrompt }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    val promptTemplates = promptTemplateUseCase.getTemplates()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val factoryDefaultPrompt: String = AppConfig.DEFAULT_SYSTEM_PROMPT

    fun updatePrompt(prompt: String) {
        viewModelScope.launch {
            configUseCase.updateSystemPrompt(prompt)
        }
    }

    fun loadFactoryDefaultPrompt() {
        updatePrompt(factoryDefaultPrompt)
    }

    fun createTemplate(title: String, content: String) {
        viewModelScope.launch {
            promptTemplateUseCase.createTemplate(title, content)
        }
    }

    fun updateTemplate(id: Long, title: String, content: String) {
        viewModelScope.launch {
            promptTemplateUseCase.updateTemplate(
                PromptTemplate(
                    id = id,
                    title = title,
                    content = content,
                )
            )
        }
    }

    fun deleteTemplate(id: Long) {
        viewModelScope.launch {
            promptTemplateUseCase.deleteTemplate(id)
        }
    }
}
