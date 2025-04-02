package com.weberhsu.data.di

import com.weberhsu.data.repository.dataSource.LocalCardDataSource
import com.weberhsu.data.repository.dataSourceImpl.LocalCardDataSourceImpl
import com.weberhsu.data.repository.CryptoRepositoryImpl
import com.weberhsu.domain.repository.CardRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

/**
 * author : weber
 * desc :
 */
@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun provideLocalCardDataSource(localDataSourceImpl: LocalCardDataSourceImpl): LocalCardDataSource

    @Binds
    @ViewModelScoped
    abstract fun provideCardRepository(repository: CryptoRepositoryImpl): CardRepository
}