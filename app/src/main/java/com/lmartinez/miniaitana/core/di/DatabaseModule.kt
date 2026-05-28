package com.lmartinez.miniaitana.core.di

import android.content.Context
import androidx.room.Room
import com.lmartinez.miniaitana.core.data.db.AppDatabase
import com.lmartinez.miniaitana.core.data.db.dao.ContactDao
import com.lmartinez.miniaitana.core.data.db.dao.ConversationLogDao
import com.lmartinez.miniaitana.core.data.db.dao.PromptTemplateDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, AppDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()

    @Provides
    fun provideContactDao(db: AppDatabase): ContactDao = db.contactDao()

    @Provides
    fun provideConversationLogDao(db: AppDatabase): ConversationLogDao = db.conversationLogDao()

    @Provides
    fun providePromptTemplateDao(db: AppDatabase): PromptTemplateDao = db.promptTemplateDao()
}
