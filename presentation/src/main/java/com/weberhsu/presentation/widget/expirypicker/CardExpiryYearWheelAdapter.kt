package com.weberhsu.presentation.widget.expirypicker

import com.weberhsu.presentation.widget.expirypicker.wheelview.adapter.WheelAdapter

class CardExpiryYearWheelAdapter(private val minValue: Int, private val maxValue: Int) : WheelAdapter<Any> {

    override fun getItem(index: Int): Any {
        return if (index in 0 until itemsCount && (minValue + index).toString().length > 2) {
            (minValue + index).toString()
                .substring(
                    (minValue + index).toString().length - 2,
                    (minValue + index).toString().length
                ) as Any
        } else 0
    }

    override val itemsCount: Int
        get() = maxValue - minValue + 1

    override fun indexOf(o: Any): Int {
        return try {
            o as Int - minValue
        } catch (e: Exception) {
            -1
        }
    }

}