/**
 * \file
 * Defines the IAndroidView class.
 *
 * \author Alessandro Antonello <aantonello@paralaxe.com.br>
 * \date   outubro 24, 2012
 *
 * \par Copyright
 * This file is provided in hope that it will be useful to someone. It is
 * offered in public domain. You may use, modify or distribute it freely.
 *
 * The code is provided "AS IS". There is no warranty at all, of any kind. You
 * may change it if you like. Or just use it as it is.
 */
package x.android.ui;

import x.android.nms.INHandler;

/**
 * \ingroup x_android_ui
 * Declare function implemented by all class that extends a view.
 *//* --------------------------------------------------------------------- */
public interface IAndroidView extends INHandler
{
// ALERT FUNCTIONS
    // public void alert(String msg, int notifyID);/*{{{*/
    /**
     * Shows an alert message to the user.
     * \param msg String with the message.
     * \param notifyID Identifier to be used to send a notification when the
     * dialog is dismissed. This value will be received in the \c nParam
     * parameter of a message with \c msgID equals to \c IMSG.MSG_INFO.
     **/
    public void alert(String msg, int notifyID);/*}}}*/
    // public void alert(int msgID, int notifyID);/*{{{*/
    /**
     * Shows an alert message to the user.
     * \param msgID Identifier of a string in the application resource.
     * \param notifyID Identifier to be used to send a notification when the
     * dialog is dismissed. This value will be received in the \c nParam
     * parameter of a message with \c msgID equals to \c IMSG.MSG_INFO.
     **/
    public void alert(int msgID, int notifyID);/*}}}*/
    // public void query(String msg, int notifyID);/*{{{*/
    /**
     * Shows a query message to the user.
     * \param msg String with the message.
     * \param notifyID Identifier to be used to send a notification when the
     * dialog is dismissed. This value will be received in the \c nParam
     * parameter of a message with \c msgID equals to \c IMSG.MSG_QUERY, and
     * the \c lParam parameter will have \c ALERT.BUTTON.YES or \c
     * ALERT.BUTTON.NO.
     **/
    public void query(String msg, int notifyID);/*}}}*/
    // public void query(int msgID, int notifyID);/*{{{*/
    /**
     * Shows a query message to the user.
     * \param msgID Identifier of a string in the application resource.
     * \param notifyID Identifier to be used to send a notification when the
     * dialog is dismissed. If this parameter is zero the value
     * ALERT.TYPE#QUERY will be used.
     **/
    public void query(int msgID, int notifyID);/*}}}*/
}
// vim:syntax=java.doxygen
