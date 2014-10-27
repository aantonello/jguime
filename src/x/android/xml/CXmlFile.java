/**
 * \file
 * Defines the CXmlFile class.
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
package x.android.xml;

import java.io.*;

import x.android.defs.*;
import x.android.utils.*;
import x.android.io.*;

/**
 * \ingroup x_android_xml
 * Represents a XML file or stream.
 * Right now this class only implements the reading and parsing operations. No
 * output or writing operations are available.
 *//* --------------------------------------------------------------------- */
public class CXmlFile
{
    /** \name CONSTRUCTORS */ //@{
    // public CXmlFile();/*{{{*/
    /**
     * Constructs an empty CXmlFile object.
     * The root node will be \b null. You must create and set the root node so
     * this file can be written.
     **/
    public CXmlFile()
    {
        this.root = null;
    }/*}}}*/
    //@}

    /** \name STATIC FUNCTIONS */ //@{
    // public static CXmlFile Load(CXmlTok tok);/*{{{*/
    /**
     * Creates a CXmlFile object from a CXmlTok object.
     * \param tok The CXmlTok object to parse and load the XML file.
     * \return The CXmlFile created or \b null if an error occurs.
     **/
    public static CXmlFile Load(CXmlTok tok)
    {
        if (tok == null) return null;       /* Invalid file. */

        /* The first 5 characters of the file must be '<?xml' */
        String buff = tok.next(5);
        if (!buff.equals("<?xml")) {
            debug.w("CXmlFile::Load() failed! File does not start with '<?xml': '%s'", buff);
            tok.dump();
            return null;
        }

        CXmlFile doc = new CXmlFile();

        char c;

        while ((c = tok.next()) != '\0') {
            if ((c == '?') && (tok.next('>') == '>')) {
                c = tok.getNextToken();
                if (c != '<') {
                    debug.w("CXmlFile::Load() failed! Invalid root element.");
                    return null;        /* Invalid root element. */
                }

                tok.back(1);
                doc.root = CXmlNode.Parse(tok);
                break;
            }
        }

        if ((debug.enabled) && (doc.root == null))
        {
            tok.dump();
        }
        return ((doc.root == null) ? null : doc);
    }/*}}}*/
    // public static CXmlFile Load(String pathName, String encoding);/*{{{*/
    /**
     * Loads an XML file from disc.
     * @param pathName Path name and extension of the file to load. Cannot be
     * \b null not empty string.
     * @param encoding The encoding of the file on disc. If \b null or empty
     * string the default will be \b UTF-8.
     * @return On success a newly created %CXmlFile object. Otherwise \b null.
     * @since 2.4
     **/
    public static CXmlFile Load(String pathName, String encoding)
    {
        if (strings.empty(pathName)) return null;
        if (strings.empty(encoding)) encoding = ENC.UTF8;

        FileInputStream fis;

        try { fis = new FileInputStream(pathName); }
        catch (Exception ex) {
            debug.e(ex, "$n in CXmlFile::Load(String,String) for file: '%s'\n\t|=> $s\n",
                    pathName);
            return null;
        }
        return Load(fis, encoding);
    }
    /*}}}*/
    // public static CXmlFile Load(File file, String enc);/*{{{*/
    /**
     * Loads an XML file from disc.
     * @param file \c File object naming the file to load. Cannot be \b null.
     * @param enc The encoding of the file on disc. If \b null or empty string
     * the default will be \b UTF-8.
     * @return On success a newly created %CXmlFile object. Otherwise \b null.
     * @since 2.4
     **/
    public static CXmlFile Load(File file, String enc)
    {
        if (file == null) return null;

        if (strings.empty(enc)) enc = ENC.UTF8;

        FileInputStream fis;

        try { fis = new FileInputStream(file); }
        catch (Exception ex) {
            debug.e(ex, "$n in CXmlFile::Load(File,String) for file: '%s'\n\t|=> $s\n",
                    file.getAbsolutePath());
            return null;
        }
        return Load(fis, enc);
    }/*}}}*/
    // public static CXmlFile Load(InputStream is);/*{{{*/
    /**
     * Loads a XML file from an InputStream.
     * \param is The \c InputStream with the XML file to load.
     * \returns The \c CXmlFile instance created or \b null if something goes
     * wrong.
     * \remarks The \a is stream is passed to CXmlTok#LoadStream(InputStream).
     * So it is closed when the reading ends. Even when it fails.
     **/
    public static CXmlFile Load(InputStream is)
    {
        CXmlTok tok = CXmlTok.LoadStream( is );
        if (tok == null) return null;

        return Load( tok );
    }/*}}}*/
    // public static CXmlFile Load(InputStream is, String enc);/*{{{*/
    /**
     * Loads a XML file from the givem stream.
     * \param is The \c InputStream with the file in memory.
     * \param enc The encoding of the file. See \c x.android.defs#ENC.
     * \returns The \c CXmlFile instance created or \b null if something goes
     * wrong.
     * \remarks The \a is stream is passed to
     * CXmlTok#LoadStream(InputStream,String). So it is closed when the
     * reading ends. Even when it fails.
     **/
    public static CXmlFile Load(InputStream is, String enc)
    {
        CXmlTok tok = CXmlTok.LoadStream(is, enc);
        if (tok == null) return null;
        return Load( tok );
    }/*}}}*/
    // public static CXmlFile Load(stream_t stream, String enc);/*{{{*/
    /**
     * Reads an XML file from a `stream_t` object.
     * \param stream Stream with data to read from. This stream **must not**
     * have the BOM (Byte Order Mark) on its beginning.
     * \param enc The encoded data. This will be used to convert the data into
     * standard Java String encoding.
     **/
    public static CXmlFile Load(stream_t stream, String enc)
    {
        CXmlTok tok = CXmlTok.LoadStream(stream, enc);
        if (tok == null) return null;
        return CXmlFile.Load(tok);
    }/*}}}*/
    //@}

    /** \name OVERRIDES */ //@{
    // public String toString();/*{{{*/
    /**
     * Returns a string representation of this XML file object.
     * The resulting string will be ready to be written in an XML file.
     **/
    public String toString() {
        StringBuilder sb = new StringBuilder(1024);

        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sb.append(root.toString());

        return sb.toString();
    }/*}}}*/
    //@}

    /** \name OVERRIDABLES */ //@{
    // public void write(String pathName, String enc, boolean bom);/*{{{*/
    /**
     * Writes a file on disc.
     * @param pathName Path, name and extension for the target file. Cannot be
     * \b null nor an empty string.
     * @param enc The encoding to be used to write the file. If \b null or
     * empty string \b UTF-8 will be used.
     * @param bom Set to \b true to write the Byte Order Mark in the beginning
     * of the file. \b false to leave the mark of.
     * @since 2.4
     **/
    public void write(String pathName, String enc, boolean bom)
    {
        if (strings.empty(pathName)) return;
        if (strings.empty(enc)) enc = ENC.UTF8;

        FileOutputStream fos;

        try { fos = new FileOutputStream(pathName); }
        catch (Exception ex) {
            debug.e(ex, "$n in CXmlFile::write(String,String,boolean) for file: '%s'\n\t|=> $s\n",
                    pathName);
            return;
        }

        write(fos, enc, bom);   /* "fos" is closed here. */
    }
    /*}}}*/
    // public void write(File file, String enc, boolean bom);/*{{{*/
    /**
     * Writes a file on disc.
     * @param file \c File object representing the file to write. Cannot be \b
     * null.
     * @param enc The encoding to be used to write the file. If \b null or
     * empty string \b UTF-8 will be used.
     * @param bom Set to \b true to write the Byte Order Mark in the beginning
     * of the file. \b false to leave the mark of.
     * @since 2.4
     **/
    public void write(File file, String enc, boolean bom)
    {
        if (file == null) return;

        if (strings.empty(enc)) enc = ENC.UTF8;

        FileOutputStream fos;

        try { fos = new FileOutputStream(file); }
        catch (Exception ex) {
            debug.e(ex, "$n in CXmlFile::write(File,String,boolean) for file: '%s'\n\t|=> $s\n",
                    file.getAbsolutePath());

            return;
        }

        write(fos, enc, bom);   /* "fos" is closed here. */
    }
    /*}}}*/
    // public void write(OutputStream os, String enc, boolean bom);/*{{{*/
    /**
     * Writes the content of this XML file to a stream.
     * \param os The OutputStream to write to.
     * \param enc The encoding to write the file.
     * \param bom Sets if BOM (byte order mark) will be written to the file or
     * not.
     * \remarks The stream is closed after the writing ends.
     **/
    public void write(OutputStream os, String enc, boolean bom) {
        CStreamWriter writer = new CStreamWriter(os);

        /* First things first. Write the BOM if requested so. */
        if (bom)
        {
            if (enc.equals(ENC.UTF32LE))
                writer.put(new byte[] { (byte)0xFF, (byte)0xFE, 0x00, 0x00 });
            else if (enc.equals(ENC.UTF32BE))
                writer.put(new byte[] { 0x00, 0x00, (byte)0xFE, (byte)0xFF });
            else if (enc.equals(ENC.UTF16LE))
                writer.put(new byte[] { (byte)0xFF, (byte)0xFE });
            else if (enc.equals(ENC.UTF16BE))
                writer.put(new byte[] { (byte)0xFE, (byte)0xFF });
            else if (enc.equals(ENC.UTF8))
                writer.put(new byte[] { (byte)0xEF, (byte)0xBB, (byte)0xBF });
        }

        /* Starts the file using the encoding. */
        writer.put("<?xml version=\"1.0\" encoding=\""+enc+"\"?>\n", enc);
        if (this.root != null) {
            this.root.write(writer, enc);
        }
        writer.flush();
        writer.close();
    }/*}}}*/
    // public void write(stream_t stream, String enc, boolean bom);/*{{{*/
    /**
     * Writes the content of this XML file into a stream object.
     * \param stream The stream object to write the file.
     * \param enc Encoding to write the stream.
     * \param bom **true** to write the Byte Order Mark in the start of the
     * stream. **false** will skip this task.
     **/
    public void write(stream_t stream, String enc, boolean bom) {
        /* First things first. Write the BOM if requested so. */
        if (bom)
        {
            if (enc.equals(ENC.UTF32LE))
                stream.write(new byte[] { (byte)0xFF, (byte)0xFE, 0x00, 0x00 });
            else if (enc.equals(ENC.UTF32BE))
                stream.write(new byte[] { 0x00, 0x00, (byte)0xFE, (byte)0xFF });
            else if (enc.equals(ENC.UTF16LE))
                stream.write(new byte[] { (byte)0xFF, (byte)0xFE });
            else if (enc.equals(ENC.UTF16BE))
                stream.write(new byte[] { (byte)0xFE, (byte)0xFF });
            else if (enc.equals(ENC.UTF8))
                stream.write(new byte[] { (byte)0xEF, (byte)0xBB, (byte)0xBF });
        }

        /* Starts the file using the encoding. */
        stream.write("<?xml version=\"1.0\" encoding=\""+enc+"\"?>\n", enc);
        if (this.root != null) {
            this.root.write(stream, enc);
        }
    }/*}}}*/
    // public void log();/*{{{*/
    /**
     * Writes this XML file in the LOG output.
     * This is used only in DEBUG mode. If DEBUG is disabled this function
     * does nothing.
     **/
    public void log() {
        if (!debug.enabled) return;
        if (root == null) return;

        debug.w("\n");      /* Writes the timestamp. */
        debug.timestamp = false;
        debug.w("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
        root.log(0);
        debug.timestamp = true;
        debug.w("\n");      /* Writes the timestamp. */
    }/*}}}*/
    //@}

    /** \name FIELDS */ //@{
    public CXmlNode root;              /**< The root node. */
    //@}
}
// vim:syntax=java.doxygen
