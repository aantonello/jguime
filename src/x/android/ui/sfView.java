/**
 * \file
 * Defines the sfView class.
 *
 * \author Alessandro Antonello <aantonello@paralaxe.com.br>
 * \date   novembro 19, 2013
 *
 * \copyright
 * This file is provided in hope that it will be useful to someone. It is
 * offered in public domain. You may use, modify or distribute it freely.
 *
 * The code is provided "AS IS". There is no warranty at all, of any kind. You
 * may change it if you like. Or just use it as it is.
 */
package x.android.ui;
/* #imports {{{ */
import java.util.*;

import android.app.Activity;
import android.view.View;

import x.android.utils.debug;
import x.android.nms.INHandler;
import x.android.nms.issuer;
/* }}} #imports */
/**
 * New implementation of AbstractView class.
 * This new implementation avoids the instantiation of the class building a
 * cache of reusable elements. For this metter the constructors are protected.
 * When one needs an instance of this class it should call one of its \c get()
 * method that will return an reusable object from the cache or a new one.
 * When you don't need the instance any more you must call the \c release()
 * method.
 *
 * Since version 2.4.156 you can also use \c autorelease() embedded with a
 * chain of calls. For example, you can use \c autorelease() this way:
 * @code
   return sfView.get(this).id(R.id.some_text_field).autorelease().text();
   @endcode
 * With this kind of code you will get an instance from the cache, get the
 * value that you want and, when the code goes out of scope, the instance will
 * be back in the cache again.
 *
 * Since version 2.5.157 you don't need to call \c release() nor \c
 * autorelease(). All sfView instances will be auto released automaticaly.
 *//* --------------------------------------------------------------------- */
public class sfView extends AbstractView<sfView> implements INHandler
{
    /** \name Constructors */ //@{
    // protected sfView(Activity activity);/*{{{*/
    /**
     * Builds an instance of this class using an Activity instance.
     * \param activity The Activity instance.
     **/
    protected sfView(Activity activity) {
        super(activity);
    }/*}}}*/
    // protected sfView(View view);/*{{{*/
    /**
     * Constructs a new instance of this class.
     * \param view A View object. It will be set as root and as current
     * operating View.
     **/
    protected sfView(View view) {
        super(view);
    }/*}}}*/
    //@}

    /** \name Static Operations */ //@{
    // public static sfView get(Activity activity);/*{{{*/
    /**
     * Gets an instance of this class.
     * @param activity Activity defining the View hierarchy.
     * @returns An instance of this class.
     **/
    public static sfView get(Activity activity)
    {
        sfView view = sfView.objectFromCache();

        if (view == null)
            view = new sfView(activity);
        else
            view.reset(activity, null);

        return view.self_release();
    }/*}}}*/
    // public static sfView get(View view);/*{{{*/
    /**
     * Gets an instance of this class.
     * @param view View used as root view for hierarchy.
     * @returns An instance of this class.
     **/
    public static sfView get(View view)
    {
        sfView sfv = sfView.objectFromCache();

        if (sfv == null)
            sfv = new sfView(view);
        else
            sfv.reset(null, view);

        return sfv.self_release();
    }/*}}}*/
    //@}

    /** \name Operations */ //@{
    // public final void release();/*{{{*/
    /**
     * This must be called when this instance isn't needed any more.
     * The function will return this instance into the reusable objects cache.
     * @deprecated Since version 2.4.157 this function does nothing. You can
     * safelly remove its call.
     **/
    public final void release()
    {
        debug.w("sfView::release() called! This call can be removed. It is no longer needed.\n");
    }/*}}}*/
    // public final sfView autorelease();/*{{{*/
    /**
     * Releases this instance when the code goes out of scope.
     * @returns \b this.
     * @deprecated Since version 2.4.157 this function does nothing. You can
     * safelly remove its call.
     **/
    public final sfView autorelease()
    {
        debug.w("sfView::autorelease() called! This call can be removed. It is no longer needed.\n");
        return this;
    }/*}}}*/
    //@}

    /** \name Implementation */ //@{
    // protected sfView self_release();/*{{{*/
    /**
     * Self releases this object instance.
     * This function replaces \c release() and \c autorelease(). Those
     * functions are only dummy declarations now. Any instance got from one of
     * those \c get() functions will be auto released as soon as the code goes
     * out of scope.
     **/
    protected sfView self_release()
    {
        issuer.post(this, IMSG.MSG_RELEASE, 0, 0L, this);
        return this;
    }/*}}}*/
    // protected static sfView objectFromCache();/*{{{*/
    /**
     * Retrieves an object from cache.
     * @return An instance of this class when success. If the cache is empty,
     * a \b null reference.
     **/
    protected static sfView objectFromCache()
    {
        if ((s_list == null) || s_list.isEmpty())
            return null;

        try { return s_list.remove(0); }
        catch (Exception ex) { /* This should not happen. */ }

        return null;
    }/*}}}*/
    //@}

    /** \name INHandler Implementation */ //@{
    // public boolean onMessage(int msgID, int nParam, long lParam, Object extra);/*{{{*/
    /**
     * Receives a message from the message system.
     * \param msgID Identifier of this message.
     * \param nParam First argument of this message. Message specific data.
     * \param lParam Second argument of this message. Message specific data.
     * \param extra Any extra data packed in an Object class.
     * \return Classes that handle messages usually return \b true, which
     * instructs the message system to stop propagation and release the
     * message object.
     **/
    public boolean onMessage(int msgID, int nParam, long lParam, Object extra)
    {
        if ((msgID == IMSG.MSG_RELEASE) && (extra == this))
        {
            if (s_list == null) s_list = new ArrayList<sfView>(1);

            /* Removing any references so the objects can be destroied. */
            m_activity = null;
            m_view     = null;
            m_root     = null;

            if (!s_list.contains(this))
                s_list.add(this);
        }
        return true;
    }/*}}}*/
    //@}

    /** \name Static Members */ //@{
    static ArrayList<sfView> s_list = null; /**< Cached instances. */
    //@}
}
// vim:syntax=java.doxygen
