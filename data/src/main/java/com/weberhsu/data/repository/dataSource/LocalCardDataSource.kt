package com.weberhsu.data.repository.dataSource

import com.weberhsu.data.source.database.CardDataModel

/**
 * author : weber
 * desc :
 */
interface LocalCardDataSource {

    suspend fun deleteCard(id: String)

    suspend fun getCardById(id: String): CardDataModel

    suspend fun getAllCards(): List<CardDataModel>

    suspend fun addCard(card: CardDataModel)

    suspend fun updateCardName(id: String, cardName: String)

    suspend fun updateCardNumber(id: String, cardNumber: String)

    suspend fun updateCardExpiryDate(id: String, month: String, year: String)

    suspend fun updateCardCvv(id: String, cvv: String)

    suspend fun updateCardUserName(id: String, name: String)

    suspend fun updateCardIsFavorite(id: String, isFavorite: Boolean)

    suspend fun updateCards(cards: List<CardDataModel>)
}