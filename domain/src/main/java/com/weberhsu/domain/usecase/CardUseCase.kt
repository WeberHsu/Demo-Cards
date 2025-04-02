package com.weberhsu.domain.usecase

import com.weberhsu.domain.entity.CardEntity
import com.weberhsu.domain.repository.CardRepository
import javax.inject.Inject

/**
 * author : weber
 * desc :
 */
class CardUseCase @Inject constructor(
    private val repo: CardRepository,
) {

    suspend fun getAllCard(): List<CardEntity> {
        return repo.getAllCard()
    }

    suspend fun addCard(card: CardEntity) {
        repo.addCard(card)
    }

    suspend fun getCard(id: String): CardEntity {
        return repo.getCard(id)
    }

    suspend fun deleteCard(id: String) {
        repo.deleteCard(id)
    }

    suspend fun updateCardNumber(id: String, cardNumber: String) {
        repo.updateCardNumber(id, cardNumber)
    }

    suspend fun updateCardName(id: String, cardName: String) {
        repo.updateCardName(id, cardName)
    }

    suspend fun updateCardExpiryDate(id: String, month: String, year: String) {
        repo.updateCardExpiryDate(id, month, year)
    }

    suspend fun updateCardCvv(id: String, cvv: String) {
        repo.updateCardCvv(id, cvv)
    }

    suspend fun updateCardUserName(id: String, name: String) {
        repo.updateCardUserName(id, name)
    }
}