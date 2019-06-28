/**
 * \file
 * Defines the CBinaryWriter class.
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

import java.io.DataOutput;
import x.android.defs.*;
import x.android.utils.*;

/**
 * \ingroup x_android_io
 * Writes raw Java types to a byte array.
 *//* --------------------------------------------------------------------- */
public class CBinaryWriter implements DataOutput
{
    /** \name CONSTRUCTOR */ //@{
    /**
     * Default constructor.
     **/
    public CBinaryWriter() {
        m_buff = null;
        m_curr = -1;
        m_grow = true;
    }

    /**
     * Parametrized constructor.
     * \param buffer Array to be used as written output. Just a reference is
     * kept. The internal arrays is not growable.
     **/
    public CBinaryWriter(byte[] buffer) {
        m_buff = buffer;
        m_curr = 0;
        m_grow = false;
    }

    /**
     * Parametrized constructor.
     * \param capacity The initial capacity of the internal buffer. It will be
     * allocated in place. This capacity is only a hint. The internal buffer
     * will grow, if needed.
     **/
    public CBinaryWriter(int capacity) {
        m_buff = new byte[ capacity ];
        m_curr = 0;
        m_grow = true;
    }
    //@}

    /** \name ATTRIBUTES */ //@{
    /**
     * Gets the current length of the internal buffer.
     **/
    public int length() {
        return arrays.length(m_buff);
    }

    /**
     * Gets the current available space in the internal buffer.
     **/
    public int available() {
        return (arrays.length(m_buff) - m_curr);
    }

    /**
     * Gets the internal buffer.
     **/
    public byte[] get() {
        return m_buff;
    }
    //@}

    /** \name DataOuput IMPLEMENTATION */ //@{
    /**
     * Writes a single byte in the array.
     * \param value The boolean value to be written. If this value is \b true
     * a byte with value one will be written. Otherwise a byte with value zero
     * will be written.
     **/
    public void writeBoolean(boolean value) {
        write( value ? 1 : 0 );
    }

    /**
     * Writes one byte of the array with the given value.
     * \param aByte An integer value which will be cast to a byte.
     **/
    public void writeByte(int aByte) {
        write( aByte );
    }

    /**
     * Writes a series of bytes in the internal buffer.
     * \param data A string that will be converted to an ASCII encoded
     * array of bytes.
     **/
    public void writeBytes(String data) {
        int len = ((data == null) ? 0 : data.length());
        if (!grow(len+1)) return;

        byte[] bytes = data.getBytes();
        arrays.copy(m_buff, m_curr, bytes, 0, len);
        m_curr += len;

        /* A terminator is added to the end of the data. */
        m_buff[ m_curr++ ] = 0x00;
    }

    /**
     * Writes a character value to the array.
     * \param aChar An integer which the character value will be added.
     **/
    public void writeChar(int aChar) {
        if (!grow(2)) return;
        m_buff[ m_curr++ ] = (byte)(0xFF & (aChar >> 8));
        m_buff[ m_curr++ ] = (byte)(0xFF & aChar);
    }

    /**
     * Writes a string using UTF-16 character set.
     * \param data The string to be written. In the data buffer a zero value
     * character will be added to end the string in the internal array.
     **/
    public void writeChars(String data) {
        int len = ((data == null) ? 0 : data.length());

        if (!grow( (len * 2) + 2 )) return;
        char[] cl = data.toCharArray();

        for (int i = 0; i < len; i++) {
            m_buff[m_curr++] = (byte)(0xFF & (cl[i] >> 8));
            m_buff[m_curr++] = (byte)(0xFF & cl[i]);
        }

        /* Add the terminator, 2 bytes for it. */
        m_buff[m_curr++] = 0x00;
        m_buff[m_curr++] = 0x00;
    }

    /**
     * Write a short value into the array.
     * \param aShort An integer which the short value to be written.
     **/
    public void writeShort(int aShort) {
        if (!grow(2)) return;
        m_buff[ m_curr++ ] = (byte)(0xFF & (aShort >> 8));
        m_buff[ m_curr++ ] = (byte)(0xFF & aShort);
    }

    /**
     * Writes an integer value to the array.
     * \param value The integer value to be added.
     **/
    public void writeInt(int value) {
        if (!grow(4)) return;
        arrays.intAsBigEnd(m_buff, m_curr, value);
        m_curr += 4;
    }

    /**
     * Writes a long value to the array.
     * \param value The long value to be written.
     **/
    public void writeLong(long value) {
        if (!grow(8)) return;
        arrays.longAsBigEnd(m_buff, m_curr, value);
        m_curr += 8;
    }

    /**
     * Writes a float value to the array.
     * \param value The float value to be written.
     * \remarks This function is not implemented.
     **/
    public void writeFloat(float value) {
        return;
    }

    /**
     * Writes a double value to the array.
     * \param value The double value to be written.
     * \remarks This function is not implemented.
     **/
    public void writeDouble(double value) {
        return;
    }

    /**
     * Converts a string to the modified UTF-8 encoding writing it to the
     * array.
     * \param value The string to be written.
     **/
    public void writeUTF(String value) {
        char[] data = value.toCharArray();
        int    temp;
        int    pos  = m_curr;           /* We will back this position later. */
        int   count = data.length;
        
        /* The first 2 bytes carries the number of bytes needed to pack the
         * string. We don't know this value until later. So, we check if there
         * is room and jump to the next location.
         */
        if (!grow(2)) return;
        m_curr += 2;

        for (int i = 0; i < count; i++)
        {
            temp = (int)data[i];

            /* If the character is in range from 0x0001 to 0x007F it is stored
             * as a single character.
             */
            if ((temp >= 0x0001) && (temp <= 0x007F))
            {
                if (!grow(1)) break;
                m_buff[m_curr++] = (byte)(0xFF & temp);
            }

            /* If the character is in the range from 0x0080 through 0x07FF it
             * is represented as two bytes.
             */
            else if ((temp >= 0x0080) && (temp <= 0x07FF))
            {
                if (!grow(2)) break;
                m_buff[m_curr++] = (byte)(0xC0 | (0x1F & (temp >> 6)));
                m_buff[m_curr++] = (byte)(0x80 | (0x3F & temp));
            }

            /* If the character is in range from 0x0800 through 0xFFFF then it
             * is represented as three bytes.
             */
            else if ((temp >= 0x0800) && (temp <= 0xFFFF))
            {
                if (!grow(3)) break;
                m_buff[m_curr++] = (byte)(0xE0 | (0x0F & (temp >> 12)));
                m_buff[m_curr++] = (byte)(0x80 | (0x3F & (temp >> 6)));
                m_buff[m_curr++] = (byte)(0x80 | (0x3F & temp));
            }
        }
        /* The difference of 'm_curr' and the start position (pos) is the
         * number of bytes written to the buffer. We need to write this value
         * in the past 2 bytes jumped from the start of this function.
         */
        count = (m_curr - pos);
        m_buff[pos + 0] = (byte)(0xFF & (count >> 8));
        m_buff[pos + 1] = (byte)(0xFF & count);
    }

    /**
     * Writes one byte to the array.
     * \param aByte An integer which the byte value to be written.
     **/
    public void write(int aByte) {
        if (!grow(1)) return;
        m_buff[m_curr++] = (byte)(0x000000FF & aByte);
    }

    /**
     * Appends a byte array to the internal array.
     * \param data The byte array to be appended. No other data is appended to
     * set the size of this array.
     **/
    public void write(byte[] data) {
        int len = arrays.length(data);
        if (!grow(len)) return;
        arrays.copy(m_buff, m_curr, data, 0, len);
        m_curr += len;
    }

    /**
     * Appends data to the end of the internal array.
     * \param data The data buffer to append.
     * \param offset The first element from \a data to be appended.
     * \param count The number of elements, starting from \a offset, to append
     * in the internal array. If less than zero all elements, starting from \a
     * offset, will be appended.
     **/
    public void write(byte[] data, int offset, int count) {
        if ((data == null) || (count == 0)) return;

        int len = data.length;
        if (count > (len - offset)) count = (len - offset);
        if (count < 0) count = (len - offset);

        if (!grow(count)) return;
        arrays.copy(m_buff, m_curr, data, offset, count);
        m_curr += count;
    }
    //@}

    /** \name OPERATIONS */ //@{
    /**
     * Writes a string, using the specified encoding.
     * \param data The string to be written.
     * \param minLen The minimal buffer space to the string. If the string
     * length is less than this value the empty space will be filled with
     * zeroes.
     * \param encoding The enconding to convert the string. This can be
     * 'ISO-8859-1' or 'UTF-8' or any other valid encoding. If the required
     * encoding is not available then the string will be written using the \c
     * writeBytes(String) method.
     **/
    public void writeString(String data, int minLen, String encoding) {
        if (!grow(minLen)) return;

        byte[] bl = strings.encode(data, encoding);
        if (bl == null) {
            debug.w("CBinaryWriter::writeString('%s', '%s')", data, encoding);
            debug.w("** the specified encoding does not exists!!!");
            writeBytes(data);
            return;
        }

        arrays.set(m_buff, (byte)0x00, m_curr, minLen);
        arrays.copy(m_buff, m_curr, bl, 0, bl.length);
        m_curr += minLen;
    }

    /**
     * Resets the current position moving it to the beginning of the array.
     **/
    public void reset() {
        m_curr = ((m_buff != null) ? 0 : -1 );
    }

    /**
     * Replaces the internal array to the buffer specified.
     * \param buffer The buffer to replace the internal array. Just a
     * reference to this array is kept. This makes the internal buffer not
     * growable. Thats means the buffer needs to have enough room to add all
     * required data.
     **/
    public void reset(byte[] buffer) {
        m_buff = buffer;
        m_curr = 0;
        m_grow = false;
    }

    /**
     * Resets the internal capacity of the buffer.
     * \param capacity The total capacity of the internal buffer. This resets
     * any current content of the internal array. The internal array still can
     * grow if there is not enough room to add all required data.
     **/
    public void reset(int capacity) {
        m_buff = new byte[ capacity ];
        m_curr = 0;
        m_grow = true;
    }

    /**
     * Gets the size of the buffer that is fulfill.
     **/
    public int  size() {
        return m_curr;
    }
    //@}

    /** \name IMPLEMENTATION */ //@{
    /**
     * Grows the array, allocating more memory, if needed.
     * \param amount The amount of space required.
     * \return \b true if the operation succeeds. \b false otherwise.
     **/
    protected final boolean grow(int amount) {
        if (available() >= amount) return true;
        if (!m_grow) return false;

        int total = (amount - available()) + arrays.length(m_buff);
        m_buff = arrays.realloc(m_buff, total);
        m_curr = (m_curr < 0 ? 0 : m_curr);
        return (m_buff != null);
    }
    //@}

    /** \name DATA MEMBERS */ //@{
    protected byte[] m_buff;        /**< The data buffer.        */
    protected int    m_curr;        /**< Current write position. */
    protected boolean m_grow;       /**< Sets the growable property. */
    //@}
}
// vim:syntax=java.doxygen
