package com.weberhsu.presentation.widget.expirypicker

import android.content.Context
import com.weberhsu.presentation.widget.expirypicker.listener.OnCardExpirySelectedListener
import java.util.*


class CardExpiryPickerBuilder(context: Context) : BasePickerViewBuilder(context) {

    fun addOnCardExpirySelectedListener(expiryListener: OnCardExpirySelectedListener): CardExpiryPickerBuilder? {
        mPickerOptions.cardExpirySelectedListener = expiryListener
        return this
    }

    fun setExpiryDate(date: Calendar): BasePickerViewBuilder {
        mPickerOptions.date = date
        return this
    }

    override fun build(): CardExpiryPickerView {
        return CardExpiryPickerView(
            mPickerOptions
        )
    }
}