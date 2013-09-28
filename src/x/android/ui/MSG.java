/**
 * \file MSG.java
 * Defines the MSG class.
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
 * List of default messages identifiers.
 * These constants are used with common messages and notifies used with this
 * library.
 *//* --------------------------------------------------------------------- */
public final class MSG
{
    /**
     * \name MESSAGES
     * List of messages constants.
     * They are heavly used inside this library.
     *///@{
    public static final int ALERT     = 0x00001001; /**< Alert messages. \sa MSG.DIALOG */
    public static final int TIMER     = 0x00001002; /**< Timer (scheduled) messages.   */
    public static final int COMMAND   = 0x00001003; /**< General purpose command messages. */
    public static final int CLICK     = 0x00001004; /**< OnClickListener calls. */
    public static final int FOCUS     = 0x00001005; /**< Focus changing.    */
    public static final int ITEMCLICK = 0x00001006; /**< Item clicked.      */
    public static final int SELECTED  = 0x00001007; /**< Item selected.     */
    public static final int CHECKED   = 0x00001008; /**< Item checked/unchecked. */
    public static final int SLIDE     = 0x00001009; /**< SeekBar tracking.  */
    public static final int QUIT      = 0x00001FFF; /**< A general purpose quit message. */
    public static final int APP       = 0x00010000; /**< Apps start their messages here. */
    ///@} MESSAGES

    // public static final class DIALOG { }/*{{{*/
    /**
     * Sent to a handler when an alert dialog is closed.
     * These values are passed in the \c Message.what member field. The \c
     * Message.arg2 has the identifier of the button chosen by the user. The
     * \c Message.arg1 member will have the dialog identifier.
     **/
    public static final class DIALOG
    {
        // public static final int INFO     = 0x00000100;/*{{{*/
        /**
         * Sent by an Alert dialog of type \c ALERT.TYPE.INFO.
         * The \c Message.arg2 member will have the value \c ALERT.BUTTON.OK.
         **/
        public static final int INFO     = 0x00000100;/*}}}*/
        // public static final int CONFIRM  = 0x00000101;/*{{{*/
        /**
         * Sent by an Alert of type \c ALERT.TYPE.CONFIRM.
         * The \c Message.arg2 member can have the values \c ALERT.BUTTON.OK
         * or \c ALERT.BUTTON.CANCEL.
         **/
        public static final int CONFIRM  = 0x00000101;/*}}}*/
        // public static final int QUESTION = 0x00000102;/*{{{*/
        /**
         * Sent by an Alert of type \c ALERT.TYPE.QUESTION.
         * The \c Message.arg2 member can have the values \c ALERT.BUTTON.YES
         * or \c ALERT.BUTTON.NO.
         **/
        public static final int QUESTION = 0x00000102;/*}}}*/
    }/*}}}*/
    // public static final class STATE { }/*{{{*/
    /**
     * Several values used to inform changes in view or widget state.
     * These are all passed in the \c Message.arg2 field member.
     **/
    public static final class STATE
    {
        // public static final int INACTIVE  = 0x00000200;/*{{{*/
        /**
         * The underlined View has lost its focus.
         * This value is passed through the \c Message.arg2 member of the \c
         * MSG.FOCUS message.
         **/
        public static final int INACTIVE  = 0x00000200;/*}}}*/
        // public static final int ACTIVE    = 0x00000201;/*{{{*/
        /**
         * The View has received focus.
         * This value is passed through the \c Message.arg2 member of the \c
         * MSG.FOCUS message.
         **/
        public static final int ACTIVE    = 0x00000201;/*}}}*/
        // public static final int CHECKED   = 0x00000202;/*{{{*/
        /**
         * The View has been checked.
         * This value is passed through the \c Message.arg2 member of the \c
         * MSG.CHECKED message.
         **/
        public static final int CHECKED   = 0x00000202;/*}}}*/
        // public static final int UNCHECKED = 0x00000203;/*{{{*/
        /**
         * The view has been unchecked.
         * This value is passed through the \c Message.arg2 member of the \c
         * MSG.CHECKED message.
         **/
        public static final int UNCHECKED = 0x00000203;/*}}}*/
        // public static final int START     = 0x00000204;/*{{{*/
        /**
         * An SeekBar started to be tracked.
         * This value is passed through the \c Message.arg2 member of the \c
         * MSG.SLIDE message.
         **/
        public static final int START     = 0x00000204;/*}}}*/
        // public static final int STOP      = 0x00000205;/*{{{*/
        /**
         * A SeekBar stoped to be tracked.
         * This value is passed through the \c Message.arg2 member of the \c
         * MSG.SLIDE message.
         **/
        public static final int STOP      = 0x00000205;/*}}}*/
    }/*}}}*/
}
// vim:syntax=java.doxygen
