/**
 * \file
 * Defines the view_t class.
 *
 * \author Alessandro Antonello <aantonello@paralaxe.com.br>
 * \date   Outubro 26, 2012
 *
 * \par Copyright
 * This file is provided in hope that it will be useful to someone. It is
 * offered in public domain. You may use, modify or distribute it freely.
 *
 * The code is provided "AS IS". There is no warranty at all, of any kind. You
 * may change it if you like. Or just use it as it is.
 */
package x.android.ui;

import android.app.Activity;
import android.view.View;

/**
 * \ingroup x_android_ui
 * Sole implementation of the AbstractView class.
 * Used as a common implementation.
 *//* --------------------------------------------------------------------- */
public class view_t extends AbstractView<view_t>
{
    /** \name CONSTRUCTOR */ //@{
    // public view_t(Activity activity);/*{{{*/
    /**
     * Builds an instance of this class using an Activity instance.
     * \param activity The Activity instance.
     **/
    public view_t(Activity activity) {
        super(activity);
    }/*}}}*/
    // public view_t(View view);/*{{{*/
    /**
     * Constructs a new instance of this class.
     * \param view A View object. It will be set as root and as current
     * operating View.
     **/
    public view_t(View view) {
        super(view);
    }/*}}}*/
    // public view_t(View root, View view);/*{{{*/
    /**
     * Constructs a new instance of this class.
     * \param root A View object used as base root of View chain.
     * \param view A View instance to be defined as current operating View.
     **/
    public view_t(View root, View view) {
        super(root, view);
    }/*}}}*/
    //@}

    /** \name PUBLIC STATIC FUNCTIONS */ //@{
    // public static view_t get(Activity activity);/*{{{*/
    /**
     * Builds an instance of this class using an Activity instance.
     * \param activity The Activity instance.
     * \returns view_t instance.
     **/
    public static view_t get(Activity activity) {
        return new view_t(activity);
    }/*}}}*/
    // public static view_t get(View view);/*{{{*/
    /**
     * Constructs a new instance of this class.
     * \param view A View object. It will be set as root and as current
     * operating View.
     * \returns view_t instance.
     **/
    public static view_t get(View view) {
        return new view_t(view);
    }/*}}}*/
    // public static view_t get(View root, View view);/*{{{*/
    /**
     * Constructs a new instance of this class.
     * \param root A View object used as base root of View chain.
     * \param view A View instance to be defined as current operating View.
     * \returns view_t instance.
     **/
    public static view_t get(View root, View view) {
        return new view_t(root, view);
    }/*}}}*/
    //@}
}
// vim:syntax=java.doxygen
