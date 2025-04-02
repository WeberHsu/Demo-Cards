package com.weberhsu.presentation.ui.cards

import java.util.regex.Pattern

enum class Cards(
    val code: String,
    val displayName: String,

    /**
     * Accepted CVC lengths
     */
    val cvcLength: Set<Int> = setOf(3),

    /**
     * The default max length when the card number is formatted without spaces (e.g. "4242424242424242")
     *
     * Note that [Cards.DINERS_CLUB]'s max length depends on the BIN (e.g. card number prefix).
     * In the case of a [Cards.DINERS_CLUB] card, use [getMaxLengthForCardNumber].
     */
    val defaultMaxLength: Int = 19,

    /**
     * Based on [Issuer identification number table](http://en.wikipedia.org/wiki/Bank_card_number#Issuer_identification_number_.28IIN.29)
     */
    val pattern: Pattern? = null,

    /**
     * Patterns for discrete lengths
     */
    private val partialPatterns: Map<Int, Pattern> = emptyMap(),

    /**
     * The position of spaces in a formatted card number. For example, "4242424242424242" is
     * formatted to "4242 4242 4242 4242".
     */
    val defaultSpacePositions: Set<Int> = setOf(4, 9, 14, 19),

    /**
     * By default, a [Cards] does not have variants.
     */
    private val variantMaxLength: Map<Pattern, Int> = emptyMap(),

    private val variantSpacePositions: Map<Pattern, Set<Int>> = emptyMap()
) {
    AMERICAN_EXPRESS(
        "american express",//Align with back-end
        "American Express",
        cvcLength = setOf(3, 4),
//        defaultMaxLength = 15,
        pattern = Pattern.compile("^(34|37)[0-9]*$"),
        defaultSpacePositions = setOf(4, 11)
    ),

    DISCOVER(
        "Discover",
        "Discover",
        pattern = Pattern.compile("^(60|64|65)[0-9]*$")
    ),

    /**
     * JCB
     *
     * BIN range: 352800 to 358999
     */
    JCB(
        "Jcb",
        "JCB",
        pattern = Pattern.compile("^(352[89]|35[3-8][0-9])[0-9]*$"),
        partialPatterns = mapOf(
            2 to Pattern.compile("^(35)$"),
            3 to Pattern.compile("^(35[2-8])$")
        )
    ),

    /**
     * Diners Club
     *
     * 14-digits: BINs starting with 36
     * 16-digits: BINs starting with 30, 38, 39
     */
    DINERS_CLUB(
        "Diners",
        "Diners Club",
//        defaultMaxLength = 16,
        pattern = Pattern.compile("^(36|30|38|39)[0-9]*$"),
        variantMaxLength = mapOf(
            Pattern.compile("^(36)[0-9]*$") to 14
        ),
        variantSpacePositions = mapOf(
            Pattern.compile("^(36)[0-9]*$") to setOf(4, 11)
        )
    ),

    VISA(
        "Visa",
        "Visa",
        pattern = Pattern.compile("^(4)[0-9]*$"),
        cvcLength = setOf(3, 4)
    ),

    MASTERCARD(
        "Mastercard",
        "Mastercard",
        pattern = Pattern.compile("^(50|51|52|53|54|55)[0-9]*$"),
        partialPatterns = mapOf(
            2 to Pattern.compile("^(51|52|53|54|55)$")
        ),
        cvcLength = setOf(3, 4)
    ),

    UNION_PAY(
        "Unionpay",
        "UnionPay",
        pattern = Pattern.compile("^(62|81)[0-9]*$")
    ),

    DEFAULT(
        MASTERCARD.code,
        MASTERCARD.displayName,
        cvcLength = setOf(3, 4)
    ),

    UNKNOWN(
        MASTERCARD.code,
        MASTERCARD.displayName,
        cvcLength = setOf(3, 4)
    );

    companion object {
        /**
         * @param cardNumber a card number
         * @return the [Cards] that matches the [cardNumber]'s prefix, if one is found;
         * otherwise, [Cards.UNKNOWN]
         */
        fun fromCardNumber(cardNumber: String?): Cards {
            if (cardNumber.isNullOrBlank()) {
                return DEFAULT
            }

            return values()
                .firstOrNull { cardType ->
                    cardType.getPatternForLength(cardNumber)?.matcher(cardNumber)?.matches() == true
                } ?: DEFAULT
        }

        /**
         * @param code a brand code, such as `Visa` or `American Express`.
         */
        fun fromCode(code: String?): Cards {
            return values().firstOrNull { it.code.equals(code, ignoreCase = true) } ?: DEFAULT
        }

        private const val CVC_COMMON_LENGTH: Int = 3
    }

    val maxCvvLength: Int
        get() {
            return cvcLength.maxOrNull() ?: CVC_COMMON_LENGTH
        }

    /**
     * Returns a boolean showing is the cvv is valid in relation to the card type
     *
     * @param cvv  the card cvv
     * @return boolean representing validity
     */
    fun isValidCvv(cvv: String): Boolean {
        return cvcLength.contains(cvv.length)
    }

    private fun getPatternForLength(cardNumber: String): Pattern? {
        return partialPatterns[cardNumber.length] ?: pattern
    }

    fun getMaxLengthWithSpacesForCardNumber(cardNumber: String): Int {
        return getMaxLengthForCardNumber(cardNumber) +
                getSpacePositionsForCardNumber(cardNumber).size
    }

    /**
     * If the [Cards] has variants, and the [cardNumber] starts with one of the variant
     * prefixes, return the length for that variant. Otherwise, return [defaultMaxLength].
     *
     * Note: currently only [Cards.DINERS_CLUB] has variants
     */
    fun getSpacePositionsForCardNumber(cardNumber: String): Set<Int> {
        val normalizedCardNumber = CardUtils.removeSpacesAndHyphens(cardNumber).orEmpty()
        return variantSpacePositions.entries.firstOrNull { (pattern, _) ->
            pattern.matcher(normalizedCardNumber).matches()
        }?.value ?: defaultSpacePositions
    }

    /**
     * If the [Cards] has variants, and the [cardNumber] starts with one of the variant
     * prefixes, return the length for that variant. Otherwise, return [defaultMaxLength].
     *
     * Note: currently only [Cards.DINERS_CLUB] has variants
     */
    fun getMaxLengthForCardNumber(cardNumber: String): Int {
        val normalizedCardNumber = CardUtils.removeSpacesAndHyphens(cardNumber).orEmpty()
        return variantMaxLength.entries.firstOrNull { (pattern, _) ->
            pattern.matcher(normalizedCardNumber).matches()
        }?.value ?: defaultMaxLength
    }

    /**
     * Format a number according to brand requirements.
     *
     * e.g. `"4242424242424242"` will return `"4242 4242 4242 4242"`
     */
    fun formatNumber(cardNumber: String): String {
        return groupNumber(cardNumber)
            .takeWhile { it != null }
            .joinToString(" ")
    }

    /**
     * Separates a card number according to the brand requirements, including prefixes of card
     * numbers, so that the groups can be easily displayed if the user is typing them in.
     * Note that this does not verify that the card number is valid, or even that it is a number.
     *
     * e.g. `"4242424242424242"` will return `["4242", "4242", "4242", "4242"]`
     *
     * @param cardNumber the raw card number
     *
     * @return an array of strings with the number groups, in order. If the number is not complete,
     * some of the array entries may be `null`.
     */
    fun groupNumber(cardNumber: String): Array<String?> {
        val spacelessCardNumber = cardNumber.take(getMaxLengthForCardNumber(cardNumber))
        val spacePositions = getSpacePositionsForCardNumber(cardNumber)
        val groups = arrayOfNulls<String?>(spacePositions.size + 1)

        val length = spacelessCardNumber.length
        var lastUsedIndex = 0

        spacePositions
            .toList().sorted().forEachIndexed { idx, spacePosition ->
                val adjustedSpacePosition = spacePosition - idx
                if (length > adjustedSpacePosition) {
                    groups[idx] = spacelessCardNumber.substring(
                        lastUsedIndex,
                        adjustedSpacePosition
                    )
                    lastUsedIndex = adjustedSpacePosition
                }
            }

        // populate any remaining digits in the first index with a null value
        groups
            .indexOfFirst { it == null }
            .takeIf {
                it != -1
            }?.let {
                groups[it] = spacelessCardNumber.substring(lastUsedIndex)
            }

        return groups
    }

    /**
     * Checks to see whether the input number is of the correct length, given the assumed brand of
     * the card. This function does not perform a Luhn check.
     *
     * @param cardNumber the card number with no spaces or dashes
     * @return `true` if the card number is the correct length for the assumed brand
     */
    fun isValidCardNumberLength(cardNumber: String?): Boolean {
        return cardNumber != null && DEFAULT != this &&
                cardNumber.length == getMaxLengthForCardNumber(cardNumber)
    }
}