package com.banking;

/**
 * Tools for processing payment references.
 */
public class PaymentRefTools {

    /**
     * Check if a character is a hyphen.
     * 
     * @param c The character to check
     * @return true if the character is a hyphen, false otherwise
     */
    public static boolean isHyphen(char c) {
        return c == '-';
    }

    /**
     * Check if a character is a full stop.
     * 
     * @param c The character to check
     * @return true if the character is a full stop, false otherwise
     */
    public static boolean isFullStop(char c) {
        return c == '.';
    }

    /**
     * Check if a character is a space.
     * 
     * @param c The character to check
     * @return true if the character is a space, false otherwise
     */
    public static boolean isSpace(char c) {
        return c == ' ';
    }

    /**
     * Check if a character is a valid symbol for payment reference (hyphen, full stop, or space).
     * 
     * @param c The character to check
     * @return true if the character is a valid symbol, false otherwise
     */
    public static boolean isValidSymbolForPaymentReference(char c) {
        return isHyphen(c) || isFullStop(c) || isSpace(c);
    }

    /**
     * Check if a character is within the range of characters 'a' to 'z'.
     * 
     * @param c The character to check
     * @return true if the character is between 'a' and 'z', false otherwise
     */
    public static boolean isLowerLatinAlphabet(char c) {
        return c >= 'a' && c <= 'z';
    }

    /**
     * Check if a character is within the range of characters 'A' to 'Z'.
     * 
     * @param c The character to check
     * @return true if the character is between 'A' and 'Z', false otherwise
     */
    public static boolean isUpperLatinAlphabet(char c) {
        return c >= 'A' && c <= 'Z';
    }

    /**
     * Check if a character is a Latin alphabet character (upper or lowercase).
     * 
     * @param c The character to check
     * @return true if the character is a Latin alphabet character, false otherwise
     */
    public static boolean isLatinAlphabet(char c) {
        return isLowerLatinAlphabet(c) || isUpperLatinAlphabet(c);
    }

    /**
     * Check if a character is valid for a payment reference.
     * Valid characters are: hyphen, full stop, space, Latin letters (a-z, A-Z), or digits (0-9).
     * 
     * @param c The character to check
     * @return true if the character is valid, false otherwise
     */
    public static boolean isValidCharacterForPaymentReference(char c) {
        return isValidSymbolForPaymentReference(c) || isLatinAlphabet(c) || Character.isDigit(c);
    }

    /**
     * Check if a payment reference exceeds the maximum length of 40 characters.
     * 
     * @param paymentRef The payment reference string
     * @return true if the string exceeds 40 characters, false otherwise
     */
    public static boolean exceedsMaximumPaymentRefLength(String paymentRef) {
        return paymentRef.length() > 40;
    }

    /**
     * Truncate a string to the specified maximum length.
     * 
     * @param paymentRef The payment reference string
     * @param maxLength The maximum length
     * @return The truncated string (or original if it doesn't exceed the maximum)
     */
    public static String truncate(String paymentRef, int maxLength) {
        if (paymentRef.length() > maxLength) {
            return paymentRef.substring(0, maxLength);
        }
        return paymentRef;
    }
}
