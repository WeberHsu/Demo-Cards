package com.weberhsu.presentation.util

import android.content.Context
import android.util.TypedValue

object ScreenUtils {
    fun dp2px(context: Context, dps: Float): Int {
        return Math.round(context.resources.displayMetrics.density * dps)
    }

    fun sp2px(context: Context, spVal: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            spVal, context.resources.displayMetrics
        ).toInt()
    }

    fun getScreenWidth(context: Context): Int {
        return context.resources.displayMetrics.widthPixels
    }

}