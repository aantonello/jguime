/**
 * \file
 * Defines the CStreamWriter class.
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

import java.io.OutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Writes data to an output stream.
 * Extends \c DataOutputStream providing some extra functionality.
 *//* --------------------------------------------------------------------- */
public class CStreamWriter implements DataOutput
{
    /** \name CONSTRUCTOR */ //@{
    // public CStreamWriter(OutputStream out);/*{{{*/
    /**
     * Default constructor.
     * \param out The output stream to be written.
     **/
    public CStreamWriter(OutputStream out) {
        m_dos = new DataOutputStream(out);
    }/*}}}*/
    //@}

    /** \name ATTRIBUTES */ //@{
    // public int length();/*{{{*/
    /**
     * Returns the number of bytes written to the stream so far.
     **/
    public int length() {
        return m_dos.size();
    }/*}}}*/
    //@}

    /** \name DataOutput IMPLEMENTATION */ //@{
    // public void write(byte[] data, int start, int count);/*{{{*/
    /**
     * Writes \a data to the internal buffer.
     * \param data The array to be written.
     * \param start Index of the first byte in \a data to be written.
     * \param count Total number of bytes to write.
     **/
    public void write(byte[] data, int start, int count) {
        this.put(data, start, count);
    }/*}}}*/
    // public void write(byte[] data);/*{{{*/
    /**
     * Writes the \a data array into the stream.
     * \param data Array to be written.
     **/
    public void write(byte[] data) {
        this.put(data);
    }/*}}}*/
    // public void write(int b);/*{{{*/
    /**
     * Writes a single byte to the stream.
     * Only the low-order byte of the integer is written.
     * \param b Value to be written.
     **/
    public void write(int b) {
        this.putByte(b);
    }/*}}}*/
    // public void writeBoolean(boolean value);/*{{{*/
    /**
     * Writes a boolean value to the internal buffer.
     * This function writes a single byte. When \a value is \b true a byte of
     * value 1 is written. When \a value is \b false, a byte with value 0 is
     * written.
     * \param value Boolean value to be written.
     **/
    public void writeBoolean(boolean value) {
        this.putBoolean(value);
    }/*}}}*/
    // public void writeByte(int b);/*{{{*/
    /**
     * Writes a single byte to the internal buffer.
     * Only the low-order byte of the integer is written.
     * \param b Value to be written.
     **/
    public void writeByte(int b) {
        this.putByte(b);
    }/*}}}*/
    // public void writeChar(int c);/*{{{*/
    /**
     * Writes a \b char value into the internal buffer.
     * \param c The character to be written.
     * \remarks The Java \c char has two bytes written in big-endian order.
     **/
    public void writeChar(int c) {
        this.putChar(c);
    }/*}}}*/
    // public void writeShort(int s);/*{{{*/
    /**
     * Writes a \b short value to the internal buffer.
     * Two bytes are written in big-endian order.
     * \param s The \b short value to be written.
     **/
    public void writeShort(int s) {
        this.putShort(s);
    }/*}}}*/
    // public void writeInt(int v);/*{{{*/
    /**
     * Writes an \b integer value to the internal buffer.
     * Four bytes in big-endian order are written.
     * \param v The integer value to be written.
     **/
    public void writeInt(int v) {
        this.putInt(v);
    }/*}}}*/
    // public void writeLong(long l);/*{{{*/
    /**
     * Writes a \b long value to the internal buffer.
     * Eight bytes in big-endian order are written.
     * \param l The long value to be written.
     **/
    public void writeLong(long l) {
        this.putLong(l);
    }/*}}}*/
    // public void writeFloat(float f);/*{{{*/
    /**
     * Writes a \b float value into the internal buffer.
     * Four bytes in big-endian order are written.
     * \param f The float value to be written.
     **/
    public void writeFloat(float f) {
        this.putFloat(f);
    }/*}}}*/
    // public void writeDouble(double d);/*{{{*/
    /**
     * Writes a \b double value to the internal buffer.
     * Eight bytes are written in big-endian order.
     * \param d The double value to be written.
     **/
    public void writeDouble(double d) {
        this.putDouble(d);
    }/*}}}*/
    // public void writeBytes(String s);/*{{{*/
    /**
     * Write bytes to the internal buffer.
     * The characters of the string \a s are converted to bytes and written,
     * in order, into the internal buffer. No special encoding is used.
     * \param s The string to be written.
     * \remarks As an addition to the standard \b DataOutput contract, the
     * bytes written will be appended with a single zero (null) byte.
     **/
    public void writeBytes(String s) {
        this.putBytes(s);
    }/*}}}*/
    // public void writeChars(String s);/*{{{*/
    /**
     * Write bytes to the internal buffer.
     * Each character in the source string \a s is written using two bytes in
     * big-endian order. As an addition to the \c DataOutput standard
     * contract, the sequence is ended with a \b NULL character (two bytes
     * zero).
     * \param s The string to be written.
     **/
    public void writeChars(String s) {
        this.putChars(s);
    }/*}}}*/
    // public void writeUTF(String s);/*{{{*/
    /**
     * Writes a string using a modified UTF-8 encoding.
     * This encoding is specified in the Java documentation. Each character
     * can be represented by 1, 2 or 3 bytes. The first 2 bytes of the
     * sequence will hold the length of the converted byte array.
     * \param s The string to be converted and written.
     * \see http://docs.oracle.com/javase/1.5.0/docs/api/java/io/DataInput.html#modified-utf-8
     **/
    public void writeUTF(String s) {
        this.putUTF(s);
    }/*}}}*/
    //@}

    /** \name OVERRIDES */ //@{
    // public void flush();/*{{{*/
    /**
     * Flushes the data in the stream.
     * No exception is thrown in this operation.
     **/
    public void flush() {
        try { m_dos.flush(); }
        catch (Exception ex) {
            debug.e("CStreamWriter::flush(): %s", ex);
        }
    }/*}}}*/
    // public void close();/*{{{*/
    /**
     * Closes the stream.
     * This operation does not throw any exception.
     **/
    public void close() {
        try { m_dos.close(); }
        catch (Exception ex) {
            /* Ignored, does not metter */
        }
    }/*}}}*/
    //@}

    /** \name EXTENSIONS */ //@{
    // public int putBoolean(boolean value);/*{{{*/
    /**
     * Write a boolean value to the stream.
     * \param value The boolean value to be written. If it is \b true the
     * function writes one byte with value equals 1. If \b false one byte with
     * value zero is written.
     * \returns And error code. Zero means success. Any other value is an
     * error code. Notably \c ERROR#IO.
     **/
    public int putBoolean(boolean value) {
        try { m_dos.writeBoolean(value); }
        catch (Exception ex) {
            debug.e("CStreamWriter::putBoolean(): %s", ex);
            return ERROR.IO;
        }
        return ERROR.SUCCESS;
    }/*}}}*/
    // public int putByte(int value);/*{{{*/
    /**
     * Write one byte to the output stream.
     * \param value An integer which the low order byte will be written.
     * \returns And error code. Zero means success. Any other value is an
     * error code. Notably \c ERROR#IO.
     **/
    public int putByte(int value) {
        try { m_dos.writeByte(value); }
        catch (Exception ex) {
            debug.e("CStreamWriter::putByte(): %s", ex);
            return ERROR.IO;
        }
        return ERROR.SUCCESS;
    }/*}}}*/
    // public int putBytes(String data);/*{{{*/
    /**
     * Write a string encoded as ASCII byte sequence.
     * \param data The string to be written. It will be followed by a zero
     *      byte value.
     * \returns The number of bytes written, including the terminator NULL
     * character, means success. A value less than zero means failure.
     **/
    public int putBytes(String data) {
        int curr = length();
        try { m_dos.writeBytes(data); m_dos.writeByte(0); }
        catch (Exception ex) {
            debug.e("CStreamWriter::putBytes(): %s", ex);
            return ERROR.IO;
        }
        return length() - curr;
    }/*}}}*/
    // public int putChar(int value);/*{{{*/
    /**
     * Write a character into the output stream.
     * \param value An integer to be converted to a character for writing.
     * \returns And error code. Zero means success. Any other value is an
     * error code. Notably \c ERROR#IO.
     **/
    public int putChar(int value) {
        try { m_dos.writeChar(value); }
        catch (Exception ex) {
            debug.e("CStreamWriter::putChar(): %s", ex);
            return ERROR.IO;
        }
        return ERROR.SUCCESS;
    }/*}}}*/
    // public int putChars(String data);/*{{{*/
    /**
     * Writes a string in UTF-16 encoding to the output stream.
     * \param data String to be written. The string will follow 2 bytes of an
     *      terminator NULL character.
     * \returns The number of bytes written, including the terminator NULL
     * character, means success. A value less than zero means failure.
     **/
    public int putChars(String data) {
        int curr = length();
        try { m_dos.writeChars(data); m_dos.write(new byte[] { 0x00, 0x00 }); }
        catch (Exception ex) {
            debug.e("CStreamWriter::putChars(): %s", ex);
            return ERROR.IO;
        }
        return length() - curr;
    }/*}}}*/
    // public int putShort(int value);/*{{{*/
    /**
     * Write a short value using 2 bytes of the output stream.
     * \param value An integer which will be converted to a short for writing.
     * \returns And error code. Zero means success. Any other value is an
     * error code. Notably \c ERROR#IO.
     **/
    public int putShort(int value) {
        try { m_dos.writeShort(value); }
        catch (Exception ex) {
            debug.e("CStreamWriter::putShort(): %s", ex);
            return ERROR.IO;
        }
        return ERROR.SUCCESS;
    }/*}}}*/
    // public int putInt(int value);/*{{{*/
    /**
     * Writes an integer value using four bytes of the output stream.
     * \param value The integer value to be written.
     * \returns And error code. Zero means success. Any other value is an
     * error code. Notably \c ERROR#IO.
     **/
    public int putInt(int value) {
        try { m_dos.writeInt( value ); }
        catch (Exception ex) {
            debug.e("CStreamWriter::putInt(): %s", ex);
            return ERROR.IO;
        }
        return ERROR.SUCCESS;
    }/*}}}*/
    // public int putLong(long value);/*{{{*/
    /**
     * Writes a long value using eight bytes from the output stream.
     * \param value The long value to be written.
     * \returns And error code. Zero means success. Any other value is an
     * error code. Notably \c ERROR#IO.
     **/
    public int putLong(long value) {
        try { m_dos.writeLong(value); }
        catch (Exception ex) {
            debug.e("CStreamWriter::putLong(): %s", ex);
            return ERROR.IO;
        }
        return ERROR.SUCCESS;
    }/*}}}*/
    // public int putFloat(float value);/*{{{*/
    /**
     * Writes a float value using four bytes from the output stream.
     * \param value The float value to write.
     * \returns And error code. Zero means success. Any other value is an
     * error code. Notably \c ERROR#IO.
     **/
    public int putFloat(float value) {
        try { m_dos.writeFloat(value); }
        catch (Exception ex) {
            debug.e("CStreamWriter::putFloat(): %s", ex);
            return ERROR.IO;
        }
        return ERROR.SUCCESS;
    }/*}}}*/
    // public int putDouble(double value);/*{{{*/
    /**
     * Writes a double value in the output stream.
     * \param value The double value to write.
     * \returns And error code. Zero means success. Any other value is an
     * error code. Notably \c ERROR#IO.
     **/
    public int putDouble(double value) {
        try { m_dos.writeDouble(value); }
        catch (Exception ex) {
            debug.e("CStreamWriter::putDouble(): %s", ex);
            return ERROR.IO;
        }
        return ERROR.SUCCESS;
    }/*}}}*/
    // public int putUTF(String data);/*{{{*/
    /**
     * Writes a string using the modified UTF-8 encoding.
     * \param data The string to write.
     * \return The number of bytes written indicates success. A values less
     * than zero is an error code.
     **/
    public int putUTF(String data) {
        int curr = length();
        try { m_dos.writeUTF(data); }
        catch (Exception ex) {
            debug.e("CStreamWriter::putUTF(): %s", ex);
            return ERROR.IO;
        }
        return length() - curr;
    }/*}}}*/
    // public int put(byte[] data);/*{{{*/
    /**
     * Writes data to the output stream.
     * \param data The data to be written.
     * \return The number of bytes written indicates success. A values less
     * than zero is an error code. Notably \c ERROR#IO or \c ERROR#POINTER.
     **/
    public int put(byte[] data) {
        return put(data, 0, arrays.length(data));
    }/*}}}*/
    // public int put(byte[] data, int offset, int count);/*{{{*/
    /**
     * Writes data to the output stream.
     * \param data The data to be written.
     * \param offset First element of \a data to write in the stream.
     * \param count Total number of bytes, starting from \a offset, to write
     *      to the stream.
     * \return The number of bytes written indicates success. A values less
     * than zero is an error code. Notably \c ERROR#IO or \c ERROR#POINTER.
     **/
    public int put(byte[] data, int offset, int count) {
        int curr = length();
        try { m_dos.write(data, offset, count); }
        catch (IOException ex) {
            debug.e("CStreamWriter::put(): %s", ex);
            return ERROR.IO;
        }
        catch (NullPointerException nullex) {
            debug.e("CStreamWriter::put(): %s", nullex);
            return ERROR.POINTER;
        }
        return (length() - curr);
    }/*}}}*/
    // public int putString(String data, int minLen, String enc);/*{{{*/
    /**
     * Writes a string, using the specified encoding, in the stream.
     * \param data String to write.
     * \param minLen The minimum length of the binary output. This length is
     * taken literally. So if encoding is UTF-16 each character will occupy 2
     * bytes in the stream. If UTF-8 a character can have 1, 2 or 3 bytes.
     * Care should be taken when specifying encoding. Is more efficient to use
     * \c putChars() when writing UTF-16 strings than this method. This method
     * is better only when writing Latin1 strings with fixed field length.
     * \param enc The encoding to use. Can be:
     *      - \c ENC#LATIN1: ISO-8859-1 character encoding. \a minLen is
     *        respected and the string will be zero terminated.
     *      - \c ENC#UTF16: 16-bit character encoding. \a minLen is respected
     *        and must take account the primer of 2 bytes per character. The
     *        string will be zero terminated.
     *      - \c ENC#MUTF8: 1, 2 or 3 bytes per character, as needed. \a
     *        minLen is ignored. The final length of the data will be written
     *        in the first 2 bytes of the field, as an unsigned short value.
     *      .
     * \returns The total bytes written to the stream means success. Any other
     * value is an error code.
     **/
    public int putString(String data, int minLen, String enc) {
        if (ENC.MUTF8.equals(enc)) {
            return putUTF(data);
        }
        else if (!ENC.UTF16.equals(enc) && !ENC.LATIN1.equals(enc))
            return ERROR.PARM;

        byte[] array = strings.encode(data, enc, minLen);

        if (array == null) return ERROR.FAILED;
        put(array);
        return array.length;
    }/*}}}*/
    // public int put(String data, String enc);/*{{{*/
    /**
     * Converts and writes the string to the stream.
     * \param data String to convert.
     * \param enc Encoding to convert the string. Can be one of the following:
     * - ENC#ASCII: The string will be converted to ASCII and written to
     * stream.
     * - ENC#LATIN1: The string will be converted to ISO-8859-1 and then
     * written to the stream.
     * - ENC#UTF16LE: The string will be converted to UTF-16 little-endian and
     * then written to the stream.
     * - ENC#UTF16BE: The string will be converted to UTF-16 big-endian and
     * then written to the stream.
     * - ENC#UTF32LE: The string will be converted to UTF-32 little-endian and
     * then written to the stream.
     * - ENC#UTF32BE: The string will be converted to UTF-32 big-endian and
     * then written to the stream.
     * - ENC#UTF8: The string will be converted to UTF-8 and written to the
     * stream. This is the standard UTF-8 enconding. Not the Java modified
     * version. If you want to write the Java modified UTF-8 encoding use the
     * putUTF() function.
     * .
     * \return The number of bytes written in the stream.
     **/
    public int put(String data, String enc) {
        return this.put(strings.encode(data, enc));
    }/*}}}*/
    //@}

    /** \name DATA MEMBERS */ //@{
    protected DataOutputStream m_dos;       /**< The output stream. */
    //@}
}
// vim:syntax=java.doxygen
