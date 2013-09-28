/**
 * \file
 * Defines the RES class.
 *
 * \author Alessandro Antonello <aantonello@paralaxe.com.br>
 * \date   August 04, 2011
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
 * Declares the published resource identifiers.
 *//* --------------------------------------------------------------------- */
public final class RES
{
    // public static final class IDS; {{{
    /**
     * Declares all string resource IDs.
     * This list is used to load the string resources.
     * \see utils#res
     **/
    public static final class IDS {
        /* INTERFACE STRINGS {{{ */
        /**
         * \name INTERFACE STRINGS
         * Strings used in the common user interface.
         *///@{
        public static final int CANCEL = 1;
        public static final int OK     = 2;
        public static final int YES    = 3;
        public static final int NO     = 4;
        ///@} INTERFACE STRINGS }}}
        /* CALENDAR STRINGS {{{ */
        /**
         * \name CALENDAR STRINGS
         * Strings used to name days and months.
         *///@{
        public static final int SUNDAY      = 1;
        public static final int MONDAY      = 2;
        public static final int THUSDAY     = 3;
        public static final int WEDNESDAY   = 4;
        public static final int TURSDAY     = 5;
        public static final int FRIDAY      = 6;
        public static final int SATURDAY    = 7;

        public static final int JAN = 11;
        public static final int FEB = 12;
        public static final int MAR = 13;
        public static final int APR = 14;
        public static final int MAY = 15;
        public static final int JUN = 16;
        public static final int JUL = 17;
        public static final int AUG = 18;
        public static final int SEP = 19;
        public static final int OCT = 20;
        public static final int NOV = 21;
        public static final int DEC = 22;
        ///@} CALENDAR STRINGS }}}
    } /*}}}*/
    // public static final String ERRORS = "/x/android/[...]/errors.xml";/*{{{*/
    /**
     * Resource path for error description.
     **/
    public static final String ERRORS = "/x/android/[...]/errors.xml";/*}}}*/
    // public static final String GUI    = "/x/android/[...]/gui.xml";/*{{{*/
    /**
     * Resource path for strings used in the `x.android.ui` package.
     **/
    public static final String GUI    = "/x/android/[...]/gui.xml";/*}}}*/
    // public static final String CALEND = "/x/android/[...]/calendar.xml";/*{{{*/
    /**
     * Resource path for calendar and dates strings.
     **/
    public static final String CALEND = "/x/android/[...]/calendar.xml";/*}}}*/
}
// vim:syntax=java.doxygen
