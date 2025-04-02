package com.weberhsu.presentation.ui.cards.widget

import CardUtils
import android.content.Context
import android.os.Build
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.annotation.VisibleForTesting
import androidx.core.content.ContextCompat
import com.weberhsu.presentation.R
import com.weberhsu.presentation.ui.cards.Cards
import com.weberhsu.presentation.ui.cards.widget.base.CardTextWatcher
import com.weberhsu.presentation.widget.KitInputText

class CardNumberInput @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : KitInputText(context, attrs) {

    init {
        initView()
        listenForTextChanges()
        setTitle(context.getString(R.string.card_number_required))
        setInputHint("0000 0000 0000 0000")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setAutofillHints(View.AUTOFILL_HINT_CREDIT_CARD_NUMBER)
        }
    }

    companion object {
        private const val FIRST_CHECK_TYPE_INDEX = 4
    }


    val fieldText: String
        get() {
            return getEtInputContentView().text?.toString().orEmpty()
        }

    @VisibleForTesting
    var cardType: Cards = Cards.DEFAULT
        internal set(value) {
            val prevType = field
            field = value
            if (value != prevType) {
                typeChangeCallback(cardType)
                updateLengthFilter()
            }
//            updateCardTypeIcon(value)
        }

    @JvmSynthetic
    internal var typeChangeCallback: (Cards) -> Unit = {}
        set(callback) {
            field = callback

            // Immediately display the brand if known, in case this method is invoked when
            // partial data already exists.
            callback(cardType)
        }

    val lengthMax: Int
        get() {
            return cardType.getMaxLengthWithSpacesForCardNumber(fieldText)
        }

    private var ignoreChanges = false

    /**
     * Check whether or not the card number is valid
     */
    var isCardNumberValid: Boolean = false
        private set

    /**
     * A normalized form of the card number. If the entered card number is "4242 4242 4242 4242",
     * this will be "4242424242424242". If the entered card number is invalid, this is `null`.
     */
    val cardNumber: String?
        get() = if (isCardNumberValid) {
            CardUtils.removeSpacesAndHyphens(fieldText)
        } else {
            null
        }

    private fun initView() {
        resetInputColor()

        getEtInputContentView().run {
            maxLines = 1
            gravity = Gravity.START or Gravity.CENTER_VERTICAL
            inputType = InputType.TYPE_CLASS_NUMBER
            imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI
        }
//        background = AppCompatResources.getDrawable(context, R.drawable.uikit_input_text_bg)
    }

    private fun resetInputColor(enabled: Boolean? = null) {
        enabled?.let {
            isEnabled = enabled
        }

        if (isEnabled) {
            getEtInputContentView().setTextColor(ContextCompat.getColor(context, R.color.black))
            getEtInputContentView().setHintTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.placeholder
                )
            )
        } else {
            getEtInputContentView().setTextColor(ContextCompat.getColor(context, R.color.black))
            getEtInputContentView().setHintTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.placeholder
                )
            )
        }

    }

    @JvmSynthetic
    internal fun updateLengthFilter() {
        getEtInputContentView().filters = arrayOf<InputFilter>(InputFilter.LengthFilter(lengthMax))
    }

    /**
     * Updates the selection index based on the current (pre-edit) index, and
     * the size change of the number being input.
     *
     * @param newLength the post-edit length of the string
     * @param editActionStart the position in the string at which the edit action starts
     * @param editActionAddition the number of new characters going into the string (zero for
     * delete)
     * @return an index within the string at which to put the cursor
     */
    @JvmSynthetic
    internal fun updateSelectionIndex(
        newLength: Int,
        editActionStart: Int,
        editActionAddition: Int
    ): Int {
        var gapsJumped = 0
        val gapSet = cardType.getSpacePositionsForCardNumber(fieldText)

        var skipBack = false
        // noinspection NewApi
        gapSet.forEach { gap ->
            if (editActionStart <= gap && editActionStart + editActionAddition > gap) {
                gapsJumped++
            }

            // editActionAddition can only be 0 if we are deleting,
            // so we need to check whether or not to skip backwards one space
            if (editActionAddition == 0 && editActionStart == gap + 1) {
                skipBack = true
            }
        }

        var newPosition: Int = editActionStart + editActionAddition + gapsJumped
        if (skipBack && newPosition > 0) {
            newPosition--
        }

        return if (newPosition <= newLength) {
            newPosition
        } else {
            newLength
        }
    }

    private fun listenForTextChanges() {
        textWatcherListener = object : CardTextWatcher() {
            private var latestChangeStart: Int = 0
            private var latestInsertionSize: Int = 0

            private var newCursorPosition: Int? = null
            private var formattedNumber: String? = null

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                if (!ignoreChanges) {
                    latestChangeStart = start
                    latestInsertionSize = after
                }
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (ignoreChanges) {
                    return
                }

                val inputText = s?.toString().orEmpty()
                if (start < FIRST_CHECK_TYPE_INDEX) {
                    updateCardTypeFromNumber(inputText)
                }

                if (start > cardType.defaultMaxLength) {
                    // no need to do formatting if we're past all of the spaces.
                    return
                }
                val spacelessNumber = CardUtils.removeSpacesAndHyphens(inputText)
                    ?: return

                val formattedNumber = cardType.formatNumber(spacelessNumber)
                this.newCursorPosition = updateSelectionIndex(
                    formattedNumber.length,
                    latestChangeStart, latestInsertionSize
                )
                this.formattedNumber = formattedNumber
            }

            override fun afterTextChanged(s: Editable?) {
                if (ignoreChanges) {
                    return
                }

                ignoreChanges = true
                if (!isLastKeyDelete && formattedNumber != null) {
                    getEtInputContentView().setText(formattedNumber)
                    newCursorPosition?.let {
                        getEtInputContentView().setSelection(it.coerceIn(0, fieldText.length))
                    }
                }
                formattedNumber = null
                newCursorPosition = null

                ignoreChanges = false

                if (fieldText.length == lengthMax) {
                    val wasCardNumberValid = isCardNumberValid
                    isCardNumberValid = CardUtils.isValidCardNumber(fieldText)
                    if (!wasCardNumberValid && isCardNumberValid) {
                        onInputSuccess()
                    } else {
                        onInputError()
                    }
                } else {
                    isCardNumberValid = CardUtils.isValidCardNumber(fieldText)
                    onInputNotCompleted()
                }
            }
        }
    }

    internal fun updateCardTypeFromCode(code: String) {
        cardType = Cards.fromCode(code)
    }

    @JvmSynthetic
    internal fun updateCardTypeFromNumber(partialNumber: String) {
        cardType = CardUtils.getPossibleCardType(partialNumber)
    }

    // Only support visa and mastercard now
    fun isValidCardType(): Boolean {
        return when (cardType) {
            Cards.VISA, Cards.MASTERCARD -> true
            else -> false
        }
    }
}