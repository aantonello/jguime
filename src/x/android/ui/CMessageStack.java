/**
 * \file
 * Defines the CMessageStack class.
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

import java.util.Stack;

import android.os.Handler;
import android.os.Message;

import x.android.utils.time_t;

/**
 * Implements the stack of the notification message system.
 * This class and its companion \c Msg, implements the simple message
 * system.
 *//* --------------------------------------------------------------------- */
public class CMessageStack implements Handler.Callback
{
    /** \name CONSTRUCTOR */ //@{
    // public CMessageStack();/*{{{*/
    /**
     * Default constructor.
     **/
    public CMessageStack() {
        super();
        m_stack   = new Stack<CMessageStack.Msg>();
        m_handler = new Handler((Handler.Callback)this);
    }/*}}}*/
    //@}

    /** \name PUBLIC METHODS */ //@{
    // public void send(INotifyHandler handler, int msgID, int nParam, long lParam, Object extra);/*{{{*/
    /**
     * Sends a message to the specified target object.
     * The message is sent directly to the target handler.
     * \param handler The target handler of the message.
     * \param msgID The message identifier.
     * \param nParam Integer parameter.
     * \param lParam Long parameter.
     * \param extra Extra parameter.
     * \remarks This function must not be used to send messages between
     * threads becouse the handler implementaion will be called within the
     * caller thread. Notifications between threads must use \c post()
     * instead.
     **/
    public void send(INotifyHandler handler, int msgID, int nParam, long lParam, Object extra) {
        if (handler == null) return;
        handler.onMessage(msgID, nParam, lParam, extra);
    }/*}}}*/
    // public void post(INotifyHandler handler, int msgID, int nParam, long lParam, Object extra);/*{{{*/
    /**
     * Posts a message to the specified target handler.
     * The message is posted at the end of the thread message queue.
     * \param handler The target handler of the message.
     * \param msgID The message identifier.
     * \param nParam Integer parameter.
     * \param lParam Long parameter.
     * \param extra Extra parameter.
     **/
    public void post(INotifyHandler handler, int msgID, int nParam, long lParam, Object extra) {
        Msg msg = this.any();
        msg.set(handler, msgID, nParam, lParam, extra);
        Message _msg = m_handler.obtainMessage(IMSG.MSG_FIRST, msg);
        m_handler.sendMessage(_msg);
    }/*}}}*/
    // public void post(INotifyHandler handler, int msgID, int nParam, long lParam, Object extra, long delay);/*{{{*/
    /**
     * Post a message to the specified handler after a delay.
     * The message is posted at the end of the thread message queue after the
     * specified delay.
     * \param handler The target handler of the message.
     * \param msgID The message identifier.
     * \param nParam Integer parameter.
     * \param lParam Long parameter.
     * \param extra Extra parameter.
     * \param delay The delay period to wait before send the message. In
     * milliseconds.
     **/
    public void post(INotifyHandler handler, int msgID, int nParam, long lParam, Object extra, long delay) {
        Msg msg = this.any();
        msg.set(handler, msgID, nParam, lParam, extra);
        Message _msg = m_handler.obtainMessage(IMSG.MSG_FIRST, msg);
        m_handler.sendMessageDelayed(_msg, delay);
    }/*}}}*/
    //@}

    /** \name Handler.Callback IMPLEMENTATION */ //@{
    // public boolean handleMessage(Message msg);/*{{{*/
    /**
     * Handles the message retrieved from the queue.
     * \param msg The Message object.
     * \returns \b true if the message is processed. This implementation
     * always returns \b true.
     **/
    public boolean handleMessage(Message msg) {
        Msg _msg = (Msg)msg.obj;
        INotifyHandler handler = (INotifyHandler)_msg.target;

        if (handler != null) {
            handler.onMessage(_msg.msgID, _msg.nParam, _msg.lParam, _msg.extra);
        }
        _msg.target = null;
        _msg.extra  = null;
        m_stack.push(_msg);

        return true;
    }/*}}}*/
    //@}

    /** \name PROTECTED METHODS */ //@{
    // protected CMessageStack.Msg any();/*{{{*/
    /**
     * Recovers a Msg object from the stack.
     * If the stack is empty a new object is created.
     **/
    protected CMessageStack.Msg any() {
        if (m_stack.empty()) {
            return this.new Msg();
        }
        return m_stack.pop();
    }/*}}}*/
    //@}

    /** \name PROTECTED FIELDS */ //@{
    protected Handler m_handler;        /**< Handles the Android messages.  */
    protected Stack<CMessageStack.Msg> m_stack;     /**< Msg stack.         */
    //@}

    /** \name Msg CLASS */ //@{
    // protected class Msg/*{{{*/
    /**
     * This is a message whith holds the data of the notification.
     **/
    protected class Msg
    {
        /** \name CONSTRUCTOR */ //@{
        // public Msg();/*{{{*/
        /**
         * Constructs an empty Msg object.
         **/
        public Msg() {
            this.msgID = 0; this.nParam = 0; this.lParam = 0;
            this.extra = null; this.target = null;
            this.time = time_t.now();
        }/*}}}*/
        // public Msg(Object target, int msgID, int nParam, long lParam, Object extra);/*{{{*/
        /**
         * Builds a Msg object defining some of its properties.
         * \param target Required. The target object of the message.
         * \param msgID Required. The message identifier.
         * \param nParam Optional. An integer parameter.
         * \param lParam Optional. A long parameter.
         * \param extra Optional. An object with extra data.
         **/
        public Msg(Object target, int msgID, int nParam, long lParam, Object extra) {
            this.target = target;
            this.msgID  = msgID;
            this.nParam = nParam;
            this.lParam = lParam;
            this.extra  = extra;
            this.time   = time_t.now();
        }/*}}}*/
        //@}

        /** \name PUBLIC METHODS */ //@{
        // public set(Object handler, int msgID, int nParam, long lParam, Object extra);/*{{{*/
        /**
         * Sets the properties of this Msg object.
         * \param handler The target object.
         * \param msgID The message identifier.
         * \param nParam Integer parameter.
         * \param lParam Long parameter.
         * \param extra Extra parameter.
         **/
        public void set(Object handler, int msgID, int nParam, long lParam, Object extra) {
            this.target = handler;
            this.msgID  = msgID;
            this.nParam = nParam;
            this.lParam = lParam;
            this.extra  = extra;
        }/*}}}*/
        //@}

        /** \name PUBLIC FIELDS */ //@{
        public int    msgID;                /**< The message identifier.    */
        public int    nParam;               /**< The first argument.        */
        public long   lParam;               /**< The second argument.       */
        public long   time;                 /**< When the msg was post.     */
        public Object extra;                /**< Msg extra data.            */
        public Object target;               /**< The target handler.        */
        //@}
    }/*}}}*/
    //@}
}
// vim:syntax=java.doxygen
