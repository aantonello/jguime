/**
 * \file
 * Defines the CStreamReader class.
 *
 * \author Alessandro Antonello <aantonello@paralaxe.com.br>
 * \date   October 04, 2010
 *
 * \par Copyright
 * This file is provided in hope that it will be useful to someone. It is
 * offered in public domain. You may use, modify or distribute it freely.
 *
 * The code is provided "AS IS". There is no warranty at all, of any kind. You
 * may change it if you like. Or just use it as it is.
 */
package x.android.io;

import x.android.defs.ERROR;
import x.android.defs.ENC;
import x.android.utils.strings;
import x.android.utils.arrays;
import x.android.utils.debug;

import java.io.InputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.EOFException;

/**
 * \ingroup x_android_io
 * Class to read data from input streams.
 *//* --------------------------------------------------------------------- */
public class CStreamReader implements DataInput
{
    /** \name CONSTRUCTOR */ //@{
    // public CStreamReader(InputStream _is);/*{{{*/
    /**
     * Default constructor.
     * \param _is The stream to read.
     **/
    public CStreamReader(InputStream _is) {
        m_dis = new DataInputStream(_is);
    }/*}}}*/
    //@}

    /** \name ATTRIBUTES */ //@{
    // public int available();/*{{{*/
    /**
     * Returns the number of bytes ready to be read.
     * Notice that the return value is just an estimate and cannot be trusted.
     * Seed the documentation of the \c InputStream#available() method.
     * \return An integer with the number of bytes available to be read. If an
     * error occurs, the function will return \c ERROR#IO.
     **/
    public int available() {
        try { return m_dis.available(); }
        catch (Exception ex) {
            debug.w("CStreamReader::available() trowed an exception!");
            debug.e("=> %s", ex);
            return ERROR.IO;
        }
    }/*}}}*/
    //@}

    /** \name OVERRIDED */ //@{
    // public boolean readBoolean();/*{{{*/
    /**
     * Reads a boolean value from the stream.
     * A single byte is read. If it is different from zero the return will be
     * \c true. If the read byte is zero the result is \b false. No exception
     * is thrown so care must be taken. If an error occur the result will be
     * \c false.
     **/
    public boolean readBoolean() {
        try { return m_dis.readBoolean(); }
        catch (Exception ex) {
            debug.e("CStreamReader::readBoolean(): %s", ex);
        }
        return false;
    }/*}}}*/
    // public byte    readByte();/*{{{*/
    /**
     * Reads one byte from the stream.
     * \returns The byte value.
     * \remarks No exception is thrown. If an error occurs the result will be
     * zero.
     **/
    public byte    readByte() {
        try { return m_dis.readByte(); }
        catch (Exception ex) {
            debug.e("CStreamReader::readByte(): %s", ex);
        }
        return 0x00;
    }/*}}}*/
    // public char    readChar();/*{{{*/
    /**
     * Reads a character from the stream.
     * A character is packed in two bytes.
     * \return The character read.
     * \remarks No exception is thrown. If an error occurs the result will be
     * zero.
     **/
    public char    readChar() {
        try { return m_dis.readChar(); }
        catch (Exception ex) {
            debug.e("CStreamReader::readChar(): %s", ex);
        }
        return '\0';
    }/*}}}*/
    // public short   readShort();/*{{{*/
    /**
     * Reads two bytes from the input stream.
     * \return A short value made up from the two bytes read.
     * \remarks No exception is thrown. If an error occurs the result will be
     * zero.
     **/
    public short   readShort() {
        try { return m_dis.readShort(); }
        catch (Exception ex) {
            debug.e("CStreamReader::readShort(): %s", ex);
        }
        return (short)0;
    }/*}}}*/
    // public int     readInt();/*{{{*/
    /**
     * Reads four bytes from the input stream.
     * \returns An integer made up from the four bytes read.
     * \remarks No exception is thrown. If an error occurs the result will be
     * zero.
     **/
    public int     readInt() {
        try { return m_dis.readInt(); }
        catch (Exception ex) {
            debug.e("CStreamReader::readInt(): %s", ex);
        }
        return 0;
    }/*}}}*/
    // public long    readLong();/*{{{*/
    /**
     * Reads eight bytes from the input stream.
     * \returns A long value made up from the eight bytes read.
     * \remarks No exception is thrown. If an error occurs the result will be
     * zero.
     **/
    public long    readLong() {
        try { return m_dis.readLong(); }
        catch (Exception ex) {
            debug.e("CStreamReader::readLong(): %s", ex);
        }
        return 0L;
    }/*}}}*/
    // public float   readFloat();/*{{{*/
    /**
     * Reads four bytes from the input stream.
     * \returns A float value made up from the four bytes read.
     * \remarks No exception is thrown. If an error occurs the result will be
     * zero.
     **/
    public float   readFloat() {
        try { return m_dis.readFloat(); }
        catch (Exception ex) {
            debug.e("CStreamReader::readFloat(): %s", ex);
        }
        return (float)0.0;
    }/*}}}*/
    // public double  readDouble();/*{{{*/
    /**
     * Reads eight bytes from the input stream.
     * \returns A double value made up from the eight bytes read.
     * \remarks No exception is thrown. If an error occurs the result will be
     * zero.
     **/
    public double  readDouble() {
        try { return m_dis.readDouble(); }
        catch (Exception ex) {
            debug.e("CStreamReader::readDouble(): %s", ex);
        }
        return 0.0;
    }/*}}}*/
    // public String  readUTF();/*{{{*/
    /**
     * Reads a string encoded in modified UTF-8.
     * \returns the string read.
     * \remarks No exception is thrown. If an error occurs the result will be
     * null.
     **/
    public String  readUTF() {
        try { return m_dis.readUTF(); }
        catch (Exception ex) {
            debug.e("CStreamReader::readUTF(): %s", ex);
        }
        return null;
    }/*}}}*/
    // public int     readUnsignedByte();/*{{{*/
    /**
     * Reads a single byte from the input stream.
     * \returns An integer made up from the byte read. The result is always
     * unsigned.
     * \remarks No exception is thrown. If an error occurs the result will be
     * error code.
     **/
    public int     readUnsignedByte() {
        try { return m_dis.readUnsignedByte(); }
        catch (EOFException eof) {
            debug.e("CStreamReader::readUnsignedByte(): %s", eof);
            return ERROR.EOF;
        }
        catch (IOException _io) {
            debug.e("CStreamReader::readUnsignedByte(): %s", _io);
            return ERROR.IO;
        }
    }/*}}}*/
    // public int     readUnsignedShort();/*{{{*/
    /**
     * Reads two bytes from the input stream.
     * \returns An integer value made up from the two bytes read. The result
     * is always unsigned.
     * \remarks No exception is thrown. If an error occurs the result will be
     * the error code.
     **/
    public int     readUnsignedShort() {
        try { return m_dis.readUnsignedShort(); }
        catch (EOFException eof) {
            debug.e("CStreamReader::readUnsignedShort(): %s", eof);
            return ERROR.EOF;
        }
        catch (IOException _io) {
            debug.e("CStreamReader::readUnsignedShort(): %s", _io);
            return ERROR.IO;
        }
    }/*}}}*/
    // public String  readLine();/*{{{*/
    /**
     * This method is deprecated and cannot be supported.
     * \returns A \b null string, always.
     **/
    public String  readLine() {
        return null;
    }/*}}}*/
    // public void    readFully(byte[] buffer);/*{{{*/
    /**
     * Read bytes from the input stream.
     * \param buffer Array where the data should be copied. The operation will
     * block until \c buffer.length bytes have been read.
     * \remarks No exception is thrown. If an error occurs the \a buffer
     * argument will return unchanged. Use \c read(byte[]) instead.
     **/
    public void    readFully(byte[] buffer) {
        read(buffer);
    }/*}}}*/
    // public void    readFully(byte[] buffer, int offset, int length);/*{{{*/
    /**
     * Read bytes from the input stream.
     * \param buffer Buffer where the read bytes should be copied.
     * \param offset First element of \a buffer where bytes should be copied.
     * \param length Number of bytes to copy to \a buffer starting from \a
     * offset.
     * \remarks No exception is thrown. If an error occurs the \a buffer argument
     * will return unchanged. Use \c read(byte[],int,int) instead.
     **/
    public void    readFully(byte[] buffer, int offset, int length) {
        read(buffer, offset, length);
    }/*}}}*/
    // public int     skipBytes(int count);/*{{{*/
    /**
     * Skip bytes from the input stream.
     * \param count Number of bytes to skip.
     * \return The number of bytes actually skipped or an error value.
     * \remarks No exception is thrown. If an error occurs the result will be
     * error value.
     **/
    public int     skipBytes(int count) {
        try { return m_dis.skipBytes(count); }
        catch (IOException _io) {
            debug.e("CStreamReader::skipBytes(): %s", _io);
            return ERROR.IO;
        }
    }/*}}}*/
    //@}

    /** \name RAW METHODS */ //@{
    // public int read();/*{{{*/
    /**
     * Reads one byte and returns its value as an integer.
     * \returns The byte value or an error value. Error values are always
     * negative. The byte value is always returned as a positive number
     * regardless of its signal.
     **/
    public int read() {
        int result = ERROR.EOF;
        try { result = m_dis.read(); }
        catch (IOException ex) {
            result = ERROR.IO;
        }
        if (result == -1) result = ERROR.EOF;
        return result;
    }/*}}}*/
    // public int read(byte[] buffer);/*{{{*/
    /**
     * Read bytes from the input stream.
     * \param buffer Buffer to store the bytes read. The operation will block
     * until \c buffer#length bytes have been read.
     * \returns The number of bytes actually read or an error value.
     * \remarks No exception is thrown. If an error occurs the result will be
     * the error code.
     **/
    public int read(byte[] buffer) {
        return read(buffer, 0, buffer.length);
    }/*}}}*/
    // public int read(byte[] buffer, int offset, int length);/*{{{*/
    /**
     * Read bytes from the input stream.
     * \param buffer Buffer to store the bytes read. The operation will block
     *      until \c length bytes have been read.
     * \param offset First element of \a buffer to write the data.
     * \param length Total number of bytes to read.
     * \returns The number of bytes actually read or ERROR#IO if an error
     * occurred.
     * \remarks No exception is thrown. If an error occurs the result will be
     * the error code.
     **/
    public int read(byte[] buffer, int offset, int length) {
        try { return m_dis.read(buffer, offset, length); }
        catch (IOException _io) {
            debug.e("CStreamReader::read(): %s", _io);
            return ERROR.IO;
        }
    }/*}}}*/
    // public void close();/*{{{*/
    /**
     * Closes the stream.
     * This overrode implementation does not throw any exception.
     **/
    public void close() {
        try { m_dis.close(); }
        catch (Exception ex) {
            /* We do nothing. */
        }
        m_dis = null;
    }/*}}}*/
    //@}

    /** \name EXTENSIONS */ //@{
    // public String readString(int count, String enc);/*{{{*/
    /**
     * Reads a zero terminated string using the specified encoding.
     * \param count The minimum length of the string field.
     * \param enc String defining the encoding to convert from. This can be
     *      the following values:
     *      - \c ENC#LATIN1: This assumes that each byte represents one
     *        character.  The string is zero terminated, assuming that it was
     *        written using \c CStreamWriter::writeString(). You can use the
     *        \c LATIN1 constant.
     *      - \c ENC#UTF16: This is the default Java encoding. Each character is
     *        represented by 2 bytes. In this encoding the \a count parameter
     *        is interpreted as the length of the string, that is, it will be
     *        multiplied by 2 to reach the length in bytes. You can use the \c
     *        UTF16 constant.
     *      - \c ENC#MUTF8: Modified utf-8 encoding. In this encoding a
     *        character can have 1, 2 or 3 bytes. In this encoding the length
     *        of the string is taken from the first 2 bytes of the stream
     *        current position. The \a count parameter is not used. You can
     *        specify the \c MUTF8 constant.
     *      .
     * \returns A string result of the read and conversion. If an error occurs
     * the result will be \b null.
     **/
    public String readString(int count, String enc) {
        if (ENC.MUTF8.equals(enc)) {
            return readUTF();
        }
        else if (ENC.UTF16.equals(enc)) {
            int   cbLen = count * 2;
            byte[] data = new byte[ cbLen ];
            StringBuffer sb = new StringBuffer(count);

            read(data, 0, cbLen);
            for (int i = 0; i < cbLen; i+=2) {
                sb.append((char)((0xFF & (data[i] << 8)) | (0xFF & data[i+1])));
            }
            return sb.toString();
        }
        else if (ENC.LATIN1.equals(enc)) {
            byte[] str = new byte[ count ];

            if (read(str) < 0) return "";

            /* Search for the null terminator in reverse order. */
            count -= 1;
            while ((count >= 0) && (str[count] == 0x00)) count--;

            return strings.decode(str, 0, count+1, ENC.LATIN1);
        }
        return null;
    }/*}}}*/
    //@}

    /** \name DATA MEMBERS */ //@{
    protected DataInputStream m_dis;   /**< Source of data. */
    //@}
}
// vim:syntax=java.doxygen
