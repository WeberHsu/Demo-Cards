package com.weberhsu.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CardEntity(
    val id: String? = null,
    var userName: String,
    var cardName: String,
    var cardNumber: String,
    var expiryMonth: String,
    var expiryYear: String,
    var cvv: String,
    var sort: Int = 0,
    var isFavorite: Boolean = false
) : Parcelable {
    fun getExpiryDateString(): String = "$expiryMonth/$expiryYear"

    fun getSecretCardNumber(): String = "•••• ${cardNumber.takeLast(4)}"

    fun getPhysicalCardNumber(): String = "Physical • ${cardNumber.takeLast(4)}"
}