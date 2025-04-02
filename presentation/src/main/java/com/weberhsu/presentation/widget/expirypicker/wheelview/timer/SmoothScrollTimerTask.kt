package com.weberhsu.presentation.widget.expirypicker.wheelview.timer

import com.weberhsu.presentation.widget.expirypicker.wheelview.view.KitWheelView
import java.util.*


class SmoothScrollTimerTask(wheelView: com.weberhsu.presentation.widget.expirypicker.wheelview.view.KitWheelView, offset: Int) : TimerTask() {
    private var realTotalOffset: Int
    private var realOffset: Int
    private val offset: Int = offset
    private val wheelView: com.weberhsu.presentation.widget.expirypicker.wheelview.view.KitWheelView = wheelView
    override fun run() {
        if (realTotalOffset == Int.MAX_VALUE) {
            realTotalOffset = offset
        }

        realOffset = (realTotalOffset.toFloat() * 0.1f).toInt()
        if (realOffset == 0) {
            realOffset = if (realTotalOffset < 0) {
                -1
            } else {
                1
            }
        }
        if (Math.abs(realTotalOffset) <= 1) {
            wheelView.cancelFuture()
            wheelView.handler
                .sendEmptyMessage(MessageHandler.WHAT_ITEM_SELECTED)
        } else {
            wheelView.totalScrollY = wheelView.totalScrollY + realOffset

            if (!wheelView.isLoop) {
                val itemHeight: Float = wheelView.itemHeight
                val top = (-wheelView.initPosition).toFloat() * itemHeight
                val bottom =
                    (wheelView.itemsCount - 1 - wheelView.initPosition).toFloat() * itemHeight
                if (wheelView.totalScrollY <= top || wheelView.totalScrollY >= bottom) {
                    wheelView.totalScrollY = wheelView.totalScrollY - realOffset
                    wheelView.cancelFuture()
                    wheelView.handler
                        .sendEmptyMessage(MessageHandler.WHAT_ITEM_SELECTED)
                    return
                }
            }
            wheelView.handler
                .sendEmptyMessage(MessageHandler.WHAT_INVALIDATE_LOOP_VIEW)
            realTotalOffset -= realOffset
        }
    }

    init {
        realTotalOffset = Int.MAX_VALUE
        realOffset = 0
    }
}