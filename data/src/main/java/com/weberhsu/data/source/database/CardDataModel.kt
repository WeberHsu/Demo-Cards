package com.weberhsu.data.source.database

data class CardDataModel(
    val id: Int? = null,
    val userName: String, // name on card
    val cardName: String,
    val cardNumber: String,
    val expiryMonth: String,
    val expiryYear: String,
    val cvv: String
)