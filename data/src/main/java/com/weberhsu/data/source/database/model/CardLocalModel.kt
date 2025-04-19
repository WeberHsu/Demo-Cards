package com.weberhsu.data.source.database.model

import androidx.room.ColumnInfo
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
    val cvv: String,
    @ColumnInfo(name = "sort", defaultValue = "0")
    val sort: Int = 0,
    @ColumnInfo(name = "isFavorite", defaultValue = "0")
    val isFavorite: Boolean = false
)