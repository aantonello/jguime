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
/* }}} #imports */
/**
 * New implementation of AbstractView class.
 * This new implementation avoids the instantiation of the class building a
 * cache of reusable elements. For this metter the constructors are protected.
 * When one needs an instance of this class it should call one of its \c get()
 * method that will return an reusable object from the cache or a new one.
 * When you don't need the instance any more you must call the \c release()
 * method.
 *//* --------------------------------------------------------------------- */
public class sfView extends AbstractView<sfView>
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
    // protected sfView(View root, View view);/*{{{*/
    /**
     * Constructs a new instance of this class.
     * \param root A View object used as base root of View chain.
     * \param view A View instance to be defined as current operating View.
     **/
    protected sfView(View root, View view) {
        super(root, view);
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
        sfView view = null;

        if ((s_list != null) && !s_list.isEmpty())
        {
            try {
                view = s_list.remove(0);
                return view.reset(activity, null);
            }
            catch (Exception ex) {
                /* This should not happen. */
            }
        }
        return new sfView(activity);
    }/*}}}*/
    // public static sfView get(View view);/*{{{*/
    /**
     * Gets an instance of this class.
     * @param view View used as root view for hierarchy.
     * @returns An instance of this class.
     **/
    public static sfView get(View view)
    {
        sfView sfv = null;

        if ((s_list != null) && !s_list.isEmpty())
        {
            try {
                sfv = s_list.remove(0);
                return sfv.reset(null, view);
            }
            catch (Exception ex) {
                /* This should not happen. */
            }
        }
        return new sfView(view);
    }/*}}}*/
    // public static sfView get(View root, View view);/*{{{*/
    /**
     * Gets an instance of this class.
     * @param root View used as root view for hierarchy.
     * @param view View used as initial operating view.
     * @returns An instance of this class.
     **/
    public static sfView get(View root, View view)
    {
        sfView sfv = null;

        if ((s_list != null) && !s_list.isEmpty())
        {
            try {
                sfv = s_list.remove(0);
                return sfv.reset(null, root, view);
            }
            catch (Exception ex) {
                /* This should not happen. */
            }
        }
        return new sfView(root, view);
    }/*}}}*/
    //@}

    /** \name Operations */ //@{
    // public void release();/*{{{*/
    /**
     * This must be called when this instance isn't needed any more.
     * The function will return this instance into the reusable objects cache.
     **/
    public void release()
    {
        if (s_list == null) s_list = new ArrayList<sfView>(1);

        /* Removing any references so the objects can be destroied. */
        m_activity = null;
        m_view     = null;
        m_root     = null;

        if (!s_list.contains(this)) {
            s_list.add(this);
        }
    }/*}}}*/
    //@}

    /** \name Static Members */ //@{
    static ArrayList<sfView> s_list = null; /**< Cached instances. */
    //@}
}
// vim:syntax=java.doxygen
