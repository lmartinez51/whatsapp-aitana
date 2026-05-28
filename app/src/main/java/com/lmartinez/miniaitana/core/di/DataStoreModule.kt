package com.lmartinez.miniaitana.core.di

import android.content.Context
import com.lmartinez.miniaitana.core.data.datastore.AppConfigDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Singleton
    @Provides
    fun provideAppConfigDataStore(@ApplicationContext context: Context): AppConfigDataStore =
        AppConfigDataStore(context)
}
