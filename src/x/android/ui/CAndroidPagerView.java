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
    implements ViewTreeObserver.OnGlobalLayoutListener
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
    // public CAndroidPagerView(Context context, int sideBuffer);/*{{{*/
    /**
     * Constructor.
     * @param context Current context.
     * @param sideBuffer How many views to load off screen?
     **/
    public CAndroidPagerView(Context context, int sideBuffer)
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
    // public void setAdapter(ArrayAdapter<View> adapter, int initialPosition);/*{{{*/
    /**
     * Sets the adapter and the initial position.
     * @param adapter An ArrayAdapter.
     * @param initialPosition The initial position.
     **/
    public void setAdapter(ArrayAdapter<View> adapter, int initialPosition)
    {
        if (m_adapter != null)
            m_adapter.unregisterDataSetObserver(m_datasetObserver);

        m_adapter = adapter;

        if (m_adapter != null)
        {
            if (m_datasetObserver == null)
                m_datasetObserver = new AdapterDataSetObserver();
            m_adapter.registerDataSetObserver(m_datasetObserver);
        }

        if (m_adapter == null || m_adapter.getCount() == 0)
            return;
        
        setSelection(initialPosition);      
    }/*}}}*/
    // public void addView(View view);/*{{{*/
    /**
     * Adds a View in the child list.
     * @param view The View to add.
     **/
    public void addView(View view)
    {
        if (m_adapter == null) return;
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
        if ((ev.getAction() == MotionEvent.ACTION_MOVE) && (m_touchState == TOUCH_STATE_SCROLLING))
            /* For now on, all events will be delivered directly to our own
             * implementation of onTouchEvent().
             */
            return true;
        else
            return false;
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
        final int childWidthSpec = getChildMeasureSpec(parentWidthMeasureSpec, getWidthPadding(), lp.width);
        final int childHeightSpec = getChildMeasureSpec(parentHeightMeasureSpec, getHeightPadding(), lp.height);
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
        if (m_scroller.computeScrollOffset()) {
            scrollTo(m_scroller.getCurrX(), m_scroller.getCurrY());
            postInvalidate();
        } else if (m_nextScreen != INVALID_SCREEN) {
            m_currentScreen = Math.max(0,
                    Math.min(m_nextScreen, getChildCount() - 1));
            m_nextScreen = INVALID_SCREEN;
            post(new Runnable() {
                @Override public void run() {
                    postViewSwitched(m_lastScrollDirection);
                }
            });
        }
    }/*}}}*/
    // public    void  onConfigurationChanged(Configuration newConfig);/*{{{*/
    /**
     * Called when the current configuration of the resources being used by
     * the application have changed.
     * You can use this to decide when to reload resources that can changed
     * based on orientation and other configuration characterstics. You only
     * need to use this if you are not relying on the normal \c Activity
     * mechanism of recreating the activity instance upon a configuration
     * change.
     * @param newConfig The new resource configuration.
     **/
    public void onConfigurationChanged(Configuration newConfig)
    {
        if (newConfig.orientation != m_lastOrientation) {
            m_lastOrientation = newConfig.orientation;
            getViewTreeObserver().addOnGlobalLayoutListener(this);
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

        switch (action) {
        case MotionEvent.ACTION_DOWN:
            /*
             * If being flinged and user touches, stop the fling. isFinished
             * will be false if being flinged.
             */
            if (!m_scroller.isFinished()) {
                m_scroller.abortAnimation();
            }

            // Remember where the motion event started
            m_lastMotionX = x;

            m_touchState = m_scroller.isFinished() ? TOUCH_STATE_REST : TOUCH_STATE_SCROLLING;

            break;

        case MotionEvent.ACTION_MOVE:
            final int deltaX = (int) (m_lastMotionX - x);

            boolean xMoved = Math.abs(deltaX) > m_touchSlop;

            if (xMoved) {
                // Scroll if the user moved far enough along the X axis
                m_touchState = TOUCH_STATE_SCROLLING;
            }

            if (m_touchState == TOUCH_STATE_SCROLLING) {
                // Scroll to follow the motion event

                m_lastMotionX = x;

                final int scrollX = getScrollX();
                if (deltaX < 0) {
                    if (scrollX > 0) {
                        scrollBy(Math.max(-scrollX, deltaX), 0);
                    }
                } else if (deltaX > 0) {
                    final int availableToScroll = getChildAt(
                            getChildCount() - 1).getRight()
                            - getPaddingRight() - getHorizontalFadingEdgeLength()
                            - scrollX - getChildWidth();
                    if (availableToScroll > 0) {
                        scrollBy(Math.min(availableToScroll, deltaX), 0);
                    }
                }
                return true;
            }
            break;

        case MotionEvent.ACTION_UP:
            if (m_touchState == TOUCH_STATE_SCROLLING) {
                final VelocityTracker velocityTracker = m_velocityTracker;
                velocityTracker.computeCurrentVelocity(1000, m_maximumVelocity);
                int velocityX = (int) velocityTracker.getXVelocity();

                if (velocityX > SNAP_VELOCITY && m_currentScreen > 0) {
                    // Fling hard enough to move left
                    snapToScreen(m_currentScreen - 1);
                } else if (velocityX < -SNAP_VELOCITY
                        && m_currentScreen < getChildCount() - 1) {
                    // Fling hard enough to move right
                    snapToScreen(m_currentScreen + 1);
                } else {
                    snapToDestination();
                }

                if (m_velocityTracker != null) {
                    m_velocityTracker.recycle();
                    m_velocityTracker = null;
                }
            }

            m_touchState = TOUCH_STATE_REST;

            break;
        case MotionEvent.ACTION_CANCEL:
            snapToDestination();
            m_touchState = TOUCH_STATE_REST;
        }
        return true;
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
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int childWidth = 0;
        int childHeight = 0;
        int childState = 0;

        final int widthPadding = getWidthPadding();
        final int heightPadding = getHeightPadding();

        int count = getViewsCount();
        if (count > 0) {
            final View child = obtainView(0);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            childWidth = child.getMeasuredWidth();
            childHeight = child.getMeasuredHeight();
            childState = child.getMeasuredState();
            m_recycledViews.add(child);
        }

        switch (widthMode) {
            case MeasureSpec.UNSPECIFIED:
                widthSize = childWidth + widthPadding;
                break;
            case MeasureSpec.AT_MOST:
                widthSize = (childWidth + widthPadding) | childState;
                break;
            case MeasureSpec.EXACTLY:
                if (widthSize < childWidth + widthPadding)
                    widthSize |= MEASURED_STATE_TOO_SMALL;
                break;
        }
        switch (heightMode) {
            case MeasureSpec.UNSPECIFIED:
                heightSize = childHeight + heightPadding;
                break;
            case MeasureSpec.AT_MOST:
                heightSize = (childHeight + heightPadding) | (childState >> MEASURED_HEIGHT_STATE_SHIFT);
                break;
            case MeasureSpec.EXACTLY:
                if (heightSize < childHeight + heightPadding)
                    heightSize |= MEASURED_STATE_TOO_SMALL;
                break;
        }

        if (heightMode == MeasureSpec.UNSPECIFIED) {
            heightSize = heightPadding + childHeight;
        } else {
            heightSize |= (childState&MEASURED_STATE_MASK);
        }

        setMeasuredDimension(widthSize, heightSize);
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

        final int count = getChildCount();
        for (int i = 0; i < count ; ++i) {
            final View child = getChildAt(i);
            child.measure(MeasureSpec.makeMeasureSpec(getChildWidth(), MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(getChildHeight(), MeasureSpec.EXACTLY));
        }

        if (m_scroller == null)     /* First layout. */
        {
            m_scroller = new Scroller( getContext() );
            m_scroller.startScroll(0, 0, m_currentScreen * getChildWidth(), 0, 0);
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
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                final int childWidth = child.getMeasuredWidth();
                child.layout(childLeft, getPaddingTop(), childLeft + childWidth,
                        getPaddingTop() + child.getMeasuredHeight());
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
        setAdapter(adapter, 0);
    }/*}}}*/
    // public View    getSelectedView();/*{{{*/
    /**
     * Get the selected View.
     * @returns The selected View.
     **/
    @Override
    public View getSelectedView()
    {
        return ((m_currentBufferIndex < m_loadedViews.size()) ?
                m_loadedViews.get(m_currentBufferIndex) : null);
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
        return m_currentAdapterIndex;
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
        m_nextScreen = INVALID_SCREEN;
        m_scroller.forceFinished(true);
        if (m_adapter == null)
            return;
        
        position = Math.max(position, 0);
        position = Math.min(position, m_adapter.getCount()-1);

        recycleViews();

        View currentView = makeAndAddView(position, true);
        m_loadedViews.addLast(currentView);

        for(int offset = 1; m_sideBuffer - offset >= 0; offset++) {
            int leftIndex = position - offset;
            int rightIndex = position + offset;
            if(leftIndex >= 0)
                m_loadedViews.addFirst(makeAndAddView(leftIndex, false));
            if(rightIndex < m_adapter.getCount())
                m_loadedViews.addLast(makeAndAddView(rightIndex, true));
        }

        m_currentBufferIndex = m_loadedViews.indexOf(currentView);
        m_currentAdapterIndex = position;

        requestLayout();
        setVisibleView(m_currentBufferIndex, false);
    }/*}}}*/
    //@}

    /** \name Overridables */ //@{
    // protected void recycleViews();/*{{{*/
    /**
     * Recycle Views.
     **/
    protected void recycleViews()
    {
        while (!m_loadedViews.isEmpty())
            recycleView(m_loadedViews.remove());
    }/*}}}*/
    // protected void recycleView(View v);/*{{{*/
    /**
     * Recycle a single View.
     * @param v The View to recycle.
     **/
    protected void recycleView(View v)
    {
        if (v == null)
            return;
        m_recycledViews.addFirst(v);
        detachViewFromParent(v);
    }/*}}}*/
    // protected View getRecycledView();/*{{{*/
    /**
     * Gets a recycled View.
     * @returns View.
     **/
    protected View getRecycledView()
    {
        return (m_recycledViews.isEmpty() ? null : m_recycledViews.remove());
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
        m_scroller      = null;
        m_loadedViews   = new LinkedList<View>();
        m_recycledViews = new LinkedList<View>();
        m_sideBuffer = 3;
        m_touchState = TOUCH_STATE_REST;
        m_nextScreen = INVALID_SCREEN;
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
    // private void snapToDestination();/*{{{*/
    /**
     * Snap the scroll position to the destination View.
     **/
    private void snapToDestination()
    {
        final int screenWidth = getChildWidth();
        final int whichScreen = (getScrollX() + (screenWidth / 2))
                / screenWidth;

        snapToScreen(whichScreen);
    }/*}}}*/
    // private void snapToScreen(int whichScreen);/*{{{*/
    /**
     * Snap the bounds to a screen position.
     * @param whichScreen The screen to snap in.
     **/
    private void snapToScreen(int whichScreen)
    {
        m_lastScrollDirection = whichScreen - m_currentScreen;
        if (!m_scroller.isFinished())
            return;

        whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));

        m_nextScreen = whichScreen;

        final int newX = whichScreen * getChildWidth();
        final int delta = newX - getScrollX();
        m_scroller.startScroll(getScrollX(), 0, delta, 0, Math.abs(delta) * 2);
        invalidate();
    }/*}}}*/
    // private void setVisibleView(int indexInBuffer, boolean uiThread);/*{{{*/
    /**
     * Scroll to the View in the view buffer specified by the index.
     * @param indexInBuffer Index of the view in the view buffer.
     */
    private void setVisibleView(int indexInBuffer, boolean uiThread)
    {
        m_currentScreen = Math.max(0,
                Math.min(indexInBuffer, getChildCount() - 1));
        int dx = (m_currentScreen * getChildWidth()) - m_scroller.getCurrX();
        m_scroller.startScroll(m_scroller.getCurrX(), m_scroller.getCurrY(), dx,
                0, 0);
        if(dx == 0)
            onScrollChanged(m_scroller.getCurrX() + dx, m_scroller.getCurrY(), m_scroller.getCurrX() + dx, m_scroller.getCurrY());
        if (uiThread)
            invalidate();
        else
            postInvalidate();
    }/*}}}*/
    // private void resetFocus();/*{{{*/
    /**
     * Resets the current focus.
     **/
    private void resetFocus()
    {
        logBuffer();
        recycleViews();
        removeAllViewsInLayout();

        for (int i = Math.max(0, m_currentAdapterIndex - m_sideBuffer); i < Math
                .min(m_adapter.getCount(), m_currentAdapterIndex + m_sideBuffer
                        + 1); i++) {
            m_loadedViews.addLast(makeAndAddView(i, true));
            if (i == m_currentAdapterIndex) {
                m_currentBufferIndex = m_loadedViews.size() - 1;
            }
        }
        logBuffer();
        requestLayout();
    }/*}}}*/
    // private void postViewSwitched(int direction);/*{{{*/
    /**
     * Changes the current view and posts the switch event.
     * @param direction The direction of the movement.
     **/
    private void postViewSwitched(int direction)
    {
        if (direction == 0)
            return;

        if (direction > 0) { // to the right
            m_currentAdapterIndex++;
            m_currentBufferIndex++;

            // Recycle view outside buffer range
            if (m_currentAdapterIndex > m_sideBuffer) {
                recycleView(m_loadedViews.removeFirst());
                m_currentBufferIndex--;
            }

            // Add new view to buffer
            int newBufferIndex = m_currentAdapterIndex + m_sideBuffer;
            if (newBufferIndex < m_adapter.getCount())
                m_loadedViews.addLast(makeAndAddView(newBufferIndex, true));

        } else { // to the left
            m_currentAdapterIndex--;
            m_currentBufferIndex--;

            // Recycle view outside buffer range
            if (m_adapter.getCount() - 1 - m_currentAdapterIndex > m_sideBuffer) {
                recycleView(m_loadedViews.removeLast());
            }

            // Add new view to buffer
            int newBufferIndex = m_currentAdapterIndex - m_sideBuffer;
            if (newBufferIndex > -1) {
                m_loadedViews.addFirst(makeAndAddView(newBufferIndex, false));
                m_currentBufferIndex++;
            }

        }

        requestLayout();
        setVisibleView(m_currentBufferIndex, true);
        logBuffer();
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
    // private View makeAndAddView(int position, boolean addToEnd);/*{{{*/
    /**
     * Creates and adds a View in the hierarchy.
     * @param position Position to add.
     * @param addToEnd Add to end?
     * @return The View.
     **/
    private View makeAndAddView(int position, boolean addToEnd)
    {
        View view = obtainView(position);
        return setupChild(view, addToEnd, m_lastObtainedViewWasRecycled);
    }/*}}}*/
    // private View obtainView(int position);/*{{{*/
    /**
     * Gets a View.
     * @param position View position.
     * @returns The View.
     **/
    private View obtainView(int position)
    {
        View convertView = getRecycledView();
        View view = m_adapter.getView(position, convertView, this);
        if(view != convertView && convertView != null)
            m_recycledViews.add(convertView);
        m_lastObtainedViewWasRecycled = (view == convertView);
        ViewGroup.LayoutParams p = view.getLayoutParams();
        if (p == null) {
            p = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
            view.setLayoutParams(p);
        }
        return view;
    }/*}}}*/
    // private void logBuffer();/*{{{*/
    /**
     * Logging facility.
     **/
    private void logBuffer()
    {

        debug.w("CAndroidPagerView: sizeof m_loadedView: %d, sizeof recycledViews: %d\n",
                m_loadedViews.size(), m_recycledViews.size());
        debug.w("CAndroidPagerView: currentX: %d, currentY: %d\n",
                m_scroller.getCurrX(), m_scroller.getCurrY());
        debug.w("CAndroidPagerView: index in adapter: %d, in buffer: %d\n",
                m_currentAdapterIndex, m_currentBufferIndex);
    }/*}}}*/
    //@}

    /** \name ViewTreeObserver.OnGlobalLayoutListener Implementations */ //@{
    // public void onGlobalLayout();/*{{{*/
    /**
     * Callback method to be invoked when the global layout state or the
     * visibility of views within the view tree changes.
     **/
    @Override
    public void onGlobalLayout()
    {
        getViewTreeObserver().removeGlobalOnLayoutListener(this);
        setSelection(m_currentAdapterIndex);
    }/*}}}*/
    //@}

    /** \name Inner Classes */ //@{
    // class AdapterDataSetObserver extends DataSetObserver;/*{{{*/
    /**
     * Data set observer class.
     **/
    class AdapterDataSetObserver extends DataSetObserver
    {

        @Override
        public void onChanged() {
            View v = getChildAt(m_currentBufferIndex);
            if (v != null) {
                for (int index = 0; index < m_adapter.getCount(); index++) {
                    if (v.equals(m_adapter.getItem(index))) {
                        m_currentAdapterIndex = index;
                        break;
                    }
                }
            }
            resetFocus();
        }

        @Override
        public void onInvalidated() {
            // Not yet implemented!
        }

    }/*}}}*/
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
            return super.getItem(pos);
        }/*}}}*/
        //@}
    }/*}}}*/
    //@}

    /** \name Data Members */ //@{
    private AdapterDataSetObserver m_datasetObserver;
    private LinkedList<View> m_loadedViews;
    private LinkedList<View> m_recycledViews;
    private VelocityTracker  m_velocityTracker;
    private Scroller         m_scroller;
    private ArrayAdapter<View> m_adapter;
    private float            m_lastMotionX;
    private boolean          m_scrollEnabled;
    private int m_currentBufferIndex;
    private int m_currentAdapterIndex;
    private int m_sideBuffer;
    private int m_touchState;
    private int m_touchSlop;
    private int m_maximumVelocity;
    private int m_currentScreen;
    private int m_nextScreen;
    private int m_lastScrollDirection;
    private int m_lastOrientation = -1;
    /* Extra return value from obtainView: tells you whether the item it returned on the last call was recycled rather than created by the adapter.
     * This is a member because getting a second return value requires an allocation. */
    private boolean m_lastObtainedViewWasRecycled = false;
    //@}

    /** \name Constants */ //@{
    private static final int SNAP_VELOCITY         = 1000;
    private static final int INVALID_SCREEN        = -1;
    private static final int TOUCH_STATE_REST      = 0;
    private static final int TOUCH_STATE_SCROLLING = 1;
    //@}
}
// vim:syntax=java.doxygen
