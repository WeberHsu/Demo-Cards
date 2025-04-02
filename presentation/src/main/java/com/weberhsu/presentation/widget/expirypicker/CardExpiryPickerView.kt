package com.weberhsu.presentation.widget.expirypicker

import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.weberhsu.presentation.R
import com.weberhsu.presentation.widget.expirypicker.wheelview.NumericWheelAdapter
import java.util.*

class CardExpiryPickerView(pickerOptions: PickerOptions) : BasePickerView(pickerOptions.context!!) {

    private var mBeginYear = Calendar.getInstance().get(Calendar.YEAR)
    private var mBeginMonth = 1
    private var mEndYear = MAX_YEAR
    private var mEndMonth = 12
    private var mCurrentYear = Calendar.getInstance().get(Calendar.YEAR)
    private var mCurrentMonth = Calendar.getInstance().get(Calendar.MONTH)

    /**
     * Setup pickerview content
     */
    override fun initContentView() {
        if (mPickerOptions == null) return

        setDialogOutSideCancelable()
        initViews()
        initAnim()

        if (mPickerOptions!!.customListener == null) {
            LayoutInflater.from(mPickerOptions!!.context!!)
                .inflate(R.layout.pickerview_ocbs_card_expiry, contentContainer)
        } else {
            mPickerOptions!!.customListener!!.customLayout(
                LayoutInflater.from(mPickerOptions!!.context!!)
                    .inflate(mPickerOptions!!.layoutRes!!, contentContainer)
            )
        }

        if (mPickerOptions!!.date != null) {
            mCurrentYear = mPickerOptions!!.date!![Calendar.YEAR]
            mCurrentMonth = mPickerOptions!!.date!![Calendar.MONTH]
        }

        initOptions(mPickerOptions!!)

        val monthWheelView =
            findContentChildViewById(R.id.month) as com.weberhsu.presentation.widget.expirypicker.wheelview.view.KitWheelView
        monthWheelView.apply {
            setAdapter(NumericWheelAdapter(mBeginMonth, mEndMonth))
            currentItem = mCurrentMonth - mBeginMonth + 1
            setGravity(mPickerOptions!!.textGravity)
        }

        val yearWheelView =
            findContentChildViewById(R.id.year) as com.weberhsu.presentation.widget.expirypicker.wheelview.view.KitWheelView
        yearWheelView.apply {
            setAdapter(
                CardExpiryYearWheelAdapter(
                    mBeginYear,
                    mEndYear
                )
            )
            currentItem = mCurrentYear - mBeginYear
            setGravity(mPickerOptions!!.textGravity)
        }

        setOutSideCancelable(mPickerOptions!!.cancelable)
        initCommonOptions(monthWheelView)
        initCommonOptions(yearWheelView)

        onConfirm = {
            mPickerOptions!!.cardExpirySelectedListener?.onCardExpirySelected(
                (yearWheelView.currentItem + mBeginYear).toString(),
                (monthWheelView.currentItem + 1).toString()
            )
        }
    }

    private fun initOptions(options: PickerOptions) {
        options.lineSpacingMultiplier = 2.2f
        options.textColorCenter = ContextCompat.getColor(options.context!!, R.color.black)
        options.dividerType =
            com.weberhsu.presentation.widget.expirypicker.wheelview.view.KitWheelView.DividerType.NONE
        options.itemsVisibleCount = DEFAULT_VISIBLE_COUNT
    }

    override fun isDialog(): Boolean {
        return mPickerOptions?.isDialog ?: true
    }

    companion object {
        private const val DEFAULT_VISIBLE_COUNT = 7
        private const val MAX_YEAR = 2099
    }

    init {
        mPickerOptions = pickerOptions
        initContentView()
    }

}