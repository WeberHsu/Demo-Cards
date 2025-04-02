package com.weberhsu.presentation.ui.cards.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.View.OnClickListener
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.weberhsu.presentation.R
import com.weberhsu.presentation.extensions.isRtlDirection
import com.weberhsu.presentation.widget.expirypicker.CardExpiryPickerBuilder
import com.weberhsu.presentation.widget.expirypicker.listener.OnCardExpirySelectedListener
import java.util.Calendar

class CardExpiryDatePickerTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : androidx.appcompat.widget.AppCompatTextView(context, attrs), OnClickListener {
    internal var expiryMonth: String = ""
    internal var expiryYear: String = ""
    var callback: CardExpiryDateCallback? = null

    @ColorInt
    var expTextColor = ContextCompat.getColor(context, R.color.white)

    private val onCardExpirySelectedListener = object : OnCardExpirySelectedListener {
        override fun onCardExpirySelected(year: String, month: String) {
            var dateValid = true
            dateValid = when {
                year.toInt() < Calendar.getInstance().get(Calendar.YEAR) -> {
                    false
                }

                year.toInt() == Calendar.getInstance().get(Calendar.YEAR) -> {
                    month.toInt() >= (Calendar.getInstance().get(Calendar.MONTH) + 1)
                }

                else -> {
                    true
                }
            }
            val selectedMonth = if (month.toInt() < MONTH_CHECK_NUMBER) "0$month" else month
            setCardExpiryDate(year, selectedMonth)
            expiryMonth = selectedMonth
            expiryYear = year

            callback?.onExpiryDateSelected(
                this@CardExpiryDatePickerTextView,
                expiryYear,
                expiryMonth,
                dateValid
            )
        }
    }

    init {
        text = "MM/YY"
//        background = ContextCompat.getDrawable(context, R.drawable.bg_kit_input_text)
        setTextColor(ContextCompat.getColor(context, R.color.placeholder))
        gravity = Gravity.START or Gravity.CENTER_VERTICAL
    }


    @SuppressLint("SetTextI18n")
    fun setCardExpiryDate(year: String, month: String) {
        text = if (isRtlDirection(resources)) {
            //in Right To Left layout
            year.substring(
                year.length - 2,
                year.length
            ) + "/$month"
        } else {
            "$month/" + year.substring(
                year.length - 2,
                year.length
            )
        }
        setTextColor(expTextColor)
    }

    interface CardExpiryDateCallback {
        fun onExpiryDateSelected(
            view: CardExpiryDatePickerTextView,
            year: String,
            month: String,
            dateValid: Boolean
        )
    }

    companion object {
        private const val MONTH_CHECK_NUMBER = 10
    }

    fun showExpiryDatePicker() {
        CardExpiryPickerBuilder(context).apply {
            addOnCardExpirySelectedListener(onCardExpirySelectedListener)

            if (expiryYear.isNotBlank() && expiryMonth.isNotBlank()) {
                // set previous value
                setExpiryDate(
                    Calendar.getInstance().apply {
                        set(Calendar.YEAR, expiryYear.toInt())
                        set(Calendar.MONTH, expiryMonth.toInt() - 1)
                    }
                )
            }

            setSubmitText(resources.getString(R.string.confirm))
            setCancelText(resources.getString(R.string.cancel))
            setTitleText(resources.getString(R.string.select_expiry_date))
        }.build().show()
    }

    override fun onClick(v: View?) {
        showExpiryDatePicker()
    }
}