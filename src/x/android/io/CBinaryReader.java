/**
 * \file
 * Defines the CBinaryReader class.
 *
 * \author Alessandro Antonello <aantonello@paralaxe.com.br>
 * \date   October 01, 2010
 *
 * \par Copyright
 * This file is provided in hope that it will be useful to someone. It is
 * offered in public domain. You may use, modify or distribute it freely.
 *
 * The code is provided "AS IS". There is no warranty at all, of any kind. You
 * may change it if you like. Or just use it as it is.
 */
package x.android.io;

import java.io.DataInput;
import x.android.defs.ENC;
import x.android.utils.*;

/**
 * \ingroup x_android_io
 * Implements a binary data reader.
 * This class is useful to read binary data, that is, stored as byte arrays,
 * and convert data into native Java types.
 *//* --------------------------------------------------------------------- */
public class CBinaryReader implements DataInput
{
    /** \name CONSTRUCTORS */ //@{
    // public CBinaryReader(byte[] data);/*{{{*/
    /**
     * Class constructor.
     * \param data Array to be read.
     **/
    public CBinaryReader(byte[] data) {
        m_data = data;
        m_curr = 0;
    }/*}}}*/
    // public CBinaryReader(byte[] data, int offset, int count);/*{{{*/
    /**
     * Class constructor.
     * \param data Array to be read.
     * \param offset Index of the first element in the array where the read
     * should start.
     * \param count The number of elements, in the array, starting from \a
     * offset, that will be read.
     * \remarks The data array is sliced in this class.
     **/
    public CBinaryReader(byte[] data, int offset, int count) {
        m_curr = 0;
        m_data = new byte[count];
        arrays.copy(m_data, 0, data, offset, count);
    }/*}}}*/
    //@}

    /** \name ATTRIBUTES */ //@{
    // public int length();/*{{{*/
    /**
     * Gets the length of the data in this instance.
     **/
    public int length() {
        return arrays.length(m_data);
    }/*}}}*/
    // public int available();/*{{{*/
    /**
     * Gets the number of bytes available to be read.
     * This count takes in account the number of bytes already read.
     **/
    public int available() {
        return (this.length() - m_curr);
    }/*}}}*/
    //@}

    /** \name CONVERSION OPERATIONS */ //@{
    // public boolean readBoolean();/*{{{*/
    /**
     * Reads a boolean value.
     * A single byte is read.
     * \return If the byte is different from zero the function returns \b
     * true. Otherwise the function returns \b false.
     **/
    public boolean readBoolean() {
        return (read() != 0);
    }/*}}}*/
    // public byte    readByte();/*{{{*/
    /**
     * Reads one byte from the array.
     * \returns The byte read.
     **/
    public byte    readByte() {
        return (byte)(read() & 0x000000FF);
    }/*}}}*/
    // public char    readChar();/*{{{*/
    /**
     * Reads two bytes of the array converting it in a char type.
     **/
    public char    readChar() {
        int result = ((read() << 8) | read());
        return (char)(result & 0x0000FFFF);
    }/*}}}*/
    // public short   readShort();/*{{{*/
    /**
     * Reads two bytes of the array and returns a short value.
     **/
    public short   readShort() {
        if (available() < 2) return 0;
        short result = arrays.bigEndToShort(m_data, m_curr);
        m_curr += 2;
        return result;
    }/*}}}*/
    // public int     readInt();/*{{{*/
    /**
     * Reads four bytes from the array and returns an integer value.
     **/
    public int     readInt() {
        if (available() < 4) return 0;
        int result = arrays.bigEndToInt(m_data, m_curr);
        m_curr += 4;
        return result;
    }/*}}}*/
    // public long    readLong();/*{{{*/
    /**
     * Reads eight bytes from the array and returns a long value.
     **/
    public long    readLong() {
        if (available() < 8) return 0L;
        long result = arrays.bigEndToLong(m_data, m_curr);
        m_curr += 8;
        return result;
    }/*}}}*/
    // public float   readFloat();/*{{{*/
    /**
     * Reads four bytes from the array and returns a float value.
     * \note This operation is not implemented.
     **/
    public float   readFloat() {
        return (float)0.0;
    }/*}}}*/
    // public double  readDouble();/*{{{*/
    /**
     * Reads eight bytes from the array and returns a double value.
     * \note This operation is not implemented.
     **/
    public double  readDouble() {
        return 0.0;
    }/*}}}*/
    // public String  readUTF();/*{{{*/
    /**
     * Returns a string in the modified UTF-8 format.
     * This function expects that the string was written using \c writeUTF()
     * function. The first two bytes of the set contain the length of the
     * string, in characters. The remaining bytes have the string in the
     * modified UTF-8 format.
     **/
    public String  readUTF() {
        if (available() < 2) return null;

        int uLength = readUnsignedShort();
        int pos     = m_curr;
        int total   = m_data.length;
        int counter = 0;                /* Number of bytes read. */
        byte temp;                      /* To be used as temporary. */
        char[] str;                     /* Array of characters. */

        if (available() < uLength) return null;
        str = new char[uLength];

        /* Does the reading. */
        while (pos < total)
        {
            /* If the first byte matches the pattern 0xxx xxxx (binary value
             * wher x means '0' or '1'), then this is the only byte of the
             * char.
             */
            if ((m_data[pos] & 0x80) == 0x00)
                str[counter] = (char)(m_data[pos] & 0xFF);

            /* If the first byte matches the bit pattern 110x xxxx then the
             * character is built of two bytes.
             */
            else if ((m_data[pos] & 0xC0) == 0xC0) {
                str[counter] = (char)(((m_data[pos] & 0x1F) << 6) | (m_data[++pos] & 0x3F));
            }

            /* If the first byte matches the bit pattern 1110 xxxx then the
             * character is built of three bytes.
             */
            else if ((m_data[pos] & 0xE0) == 0xE0) {
                str[counter] = (char)(((m_data[pos] & 0x0F) << 12) |
                                      ((m_data[++pos] & 0x3F) << 6) |
                                      (m_data[++pos] & 0x3F));
            }

            /* Otherwise we have an encoding error. */
            else {
                break;
            }
            pos++;
            counter++;
        }

        String result;

        /* Converting the character sequence into a String object. */
        try {
            result = new String(str, 0, counter);
        }
        catch (Exception ex) {
            debug.e("CBinaryReader::readUTF()\n:: %s", ex);
            return null;
        }
        m_curr += pos;          /* Updates the current position. */
        return result;
    }/*}}}*/
    // public String  readLine();/*{{{*/
    /**
     * Reads a line of text from the byte array.
     * The function expects that this line had be encoded in ISO-8859-1
     * character set. Bytes are read in sequence until a carriage return
     * ('\\r') or line feed ('\\n') characters are found. Then, the character
     * is removed from the portion of the array read and the result is
     * converted to a string object.
     **/
    public String  readLine() {
        int index = m_curr;
        int limit = arrays.length(m_data);
        String result = null;

        while (index < limit) {
            if ((m_data[index] == (byte)'\r') || (m_data[index] == (byte)'\n')) {
                result = strings.decode(m_data, m_curr, (index - m_curr), ENC.LATIN1);
                index++;
                if ((index < limit) && ((m_data[index] == (byte)'\r') || (m_data[index] == (byte)'\n')))
                    index++;

                break;
            }
            index++;
        }
        m_curr = index;
        return result;
    }/*}}}*/
    // public int     readUnsignedByte();/*{{{*/
    /**
     * Reads one byte of the array and returns its value.
     * \returns An int value to keep the byte value unsigned.
     **/
    public int     readUnsignedByte() {
        return (int)(readByte() & 0x000000FF);
    }/*}}}*/
    // public int     readUnsignedShort();/*{{{*/
    /**
     * Reads two bytes from the array and returns its value.
     * \returns A int value to keep the short value unsigned.
     **/
    public int     readUnsignedShort() {
        return (int)(readShort() & 0x0000FFFF);
    }/*}}}*/
    // public void    readFully(byte[] buffer);/*{{{*/
    /**
     * Reads data into the passed buffer.
     * \param buffer Array to copy the internal data. The function will copy
     * up to \e buffer.length bytes or until the internal array where
     * exhausted.
     **/
    public void    readFully(byte[] buffer) {
        readFully(buffer, 0, arrays.length(buffer));
    }/*}}}*/
    // public void    readFully(byte[] buffer, int offset, int count);/*{{{*/
    /**
     * Read byte from the array, storing them in the given buffer.
     * \param buffer Array to copy the read data.
     * \param offset Index where the first byte should be copied into \a
     * buffer.
     * \param count Total number of bytes to copy.
     **/
    public void    readFully(byte[] buffer, int offset, int count) {
        read(buffer, offset, count);
    }/*}}}*/
    // public int     skipBytes(int count);/*{{{*/
    /**
     * Skip the amount of bytes passed.
     * \param count The number of bytes to skip.
     * \return The amount of bytes actually skipped. This can be less than \a
     * count if there is not enough elements in the internal array.
     **/
    public int     skipBytes(int count) {
        int total = available();
        if (total < count) count = total;
        m_curr += count;
        return count;
    }/*}}}*/
    //@}

    /** \name RAW READ OPERATIONS */ //@{
    // public int     read();/*{{{*/
    /**
     * Read a single byte from the array.
     * \returns The byte value as an integer.
     **/
    public int     read() {
        if (available() < 1) return 0;
        int value = (int)(m_data[m_curr] & 0x000000FF);
        m_curr++;
        return value;
    }/*}}}*/
    // public int     read(byte[] buff);/*{{{*/
    /**
     * Read bytes from the array, storing them in the given buffer.
     * \param buff Buffer to copy the data read.
     * \returns The number of bytes actually copied.
     **/
    public int     read(byte[] buff) {
        return read(buff, 0, arrays.length(buff));
    }/*}}}*/
    // public int     read(byte[] buff, int offset, int count);/*{{{*/
    /**
     * Read byte from the array, storing them in the given buffer.
     * \param buff Array to copy the read data.
     * \param offset Index where the first byte should be copied into \a
     * buffer.
     * \param count Total number of bytes to copy.
     **/
    public int     read(byte[] buff, int offset, int count) {
        int total = Math.min(count, available());
        arrays.copy(buff, offset, m_data, m_curr, total);
        m_curr += total;
        return total;
    }/*}}}*/
    // public String  readString(int count, String encoding);/*{{{*/
    /**
     * Reads an encoded string from the array.
     * \param count The number of bytes to read. The function will read this
     * amount of bytes unless they are less than the available amount of
     * elements in the internal array. The string length can be less than this
     * amount. The function will search for a zero byte value thats marks the
     * end of the string. This zero byte is not passed in the result string.
     * \param encoding The encoding of the string. This can be "UTF-8" or
     * "ISO-8859-1" or "ISO-8859-2" or whatever was encoded the string.
     * \returns The result string of the reading operation and the data
     * conversion.
     **/
    public String  readString(int count, String encoding) {
        int total = Math.min(count, available());
        int end   = (m_curr + total) - 1;

        /* We search in the reverse order. When there is not more zero bytes,
         * the end of the string was found.
         */
        while ((end > m_curr) && (m_data[end] == 0x00))
            end--;

        if (end == m_curr) return null;

        /* Convert only valid bytes. */
        String text = strings.decode(m_data, m_curr, ((end+1) - m_curr), encoding);
        m_curr += total;

        return text;
    }/*}}}*/
    //@}

    /** \name OPERATIONS */ //@{
    // public void reset();/*{{{*/
    /**
     * Resets the current position to the beginning.
     **/
    public void reset() {
        if (m_data == null) return;
        m_curr = 0;
    }/*}}}*/
    // public void assign(byte[] data);/*{{{*/
    /**
     * Reassigns or changes the current array.
     * \param data The new array to be used.
     * \remarks This function also resets the read position to the start.
     **/
    public void assign(byte[] data) {
        m_data = data;
        m_curr = 0;
    }/*}}}*/
    // public void assign(byte[] data, int offset, int count);/*{{{*/
    /**
     * Replaces the internal array with a portion of the given data.
     * \param data Array to be used.
     * \param offset The first byte to be used to read.
     * \param count The total bytes to read starting from \a offset.
     * \remarks The portion of the array will be internally copied to a new
     * buffer.
     **/
    public void assign(byte[] data, int offset, int count) {
        m_data = new byte[ count ];
        m_curr = 0;
        arrays.copy(m_data, 0, data, offset, count);
    }/*}}}*/
    //@}

    /** \name DATA MEMBERS */ //@{
    protected byte[] m_data;        /**< Data to be read. */
    protected int    m_curr;        /**< Current position. */
    //@}
}
// vim:syntax=java.doxygen
