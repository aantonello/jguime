/**
 * \file
 * Defines the ALERT class.
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
package x.android.ui;

/**
 * Declares the constants used to show Alert dialogs.
 *//* --------------------------------------------------------------------- */
public final class ALERT
{
    // public static final class BUTTON;/*{{{*/
    /**
     * List of buttons constants.
     **/
    public static final class BUTTON
    {
        public static final int OK     = 1; /**< Represents the OK button.  */
        public static final int CANCEL = 2; /**< The Cancel button.         */
        public static final int YES    = 3; /**< The Yes button.            */
        public static final int NO     = 4; /**< The No button.             */
    }/*}}}*/
    // public static final class TYPE;/*{{{*/
    /**
     * List of types for alert dialog.
     **/
    public static final class TYPE
    {
        // public static final int INFO  = IMSG.MSG_INFO;/*{{{*/
        /**
         * Defines an Alert Dialog for simple information.
         * This kind of Alert will carry only an OK button with has the code
         * \c ALERT.BUTTON.OK.
         **/
        public static final int INFO  = IMSG.MSG_INFO;/*}}}*/
        // public static final int QUERY = IMSG.MSG_QUERY;/*{{{*/
        /**
         * Defines an Alert Dialog for a confirmation or question.
         * This type of Alert will have two buttons: \c ALERT.BUTTON.YES and
         * \c ALERT.BUTTON.NO.
         **/
        public static final int QUERY = IMSG.MSG_QUERY;/*}}}*/
    }/*}}}*/
}
// vim:syntax=java.doxygen
