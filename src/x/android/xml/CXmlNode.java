/**
 * \file
 * Defines the CXmlNode class.
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

import x.android.utils.*;
import x.android.io.CStreamWriter;
import x.android.io.stream_t;

/**
 * Represents a XML node element.
 * A note is a simplest element in a XML file. A node can have child nodes or
 * a text content. But not both (at least in this implementation).
 *//* --------------------------------------------------------------------- */
public class CXmlNode
{
    /** \name CONSTRUCTORS */ //@{
    // public CXmlNode();/*{{{*/
    /**
     * Creates an empty XML node.
     **/
    public CXmlNode() {
        this.nodeName = null;
        this.attrs    = null;
        this.children = null;
    }/*}}}*/
    // public CXmlNode(String name);/*{{{*/
    /**
     * Creates an XML node with the specified name.
     * \param name A string with the name for the node.
     **/
    public CXmlNode(String name) {
        this.nodeName = name;
        this.attrs    = null;
        this.children = null;
    }/*}}}*/
    //@}

    /** \name STATIC FUNCTIONS */ //@{
    // public static CXmlNode Parse(CXmlTok tok);/*{{{*/
    /**
     * Parses a XML document fragment into a node object.
     * \param tok A CSgmlTok object with the XML fragment.
     * \return An instance of a CXmlNode object indicates success. \b null
     * will be returned if the fragment is invalid or a block comment.
     * \remarks The \a tok object must be pointing to the angle bracket that
     * starts the fragment ('\<'). The parse will end when the closing TAG is
     * found.
     **/
    public static CXmlNode Parse(CXmlTok tok) {
        char c = tok.getNextToken();
        if (c != '<') {
            debug.w("CXmlNode::Parse() failed! Invalid tag start.");
            return null;
        }

        String data;
        String temp = tok.getNextWord();
        if (temp == null) {
            debug.w("CXmlNode::Parse() failed! Tag doesn't have a name.");
            return null;
        }

        CXmlNode node = new CXmlNode(temp);

        /* Process the attributes. */
        while ((temp = tok.getNextWord()) != null)
        {
            if ((c = tok.next()) != '=') {
                debug.w("CXmlNode::Parse('%s') failed! Invalid attribute: '%s'.", node.nodeName, temp);
                return null;            /* Invalid attribute specifier. */
            }

            data = tok.getQuotedText();
            if (data == null) {
                debug.w("CXmlNode::Parse('%s') failed! Attribute with no value: '%s'", node.nodeName, temp);
                return null;            /* Malformed XML. */
            }
            node.append(temp, CXmlTok.Decode(data));  /* Append this attribute. */
        }

        c = tok.getNextToken();
        if (c == '/')
        {
            if ((c = tok.getNextToken()) == '>')
                return node;            /* End of this node. */
            else
            {
                debug.w("CXmlNode::Parse('%s') failed! Invalid tag end: '%c'", node.nodeName, c);
                return null;            /* Malformed XML. */
            }
        }

        if (c != '>') {
            debug.w("CXmlNode::Parse('%s') failed! Invalid tag end mark: '%c'", node.nodeName, c);
            return null;
        }

        CXmlNode child = null;

        /* Process child nodes, if any, or the end of this node. */
        while ((c = tok.getNextToken()) != '\0')
        {
            if (c != '<') {
                debug.w("CXmlNode::Parse('%s') failed! Invalid child tag start.", node.nodeName);
                return null;            /* Malformed XML. */
            }

            if ((c = tok.next()) == '/') {
                temp = tok.getNextWord();
                if (!temp.equals(node.nodeName)) {
                    debug.w("CXmlNode::Parse('%s') failed! Invalid finish tag name: '%s'", node.nodeName, temp);
                    return null;        /* Malformed XML. */
                }

                if ((c = tok.next()) != '>') {
                    debug.w("CXmlNode::Parse('%s') failed! Invalid tag end mark: '%c'", node.nodeName, c);
                    return null;        /* Malformed XML. */
                }

                return node;            /* End of this node found. */
            }

            /* Start the process of a child node. */
            tok.back(2);
            child = CXmlNode.Parse(tok);
            if (child == null) {
                debug.w("CXmlNode::Parse('%s') failed! Invalid child element", node.nodeName);
                return null;        /* Malformed XML. */
            }
            node.append(child);
        }
        return node;
    }/*}}}*/
    //@}

    /** \name PROPERTIES */ //@{
    // public final boolean hasAttributes();/*{{{*/
    /**
     * Checks if this node has attributes.
     * \returns \b true if this node has attributes. Otherwise \b false.
     **/
    public final boolean hasAttributes() {
        return ((this.attrs != null) && (this.attrs.length > 0));
    }/*}}}*/
    // public final boolean hasChildren();/*{{{*/
    /**
     * Check if this node has child nodes.
     * \returns \b true if this node has child nodes. Otherwise \b false.
     **/
    public final boolean hasChildren() {
        return ((this.children != null) && (this.children.length > 0));
    }/*}}}*/
    // public final boolean hasAttribute(String name);/*{{{*/
    /**
     * Checks if an attribute was set in this node.
     * \param name Attribute name to check.
     * \return \b true when the attribute exists. Otherwise \b false.
     **/
    public final boolean hasAttribute(String name) {
        CXmlAttr[] attrs = this.attrs;
        int        limit = arrays.length(attrs);

        for (int i = 0; i < limit; i++) {
            if (name.equals(attrs[i].name))
                return true;
        }
        return false;
    }/*}}}*/
    // public final int     childCount();/*{{{*/
    /**
     * Gets the number of children elements.
     **/
    public final int     childCount() {
        return arrays.length(this.children);
    }/*}}}*/
    //@}

    /** \name ATTRIBUTES OPERATIONS */ //@{
    // public final int      indexOf(CXmlAttr attr);/*{{{*/
    /**
     * Gets the index of the given attribute.
     * \param attr Attribute to get its index.
     * \return The attribute index or -1 if the \a attr attribute is not in
     * the list.
     **/
    public final int      indexOf(CXmlAttr attr) {
        int limit = arrays.length(this.attrs);
        CXmlAttr[] attrList = this.attrs;

        for (int i = 0; i < limit; i++) {
            if (attr == attrList[i])
                return i;
        }
        return -1;
    }/*}}}*/
    // public final CXmlAttr getAttribute(String name);/*{{{*/
    /**
     * Get the attribute object of the specified name.
     * \param name The name of the attribute required.
     * \returns A reference of the attribute object or \b null if an attribute
     * with the specified name was not found.
     **/
    public final CXmlAttr getAttribute(String name) {
        CXmlAttr[] list = this.attrs;
        int count = arrays.length(list);

        for (int i = 0; i < count; i++) {
            if (list[i].name.equals(name)) {
                return list[i];
            }
        }
        return null;
    }/*}}}*/
    // public final String   getStringValue(String name);/*{{{*/
    /**
     * Get the value of a named attribute as a String.
     * \param name The name of the attribute.
     * \returns The value of the attribute. \b null if the attribute is not
     * found.
     **/
    public final String getStringValue(String name) {
        CXmlAttr attr = getAttribute(name);
        if (attr == null) return null;
        return attr.value;
    }/*}}}*/
    // public final int      getIntValue(String name);/*{{{*/
    /**
     * Gets the value of an attribute as an integer.
     * \param name The name of the attribute.
     * \return An integer converted from the attribute value.
     * \remarks There is no exception if the value could not be converted to
     * an integer. If the string is not a valid number a zero will be
     * returned.
     **/
    public final int      getIntValue(String name) {
        CXmlAttr attr = getAttribute(name);
        if (attr == null) return 0;
        return attr.intValue();
    }/*}}}*/
    // public final float    getFloatValue(String name);/*{{{*/
    /**
     * Gets the value of an attribute as a float number.
     * \param name The name of the attribute.
     * \return A float number converted from the attribute value.
     * \remarks There is no exception if the value could not be converted to
     * float. If the string is not a valid number, zero will be
     * returned.
     **/
    public final float    getFloatValue(String name) {
        CXmlAttr attr = getAttribute(name);
        if (attr == null) return 0.0f;
        return attr.floatValue();
    }/*}}}*/
    // public final int      append(CXmlAttr attr);/*{{{*/
    /**
     * Appends an attribute object in the list of this node.
     * \param attr The attribute object.
     * \returns The index position of this attribute in the list. If the
     * function fails the return is less than zero.
     **/
    public final int append(CXmlAttr attr) {
        CXmlAttr[] list = null;
        int count = arrays.length(this.attrs);

        list = new CXmlAttr[count + 1];
        for (int i = 0; i < count; i++) {
            list[i] = this.attrs[i];
        }
        list[count] = attr;
        this.attrs  = list;
        return count;
    }/*}}}*/
    // public final int      append(String name, String value);/*{{{*/
    /**
     * Append an attribute in the list of this node.
     * \param name The name for the attribute.
     * \param value The value for the attribute.
     * \returns The index position of this attribute in the list. If the
     * function fails the return is less than zero. If this node already has
     * an attribute with the passed name its value will be changed and its
     * index will be returned.
     **/
    public final int append(String name, String value) {
        CXmlAttr attr = getAttribute(name);
        if (attr != null) {
            attr.value = value;
            return indexOf(attr);
        }
        return append(new CXmlAttr(name, value));
    }/*}}}*/
    // public final int      append(String name, int value);/*{{{*/
    /**
     * \copydoc append(String,String)
     **/
    public final int append(String name, int value) {
        return append(name, String.valueOf(value));
    }/*}}}*/
    // public final int      append(String name, long value);/*{{{*/
    /**
     * \copydoc append(String,String)
     **/
    public final int append(String name, long value) {
        return append(name, String.valueOf(value));
    }/*}}}*/
    // public final int      append(String name, float value);/*{{{*/
    /**
     * \copydoc append(String,String)
     **/
    public final int      append(String name, float value) {
        return append(name, String.valueOf(value));
    }/*}}}*/
    // public final CXmlAttr remove(String name);/*{{{*/
    /**
     * Remove um atributo.
     * \param name Nome do atributo que deve ser removido. Note que a pesquisa
     * é sensível ao caso.
     * \return A função retorna o atributo removido.
     **/
    public final CXmlAttr remove(String name) {
        int count = arrays.length(this.attrs);
        CXmlAttr[] attrList = this.attrs;

        if (count == 0) return null;
        if (count == 1) {
            this.attrs = null;
            if (name.equals(attrList[0].name))
                return attrList[0];
            else
                return null;
        }

        CXmlAttr[] otherList = new CXmlAttr[count - 1];
        CXmlAttr   result    = null;
        int index = 0;

        for (int i = 0; i < count; i++) {
            if (name.equals(attrList[i].name))
                result = attrList[i];
            else
            {
                otherList[index] = attrList[i];
                index++;
            }
        }
        this.attrs = otherList;
        return result;
    }/*}}}*/
    //@}

    /** \name NODES OPERATIONS */ //@{
    // public final int      indexOf(String name);/*{{{*/
    /**
     * Get the index of the specified node.
     * \param name The name of the child node requested.
     * \returns A zero based index representing the position of the node in
     * this node list or -1, if the a node with the specified name is not
     * found.
     **/
    public final int indexOf(String name) {
        CXmlNode[] list = this.children;
        int count = arrays.length(list);

        for (int i = 0; i < count; i++) {
            if (list[i].nodeName.equals(name)) {
                return i;
            }
        }
        return -1;
    }/*}}}*/
    // public final CXmlNode getChild(String name);/*{{{*/
    /**
     * Get a reference of a child with the specified name.
     * \param name The name of the child node required.
     * \returns A \c CXmlNode object reference or \b null, if a node with the
     * specified name is not found.
     **/
    public final CXmlNode getChild(String name) {
        int index = indexOf(name);
        if (index < 0) return null;
        return this.children[index];
    }/*}}}*/
    // public final int      append(CXmlNode node);/*{{{*/
    /**
     * Appends a node element in the child list of this node.
     * \param node The node object to be appended.
     * \returns A zero based index position of the node in the list means
     * success. A value less than zero means failure.
     **/
    public final int append(CXmlNode node) {
        CXmlNode[] list = null;
        int count = arrays.length(this.children);

        list = new CXmlNode[count + 1];
        for (int i = 0; i < count; i++) {
            list[i] = this.children[i];
        }
        list[count]   = node;
        this.children = list;
        return count;
    }/*}}}*/
    //@}

    /** \name OVERRIDES */ //@{
    // public boolean equals(Object obj);/*{{{*/
    /**
     * Checks the equality of this instance with another object.
     * \param obj the object to compare to.
     * \return \b true when the objects are equal. Otherwise \b false. The
     * objects are equal only if all its attributes and children are also
     * equal.
     **/
    public boolean equals(Object obj) {
        if (obj instanceof CXmlNode)
        {
            CXmlNode node = (CXmlNode)obj;

            if ((this.nodeName != null) && this.nodeName.equals(node.nodeName))
            {
                int limit = arrays.length(this.attrs);

                for (int i = 0; i < limit; i++)
                    if (!this.attrs[i].equals(node.attrs[i]))
                        return false;

                limit = arrays.length(this.children);
                for (int x = 0; x < limit; x++)
                    if (!this.children[x].equals(node.children[x]))
                        return false;

                return true;
            }
        }
        return false;
    }/*}}}*/
    // public String  toString();/*{{{*/
    /**
     * Returns a string representation of this node object.
     * The resulting string will be ready to be written in an XML file.
     **/
    public String  toString() {
        StringBuilder sb = new StringBuilder(256);

        sb.append("<"+this.nodeName);

        int limit = arrays.length(this.attrs);
        for (int i = 0; i < limit; i++)
            sb.append(this.attrs[i].toString());

        limit = arrays.length(this.children);
        if (limit == 0)
            sb.append(" />\n");
        else
        {
            sb.append(">\n");
            for (int d = 0; d < limit; d++)
                sb.append(this.children[d].toString());
            sb.append("</"+this.nodeName+">\n");
        }
        return sb.toString();
    }/*}}}*/
    //@}

    /** \name OVERRIDABLES */ //@{
    // public void write(CStreamWriter sw, String enc);/*{{{*/
    /**
     * Writes the content of this node to a stream.
     * \param sw The stream to write this node.
     * \param enc The encoding to convert this node content.
     **/
    public void write(CStreamWriter sw, String enc) {
        sw.put("<"+nodeName, enc);

        int limit = arrays.length(this.attrs);
        for (int i = 0; i < limit; i++)
            sw.put(this.attrs[i].toString(), enc);

        limit = arrays.length(this.children);
        if (limit == 0)
            sw.put(" />\n", enc);
        else
        {
            sw.put(">\n", enc);
            for (int d = 0; d < limit; d++)
                this.children[d].write(sw, enc);
            sw.put("</"+nodeName+">\n", enc);
        }
    }/*}}}*/
    // public void write(stream_t stream, String enc);/*{{{*/
    /**
     * Writes the content of this node, and all its child nodes, into the
     * *stream_t* class.
     * \param stream `stream_t` class to write the content to.
     * \param enc The encoding to be used to write the data.
     **/
    public void write(stream_t stream, String enc) {
        stream.write("<"+nodeName, enc);

        int limit = arrays.length(this.attrs);
        for (int i = 0; i < limit; i++)
            stream.write(this.attrs[i].toString(), enc);

        limit = arrays.length(this.children);
        if (limit == 0)
            stream.write(" />\n", enc);
        else
        {
            stream.write(">\n", enc);
            for (int d = 0; d < limit; d++)
                this.children[d].write(stream, enc);
            stream.write("</"+nodeName+">\n", enc);
        }
    }/*}}}*/
    // public void log(int indent);/*{{{*/
    /**
     * Writes this node in the LOG output.
     * \param indent Indentation level.
     **/
    public void log(int indent) {
        StringBuilder sb = new StringBuilder(1024);

        sb.append(strings.repeat(' ', indent))
            .append('<').append(this.nodeName);

        int index = 0;
        int limit = arrays.length(this.attrs);

        while (index < limit)
            sb.append(this.attrs[index++].toString());

        limit = arrays.length(this.children);
        if (limit == 0)
        {
            sb.append(" />\n");
            debug.w(sb.toString());
            return;
        }

        sb.append(">\n");
        debug.w(sb.toString());
        sb = null;

        index = 0;
        while (index < limit)
            this.children[index++].log(indent + 2);

        debug.w("%s</%s>\n", strings.repeat(' ', indent), this.nodeName);
    }/*}}}*/
    //@}

    /** \name DATA MEMBERS */ //@{
    public String     nodeName;    /**< The name of this node. */
    public CXmlAttr[] attrs;       /**< The list of attributes and values. */
    public CXmlNode[] children;    /**< Array of children nodes, if any.   */
    //@}
}
// vim:syntax=java.doxygen
