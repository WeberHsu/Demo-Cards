package com.weberhsu.presentation.widget.expirypicker.wheelview.listener

import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import com.weberhsu.presentation.widget.expirypicker.wheelview.view.KitWheelView

class LoopViewGestureListener(private val wheelView: com.weberhsu.presentation.widget.expirypicker.wheelview.view.KitWheelView) : SimpleOnGestureListener() {
    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        wheelView.scrollBy(velocityY)
        return true
    }

}