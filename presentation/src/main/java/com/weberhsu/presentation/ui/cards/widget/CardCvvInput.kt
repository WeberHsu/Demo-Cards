package com.weberhsu.presentation.ui.cards.widget

import android.content.Context
import android.os.Build
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.method.DigitsKeyListener
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.weberhsu.presentation.R
import com.weberhsu.presentation.ui.cards.Cards
import com.weberhsu.presentation.ui.cards.widget.base.CardTextWatcher
import com.weberhsu.presentation.widget.KitInputText

class CardCvvInput @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : KitInputText(context, attrs) {

    init {
        getEtInputContentView().run {
            maxLines = 1
            filters = createFilters(Cards.DEFAULT)
            inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL
            keyListener = DigitsKeyListener.getInstance(false, true)
        }
        setInputHint("Code")
        setTitle(context.getString(R.string.card_cvv_required))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setAutofillHints(View.AUTOFILL_HINT_CREDIT_CARD_SECURITY_CODE)
        }

        textWatcherListener = object : CardTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                if (!cvvValue.isNullOrEmpty()) {
                    // 已经输入完成
                    onInputSuccess()
                } else if (!cardType.isValidCvv(rawCvvValue)) {
                    // 还未输入完
                    onInputNotCompleted()
                } else {
                    onInputError()
                }
            }
        }
    }

    private var cardType: Cards = Cards.DEFAULT

    val isValid: Boolean
        get() {
            return cardType.isValidCvv(rawCvvValue)
        }

    /**
     * The inputted CVV value if valid; otherwise, `null`.
     */
    val cvvValue: String?
        get() {
            return rawCvvValue.takeIf { isValid }
        }

    internal val rawCvvValue: String
        @JvmSynthetic
        get() {
            return getInputText().trim()
        }

    private fun createFilters(cardType: Cards): Array<InputFilter> {
        return arrayOf(InputFilter.LengthFilter(cardType.maxCvvLength))
    }

    /**
     * @param cardNumber bank card number
     * @param customHintText optional user-specified hint text
     * instead of directly on the [CardCvvInput]
     */
    @JvmSynthetic
    internal fun updateCardType(
        cardNumber: String,
        customHintText: String? = null
    ) {
        this.cardType = CardUtils.getPossibleCardType(cardNumber)
        getEtInputContentView().filters = createFilters(cardType)
    }

    private fun setInputIsEnable(enable: Boolean) {
        isEnabled = enable
        if (enable) {
            getEtInputContentView().setTextColor(ContextCompat.getColor(context, R.color.black))
//            setBackgroundColor(ContextCompat.getColor(context, R.color.Color_Input))
        } else {
            getEtInputContentView().setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.placeholder
                )
            )
//            setBackgroundColor(ContextCompat.getColor(context, R.color.Color_Line))
        }
    }
}