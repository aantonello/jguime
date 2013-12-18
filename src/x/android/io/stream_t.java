/**
 * \file
 * Defines the stream_t class.
 *
 * \author Alessandro Antonello <aantonello@paralaxe.com.br>
 * \date   Novembro 01, 2012
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
import java.io.DataOutput;
import java.io.InputStream;
import java.io.OutputStream;

import x.android.defs.ENC;
import x.android.defs.ERROR;
import x.android.utils.*;

/**
 * \ingroup x_android_io
 * A memory stream class capeable of read and write operations.
 * This class implements both \c DataInput interface and \c DataOutput
 * interface. It can be used to read and write operations at the same time.
 *
 * The class keeps tracking of read and write positions independently. The
 * internal buffer is reallocated as needed. The capacity of the buffer
 * independ of the number of data that it has. The actual number of valid
 * bytes in the buffer is defined by the current write position cursor. The
 * write position also defines how many bytes can be read in a read operation.
 *
 * Despite of what is stated in the Java documentation, no exception is thrown
 * by this class. The success or failure of a reading operation can be checked
 * by a single method invocation \c readStatus() that returns an error
 * condition of the last read operation. See \c ERROR class.
 *//* --------------------------------------------------------------------- */
public class stream_t implements DataInput, DataOutput
{
    /** \name CONSTRUCTOR */ //@{
    // public stream_t();/*{{{*/
    /**
     * Build an empty stream_t class.
     **/
    public stream_t() {
        m_data  = null;
        m_read  = 0;
        m_write = 0;
        m_lastRead  = 0;
        m_lastWrite = 0;
    }/*}}}*/
    // public stream_t(int capacity);/*{{{*/
    /**
     * Builds the stream_t instance defining an initial capacity.
     * \param capacity The initial capacity for the internal buffer.
     **/
    public stream_t(int capacity) {
        m_data      = new byte[capacity];
        m_read      = 0;
        m_write     = 0;
        m_lastRead  = 0;
        m_lastWrite = 0;
    }/*}}}*/
    // public stream_t(byte[] data);/*{{{*/
    /**
     * Builds a new stream_t instance copying the passed array.
     * \param data The array which data should initializa the internal buffer.
     * This array will define the initial capacity and, also, the write
     * position will be at end of the internal buffer.
     **/
    public stream_t(byte[] data) {
        m_data      = null;
        m_read      = 0;
        m_write     = 0;
        m_lastRead  = 0;
        m_lastWrite = 0;

        write(data);
    }/*}}}*/
    // public stream_t(byte[] data, int start, int count);/*{{{*/
    /**
     * Builds the new stream_t instance copying part of an array.
     * \param data The array whose part will initialize this stream.
     * \param start Index of the first byte to read in \a data array.
     * \param count Number of bytes to read starting of \a start.
     * \note The \a count parameter defines the initial capacity of the
     * internal buffer. Also, the write cursor position will be put at the end
     * of the buffer.
     **/
    public stream_t(byte[] data, int start, int count) {
        m_data      = null;
        m_read      = 0;
        m_write     = 0;
        m_lastRead  = 0;
        m_lastWrite = 0;

        write(data, start, count);
    }/*}}}*/
    //@}

    /** \name ATTRIBUTES */ //@{
    // public int length();/*{{{*/
    /**
     * Returns the current length of the internal buffer.
     * The length represents the total amount of bytes that can be read
     * starting from the first index.
     **/
    public int length() {
        return m_write;
    }/*}}}*/
    // public int capacity();/*{{{*/
    /**
     * Gets the current buffer capacity.
     * Usually this is slitely bigger than the current length.
     **/
    public int capacity() {
        return arrays.length(m_data);
    }/*}}}*/
    // public int available();/*{{{*/
    /**
     * Gets the number of available bytes in the internal buffer.
     * The available bytes are the bytes between the read position and the
     * number of valid bytes in the buffer.
     **/
    public int available() {
        return (m_write - m_read);
    }/*}}}*/
    // public int getReadPos();/*{{{*/
    /**
     * Gets the current read position.
     * The returned value represents the distance between the start of the
     * buffer and the next byte that can be read.
     **/
    public int getReadPos() {
        return m_read;
    }/*}}}*/
    // public int getWritePos();/*{{{*/
    /**
     * Get the current write position.
     * The write position is the distance between the first byte and the next
     * index that will be overwrite (or added) in a possible write operation.
     **/
    public int getWritePos() {
        return m_write;
    }/*}}}*/
    // public int readStatus();/*{{{*/
    /**
     * Gets the status of the last read operation.
     * Note that this status is updated at every read operation.
     * \returns The result value is an error code.
     * \see ERROR.
     **/
    public int readStatus() {
        return m_lastRead;
    }/*}}}*/
    // public int writeStatus();/*{{{*/
    /**
     * Returns the status of the last write operation.
     * Notice that this value is updated at every write.
     * \returns The result value is an error code.
     * \see ERROR.
     **/
    public int writeStatus() {
        return m_lastWrite;
    }/*}}}*/
    //@}

    /** \name DataInput IMPLEMENTATION */ //@{
    // public boolean readBoolean();/*{{{*/
    /**
     * Reads a single byte from the internal buffer.
     * \returns \b true if the byte is different from zero. Otherwise \b
     * false.
     * \remarks The read position is increased by 1.
     **/
    public boolean readBoolean() {
        if (m_read >= m_write) {
            m_lastRead = ERROR.EOF;
            return false;
        }
        m_lastRead = 0;
        if (m_data[m_read++] == (byte)0)
            return false;
        else
            return true;
    }/*}}}*/
    // public byte    readByte();/*{{{*/
    /**
     * Reads a single byte returning its value.
     * \remarks The read position is increased by 1.
     **/
    public byte readByte() {
        if (m_read >= m_write) {
            m_lastRead = ERROR.EOF;
            return (byte)0;
        }
        m_lastRead = 0;
        return m_data[m_read++];
    }/*}}}*/
    // public char    readChar();/*{{{*/
    /**
     * Reads a character from the input buffer.
     * Notice that this character is expected to be in Java UNICODE encoding
     * where each character has 2 bytes.
     * \remarks The read position is increased by 2.
     * \warning The Java specification defines that the organization of the
     * bytes in an array is big-endian. If you need a little-endian char, use
     * the \c getChar() function.
     **/
    public char readChar() {
        char result = '\0';

        if ((m_read + 2) > m_write) {
            m_lastRead = ERROR.EOF;
            return result;
        }
        m_lastRead = 0;
        result = arrays.bigEndToChar(m_data, m_read);
        m_read += 2;
        return result;
    }/*}}}*/
    // public short   readShort();/*{{{*/
    /**
     * Reads two bytes (big-endian) and returns a short value.
     * If you need a little-endian short value use \c getShort() instead.
     * \remarks The read position is increased by 2.
     **/
    public short readShort() {
        short result = 0;

        if ((m_read + 2) > m_write) {
            m_lastRead = ERROR.EOF;
            return result;
        }
        m_lastRead = 0;
        result     = arrays.bigEndToShort(m_data, m_read);
        m_read += 2;

        return result;
    }/*}}}*/
    // public int     readInt();/*{{{*/
    /**
     * Reads four bytes (big-endian) and returns an integer value.
     * If you need a little-endian integer value use \c getInt().
     * \remarks The read position is increased by 4.
     **/
    public int readInt() {
        int result = 0;

        if ((m_read + 4) > m_write) {
            m_lastRead = ERROR.EOF;
            return result;
        }
        m_lastRead = 0;
        result     = arrays.bigEndToInt(m_data, m_read);
        m_read += 4;

        return result;
    }/*}}}*/
    // public long    readLong();/*{{{*/
    /**
     * Reads eight bytes (big-endian) and returns a long value.
     * If you need a little-endian long value use \c getLong().
     * \remarks The read position is increased by 4.
     **/
    public long readLong() {
        long result = 0;

        if ((m_read + 8) > m_write) {
            m_lastRead = ERROR.EOF;
            return result;
        }
        m_lastRead = 0;
        result     = arrays.bigEndToLong(m_data, m_read);
        m_read += 8;

        return result;
    }/*}}}*/
    // public int     readUnsignedByte();/*{{{*/
    /**
     * Reads a byte from the internal buffer.
     * \returns An integer value formed by the unsigned byte value. The range
     * will be 0 to 255.
     * \remarks The read position is increased by 4.
     **/
    public int readUnsignedByte() {
        if ((m_read + 1) > m_write) {
            m_lastRead = ERROR.EOF;
            return 0;
        }
        m_lastRead = 0;
        return numbers.toInt(m_data[m_read++]);
    }/*}}}*/
    // public int     readUnsignedShort();/*{{{*/
    /**
     * Reads two bytes from the internal buffer and returns a short value.
     * The result will cast to an integer so the result is named to be
     * unsigned, in the range of 0 to 65535.
     * \remarks This conversion of byte to short is big-endian. If you need a
     * little-endian support use \c getUShort() instead.
     * \note The read position is increased by 2.
     **/
    public int readUnsignedShort() {
        short result = readShort();
        if (m_lastRead != 0) return 0;
        return numbers.toInt(result);
    }/*}}}*/
    // public float   readFloat();/*{{{*/
    /**
     * Reads four bytes from the internal buffer and returns a float value.
     * This convertion is made in big-endian notation. If you need
     * little-endian support use \c getFloat() instead.
     * \note The read position is increased by 4.
     **/
    public float readFloat() {
        int result = readInt();
        if (m_lastRead != 0) return (float).0;
        return Float.intBitsToFloat(result);
    }/*}}}*/
    // public double  readDouble();/*{{{*/
    /**
     * Reads eight bytes from the internal buffer and returns a double value.
     * \remarks This conversion is made in big-endian notation. If you need
     * little-endian support use \b getDouble() instead.
     * \note The read position is increased by 8.
     **/
    public double readDouble() {
        long result = readLong();
        if (m_lastRead != 0) return 0;
        return Double.longBitsToDouble(result);
    }/*}}}*/
    // public void    readFully(byte[] buffer);/*{{{*/
    /**
     * Read raw bytes from the internal buffer.
     * \param buffer Array where to copy the data. The number of bytes
     * actually read depends on the length of this buffer and the number of
     * bytes available. When the length of this *buffer* is greater than the
     * number of bytes available the function will copy the available data,
     * fill the excedent space with zeroes and the #readStatus() function
     * will return `ERROR::LENGTH`. By this means you will not know the real
     * count of bytes read. We reccomend you to use the #read(byte[],int,int)
     * function instead that is capable of return more rich information.
     * \note The read position will be updated according to the final count of
     * reading bytes.
     **/
    public void readFully(byte[] buffer) {
        read(buffer, 0, arrays.length(buffer));
    }/*}}}*/
    // public void    readFully(byte[] buffer, int start, int count);/*{{{*/
    /**
     * Reads \a count bytes from the internal buffer and copies the in the \a
     * buffer starting from the \a start index.
     * \param buffer The buffer where the data should be copied.
     * \param start Index of the first element of \a buffer where the copy
     * should start.
     * \param count The total number of bytes to read. If this number is
     * greater than the count of available bytes, the function will read only
     * the available bytes, padding the \a buffer array with zeroes. The
     * function \c readStatus() will return \c ERROR#LENGTH in this case.
     * \remarks As you'll never know if the function returned the amount of
     * that requested we discourage the use of this function. You should use
     * the \c read() set instead.
     * \note The read position is updated by the number of actualy read bytes.
     **/
    public void readFully(byte[] buffer, int start, int count) {
        read(buffer, start, count);
    }/*}}}*/
    // public String  readLine();/*{{{*/
    /**
     * Process the internal buffer and returns a line of text.
     * The reading starts from the current read position and goes until a new
     * line sequence is found (\\n or \\r\\n) or the end of data is reached.
     * \remarks This function will process each byte as an LATIN1 character
     * code.
     * \note The read position will be updated according to the number of
     * read bytes.
     **/
    public String readLine() {
        int index = m_read;
        int limit = arrays.length(m_data);
        String result = null;

        while (index < limit) {
            if ((m_data[index] == (byte)'\r') || (m_data[index] == (byte)'\n')) {
                result = strings.decode(m_data, m_read, (index - m_read), ENC.LATIN1);
                index++;
                if ((index < limit) && (m_data[index] == (byte)'\n'))
                    index++;

                break;
            }
            index++;
        }
        m_read = index;
        return result;
    }/*}}}*/
    // public String  readUTF();/*{{{*/
    /**
     * Interprets the internal data as a modified UTF-8 sequence.
     * Read the documentation of \c DataInput interface from Java to see the
     * explanation of modified UTF-8 encoding.
     * \return The string resulting of the conversion or \b null, if the
     * encoded bytes are wrong.
     * \note When succeeds the read position will be updated to the end of the
     * UTF-8 sequence read.
     * \see http://docs.oracle.com/javase/1.5.0/docs/api/java/io/DataInput.html#modified-utf-8
     **/
    public String readUTF() {
        int uLength = 0x0000FFFF & arrays.bigEndToShort(m_data, m_read);
        String result = arrays.utf8String(m_data, m_read);

        if (result == null) return null;
        m_read += uLength;      /* Updating the read position. */
        return result;
    }/*}}}*/
    // public int     skipBytes(int n);/*{{{*/
    /**
     * Jump the number of bytes.
     * \param n The number of bytes to skip.
     * \remarks The function skip these bytes updating the read position.
     * \return The actual number of bytes jumped. This can be less than the
     * number of bytes requested.
     **/
    public int skipBytes(int n) {
        if ((m_read + n) > m_write)
            n = m_write - m_read;

        m_read += n;
        return n;
    }/*}}}*/
    //@}

    /** \name DataOutput IMPLEMENTATION */ //@{
    // public void write(byte[] data, int start, int count);/*{{{*/
    /**
     * Writes \a data to the internal buffer.
     * Memory is allocated if needed.
     * \param data The array to be written.
     * \param start Index of the first byte in \a data to be written.
     * \param count Total number of bytes to write.
     * \note The write position will be updated according to \a count.
     * \remarks If the write fails the \c writeStatus() will return an error
     * code.
     **/
    public void write(byte[] data, int start, int count) {
        if (!_internal_checkRoom(m_write + count)) {
            m_lastWrite = ERROR.NOMEM;
            return;
        }
        arrays.copy(m_data, m_write, data, start, count);
        m_write += count;
    }/*}}}*/
    // public void write(byte[] data);/*{{{*/
    /**
     * Writes the \a data array into the internal buffer.
     * \param data Array to be written.
     * \note The write position is updated according to the length of the \a
     * data. Memory is allocated if needed.
     * \remarks If the operation fails the \c writeStatus() will return an
     * error code.
     **/
    public void write(byte[] data) {
        write(data, 0, arrays.length(data));
    }/*}}}*/
    // public void write(int b);/*{{{*/
    /**
     * Writes a single byte to the internal buffer.
     * Only the low-order byte of the integer is written.
     * \param b Value to be written.
     * \note The write position is updated by 1.
     * \see writeStatus()
     **/
    public void write(int b) {
        if (!_internal_checkRoom(m_write + 1)) {
            m_lastWrite = ERROR.NOMEM;
            return;
        }
        m_data[m_write] = (byte)(b & 0x000000FF);
        m_write++;
    }/*}}}*/
    // public void writeBoolean(boolean value);/*{{{*/
    /**
     * Writes a boolean value to the internal buffer.
     * This function writes a single byte. When \a value is \b true a byte of
     * value 1 is written. When \a value is \b false, a byte with value 0 is
     * written.
     * \param value Boolean value to be written.
     * \note The write position is updated by 1.
     * \see writeStatus()
     **/
    public void writeBoolean(boolean value) {
        write((value ? 1 : 0));
    }/*}}}*/
    // public void writeByte(int b);/*{{{*/
    /**
     * Writes a single byte to the internal buffer.
     * Only the low-order byte of the integer is written.
     * \param b Value to be written.
     * \note The write position is updated by 1.
     * \see writeStatus()
     **/
    public void writeByte(int b) {
        write(b);
    }/*}}}*/
    // public void writeChar(int c);/*{{{*/
    /**
     * Writes a \b char value into the internal buffer.
     * The character write by this function can be read by the \c readChar()
     * function.
     * \param c The character to be written.
     * \remarks The Java \c char has two bytes written in big-endian order. If
     * you need little-endian support, use the \c addChar() function.
     * \note The write position is updated by 2.
     * \see writeStatus()
     **/
    public void writeChar(int c) {
        write(arrays.charAsBigEnd((char)c));
    }/*}}}*/
    // public void writeShort(int s);/*{{{*/
    /**
     * Writes a \b short value to the internal buffer.
     * Two bytes are written in big-endian order. If you need little-endian
     * support use the \c addShort() function.
     * \param s The \b short value to be written.
     * \note The write position will be updated by 2.
     * \see writeStatus()
     **/
    public void writeShort(int s) {
        write( arrays.shortAsBigEnd((short)s) );
    }/*}}}*/
    // public void writeInt(int v);/*{{{*/
    /**
     * Writes an \b integer value to the internal buffer.
     * Four bytes in big-endian order are written. If you need little-endian
     * support use the \c addInt() function.
     * \param v The integer value to be written.
     * \note The write position is updated by 4.
     * \see writeStatus()
     **/
    public void writeInt(int v) {
        write( arrays.intAsBigEnd(v) );
    }/*}}}*/
    // public void writeLong(long l);/*{{{*/
    /**
     * Writes a \b long value to the internal buffer.
     * Eight bytes in big-endian order are written. If you need little-endian
     * support, use the \c addLong() function.
     * \param l The long value to be written.
     * \note The write position will be updated by 8.
     * \see writeStatus()
     **/
    public void writeLong(long l) {
        write( arrays.longAsBigEnd(l) );
    }/*}}}*/
    // public void writeFloat(float f);/*{{{*/
    /**
     * Writes a \b float value into the internal buffer.
     * Four bytes in big-endian order are written. If you need little-endian
     * support, use the \c addFloat() function.
     * \param f The float value to be written.
     * \note The write position will be updated by 4.
     * \see writeStatus()
     **/
    public void writeFloat(float f) {
        int bits = Float.floatToIntBits(f);
        write( arrays.intAsBigEnd(bits) );
    }/*}}}*/
    // public void writeDouble(double d);/*{{{*/
    /**
     * Writes a \b double value to the internal buffer.
     * Eight bytes are written in big-endian order. If you need little-endian
     * support use the \c addDouble() function.
     * \param d The double value to be written.
     * \note The write position will be updated by 8.
     * \see writeStatus()
     **/
    public void writeDouble(double d) {
        long bits = Double.doubleToLongBits(d);
        write( arrays.longAsBigEnd(bits) );
    }/*}}}*/
    // public void writeBytes(String s);/*{{{*/
    /**
     * Write bytes to the internal buffer.
     * The characters of the string \a s are converted to bytes and written,
     * in order, into the internal buffer. No special encoding is used.
     * \param s The string to be written.
     * \note The write position will be updated by the length of the string,
     * plus 1.
     * \note If the string \a s is \b null the \c writeStatus() function will
     * return \c ERROR#POINTER.
     * \remarks As an addition to the standard \b DataOutput contract, the
     * bytes written will be appended with a single zero (null) byte. This
     * string can be read using \c getBytes().
     **/
    public void writeBytes(String s) {
        if (s == null) { m_lastWrite = ERROR.POINTER; return; }
        if (!_internal_checkRoom(m_write + s.length() + 1)) {
            m_lastWrite = ERROR.NOMEM;
            return;
        }
        char[] chars = strings.getChars(s);
        byte[] bytes = new byte[arrays.length(chars) + 1];
        int    limit = arrays.length(chars);

        for (int i = 0; i < limit; i++) {
            bytes[i] = (byte)chars[i];
        }
        bytes[limit] = 0x00;            /* The terminator. */
        write( bytes );
    }/*}}}*/
    // public void writeChars(String s);/*{{{*/
    /**
     * Write bytes to the internal buffer.
     * Each character in the source string \a s is written using two bytes in
     * big-endian order. As an addition to the \c DataOutput standard
     * contract, the sequence is ended with a \b NULL character (two bytes
     * zero).
     * \param s The string to be written.
     * \remarks The bytes written with this function can be further read using
     * \c getChars() function.
     * \note The write position will be updated by the length of the string
     * passed multiplied by 2, plus 2 bytes of the added \b NULL character.
     * \see writeStatus()
     **/
    public void writeChars(String s) {
        if (s == null) { m_lastWrite = ERROR.POINTER; return; }
        if (!_internal_checkRoom(m_write + (s.length() + 1) * 2)) {
            m_lastWrite = ERROR.NOMEM;
            return;
        }
        char[] chars = strings.getChars(s);
        int    limit = arrays.length(chars);
        byte[] bytes = new byte[limit * 2 + 2];

        for (int i = 0, x = 0; i < limit; i++, x+=2) {
            bytes[x+0] = (byte)(0xFF & (chars[i] >> 8));
            bytes[x+1] = (byte)(0xFF & chars[i]);
        }

        /* The terminator character. */
        bytes[limit*2+0] = 0x00;
        bytes[limit*2+1] = 0x00;

        write( bytes );
    }/*}}}*/
    // public void writeUTF(String s);/*{{{*/
    /**
     * Writes a string using a modified UTF-8 encoding.
     * This encoding is specified in the Java documentation. Each character
     * can be represented by 1, 2 or 3 bytes. The first 2 bytes of the
     * sequence will hold the length of the converted byte array.
     * \param s The string to be converted and written.
     * \note The write position will point to the area after the added
     * sequence.
     * \note If the convertion fails, the function \c writeStatus() will
     * return an error code.
     * \remarks If the string \a s is null or zero length only the first two
     * bytes, with the string length, will be written.
     * \see http://docs.oracle.com/javase/1.5.0/docs/api/java/io/DataInput.html#modified-utf-8
     **/
    public void writeUTF(String s) {
        int limit = strings.length(s);
        if (limit == 0) {
            writeShort(0);
            return;
        }

        /* The conversion will be done in a temporary buffer. */
        byte[] buffer = new byte[limit * 3 + 2];
        char[] chars  = strings.getChars(s);
        int    index  = 2;

        for (int i = 0; i < limit; i++) {
            if ((chars[i] >= 0x0001) && (chars[i] <= 0x007F)) {
                buffer[index] = (byte)chars[i];
                index++;
            }
            else if ((chars[i] == 0x0000) || ((chars[i] >= 0x0080) && (chars[i] <= 0x07FF))) {
                buffer[index+0] = (byte)(0xC0 | (0x1F & (chars[i] >> 6)));
                buffer[index+1] = (byte)(0x80 | (0x3F & chars[i]));
                index += 2;
            }
            else if (chars[i] >= 0x0800) {
                buffer[index+0] = (byte)(0xE0 | (0x0F & (chars[i] >> 12)));
                buffer[index+1] = (byte)(0x80 | (0x3F & (chars[i] >>  6)));
                buffer[index+2] = (byte)(0x80 | (0x3F & chars[i]));
                index += 3;
            }
        }
        /* The index variable has the length of encoded bytes. Write it to the
         * output buffer.
         */
        arrays.shortAsBigEnd(buffer, 0, (short)(index - 2));
        write(buffer, 0, index);
    }/*}}}*/
    //@}

    /** \name READ OPERATIONS */ //@{
    // public char   getChar();/*{{{*/
    /**
     * Reads a \b char from the internal buffer.
     * This function is the same as \c readChar(), with the difference that it
     * expects that the character bits, in the internal buffer, are organized
     * in little-endian order.
     * \note The read position is increased by 2.
     **/
    public char getChar() {
        if ((m_read + 2) > m_write) {
            m_lastRead = ERROR.EOF;
            return '\0';
        }
        char c = arrays.littleEndToChar(m_data, m_read);
        m_read += 2;
        m_lastRead = 0;
        return c;
    }/*}}}*/
    // public short  getShort();/*{{{*/
    /**
     * Read two bytes from the internal buffer and returns a \b short value.
     * This function works the same way as \c readShort(), except that this
     * function expects that the value is organized in little-endian order.
     * \note The read position is increased by 2.
     **/
    public short getShort() {
        if ((m_read + 2) > m_write) {
            m_lastRead = ERROR.EOF;
            return 0;
        }
        short s = arrays.littleEndToShort(m_data, m_read);
        m_read += 2;
        m_lastRead = 0;
        return s;
    }/*}}}*/
    // public int    getInt();/*{{{*/
    /**
     * Reads four bytes and returns an integer value.
     * This function works the same way as \c readInt(), except that its
     * expects the value to be organized in little-endian order.
     * \remarks The read position is increased by 4.
     **/
    public int getInt() {
        if ((m_read + 4) > m_write) {
            m_lastRead = ERROR.EOF;
            return 0;
        }
        int i = arrays.littleEndToInt(m_data, m_read);
        m_read += 4;
        m_lastRead = 0;
        return i;
    }/*}}}*/
    // public long   getLong();/*{{{*/
    /**
     * Reads eight bytes and returns a long value.
     * This function works the same way as \c readLong(), except that its
     * expects the value to be organized in little-endian order.
     * \note The read position is increased by 8.
     **/
    public long getLong() {
        if ((m_read + 8) > m_write) {
            m_lastRead = ERROR.EOF;
            return 0;
        }
        long l = arrays.littleEndToLong(m_data, m_read);
        m_read += 8;
        m_lastRead = 0;
        return l;
    }/*}}}*/
    // public int    getUShort();/*{{{*/
    /**
     * Reads two bytes and returns an unsigned short value casted to an int.
     * This function works in the same way as \c readUnsignedShort(), with the
     * exception that it expects the value to be organized in little-endian
     * byte order.
     * \note The read position is increased by 2.
     **/
    public int getUShort() {
        return (0x0000FFFF | getShort());
    }/*}}}*/
    // public float  getFloat();/*{{{*/
    /**
     * Read four bytes and returns a \b float value.
     * This function works the same way as \c readFloat() with the exception
     * that it expects the value to be organized in little-endian byte order.
     * \note The read position is increased by 4.
     **/
    public float getFloat() {
        return Float.intBitsToFloat(getInt());
    }/*}}}*/
    // public double getDouble();/*{{{*/
    /**
     * Read eight bytes and returns a \b double value.
     * This function works the same way as \c readDouble(), with the exception
     * that it expects the value to be organized in little-endian byte order.
     * \note The read position is increased by 8.
     **/
    public double getDouble() {
        return Double.longBitsToDouble(getLong());
    }/*}}}*/
    // public String getBytes();/*{{{*/
    /**
     * Reads a sequence of bytes and returns a String.
     * This function reads a string, from the internal buffer, written by the
     * \c writeBytes(String) function. Each byte represents a single character
     * with can be in US-ASCII or LATIN1 encoding. No other encoding are
     * supported. The read operation starts in the current read position and
     * goes until a NULL terminator is found or the end of the buffer is
     * reached.
     * \return The string decoded by this function or \b null, if no string
     * could be found.
     * \note The read position will be increased by the number of read bytes.
     * \see readStatus()
     **/
    public String getBytes() {
        int limit = this.length();
        int index = m_read, i = 0;
        char[] chars = new char[limit];

        while (index < limit) {
            if (m_data[index] == 0x00) break;
            chars[i++] = (char)m_data[index++];
        }
        m_read = index;
        m_lastRead = 0;

        try { return new String(chars, 0, i); }
        catch (Exception ex) {
            m_lastRead = ERROR.NOTFOUND;
            return null;
        }
    }/*}}}*/
    // public String getChars();/*{{{*/
    /**
     * Reads a sequence of characters and returns a String.
     * This function reads a string, from the internal buffer, written by the
     * \c writeChars(String) function. Each character is composed by two bytes
     * in big-endian order. The string is encoded using the standard 16bits
     * Java encoding.
     *
     * The read operation starts in the current read position and
     * goes until a NULL terminator is found or the end of the buffer is
     * reached.
     * \return The string decoded by this function or \b null, if no string
     * could be found.
     * \note The read position will be increased by the number of read bytes.
     * \see readStatus()
     **/
    public String getChars() {
        int limit = this.length();
        int index = m_read, i = 0;
        char[] chars = new char[limit/2];

        while (index < limit - 1) {
            if ((m_data[index] == 0x00) && (m_data[index+1] == 0x00))
                break;

            chars[i++] = (char)((0xFF & (m_data[index] << 8)) | m_data[index+1]);
            index+=2;
        }
        m_read = index;
        m_lastRead = 0;

        try { return new String(chars, 0, i); }
        catch (Exception ex) {
            m_lastRead = ERROR.NOTFOUND;
            return null;
        }
    }/*}}}*/
    // public int    read(byte[] buffer, int start, int count);/*{{{*/
    /**
     * Reads bytes from the internal buffer.
     * \param buffer Where the bytes are copied. Must have enough room to copy
     * the requested bytes.
     * \param start Index of the first element in \a buffer where the copy
     * starts.
     * \param count The total amount of bytes to read. If this value is less
     * than zero or greater than the available bytes, the read will stop at
     * the end of the internal buffer.
     * \return The function returns the total of bytes actualy read on
     * success. On failure the result is an error code, equals the value
     * returned by #readStatus().
     * \sa readFully(byte[],int,int)
     **/
    public int read(byte[] buffer, int start, int count) {
        if ((count < 0) || (count > available()))
            count = available();

        if (count > (arrays.length(buffer) - start)) {
            m_lastRead = ERROR.SPACE;
            return ERROR.SPACE;
        }

        arrays.copy(buffer, start, m_data, m_read, count);
        m_read += count;
        m_lastRead = 0;

        return count;
    }/*}}}*/
    // public String read(String encoding, int count);/*{{{*/
    /**
     * Reads a string from the internal buffer.
     * The encoding especification of the string must be set to one of the
     * encodings defined in the \c ENC class.
     * \param encoding The encoding of the string in the buffer.
     * \param count Number of bytes to read. This parameter is a hint for the
     * function if you know the length of encoded bytes. If this value is
     * passed, the total count of bytes is read and decoded. If this
     * value is less than zero, the function will read the buffer until a
     * terminator NULL character is found. Then the decoding is done.
     * \return A string built with the decoded bytes or \b EMPTY, if an error
     * occurs.
     * \note The read position will be updated by the number of \a count bytes
     * passed. The \c readStatus() always returns 0. This function never
     * fails.
     **/
    public String read(String encoding, int count)
    {
        if (count > available())
            count = available();
        else if (count < 0)
        {
            int index = m_read;
            int limit = arrays.length(m_data);

            if (encoding.equals(ENC.ASCII) || encoding.equals(ENC.LATIN1) || encoding.equals(ENC.UTF8)) {
                while (index < limit) {
                    if (m_data[index] == 0x00)
                        break;
                    index++;
                }
            }
            else if (encoding.equals(ENC.UTF16LE) || encoding.equals(ENC.UTF16BE) || encoding.equals(ENC.UTF16)) {
                while (index < limit) {
                    if (arrays.bigEndToShort(m_data, index) == (short)0)
                        break;
                    index+=2;
                }
            }
            else if (encoding.equals(ENC.UTF32LE) || encoding.equals(ENC.UTF32BE)) {
                while (index < limit) {
                    if (arrays.bigEndToInt(m_data, index) == 0)
                        break;
                    index+=4;
                }
            }
            count = (index - m_read);
        }

        String result = null;

        if (count == 0)
            result = strings.EMPTY;
        else
            result = strings.decode(m_data, m_read, count, encoding);

        m_read += count;
        m_lastRead = 0;
        return result;
    }/*}}}*/
    // public int    read(DataOutput output, int count);/*{{{*/
    /**
     * Reads the contents of this stream into a \c DataOutput object.
     * \param output The \c DataOutput instance where the data must be read.
     * \param count The total number of bytes to read. The read operation
     * starts at the current read position. If less than zero or greater than
     * the total amount of available bytes only the valid bytes will be read.
     * \return The function returns the total bytes read unless an error
     * occurs. In this case, an error code will be returned.
     * \note The read position will be increased by \a count bytes.
     **/
    public int read(DataOutput output, int count) {
        if ((count < 0) || (count > available()))
            count = available();

        try { output.write(m_data, m_read, count); }
        catch (Exception ex) {
            m_lastRead = ERROR.IO;
            return ERROR.IO;
        }
        m_lastRead = 0;
        m_read += count;
        return count;
    }/*}}}*/
    //@}

    /** \name WRITE OPERATIONS */ //@{
    // public int addChar(int c);/*{{{*/
    /**
     * Writes a character value into the buffer.
     * The character written by this function can be read by \c getChar()
     * function.
     * \param c The character to write.
     * \return On success the function returns 0. On failure an error code
     * will be returned.
     * \remarks This function will organize the two bytes of the \a c
     * character in little-endian byte order.
     * \note The write position will be increased by 2.
     * \see writeStatus()
     **/
    public int addChar(int c) {
        write(arrays.charAsLittleEnd((char)c));
        return m_lastWrite;
    }/*}}}*/
    // public int addShort(int s);/*{{{*/
    /**
     * Writes a short value into the buffer.
     * The value written by this function can be read by \c getShort()
     * function.
     * \param s The short value to write. Two bytes will be written.
     * \return On success the function returns 0. On failure an error code
     * will be returned.
     * \remarks This function organize the bytes in little-endian order.
     * \note The write position will be increased by 2.
     * \see writeStatus()
     **/
    public int addShort(int s) {
        write(arrays.shortAsLittleEnd((short)s));
        return m_lastWrite;
    }/*}}}*/
    // public int addInt(int i);/*{{{*/
    /**
     * Write an integer value into the buffer.
     * The value written by this function can be read by \c getInt()
     * function.
     * \param i The integer value to write. Four bytes will be written.
     * \return On success the function returns 0. On failure an error code
     * will be returned.
     * \remarks This function organize the bytes in little-endian order.
     * \note The write position will be increased by 4.
     * \see writeStatus()
     **/
    public int addInt(int i) {
        write(arrays.intAsLittleEnd(i));
        return m_lastWrite;
    }/*}}}*/
    // public int addLong(long l);/*{{{*/
    /**
     * Write a long value into the buffer.
     * The value written by this function can be read by \c getLong()
     * function.
     * \param l The long value to write. Eight bytes will be written.
     * \return On success the function returns 0. On failure an error code
     * will be returned.
     * \remarks This function organize the bytes in little-endian order.
     * \note The write position will be increased by 8.
     * \see writeStatus()
     **/
    public int addLong(long l) {
        write(arrays.longAsLittleEnd(l));
        return m_lastWrite;
    }/*}}}*/
    // public int addFloat(float f);/*{{{*/
    /**
     * Writes a float value into the buffer.
     * The value written by this function can be read by \c getFloat()
     * function.
     * \param f The float value to write. Four bytes will be written.
     * \return On success the function returns 0. On failure an error code
     * will be returned.
     * \remarks This function organize the bytes in little-endian order.
     * \note The write position will be increased by 4.
     * \see writeStatus()
     **/
    public int addFloat(float f) {
        write(arrays.intAsLittleEnd(Float.floatToIntBits(f)));
        return m_lastWrite;
    }/*}}}*/
    // public int addDouble(double d);/*{{{*/
    /**
     * Writes a double value into the buffer.
     * The value written by this function can be read by \c getDouble()
     * function.
     * \param d The double value to write. Eight bytes will be written.
     * \return On success the function returns 0. On failure an error code
     * will be returned.
     * \remarks This function organize the bytes in little-endian order.
     * \note The write position will be increased by 8.
     * \see writeStatus()
     **/
    public int addDouble(double d) {
        write(arrays.longAsLittleEnd(Double.doubleToLongBits(d)));
        return m_lastWrite;
    }/*}}}*/
    // public int write(DataInput input, int count);/*{{{*/
    /**
     * Reads from a \c DataInput and writes the internal buffer.
     * \param input The \c DataInput to read.
     * \param count Total number of bytes to read from \a input.
     * \return The function returns \a count. If an error occurred, like an
     * out-of-memory exception, the function returns an error code.
     * \note The write position will be increased by \a count.
     * \see writeStatus()
     **/
    public int write(DataInput input, int count) {
        if (!_internal_checkRoom(m_write + count)) {
            m_lastWrite = ERROR.NOMEM;
            return ERROR.NOMEM;
        }

        try { input.readFully(m_data, m_write, count); }
        catch (Exception ex) {
            m_lastWrite = ERROR.IO;
            return m_lastWrite;
        }
        m_write += count;
        m_lastWrite = 0;
        return count;
    }/*}}}*/
    // public int write(String s, String enc, int maxLen);/*{{{*/
    /**
     * Writes a string into the buffer.
     * The string will be encoded according to the specified encoding name.
     * \param s The string to be encoded and written.
     * \param enc The encoding to be used.
     * \param maxLen If less than zero the function will write the entire
     * string to the buffer adding a null terminator at the end. If greater
     * than zero the function will truncate the string if necessary. But, only
     * this amount of chars will be write. If this count is greater than the
     * length of the string, the buffer will be padded with zeroes.
     * \return The actual number of bytes written. If the function fails the
     * result will be an error code.
     * \note The write position will be increased by the amount actualy
     * written.
     * \see writeStatus()
     **/
    public int write(String s, String enc, int maxLen) {
        if (maxLen == 0) { m_lastWrite = ERROR.PARM; return ERROR.PARM; }
        if (maxLen > 0) {
            s = strings.substr(s, 0, maxLen);
        }

        byte[] bytes = null;

        try { bytes = s.getBytes(enc); }
        catch (Exception ex) {
            m_lastWrite = ERROR.CHARSET;
            return m_lastWrite;
        }

        write( bytes );     /* Writes the string encoded. */
        /* By the encoding used, write the null terminator. */
        if (enc.equals(ENC.ASCII) || enc.equals(ENC.LATIN1) || enc.equals(ENC.UTF8)) {
            writeByte( 0x00 );
            maxLen = arrays.length(bytes) + 1;
        }
        else if (enc.equals(ENC.UTF16) || enc.equals(ENC.UTF16LE) || enc.equals(ENC.UTF16BE)) {
            writeShort( 0x00 );
            maxLen = arrays.length(bytes) + 2;
        }
        else {
            writeInt( 0x00 );
            maxLen = arrays.length(bytes) + 4;
        }

        return maxLen;
    }/*}}}*/
    // public int write(String s, String enc);/*{{{*/
    /**
     * Writes a string into the buffer.
     * The string will be written in the current write position, updating the
     * reading position to the end of the write. No terminator character will
     * be appended in this implementation.
     * \param s The string to be written.
     * \param enc The encoding to be used when converting the string to byte
     * array.
     * \return On success the function returns the amount of bytes written to
     * this stream. On failure the result will be an error code. The error
     * code will also be available through the function \ref writeStatus().
     **/
    public int write(String s, String enc) {
        if ((s == null) || (s.length() == 0) || (enc == null) || (enc.length() == 0))
        {
            m_lastWrite = ERROR.PARM;
            return m_lastWrite;
        }

        byte[] bytes = null;

        try { bytes = s.getBytes(enc); }
        catch (Exception ex) {
            m_lastWrite = ERROR.CHARSET;
            return m_lastWrite;
        }

        /* Writes in the stream, updates the current write and read position
         * and returns the number of bytes written.
         */
        write( bytes );
        return bytes.length;
    }/*}}}*/
    //@}

    /** \name BUFFER CONTROL OPERATIONS */ //@{
    // public boolean setWritePos(int pos);/*{{{*/
    /**
     * Changes the write position.
     * When the write position is changed the read position is automatically
     * reseted.
     * \param pos The new write position. This is the absolute offset from the
     * start of the internal buffer. The first byte has offset zero.
     * \return \b true on sucess. \b false if the \a pos value is invalid.
     **/
    public boolean setWritePos(int pos) {
        if ((pos < 0) || (pos > this.capacity()))
            return false;

        m_read = 0;
        m_write = pos;
        return true;
    }/*}}}*/
    // public boolean setReadPos(int pos);/*{{{*/
    /**
     * Changes the read position.
     * \param pos The new read position. This is the absolute offset between
     * the start of the buffer and the current write position.
     * \return \b true on success. \b false means that the \a pos value is
     * invalid.
     **/
    public boolean setReadPos(int pos) {
        if ((pos < 0) || (pos > m_write))
            return false;

        m_read = pos;
        return true;
    }/*}}}*/
    // public int     purge();/*{{{*/
    /**
     * Remove all read data.
     * The read position will be automaticaly reseted. The write position will
     * be updated as needed.
     * \returns The number of actual bytes removed.
     **/
    public int purge() {
        arrays.move(m_data, 0, m_data, m_read, -1);
        m_write = (m_write - m_read);
        int result = m_read;
        m_read = 0;
        return result;
    }/*}}}*/
    // public void    reset();/*{{{*/
    /**
     * Reset both read and write positions.
     * After this function return any write operation will start from the
     * first byte. Any read operation will not be available until a write
     * operation put some bytes to read.
     * \note No memory will be deallocated.
     * \see free()
     **/
    public void reset() {
        m_write = 0;
        m_read  = 0;
    }/*}}}*/
    // public void    free();/*{{{*/
    /**
     * Frees the memory allocated by this object.
     * Read and write positions will be reseted.
     **/
    public void free() {
        m_data  = null;
        m_read  = 0;
        m_write = 0;
    }/*}}}*/
    //@}

    /** \name STREAM OPERATIONS */ //@{
    // public int writeFrom(CBinaryReader reader, int count);/*{{{*/
    /**
     * Writes data from a CBinaryReader object into the internal buffer.
     * The write position will be updated after this operation.
     * \param reader CBinaryReader object.
     * \param count Number of bytes to read from \a reader.
     * \return The function returns \a count. If \a count is greater than the
     * number of bytes available in \a reader the function will return just
     * the number of bytes actualy written to the internal buffer. If an error
     * occurs the return can be ERROR#IO or ERROR#NOMEM.
     **/
    public int writeFrom(CBinaryReader reader, int count) {
        if (!_internal_checkRoom(count)) {
            m_lastWrite = ERROR.NOMEM;
            return m_lastWrite;
        }
        m_lastWrite = reader.read(m_data, m_write, count);
        if (m_lastWrite < 0) return m_lastWrite;

        count = m_lastWrite;
        m_write += count;
        m_lastWrite = 0;
        return count;
    }/*}}}*/
    // public int writeFrom(CStreamReader reader, int count);/*{{{*/
    /**
     * Writes data from a CStreamReader object into the internal buffer.
     * The write position will be updated after this operation.
     * \param reader CStreamReader object.
     * \param count Number of bytes to read from \a reader.
     * \return The function returns \a count. If \a count is greater than the
     * number of bytes available in \a reader the function will return just
     * the number of bytes actualy written into the internal buffer. If an
     * error occurs the return can be ERROR#IO or ERROR#NOMEM.
     **/
    public int writeFrom(CStreamReader reader, int count) {
        if (!_internal_checkRoom(m_write + count)) {
            m_lastWrite = ERROR.NOMEM;
            return m_lastWrite;
        }
        m_lastWrite = reader.read(m_data, m_write, count);
        if (m_lastWrite < 0) return m_lastWrite;

        count = m_lastWrite;
        m_write += count;
        m_lastWrite = 0;
        return count;
    }/*}}}*/
    // public int readInto(CBinaryWriter writer, int count);/*{{{*/
    /**
     * Reads data from the internal buffer into the CBinaryWriter object.
     * The read position will be updated after this operation.
     * \param writer CBinaryWriter to write the data into.
     * \param count Number of bytes to read from the internal buffer. If there
     * is not such amount of bytes available the function will write only the
     * available bytes. If \a count is less than zero, all available bytes
     * will be read.
     * \return The function returns the number of bytes read from the input
     * buffer and written into the \a writer object. Zero is a valid value if
     * no data was available. If an error occurs, the function returns an
     * error code.
     **/
    public int readInto(CBinaryWriter writer, int count) {
        int amount = this.available();
        if ((amount < count) || (count < 0)) count = amount;
        if (count == 0) return count;

        writer.write(m_data, m_read, count);
        m_read += count;
        m_lastRead = ERROR.SUCCESS;

        return count;
    }/*}}}*/
    // public int readInto(CStreamWriter writer, int count);/*{{{*/
    /**
     * Reads data from the internal buffer into the CStreamWriter object.
     * The read position will be updated after this operation.
     * \param writer CStreamWriter to write the data into.
     * \param count Number of bytes to read from the internal buffer. If there
     * is not such amount of bytes available the function will write only the
     * available bytes. If \a count is less than zero, all available bytes
     * will be read.
     * \return The function returns the number of bytes read from the input
     * buffer and written into the \a writer object. Zero is a valid value if
     * no data was available. If an error occurs, the function returns an
     * error code.
     **/
    public int readInto(CStreamWriter writer, int count) {
        int amount = this.available();
        if ((amount < count) || (count < 0)) count = amount;
        if (count == 0) return 0;

        m_lastRead = writer.put(m_data, m_read, count);
        if (m_lastRead < 0) return m_lastRead;

        count = m_lastRead;
        m_read += count;
        m_lastRead = 0;
        return count;
    }/*}}}*/
    //@}

    /** \name Input/Output Streams Support */ //@{
    // public int writeFromInputStream(InputStream is, int count);/*{{{*/
    /**
     * Writes the internal buffer reading from the passed stream.
     * @param is The InputStream to read.
     * @param count Number of bytes to read. When less than zero the function
     * will ask the available bytes and read then all.
     * @return The result is the total number of bytes read from \a is
     * and written in the internal buffer. If an error occurs the result will
     * be an error code. Also the error can be retrieved by #writeStatus()
     * function.
     * @sa ERROR
     **/
    public int writeFromInputStream(InputStream is, int count)
    {
        m_lastWrite = 0;

        if (count < 0)
        {
            try { count = is.available(); }
            catch (Exception ex) {
                debug.e(ex, "stream_t::writeFromInputStream() InputStream::available($n): $s\n");
                m_lastWrite = ERROR.READ;
                return m_lastWrite;
            }
        }
        if (count == 0) return 0;       /* Nothing to read. */

        if (!_internal_checkRoom(m_write + count))
        {
            m_lastWrite = ERROR.NOMEM;
            return m_lastWrite;
        }

        try { count = is.read(m_data, m_write, count); }
        catch (Exception ex) {
            debug.e(ex, "stream_t::writeFromInputStream() InputStream::read($n): $s\n");
            m_lastWrite = ERROR.READ;
            return m_lastWrite;
        }

        if (count < 0)
        {
            m_lastWrite = ERROR.EOF;
            return m_lastWrite;
        }

        m_write += count;
        return count;
    }/*}}}*/
    // public int readIntoOutputStream(OutputStream os, int count);/*{{{*/
    /**
     * Reads the internal buffer from the current position writing the
     * OutputStream passed.
     * @param os OutputStream to write.
     * @param count Number of bytes to write. When less than zero all internal
     * available bytes starting from the current position will be written in
     * the OutputStream.
     * @return The total number of bytes written means success. Zero is a
     * valid result value if there is no bytes available to write. If un error
     * occurs the result will be its error code. The erro code can also be
     * retrieved from #readStatus() function.
     * @sa ERROR
     **/
    public int readIntoOutputStream(OutputStream os, int count)
    {
        m_lastRead = 0;
        if ((count < 0) || (count > available()))
            count = available();

        try { os.write(m_data, m_read, count); }
        catch (Exception ex) {
            debug.e(ex, "stream_t::readIntoOutputStream(): os.write($n): $s\n");
            m_lastRead = ERROR.WRITE;
            return m_lastRead;
        }
        m_read += count;
        return count;
    }/*}}}*/
    //@}

    /** \name CYCLIC REDUNDANCY CHECK */ //@{
    // public final short crc16(int start, int count);/*{{{*/
    /**
     * Calculate the CRC16-CCITT of this stream content.
     * \param start Position of the first byte to start calculating the CRC.
     * \param count Total number of byte to use in the calculation. Pass -1 to
     * calculate the CRC from \a start till the end of the stream.
     * \return The CRC16 calculated value.
     * \remarks The calculus will be initialized with **0xFFFF** 16 bit value.
     * The current read position will not be changed in this function.
     **/
    public final short crc16(int start, int count) {
        int result = 0x0000FFFF;

        if (start < 0) return (short)0;
        if (count < 0) count = m_write - start;
        if (count <= 0) return (short)0;

        for (int i = 0; i < count; i++)
        {
            result  = (((result >> 8) | (result << 8)) & 0x0000FFFF);
            result ^= (m_data[i+start] & 0x000000FF);
            result ^= (((result & 0x000000FF) >> 4) & 0x0000FFFF);
            result ^= ((result << 12) & 0x0000FFFF);
            result ^= (((result & 0x000000FF) << 5) & 0x0000FFFF);
        }
        return (short)(result & 0x0000FFFF);
    }/*}}}*/
    //@}

    /** \name OVERRIDES */ //@{
    // public boolean equals(Object object);/*{{{*/
    /**
     * Checks the equality with another object.
     * \param object The object to compare to.
     * \return \b true if the objects instances are the same. Otherwise \b
     * false.
     **/
    public boolean equals(Object object) {
        if (!(object instanceof stream_t)) return false;
        stream_t stream = (stream_t)object;

        return (boolean)(stream == this);
    }/*}}}*/
    // public String  toString();/*{{{*/
    /**
     * Returns a textual representation of this object.
     * The result will be a string with binary data represented as groups of
     * bytes in hexadecimal notation. Example, supose this object has 3 bytes
     * of values: @code 0xAC, 0x33, 0x01 @endcode. The output of this function
     * will be:
     * @code
     * AC 33 01
     * @endcode
     * \note Only the valid part of the buffer will be converted. That is, the
     * parte between the current read position and the write position.
     **/
    public String toString() {
        byte[] temp = arrays.slice(m_data, m_read, this.available());
        return arrays.toString(temp, " ", 1);
    }/*}}}*/
    //@}

    /** \name INTERNAL FUNCTIONS */ //@{
    // protected boolean _internal_checkRoom(int size);/*{{{*/
    /**
     * Checks the capacity of the internal buffer.
     * If the capacity is less than the size needed, more memory is allocated.
     * \param size The required total size.
     * \returns \b true if the function succeeds. Otherwise \b false.
     **/
    protected boolean _internal_checkRoom(int size) {
        if (capacity() >= size) return true;
        m_data = arrays.realloc(m_data, size);
        return (boolean)(m_data != null);
    }/*}}}*/
    //@}

    /** \name PROTECTED FIELDS */ //@{
    protected byte[] m_data;            /**< The actual stream data.        */
    protected int    m_read;            /**< The read cursor position.      */
    protected int    m_write;           /**< The write cursor position.     */
    protected int    m_lastRead;        /**< Last read status.              */
    protected int    m_lastWrite;       /**< Last write status.             */
    //@}
}
// vim:syntax=java.doxygen
