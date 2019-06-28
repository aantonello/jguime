/**
 * \file
 * Defines the ALIGN class.
 *
 * \author Alessandro Antonello <aantonello@paralaxe.com.br>
 * \date   October 21, 2010
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
 * Declares the alignment constants.
 *//* --------------------------------------------------------------------- */
public final class ALIGN
{
    /**
     * Aligns the left side of the client object with the left side of the
     * reference object.
     **/
    public static final int LEFT     = 0x0001;

    /**
     * Aligns the top edge of the client object with the top edge of the
     * reference object.
     **/
    public static final int TOP      = 0x0002;

    /**
     * Aligns the right edge of the client object with the right edge of the
     * reference object.
     **/
    public static final int RIGHT    = 0x0004;

    /**
     * Aligns the bottom edge of the client object with the bottom edge of the
     * reference object.
     **/
    public static final int BOTTOM   = 0x0008;

    /**
     * Aligns the right edge of the client object with the left edge of the
     * reference object.
     **/
    public static final int ATLEFT   = 0x0010;

    /**
     * Aligns the bottom edge of the client object at the top edge of the
     * reference object.
     **/
    public static final int ATTOP    = 0x0020;

    /**
     * Aligns the left edge of the client object at the right edge of the
     * reference object.
     **/
    public static final int ATRIGHT  = 0x0040;

    /**
     * Aligns the top edge of the client object at the bottom edge of the
     * reference object.
     **/
    public static final int ATBOTTOM = 0x0080;

    /**
     * Horizontal align the client object at the center of the reference
     * object.
     **/
    public static final int HCENTER  = 0x0100;

    /**
     * Vertical align the client object at the center of the reference object.
     **/
    public static final int VCENTER  = 0x0200;

    /**
     * Centralizes the client object in the center of the reference object.
     **/
    public static final int CENTER   = (HCENTER | VCENTER);
}
// vim:syntax=java.doxygen
