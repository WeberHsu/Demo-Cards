package com.weberhsu.presentation.widget.expirypicker.wheelview

import com.weberhsu.presentation.widget.expirypicker.wheelview.adapter.WheelAdapter

class NumericWheelAdapter(private val minValue: Int, private val maxValue: Int) :
    com.weberhsu.presentation.widget.expirypicker.wheelview.adapter.WheelAdapter<Any> {
    override fun getItem(index: Int): Any {
        return if (index in 0 until itemsCount) {
            minValue + index
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