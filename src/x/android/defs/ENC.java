/**
 * \file
 * Defines the ENC class.
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
package x.android.defs;

/**
 * \ingroup x_android_defs
 * Declares the list of string enconding supported.
 * These encodings are used in the \c utils.strings#encode() and \c
 * utils.strings#decode() functions. Also they are used in the classes,
 * io.CBinaryReader, io.CBinaryWriter, io.CStreamReader, io.CStreamWriter.
 *//* --------------------------------------------------------------------- */
public final class ENC
{
    public static final String ASCII  = "US-ASCII";     /**< ASCII encoding.*/
    public static final String LATIN1 = "ISO-8859-1";   /**< Latin1 encoding.*/
    public static final String UTF16  = "UTF-16";       /**< UTF16 with BOM. */
    public static final String UTF8   = "UTF-8";        /**< UTF-8 encoding.*/
    public static final String MUTF8  = "UTF-8";        /**< UTF-8 encoding.*/
    public static final String UTF16LE = "UTF-16LE";    /**< UTF-16 little endianess encoding. */
    public static final String UTF16BE = "UTF-16BE";    /**< UTF-16 big endianess encoding. */
    public static final String UTF32LE = "UTF-32LE";    /**< UTF-32 little endianess encoding. */
    public static final String UTF32BE = "UTF-32BE";    /**< UTF-32 big endianess encoding. */
}
// vim:syntax=java.doxygen
