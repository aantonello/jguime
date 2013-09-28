/**
 * \file
 * Defines the SIZE class.
 *
 * \author Alessandro Antonello <aantonello@paralaxe.com.br>
 * \date   Outubro 23, 2012
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
 * Sizing constants.
 * This class is static. That is, it only declares static members.
 *//* --------------------------------------------------------------------- */
public final class SIZE
{
    // public static final int WIDTH   = 0x0001;/*{{{*/
    /**
     * Sizes only the width of the source object.
     **/
    public static final int WIDTH   = 0x0001;/*}}}*/
    // public static final int HEIGHT  = 0x0002;/*{{{*/
    /**
     * Sizes only the height of the source object.
     **/
    public static final int HEIGHT  = 0x0002;/*}}}*/
    // public static final int FIT     = 0x0004;/*{{{*/
    /**
     * Sizes width and height of the source object matching the size of the
     * referenced object but keep the aspect ratio.
     **/
    public static final int FIT     = 0x0004;/*}}}*/
    // public static final int STRETCH = (WIDTH | HEIGHT);/*{{{*/
    /**
     * Sizes width and height of the source object matching the size of the
     * referenced object.
     **/
    public static final int STRETCH = (WIDTH | HEIGHT);/*}}}*/
}
// vim:syntax=java.doxygen
