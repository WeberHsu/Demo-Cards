package com.weberhsu.data.source.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.weberhsu.data.source.database.model.CardLocalModel

@Dao
interface CardItemDao {
    @Query("DELETE FROM cards where id = :id")
    suspend fun deleteCard(id: String)

    @Transaction
    @Query("SELECT * FROM cards where id = :id")
    suspend fun getCardById(id: String): CardLocalModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(card: CardLocalModel)

    @Query("SELECT * FROM cards ORDER BY `sort` ASC")
    suspend fun getAllCards(): List<CardLocalModel>

    @Query("UPDATE cards SET cardName = :name WHERE id = :id")
    suspend fun updateCardName(id: String, name: String?)

    @Query("UPDATE cards SET cardNumber = :cardNumber WHERE id = :id")
    suspend fun updateCardNumber(id: String, cardNumber: String?)

    @Query("UPDATE cards SET expiryMonth = :month, expiryYear = :year WHERE id = :id")
    suspend fun updateCardExpiryDate(id: String, month: String?, year: String?)

    @Query("UPDATE cards SET cvv = :cardCvv WHERE id = :id")
    suspend fun updateCardCvv(id: String, cardCvv: String?)

    @Query("UPDATE cards SET userName = :name WHERE id = :id")
    suspend fun updateCardUserName(id: String, name: String?)

    @Query("UPDATE cards SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateCardIsFavorite(id: String, isFavorite: Boolean)

    @Update
    suspend fun updateCards(cards: List<CardLocalModel>)
}