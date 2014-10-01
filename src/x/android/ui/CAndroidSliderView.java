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
/* #imports {{{ */
import android.app.*;
import android.content.*;
import android.content.res.*;

import android.util.AttributeSet;

import android.view.*;
import android.widget.*;
import android.view.animation.*;

import x.android.defs.*;
import x.android.utils.*;
import x.android.nms.*;

/* }}} #imports */
/**
 * \ingroup x_android_ui
 * A slider view that provides functionality resemble SlidingDrawer.
 * This class builds a ViewGroup that must have only three Views or ViewGroups
 * as children. The top most View/ViewGroup will be the content View and will
 * be slide in the group. The second/bottom View/ViewGroup will be revealed
 * when the top View slides down.
 *
 * To slide the View the user will use a handle. A small Button or ImageView
 * where this ViewGroup will check for touches. The handle will be aligned at
 * the top center edge. It must be the third View in this group and will be
 * controled by this class.
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
        _internal_init();
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
        _internal_init();
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
        _internal_init();
    }/*}}}*/
    //@}

    /** \name Attributes */ //@{
    // public final CAndroidSliderView setHandlerView(View handlerView);/*{{{*/
    /**
     * Sets or changes the View used as handler of the slider.
     * The View must be placed at this parent top edge.
     * @param handlerView View used as slider handler.
     * @returns This function returns \b this.
     **/
    public final CAndroidSliderView setHandlerView(View handlerView)
    {
        m_handlerView = handlerView;
        debug.w("CAndroidSliderView::setHandlerView(%d)\n", indexOfChild(handlerView));
        return this;
    }/*}}}*/
    // public final CAndroidSliderView setContentView(View contentView);/*{{{*/
    /**
     * Sets or changes the content view.
     * @param contentView The view that will fullfil the entire client area.
     * @returns This function returns \b this.
     **/
    public final CAndroidSliderView setContentView(View contentView) {
        m_contentView = contentView;
        debug.w("CAndroidSliderView::setContentView(%d)\n", indexOfChild(contentView));
        return this;
    }/*}}}*/
    // public final CAndroidSliderView setHandlerView(int viewID);/*{{{*/
    /**
     * Sets or changes the View used as handler of the slider.
     * The View must be placed at this parent top edge. This function is
     * useful when this View is loaded from an XML resource.
     * @param viewID The handler View identifier.
     * @returns This function returns \b this.
     **/
    public final CAndroidSliderView setHandlerView(int viewID) {
        return setHandlerView(findViewById(viewID));
    }/*}}}*/
    // public final CAndroidSliderView setContentView(int viewID);/*{{{*/
    /**
     * Sets or changes the content view.
     * This function is useful when this View is loaded from an XML resource.
     * @param viewID The content View identifier.
     * @returns This function returns \b this.
     **/
    public final CAndroidSliderView setContentView(int viewID) {
        return setContentView(findViewById(viewID));
    }/*}}}*/
    // public final View getHandlerView();/*{{{*/
    /**
     * Retrieves the defined handler View.
     * @return The View reference. \b null if not set yet.
     **/
    public final View getHandlerView() {
        return m_handlerView;
    }/*}}}*/
    // public final View getContentView();/*{{{*/
    /**
     * Retrieves the content View.
     * @returns The content View reference or \b null if not set yet.
     **/
    public final View getContentView() {
        return m_contentView;
    }/*}}}*/
    //@}

    /** \name ViewGroup: Overrides */ //@{
    // public boolean onInterceptTouchEvent(MotionEvent event);/*{{{*/
    /**
     * Intercepts all touch events to this View hierarchy.
     * @param event The event to handle.
     * @return \b true if the sequence of events are handles by this object.
     * Otherwise \b false.
     **/
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event)
    {
        if ((event.getAction() == MotionEvent.ACTION_DOWN) && (m_handlerView != null))
        {
            CRect handlerRect = CRect.ViewRect(m_handlerView);

            if (handlerRect.hasPoint((int)event.getX(), (int)event.getY()))
            {
                m_lastMotionY = (int)event.getY();
                m_slideState  = TOUCH_CAPTURED;
                return true;
            }
        }
        return false;
    }/*}}}*/
    //@}

    /** \name View: Overrides */ //@{
    // public boolean onTouchEvent(MotionEvent event);/*{{{*/
    /**
     * Handles touches on this View.
     * @param event The event to handle.
     * @returns \b true if the event was handled. \b false otherwise.
     **/
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (m_slideState != SLIDE_RESTING)
        {
            int action = event.getAction();

            if (m_tracker == null) m_tracker = VelocityTracker.obtain();
            m_tracker.addMovement(event);

            switch (action)
            {
            case MotionEvent.ACTION_MOVE:
                _internal_actionMove((int)event.getY());
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                _internal_actionEnd((int)event.getY());
                break;
            }
            return true;
        }
        return false;
    }/*}}}*/
    // protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)/*{{{*/
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
        int wSize = MeasureSpec.getSize(widthMeasureSpec);
        int hSize = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(wSize, hSize);
    }/*}}}*/
    // protected void onLayout(boolean changed, int left, int top, int right, int bottom);/*{{{*/
    /**
     * Called to set size and position of each children.
     * @param changed \b true when this is a new size and position for this
     * View. Otherwise \b false.
     * @param left The X coordinate, relative to parent View.
     * @param top The Y coordinate, relative to parent View.
     * @param right The end X coordinate, relative to parent View.
     * @param bottom The end Y coordinate, relative to parent View.
     **/
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        CRect rect, rctl = new CRect();

        rect = new CRect();
        rect.box(0, 0, (right - left), (bottom - top));
        rect.deflate(getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom());

        final int limit = getChildCount();
        View childView;

        for (int i = 0; i < limit; i++)
        {
            childView = getChildAt(i);
            if (childView.getVisibility() != View.GONE)
            {
                if (childView == m_handlerView)
                {
                    rctl = CRect.MeasuredRect(childView, rctl);
                    rctl.align(rect, ALIGN.TOP | ALIGN.HCENTER, null);
                    rctl.moveTo(rctl.left, m_currentY);
                    rctl.setViewRect(childView);
                }
                else if (childView == m_contentView)
                {
                    rctl.set(rect);
                    rctl.moveTo(rect.left, m_currentY);
                    rctl.setViewRect(childView);
                }
                else
                {
                    rctl.set(rect);
                    rctl.setViewRect(childView);
                }
            }
        }
    }/*}}}*/
    // protected void onSizeChanged(int w, int h, int oldw, int oldh);/*{{{*/
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

        if (m_slideState != SLIDE_RESTING) return;

        w = w - (getPaddingLeft() + getPaddingRight());
        h = h - (getPaddingTop() + getPaddingBottom());

        final int limit = getChildCount();
        View childView;

        for (int i = 0; i < limit; i++)
        {
            childView = getChildAt(i);
            if (childView == m_handlerView)
            {
                childView.measure(MeasureSpec.makeMeasureSpec(w, MeasureSpec.AT_MOST),
                                  MeasureSpec.makeMeasureSpec(h, MeasureSpec.AT_MOST));
            }
            else
            {
                childView.measure(MeasureSpec.makeMeasureSpec(w, MeasureSpec.EXACTLY),
                                  MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY));
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
        if (msgID == IMSG.MSG_DELAY)
        {
            boolean stillScrolling = m_scroller.computeScrollOffset();
            int deltaY = (m_scroller.getCurrY() - m_currentY);

//            debug.w("CAndroidSliderView::onMessage()\n" +
//                    "==> m_currentY: %d\n" +
//                    "==> getCurrY(): %d\n" +
//                    "==> deltaY....: %d\n",
//                    m_currentY, m_scroller.getCurrY(), deltaY);

            _internal_offsetViews(deltaY);

            if (stillScrolling)
                issuer.post(this, IMSG.MSG_DELAY, nParam, 0L, null);
            else if (nParam == NP_FLING)
            {
                debug.w("CAndroidSliderView::NP_FLING ENDED! ========================================= ||\n");
                int targetY;
                int averageY = (m_contentView.getHeight() / 2);

                if (m_currentY > averageY)
                    targetY = (m_contentView.getHeight() - m_handlerView.getHeight());
                else
                    targetY = 0;

                m_scroller.startScroll(0, m_currentY, 0, (targetY - m_currentY), 300);
                issuer.post(this, IMSG.MSG_DELAY, NP_SLIDE, 0L, null);
            }
            return true;
        }
        return false;
    }/*}}}*/
    //@}

    /** \name Local Implementation */ //@{
    // private void _internal_init();/*{{{*/
    /**
     * Initializes the internal data.
     **/
    private void _internal_init()
    {
        DecelerateInterpolator di = new DecelerateInterpolator(1.0f);
        m_scroller    = new OverScroller(getContext(), di);
        m_tracker     = null;
        m_handlerView = null;
        m_contentView = null;
        m_lastMotionY = 0;
        m_currentY    = 0;

        ViewConfiguration vc = ViewConfiguration.get( getContext() );
        m_touchSlop = vc.getScaledTouchSlop();

//        m_scroller.setFriction(vc.getScrollFriction() * 10.0f);
    }/*}}}*/
    // private void _internal_actionMove(int pointY);/*{{{*/
    /**
     * Intercepts the \b ACTION_MOVE of \c MotionEvent event.
     * @param pointY The Y coordinate of the motion.
     **/
    private void _internal_actionMove(int pointY)
    {
        int deltaY = (pointY - m_lastMotionY);

        /* Start sliding only if the movement superseds the slop distance.
         * After that, make movement smooth.
         */
        if ((Math.abs(deltaY) > m_touchSlop) || (m_slideState == SLIDE_MOVING))
        {
            _internal_offsetViews(deltaY);
            m_slideState  = SLIDE_MOVING;
            m_lastMotionY = pointY;
        }
    }/*}}}*/
    // private void _internal_actionEnd(int pointY);/*{{{*/
    /**
     * Finishes the sliding.
     * This is called in \c onTouchEvent() when the action is \c ACTION_UP or
     * \c ACTION_CANCEL.
     * @param pointY The final Y position of the pointer.
     **/
    private void _internal_actionEnd(int pointY)
    {
        if (m_slideState == SLIDE_MOVING)
        {
            int deltaY = (pointY - m_lastMotionY);

            _internal_offsetViews(deltaY);

            /* Setting the limits for the movement. */
            int minY = 0;
            int maxY = (m_contentView.getHeight() - m_handlerView.getHeight());

            if (m_currentY > maxY)      /* We pass the bottom limit. */
            {
                m_scroller.startScroll(0, m_currentY, 0, (maxY - m_currentY), 300);
                issuer.post(this, IMSG.MSG_DELAY, NP_SLIDE, 0L, null);
            }
            else if (m_currentY < minY) /* We pass the upper limit. */
            {
                m_scroller.startScroll(0, m_currentY, 0, -m_currentY, 300);
                issuer.post(this, IMSG.MSG_DELAY, NP_SLIDE, 0L, null);
            }
            else
            {
                int velocity;
                
                /* Compute velocity in pixels per second. */
                m_tracker.computeCurrentVelocity(1000);
                velocity = (int)m_tracker.getYVelocity();

//                debug.w("CAndroidSliderView::_internal_actionEnd()\n" +
//                        "==> m_currentY: %d\n" +
//                        "==> minY......: %d\n" +
//                        "==> maxY......: %d\n" +
//                        "==> velocity..: %d\n", m_currentY, minY, maxY, velocity);

                /* NOTE: The fling is starting too much slower than the
                 * movement of the user. So we are cutting it in the half so
                 * its ends earlier and we can take the scroll again to reach
                 * the final position.
                 */
                m_scroller.fling(0, m_currentY, 0, velocity, 0, 0, minY, maxY);
                issuer.post(this, IMSG.MSG_DELAY, NP_FLING, 0L, null);
            }

            m_tracker.recycle();
            m_tracker = null;
            m_slideState = SLIDE_RESTING;
        }
    }/*}}}*/
    // private void _internal_offsetViews(int offsetY);/*{{{*/
    /**
     * Apply the offset position to all sliding Views.
     * @param offsetY The offset to apply.
     **/
    private void _internal_offsetViews(int offsetY)
    {
        m_currentY = (m_currentY + offsetY);
        if (m_contentView != null)
            m_contentView.offsetTopAndBottom(offsetY);

        if (m_handlerView != null)
            m_handlerView.offsetTopAndBottom(offsetY);

        postInvalidate();
    }/*}}}*/
    //@}

    /** \name Data Members */ //@{
    private VelocityTracker m_tracker;  /**< Help us with fling movement.   */
    private OverScroller m_scroller;    /**< Scroller to help in animations.*/
    private View m_handlerView;         /**< Handler for the user motions.  */
    private View m_contentView;         /**< Content of this group.         */
    private int  m_lastMotionY;         /**< Holds the last Y coords.       */
    private int  m_currentY;            /**< The current Y coordinate.      */
    private int  m_touchSlop;           /**< Slop distance to start move.   */
    private int  m_slideState;          /**< Current slide state.           */
    //@}

    /** \name Constants */ //@{
    private static final int SLIDE_RESTING  = 0;    /**< Not sliding.       */
    private static final int TOUCH_CAPTURED = 1;    /**< Touch point was captured. */
    private static final int SLIDE_MOVING   = 2;    /**< We are moving.     */
    private static final int NP_SLIDE       = 1;    /**< When scrolling.    */
    private static final int NP_FLING       = 2;    /**< When flinging.     */
    //@}

}
// vim:syntax=java.doxygen
