package com.weberhsu.presentation.widget.expirypicker.wheelview.timer

import com.weberhsu.presentation.widget.expirypicker.wheelview.view.KitWheelView
import java.util.*
import kotlin.math.abs


class InertiaTimerTask(wheelView: com.weberhsu.presentation.widget.expirypicker.wheelview.view.KitWheelView, velocityY: Float) : TimerTask() {
    private var mCurrentVelocityY: Float //Current Scroll velocity
    private val mFirstVelocityY: Float = velocityY //Initial velocity of finger exit screen
    private val mWheelView: com.weberhsu.presentation.widget.expirypicker.wheelview.view.KitWheelView = wheelView
    override fun run() {

        if (mCurrentVelocityY == Int.MAX_VALUE.toFloat()) {
            mCurrentVelocityY = if (abs(mFirstVelocityY) > 2000f) {
                if (mFirstVelocityY > 0) 2000f else -2000f
            } else {
                mFirstVelocityY
            }
        }

        // Send handler message to handle stopping scrolling logics smoothly
        if (Math.abs(mCurrentVelocityY) in 0.0f..20f) {
            mWheelView.cancelFuture()
            mWheelView.handler.sendEmptyMessage(MessageHandler.WHAT_SMOOTH_SCROLL)
            return
        }
        val dy = (mCurrentVelocityY / 100f).toInt()
        mWheelView.totalScrollY = mWheelView.totalScrollY - dy
        if (!mWheelView.isLoop) {
            val itemHeight: Float = mWheelView.itemHeight
            var top: Float = -mWheelView.initPosition * itemHeight
            var bottom: Float = (mWheelView.itemsCount - 1 - mWheelView.initPosition) * itemHeight
            if (mWheelView.totalScrollY - itemHeight * 0.25 < top) {
                top = mWheelView.totalScrollY+ dy
            } else if (mWheelView.totalScrollY + itemHeight * 0.25 > bottom) {
                bottom = mWheelView.totalScrollY + dy
            }
            if (mWheelView.totalScrollY <= top) {
                mCurrentVelocityY = 40f
                mWheelView.totalScrollY = top
            } else if (mWheelView.totalScrollY >= bottom) {
                mWheelView.totalScrollY = bottom
                mCurrentVelocityY = -40f
            }
        }
        mCurrentVelocityY = if (mCurrentVelocityY < 0.0f) {
            mCurrentVelocityY + 20f
        } else {
            mCurrentVelocityY - 20f
        }

        //刷新UI
        mWheelView.handler.sendEmptyMessage(MessageHandler.WHAT_INVALIDATE_LOOP_VIEW)
    }

    /**
     * @param wheelView Scroll object
     * @param velocityY Scroll velocity of y axie
     */
    init {
        mCurrentVelocityY = Int.MAX_VALUE.toFloat()
    }
}