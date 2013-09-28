/**
 * \file
 * Defines the SLIDER class.
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

/**
 * Provides constant values used in CAndroidSliderView configuration.
 * This is an static class. That is, all its members are `static final`. This
 * class should not be instanteated.
 *//* --------------------------------------------------------------------- */
public final class SLIDER
{
    // public static final class ORIENTATION;/*{{{*/
    /**
     * Values for the \ref CAndroidSliderView::orientation() attribute.
     **/
    public static final class ORIENTATION
    {
        // public static final int HORIZONTAL = -1;/*{{{*/
        /**
         * Horizontal orientation.
         * The group will slide from left to right or vice-versa.
         **/
        public static final int HORIZONTAL = -1;/*}}}*/
        // public static final int VERTICAL   = 1;/*{{{*/
        /**
         * Vertical orientation.
         * The group will slide from top to bottom or vice-versa.
         **/
        public static final int VERTICAL   = 1;/*}}}*/
    }/*}}}*/
    // public static final class BOUND;/*{{{*/
    /**
     * Constant values for the \c CAndroidSliderView::bound() attribute.
     **/
    public static final class BOUND
    {
        // public static final int LEFT   = 3;/*{{{*/
        /**
         * The slider will be bound to the left edge of its parent when
         * closed.
         * This sets the orientation to SLIDER::ORIENTATION::HORIZONTAL and
         * direction of opening from left to right.
         **/
        public static final int LEFT   = 3;/*}}}*/
        // public static final int TOP    = 2;/*{{{*/
        /**
         * The slider will be bound to the top edge of its parent when closed.
         * This sets the orientation as SLIDER::ORIENTATION::VERTICAL and
         * direction of opening from top to bottom.
         **/
        public static final int TOP    = 2;/*}}}*/
        // public static final int RIGHT  = 1;/*{{{*/
        /**
         * The slider will be bound to the right edge of its parent when
         * closed.
         * This sets the orientation as SLIDER::ORIENTATION::HORIZONTAL and
         * direction of opening from right to left.
         **/
        public static final int RIGHT  = 1;/*}}}*/
        // public static final int BOTTOM = 0;/*{{{*/
        /**
         * The slider will be bound to the bottom area of its parent when
         * closed.
         * This sets the orientation as SLIDER::ORIENTATION::VERTICAL and
         * direction of opening from bottom to top.
         **/
        public static final int BOTTOM = 0;/*}}}*/
    }/*}}}*/
}
// vim:syntax=java.doxygen
