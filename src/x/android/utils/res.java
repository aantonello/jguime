/**
 * \file
 * Defines the res class.
 *
 * \author Alessandro Antonello <aantonello@paralaxe.com.br>
 * \date   August 05, 2011
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
import x.android.defs.*;
import x.android.xml.*;

/**
 * Gives access and parsers to default resources.
 * When loading string resource they can be localized in a easy way. All
 * functions have a \a resName argument that must have the path and name of
 * the resource to load. This string can have a pseudo-macro ([...]) where
 * those function replace this macro with the locale identifier.
 *//* --------------------------------------------------------------------- */
public final class res
{
    /** \name STATIC LOADERS */ //@{
    // public static InputStream loadAsStream(String resName);/*{{{*/
    /**
     * Loads a resource into a in memory stream.
     * \param resName String with resource path and name.
     * \returns An instance of \c InputStream object means success. \b null
     * means failure.
     **/
    public static InputStream loadAsStream(String resName) {
        if (resName == null) return null;
        String resPath = localizePath(resName);
        return res.class.getClassLoader().getResourceAsStream(resPath);
    }/*}}}*/
    // public static CXmlFile    loadAsFile(String resName, String enc);/*{{{*/
    /**
     * Loads a resource as a in memory XML file.
     * \param resName String with path and the resource file name.
     * \param enc The file encoding. This is useful to parse correctly the
     * file loaded.
     * \returns The \c CXmlFile created or \b null if an error occurs.
     **/
    public static CXmlFile loadAsFile(String resName, String enc) {
        InputStream is = res.loadAsStream(resName);
        if (is == null) {
            return null;
        }

        return CXmlFile.Load(is, enc);
    }/*}}}*/
    // public static CXmlFile    loadAsFile(String resName);/*{{{*/
    /**
     * Loads a resource as a in memory XML file.
     * \param resName String with path and the resource file name.
     * \returns The \c CXmlFile created or \b null if an error occurs.
     **/
    public static CXmlFile loadAsFile(String resName) {
        InputStream is = res.loadAsStream(resName);
        if (is == null) return null;

        return CXmlFile.Load(is);
    }/*}}}*/
    // public static CStringTable loadAsTable(String resName, String enc);/*{{{*/
    /**
     * Loads a string table resource stream into memory.
     * \param resName Path and name of the string table resource.
     * \param enc The file encoding. This is useful to parse correctly the
     * file loaded.
     * \return A \c CStringTable object.
     **/
    public static CStringTable loadAsTable(String resName, String enc) {
        return _internal_getTable(res.loadAsFile(resName, enc));
    }/*}}}*/
    // public static CStringTable loadAsTable(String resName);/*{{{*/
    /**
     * Loads a string table resource stream into memory.
     * \param resName Path and name of the string table resource.
     * \return A \c CStringTable object.
     **/
    public static CStringTable loadAsTable(String resName) {
        return _internal_getTable(res.loadAsFile(resName));
    }/*}}}*/
    //@}

    /** \name STATIC FINDERS */ //@{
    // public static String getString(int resID, String resName, String enc);/*{{{*/
    /**
     * Gets a string from a string table resource.
     * A string table resource is a XML file with a known format.
     * \param resID The numeric identifier of the string to get.
     * \param resName The name of the resource where the string is.
     * \param enc The encoding of the file. If it is a standard resource file
     * the encoding is always UTF-8.
     * \returns The string object or \b null if an error occurs.
     **/
    public static String getString(int resID, String resName, String enc) {
        CXmlFile file = res.loadAsFile(resName, enc);

        if (file == null) return null;
        return _internal_getString(resID, file.root);
    }/*}}}*/
    // public static String getString(int resID, String resName);/*{{{*/
    /**
     * Gets a string from a string table resource.
     * A string table resource is a XML file with a known format.
     * \param resID The numeric identifier of the string to get.
     * \param resName The name of the resource where the string is.
     * \returns The string object or \b null if an error occurs.
     **/
    public static String getString(int resID, String resName) {
        CXmlFile file = res.loadAsFile(resName);

        if (file == null) return null;
        return _internal_getString(resID, file.root);
    }/*}}}*/
    // public static String getErrorDesc(int errCode);/*{{{*/
    /**
     * Get an error description.
     * The text is loaded from an standard resource.
     * \param errCode Code or error number.
     **/
    public static String getErrorDesc(int errCode) {
        return getString(errCode, localizePath("/x/android/[...]/errors.xml"), ENC.UTF8);
    }/*}}}*/
    //@}

    /** \name STATIC LOCALE */ //@{
    // public static String localizePath(String path);/*{{{*/
    /**
     * Localizes a path to load the correct resource string.
     * \param path String with a path to localize. Where the locale identifier
     * should be positioned the string must have a pseudo-macro ([...]) that
     * will be replaced.
     **/
    public static String localizePath(String path)
    {
        String country = System.getProperty("persist.sys.country");
        String locale;

        if (country == null) {
            country = System.getProperty("user.language");
        }

        if (strings.ncasecmp(country, "BR", "PT") >= 0)
            locale = "pt_BR";
        else if (strings.ncasecmp(country, "EN", "US") >= 0)
            locale = "en_US";
        else
            locale = "pt_BR";

        return strings.replace(path, "[...]", locale);
    }/*}}}*/
    //@}

    /** \name STATIC INTERNALS */ //@{
    // static String _internal_getString(int resID, CXmlNode node);/*{{{*/
    /**
     * Finds a string in a XML file.
     * \param resID The numerical string identifier.
     * \param rootNode The root element of the XML file.
     * \returns The string object or \b null if an error occurs.
     **/
    static String _internal_getString(int resID, CXmlNode rootNode) {
        CXmlNode[] nodes = rootNode.children;

        if (!rootNode.nodeName.equals("stringtable"))
            return null;            /* Invalid XML format. */
        else if (arrays.length(nodes) == 0)
            return null;            /* No string in this file. */

        CXmlNode node;
        CXmlAttr attrID;
        CXmlAttr attrValue;
        int count = nodes.length;

        for (int i = 0; i < count; i++) {
            node = nodes[i];

            if (!node.nodeName.equals("string"))
                continue;       /* Jump to the next node. */

            attrID = node.getAttribute("id");
            attrValue = node.getAttribute("value");

            if ((attrID == null) || (attrValue == null))
                continue;       /* Missing attribute. */

            if (resID != attrID.intValue())
                continue;

            /* Identifier found. Return its value. */
            return attrValue.value;
        }

        /* If we got here nothing was found. */
        return null;
    }/*}}}*/
    // static CStringTable _internal_getTable(CXmlFile file);/*{{{*/
    /**
     * Loads a string table resource stream into memory.
     * \param file A XML file to parse and load.
     * \return A \c CStringTable object.
     **/
    static CStringTable _internal_getTable(CXmlFile file) {
        if ((file == null) || (file.root == null)) {
            return new CStringTable();     /* Empty string table. */
        }

        CXmlNode rootNode = file.root;

        if (!rootNode.nodeName.equals("stringtable")) {
            return new CStringTable();     /* Empty string table. */
        }

        return new CStringTable(rootNode.children);
    }/*}}}*/
    //@}
}
// vim:syntax=java.doxygen
