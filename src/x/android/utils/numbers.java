/**
 * \file
 * Defines the numbers class.
 *
 * \author Alessandro Antonello <aantonello@paralaxe.com.br>
 * \date   Dezembro 17, 2012
 *
 * \par Copyright
 * This file is provided in hope that it will be useful to someone. It is
 * offered in public domain. You may use, modify or distribute it freely.
 *
 * The code is provided "AS IS". There is no warranty at all, of any kind. You
 * may change it if you like. Or just use it as it is.
 */
package x.android.utils;

/**
 * Static class providing common numbers operations.
 *//* --------------------------------------------------------------------- */
public final class numbers
{
    /** \name REVERSE FUNCTIONS */ //@{
    // public static short reverse(short value);/*{{{*/
    /**
     * Reverses the byte order of a value.
     * \param value Value to be reversed.
     * \return The \a value reversed. If the parameter is in little-endian the
     * return will be in big-endian and vise-versa.
     **/
    public static short reverse(short value) {
        return Short.reverseBytes(value);
    }/*}}}*/
    // public static int   reverse(int value);/*{{{*/
    /**
     * \copydoc numbers::reverse(short)
     **/
    public static int reverse(int value) {
        return Integer.reverseBytes(value);
    }/*}}}*/
    // public static long  reverse(long value);/*{{{*/
    /**
     * \copydoc numbers::reverse(short)
     **/
    public static long  reverse(long value) {
        return Long.reverseBytes(value);
    }/*}}}*/
    //@}

    /** \name NUMBER DIVIDERS */ //@{
    // public static int   lonibble(byte value);/*{{{*/
    /**
     * Gets the low order nibble of a byte value.
     * \param value The byte value to divide.
     * \return The low order nibble in an integer value.
     **/
    public static int   lonibble(byte value) {
        return (value & 0x0F);
    }/*}}}*/
    // public static int   hinibble(byte value);/*{{{*/
    /**
     * Gets the high order nibble of a byte.
     * \param value byte value to divide.
     * \return The high order nibble as an **int** value.
     **/
    public static int   hinibble(byte value) {
        return ((value >> 4) & 0x0F);
    }/*}}}*/
    // public static byte  lobyte(short value);/*{{{*/
    /**
     * Gets the low order byte of a shoft value.
     * \param value The short value to divide.
     * \return The low order byte value.
     **/
    public static byte  lobyte(short value) {
        return (byte)(value & 0x00FF);
    }/*}}}*/
    // public static byte  hibyte(short value);/*{{{*/
    /**
     * Returns the high order byte of a short value.
     * \param value short value to divide.
     * \return The high order byte value.
     **/
    public static byte  hibyte(short value) {
        return (byte)((value >> 8) & 0x00FF);
    }/*}}}*/
    // public static short loword(int value);/*{{{*/
    /**
     * Gets the low order word (2 bytes) of an integer.
     * \param value The integer value to divide.
     * \return The low order word, as a **short** number.
     **/
    public static short loword(int value) {
        return (short)(value & 0x0000FFFF);
    }/*}}}*/
    // public static short hiword(int value);/*{{{*/
    /**
     * Returns the high order word (2 bytes) of an integer.
     * \param value The integer to divide.
     * \return The high order word as a **short** number.
     **/
    public static short hiword(int value) {
        return (short)((value >> 16) & 0x0000FFFF);
    }/*}}}*/
    // public static int   lodword(long value);/*{{{*/
    /**
     * Returns the low order double word (4 bytes) from a long value.
     * \param value Long value to divide.
     * \return A low order double word as an **int** number.
     **/
    public static int lodword(long value) {
        return (int)value;
    }/*}}}*/
    // public static int   hidword(long value);/*{{{*/
    /**
     * Returns the high order double word (4 bytes) of a long value.
     * \param value Long value to divide.
     * \return The high order double word as an **int** number.
     **/
    public static int hidword(long value) {
        return (int)(value >> 32);
    }/*}}}*/
    //@}

    /** \name NUMBERS CASTING */ //@{
    // public static short toShort(byte value);/*{{{*/
    /**
     * Cast a value to a *short integer*.
     * \param value Value to cast.
     * \returns A **short** value, result of the cast, in standard Big-Endian
     * Java format.
     **/
    public static short toShort(byte value) {
        return numbers.word((byte)0, value);
    }/*}}}*/
    // public static int   toInt(short value);/*{{{*/
    /**
     * Cast a value to an *integer*.
     * \param value Value to cast.
     * \return An **int** value, result of the cast, in standard Big-Endian
     * Java format.
     **/
    public static int toInt(short value) {
        return numbers.dword((short)0, value);
    }/*}}}*/
    // public static int   toInt(byte value);/*{{{*/
    /**
     * \copydoc numbers::toInt(short)
     **/
    public static int toInt(byte value) {
        return numbers.toInt(numbers.toShort(value));
    }/*}}}*/
    // public static long  toLong(int value);/*{{{*/
    /**
     * Cast a value to a *long integer*.
     * \param value Value to cast.
     * \return A *Long* value, result of the cast, in standard Big-Endian Java
     * format.
     **/
    public static long  toLong(int value) {
        return numbers.qword(0, value);
    }/*}}}*/
    // public static long  toLong(short value);/*{{{*/
    /**
     * \copydoc numbers::toLong(int)
     **/
    public static long  toLong(short value) {
        return numbers.toLong(numbers.toInt(value));
    }/*}}}*/
    // public static long  toLong(byte value);/*{{{*/
    /**
     * \copydoc numbers::toLong(int)
     **/
    public static long  toLong(byte value) {
        return numbers.toLong(numbers.toInt(numbers.toShort(value)));
    }/*}}}*/

    // public static short byteToShort(byte unsignedByte);/*{{{*/
    /**
     * Converts a **byte** value to **short**.
     * The conversion is done as of the **byte** value was unsigned. That is,
     * if the byte value is less than zero, the conversion will produce a
     * positive number, as of the byte value was unsigned.
     * \param unsignedByte The **byte** value to convert to **short**.
     * \return A positive **short** value.
     **/
    public static short byteToShort(byte unsignedByte) {
        return (short)(0x00FF & unsignedByte);
    }/*}}}*/
    // public static int   byteToInt(byte value);/*{{{*/
    /**
     * Converts a byte value into an integer.
     * The conversion will be done like the **byte** value was unsigned. That
     * is, if the byte value has a negative number, the result integer will be
     * a positive one.
     * \param unsignedByte The **byte** value to convert to **int**.
     * \return The **int** value. Unsigned.
     **/
    public static int byteToInt(byte unsignedByte) {
        return (0x000000FF & unsignedByte);
    }/*}}}*/
    // public static long  byteToLong(byte unsignedByte);/*{{{*/
    /**
     * Converts a **byte** value to **long**.
     * The conversion is done as of the **byte** value was unsigned. That is,
     * if the byte value is less than zero, the conversion will produce a
     * positive number, as of the byte value was unsigned.
     * \param unsignedByte The **byte** value to convert to **long**.
     * \return A positive **long** value.
     **/
    public static long byteToLong(byte unsignedByte) {
        return (0x00000000000000FF & unsignedByte);
    }/*}}}*/

    // public static int   shortToInt(short shortValue);/*{{{*/
    /**
     * Converts a **short** value to a positive **int** number.
     * The conversion always produces a positive number. That is, even the
     * *shortValue* is negative, the result will be a positive number, as of
     * the *shortValue* was ungined.
     * \param shortValue The **short** value to convert.
     * \return A positive **int** value result of the conversion.
     **/
    public static int shortToInt(short shortValue) {
        return (0x0000FFFF & shortValue);
    }/*}}}*/
    // public static long  shortToLong(short shortValue);/*{{{*/
    /**
     * Converts a **short** value to a positive **long** number.
     * The conversion always produces a positive number. That is, even the
     * *shortValue* is negative, the result will be a positive number, as of
     * the *shortValue* was ungined.
     * \param shortValue The **short** value to convert.
     * \return A positive **long** value result of the conversion.
     **/
    public static long shortToLong(short shortValue) {
        return (0x000000000000FFFF & shortValue);
    }/*}}}*/

    // public static long  intToLong(int intValue);/*{{{*/
    /**
     * Cast an **int** value into a positive **long** number.
     * Even if the *intValue* is negative, the result will be a positive
     * number as of the *intValue* was unsigned.
     * \param intValue **int** value to cast.
     * \return A positive **long** number.
     **/
    public static long intToLong(int intValue) {
        return (0x00000000FFFFFFFF & intValue);
    }/*}}}*/
    //@}

    /** \name NUMBER MAKERS */ //@{
    // public static long  qword(int highOrderInt, int lowOrderInt);/*{{{*/
    /**
     * Build a long number from two others integer.
     * \param highOrderInt The high order integer of the value.
     * \param lowOrderInt The low order integer of the value.
     * \returns A long value composed from the two integers.
     **/
    public static long qword(int highOrderInt, int lowOrderInt) {
        return ((intToLong(highOrderInt) << 32) | intToLong(lowOrderInt));
    }/*}}}*/
    // public static int   dword(short highOrderWord, short lowOrderWord);/*{{{*/
    /**
     * Build an integer number from two others short values.
     * \param highOrderWord The high order short of the value.
     * \param lowOrderWord The low order short of the value.
     * \returns An integer value composed from the two shorts.
     **/
    public static int dword(short highOrderWord, short lowOrderWord) {
        return ((shortToInt(highOrderWord) << 16) | shortToInt(lowOrderWord));
    }/*}}}*/
    // public static short word(byte highOrderByte, byte lowOrderByte);/*{{{*/
    /**
     * Build a short number from two others bytes.
     * \param highOrderByte The high order byte of the value.
     * \param lowOrderByte The low order byte of the value.
     * \returns A short value composed from the two bytes.
     **/
    public static short word(byte highOrderByte, byte lowOrderByte) {
        return (short)((byteToShort(highOrderByte) << 8) | byteToShort(lowOrderByte));
    }/*}}}*/
    // public static byte  makebyte(byte highOrderNibble, byte lowOrderNibble);/*{{{*/
    /**
     * Constructs a **byte** number from values of two nibbles.
     * \param highOrderNibble The value of the high order nibble.
     * \param lowOrderNibble The value of the low order nibble.
     * \return A **byte** value constructed from two nibbles.
     **/
    public static byte  makebyte(byte highOrderNibble, byte lowOrderNibble) {
        return (byte)(((highOrderNibble & 0x0F) << 4) | (lowOrderNibble & 0x0F));
    }/*}}}*/
    //@}
}
// vim:syntax=java.doxygen
