package com.weberhsu.presentation.widget.expirypicker

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import com.weberhsu.presentation.widget.expirypicker.listener.CustomListener
import com.weberhsu.presentation.widget.expirypicker.listener.OnCardExpirySelectedListener
import com.weberhsu.presentation.widget.expirypicker.wheelview.view.KitWheelView
import java.util.Calendar
import java.util.Locale

class PickerOptions {
    var customListener: CustomListener? = null
    var cardExpirySelectedListener: OnCardExpirySelectedListener? = null

    //time picker
    var date: Calendar? = null // Current selected time

    var textGravity = Gravity.CENTER
    var dialogGravity = Gravity.BOTTOM // Default dialog align bottom
    var cyclic = true

    //******* general field ******//
    var currentLanguage: String = Locale.getDefault().language
    var defaultValue: Any? = null
    var layoutRes: Int? = null
    var decorView: ViewGroup? = null
    var context: Context? = null
    var textContentConfirm: String? = null
    var textContentCancel: String? = null
    var textContentTitle: String? = null
    var textColorConfirm: Int? = null
    var textColorCancel: Int? = null
    var textColorTitle: Int? = null
    var bgColorTitle: Int? = null
    var textSizeSubmitCancel = 17
    var textSizeTitle = 18
    var textSizeContent = 18
    var outSideColor = -1
    var lineSpacingMultiplier = 1.8f
    var isDialog = true
    var cancelable = true
    var itemsVisibleCount = 7 // max visible items
    var isAlphaGradient = true

    var isCenterLabel = false
    var dividerColor = -0x2a2a2b //divide line color
    var dividerType: KitWheelView.DividerType = KitWheelView.DividerType.NONE
    var textColorOut = Color.parseColor("#848E9C")
    var textColorCenter = Color.parseColor("#F0B90B")

}