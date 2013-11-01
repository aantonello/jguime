/**
 * \file
 * Defines the CXmlAttr class.
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

import x.android.utils.strings;

/**
 * \ingroup x_android_xml
 * Represents a XML attribute entity.
 * An attribute entity has a name and a value. Both are in text type but can
 * be converted in any numeric type.
 *//* --------------------------------------------------------------------- */
public final class CXmlAttr
{
    /** \name CONSTRUCTOR */ //@{
    // public CXmlAttr();/*{{{*/
    /**
     * Default constructor.
     **/
    public CXmlAttr() {
        this.name = null;
        this.value = null;
    }/*}}}*/
    // public CXmlAttr(String name, String value);/*{{{*/
    /**
     * Parametrized constructor.
     * \param name Name of the attribute.
     * \param value Value of the attribute.
     **/
    public CXmlAttr(String name, String value) {
        this.name  = name;
        this.value = value;
    }/*}}}*/
    //@}

    /** \name ATTRIBUTES */ //@{
    // public final boolean isBoolean();/*{{{*/
    /**
     * Check if this attribute value can be translated to a boolean value.
     * This implementation supports strings as 'true', 'yes', 'false' or 'no'
     * as boolean values. The comparison is case sensitive.
     * \returns \b true if the value of this attribute can be translated in a
     * boolean value. Otherwise the return is \b false.
     **/
    public final boolean isBoolean() {
        return ((this.value != null) && (this.value.equals("true") ||
                                         this.value.equals("false") ||
                                         this.value.equals("yes") ||
                                         this.value.equals("no")
                                        )
               );
    }/*}}}*/
    // public final boolean isFloat();/*{{{*/
    /**
     * Checks if the value of this attribute is a floating point number.
     * This implementation supports positive and negative floating point
     * numbers in the normal notation. Scientific notation is not supported.
     * \returns \b true if the value can be translated as a floating point
     * number. Otherwise the function returns \b false.
     **/
    public final boolean isFloat() {
        if (this.value == null) return false;

        char[] list = this.value.toCharArray();
        char   c;
        int    count = list.length;

        for (int i = 0; i < count; i++) {
            c = list[i];
            if ((i == 0) && (c == '+') || (c == '-'))
                continue;
            else if ((i == 0) && (c == '0'))
                return false;       /* Octal or hex number. */
            else if ((c == '.') || ((c >= '0') && (c <= '9')))
                continue;
            else
                return false;
        }
        return true;
    }/*}}}*/
    // public final boolean isDecimal();/*{{{*/
    /**
     * Checks if the value can be translated in a decimal number.
     * The implementation supports positive and negative numbers.
     * \returns \b true if the value has only decimal digits. Otherwise the
     * return is \b false.
     **/
    public final boolean isDecimal() {
        if (this.value == null) return false;

        String ref = this.value;
        int  count = ref.length();
        char c;

        for (int i = 0; i < count; i++) {
            c = ref.charAt(i);
            if ((i == 0) && ((c == '+') || (c == '-')))
                continue;
            else if ((i == 0) && (c == '0'))
                return false;       /* Octal or hex number. */
            else if ((c >= '0') && (c <= '9'))
                continue;
            else
                return false;
        }
        return true;
    }/*}}}*/
    // public final boolean isOctal();/*{{{*/
    /**
     * Checks if the value can be translated as an octal number.
     * This implementation supports only positive octal numbers. No plus ('+')
     * character allowed. The string must start with '0'.
     * \return \b true if the value can be translated as an octal number.
     * Otherwise the function returns \b false.
     **/
    public final boolean isOctal() {
        if (this.value == null) return false;

        String ref = this.value;
        if (ref.charAt(0) != '0') return false;

        int  count = ref.length();
        char c;

        for (int i = 1; i < count; i++) {
            c = ref.charAt(i);
            if ((c < '0') || (c > '7'))
                return false;
        }
        return true;
    }/*}}}*/
    // public final boolean isHex();/*{{{*/
    /**
     * Checks if the value can be translated in a hexadecimal number.
     * This implementation supports only positive hexadecimal numbers. No plus
     * '+' sign is allowed. The string must begin with '0x' or '0X'.
     * \returns \b true if the value can be translated as a hexadecimal number.
     * Otherwise the function returns \b false.
     **/
    public final boolean isHex() {
        if (this.value == null) return false;

        String ref = this.value;
        if (!ref.startsWith("0x") && !ref.startsWith("0X"))
            return false;

        int count = ref.length();
        char c;
        for (int i = 2; i < count; i++) {
            if (!strings.isHexChar(ref.charAt(i)))
                return false;
        }
        return true;
    }/*}}}*/
    // public final boolean isNumeric();/*{{{*/
    /**
     * Checks if the value of this attribute if formed only by numbers.
     * This implementation supports hexadecimal number that starts with '0x' or
     * '0X'. Also supports octal numbers starting with '0'. Decimal and
     * floating point numbers, positive or negative, are also supported.
     * \returns \b true if the value can be translated in a number of any kind.
     * Otherwise the return is \b false.
     **/
    public final boolean isNumeric() {
        return (isFloat() || isDecimal() || isOctal() || isHex());
    }/*}}}*/
    // public final int     intValue();/*{{{*/
    /**
     * Convert the attribute value to an integer number.
     * This conversion is valid only when the attribute has a number. Notice
     * that no value is reserved to indicate an error. When the conversion
     * cannot be done, the result is zero.
     **/
    public final int intValue() {
        if (isDecimal() || isFloat())
            return strings.toInt(this.value, 10);
        else if (isHex())
            return strings.toInt(this.value, 16);
        else if (isOctal())
            return strings.toInt(this.value, 8);

        return 0;
    }/*}}}*/
    // public final long    longValue();/*{{{*/
    /**
     * Converts the attribute value to a long number.
     * This conversion is valid only when the attribute has a number. No value
     * is reserved to indicate an error. When the conversion cannot be done,
     * the result is zero.
     **/
    public final long longValue() {
        if (isDecimal() || isFloat())
            return strings.toLong(this.value, 10);
        else if (isHex())
            return strings.toLong(this.value, 16);
        else if (isOctal())
            return strings.toLong(this.value, 8);

        return 0L;
    }/*}}}*/
    // public final float   floatValue();/*{{{*/
    /**
     * Converts the attribute value to a float number.
     * This conversion is valid only when the attribute has a number. No value
     * is reserved to indicate an error. When the conversion cannot be done,
     * the result is zero.
     **/
    public final float floatValue() {
        if (isFloat())
            return strings.toFloat(this.value);
        else
            return (float)this.intValue();
    }/*}}}*/
    //@}

    /** \name OPERATIONS */ //@{
    // public final String entityValue();/*{{{*/
    /**
     * Process this attribute value, replacing default XML symbols with their
     * entities.
     * This process is done automaticaly when getting the attribute text via
     * #toString() method. Replacing of symbols with entities is needed for
     * the XML specification.
     **/
    public final String entityValue() {
        int  limit   = strings.length(this.value);
        String value = this.value;

        if (limit == 0) return strings.EMPTY;

        char c;
        StringBuilder sb = new StringBuilder(limit * 4);

        for (int i = 0; i < limit; i++) {
            c = value.charAt(i);
            if (c == '&')
                sb.append("&amp;");
            else if (c == '"')
                sb.append("&quot;");
            else if (c == '\'')
                sb.append("&apos;");
            else if (c == '<')
                sb.append("&lt;");
            else if (c == '>')
                sb.append("&gt;");
            else
                sb.append(c);
        }
        return sb.toString();
    }/*}}}*/
    //@}

    /** \name OVERRIDES */ //@{
    // public boolean equals(Object obj);/*{{{*/
    /**
     * Checks the equality of this instance with another object.
     * \param obj the object to compare to.
     * \return \b true when the objects are equal. Otherwise \b false. The
     * objects are equal when both name and value are equals.
     **/
    public boolean equals(Object obj) {
        if (obj instanceof CXmlAttr)
        {
            CXmlAttr other = (CXmlAttr)obj;
            if ((this.name != null) && this.name.equals(other.name))
                if ((this.value != null) && this.value.equals(other.value))
                    return true;
        }
        return false;
    }/*}}}*/
    // public String  toString();/*{{{*/
    /**
     * Returns a string representation of this attribute object.
     * The returning string will have the following format:
     * @code
     * ' name="value"'
     * @endcode
     * With a space added before them.
     **/
    public String  toString() {
        return strings.format(" %s=\"%s\"", this.name, this.entityValue());
    }/*}}}*/
    //@}

    /** \name FIELDS */ //@{
    public String name;         /**< The attribute name. */
    public String value;        /**< The attribute value. */
    //@}
}
// vim:syntax=java.doxygen
