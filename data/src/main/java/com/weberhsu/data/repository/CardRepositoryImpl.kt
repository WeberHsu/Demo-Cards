package com.weberhsu.data.repository

import com.weberhsu.base.Mapper
import com.weberhsu.data.source.database.CardDataModel
import com.weberhsu.data.repository.dataSource.LocalCardDataSource
import com.weberhsu.domain.entity.CardEntity
import com.weberhsu.domain.repository.CardRepository
import javax.inject.Inject

class CryptoRepositoryImpl @Inject constructor(
    private val localDataSource: LocalCardDataSource,
    private val cardMapper: Mapper<CardDataModel, CardEntity>,
) : CardRepository {

    override suspend fun getAllCard(): List<CardEntity> {
        return localDataSource.getAllCards().map { cardMapper.from(it) }
    }

    override suspend fun addCard(card: CardEntity) {
        localDataSource.addCard(cardMapper.to(card))
    }

    override suspend fun getCard(id: String): CardEntity {
        return cardMapper.from(localDataSource.getCardById(id))
    }

    override suspend fun deleteCard(id: String) {
        localDataSource.deleteCard(id)
    }

    override suspend fun updateCardNumber(id: String, cardNumber: String) {
        localDataSource.updateCardNumber(id, cardNumber)
    }

    override suspend fun updateCardName(id: String, cardName: String) {
        localDataSource.updateCardName(id, cardName)
    }

    override suspend fun updateCardExpiryDate(id: String, month: String, year: String) {
        localDataSource.updateCardExpiryDate(id, month, year)
    }

    override suspend fun updateCardCvv(id: String, cvv: String) {
        localDataSource.updateCardCvv(id, cvv)
    }

    override suspend fun updateCardUserName(id: String, name: String) {
        localDataSource.updateCardUserName(id, name)
    }
}