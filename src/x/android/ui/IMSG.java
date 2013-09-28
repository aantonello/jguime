/**
 * \file
 * Defines the IMSG interface.
 *
 * \author Alessandro Antonello <aantonello@paralaxe.com.br>
 * \date   outubro 27, 2012
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
 * Interface that declares common message identifiers.
 *//* --------------------------------------------------------------------- */
public interface IMSG
{
    // public static final int MSG_FIRST  = 0x00000100;/*{{{*/
    /**
     * First message identifier.
     * Identifiers starting with this code are used in the system and cannot
     * be overriden.
     **/
    public static final int MSG_FIRST = 0x00000100;/*}}}*/
    // public static final int MSG_INFO   = 0x00000101;/*{{{*/
    /**
     * Sent by an Alert dialog which type is \c ALERT.TYPE.INFO.
     * This code is sent in the \c msgID argument of the notification handler
     * and the value \c ALERT.BUTTON.OK is passed in the \c nParam argument.
     **/
    public static final int MSG_INFO  = 0x00000101;/*}}}*/
    // public static final int MSG_QUERY  = 0x00000102;/*{{{*/
    /**
     * Sent by an Alert dialog whith type is \c ALERT.TYPE.QUERY.
     * This code is sent in the \c msgID argument of the notification handler.
     * The argument \c nParam will carrie the code of the pressed button. That
     * can be \c ALERT.BUTTON.YES or \c ALERT.BUTTON.NO.
     **/
    public static final int MSG_QUERY = 0x00000102;/*}}}*/
    // public static final int MSG_DELAY  = 0x00000103;/*{{{*/
    /**
     * Identificador utilizado nas mensagens agendadas.
     **/
    public static final int MSG_DELAY = 0x00000103;/*}}}*/
    // public static final int MSG_DIALOG = 0x00000104;/*{{{*/
    /**
     * Sent notifications from a dialog implementation.
     * This code is put on \c msgID parameter of \c INHandler::onMessage().
     * function. The \a nParam parameter has the dialog identifier and the \a
     * lParam param the action taken. See \c IMSG.DIALOG or a list of possible
     * actions. The \a extra parameter has the \c CAndroidProgressDialog
     * instance.
     **/
    public static final int MSG_DIALOG = 0x00000104;/*}}}*/
    // public static final int MSG_LAST   = 0x00000FFF;/*{{{*/
    /**
     * Last message identifier.
     * This is the last message identifier reserved to the system.
     **/
    public static final int MSG_LAST  = 0x00000FFF;/*}}}*/
    // public static final int MSG_APP    = 0x00001000;/*{{{*/
    /**
     * Starts the set of identifiers that can be used by applications.
     * Starting with this code, the application can declare others identifiers
     * to build its own set of private notifications.
     **/
    public static final int MSG_APP   = 0x00001000;/*}}}*/

    /** \name INNER INTERFACES */ //@{
    // public interface DIALOG;/*{{{*/
    /**
     * Possible actions from a \c MSG_DIALOG notification.
     * These are passed through \a lParam parameter.
     **/
    public interface DIALOG
    {
        public static final long STARTED = 1L;      /**< Dialog has been started. */
        public static final long DISMISS = 2L;      /**< Dialog has been dismissed. */
        public static final long CANCEL  = 3L;      /**< Dialog has been canceled. */
        public static final long OK      = 4L;      /**< Dialog has been applied. */
    }/*}}}*/
    //@}
}
// vim:syntax=java.doxygen
