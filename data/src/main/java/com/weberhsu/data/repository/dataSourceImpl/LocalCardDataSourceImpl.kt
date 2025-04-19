package com.weberhsu.data.repository.dataSourceImpl

import com.weberhsu.base.Mapper
import com.weberhsu.data.repository.dataSource.LocalCardDataSource
import com.weberhsu.data.source.database.CardDataModel
import com.weberhsu.data.source.database.dao.CardItemDao
import com.weberhsu.data.source.database.model.CardLocalModel
import javax.inject.Inject

/**
 * author : weber
 * desc :
 */
class LocalCardDataSourceImpl @Inject constructor(
    private val cardDao: CardItemDao,
    private val cardMapper: Mapper<CardLocalModel, CardDataModel>,
) : LocalCardDataSource {
    override suspend fun deleteCard(id: String) {
        cardDao.deleteCard(id)
    }

    override suspend fun getCardById(id: String): CardDataModel {
        return cardMapper.from(cardDao.getCardById(id))
    }

    override suspend fun getAllCards(): List<CardDataModel> {
        return cardDao.getAllCards().map { cardMapper.from(it) }
    }

    override suspend fun addCard(card: CardDataModel) {
        cardDao.insertCard(cardMapper.to(card))
    }

    override suspend fun updateCardName(id: String, cardName: String) {
        cardDao.updateCardName(id, cardName)
    }

    override suspend fun updateCardNumber(id: String, cardNumber: String) {
        cardDao.updateCardNumber(id, cardNumber)
    }

    override suspend fun updateCardExpiryDate(id: String, month: String, year: String) {
        cardDao.updateCardExpiryDate(id, month, year)
    }

    override suspend fun updateCardCvv(id: String, cvv: String) {
        cardDao.updateCardCvv(id, cvv)
    }

    override suspend fun updateCardUserName(id: String, name: String) {
        cardDao.updateCardUserName(id, name)
    }

    override suspend fun updateCardIsFavorite(id: String, isFavorite: Boolean) {
        cardDao.updateCardIsFavorite(id, isFavorite)
    }

    override suspend fun updateCards(cards: List<CardDataModel>) {
        cardDao.updateCards(cardMapper.toList(cards))
    }

}