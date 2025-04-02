package com.weberhsu.data.di

import com.weberhsu.base.Mapper
import com.weberhsu.data.source.database.CardDataModel
import com.weberhsu.data.mapper.CardDataEntityMapper
import com.weberhsu.data.mapper.CardLocalDataMapper
import com.weberhsu.data.source.database.model.CardLocalModel
import com.weberhsu.domain.entity.CardEntity
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * author : weber
 * desc :
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class MapperModule {
    @Binds
    abstract fun bindsCryptoDataEntityMapper(mapper: CardDataEntityMapper): Mapper<CardDataModel, CardEntity>

    @Binds
    abstract fun bindsCryptoLocalDataMapper(mapper: CardLocalDataMapper): Mapper<CardLocalModel, CardDataModel>
}

