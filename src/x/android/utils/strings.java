/**
 * \file
 * Defines the strings class.
 *
 * \author Alessandro Antonello <aantonello@paralaxe.com.br>
 * \date   October 06, 2010
 *
 * \par Copyright
 * This file is provided in hope that it will be useful to someone. It is
 * offered in public domain. You may use, modify or distribute it freely.
 *
 * The code is provided "AS IS". There is no warranty at all, of any kind. You
 * may change it if you like. Or just use it as it is.
 */
package x.android.utils;

import java.io.InputStream;
import x.android.defs.ENC;
import x.android.xml.*;

/**
 * \ingroup x_android_utils
 * Implement function to work with strings and text.
 *//* --------------------------------------------------------------------- */
public final class strings
{
    // public static String substr(String str, int start, int len);/*{{{*/
    /**
     * Gets a sub-string of the given string.
     * \param str String to extract the sub-string.
     * \param start Zero based index of the starting character to extract.
     * \param len Length of the sub-string. If less than zero all characters
     * starting from \a start will be extracted.
     * \returns A string resulting the extracted sub-string or \b null if no
     * sub-string could be extracted.
     **/
    public static String substr(String str, int start, int len) {
        if ((str == null) || (start < 0) || (len == 0))
            return null;

        if (len < 0) len = (str.length() - start);

        try { return str.substring(start, (len + start)); }
        catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }/*}}}*/
    // public static String replace(String str, String rem, String add);/*{{{*/
    /**
     * Replaces a sub-string with another string value.
     * \param str The original string, where the substitution must be done.
     * \param rem The string to be removed from the original \a str string.
     * \param add The string to be added in the place where \a rem string was.
     * \return The new string with the substitution done. If the \a rem string
     * isn't found the original \a str string will be returned without
     * changes.
     * \remarks All occurrencies of *rem* will be replaced with *add*.
     **/
    public static String replace(String str, String rem, String add) {
        int index = str.indexOf(rem);
        int count = strings.length(rem);
        String start, end;

        while (index >= 0)
        {
            start = strings.substr(str, 0, index);
            end   = strings.substr(str, index + count, -1);

            str = ((start != null) ? start : strings.EMPTY) + add
                  + ((end != null) ? end : strings.EMPTY);

            index = str.indexOf(rem);
        }
        return str;
    }/*}}}*/
    // public static int    ncasecmp(String str, String...list);/*{{{*/
    /**
     * Compares a string against a list of options.
     * \param str String to compare with the list.
     * \param list Optional list of strings to compare with \a str.
     * \return If one of the items of the list is equal to \a str the function
     * returns its index, zero based. If no item match, the result is -1.
     * \remarks The name \c ncasecmp() resembles to <b>No case sensitive</b>
     * compare.
     **/
    public static int ncasecmp(String str, String...list) {
        if ((str == null) || (list == null) || (list.length == 0))
            return -1;

        int limit = list.length;

        for (int i = 0; i < limit; i++) {
            if (str.equalsIgnoreCase(list[i]))
                return i;
        }
        return -1;
    }/*}}}*/
    // public static String sanitize(String str);/*{{{*/
    /**
     * Sanitizes the passed in String object.
     * Sanitize means make it as valid as possible. The function checks for a
     * \b null String converting it to the valid strings#EMPTY object. The
     * funtion also checks for the possible presence of unprintable
     * characters, removing them.
     * \param str String to sanitize.
     * \return The function garantee that the return value is a printable
     * string or a zero length string. This function never returns \b null.
     **/
    public static String sanitize(String str) {
        if (strings.length(str) == 0) return EMPTY;

        char[] chars = strings.getChars(str);
        char[] letters = new char[ arrays.length(chars) ];
        int counter  = 0, limit = arrays.length(chars);

        for (int i = 0; i < limit; i++) {
            if (Character.isLetterOrDigit(chars[i]) || Character.isWhitespace(chars[i])) {
                letters[counter] = chars[i];
                counter++;
            }
        }

        if (counter == 0) return EMPTY;
        return new String(letters, 0, counter);
    }/*}}}*/
    // public static String repeat(char c, int times);/*{{{*/
    /**
     * Build a string by repeating a char some times.
     * \param c Character to use to build the string.
     * \param times Number of times to repeat the character \a c.
     * \return The string built from the repeating character.
     **/
    public static String repeat(char c, int times) {
        if (times == 0) return strings.EMPTY;
        char[] array = new char[times];
        arrays.set(array, c);
        return new String(array);
    }/*}}}*/
    // public static int    toInt(String num, int radix);/*{{{*/
    /**
     * Converts a string to a number using the given radix.
     * \param num The string with the number.
     * \param radix The radix to do the conversion. This must be 0 or in range
     *      2-36. If this value is 0 the function will try to recognize the
     *      radix from the string. If the string starts with '0x' or '0X' radix
     *      16 will be used (hex number). If the string starts with '0' radix 8
     *      will be used (octal number). If the string starts with a character
     *      from '1' to '9' radix 10 will be used (decimal number).
     * \return The \b int value result of the string conversion.
     * \remarks The function supports that the string has a signal. That is,
     * the value can have a minus '-' or plus '+' signal in front of it. The
     * conversion will stop in the first invalid character. No error or
     * exception will be thrown.
     **/
    public static int toInt(String num, int radix) {
        return (int)(strings.toLong(num, radix) & 0xFFFFFFFF);
    }/*}}}*/
    // public static long   toLong(String num, int radix);/*{{{*/
    /**
     * Converts a string to a number using the given radix.
     * \param num The string with the number.
     * \param radix The radix to do the conversion. This must be 0 or in range
     *      2-36. If this value is 0 the function will try to recognize the
     *      radix from the string. If the string starts with '0x' or '0X' radix
     *      16 will be used (hex number). If the string starts with '0' radix 8
     *      will be used (octal number). If the string starts with a character
     *      from '1' to '9' radix 10 will be used (decimal number).
     * \return The \b long value result of the string conversion.
     * \remarks The function supports that the string has a signal. That is,
     * the value can have a minus '-' or plus '+' signal in front of it. The
     * conversion will stop in the first invalid character. No error or
     * exception will be thrown.
     **/
    public static long toLong(String num, int radix) {
        long result = 0L;

        if ((num == null) || (num.length() == 0)) {
            return result;
        }

        char[] arr = num.toCharArray();
        char c;
        int   pos = 0;
        int count = arr.length;
        int   val = 0;
        boolean negative = false;

        while ((pos < count) && Character.isWhitespace(arr[pos])) {
            pos++;                  /* Skip white spaces. */
        }

        if (pos >= count) {
            return result;          /* Only spaces in the text. */
        }

        /* Check of a sign. */
        if (arr[pos] == '-') {
            pos++;
            negative = true;
        } else if (arr[pos] == '+') {
            pos++;
        }

        /* We always need to check about the end of the string. */
        if (pos >= count) return result;

        /* If radix is zero we need to figure out. */
        if (radix == 0) {
            if ((arr[pos] == '0') && (pos < (count - 1))) {
                if ((arr[pos+1] == 'x') || (arr[pos+1] == 'X'))
                    radix = 16;
                else
                    radix = 8;
            }
            else
                radix = 10;
        }

        /* Validating the 'radix' value. */
        if ((radix < 0) || (radix == 1) || (radix > 36)) {
            return result;
        } else if (radix == 16) {
            /* The text might have '0x' on it. We should remove it. */
            if ((arr[pos] == '0') && (pos < (count - 1))) {
                if ((arr[pos+1] == 'x') || (arr[pos+1] == 'X'))
                    pos += 2;
            }
        }

        while (pos < count) {
            c = arr[pos++];

            if (Character.isDigit(c))
                val = Character.digit(c, 10);
            else if (Character.isLetter(c))
                val = Character.digit(c, 16);
            else
                break;                  /* Invalid char. */

            if (val >= radix) break;    /* Another invalid value. */
            result = result * radix + val;  /* NOTE: possibility of ArithmeticException. */
        }

        /* End of the conversion. Negate the number if we found a sign. */
        return (negative ? -result : result);
    }/*}}}*/
    // public static float  toFloat(String num);/*{{{*/
    /**
     * Converts a string to a float value.
     * \param num The string to be converted.
     * \return A float value result of the string convertion. If the string
     * doesn't represent a valid float value the return will be zero.
     **/
    public static float  toFloat(String num) {
        try { return Float.parseFloat(num); }
        catch (Exception ex) { /* we will ignore this for while. */ }
        return .0F;
    }/*}}}*/
    // public static char[] getChars(String s);/*{{{*/
    /**
     * Gets an array of characters for this string.
     * \param s The string to convert in a character array.
     * \return An array of chars.
     * \see getChars(String,int,int)
     **/
    public static char[] getChars(String s) {
        return strings.getChars(s, 0, -1);
    }/*}}}*/
    // public static char[] getChars(String s, int start, int count);/*{{{*/
    /**
     * Gets an array of characters from a substring of \a s.
     * \param s The string to get the characters.
     * \param start The starting index of the source \a s.
     * \param count Total number of characters to convert. If less than zero
     * the total length of the string, starting at \a start, is converted.
     * \return The array of characters.
     * \remarks When \a s is null or zero length, the result will be \b null.
     * If the final position (start + count) is greater than the total length
     * of the string, only the available characters are returned. When \a
     * start is invalid (out side the bounds of the string) the result will
     * also be \b null.
     **/
    public static char[] getChars(String s, int start, int count) {
        int limit = strings.length(s);
        if (limit == 0) return null;
        if ((start < 0) || (start >= limit)) return null;
        if ((count < 0) || ((start + count) > limit)) {
            count = limit - start;
        }
        char[] result = new char[count];
        try { s.getChars(start, count, result, 0); }
        catch (Exception ex) { result = null; }
        return result;
    }/*}}}*/
    // public static String toString(byte[] array, int start, int count, String enc);/*{{{*/
    /**
     * Convert a byte array in a string using the specified encoding.
     * \copydetails strings#decode()
     **/
    public static String toString(byte[] array, int start, int count, String enc) {
        return decode(array, start, count, enc);
    }/*}}}*/
    // public static String toString(byte[] array, int start, int count);/*{{{*/
    /**
     * Converts a byte array to a string object.
     * This function recognizes the character encoding through the BOM at the
     * begining of the array. The following encodings are recognized as the
     * sequence of BOM bytes:
     * - UTF-8: EF BB BF.
     * - UTF-16be: FE FF.
     * - UTF-16le: FF FE.
     * - UTF-32be: 00 00 FE FF.
     * - UTF-32le: FF FE 00 00.
     * - ISO-8859-1: When no BOM is found.
     * .
     * \param array The byte array to be converted.
     * \param start The start offset in the byte \a array.
     * \param count The number of bytes to convert.
     * \return On success a \b String object is returned. On failure the
     * function returns \b null.
     **/
    public static String toString(byte[] array, int start, int count) {
        if ((array[0] == (byte)0xEF) && (array[1] == (byte)0xBB) && (array[2] == (byte)0xBF))
            return strings.decode(array, (start + 3), (count - 3), ENC.UTF8);
        else if ((array[0] == (byte)0xFE) && (array[1] == (byte)0xFF))
            return strings.decode(array, (start + 2), (count - 2), ENC.UTF16BE);
        else if ((array[0] == (byte)0xFF) && (array[1] == (byte)0xFE) && (array[2] != 0x00))
            return strings.decode(array, (start + 2), (count - 2), ENC.UTF16LE);
        else if ((array[0] == 0x00) && (array[1] == 0x00) && (array[2] == (byte)0xFE) && (array[3] == (byte)0xFF))
            return strings.decode(array, (start + 4), (count - 4), ENC.UTF32BE);
        else if ((array[0] == (byte)0xFF) && (array[1] == (byte)0xFE) && (array[2] == 0x00) && (array[3] == 0x00))
            return strings.decode(array, (start + 4), (count - 4), ENC.UTF32LE);
        else
            return strings.decode(array, start, count, ENC.LATIN1);
    }/*}}}*/
    // public static String format(String fmt, Object... args);/*{{{*/
    /**
     * Returns a localized formatted string, using the supplied format and
     * arguments, using the user's default locale.
     * \param fmt The formater string.
     * \param args The list of arguments to format.
     * \returns The formated string.
     * \note No exception is thrown in this function. Instead of error the
     * result will be \b null.
     **/
    public static String format(String fmt, Object... args) {
        String result = null;
        try { result = String.format(java.util.Locale.US, fmt, args); }
        catch (Exception ex) { /* Nothing need to be done. */ }
        return result;
    }/*}}}*/
    // public static int    length(String s);/*{{{*/
    /**
     * Safely gets the length of a string.
     * \param s The string to get the length of.
     * \returns The length of the string, in characters, or zero, if the
     * string is empty or \b null.
     **/
    public static int length(String s) {
        return ((s == null) ? 0 : s.length());
    }/*}}}*/
    // public static String decode(byte[] array, int start, int count, String enc);/*{{{*/
    /**
     * Decodes a byte array into a String value.
     * \param array The byte array holding the string value.
     * \param start The starting point, int the array \a a, where the string
     * starts.
     * \param count The length of the string value in the array \a a, byte
     * count.
     * \param enc The identifier specifing how the string was encoded into the
     * array \a a. Possible values are listed in defs#ENC.
     * \returns A string with the value decoded from the array means success.
     * A zero length string means that the string has zero length. \b null
     * means decoding error.
     **/
    public static String decode(byte[] array, int start, int count, String enc) {
        /* We must remove all zero bytes becouse the constructor doesn't do
         * that.
         */
        byte[] temp;
        int length = 0;
        if (enc.equals(ENC.ASCII) || enc.equals(ENC.LATIN1) || enc.equals(ENC.UTF8))
        {
            for (int i = 0; i < count; i++) {
                if (array[i+start] == 0x00)
                    break;
                length++;
            }
        }
        else if (enc.equals(ENC.UTF16) || enc.equals(ENC.UTF16LE) || enc.equals(ENC.UTF16BE))
        {
            for (int i = 0; i < count; i+=2) {
                if ((array[i+start] == 0x00) && (array[i+start+1] == 0x00))
                    break;
                length+=2;
            }
        }
        else
        {
            for (int i = 0; i < count; i+=4) {
                if (arrays.littleEndToInt(array, i+start) == 0)
                    break;
                length+=4;
            }
        }

        if (length == 0) return EMPTY;
        temp = arrays.slice(array, start, length);

        try { return new String(temp, 0, temp.length, enc); }
        catch (Exception ex) { return null; }
    }/*}}}*/
    // public static byte[] encode(String text, String enc);/*{{{*/
    /**
     * Encode a string into a byte array using the specified encoding.
     * \param text The string to convert in a byte array.
     * \param enc The encoding to encode the string into the byte array.
     * \return The result array on success. \b null if some error occurs.
     * \remarks This function will use \c String.getBytes() method to convert
     * the string object into a byte array using the specified encoding. No
     * exception will be thrown. When the convertion cannot be done, a \b null
     * array will be returned.
     **/
    public static byte[] encode(String text, String enc)
    {
        byte[] array;
        try { array = text.getBytes(enc); }
        catch (Exception ex) {
            return null;
        }
        return array;
    }/*}}}*/
    // public static byte[] encode(String text, String enc, int len);/*{{{*/
    /**
     * Encode a string into a byte array using the specified encoding.
     * \param text The string to convert in a byte array.
     * \param enc The encoding to encode the string into the byte array.
     * \param len The length for the resulting array. This is useful
     * when encoding a string to put in a binary file. If the length of the
     * converted array is bigger than this value, the resulbe will be
     * truncated. When the converted array length is less than this value, the
     * excedent space is filled with zeroes.
     * \return The result array on success. If an error occurs the resulting
     * array will be filled with zeroes.
     **/
    public static byte[] encode(String text, String enc, int len)
    {
        byte[] result = new byte[len];
        byte[] array  = encode(text, enc);

        arrays.set(result, (byte)0);
        if (array == null) return result;

        int limit = Math.min(len, array.length);
        arrays.copy(result, 0, array, 0, limit);
        return result;
    }/*}}}*/

    // public static boolean isHexChar(char c);/*{{{*/
    /**
     * Checks if a character can represent an hexadecimal number.
     * \param c The character to check.
     * \returns \b true if the character can represent an hexadecimal number,
     * \b false otherwise.
     **/
    public static boolean isHexChar(char c) {
        return ("0123456789ABCDEFabcdef".indexOf(c) >= 0);
    }/*}}}*/

    public static final String EMPTY = "";      /**< Empty string object. */
}
// vim:syntax=java.doxygen
