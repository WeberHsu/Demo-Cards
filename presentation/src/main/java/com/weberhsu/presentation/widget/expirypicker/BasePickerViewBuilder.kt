package com.weberhsu.presentation.widget.expirypicker

import android.content.Context
import android.view.ViewGroup
import androidx.annotation.ColorInt
import com.weberhsu.presentation.widget.expirypicker.listener.CustomListener

abstract class BasePickerViewBuilder(context: Context) {

    protected val mPickerOptions: PickerOptions =
        PickerOptions()

    fun setDefaultValue(value: String?): BasePickerViewBuilder {
        mPickerOptions.defaultValue = value
        return this
    }

    fun setSubmitText(textContentConfirm: String?): BasePickerViewBuilder {
        mPickerOptions.textContentConfirm = textContentConfirm
        return this
    }

    fun isDialog(isDialog: Boolean): BasePickerViewBuilder {
        mPickerOptions.isDialog = isDialog
        return this
    }

    fun setCancelText(textContentCancel: String?): BasePickerViewBuilder {
        mPickerOptions.textContentCancel = textContentCancel
        return this
    }

    fun setTitleText(textContentTitle: String?): BasePickerViewBuilder {
        mPickerOptions.textContentTitle = textContentTitle
        return this
    }

    fun setSubmitColor(textColorConfirm: Int): BasePickerViewBuilder {
        mPickerOptions.textColorConfirm = textColorConfirm
        return this
    }

    fun setCancelColor(textColorCancel: Int): BasePickerViewBuilder {
        mPickerOptions.textColorCancel = textColorCancel
        return this
    }

    fun isCyclic(cyclic: Boolean): BasePickerViewBuilder {
        mPickerOptions.cyclic = cyclic
        return this
    }

    fun setOutSideCancelable(cancelable: Boolean): BasePickerViewBuilder {
        mPickerOptions.cancelable = cancelable
        return this
    }

    fun setLayoutRes(res: Int, customListener: CustomListener?): BasePickerViewBuilder {
        mPickerOptions.layoutRes = res
        mPickerOptions.customListener = customListener
        return this
    }

    /**
     * alpha is gradient or not
     *
     * @param isAlphaGradient true of false
     */
    fun isAlphaGradient(isAlphaGradient: Boolean): BasePickerViewBuilder {
        mPickerOptions.isAlphaGradient = isAlphaGradient
        return this
    }

    fun setDecorView(decorView: ViewGroup?): BasePickerViewBuilder {
        mPickerOptions.decorView = decorView
        return this
    }

    fun setTitleBgColor(bgColorTitle: Int): BasePickerViewBuilder {
        mPickerOptions.bgColorTitle = bgColorTitle
        return this
    }

    fun setTitleColor(textColorTitle: Int): BasePickerViewBuilder {
        mPickerOptions.textColorTitle = textColorTitle
        return this
    }

    fun setSubCalSize(textSizeSubmitCancel: Int): BasePickerViewBuilder {
        mPickerOptions.textSizeSubmitCancel = textSizeSubmitCancel
        return this
    }

    fun setTitleSize(textSizeTitle: Int): BasePickerViewBuilder {
        mPickerOptions.textSizeTitle = textSizeTitle
        return this
    }

    fun setContentTextSize(textSizeContent: Int): BasePickerViewBuilder {
        mPickerOptions.textSizeContent = textSizeContent
        return this
    }

    fun setDialogGravity(gravity: Int): BasePickerViewBuilder {
        mPickerOptions.dialogGravity = gravity
        return this
    }

    fun setTextColorCenter(@ColorInt textColorCenter: Int): BasePickerViewBuilder {
        mPickerOptions.textColorCenter = textColorCenter
        return this
    }

    fun setTextColorOut(@ColorInt textColorOut: Int): BasePickerViewBuilder {
        mPickerOptions.textColorOut = textColorOut
        return this
    }

    abstract fun build(): BasePickerView

    //Required
    init {
        mPickerOptions.context = context
    }
}