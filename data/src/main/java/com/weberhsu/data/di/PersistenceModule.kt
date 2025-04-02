package com.weberhsu.data.di

import android.content.Context
import com.weberhsu.data.source.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * author : weber
 * desc : Module that holds database related classes
 */
@Module
@InstallIn(SingletonComponent::class)
object PersistenceModule {
    /**
     * Provides [AppDatabase] instance
     */
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    /**
     * Provides [CardItemDao] instance
     */
    @Provides
    @Singleton
    fun provideCardItemDAO(appDatabase: AppDatabase) = appDatabase.cardItemDao()

}