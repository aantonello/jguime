/**
 * \file
 * Defines the CRect class.
 *
 * \author Alessandro Antonello <aantonello@paralaxe.com.br>
 * \date   October 21, 2010
 *
 * \par Copyright
 * This file is provided in hope that it will be useful to someone. It is
 * offered in public domain. You may use, modify or distribute it freely.
 *
 * The code is provided "AS IS". There is no warranty at all, of any kind. You
 * may change it if you like. Or just use it as it is.
 */
package x.android.ui;

import android.graphics.Rect;
import android.graphics.Point;
import android.view.View;

import x.android.defs.ALIGN;
import x.android.defs.SIZE;
import x.android.utils.strings;

/**
 * Represents a screen coordinate.
 *//* --------------------------------------------------------------------- */
public class CRect
{
    /** \name PUBLIC FIELDS */ //@{
    public int left;                    /**< The left coordinate.   */
    public int top;                     /**< The top coordinate.    */
    public int right;                   /**< The right coordinate.  */
    public int bottom;                  /**< The botto coordinate.  */
    //@}

    /** \name CONSTRUCTORS */ //@{
    // public CRect();/*{{{*/
    /**
     * Default constructor.
     **/
    public CRect() {
        set(0, 0, 0, 0);
    }/*}}}*/
    // public CRect(int l, int t, int r, int b);/*{{{*/
    /**
     * Initializing constructor.
     **/
    public CRect(int l, int t, int r, int b) {
        set(l, t, r, b);
    }/*}}}*/
    // public CRect(CRect r);/*{{{*/
    /**
     * Copy constructor.
     **/
    public CRect(CRect r) {
        set(r.left, r.top, r.right, r.bottom);
    }/*}}}*/
    // public CRect(Rect r);/*{{{*/
    /**
     * Copy constructor.
     * Copies from an \c android.graphics.Rect class.
     **/
    public CRect(Rect r) {
        set(r.left, r.top, r.right, r.bottom);
    }/*}}}*/
    //@}

    /** \name ATTRIBUTES */ //@{
    // public final int width(); {{{
    /**
     * Gets the width of this rectangle.
     **/
    public final int width() {
        return (this.right - this.left);
    } // Width() }}}
    // public final int height(); {{{
    /**
     * Gets the height of this rectangle.
     **/
    public final int height() {
        return (this.bottom - this.top);
    } // Height() }}}
    // public final boolean isEmpty();/*{{{*/
    /**
     * Returns a value that defines if this is an empty rectangle.
     * A rectangle is considered empty when its width or height are equals or
     * less than zero.
     * \returns \b true if the rectangle is empty. Otherwise \b false.
     **/
    public final boolean isEmpty() {
        return ((width() <= 0) || (height() <= 0));
    }/*}}}*/
    // public final boolean hasPoint(Point pt);/*{{{*/
    /**
     * Checks if a point lies in this rectangle boundaries.
     * \param pt CPoint object with coordinates to be checked.
     * \return \b true if the \a pt coordinates are inside this rectangle
     * boundaries. Otherwise this function returns \b false.
     **/
    public final boolean hasPoint(Point pt) {
        return hasPoint(pt.x, pt.y);
    }/*}}}*/
    // public final boolean hasPoint(int x, int y);/*{{{*/
    /**
     * Checks if a point lies in this rectangle boundaries.
     * \param x The horizontal coordinate of the point to be checked.
     * \param y The vertical coordinate of the point to be checked.
     * \return \b true if the \a x and \a y coordinates are inside this
     * rectangle boundaries. Otherwise this function returns \b false.
     **/
    public final boolean hasPoint(int x, int y) {
        return ((x >= this.left) && (x < this.right) && (y >= this.top) && (y < this.bottom));
    }/*}}}*/
    //@}

    /** \name OPERATIONS */ //@{
    // public final CRect set(int l, int t, int r, int b);/*{{{*/
    /**
     * Sets or resets this rectangle coordinates.
     * \param l Value to set the \e left coordinate.
     * \param t Value to set the \e top coordinate.
     * \param r Value to set the \e right coordinate.
     * \param b Value to set the \e bottom coordinate.
     * \returns A reference to this rect object.
     **/
    public final CRect set(int l, int t, int r, int b) {
        this.left = l; this.top = t; this.right = r; this.bottom = b;
        return this;
    }/*}}}*/
    // public final CRect set(CRect rc);/*{{{*/
    /**
     * Sets or resets this rectangle coordinates.
     * \param rc A rectangle which coordinates will be used to reset this
     *      rect instance.
     * \returns A reference to this rect object.
     **/
    public final CRect set(CRect rc) {
        return set(rc.left, rc.top, rc.right, rc.bottom);
    }/*}}}*/
    // public final CRect set(Rect r);/*{{{*/
    /**
     * Sets this rectangle class coping values from an Android Rect class.
     * \param r The Android Rect class to copy.
     * \returns This rectangle instance.
     **/
    public final CRect set(Rect r) {
        return set(r.left, r.top, r.right, r.bottom);
    }/*}}}*/
    // public final CRect box(int x, int y, int cx, int cy);/*{{{*/
    /**
     * Sets or resets this rectangle with box values.
     * \param x Value to be set at the \e left coordinate.
     * \param y Value to be set as the \e top coordinate.
     * \param cx Value to be used as the width of the rectangle.
     * \param cy Value to be used as the height of the rectangle.
     * \return A reference to this object instance.
     **/
    public final CRect box(int x, int y, int cx, int cy) {
        this.left = x; this.top = y;
        this.right = x + cx; this.bottom = y + cy;
        return this;
    }/*}}}*/
    // public final CRect deflate(int all);/*{{{*/
    /**
     * Deflates this rectangle coordinates.
     * \param all Value to be added to the \e left and \e top coordindates.
     *      This value will also be subtracted from \e right and \e bottom
     *      coordinates.
     * \returns A reference to this rect object.
     **/
    public final CRect deflate(int all) {
        this.left += all; this.top += all; this.right -= all; this.bottom -= all;
        return this;
    }/*}}}*/
    // public final CRect deflate(int l, int t, int r, int b);/*{{{*/
    /**
     * Deflates this rectangle coordinates.
     * \param l Value to be added in the \e left coordinate.
     * \param t Value to be added in the \e top coordinate.
     * \param r Value to be subtracted from the \e right coordinate.
     * \param b Value to be subtracted from the \e bottom coordinate.
     * \returns A reference to this rect object.
     **/
    public final CRect deflate(int l, int t, int r, int b) {
        this.left += l; this.top += t; this.right -= r; this.bottom -= b;
        return this;
    }/*}}}*/
    // public final CRect inflate(int l, int t, int r, int b);/*{{{*/
    /**
     * Inflates this rectangle coordinates.
     * \param l Value to be subtracted from the \e left coordinate.
     * \param t Value to be subtracted from the \e top coordinate.
     * \param r Value to be added to the \e right coordinate.
     * \param b Value to be added to the \e bottom coordinate.
     * \returns A reference to this rect object.
     **/
    public final CRect inflate(int l, int t, int r, int b) {
        this.left -= l; this.top -= t; this.right += r; this.bottom += b;
        return this;
    }/*}}}*/
    // public final CRect offset(int offsetX, int offsetY);/*{{{*/
    /**
     * Move the coordinates of this rectangle by an offset.
     * \param offsetX The offset, from the current horizontal position, for the
     *      rectangle coordinates to be moved.
     * \param offsetY The offset, from the current vertical position, for the
     *      rectangle coordinates to be mofed.
     * \returns A reference to this rect object.
     **/
    public final CRect offset(int offsetX, int offsetY) {
        return set(this.left + offsetX, this.top + offsetY, this.right + offsetX, this.bottom + offsetY);
    }/*}}}*/
    // public final CRect moveTo(int x, int y);/*{{{*/
    /**
     * Move this rectangle to a fixed place.
     * \param x The horizontal coordinate to move the rectangle.
     * \param y The vertical coordinate to move the rectangle.
     * \returns A reference to this rect object.
     **/
    public final CRect moveTo(int x, int y) {
        return set(x, y, x + this.width(), y + this.height());
    }/*}}}*/
    // public final CRect align(CRect ref, int align, CRect padd);/*{{{*/
    /**
     * Aligns this rectangle at the reference rectangle.
     * \param ref Reference rectangle.
     * \param align Alignment specification. This can be a combination of the
     * values listed in the \c x.android.defs.ALIGN class. The constant values
     * \c x.android.defs.ALIGN#STRETCH and \c x.android.defs.ALIGN#FIT are
     * ignored.
     * \param padd A padding rectangle. The members of this object define the
     * distance between the reference rectangle edges with this rectangle
     * edges in respect with the alignment specification. This parameter can
     * be \b null.
     * \return The function return this rectangle instance reference.
     **/
    public final CRect align(CRect ref, int align, CRect padd) {
        CRect rc = (padd == null ? new CRect(0, 0, 0, 0) : new CRect(padd));

        if ((align & ALIGN.LEFT) != 0)     moveTo(ref.left + rc.left, this.top);
        if ((align & ALIGN.ATLEFT) != 0)   moveTo(ref.left - rc.right - this.width(), this.top);
        if ((align & ALIGN.TOP) != 0)      moveTo(this.left, ref.top + rc.top);
        if ((align & ALIGN.ATTOP) != 0)    moveTo(this.left, ref.top - rc.bottom - this.height());
        if ((align & ALIGN.RIGHT) != 0)    moveTo(ref.right - rc.right - this.width(), this.top);
        if ((align & ALIGN.ATRIGHT) != 0)  moveTo(ref.right + rc.left, this.top);
        if ((align & ALIGN.BOTTOM) != 0)   moveTo(this.left, ref.bottom - rc.bottom - this.height());
        if ((align & ALIGN.ATBOTTOM) != 0) moveTo(this.left, ref.bottom + rc.top);
        if ((align & ALIGN.HCENTER) != 0) {
            rc.left = ref.left + rc.left;
            rc.right = ref.right - rc.right;
            moveTo(((rc.width() / 2) - (this.width() / 2)) + rc.left, this.top);
        }
        if ((align & ALIGN.VCENTER) != 0) {
            rc.top = ref.top + rc.top;
            rc.bottom = ref.bottom - rc.bottom;
            moveTo(this.left, ((rc.height() / 2) - (this.height() / 2)) + rc.top);
        }
        return this;
    }/*}}}*/
    // public final CRect size(CRect ref, int size, CRect padd);/*{{{*/
    /**
     * Resizes this rectangle based on a reference rectangle.
     * \param ref The reference rectangle.
     * \param size Sizing flags. A combination of the following values:
     * - x.android.defs.SIZE#WIDTH: Matches the width of this rectangle with
     * the width of the reference rectangle subtracting the padding
     * rectangle's left and right offsets.
     * - x.android.defs.SIZE#HEIGHT: Matches the height of this rectangle with
     * the height of the reference rectangle subtracting the padding
     * rectangle's top and bottom offsets.
     * - x.android.defs.SIZE#FIT: Resizes this rectangle so it fits the
     * reference rectangle, after applying the padding offsets, keeping the
     * aspect ratio. When this flag is passed others flags are ignored.
     * .
     * \param padd Padding rectangle. The members #left, #top, #right and
     * #bottom are used as offsets. This parameter can be \b null.
     * \return This rectangle reference.
     **/
    public final CRect size(CRect ref, int size, CRect padd) {
        CRect rc = (padd == null ? new CRect(0, 0, 0, 0) : new CRect(padd));

        /* Applying the padding. */
        rc.left   = ref.left + rc.left;
        rc.top    = ref.top + rc.top;
        rc.right  = ref.right - rc.right;
        rc.bottom = ref.bottom - rc.bottom;

        if ((size & SIZE.FIT) != 0)
        {
            if ((this.width() > rc.width()) || (this.height() > rc.height()))
            {
                if (this.width() > this.height())
                {
                    this.bottom = this.top + ((this.height() * rc.width()) / this.width());
                    this.right  = this.left + rc.width();
                }
                else
                {
                    this.right  = this.left + ((this.width() * rc.height()) / this.height());
                    this.bottom = this.top + rc.height();
                }
            }
            return this;
        }

        if ((size & SIZE.WIDTH) != 0) {
            this.right = this.left + rc.width();
        }

        if ((size & SIZE.HEIGHT) != 0) {
            this.bottom = this.top + rc.height();
        }
        return this;
    }/*}}}*/
    // public final CRect union(int x, int y, int r, int b);/*{{{*/
    /**
     * Produces an union of this rectangle and the coodinates passed.
     * \param x horizontal coordinate.
     * \param y vertical coordinate.
     * \param r right horizontal coordinate boundary.
     * \param b bottom vertical coordinate boundary.
     * \returns This object instance updated with the union of its bounds and
     * the coordinates passed.
     **/
    public final CRect union(int x, int y, int r, int b) {
        this.left   = Math.min(x, this.left);
        this.top    = Math.min(y, this.top);
        this.right  = Math.max(r, this.right);
        this.bottom = Math.max(b, this.bottom);
        return this;
    }/*}}}*/
    // public final CRect union(CRect r);/*{{{*/
    /**
     * Produces a union of this rectangle coordinates and the coordinates of
     * the passed rectangle.
     * \param r Rectangle.
     * \returns This object instance updated with the union of its bounds and
     * the coordinates passed.
     **/
    public final CRect union(CRect r) {
        return union(r.left, r.top, r.right, r.bottom);
    }/*}}}*/
    // public final CRect union(Rect r);/*{{{*/
    /**
     * Produces a union of this rectangle coordinates and the coordinates of
     * the passed rectangle.
     * \param r Rectangle.
     * \returns This object instance updated with the union of its bounds and
     * the coordinates passed.
     **/
    public final CRect union(Rect r) {
        return union(r.left, r.top, r.right, r.bottom);
    }/*}}}*/
    // public final CRect intersect(int x, int y, int r, int b);/*{{{*/
    /**
     * Produces a rectangle result of the intersection of this rectangle with
     * the coordinates passed.
     * If there is not an intersection between the coordinates, this rectangle
     * instance remains unchanged and the function returns \b null.
     * \param x horizontal coordinate.
     * \param y vertical coordinate.
     * \param r right horizontal coordinate boundary.
     * \param b bottom vertical coordinate boundary.
     * \returns This object instance changed with the intersection of the
     * coordinates or \b null if there is no intersection.
     **/
    public final CRect intersect(int x, int y, int r, int b) {
        int newX = Math.max(x, this.left);
        int newY = Math.max(y, this.top);
        int newR = Math.min(r, this.right);
        int newB = Math.min(b, this.bottom);

        if ((newR < newX) || (newB < newY))
            return null;        /* No intersection found. */

        return set(newX, newY, newR, newB);
    }/*}}}*/
    // public final CRect intersect(CRect r);/*{{{*/
    /**
     * Produces an intersection of this rectangle with the passed rectangle.
     * If there is not an intersection between the coordinates, this rectangle
     * instance remains unchanged and the function returns \b null.
     * \param r The rectangle to intersect.
     * \returns This object instance changed with the intersection of the
     * coordinates or \b null if there is no intersection.
     **/
    public final CRect intersect(CRect r) {
        return intersect(r.left, r.top, r.right, r.bottom);
    }/*}}}*/
    // public final CRect intersect(Rect r);/*{{{*/
    /**
     * Produces an intersection of this rectangle with the passed rectangle.
     * If there is not an intersection between the coordinates, this rectangle
     * instance remains unchanged and the function returns \b null.
     * \param r The rectangle to intersect.
     * \returns This object instance changed with the intersection of the
     * coordinates or \b null if there is no intersection.
     **/
    public final CRect intersect(Rect r) {
        return intersect(r.left, r.top, r.right, r.bottom);
    }/*}}}*/
    // public final CRect empty();/*{{{*/
    /**
     * Turns this rectangle empty.
     * \returns This rectangle instance.
     **/
    public final CRect empty() {
        return set(0, 0, 0, 0);
    }/*}}}*/
    //@}

    /** \name CONVERSION OPERATIONS */ //@{
    // public static final CRect Make(Rect r);/*{{{*/
    /**
     * Create a new CRect instance based on a Rect object.
     **/
    public static final CRect Make(Rect r) {
        return new CRect(r);
    }/*}}}*/
    // public static final CRect Make(View v);/*{{{*/
    /**
     * Builds a rectangle with the dimmensions of a View.
     * \param v The View to measure.
     **/
    public static final CRect Make(View v) {
        CRect rect = new CRect();
        rect.set((int)v.getLeft(), (int)v.getTop(), v.getRight(), v.getBottom());
        return rect;
    }/*}}}*/
    // public static final CRect HitRect(View v);/*{{{*/
    /**
     * Builds a rectangle from the hitting rect of a View.
     * \param v The View to get the hitting rectangle.
     * \returns The new CRect created. The hitting rectangle is returned in
     * the parent's View coordinates.
     **/
    public static final CRect HitRect(View v) {
        Rect rect = new Rect();
        v.getHitRect(rect);

        return new CRect(rect);
    }/*}}}*/
    // public static final CRect ViewRect(View v);/*{{{*/
    /**
     * Build a new CRect object with the coordinates of the passed View.
     * \param v View to get its coodinates.
     * \return A new CRect object.
     * \remarks The CRect returned will have the rectangle of the View on
     * its parent coordinates.
     **/
    public static final CRect ViewRect(View v) {
        CRect rect = new CRect();
        rect.box(v.getLeft(), v.getTop(), v.getWidth(), v.getHeight());
        return rect;
    }/*}}}*/
    // public static final CRect ClientRect(View v);/*{{{*/
    /**
     * Gets the client rectangle of a View.
     * \param v The View to get its client rectangle.
     * \return A new CRect object with the View's coordinates.
     * \remarks In the client coordinates the `left` and `top` members are
     * always zero.
     **/
    public static final CRect ClientRect(View v) {
        CRect rect = new CRect();
        rect.box(0, 0, v.getWidth(), v.getHeight());
        return rect;
    }/*}}}*/
    // public final Rect getRect(Rect r);/*{{{*/
    /**
     * Converts this instance into a android.graphics.Rect object.
     * \param r A Rect instance. Can be \b null. If passed the function will
     * match the data of the rectangle with this CRect instance. If \b null a
     * new Rect will be returned.
     **/
    public final Rect getRect(Rect r) {
        Rect rect = ((r == null) ? new Rect() : r);
        rect.left = this.left; rect.right  = this.right;
        rect.top  = this.top;  rect.bottom = this.bottom;
        return rect;
    }/*}}}*/
    //@}

    /** \name View OPERATIONS */ //@{
    // public final void setViewPos(View v);/*{{{*/
    /**
     * Changes the position of a View.
     * \param v The View to change its position.
     **/
    public final void setViewPos(View v)
    {
        CRect rect = ViewRect(v);
        v.layout(this.left, this.top, rect.right, rect.bottom);
    }/*}}}*/
    // public final void setViewRect(View v);/*{{{*/
    /**
     * Sets the rectangle for the View.
     * \param v View to be changed.
     **/
    public final void setViewRect(View v)
    {
        v.layout(this.left, this.top, this.right, this.bottom);
    }/*}}}*/
    // public final void getHitRect(View v);/*{{{*/
    /**
     * Gets the hitting rectangle of a View and fill this instance with its
     * values.
     * \param v The View to get its hitting rectangle.
     * \remarks The hitting rectangle is returned in the parent's view
     * coordinates. The function needs to build a new Rect instance to
     * accomplish its operations. If you has a reusable Rect object use the
     * overloaded version.
     **/
    public final void getHitRect(View v) {
        Rect rect = new Rect();
        this.getHitRect(v, rect);
    }/*}}}*/
    // public final void getHitRect(View v, Rect r);/*{{{*/
    /**
     * Gets the hitting rectangle of a View and fill this instance with its
     * values.
     * \param v The View to get its hitting rectangle.
     * \param r A reusable Rect to get the View hitting rectangle.
     * \remarks The hitting rectangle is returned in the parent's view
     * coordinates.
     **/
    public final void getHitRect(View v, Rect r) {
        v.getHitRect(r);
        this.set(r);
    }/*}}}*/
    // public final CRect getViewRect(View v);/*{{{*/
    /**
     * Gets a View rectangle on it's parent coordinates.
     * \param v The View to get it's bound rectangle.
     * \return The function returns **this**.
     **/
    public final CRect getViewRect(View v) {
        this.box(v.getLeft(), v.getTop(), v.getWidth(), v.getHeight());
        return this;
    }/*}}}*/
    // public final CRect getClientRect(View v);/*{{{*/
    /**
     * Gets the client rectangle of the passed View.
     * \param v View to get its client rectangle.
     * \return The function returns **this**.
     * \remarks In the client coordinates, the `left` and `top` members are
     * always zero.
     **/
    public final CRect getClientRect(View v) {
        this.box(0, 0, v.getWidth(), v.getHeight());
        return this;
    }/*}}}*/
    //@}

    /** \name OVERRIDES */ //@{
    // public boolean equals(CRect r);/*{{{*/
    /**
     * Checks for equality.
     * Two rectangles are equal only if they represent the same coordinates.
     **/
    public boolean equals(CRect r) {
        return ((this.left == r.left) && (this.top == r.top) && (this.right == r.right) && (this.bottom == r.bottom));
    }/*}}}*/
    // public boolean equals(Rect r);/*{{{*/
    /**
     * Checks for equality.
     * Two rectangles are equal only if they represent the same coordinates.
     **/
    public boolean equals(Rect r) {
        return ((this.left == r.left) && (this.top == r.top) && (this.right == r.right) && (this.bottom == r.bottom));
    }/*}}}*/
    // public boolean equals(Object obj);/*{{{*/
    /**
     * Checks for equality.
     * Two rectangles are equal only if they represent the same coordinates.
     **/
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj instanceof CRect) return equals((CRect)obj);
        if (obj instanceof android.graphics.Rect) 
            return equals((android.graphics.Rect)obj);

        return false;
    }/*}}}*/
    // public String  toString();/*{{{*/
    /**
     * Returns a textual representation of this rectangle.
     * The result is: "{left, top, right, bottom}".
     **/
    public String toString() {
        return String.format("{%d, %d, %d, %d}", this.left, this.top, this.right, this.bottom);
    }/*}}}*/
    // public String  toString(String fmt);/*{{{*/
    /**
     * Converts this objeto into a string according to the specified format.
     * \param fmt Format of the values to return. This parameter can have any
     * embedded text plus the following format specifications:
     * - \b %L: Add the `left` coordinate to the string.
     * - \b %T: Add the `top` coordinate to the string.
     * - \b %R: Add the `right` coordinate to the string.
     * - \b %B: Add the `bottom` coordinate to the string.
     * - \b %W: Add the calculated `width()` to the string.
     * - \b %H: Add the calculated `height()` to the string.
     * .
     * \return A string with values formated.
     **/
    public String toString(String fmt) {
        if (strings.length(fmt) == 0) return strings.EMPTY;

        StringBuilder sb = new StringBuilder(strings.length(fmt));
        char[]    format = fmt.toCharArray();
        int i = 0, limit = format.length;
        char c;

        while (i < limit) {
            c = format[i++];
            if ((c != '%') || (i >= limit)) {
                sb.append(c);
                continue;
            }

            c = format[i++];
            switch (c) {
            case '%': sb.append(c); break;
            case 'L': sb.append(this.left); break;
            case 'T': sb.append(this.top); break;
            case 'R': sb.append(this.right); break;
            case 'B': sb.append(this.bottom); break;
            case 'W': sb.append(this.width()); break;
            case 'H': sb.append(this.height()); break;
            }
        }
        return sb.toString();
    }/*}}}*/
    //@}
}
// vim:syntax=java.doxygen
