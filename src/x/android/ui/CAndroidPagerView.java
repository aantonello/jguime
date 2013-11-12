/**
 * \file
 * Defines the CAndroidPagerView class.
 *
 * \author Alessandro Antonello <aantonello@paralaxe.com.br>
 * \date   novembro 05, 2013
 * \since  jguime 2.4
 * \version 1.0
 *
 * \copyright
 * Paralaxe Tecnologia, 2013. All rights reserved.
 */
package x.android.ui;
/* #imports {{{ */
import android.content.*;
import android.content.res.*;
import android.database.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import java.util.*;

import x.android.utils.*;
/* }}} #imports */
/**
 * Implements a pager view.
 * The implementation provides a default adapter that handles the storage of
 * cliente views. So there is no need to supply an adapter as long as storing
 * the views in memory is enough.
 *
 * There are two implementations of \c addView() function. One that accepts a
 * View and one that handles the loading of a View from a layout resource.
 * Both will add the View in the adapter list.
 *
 * The \c getViewsCount() method will return the current number of Views in
 * the list. \c getSelectedView() gets the current shown View in the screen.
 * \c getSelectedItemPosition() returns the index of the selected View in the
 * adapter list. Finally, \c setSelection() can be used to change the current
 * shown View by its position index.
 *
 * By default the pager will intercept all motion events to acknowledge when
 * the user wants to scroll to another View. This can prevent child Views to
 * receive some motion events. When this behavior is not desired it can be
 * disabled using \c enableScroll() method.
 *//* --------------------------------------------------------------------- */
public class CAndroidPagerView extends AdapterView<ArrayAdapter<View>>
{
    /** \name Constructors */ //@{
    // public CAndroidPagerView(Context context);/*{{{*/
    /**
     * Constructor.
     * @param context Current Context.
     **/
    public CAndroidPagerView(Context context)
    {
        super(context);
        init();
    }/*}}}*/
    // public CAndroidPagerView(Context context, AttributeSet attrs)/*{{{*/
    /**
     * Constructor.
     * @param context Current Context.
     * @param attrs Attributes loaded from layout. Not used.
     **/
    public CAndroidPagerView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }/*}}}*/
    // public CAndroidPagerView(Context context, AttributeSet attrs, int style);/*{{{*/
    /**
     * Builds the object with the given parameters.
     * @param context The Context to set theme.
     * @param attrs AttributeSet from the layout.
     * @param style Index of the style to apply.
     **/
    public CAndroidPagerView(Context context, AttributeSet attrs, int style)
    {
        super(context, attrs, style);
        init();
    }/*}}}*/
    //@}

    /** \name Attributes */ //@{
    // public int getViewsCount();/*{{{*/
    /**
     * Gets the number of client Views.
     * @return The number of client Views.
     **/
    public int getViewsCount()
    {
        return ((m_adapter == null) ? 0 : m_adapter.getCount());
    }/*}}}*/
    // public int getChildWidth();/*{{{*/
    /**
     * Gets the width of the client area.
     * @return The width in pixels.
     **/
    public int getChildWidth()
    {
        return getWidth() - getWidthPadding();
    }/*}}}*/
    // public int getChildHeight();/*{{{*/
    /**
     * Gets the height of the child Views.
     * @returns The height in pixels.
     **/
    public int getChildHeight()
    {
        return getHeight() - getHeightPadding();
    }/*}}}*/
    //@}

    /** \name Operations */ //@{
    // public void addView(View view);/*{{{*/
    /**
     * Adds a View in the child list.
     * @param view The View to add.
     * @remarks You should add how much Views you need. After adding all Views
     * you must call \c requestLayout() so we can put them together in the
     * right place. If you are adding views before the first layout, the \c
     * requestLayout() call can be supressed.
     **/
    public void addView(View view)
    {
        if (m_adapter == null) return;

        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (lp == null) {
            lp = new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        }
        super.attachViewToParent(view, m_adapter.getCount(), lp);

        m_adapter.add(view);
        m_adapter.notifyDataSetChanged();
    }/*}}}*/
    // public View addView(int layoutID);/*{{{*/
    /**
     * Add a View loading it from the specified layout resource.
     * @param layoutID The identifier os the resource.
     * @returns The created View.
     **/
    public View addView(int layoutID)
    {
        LayoutInflater li = LayoutInflater.from(getContext());
        View view = li.inflate(layoutID, this, false);

        addView(view);
        return view;
    }/*}}}*/
    // public void enableScroll(boolean enabled);/*{{{*/
    /**
     * Enables or disables the scrolling of the Views in the list.
     * @param enabled Pass \b true to enable the scroll. \b false will disable
     * it. When the scrolling is disabled all motion events are passed to the
     * current shown child View.
     **/
    public void enableScroll(boolean enabled)
    {
        m_scrollEnabled = enabled;
    }/*}}}*/
    //@}

    /** \name ViewGroup: Overrides */ //@{
    // public boolean onInterceptTouchEvent(MotionEvent ev);/*{{{*/
    /**
     * Implement this method to intercept all touch screen motion events.
     * This allows you to watch events as they are dispatched to your
     * children, and take ownership of the current gesture at any point.
     *
     * Using this function takes some care, as it has a fairly complicated
     * interaction with \c View.onTouchEvent(MotionEvent), and using it
     * requires implementing that method as well as this one in the correct
     * way. Events will be received in the following order:
     * -# You will receive the down event here.
     * -# The down event will be handled either by a child of this view group,
     *    or given to your own \c onTouchEvent() method to handle; this means
     *    you should implement \c onTouchEvent() to return \b true, so you
     *    will continue to see the rest of the gesture (instead of looking for
     *    a parent view to handle it). Also, by returning \b true from \c
     *    onTouchEvent(), you will not receive any following events in \c
     *    onInterceptTouchEvent() and all touch processing must happen in \c
     *    onTouchEvent() like normal.
     * -# For as long as you return \b false from this function, each following
     *    event (up to and including the final up) will be delivered first
     *    here and then to the target's \c onTouchEvent().
     * -# If you return \b true from here, you will not receive any following
     *    events: the target view will receive the same event but with the
     *    action \c ACTION_CANCEL, and all further events will be delivered to
     *    your \c onTouchEvent() method and no longer appear here. 
     * .
     * @param ev MotionEvent sent by
     * the system.
     **/
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        if (!m_scrollEnabled) return false;
        onTouchEvent(ev);

        /* For now on, all events will be delivered directly to our own
         * implementation of onTouchEvent().
         */
        return ((ev.getAction() == MotionEvent.ACTION_MOVE) && m_scrolling);
    }/*}}}*/
    // protected void measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec);/*{{{*/
    /**
     * Ask one of the children of this view to measure itself, taking into
     * account both the MeasureSpec requirements for this view and its
     * padding.
     * The heavy lifting is done in getChildMeasureSpec.
     * @param child The child to measure.
     * @param parentWidthMeasureSpec The width requirements for this view.
     * @param parentHeightMeasureSpec The height requirements for this view.
     **/
    @Override
    protected void measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec)
    {
        LayoutParams lp = child.getLayoutParams();
        int childWidthSpec = getChildMeasureSpec(parentWidthMeasureSpec, getWidthPadding(), lp.width);
        int childHeightSpec = getChildMeasureSpec(parentHeightMeasureSpec, getHeightPadding(), lp.height);
        child.measure(childWidthSpec, childHeightSpec);
    }/*}}}*/
    //@}

    /** \name View: Overrides */ //@{
    // public    void  computeScroll();/*{{{*/
    /**
     * Called by a parent to request that a child update its values for
     * \c mScrollX and \c mScrollY if necessary.
     * This will typically be done if the child is animating a scroll using a
     * \c Scroller object.
     **/
    @Override
    public void computeScroll()
    {
        if (!m_scroller.computeScrollOffset())
            m_currentIndex = (getScrollX() / getChildWidth());
        else
        {
            scrollTo(m_scroller.getCurrX(), m_scroller.getCurrY());
            postInvalidate();
        }
    }/*}}}*/
    // public boolean  onTouchEvent(MotionEvent ev);/*{{{*/
    /**
     * Implement this method to handle touch screen motion events.
     * If this method is used to detect click actions, it is recommended that
     * the actions be performed by implementing and calling performClick().
     * This will ensure consistent system behavior, including:
     * - obeying click sound preferences.
     * - dispatching \c OnClickListener calls.
     * - handling \c ACTION_CLICK when accessibility features are enabled.
     * .
     * @param ev MotionEvent.
     * @returns \b true if the event was handled, \b false otherwise.
     **/
    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        if (getChildCount() == 0)
            return false;

        if (m_velocityTracker == null) {
            m_velocityTracker = VelocityTracker.obtain();
        }
        m_velocityTracker.addMovement(ev);

        final int action = ev.getAction();
        final float x = ev.getX();

        switch (action)
        {
        case MotionEvent.ACTION_DOWN:
            return handle_actionDown((int)x);

        case MotionEvent.ACTION_MOVE:
            return handle_actionMove((int)x);

        case MotionEvent.ACTION_UP:
        case MotionEvent.ACTION_CANCEL:
            return handle_actionUp((int)x);
        }
        return false;
    }/*}}}*/
    // protected float getTopFadingEdgeStrength();/*{{{*/
    /**
     * Returns the strength, or intensity, of the top faded edge.
     * The strength is a value between 0.0 (no fade) and 1.0 (full fade). The
     * default implementation returns 0.0 or 1.0 but no value in between.
     * Subclasses should override this method to provide a smoother fade
     * transition when scrolling occurs.
     * @return The intensity of the top fade as a float between 0.0f and
     * 1.0f. Always 0 in this implementation.
     **/
    @Override
    protected float getTopFadingEdgeStrength()
    {
        return 0.0f;
    }/*}}}*/
    // protected float getRightFadingEdgeStrength();/*{{{*/
    /**
     * Returns the strength, or intensity, of the right faded edge.
     * The strength is a value between 0.0 (no fade) and 1.0 (full fade). The
     * default implementation returns 0.0 or 1.0 but no value in between.
     * Subclasses should override this method to provide a smoother fade
     * transition when scrolling occurs.
     * @returns The intensity of the right fade as a float between 0.0f and
     * 1.0f.
     **/
    @Override
    protected float getRightFadingEdgeStrength()
    {
        // always do the fading edge
        return 1.0f;
    }/*}}}*/
    // protected float getBottomFadingEdgeStrength();/*{{{*/
    /**
     * Returns the strength, or intensity, of the bottom faded edge.
     * The strength is a value between 0.0 (no fade) and 1.0 (full fade). The
     * default implementation returns 0.0 or 1.0 but no value in between.
     * Subclasses should override this method to provide a smoother fade
     * transition when scrolling occurs.
     * @return The intensity of the bottom fade as a float between 0.0f and
     * 1.0f. Always 0 in this implementation.
     **/
    @Override
    protected float getBottomFadingEdgeStrength()
    {
        return 0.0f;
    }/*}}}*/
    // protected float getLeftFadingEdgeStrength();/*{{{*/
    /**
     * Returns the strength, or intensity, of the left faded edge.
     * The strength is a value between 0.0 (no fade) and 1.0 (full fade). The
     * default implementation returns 0.0 or 1.0 but no value in between.
     * Subclasses should override this method to provide a smoother fade
     * transition when scrolling occurs.
     * @returns The intensity of the left fade as a float between 0.0f and
     * 1.0f.
     **/
    @Override
    protected float getLeftFadingEdgeStrength()
    {
        // always do the fading edge
        return 1.0f;
    }/*}}}*/
    // protected void  onMeasure(int widthMeasureSpec, int heightMeasureSpec)/*{{{*/
    /**
     * Measure the view and its content to determine the measured width and
     * the measured height.
     * This method is invoked by measure(int, int) and should be overriden by
     * subclasses to provide accurate and efficient measurement of their
     * contents.
     * @param widthMeasureSpec Horizontal space requirements as imposed by the
     * parent. The requirements are encoded with \c View.MeasureSpec.
     * @param heightMeasureSpec Vertical space requirements as imposed by the
     * parent. The requirements are encoded with \c View.MeasureSpec.
     **/
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int wSize = (widthMeasureSpec & 0x0FFFFFFF);
        int hSize = (heightMeasureSpec & 0x0FFFFFFF); 
        int wMode = (widthMeasureSpec & 0xF0000000);
        int hMode = (heightMeasureSpec & 0xF0000000);

        int childWidth = 0;
        int childHeight = 0;
        int childState = 0;

        final int widthPadding = getWidthPadding();
        final int heightPadding = getHeightPadding();

        if (getViewsCount() > 0) {
            final View child = obtainView(0);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            childWidth = child.getMeasuredWidth();
            childHeight = child.getMeasuredHeight();
            childState = child.getMeasuredState();
        }

        switch (wMode) {
            case MeasureSpec.UNSPECIFIED:
                wSize = childWidth + widthPadding;
                break;
            case MeasureSpec.AT_MOST:
                wSize = (childWidth + widthPadding) | childState;
                break;
            case MeasureSpec.EXACTLY:
                if (wSize < childWidth + widthPadding)
                    wSize |= MEASURED_STATE_TOO_SMALL;
                break;
        }
        switch (hMode) {
            case MeasureSpec.UNSPECIFIED:
                hSize = childHeight + heightPadding;
                break;
            case MeasureSpec.AT_MOST:
                hSize = (childHeight + heightPadding) | (childState >> MEASURED_HEIGHT_STATE_SHIFT);
                break;
            case MeasureSpec.EXACTLY:
                if (hSize < childHeight + heightPadding)
                    hSize |= MEASURED_STATE_TOO_SMALL;
                break;
        }

        if (hMode == MeasureSpec.UNSPECIFIED) {
            hSize = heightPadding + childHeight;
        } else {
            hSize |= (childState&MEASURED_STATE_MASK);
        }

        setMeasuredDimension(wSize, hSize);
    }/*}}}*/
    // protected void  onSizeChanged(int w, int h, int oldw, int oldh);/*{{{*/
    /**
     * This is called during layout when the size of this view has changed.
     * If you were just added to the view hierarchy, you're called with the
     * old values of 0.
     * @param w Current width of this view.
     * @param h Current height of this view.
     * @param oldw Old width of this view.
     * @param oldh Old height of this view.
     **/
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);

        final int count        = getChildCount();
        final int wMeasureSpec = MeasureSpec.makeMeasureSpec(getChildWidth(), MeasureSpec.EXACTLY);
        final int hMeasureSpec = MeasureSpec.makeMeasureSpec(getChildHeight(), MeasureSpec.EXACTLY);
        View child = null;

        for (int i = 0; i < count ; ++i) {
            child = getChildAt(i);
            child.measure(wMeasureSpec, hMeasureSpec);
        }

        if ((oldw == 0) && (oldh == 0)) {
            m_scroller.startScroll(0, 0, m_currentIndex * getChildWidth(), 0, 0);
        }
    }/*}}}*/
    // protected void  onLayout(boolean changed, int l, int t, int r, int b);/*{{{*/
    /**
     * Called from layout when this view should assign a size and position to
     * each of its children.
     * Derived classes with children should override this method and call
     * layout on each of their children.
     * @param changed This is a new size or position for this view.
     * @param left Left position, relative to parent.
     * @param top Top position, relative to parent.
     * @param right Right position, relative to parent.
     * @param bottom Bottom position, relative to parent.
     **/
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        int childLeft = getPaddingLeft() + getHorizontalFadingEdgeLength();

        final int count = getChildCount();
        final int topPadding = getPaddingTop();
        int childWidth = 0;
        View child = null;

        for (int i = 0; i < count; i++)
        {
            child = getChildAt(i);

            if (child.getVisibility() != View.GONE)
            {
                childWidth = child.getMeasuredWidth();
                child.layout(childLeft, topPadding, childLeft + childWidth, topPadding + child.getMeasuredHeight());

                childLeft += childWidth;
            }
        }
    }/*}}}*/
    //@}

    /** \name AdapterView: Overrides */ //@{
    // public ArrayAdapter<View> getAdapter();/*{{{*/
    /**
     * Returns the adapter currently associated with this widget.
     * @returns The adapter used to provide this view's content.
     **/
    @Override
    public ArrayAdapter<View> getAdapter()
    {
        return m_adapter;
    }/*}}}*/
    // public void    setAdapter(ArrayAdapter<View> adapter);/*{{{*/
    /**
     * Sets the adapter that provides the data and the views to represent the
     * data in this widget.
     * @param adapter The adapter to use to create this view's content.
     **/
    @Override
    public void setAdapter(ArrayAdapter<View> adapter)
    {
        m_adapter = adapter;
    }/*}}}*/
    // public View    getSelectedView();/*{{{*/
    /**
     * Get the selected View.
     * @returns The selected View.
     **/
    @Override
    public View getSelectedView()
    {
        return ((m_currentIndex < 0) ? null : getChildAt(m_currentIndex));
    }/*}}}*/
    // public int     getSelectedItemPosition();/*{{{*/
    /**
     * Return the position of the currently selected item within the adapter's
     * data set.
     * @returns The selected item position or \c INVALID_POSITION if none is
     * selected.
     **/
    @Override
    public int getSelectedItemPosition()
    {
        return m_currentIndex;
    }/*}}}*/
    // public void    setSelection(int position);/*{{{*/
    /**
     * Sets the currently selected item.
     * To support accessibility subclasses that override this method must
     * invoke the overriden super method first.
     * @param position Position of the selection.
     **/
    @Override
    public void setSelection(int position)
    {
        if (m_scroller != null) m_scroller.forceFinished(true);
        
        m_currentIndex = (position % getViewsCount());
        position = (m_currentIndex * getChildWidth());
        scrollTo(position, 0);
        requestLayout();
    }/*}}}*/
    //@}

    /** \name Local Operations */ //@{
    // private void init();/*{{{*/
    /**
     * Initializes this View.
     **/
    private void init()
    {
        m_adapter       = null;
        m_scroller      = new Scroller(getContext());
        m_currentIndex  = -1;
        m_scrolling     = false;
        m_scrollEnabled = true;

        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        m_touchSlop       = configuration.getScaledTouchSlop();
        m_maximumVelocity = configuration.getScaledMaximumFlingVelocity();

        setAdapter(new ArrayListView(this));
    }/*}}}*/
    // private int  getWidthPadding()/*{{{*/
    /**
     * Calculates the horizontal padding.
     * @return The horizontal padding in pixels.
     **/
    private int getWidthPadding()
    {
        return getPaddingLeft() + getPaddingRight() + getHorizontalFadingEdgeLength() * 2;
    }/*}}}*/
    // private int  getHeightPadding();/*{{{*/
    /**
     * Gets the height of the client area.
     * @returns The height in pixels.
     **/
    private int getHeightPadding()
    {
        return getPaddingTop() + getPaddingBottom();
    }/*}}}*/
    // private void resetFocus();/*{{{*/
    /**
     * Resets the current focus.
     **/
    private void resetFocus()
    {
        removeAllViewsInLayout();
        requestLayout();
    }/*}}}*/
    // private View setupChild(View child, boolean addToEnd, boolean recycle);/*{{{*/
    /**
     * Setup a child View.
     * @param child The child View.
     * @param addToEnd Add to the end?
     * @param recycle Recycle it?
     * @return The View.
     **/
    private View setupChild(View child, boolean addToEnd, boolean recycle)
    {
        final LayoutParams lp = child.getLayoutParams();
        child.measure(MeasureSpec.makeMeasureSpec(getChildWidth(), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(getChildHeight(), MeasureSpec.EXACTLY));
        if (recycle)
            attachViewToParent(child, (addToEnd ? -1 : 0), lp);
        else
            addViewInLayout(child, (addToEnd ? -1 : 0), lp, true);
        return child;
    }/*}}}*/
    // private View obtainView(int position);/*{{{*/
    /**
     * Gets a View.
     * @param position View position.
     * @returns The View.
     **/
    private View obtainView(int position)
    {
        if (m_adapter == null) return null;

        View view = m_adapter.getView(position, null, this);
        if (view == null) return null;

        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (lp == null) {
            lp = new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
            view.setLayoutParams(lp);
        }
        return view;
    }/*}}}*/
    // private void logBuffer();/*{{{*/
    /**
     * Logging facility.
     **/
    private void logBuffer()
    {
//        debug.w("CAndroidPagerView: sizeof m_loadedView: %d, sizeof recycledViews: %d\n",
//                m_loadedViews.size(), m_recycledViews.size());
//        if (m_scroller != null)
//            debug.w("CAndroidPagerView: currentX: %d, currentY: %d\n",
//                    m_scroller.getCurrX(), m_scroller.getCurrY());
//        debug.w("CAndroidPagerView: index in adapter: %d, in buffer: %d\n",
//                m_currentAdapterIndex, m_currentBufferIndex);
    }/*}}}*/
    //@}

    /** \name Local Reimplementation */ //@{
    // final boolean handle_actionDown(int pointX);/*{{{*/
    /**
     * Handles the \b ACTION_DOWN \c MotionEvent action.
     * @param pointX The current X position in the user touch.
     * @returns \b true if this view can handle this event. Otherwise \b
     * false.
     **/
    final boolean handle_actionDown(int pointX)
    {
        /* Stop any actual scrolling animation. */
        m_scroller.abortAnimation();
        m_lastMotionX = pointX;          /* Remember this position. */
        m_scrolling = false;

        return true;
    }/*}}}*/
    // final boolean handle_actionMove(int pointX);/*{{{*/
    /**
     * Handles the \b ACTION_MOVE of \c MotionEvent action.
     * @param pointX The current X position in the motion.
     * @returns \b true when this view can handle this motion. Otherwise \b
     * false.
     **/
    final boolean handle_actionMove(int pointX)
    {
        int deltaX = (int)(m_lastMotionX - pointX);

        /* Scroll the view only if passed the minimum amount. */
        if (Math.abs(deltaX) > m_touchSlop)
        {
            m_scrolling   = true;
            m_lastMotionX = pointX;
            super.scrollBy(deltaX, 0);
            /*
             * NOTE: We don't care if the scroll will pass the first or last
             * view position. When the motion ends, we will set the correct
             * position value.
             */
        }
        return true;
    }/*}}}*/
    // final boolean handle_actionUp(int pointX);/*{{{*/
    /**
     * Handles the \b ACTION_UP or \b ACTION_CANCEL of \c MotionEvent action.
     * @param pointX The current X position.
     * @returns \b true if this view can handle the event. Otherwise \b false.
     **/
    final boolean handle_actionUp(int pointX)
    {
        if (!m_scrolling) return true;          /* We are not scrolling. */

        int velocityX, minX, maxX;
        int childWidth = getChildWidth();

        m_velocityTracker.computeCurrentVelocity(1000, m_maximumVelocity);
        velocityX = (int)m_velocityTracker.getXVelocity();

        if ((velocityX > SNAP_VELOCITY) && (m_currentIndex > 0))
        {
            minX = (childWidth * (m_currentIndex - 1)); /* Fling to the previous View. */
            maxX = (childWidth * m_currentIndex);
        }
        else if ((velocityX < -SNAP_VELOCITY) && (m_currentIndex < (getChildCount() - 1)))
        {
            minX = (childWidth * m_currentIndex);
            maxX = (childWidth * (m_currentIndex + 1)); /* Fling to the next View. */
        }
        else
        {
            minX = (childWidth * m_currentIndex);       /* Keep in the current View. */
            maxX = minX;
        }
        m_scroller.fling(getScrollX(), 0, velocityX, 0, minX, maxX, 0, 0);
        m_velocityTracker.recycle();
        m_velocityTracker = null;
        m_scrolling = false;
        return true;
    }/*}}}*/
    //@}

    /** \name Inner Classes */ //@{
    // class ArrayListView extends ArrayAdapter<View>;/*{{{*/
    /**
     * Our implementation of the adapter view.
     **/
    class ArrayListView extends ArrayAdapter<View>
    {
        /** \name Constructor */ //@{
        // public ArrayListView(View parent);/*{{{*/
        /**
         * Default constructor.
         * @param parent The parent View.
         **/
        public ArrayListView(View parent)
        {
            super(parent.getContext(), 0);
        }/*}}}*/
        //@}

        /** \name Overrides */ //@{
        // public View getView(int pos, View reuseView, ViewGroup parent);/*{{{*/
        /**
         * Returns the View int the specified position.
         * @param pos The View position.
         * @param reuseView A recycled reused View. Not used.
         * @param parent The parent ViewGroup to place the returned View. Not
         * used.
         * @returns The View at the specified position.
         **/
        public View getView(int pos, View reuseView, ViewGroup parent)
        {
            reuseView = getItem(pos);
            debug.w("ArrayListView::getView(%d) == %s\n", pos, reuseView.getClass().getSimpleName());
            return reuseView;
        }/*}}}*/
        //@}
    }/*}}}*/
    //@}

    /** \name Data Members */ //@{
    private VelocityTracker  m_velocityTracker;
    private Scroller         m_scroller;
    private ArrayAdapter<View> m_adapter;
    private float            m_lastMotionX;
    private boolean          m_scrollEnabled;
    private boolean          m_scrolling;
    private int m_currentIndex;
    private int m_touchSlop;
    private int m_maximumVelocity;
    //@}

    /** \name Constants */ //@{
    private static final int SNAP_VELOCITY = 1000;
    //@}
}
// vim:syntax=java.doxygen
