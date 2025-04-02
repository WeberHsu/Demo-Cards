package com.weberhsu.data.mapper

import com.weberhsu.base.Mapper
import com.weberhsu.data.source.database.CardDataModel
import com.weberhsu.data.source.database.model.CardLocalModel
import javax.inject.Inject

/**
 * author : weber
 * desc :
 */
class CardLocalDataMapper @Inject constructor() : Mapper<CardLocalModel, CardDataModel> {
    override fun from(i: CardLocalModel): CardDataModel {
        return CardDataModel(
            id = i.id,
            userName = i.userName,
            cardName = i.cardName,
            cardNumber = i.cardNumber,
            expiryMonth = i.expiryMonth,
            expiryYear = i.expiryYear,
            cvv = i.cvv
        )
    }

    override fun to(o: CardDataModel): CardLocalModel {
        return CardLocalModel(
            id = o.id,
            userName = o.userName,
            cardName = o.cardName,
            cardNumber = o.cardNumber,
            expiryMonth = o.expiryMonth,
            expiryYear = o.expiryYear,
            cvv = o.cvv
        )
    }
}