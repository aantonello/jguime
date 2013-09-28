/**
 * \file
 * Defines the CAndroidSliderView class.
 *
 * \author Alessandro Antonello <aantonello@paralaxe.com.br>
 * \date   Agosto 13, 2013
 *
 * \copyright
 * This file is provided in hope that it will be useful to someone. It is
 * offered in public domain. You may use, modify or distribute it freely.
 *
 * The code is provided "AS IS". There is no warranty at all, of any kind. You
 * may change it if you like. Or just use it as it is.
 */
package x.android.ui;

import android.app.*;
import android.content.*;
import android.content.res.*;

import android.util.AttributeSet;

import android.view.*;
import android.widget.*;

import x.android.defs.*;
import x.android.utils.*;
import x.android.nms.*;

/**
 * A slider view that provides functionality resemble SlidingDrawer.
 *//* --------------------------------------------------------------------- */
public class CAndroidSliderView extends ViewGroup implements INHandler
{
    /** \name CONSTRUCTOR */ //@{
    // public CAndroidSliderView(Context context);/*{{{*/
    /**
     * Context constructor.
     * \param context The application context where to create this view.
     **/
    public CAndroidSliderView(Context context) {
        super(context);
    }/*}}}*/
    // public CAndroidSliderView(Context context, AttributeSet attrs);/*{{{*/
    /**
     * Attributed constructor.
     * This constructor is called when the View is created from the XML file.
     * \param context The application context.
     * \param attrs Attributes of the XML file defining the view.
     **/
    public CAndroidSliderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }/*}}}*/
    // public CAndroidSliderView(Context context, AttributeSet attrs, int style);/*{{{*/
    /**
     * Constructs a view from attributes and style.
     * Used when creating special view that has extended styles.
     * \param context The application context.
     * \param attrs Attributes of the XML file defining the view.
     * \param style The style to be set. Can be 0 if not needed. Can be the
     * identifier of a style in resource.
     **/
    public CAndroidSliderView(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);
    }/*}}}*/
    //@}

    /** \name Attributes */ //@{
    // public int orientation();/*{{{*/
    /**
     * Get the current orientation mode.
     * \return Two values can be returned:
     * \retval SLIDER::ORIENTATION::HORIZONTAL Horizontal orientation.
     * \retval SLIDER::ORIENTATION::VERTICAL Vertical orientation.
     **/
    public int orientation() {
        return m_mode;
    }/*}}}*/
    // public int orientation(int mode);/*{{{*/
    /**
     * Sets the orientation mode.
     * \param mode The possible values are:
     * - \c SLIDER::ORIENTATION::HORIZONTAL: Horizontal orientation.
     * - \c SLIDER::ORIENTATION::VERTICAL: Vertical orientation.
     * .
     * \return The previous orientation defined.
     **/
    public int orientation(int mode) {
        int prevMode = m_mode;
        m_mode = ((mode == SLIDER.ORIENTATION.HORIZONTAL) ? mode :
                  ((mode == SLIDER.ORIENTATION.VERTICAL) ? mode : m_mode));
        return prevMode;
    }/*}}}*/
    // public int bound();/*{{{*/
    /**
     * Gets the current bound mode.
     * \returns One of the following values:
     * \retval SLIDER::BOUND::LEFT Bound to the left edge.
     * \retval SLIDER::BOUND::TOP Bound to the top edge.
     * \retval SLIDER::BOUND::RIGHT Bound to the right edge.
     * \retval SLIDER::BOUND::BOTTOM Bound to the bottom edge.
     **/
    public int bound() {
        return m_bound;
    }/*}}}*/
    // public int bound(int mode);/*{{{*/
    /**
     * Changes the current bound mode.
     * \param mode The bound mode to set. Must be one of the following values:
     * - \b SLIDER::BOUND::LEFT: Bound to the left edge.
     * - \b SLIDER::BOUND::TOP: Bound to the top edge.
     * - \b SLIDER::BOUND::RIGHT: Bound to the right edge.
     * - \b SLIDER::BOUND::BOTTOM: Bound to the bottom edge.
     * .
     * \return The previous bound mode.
     **/
    public int bound(int mode) {
        int prevMode = m_bound;
        if ((mode >= SLIDER.BOUND.BOTTOM) && (mode <= SLIDER.BOUND.LEFT))
            m_bound = mode;
        return prevMode;
    }/*}}}*/
    //@}

    /** \name Data Members */ //@{
    private View m_handlerView;         /**< Handler for the user motions.  */
    private View m_contentView;         /**< Content of the panel.          */
    private int  m_mode;                /**< Orientation mode.              */
    private int  m_bound;               /**< Bound mode.                    */
    //@}
}
// vim:syntax=java.doxygen
