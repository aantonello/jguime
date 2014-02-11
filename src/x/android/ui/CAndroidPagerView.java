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
import android.view.animation.*;
import android.widget.*;

import java.util.*;

import x.android.utils.*;
import x.android.nms.*;
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
 * The \c getChildCount() method will return the current number of Views in
 * the list. \c getCurrentView() gets the current shown View in the screen.
 * \c getCurrentViewIndex() returns the index of the selected View in the
 * adapter list. Finally, \c setCurrentView() can be used to change the
 * current shown View by its position index.
 *
 * By default the pager will intercept all motion events to acknowledge when
 * the user wants to scroll to another View. This can prevent child Views to
 * receive some motion events. When this behavior is not desired it can be
 * disabled using \c enableScroll() method.
 *//* --------------------------------------------------------------------- */
public class CAndroidPagerView extends ViewGroup
    implements INHandler
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
        _internal_init();
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
        _internal_init();
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
        _internal_init();
    }/*}}}*/
    //@}

    /** \name Attributes */ //@{
    // public int  getChildWidth();/*{{{*/
    /**
     * Gets the width of the client area.
     * @return The width in pixels.
     **/
    public int getChildWidth()
    {
        return getWidth() - _internal_horzPadding();
    }/*}}}*/
    // public int  getChildHeight();/*{{{*/
    /**
     * Gets the height of the child Views.
     * @returns The height in pixels.
     **/
    public int getChildHeight()
    {
        return getHeight() - _internal_vertPadding();
    }/*}}}*/
    // public View getCurrentView();/*{{{*/
    /**
     * Retrieves the current shown child view.
     * @return The current View. \b null if none.
     **/
    public View getCurrentView()
    {
        if (m_currentIndex < 0) return null;
        return getChildAt(m_currentIndex);
    }/*}}}*/
    // public int  getCurrentViewIndex();/*{{{*/
    /**
     * Retrieves the position index of the current selected View.
     * @return The integer position of the current View. Starting at 0. -1
     * means there is no View in this pager.
     **/
    public int  getCurrentViewIndex()
    {
        return m_currentIndex;
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
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (lp == null) {
            lp = new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        }
        int index = getChildCount();
        super.attachViewToParent(view, index, lp);
        if (m_currentIndex < 0) m_currentIndex = 0;
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
    // public CAndroidPagerView enableScroll(boolean enabled);/*{{{*/
    /**
     * Enables or disables the scrolling of the Views in the list.
     * @param enabled Pass \b true to enable the scroll. \b false will disable
     * it. When the scrolling is disabled all motion events are passed to the
     * current shown child View.
     * @returns This function returns \b this.
     **/
    public CAndroidPagerView enableScroll(boolean enabled)
    {
        m_scrollEnabled = enabled;
        return this;
    }/*}}}*/
    // public CAndroidPagerView setCurrentView(int position, boolean animated);/*{{{*/
    /**
     * Set/Changes the current View.
     * @param position The position index of the child View. Starting at 0.
     * @param animated Boolean value indicating if the change should be
     * animated. \b true to animate the change. \b false will set the current
     * View immediatly. When this call is made before the first layout, \a
     * animated must be \b false, otherwise the \a position argument will be
     * ignored.
     * @return This function returns \b this.
     **/
    public CAndroidPagerView setCurrentView(int position, boolean animated)
    {
        if ((position < 0) || (position >= getChildCount()))
            return this;

        int offsetX = (position * getChildWidth());
        if (!animated)
        {
            scrollTo(offsetX, 0);
            m_currentIndex = position;
        }
        else
        {
            int scrollX = getScrollX();
            m_scroller.startScroll(scrollX, 0, (offsetX - scrollX), 0, 500);
            issuer.post(this, IMSG.MSG_DELAY, NP_SCROLL, 0L, null);
        }
        return this;
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

        final int action = ev.getAction();
        final float y = (int)ev.getY();

        if (action == MotionEvent.ACTION_DOWN)
        {
            /* We almost never receive this event through onTouchEvent() since
             * we are covered with component views. So pass it here to
             * initialize some internal objects. Return 'false' so the real
             * target will also receive the event.
             */
            onTouchEvent(ev);
            m_skipScroll = false;
            return false;
        }
        else if ((action == MotionEvent.ACTION_MOVE) && !m_skipScroll)
        {
            /* This check must be made before calling 'onTouchEvent()'. If the
             * user is moving in a different direction (top -> bottom or
             * vice-versa), we must not pass the event thru, unless we are
             * already scrolling.
             */
            int deltaY = (int)(m_lastMotionY - y);
            if ((Math.abs(deltaY) > m_touchSlop) && !m_scrolling)
            {
                m_skipScroll = true;        /* Skip all events starting from here. */
                if (m_velocityTracker != null)
                    m_velocityTracker.recycle();
                m_velocityTracker = null;
            }
            else
                onTouchEvent(ev);
        }
        /* When 'm_scrolling' goes 'true' we pass to receive all
         * events thru 'onTouchEvent()'.
         */
        return m_scrolling;
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
        int childWidthSpec = getChildMeasureSpec(parentWidthMeasureSpec, _internal_horzPadding(), lp.width);
        int childHeightSpec = getChildMeasureSpec(parentHeightMeasureSpec, _internal_vertPadding(), lp.height);
        child.measure(childWidthSpec, childHeightSpec);
    }/*}}}*/
    //@}

    /** \name View: Overrides */ //@{
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
        final int x = (int)ev.getX();
        final int y = (int)ev.getY();

        switch (action)
        {
        case MotionEvent.ACTION_DOWN:
            return handle_actionDown(x, y);

        case MotionEvent.ACTION_MOVE:
            return handle_actionMove(x, y);

        case MotionEvent.ACTION_UP:
            return handle_actionUp(x);

        case MotionEvent.ACTION_CANCEL:
            return handle_actionCancel();
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
        int wSize = (widthMeasureSpec & 0x00FFFFFF);
        int hSize = (heightMeasureSpec & 0x00FFFFFF); 
        int wMode = (widthMeasureSpec & 0xFF000000);
        int hMode = (heightMeasureSpec & 0xFF000000);

        measureChildren(widthMeasureSpec, heightMeasureSpec);
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

        if ((oldw == 0) && (oldh == 0))
        {
            /* Check wether the first view is different from 0. Change it
             * after we have done all layout operations.
             */
            if (m_currentIndex != 0)
                issuer.post(this, IMSG.MSG_DELAY, NP_VIEW, 0L, null);
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
//        debug.w("CAndroidPagerView::onLayout(%d, %d, %d, %d)\n", left, top, right, bottom);
        int childLeft = getPaddingLeft() + getHorizontalFadingEdgeLength();

        final int count = getChildCount();
        final int topPadding = getPaddingTop();
        int childWidth = 0;
        int childHeight = 0;
        View child = null;
        CRect rect;

 //       debug.w("==> child count: %d\n", count);

        for (int i = 0; i < count; i++)
        {
            child = getChildAt(i);

            if (child.getVisibility() != View.GONE)
            {
                childWidth  = child.getMeasuredWidth();
                childHeight = child.getMeasuredHeight();

//                debug.w("==> child %d measured width: %d, measured height: %d\n", i, childWidth, childHeight);
                child.layout(childLeft, topPadding, childLeft + childWidth, topPadding + childHeight);
                rect = CRect.ViewRect(child);
//                debug.w("==> child %d rect: %s\n", i, rect.toString("left: %L, top: %T, width: %W, height: %H"));

                childLeft += childWidth;
            }
        }
    }/*}}}*/
    //@}

    /** \name INHandler: Implementation */ //@{
    // public boolean onMessage(int msgID, int nParam, int iParam, Object extra);/*{{{*/
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
        if ((msgID == IMSG.MSG_DELAY) && (nParam != NP_VIEW))
        {
            boolean stillScrolling = m_scroller.computeScrollOffset();

            scrollTo(m_scroller.getCurrX(), 0);
            invalidate();

            if (stillScrolling)
                issuer.post(this, IMSG.MSG_DELAY, nParam, 0L, null);
            else
            {
                int offsetX = getScrollX();
                float index = (((float)offsetX) / ((float)getChildWidth()));

                m_currentIndex = Math.round(index);
//                debug.w("CAndroidPagerView::onMessage(): scroll ended! currentIndex: %d (%1.4f)\n", m_currentIndex, index);

                if (nParam == NP_FLING)
                {
                    int targetX = (m_currentIndex * getChildWidth());

                    m_scroller.startScroll(offsetX, 0, (targetX - offsetX), 0, 300);
                    issuer.post(this, IMSG.MSG_DELAY, NP_SCROLL, 0L, null);
                }
            }
            return true;
        }
        else if ((msgID == IMSG.MSG_DELAY) && (nParam == NP_VIEW))
        {
            setCurrentView(m_currentIndex, false);
            return true;
        }
        return false;
    }/*}}}*/
    //@}

    /** \name Local Operations */ //@{
    // private void _internal_init();/*{{{*/
    /**
     * Initializes this View.
     **/
    private void _internal_init()
    {
        DecelerateInterpolator di = new DecelerateInterpolator(1.0f);
        m_scroller      = new OverScroller(getContext(), di);
        m_currentIndex  = -1;
        m_skipScroll    = false;
        m_scrolling     = false;
        m_scrollEnabled = true;

        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        m_touchSlop       = configuration.getScaledTouchSlop();
//        debug.w("CAndroidPagerView::init() \n" +
//                "==> touch slop: %d\n" +
//                "==> max fling velocity: %d\n" +
//                "==> friction: %1.4f\n", m_touchSlop, 
//                configuration.getScaledMaximumFlingVelocity(),
//                configuration.getScrollFriction());
    }/*}}}*/
    // private int  _internal_horzPadding()/*{{{*/
    /**
     * Calculates the horizontal padding.
     * @return The horizontal padding in pixels.
     **/
    private int _internal_horzPadding()
    {
        return getPaddingLeft() + getPaddingRight() + (getHorizontalFadingEdgeLength() * 2);
    }/*}}}*/
    // private int  _internal_vertPadding();/*{{{*/
    /**
     * Gets the height of the client area.
     * @returns The height in pixels.
     **/
    private int _internal_vertPadding()
    {
        return getPaddingTop() + getPaddingBottom();
    }/*}}}*/
    //@}

    /** \name Local Reimplementation */ //@{
    // final boolean handle_actionDown(int pointX, int pointY);/*{{{*/
    /**
     * Handles the \b ACTION_DOWN \c MotionEvent action.
     * @param pointX The current X position in the user touch.
     * @param pointY The current Y position in the user touch.
     * @returns \b true if this view can handle this event. Otherwise \b
     * false.
     **/
    final boolean handle_actionDown(int pointX, int pointY)
    {
        /* Stop any actual scrolling animation. */
        m_scroller.abortAnimation();
        m_lastMotionX = pointX;         /* Remember this position. */
        m_lastMotionY = pointY;         /* Check point. */
        m_scrolling = false;

        return true;
    }/*}}}*/
    // final boolean handle_actionMove(int pointX, int pointY);/*{{{*/
    /**
     * Handles the \b ACTION_MOVE of \c MotionEvent action.
     * @param pointX The current X position in the motion.
     * @param pointY The current Y position in the user touch.
     * @returns \b true when this view can handle this motion. Otherwise \b
     * false.
     **/
    final boolean handle_actionMove(int pointX, int pointY)
    {
        int deltaX = (int)(m_lastMotionX - pointX);
        int deltaY = (int)(m_lastMotionY - pointY);

        /* Start scrolling the view only if passed the minimum amount. When we
         * are already scrolling, make it smooth.
         */
        if ((Math.abs(deltaX) > m_touchSlop) || m_scrolling)
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

        int velocityX = 0, minX, maxX, offsetX = getScrollX();
        int childWidth = getChildWidth();

        minX = 0;
        maxX = getChildWidth() * (getChildCount() - 1);

        if (offsetX < minX)
        {
            m_scroller.startScroll(offsetX, 0, (minX - offsetX), 0, 500);
            issuer.post(this, IMSG.MSG_DELAY, NP_SCROLL, 0L, null);
        }
        else if (offsetX > maxX)
        {
            m_scroller.startScroll(offsetX, 0, (maxX - offsetX), 0, 500);
            issuer.post(this, IMSG.MSG_DELAY, NP_SCROLL, 0L, null);
        }
        else
        {
            /* Compute velocity in pixels/second. */
            m_velocityTracker.computeCurrentVelocity(1000);
            velocityX = (int)m_velocityTracker.getXVelocity();

            minX = Math.max(minX, (getChildWidth() * (m_currentIndex - 1)));
            maxX = Math.min(maxX, (getChildWidth() * (m_currentIndex + 1)));
            m_scroller.fling(offsetX, 0, -velocityX, 0, minX, maxX, 0, 0);
            issuer.post(this, IMSG.MSG_DELAY, NP_FLING, 0L, null);
        }
//        debug.w("CAndroidPagerView::handle_actionUp() \n" +
//                "==> minX.....: %d\n" +
//                "==> maxX.....: %d\n" +
//                "==> velocityX: %d\n" +
//                "==> currentX.: %d\n", minX, maxX, -velocityX, getScrollX());
        m_velocityTracker.recycle();
        m_velocityTracker = null;
        m_scrolling = false;
        return true;
    }/*}}}*/
    // final boolean handle_actionCancel();/*{{{*/
    /**
     * Cancels the scrolling operation.
     * @return This function always returns \b true.
     **/
    final boolean handle_actionCancel()
    {
        m_scroller.abortAnimation();    /* If one was started. */
        if (m_velocityTracker != null)
            m_velocityTracker.recycle();
        m_velocityTracker = null;
        m_scrolling = false;

        /* Restore the last position. */
        setCurrentView(m_currentIndex, false);

        return true;
    }/*}}}*/
    //@}

    /** \name Data Members */ //@{
    private VelocityTracker  m_velocityTracker;
    private OverScroller     m_scroller;
    private float            m_lastMotionX;
    private float            m_lastMotionY;
    private boolean          m_scrollEnabled;
    private boolean          m_scrolling;
    private boolean          m_skipScroll;
    private int m_currentIndex;
    private int m_touchSlop;
    //@}

    /** \name Constants */ //@{
    private static final int SNAP_VELOCITY = 1000;
    private static final int NP_FLING      = 1;
    private static final int NP_SCROLL     = 0;
    private static final int NP_VIEW       = -1;
    //@}
}
// vim:syntax=java.doxygen
