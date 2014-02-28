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
    // public static final int MSG_RELEASE= 0x00000105;/*{{{*/
    /**
     * Special message used in \c sfView class.
     * Please, do not use it. Do not send it to any instance of \c sfView
     * class.
     **/
    public static final int MSG_RELEASE = 0x00000105;/*}}}*/
    // public static final int MSG_PAGER  = 0x00000106;/*{{{*/
    /**
     * Indentify all messages where the source is \c CAndroidPagerView.
     * In these messages the \e nParam argument carries the event code as of
     * defined in the \c IMSG.PAGER class. The \e lParam argument depends on
     * the code passed through \e nParam. The \e extra argument always has the
     * \c CAndroidPagerView instance.
     *
     * The \c CAndroidPagerView don't has a way to set one single handler for
     * its notifications. The interested ones must subscribe to the \c
     * IMSG.BROADCAST.PAGER broadcast notifications using \c
     * issuer::subscribe().
     **/
    public static final int MSG_PAGER  = 0x00000106;/*}}}*/
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
    // public interface PAGER;/*{{{*/
    /**
     * Lists the identifiers for the \c IMSG.MSG_PAGER notifications.
     * All these identifiers are sent in the \e nParam parameter of the
     * notification.
     **/
    public interface PAGER
    {
        // public static final int STARTSCROLL = 1;/*{{{*/
        /**
         * The user was started scrolling to the next or previous page.
         * When this message is sent the current page is not changed yet. So
         * the information on \c CAndroidPagerView::getCurrentViewIndex() or
         * \c CAndroidPagerView::getCurrentView() will return the View showed
         * just before the user starts scrolling the View. Also, this
         * notification is not sent when the current page changes by a call to
         * \c CAndroidPagerView::setCurrentView(int,boolean).
         *
         * The \e lParam parameter is not used. Its value is \b 0L.
         *
         * The \e extra parameter has the instance of \c CAndroidPagerView,
         * source of the notification.
         **/
        public static final int STARTSCROLL = 1;/*}}}*/
        // public static final int ENDSCROLL   = 2;/*{{{*/
        /**
         * Sent when the scroll ends.
         * When this notification is sent the current page was already
         * changed. Calling \c CAndroidPagerView::getCurrentView() or \c
         * CAndroidPagerView::getCurrentViewIndex() will return the updated
         * result. Thus, this does not indicate the end of the animation. The
         * pager may be changing its scroll position in the case the user
         * passed the beginning or end of the internal Views.
         *
         * This notification is not sent when the current View changes by a
         * call to \c CAndroidPagerView::setCurrentView(int,boolean). Even if
         * the \e animated parameter of that call was \b true.
         *
         * The \e lParam parameter is not used. Its value is \b 0L.
         *
         * The \e extra parameter has the instance of \c CAndroidPagerView,
         * source of the notification.
         **/
        public static final int ENDSCROLL   = 2;/*}}}*/
        // public static final int PAGECHANGED = 3;/*{{{*/
        /**
         * Sent when the current page View changes.
         * This notification is always sent. Just after the user ends
         * scrolling the View (after the \c IMSG.PAGER.ENDSCROLL notification)
         * and when the current page was changed by a call to \c
         * CAndroidPagerView::setCurrentView(int,boolean).
         *
         * The \e lParam parameter will carrie the current View index.
         *
         * The \e extra parameter has the instance of \c CAndroidPagerView,
         * source of the notification.
         **/
        public static final int PAGECHANGED = 3;/*}}}*/
        // public static final int COUNTCHANGED= 4;/*{{{*/
        /**
         * Sent when the number of View pages changed.
         * This notification is sent when new Views are added or Views are
         * removed from \c CAndroidPagerView pager.
         *
         * The \e lParam parameter carries the updated number of internal
         * Views.
         *
         * The \e extra parameter has the instance of \c CAndroidPagerView,
         * source of the notification.
         **/
        public static final int COUNTCHANGED = 4;/*}}}*/
    }/*}}}*/
    // public interface BROADCAST;/*{{{*/
    /**
     * Lists the broadcasts notifications identifiers.
     **/
    public interface BROADCAST
    {
        // public static String PAGER = "IMSG.BROADCAST.PAGER";/*{{{*/
        /**
         * Broadcast notifications sent by a \c CAndroidPagerView object.
         * All messages sent under this identifier are identified by the ID \c
         * IMSG.MSG_PAGER.
         **/
        public static String PAGER = "IMSG.BROADCAST.PAGER";/*}}}*/
    }/*}}}*/
    //@}
}
// vim:syntax=java.doxygen
