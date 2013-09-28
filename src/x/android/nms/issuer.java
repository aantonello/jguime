/**
 * \file
 * Defines the issuer class.
 *
 * \author Alessandro Antonello <aantonello@paralaxe.com.br>
 * \date   Novembro 28, 2012
 *
 * \par Copyright
 * This file is provided in hope that it will be useful to someone. It is
 * offered in public domain. You may use, modify or distribute it freely.
 *
 * The code is provided "AS IS". There is no warranty at all, of any kind. You
 * may change it if you like. Or just use it as it is.
 */
package x.android.nms;

import java.util.*;

import android.os.Handler;
import android.os.Looper;

/**
 * \ingroup x_android_nms
 * Main class of the notification message system.
 * This class is responsible for maintain the system of messages.
 *
 * The \c issuer class is a static class and the only usable methods are
 * declared in its public interface. It is intended to be used to communicate
 * notification messages only in the main thread stack. That is, others
 * threads can send messages to the main thread but there is no way to send
 * notification messages from the main thread to another.
 *//* --------------------------------------------------------------------- */
public class issuer
{
    /** \name CONSTRUCTOR */ //@{
    // protected issuer();/*{{{*/
    /**
     * Default constructor.
     **/
    protected issuer() {
        Looper looper = Looper.getMainLooper();

        this.handler = new Handler(looper);
        this.cache   = new cache_t();
        this.holder  = new cache_t();
        this.broadcastList = new subscribers();
    }/*}}}*/
    //@}

    /** \name PUBLIC STATIC FUNCTIONS */ //@{
    // public static void send(INHandler h, int id, int np, long lp, Object o);/*{{{*/
    /**
     * Sends a message to a target handler.
     * \param h The target handler instance.
     * \param id Message identifier.
     * \param np Integer parameter.
     * \param lp Long parameter.
     * \param o Object parameter.
     * The message is put in the front of the main message thread waiting to
     * be processed. Which will be as soon as possible.
     **/
    public static void send(INHandler h, int id, int np, long lp, Object o) {
        issuer.get().sendMsg(h, id, np, lp, o);
    }/*}}}*/
    // public static void post(INHandler h, int id, int np, long lp, Object o);/*{{{*/
    /**
     * Posts a message the a handler.
     * \param h The target handler instance.
     * \param id Message identifier.
     * \param np Integer parameter.
     * \param lp Long parameter.
     * \param o Object parameter.
     * \remarks The message is posted at the very end of the main message
     * queue and will be processed in order.
     **/
    public static void post(INHandler h, int id, int np, long lp, Object o) {
        issuer.get().postMsg(h, id, np, lp, o);
    }/*}}}*/
    // public static void post(INHandler h, int id, int np, long lp, Object o, long delay);/*{{{*/
    /**
     * Posts a message to a handler after the specified delay.
     * \param h The target handler instance.
     * \param id Message identifier.
     * \param np Integer parameter.
     * \param lp Long parameter.
     * \param o Object parameter.
     * \param delay Delay to post the message in milliseconds.
     * \remarks After the delay is passed, the message is posted at the end of
     * the main message queue and processed in order. Messages scheduled like
     * this can be unscheduled with the issuer#cancel() function.
     **/
    public static void post(INHandler h, int id, int np, long lp, Object o, long delay) {
        issuer.get().postMsg(h, id, np, lp, o, delay);
    }/*}}}*/
    // public static boolean cancel(INHandler h);/*{{{*/
    /**
     * Cancel scheduled messages.
     * \param h The target handler. All messages targeting this handler will
     * be canceled.
     * \return \b true if at least one message is canceled. Otherwise \b false.
     **/
    public static boolean cancel(INHandler h) {
        issuer.get().unschedule(h);
        return true;
    }/*}}}*/
    // public static boolean cancel(INHandler h, int id);/*{{{*/
    /**
     * Cancels an scheduled message.
     * \param h The target handler instance.
     * \param id Message identifier.
     * \returns \b true if the message could be unscheduled. Otherwise \b
     * false.
     **/
    public static boolean cancel(INHandler h, int id) {
        issuer.get().unschedule(h, id);
        return true;
    }/*}}}*/
    // public static boolean cancel(INHandler h, int id, int np);/*{{{*/
    /**
     * Cancels an scheduled message.
     * \param h The target handler instance.
     * \param id Message identifier.
     * \param np Integer parameter.
     * \returns \b true if the message could be unscheduled. Otherwise \b
     * false.
     **/
    public static boolean cancel(INHandler h, int id, int np) {
        issuer.get().unschedule(h, id, np);
        return true;
    }/*}}}*/
    // public static void release();/*{{{*/
    /**
     * Releases any resources hold by this implementation.
     **/
    public static void release() {
        if (__self == null) return;
        __self.free();
        __self = null;
    }/*}}}*/
    //@}

    /** \name BROADCAST NOTIFICATION FUNCTIONS */ //@{
    // public static void send(String name, int id, int np, long lp, Object o);/*{{{*/
    /**
     * Sends a broadcast notification to all its subscribers.
     * \param name Name of the broadcast notification.
     * \param id Message identifier.
     * \param np `int` parameter of the message.
     * \param lp `long` parameter of the message.
     * \param o Any extra data to be sent with this message.
     * \remarks The notification is sent immediatly, placed in front of any
     * other message in the application main loop.
     **/
    public static void send(String name, int id, int np, long lp, Object o) {
        issuer.get().sendNotify(name, id, np, lp, o);
    }/*}}}*/
    // public static void post(String name, int id, int np, long lp, Object o);/*{{{*/
    /**
     * Post a broadcast notification to all its subscribers.
     * \param name Name of the broadcast notification.
     * \param id Message identifier.
     * \param np `int` parameter of the message.
     * \param lp `long` parameter of the message.
     * \param o Any extra data to be sent with this message.
     * \remarks The notification is placed in the application main message
     * queue as the last element. It processes depend on the message queue
     * process.
     **/
    public static void post(String name, int id, int np, long lp, Object o) {
        issuer.get().postNotify(name, id, np, lp, o, 0L);
    }/*}}}*/
    // public static void post(String name, int id, int np, long lp, Object o, long delay);/*{{{*/
    /**
     * Posts a broadcast notification after the specified delay.
     * \param name Name of the broadcast notification.
     * \param id Message identifier.
     * \param np `int` parameter of the message.
     * \param lp `long` parameter of the message.
     * \param o Any extra data to be sent with this message.
     * \param delay Delay, in milliseconds, to wait until post the
     * notification message. After the specified amount o milliseconds
     * ellapses, the message is posted in the end of the main message queue.
     **/
    public static void post(String name, int id, int np, long lp, Object o, long delay) {
        issuer.get().postNotify(name, id, np, lp, o, delay);
    }/*}}}*/
    // public static void subscribe(String name, INHandler handler);/*{{{*/
    /**
     * Subscribes a handler to a broadcast notification.
     * \param name Name of the broadcast notification.
     * \param handler Instance of `INHandler` which will process these
     * notifications when they will be sent.
     **/
    public static void subscribe(String name, INHandler handler) {
        issuer.get().addClient(name, handler);
    }/*}}}*/
    // public static void unsubscribe(String name, INHandler handler);/*{{{*/
    /**
     * Cancels the subscription of an object to a broadcast notification.
     * \param name Name of the broadcast notification.
     * \param handler Instance of `INHandler` which is canceling the
     * subscription. All scheduled messages will be canceled either.
     **/
    public static void unsubscribe(String name, INHandler handler) {
        issuer.get().removeClient(name, handler);
    }/*}}}*/
    //@}

    /** \name LOCAL STATIC FUNCTIONS */ //@{
    // static synchronized issuer get();/*{{{*/
    /**
     * Gets the sole instance of this issuer object.
     **/
    static synchronized issuer get() {
        if (__self == null) {
            __self = new issuer();
        }
        return __self;
    }/*}}}*/
    //@}

    /** \name LOCAL OPERATIONS */ //@{
    // final void free();/*{{{*/
    /**
     * Releases any memory holded by this instance.
     **/
    final void free() {
        cache_t holded;
        Handler handler;

        synchronized (this) {
            holded  = this.holder;
            handler = this.handler;
        }

        /* First we search for any pending message to cancel they. */
        for (msg_t msg : holded)
            handler.removeCallbacks(msg);

        handler = null;
        holded  = null;
        synchronized (this) {
            this.cache   = null;
            this.holder  = null;
            this.handler = null;
        }
    }/*}}}*/
    // final void sendMsg(INHandler h, int id, int np, long lp, Object o);/*{{{*/
    /**
     * Sends a message to a target handler.
     * \param h The target handler instance.
     * \param id Message identifier.
     * \param np Integer parameter.
     * \param lp Long parameter.
     * \param o Object parameter.
     * The message is put in the front of the main message thread waiting to
     * be processed. Which will be as soon as possible.
     **/
    final void sendMsg(INHandler h, int id, int np, long lp, Object o) {
        Handler _handler;
        msg_t   msg;
        synchronized (this) {
            _handler = this.handler;
            msg = this.cache.get(h, id, np, o, lp, 0L);
        }
        _handler.postAtFrontOfQueue(msg);
    }/*}}}*/
    // final void postMsg(INHandler h, int id, int np, long lp, Object o);/*{{{*/
    /**
     * Posts a message the a handler.
     * \param h The target handler instance.
     * \param id Message identifier.
     * \param np Integer parameter.
     * \param lp Long parameter.
     * \param o Object parameter.
     * \remarks The message is posted at the very end of the main message
     * queue and will be processed in order.
     **/
    final void postMsg(INHandler h, int id, int np, long lp, Object o) {
        Handler handler;
        msg_t   msg;

        synchronized (this) {
            handler = this.handler;
            msg = this.cache.get(h, id, np, o, lp, 0L);
        }
        handler.post( msg );
    }/*}}}*/
    // final void postMsg(INHandler h, int id, int np, long lp, Object o, long delay);/*{{{*/
    /**
     * Posts a message to a handler after the specified delay.
     * \param h The target handler instance.
     * \param id Message identifier.
     * \param np Integer parameter.
     * \param lp Long parameter.
     * \param o Object parameter.
     * \param delay Delay to post the message in milliseconds.
     * \remarks After the delay is passed, the message is posted at the end of
     * the main message queue and processed in order. Messages scheduled like
     * this can be unscheduled with the issuer#cancel() function.
     **/
    final void postMsg(INHandler h, int id, int np, long lp, Object o, long delay) {
        Handler handler;
        cache_t holded;
        msg_t   msg;

        synchronized (this) {
            handler = this.handler;
            holded  = this.holder;
            msg     = this.cache.get(h, id, np, o, lp, delay);
        }
        handler.postDelayed(msg, delay);
        holded.push(msg);
    }/*}}}*/
    // final void unschedule(INHandler h);/*{{{*/
    /**
     * Cancel scheduled messages.
     * \param h The target handler. All messages targeting this handler will
     * be canceled.
     * \return \b true if at least one message is canceled. Otherwise \b false.
     **/
    final void unschedule(INHandler h) {
        cache_t hold;
        cache_t cach;
        Handler  hdr;
        msg_t    msg;

        synchronized (this) {
            hold = this.holder;
            cach = this.cache;
            hdr  = this.handler;
        }

        while ((msg = hold.remove(h)) != null) {
            hdr.removeCallbacks(msg);
            msg.data = null;
            cach.push(msg);
        }
    }/*}}}*/
    // final void unschedule(INHandler h, int id);/*{{{*/
    /**
     * Cancels an scheduled message.
     * \param h The target handler instance.
     * \param id Message identifier.
     * \returns \b true if the message could be unscheduled. Otherwise \b
     * false.
     **/
    final void unschedule(INHandler h, int id) {
        cache_t hold;
        cache_t cach;
        Handler  hdr;
        msg_t    msg;

        synchronized (this) {
            hold = this.holder;
            cach = this.cache;
            hdr  = this.handler;
        }

        while ((msg = hold.remove(h, id)) != null) {
            hdr.removeCallbacks(msg);
            msg.data = null;
            cach.push(msg);
        }
    }/*}}}*/
    // final void unschedule(INHandler h, int id, in np);/*{{{*/
    /**
     * Cancels an scheduled message.
     * \param h The target handler instance.
     * \param id Message identifier.
     * \param np Integer parameter.
     * \returns \b true if the message could be unscheduled. Otherwise \b
     * false.
     **/
    final void unschedule(INHandler h, int id, int np) {
        cache_t hold;
        cache_t cach;
        Handler  hdr;
        msg_t    msg;

        synchronized (this) {
            hold = this.holder;
            cach = this.cache;
            hdr  = this.handler;
        }

        while ((msg = hold.remove(h, id, np)) != null) {
            hdr.removeCallbacks(msg);
            msg.data = null;
            cach.push(msg);
        }
    }/*}}}*/
    //@}

    /** \name LOCAL BROADCAST NOTIFICATION FUNCTIONS */ //@{
    // final void addClient(String notificationName, INHandler client);/*{{{*/
    /**
     * Add a client to a broadcast notification.
     * \copydetails subscribers::add()
     **/
    final void addClient(String notificationName, INHandler client) {
        broadcastList.add(notificationName, client);
    }/*}}}*/
    // final void removeClient(String notificationName, INHandler client);/*{{{*/
    /**
     * Unsubscribe a client from a broadcast notification.
     * \copydetails subscribers::remove()
     **/
    final void removeClient(String notificationName, INHandler client) {
        broadcastList.remove(notificationName, client);
        unschedule(client);
    }/*}}}*/
    // final void sendNotify(String _name, int id, int np, long lp, Object o);/*{{{*/
    /**
     * Sends a broadcast notification to all subscribed clients.
     * \param _name Broadcast notification name.
     * \param id Message identifier.
     * \param np Integer parameter.
     * \param lp Long parameter.
     * \param o Object parameter.
     * \remarks The notification is sent to the client immediatly.
     **/
    final void sendNotify(String _name, int id, int np, long lp, Object o) {
        INHandler[] clients = broadcastList.get(_name);
        if (clients == null) return;

        int limit = clients.length;
        for (int i = 0; i < limit; i++) {
            sendMsg(clients[i], id, np, lp, o);
        }
    }/*}}}*/
    // final void postNotify(String _name, int id, int np, long lp, Object o, long t);/*{{{*/
    /**
     * Post a broadcast notification to all subscribed clients.
     * The message is posted immediatly, if *t* parameter is zero, or
     * scheduled to be posted after the *t* milliseconds delay.
     * \param _name Broadcast notification name.
     * \param id Message identifier.
     * \param np Integer parameter.
     * \param lp Long parameter.
     * \param o Object parameter.
     * \param t Delay to post the message.
     * \remarks The notification is sent to the client immediatly.
     **/
    final void postNotify(String _name, int id, int np, long lp, Object o, long t) {
        INHandler[] clients = broadcastList.get(_name);
        if (clients == null) return;

        int limit = clients.length;
        msg_t msg;                  /* We need one object per client. */
        for (int i = 0; i < limit; i++) {
            msg = this.cache.get(clients[i], id, np, o, lp, t);
            if (t == 0L)
                this.handler.post(msg);
            else {
                this.handler.postDelayed(msg, t);
                this.holder.push(msg);
            }
        }
    }/*}}}*/
    //@}

    /** \name LOCAL MEMBERS */ //@{
    Handler handler;            /**< Android messages handler.  */
    cache_t cache;              /**< Reusable msg_t objects.    */
    cache_t holder;             /**< Holding msg_t objects.     */
    subscribers broadcastList;  /**< List of broadcast notifications. */
    //@}

    /** \name STATIC MEMBERS */ //@{
    static issuer __self = null;        /**< This sole instance.            */
    //@}
}
// vim:syntax=java.doxygen
