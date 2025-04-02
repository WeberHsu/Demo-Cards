package com.weberhsu.presentation.widget.expirypicker.wheelview.timer

import android.os.Handler
import android.os.Message
import com.weberhsu.presentation.widget.expirypicker.wheelview.view.KitWheelView


class MessageHandler(private val wheelView: KitWheelView) : Handler() {
    override fun handleMessage(msg: Message) {
        when (msg.what) {
            WHAT_INVALIDATE_LOOP_VIEW -> wheelView.invalidate()
            WHAT_SMOOTH_SCROLL -> wheelView.smoothScroll(
                com.weberhsu.presentation.widget.expirypicker.wheelview.view.KitWheelView.ACTION.FLING
            )

            WHAT_ITEM_SELECTED -> wheelView.onItemSelected()
        }
    }

    companion object {
        const val WHAT_INVALIDATE_LOOP_VIEW = 1000
        const val WHAT_SMOOTH_SCROLL = 2000
        const val WHAT_ITEM_SELECTED = 3000
    }

}