import com.weberhsu.presentation.ui.cards.Cards

object CardUtils {

    private const val DECIMAL_THRESHOLD = 9
    private const val DECIMAL = 10

    fun removeSpacesAndHyphens(cardNumberWithSpaces: String?): String? {
        return cardNumberWithSpaces.takeUnless { it.isNullOrBlank() }
            ?.replace("\\s|-".toRegex(), "")
    }

    /**
     * @param cardNumber a full or partial card number
     * @return the [Cards] that matches the card number based on prefixes,
     * or [Cards.UNKNOWN] if it can't be determined
     */
    @JvmStatic
    fun getPossibleCardType(cardNumber: String?): Cards {
        return getPossibleCardType(cardNumber, true)
    }

    private fun getPossibleCardType(cardNumber: String?, shouldNormalize: Boolean): Cards {
        if (cardNumber.isNullOrBlank()) {
            return Cards.DEFAULT
        }

        val spacelessCardNumber =
            if (shouldNormalize) {
                removeSpacesAndHyphens(cardNumber)
            } else {
                cardNumber
            }

        return Cards.fromCardNumber(spacelessCardNumber)
    }

    /**
     * Checks the input string to see whether or not it is a valid card number, possibly
     * with groupings separated by spaces or hyphens.
     *
     * @param cardNumber a String that may or may not represent a valid card number
     * @return `true` if and only if the input value is a valid card number
     */
    @JvmStatic
    fun isValidCardNumber(cardNumber: String?): Boolean {
        val normalizedNumber =
            removeSpacesAndHyphens(cardNumber)
        return isValidLuhnNumber(normalizedNumber) && isValidCardLength(
            normalizedNumber
        )
    }

    /**
     * Checks the input string to see whether or not it is a valid Luhn number.
     *
     * @param cardNumber a String that may or may not represent a valid Luhn number
     * @return `true` if and only if the input value is a valid Luhn number
     */
    internal fun isValidLuhnNumber(cardNumber: String?): Boolean {
        if (cardNumber == null) {
            return false
        }

        var isOdd = true
        var sum = 0

        for (index in cardNumber.length - 1 downTo 0) {
            val c = cardNumber[index]
            if (!Character.isDigit(c)) {
                return false
            }

            var digitInteger = Character.getNumericValue(c)
            isOdd = !isOdd

            if (isOdd) {
                digitInteger *= 2
            }

            if (digitInteger > DECIMAL_THRESHOLD) {
                digitInteger -= DECIMAL_THRESHOLD
            }

            sum += digitInteger
        }

        return sum % DECIMAL == 0
    }

    /**
     * Checks to see whether the input number is of the correct length, after determining its brand.
     * This function does not perform a Luhn check.
     *
     * @param cardNumber the card number with no spaces or dashes
     * @return `true` if the card number is of known type and the correct length
     */
    internal fun isValidCardLength(cardNumber: String?): Boolean {
        return cardNumber != null &&
                getPossibleCardType(cardNumber, false).isValidCardNumberLength(cardNumber)
    }
}