package com.lmartinez.miniaitana.core.di

import com.lmartinez.miniaitana.core.data.repository.ConfigRepositoryImpl
import com.lmartinez.miniaitana.core.data.repository.ContactRepositoryImpl
import com.lmartinez.miniaitana.core.data.repository.ConversationLogRepositoryImpl
import com.lmartinez.miniaitana.core.data.repository.PromptTemplateRepositoryImpl
import com.lmartinez.miniaitana.core.domain.repository.ConfigRepository
import com.lmartinez.miniaitana.core.domain.repository.ContactRepository
import com.lmartinez.miniaitana.core.domain.repository.ConversationLogRepository
import com.lmartinez.miniaitana.core.domain.repository.PromptTemplateRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindContactRepository(impl: ContactRepositoryImpl): ContactRepository

    @Singleton
    @Binds
    abstract fun bindConfigRepository(impl: ConfigRepositoryImpl): ConfigRepository

    @Singleton
    @Binds
    abstract fun bindConversationLogRepository(
        impl: ConversationLogRepositoryImpl,
    ): ConversationLogRepository

    @Singleton
    @Binds
    abstract fun bindPromptTemplateRepository(
        impl: PromptTemplateRepositoryImpl,
    ): PromptTemplateRepository
}
