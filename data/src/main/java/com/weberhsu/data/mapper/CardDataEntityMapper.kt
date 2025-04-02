package com.weberhsu.data.mapper

import com.weberhsu.base.Mapper
import com.weberhsu.data.source.database.CardDataModel
import com.weberhsu.domain.entity.CardEntity
import javax.inject.Inject

/**
 * author : weber
 * desc :
 */
class CardDataEntityMapper @Inject constructor() : Mapper<CardDataModel, CardEntity> {
    override fun from(i: CardDataModel): CardEntity {
        return CardEntity(
            id = i.id?.toString().orEmpty(),
            userName = i.userName,
            cardName = i.cardName,
            cardNumber = i.cardNumber,
            expiryMonth = i.expiryMonth,
            expiryYear = i.expiryYear,
            cvv = i.cvv
        )
    }

    override fun to(o: CardEntity): CardDataModel {
        return CardDataModel(
            id = o.id?.toInt(),
            userName = o.userName,
            cardName = o.cardName,
            cardNumber = o.cardNumber,
            expiryMonth = o.expiryMonth,
            expiryYear = o.expiryYear,
            cvv = o.cvv
        )
    }
}