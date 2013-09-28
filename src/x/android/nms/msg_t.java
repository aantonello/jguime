/**
 * \file
 * Defines the msg_t class.
 *
 * \author Alessandro Antonello <aantonello@paralaxe.com.br>
 * \date   Janeiro 12, 2013
 *
 * \par Copyright
 * This file is provided in hope that it will be useful to someone. It is
 * offered in public domain. You may use, modify or distribute it freely.
 *
 * The code is provided "AS IS". There is no warranty at all, of any kind. You
 * may change it if you like. Or just use it as it is.
 */
package x.android.nms;

/**
 * Class that represents a notification message.
 * Every message is added to the main thread stack. That is, each instance of
 * this class is added to the main application thread loop and processed
 * according to the system time. For that to be accomplished, this classe
 * implements the \c Runnable interface which method \c run() is called when
 * is time to process the message. The messages is passed direct to its
 * target.
 *
 * The target of a notification message is a class that implements the \c
 * INHandler interface. Only the data of the message is passed to this
 * interface. Not the instance of this class. This class is only accessible
 * from inside this package.
 *//* --------------------------------------------------------------------- */
class msg_t implements Runnable
{
    /** \name CONSTRUCTOR */ //@{
    // public msg_t();/*{{{*/
    /**
     * Default constructor.
     **/
    public msg_t() {
        this.target = null;
        this.msgID  = 0;
        this.nParam = 0;
        this.data   = null;
        this.lParam = 0L;
        this.delay  = 0L;
    }/*}}}*/
    // public msg_t(INHandler h, int id, int np, Object o, long lp, long t);/*{{{*/
    /**
     * Parametrized constructor.
     * Constructs the class setting the common parameters.
     * \see #set()
     **/
    public msg_t(INHandler h, int id, int np, Object o, long lp, long t) {
        this.set(h, id, np, o, lp, t);
    }/*}}}*/
    //@}

    /** \name OPERATIONS */ //@{
    // final void set(INHandler h, int id, int np, Object o, long lp, long t);/*{{{*/
    /**
     * Sets the properties of this object.
     * \param h The target handler.
     * \param id Message identifier.
     * \param np Integer parameter.
     * \param o Object parameter.
     * \param lp Long parameter.
     * \param t Delay, if any.
     **/
    final void set(INHandler h, int id, int np, Object o, long lp, long t) {
        this.target = h;
        this.msgID  = id;
        this.nParam = np;
        this.data   = o;
        this.lParam = lp;
        this.delay  = t;
    }/*}}}*/
    //@}

    /** \name Runnable IMPLEMENTATION */ //@{
    // public void run();/*{{{*/
    /**
     * Called when the system process the main thread loop stack.
     * Calls the target handler function to process the message data.
     **/
    public void run() {
        INHandler handler = this.target;
        issuer    iss     = issuer.get();

        /* First we need to remove this instance from the holding cache to
         * avoid that the message is cancelled while we are processing it.
         */
        iss.holder.remove(this);
        handler.onMessage(this.msgID, this.nParam, this.lParam, this.data);
        this.data = null;       /* Release any resources. */

        /* Put back this instance in the available cache. */
        iss.cache.push(this);
    }/*}}}*/
    //@}

    /** \name FIELDS */ //@{
    INHandler target;           /**< Target handler of the message.     */
    int       msgID;            /**< Main message identifier.           */
    int       nParam;           /**< Integer parameter.                 */
    Object    data;             /**< Object parameter (can be anything).*/
    long      lParam;           /**< Long parameter.                    */
    long      delay;            /**< Message delay, if any.             */
    //@}
}
// vim:syntax=java.doxygen
