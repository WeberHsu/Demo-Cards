package com.weberhsu.data.source.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cards")
data class CardLocalModel(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val userName: String,
    val cardName: String,
    val cardNumber: String,
    val expiryMonth: String,
    val expiryYear: String,
    val cvv: String
)