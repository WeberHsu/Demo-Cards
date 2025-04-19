package com.weberhsu.domain.repository

import com.weberhsu.domain.entity.CardEntity

/**
 * author : weber
 * desc :
 */
interface CardRepository {
    suspend fun getAllCard(): List<CardEntity>

    suspend fun addCard(card: CardEntity)

    suspend fun getCard(id: String): CardEntity

    suspend fun deleteCard(id: String)

    suspend fun updateCardNumber(id: String, cardNumber: String)

    suspend fun updateCardName(id: String, cardName: String)

    suspend fun updateCardExpiryDate(id: String, month: String, year: String)

    suspend fun updateCardCvv(id: String, cvv: String)

    suspend fun updateCardUserName(id: String, name: String)

    suspend fun updateCardIsFavorite(id: String, isFavorite: Boolean)

    suspend fun updateCards(cards: List<CardEntity>)
}