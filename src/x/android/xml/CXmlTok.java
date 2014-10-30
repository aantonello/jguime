/**
 * \file
 * Defines the CXmlTok class.
 *
 * \author Alessandro Antonello <aantonello@paralaxe.com.br>
 * \date   October 05, 2010
 *
 * \par Copyright
 * This file is provided in hope that it will be useful to someone. It is
 * offered in public domain. You may use, modify or distribute it freely.
 *
 * The code is provided "AS IS". There is no warranty at all, of any kind. You
 * may change it if you like. Or just use it as it is.
 */
package x.android.xml;

import java.io.InputStream;
import java.io.IOException;

import x.android.defs.ENC;
import x.android.utils.arrays;
import x.android.utils.strings;
import x.android.utils.debug;
import x.android.io.CStreamReader;
import x.android.io.stream_t;

/**
 * \ingroup x_android_xml
 * Responsible for reading and tokenize a XML file.
 *//* --------------------------------------------------------------------- */
public class CXmlTok
{
    /** \name CONSTRUCTOR */ //@{
    // public CXmlTok(String stream);/*{{{*/
    /**
     * Default constructor.
     * \param stream A string with the data to be tokenized.
     **/
    public CXmlTok(String stream) {
        m_stream = stream.toCharArray();
        m_index  = 0;
    }/*}}}*/
    //@}

    /** \name STATIC FUNCTIONS */ //@{
    // public static String  Decode(String text);/*{{{*/
    /**
     * Decode special characters from a string.
     * This function is used to substitute special entity notations from a
     * string. Special characters processed by this function are:
     * - <b>\&amp;</b>: This sequence is used to embed an ampersand character.
     * - <b>\&apos;</b>: This embed a single quote character in the string.
     * - <b>\&quot;</b>: Embed a double-quote character in the string.
     * - <b>\&lt;</b>: Embed a less-than character.
     * - <b>\&gt;</b>: Embed a greater-than character.
     * - <b>%#????</b>: Embed a UNICODE character in the string. This will
     * insert a sequence like '\\u0000' in the Java string object.
     * .
     * \param text The string to be decoded.
     * \returns The same string passed in the \a text argument processed.
     **/
    public static String Decode(String text)
    {
        StringBuilder buff;
        int  i = 0, count = text.length();
        char c;

        buff = new StringBuilder( count );
        while (i < count)
        {
            c = text.charAt(i++);
            if ((c != '&') && (c != '%'))
                buff.append(c);
            else if (c == '&')
            {
                if (text.regionMatches(true, i, "amp;", 0, 4)) {
                    buff.append('&'); i += 4;
                } else if (text.regionMatches(true, i, "apos;", 0, 5)) {
                    buff.append('\''); i += 5;
                } else if (text.regionMatches(true, i, "quot;", 0, 5)) {
                    buff.append('\"'); i += 5;
                } else if (text.regionMatches(true, i, "lt;", 0, 3)) {
                    buff.append('<'); i += 3;
                } else if (text.regionMatches(true, i, "gt;", 0, 3)) {
                    buff.append('>'); i += 3;
                } else {
                    buff.append(c);
                }
            }
            else if (c == '%')
            {
                if ((i >= count) || (text.charAt(i) != '#'))
                    buff.append(c);
                else {
                    buff.append('\\').append('u');
                    while ((++i < count) && Character.isDigit(c = text.charAt(i)))
                        buff.append(c);
                }
            }
        }
        return buff.toString();
    }/*}}}*/
    // public static CXmlTok LoadStream(InputStream is);/*{{{*/
    /**
     * Creates an CXmlTok object from an InputStream.
     * \param is The stream to load the data. It is closed after the reading
     * operation.
     * \return A CXmlTok object created from the stream or \NULL, if the
     * stream could not be loaded. Notice that the XML file is not validated
     * in this operation. Any stream can be loaded.
     * \remarks This function recognizes the file encoding through its BOM
     * (Byte Order Mark) bytes.
     **/
    public static CXmlTok LoadStream(InputStream is)
    {
        CStreamReader sr = new CStreamReader( is );
        int count = 0, pos = 0;
        byte[] buffer = null;

        /* Does a progressive read of the file. Can be made in one step if the
         * method available() return to us the entire file length.
         */
        while ((count = sr.available()) > 0) {
            buffer = arrays.realloc(buffer, (pos + count));
            count  = sr.read(buffer, pos, count);
            if (count < 0) break;       /* End of File found. */
            pos += count;
        }
        sr.close();         /* Isn't needed any more. */

        if (buffer == null) {
            debug.w("CXmlTok::LoadStream(InputStream): InputStream is empty!\n");
            return null;    /* Load fails. */
        }

        /* Check if we have BOM. When we does, the enconding can be resolved
         * by the byte order mark. Otherwise we will use UTF-8 since it is the
         * default for XML files.
         */
        String text = null;
        if (skipByteOrderMark(buffer) > 0)
            text = strings.toString(buffer, 0, buffer.length);
        else
            text = strings.toString(buffer, 0, buffer.length, ENC.UTF8);

        if (text == null) {
            debug.w("CXmlTok::LoadStream(InputStream): strings.toString() FAILED!\n");
            return null;
        }
        return new CXmlTok(text);
    }/*}}}*/
    // public static CXmlTok LoadStream(InputStream is, String enc);/*{{{*/
    /**
     * Creates an CXmlTok object from an InputStream.
     * \param is The stream to load the data. It is closed after the reading
     * operation.
     * \param enc The character encoding of the stream. The object could find
     * the encoding looking into the file. But the stream is loaded into
     * memory to speed up the parsing. Knowing the file encoding before reading
     * the file is fast and easy.
     * \return A CXmlTok object created from the stream or \b null, if the
     * stream could not be loaded. Notice that the XML file is not validated
     * in this operation. Any stream can be loaded.
     **/
    public static CXmlTok LoadStream(InputStream is, String enc)
    {
        CStreamReader sr = new CStreamReader( is );
        int count = 0, pos = 0;
        byte[] buffer = null;

        /* Does a progressive read of the file. Can be made in one step if the
         * method available() return to us the entire file length.
         */
        while ((count = sr.available()) > 0) {
            buffer = arrays.realloc(buffer, (pos + count));
            count  = sr.read(buffer, pos, count);
            if (count < 0) break;       /* End of File found. */
            pos += count;
        }
        sr.close();         /* Isn't needed any more. */

        /* Check for the nullity of 'buffer'. Happens when the InputStream is
         * empty or has an error.
         */
        if (buffer == null) {
            debug.w("CXmlTok::LoadStream(InputStream,String): InputStream is empty!\n");
            return null;
        }

        /* Check if we have BOM. */
        int start = skipByteOrderMark(buffer);
        pos -= start;

        /* Now we convert it to a string using the encoding defined. */
        String text = strings.decode(buffer, start, pos, enc);
        if (text == null) {
            debug.w("CXmlTok::LoadStream(InputStream,String): strings.decode() FAILED!\n");
            return null;
        }

        /* Create the tokenizer class using this string. */
        return new CXmlTok(text);
    }/*}}}*/
    // public static CXmlTok LoadStream(stream_t stream);/*{{{*/
    /**
     * Creates a new object based in the passed stream.
     * \param stream The stream with data to read. The stream must have the
     * BOM (Byte Order Mark) on its beginning so the encoding can be
     * recognized. Also, its read position must be placed in the right place.
     * \return On success the function returns a new object to parse the XML
     * file. On failure the function returns **null**.
     **/
    public static CXmlTok LoadStream(stream_t stream)
    {
        int total = stream.available();
        byte[] bytes = new byte[ total ];
        String text  = null;

        /* With this object we can read everything in one single pass. */
        total = stream.read(bytes, 0, total);

        /* If we have BOM, the conversion will get the right encoding. When
         * not, we assume UTF-8 since it is the default for XML files.
         */
        if (skipByteOrderMark(bytes) > 0)
            text = strings.toString(bytes, 0, total);
        else
            text = strings.toString(bytes, 0, total, ENC.UTF8);

        if (text == null) {
            debug.w("CXmlTok::LoadStream(stream_t): strings.toString() FAILED!\n");
            return null;
        }

        return new CXmlTok(text);
    }/*}}}*/
    // public static CXmlTok LoadStream(stream_t stream, String enc);/*{{{*/
    /**
     * Creates a new object based in the passed stream.
     * \param stream The stream with the data to read. This stream **must
     * not** have the BOM (Byte Order Mark) on its beginning.
     * \param enc The stream encoding. This will be used to convert the binary
     * data into standard Java String object.
     * \return On success the function returns a new object to parse the XML
     * file. On failure the function returns **null**.
     **/
    public static CXmlTok LoadStream(stream_t stream, String enc)
    {
        String text = stream.read(enc, stream.available());
        if (strings.length(text) == 0) {
            debug.w("CXmlTok::LoadStream(stream_t,String): stream::read(String,int): FAILED!\n");
            return null;
        }
        return new CXmlTok(text);
    }/*}}}*/
    //@}

    /** \name ATTRIBUTES */ //@{
    // public final boolean hasMore();/*{{{*/
    /**
     * Checks if there is more characters in the stream to parse.
     * \returns \TRUE if the stream has more characters. Otherwise \FALSE.
     **/
    public final boolean hasMore() {
        return (m_index < m_stream.length);
    }/*}}}*/
    // public final boolean match(int pos, String text);/*{{{*/
    /**
     * Checks if a text matches the characters in the stream.
     * \param pos The position where the comparison should start. If less than
     *      zero the current stream position is assumed.
     * \param text The text to match in the characters.
     * \return \TRUE if the string \a text matches the characters in the
     * stream. Otherwise \FALSE.
     * \remarks This function does not changes the current stream position.
     **/
    public final boolean match(int pos, String text) {
        int count = text.length();
        if (pos < 0) pos = m_index;
        if ((pos + count) >= m_stream.length) return false;
        for (int i = 0; i < count; i++, pos++) {
            if (text.charAt(i) != m_stream[pos]) return false;
        }
        return true;
    }/*}}}*/
    //@}

    /** \name OPERATIONS */ //@{
    // public final boolean back(int count);/*{{{*/
    /**
     * Back a number of characters in the stream.
     * \param count The number of characters to return in the stream reading.
     * \return \TRUE on success. \FALSE if the \a count chars are invalid.
     **/
    public final boolean back(int count) {
        if ((m_index - count) < 0) return false;
        m_index -= count;
        return true;
    }/*}}}*/
    // public final char   next();/*{{{*/
    /**
     * Gets the next character of the stream.
     * \returns The next character or the character '\0' if no more characters
     * are available.
     * \remarks The current position is updated in this function.
     **/
    public final char next() {
        if (!hasMore()) return (char)0;
        return m_stream[m_index++];
    }/*}}}*/
    // public final char   next(char c);/*{{{*/
    /**
     * Gets the next character checking if it matches the argument passed.
     * \param c The character to match the next in the stream.
     * \return The same character passed at \a c if the next character in the
     * stream matches it. Otherwise the function returns zero.
     * \remarks If the character matches the current position is updated.
     * Otherwise the current position is left unchanged.
     **/
    public final char next(char c) {
        char n = next();
        if (n == c) return n;
        back(1); return (char)0;
    }/*}}}*/
    // public final String next(int count);/*{{{*/
    /**
     * Get the next count of characters from the stream.
     * \param count The number of characters to get.
     * \return A string formed of the characters read. If \a count is greater
     * than the number of characters remaining in the stream the function
     * returns the remaining characters in the stream ignoring \a count. When
     * there is no more characters to be read, the result is an empty string.
     **/
    public final String next(int count)
    {
        if (m_index >= m_stream.length)
            return strings.EMPTY;

        if ((m_index + count) >= m_stream.length)
            count = m_stream.length - m_index;

        String result = new String(m_stream, m_index, count);
        m_index += count;
        return result;
    }/*}}}*/
    // public final char   getNextToken();/*{{{*/
    /**
     * Get the next character token in the stream.
     * The function jumps any blank character (spaces, tabs, carriage return,
     * line feeds, etc.) and comment blocks.
     * \return The character that starts the next token means success. 0 means
     * that there is an error in the XML stream or the end of the stream was
     * found.
     * \remarks The current reading position is updated in this function.
     **/
    public final char getNextToken() {
        int i     = m_index;
        int count = m_stream.length;
        char c;

        while (i < count) {
            c = m_stream[i++];

            if ((c == ' ') || (c == '\t') || (c == '\n') || (c == '\r'))
                continue;

            if ((c == '<') && match(i, "!--")) {
                i += 3;     /* Pass over '!--' sequence. */
                while (i < count) {
                    if ((m_stream[i++] == '-') && match(i, "->")) {
                        i += 2;     /* Pass over '->' sequence. */
                        break;
                    }
                }
            } else {
                m_index = i;        /* Next character position. */
                return c;           /* Returns the token found. */
            }
        }
        return (char)0;     /* Token not found. */
    }/*}}}*/
    // public final String getNextWord();/*{{{*/
    /**
     * Get a entire word from the stream.
     * A word is a sequence of consecutive alphanumeric characters. But the
     * first character must be a letter. This function will use \c
     * getNextToken() to skip any white space or comment block to reach the
     * first word character. A sequence will be concatenated to form a string.
     * \return The string with the next word or \b null if no word is found.
     * \remarks The current reading position is updated in this function.
     **/
    public final String getNextWord() {
        char c = getNextToken();

        if (!Character.isLetter(c)) {
            back(1);
            return null;
        }
        
        int start = m_index - 1;    /* The position where 'c' was found. */
        int count = m_stream.length;
        int i     = m_index;

        while ((i < count) && (Character.isLetterOrDigit(m_stream[i]) ||
                               m_stream[i] == '_' || m_stream[i] == ':')) {
            i++;
        }

        String word = new String(m_stream, start, (i - start));

        /* Put the current position at the next stream character. */
        m_index = i;
        return word;
    }/*}}}*/
    // public final String getQuotedText();/*{{{*/
    /**
     * Get a string bounded with double quotes.
     * In a SGML file there is no single-quoted string. All strings need to be
     * double-quoted. And cannot cross a line. They are used as attribute
     * values. When this function starts the next reading position must be the
     * open double-quote character.
     * \return The entire quoted string without the quotes.
     * \remarks The current reading position is updated in this function.
     **/
    public final String getQuotedText() {
        char c = next();

        if (c != '"') return null;

        String text = null;
        int start = m_index - 1;        /* Position of 'c'. */
        int count = m_stream.length;
        int i     = m_index;

        while (i < count) {
            if ((c = m_stream[i]) == '"') {
                text = new String(m_stream, start + 1, (i - 1 - start));
                break;
            } else if ((c == '\n') || (c == '\r')) {
                break;      /* malformed XML. */
            }
            i++;
        }

        /* Update the current read position to the next character. */
        m_index = i + 1;
        return text;
    }/*}}}*/
    // public final void   dump();/*{{{*/
    /**
     * Dumps the stream content.
     **/
    public final void dump()
    {
        if (!debug.enabled) return;
        debug.timestamp = false;

        int index = 0, start = 0;
        final int limit = arrays.length(m_stream);

        do
        {
            while (index < limit)
            {
                if (m_stream[index] == '\n') {
                    index++;
                    break;
                }
                index++;
            }
            debug.w(strings.toString(m_stream, start, (index - start)));
            start = index;
            index = start;
        } while (start < limit);
        debug.timestamp = true;

    }/*}}}*/
    //@}

    /** \name Internal Static Operations */ //@{
    // static final int skipByteOrderMark(byte[] data);/*{{{*/
    /**
     * Check if the byte array has a byte order mark (BOM).
     * @param data The byte array to check.
     * @return When the BOM is found, the function return the number of bytes
     * to skip from its start.
     **/
    static final int skipByteOrderMark(byte[] data)
    {
        final int size = arrays.length(data);

        if (size >= 4)
        {
            if ((data[0] == (byte)0xFF) && (data[1] == (byte)0xFE) &&
                (data[2] == 0x00) && (data[3] == 0x00))
                return 4;       /* ENC.UTF32LE */
            else if ((data[0] == 0x00) && (data[1] == 0x00) &&
                     (data[2] == (byte)0xFE) && (data[3] == (byte)0xFF))
                return 4;       /* ENC.UTF32BE */
        }

        if (size >= 3)
        {
            if ((data[0] == (byte)0xEF) && (data[1] == (byte)0xBB) &&
                (data[2] == (byte)0xBF))
                return 3;       /* ENC.UTF8 */
        }

        if (size >= 2)
        {
            if ((data[0] == (byte)0xFF) && (data[1] == (byte)0xFE))
                return 2;       /* ENC.UTF16LE */
            else if ((data[0] == (byte)0xFE) && (data[1] == (byte)0xFF))
                return 2;       /* ENC.UTF16BE */
        }
        return 0;
    }/*}}}*/
    //@}

    /** \name FIELDS */ //@{
    public int    m_index;          /**< Next character index. */
    public char[] m_stream;         /**< The stream of characters to parse. */
    //@}
}
// vim:syntax=java.doxygen
