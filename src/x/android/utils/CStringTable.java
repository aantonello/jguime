/**
 * \file
 * Defines the CStringTable class.
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
 * \ingroup x_android_utils
 * Represents a string table resource in memory.
 * The strings and IDs of the resource are already parsed and loaded and
 * the table is ready for use.
 *
 * A string table is implemented in a plain XML file with the folloing format:
 * @code
 * <stringtable>
 *   <string id="0x01" value="Text of this item" />
 *   <string id="2" value="Text of second item" />
 * </stringtable>
 * @endcode
 * IDs should be always numeric but can be written in decimal, hexadecimal or
 * octal notaion. Each entry must have a unique ID. The class doesn't throw
 * any exception or error if it finds a duplicated ID but it will always
 * return the first found occurrency.
 *//* --------------------------------------------------------------------- */
public class CStringTable
{
    /** \name CONSTRUCTOR */ //@{
    // public CStringTable();/*{{{*/
    /**
     * Default constructor.
     **/
    public CStringTable() {
        m_ids    = null;
        m_values = null;
        m_count  = 0;
    }/*}}}*/
    // public CStringTable(CXmlNode[] nodes);/*{{{*/
    /**
     * Builds the list of strings.
     * \param nodes Nodes from a string table XML resource.
     **/
    public CStringTable(CXmlNode[] nodes) {
        _internal_parse(nodes);
    }/*}}}*/
    //@}

    /** \name Static Operations */ //@{
    // public static CStringTable LoadStream(InputStream is);/*{{{*/
    /**
     * Build an CStringTable object from an InputStream.
     * @param is The InputStream to parse. Must have a valid stringtable file.
     * @return A new CStringTable object.
     **/
    public static CStringTable LoadStream(InputStream is)
    {
        CStringTable stringTable = new CStringTable();
        CXmlFile file = CXmlFile.Load(is, ENC.UTF8);

        if (file == null) return stringTable;
        if (!"stringtable".equals(file.root.nodeName))
            return stringTable;

        stringTable._internal_parse(file.root.children);
        return stringTable;
    }/*}}}*/
    // public static LoadAsset(String assetPath);/*{{{*/
    /**
     * Load a string table in an 'assets' directory.
     * @param assetPath Path to load the file from.
     * @return A new CStringTable object.
     **/
    public static CStringTable LoadAsset(String assetPath)
    {
        InputStream is = SFAsset.Load(assetPath);
        if (is == null) return new CStringTable();

        return LoadStream(is);
    }/*}}}*/
    //@}

    /** \name OPERATIONS */ //@{
    // public boolean isEmpty();/*{{{*/
    /**
     * Checks if this table is empty or not.
     * \returns \b true if the table is empty. \b false otherwise.
     **/
    public boolean isEmpty() {
        return (m_count == 0);
    }/*}}}*/
    // public String  get(int stringID);/*{{{*/
    /**
     * Search for a string in the table.
     * \param stringID Identifier of the string to find.
     * \return The string in the specified ID or `strings::EMPTY` if not
     * found.
     **/
    public String get(int stringID) {
        for (int i = 0; i < m_count; i++) {
            if (m_ids[i] == stringID)
                return m_values[i];
        }
        return strings.EMPTY;
    }/*}}}*/
    //@}

    /** \name Internal Operations */ //@{
    // final void _internal_parse(CXmlNode[] nodes);/*{{{*/
    /**
     * Parses the nodes of a stringtable file.
     * @param nodes Nodes to parse.
     **/
    final void _internal_parse(CXmlNode[] nodes)
    {
        int count = arrays.length(nodes);

        if (count == 0) return;

        CXmlNode node;
        CXmlAttr attrID;
        CXmlAttr attrVal;

        /* Previously alloca memory for speed. */
        m_ids    = new int[count];
        m_values = new String[count];
        m_count  = 0;

        for (int i = 0; i < count; i++) {
            node = nodes[i];

            if (!node.nodeName.equals("string"))
                continue;           /* Skeep this node. */

            attrID = node.getAttribute("id");
            if (attrID == null)
                continue;           /* Without ID, skeep this too. */

            attrVal = node.getAttribute("value");
            if (attrVal == null)
                continue;           /* Without a value, doesn't matter too. */

            m_ids[i]    = attrID.intValue();
            m_values[i] = attrVal.value;
            m_count++;
        }
    }/*}}}*/
    //@}

    /** \name DATA MEMBERS */ //@{
    int[]    m_ids;         /**< String ID array.   */
    String[] m_values;      /**< String values.     */
    int      m_count;       /**< Number of entries. */
    //@}
}
// vim:syntax=java.doxygen
