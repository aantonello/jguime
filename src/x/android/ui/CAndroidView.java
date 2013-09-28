/**
 * \file
 * Defines the CAndroidView class.
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

import android.util.AttributeSet;
import android.app.Application;
import android.content.Context;
import android.view.View;
import android.view.ViewParent;

import x.android.nms.*;

/**
 * Base implementation of a Android View.
 * The job of this class is to serve as basis to construct views of an
 * application.
 *//* --------------------------------------------------------------------- */
public class CAndroidView extends View implements IAndroidView, IMSG
{
    /** \name CONSTRUCTOR */ //@{
    // public CAndroidView();/*{{{*/
    /**
     * Default constructor.
     * This constructor will get the Context object, needed for the base View
     * construct querying the static application through \c
     * CAndroidApp#currentApp().
     **/
    public CAndroidView() {
        super((Context)CAndroidApp.currentApp());
    }/*}}}*/
    // public CAndroidView(Context context);/*{{{*/
    /**
     * Context constructor.
     * \param context The application context where to create this view.
     **/
    public CAndroidView(Context context) {
        super(context);
    }/*}}}*/
    // public CAndroidView(Context context, AttributeSet attrs);/*{{{*/
    /**
     * Attributed constructor.
     * This constructor is called when the View is created from the XML file.
     * \param context The application context.
     * \param attrs Attributes of the XML file defining the view.
     **/
    public CAndroidView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }/*}}}*/
    // public CAndroidView(Context context, AttributeSet attrs, int style);/*{{{*/
    /**
     * Constructs a view from attributes and style.
     * Used when creating special view that has extended styles.
     * \param context The application context.
     * \param attrs Attributes of the XML file defining the view.
     * \param style The style to be set. Can be 0 if not needed. Can be the
     * identifier of a style in resource.
     **/
    public CAndroidView(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);
    }/*}}}*/
    //@}

    /** \name IAndroidView IMPLEMENTATION */ //@{
    // public void alert(String msg, int notifyID);/*{{{*/
    /**
     * \copydoc IAndroidView#alert(String,int)
     **/
    public void alert(String msg, int notifyID) {
        CAndroidApp.alert(getContext(), msg, notifyID, this);
    }/*}}}*/
    // public void alert(int msgID, int notifyID);/*{{{*/
    /**
     * \copydoc IAndroidView#alert(int,int)
     **/
    public void alert(int msgID, int notifyID) {
        this.alert(CAndroidApp.loadString(msgID), notifyID);
    }/*}}}*/
    // public void query(String msg, int notifyID);/*{{{*/
    /**
     * \copydoc IAndroidView#query(String,int)
     **/
    public void query(String msg, int notifyID) {
        CAndroidApp.query(getContext(), msg, notifyID, this);
    }/*}}}*/
    // public void query(int msgID, int notifyID);/*{{{*/
    /**
     * Shows a query message to the user.
     * \param msgID Identifier of a string in the application resource.
     * \param notifyID Identifier to be used to send a notification when the
     * dialog is dismissed. If this parameter is zero the value
     * ALERT.TYPE#QUERY will be used.
     **/
    public void query(int msgID, int notifyID) {
        this.query(CAndroidApp.loadString(msgID), notifyID);
    }/*}}}*/
    //@}

    /** \name LAYOUT HELPER */ //@{
    // public CRect getMeasuredRect(CRect rect);/*{{{*/
    /**
     * Gets the measured values for laying out this View.
     * \param rect A \c CRect instance. May be \b null.
     * \return The function returns the \a rect parameter, when it was passed,
     * filled with the current measured width and measured height. The \e left
     * and \e top coordinates of the rectangle are also filled with the
     * current values of the View. If the parameter \a rect is not passed in
     * the function creates a new one and returns it.
     **/
    public CRect getMeasuredRect(CRect rect) {
        CRect rc = ((rect != null) ? rect : new CRect());
        rc.box(this.getLeft(), this.getTop(), this.getMeasuredWidth(), this.getMeasuredHeight());

        return rc;
    }/*}}}*/
    // public CRect getViewRect(CRect rect);/*{{{*/
    /**
     * Get the current View rectangle.
     * \param rect A \c CRect instance. May be \b null.
     * \return The function returns the \a rect parameter if it is passed or a
     * newly created \c CRect object otherwise. The returned rectangle will
     * have the current position and size of this View.
     **/
    public CRect getViewRect(CRect rect) {
        CRect rc = ((rect == null) ? new CRect() : rect);
        rc.box(this.getLeft(), this.getTop(), this.getWidth(), this.getHeight());

        return rc;
    }/*}}}*/
    // public CRect getPaddingRect(CRect rect);/*{{{*/
    /**
     * Gets the current View padding.
     * \param rect A \c CRect instance. May be \b null.
     * \return The function returns the \a rect parameter if it is passed or a
     * newly created \c CRect object otherwise. The returned rectangle will
     * have the current padding values on its members.
     **/
    public CRect getPaddingRect(CRect rect) {
        CRect rc = ((rect == null) ? new CRect() : rect);
        rc.set(this.getPaddingLeft(), this.getPaddingTop(), this.getPaddingRight(), this.getPaddingBottom());

        return rc;
    }/*}}}*/
    //@}

    /** \name INHandler IMPLEMENTATION */ //@{
    // public boolean onMessage(int msgID, int nParam, long lParam, Object extra);/*{{{*/
    /**
     * Handle messages sent to this View.
     * \copydetails INHandler#onMessage()
     **/
    public boolean onMessage(int msgID, int nParam, long lParam, Object extra) {
        return true;
    }/*}}}*/
    //@}
}
// vim:syntax=java.doxygen
