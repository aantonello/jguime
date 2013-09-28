/**
 * \file
 * Defines the CAndroidScrollView class.
 *
 * \author Alessandro Antonello <aantonello@paralaxe.com.br>
 * \date   Dezembro 07, 2012
 *
 * \par Copyright
 * This file is provided in hope that it will be useful to someone. It is
 * offered in public domain. You may use, modify or distribute it freely.
 *
 * The code is provided "AS IS". There is no warranty at all, of any kind. You
 * may change it if you like. Or just use it as it is.
 */
package x.android.ui;

import android.annotation.Widget;

import android.content.Context;
import android.content.res.TypedArray;

import android.graphics.Rect;

import android.util.AttributeSet;
import android.util.Log;

import android.view.ContextMenu.ContextMenuInfo;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.HapticFeedbackConstants;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import android.view.animation.Transformation;

import com.android.internal.R;

/**
 * A horizontal scroll view with facilities.
 * The default values for the Gallery assume you will be using
 * android.R.styleable#Theme_galleryItemBackground as the background for
 * each View given to the Gallery from the Adapter. If you are not doing this,
 * you may need to adjust some Gallery properties, such as the spacing.
 *
 * Views given to the Gallery should use Gallery.LayoutParams as their
 * layout parameters type.
 *
 * See the <a href="{@docRoot}resources/tutorials/views/hello-gallery.html">Gallery
 * tutorial</a>.
 * 
 * \ref android.R.styleable#Gallery_animationDuration
 * \ref android.R.styleable#Gallery_spacing
 * \ref android.R.styleable#Gallery_gravity
 *//* --------------------------------------------------------------------- */
public class CAndroidScrollView extends AbsSpinner
    implements GestureDetector.OnGestureListener
{
    /** \name CONSTRUCTOR */ //@{
    // public CAndroidScrollView(Context context);/*{{{*/
    /**
     * Default Constructor.
     **/
    public CAndroidScrollView(Context context) {
        this(context, null);
    }/*}}}*/
    // public CAndroidScrollView(Context context, AttributeSet attrs);/*{{{*/
    /**
     * Inflater constructor.
     **/
    public CAndroidScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.galleryStyle);
    }/*}}}*/
    // public CAndroidScrollView(Context context, AttributeSet attrs, int defStyle);/*{{{*/
    /**
     * Custom constructor.
     **/
    public CAndroidScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        
        m_gestureDetector = new GestureDetector(context, this);
        m_gestureDetector.setIsLongpressEnabled(true);
        
        TypedArray a = context.obtainStyledAttributes(
                attrs, com.android.internal.R.styleable.Gallery, defStyle, 0);

        int index = a.getInt(com.android.internal.R.styleable.Gallery_gravity, -1);
        if (index >= 0) {
            setGravity(index);
        }

        int animationDuration =
                a.getInt(com.android.internal.R.styleable.Gallery_animationDuration, -1);
        if (animationDuration > 0) {
            setAnimationDuration(animationDuration);
        }

        int spacing =
                a.getDimensionPixelOffset(com.android.internal.R.styleable.Gallery_spacing, 0);
        setSpacing(spacing);

        float unselectedAlpha = a.getFloat(
                com.android.internal.R.styleable.Gallery_unselectedAlpha, 0.5f);
        setUnselectedAlpha(unselectedAlpha);
        
        a.recycle();

        // We draw the selected item last (because otherwise the item to the
        // right overlaps it)
        mGroupFlags |= FLAG_USE_CHILD_DRAWING_ORDER;
        
        mGroupFlags |= FLAG_SUPPORT_STATIC_TRANSFORMATIONS;
    }/*}}}*/
    //@}

    /** \name OPERATIONS */ //@{
    // public void setCallbackDuringFling(boolean shouldCallback);/*{{{*/
    /**
     * Whether or not to callback on any #getOnItemSelectedListener()
     * while the items are being flinged.
     * If false, only the final selected item
     * will cause the callback. If true, all items between the first and the
     * final will cause callbacks.
     * 
     * @param shouldCallback Whether or not to callback on the listener while
     *            the items are being flinged.
     */
    public void setCallbackDuringFling(boolean shouldCallback) {
        m_shouldCallbackDuringFling = shouldCallback;
    }/*}}}*/
    // public void setCallbackOnUnselectedItemClick(boolean shouldCallback);/*{{{*/
    /**
     * Whether or not to callback when an item that is not selected is clicked.
     * If false, the item will become selected (and re-centered). If true, the
     * #getOnItemClickListener() will get the callback.
     * 
     * @param shouldCallback Whether or not to callback on the listener when a
     *            item that is not selected is clicked.
     */
    public void setCallbackOnUnselectedItemClick(boolean shouldCallback) {
        m_shouldCallbackonUnselectedItemClick = shouldCallback;
    }/*}}}*/
    // public void setAnimationDuration(int animationDurationMillis);/*{{{*/
    /**
     * Sets how long the transition animation should run when a child view
     * changes position.
     * Only relevant if animation is turned on.
     * 
     * @param animationDurationMillis The duration of the transition, in
     *        milliseconds.
     * 
     * \ref android.R.styleable#Gallery_animationDuration
     */
    public void setAnimationDuration(int animationDurationMillis) {
        m_animationDuration = animationDurationMillis;
    }/*}}}*/
    // public void setSpacing(int spacing);/*{{{*/
    /**
     * Sets the spacing between items in a Gallery
     * 
     * @param spacing The spacing in pixels between items in the Gallery
     * 
     * @attr ref android.R.styleable#Gallery_spacing
     */
    public void setSpacing(int spacing) {
        m_spacing = spacing;
    }/*}}}*/
    public void setUnselectedAlpha(float unselectedAlpha);/*{{{*/
    /**
     * Sets the alpha of items that are not selected in the Gallery.
     * 
     * @param unselectedAlpha the alpha for the items that are not selected.
     * 
     * @attr ref android.R.styleable#Gallery_unselectedAlpha
     */
    public void setUnselectedAlpha(float unselectedAlpha) {
        m_unselectedAlpha = unselectedAlpha;
    }/*}}}*/
    // public void setGravity(int gravity);/*{{{*/
    /**
     * Describes how the child views are aligned.
     * @param gravity
     * 
     * @attr ref android.R.styleable#Gallery_gravity
     */
    public void setGravity(int gravity)
    {
        if (m_gravity != gravity) {
            m_gravity = gravity;
            requestLayout();
        }
    }/*}}}*/
    //@}

    /** \name OVERRIDES */ //@{
    // protected boolean getChildStaticTransformation(View child, Transformation t);/*{{{*/
    @Override
    protected boolean getChildStaticTransformation(View child, Transformation t) {
        
        t.clear();
        t.setAlpha(child == m_selectedChild ? 1.0f : m_unselectedAlpha);
        
        return true;
    }/*}}}*/
    // protected int computeHorizontalScrollExtent();/*{{{*/
    @Override
    protected int computeHorizontalScrollExtent() {
        // Only 1 item is considered to be selected
        return 1;
    }/*}}}*/
    // protected int computeHorizontalScrollOffset();/*{{{*/
    @Override
    protected int computeHorizontalScrollOffset() {
        // Current scroll position is the same as the selected position
        return mSelectedPosition;
    }/*}}}*/
    // protected int computeHorizontalScrollRange();/*{{{*/
    @Override
    protected int computeHorizontalScrollRange() {
        // Scroll range is the same as the item count
        return mItemCount;
    }/*}}}*/
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p);/*{{{*/
    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }/*}}}*/
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p);/*{{{*/
    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }/*}}}*/
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs);/*{{{*/
    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }/*}}}*/
    // protected ViewGroup.LayoutParams generateDefaultLayoutParams();/*{{{*/
    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        /*
         * CAndroidScrollView expects CAndroidScrollView.LayoutParams.
         */
        return new CAndroidScrollView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }/*}}}*/
    // protected void onLayout(boolean changed, int l, int t, int r, int b);/*{{{*/
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        
        /*
         * Remember that we are in layout to prevent more layout request from
         * being generated.
         */
        mInLayout = true;
        layout(0, false);
        mInLayout = false;
    }/*}}}*/
    // int getChildHeight(View child);/*{{{*/
    @Override
    int getChildHeight(View child) {
        return child.getMeasuredHeight();
    }/*}}}*/
    // void selectionChanged();/*{{{*/
    @Override
    void selectionChanged() {
        if (!m_supressSelectionChanged) {
            super.selectionChanged();
        }
    }/*}}}*/
    // void layout(int delta, boolean animate);/*{{{*/
    /**
     * Creates and positions all views for this Gallery.
     * We layout rarely, most of the time #trackMotionScroll(int) takes
     * care of repositioning, adding, and removing children.
     * 
     * @param delta Change in the selected position. +1 means the selection is
     *            moving to the right, so views are scrolling to the left. -1
     *            means the selection is moving to the left.
     */
    @Override
    void layout(int delta, boolean animate) {

        m_isRtl = isLayoutRtl();

        int childrenLeft = mSpinnerPadding.left;
        int childrenWidth = mRight - mLeft - mSpinnerPadding.left - mSpinnerPadding.right;

        if (mDataChanged) {
            handleDataChanged();
        }

        // Handle an empty gallery by removing all views.
        if (mItemCount == 0) {
            resetList();
            return;
        }

        // Update to the new selected position.
        if (mNextSelectedPosition >= 0) {
            setSelectedPositionInt(mNextSelectedPosition);
        }

        // All views go in recycler while we are in layout
        recycleAllViews();

        // Clear out old views
        //removeAllViewsInLayout();
        detachAllViewsFromParent();

        /*
         * These will be used to give initial positions to views entering the
         * gallery as we scroll
         */
        m_rightMost = 0;
        m_leftMost = 0;

        // Make selected view and center it
        
        /*
         * mFirstPosition will be decreased as we add views to the left later
         * on. The 0 for x will be offset in a couple lines down.
         */  
        mFirstPosition = mSelectedPosition;
        View sel = makeAndAddView(mSelectedPosition, 0, 0, true);
        
        // Put the selected child in the center
        int selectedOffset = childrenLeft + (childrenWidth / 2) - (sel.getWidth() / 2);
        sel.offsetLeftAndRight(selectedOffset);

        fillToGalleryRight();
        fillToGalleryLeft();
        
        // Flush any cached views that did not get reused above
        mRecycler.clear();

        invalidate();
        checkSelectionChanged();

        mDataChanged = false;
        mNeedSync = false;
        setNextSelectedPositionInt(mSelectedPosition);
        
        updateSelectedItemMetadata();
    }/*}}}*/
    // public boolean onTouchEvent(MotionEvent event);/*{{{*/
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // Give everything to the gesture detector
        boolean retValue = m_gestureDetector.onTouchEvent(event);

        int action = event.getAction();
        if (action == MotionEvent.ACTION_UP) {
            // Helper method for lifted finger
            onUp();
        } else if (action == MotionEvent.ACTION_CANCEL) {
            onCancel();
        }
        
        return retValue;
    }/*}}}*/
    // public boolean onSingleTapUp(MotionEvent e);/*{{{*/
    @Override
    public boolean onSingleTapUp(MotionEvent e) {

        if (m_downTouchPosition >= 0) {
            
            // An item tap should make it selected, so scroll to this child.
            scrollToChild(m_downTouchPosition - mFirstPosition);

            // Also pass the click so the client knows, if it wants to.
            if (m_shouldCallbackonUnselectedItemClick || m_downTouchPosition == mSelectedPosition) {
                performItemClick(m_downTouchView, m_downTouchPosition, mAdapter
                        .getItemId(m_downTouchPosition));
            }
            
            return true;
        }
        
        return false;
    }/*}}}*/
    // public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY);/*{{{*/
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        
        if (!m_shouldCallbackDuringFling) {
            // We want to suppress selection changes
            
            // Remove any future code to set m_supressSelectionChanged = false
            removeCallbacks(m_disableSupressSelectionChangedRunnable);

            // This will get reset once we scroll into slots
            if (!m_supressSelectionChanged) m_supressSelectionChanged = true;
        }
        
        // Fling the gallery!
        m_flingRunnable.startUsingVelocity((int) -velocityX);
        
        return true;
    }/*}}}*/
    // public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY);/*{{{*/
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
    {
        debug.w("CAndroidScrollView::onScroll(%d)\n", e2.getX() - e1.getX());
        /*
         * Now's a good time to tell our parent to stop intercepting our events!
         * The user has moved more than the slop amount, since GestureDetector
         * ensures this before calling this method. Also, if a parent is more
         * interested in this touch's events than we are, it would have
         * intercepted them by now (for example, we can assume when a Gallery is
         * in the ListView, a vertical scroll would not end up in this method
         * since a ListView would have intercepted it by now).
         */
        mParent.requestDisallowInterceptTouchEvent(true);
        
        // As the user scrolls, we want to callback selection changes so related-
        // info on the screen is up-to-date with the gallery's selection
        if (!m_shouldCallbackDuringFling) {
            if (m_isFirstScroll) {
                /*
                 * We're not notifying the client of selection changes during
                 * the fling, and this scroll could possibly be a fling. Don't
                 * do selection changes until we're sure it is not a fling.
                 */
                if (!m_supressSelectionChanged) m_supressSelectionChanged = true;
                postDelayed(m_disableSupressSelectionChangedRunnable, SCROLL_TO_FLING_UNCERTAINTY_TIMEOUT);
            }
        } else {
            if (m_supressSelectionChanged) m_supressSelectionChanged = false;
        }
        
        // Track the motion
        trackMotionScroll(-1 * (int) distanceX);
       
        m_isFirstScroll = false;
        return true;
    }/*}}}*/
    // public boolean onDown(MotionEvent e);/*{{{*/
    @Override
    public boolean onDown(MotionEvent e) {

        // Kill any existing fling/scroll
        m_flingRunnable.stop(false);

        // Get the item's view that was touched
        m_downTouchPosition = pointToPosition((int) e.getX(), (int) e.getY());
        
        if (m_downTouchPosition >= 0) {
            m_downTouchView = getChildAt(m_downTouchPosition - mFirstPosition);
            m_downTouchView.setPressed(true);
        }
        
        // Reset the multiple-scroll tracking state
        m_isFirstScroll = true;
        
        // Must return true to get matching events for this down event.
        return true;
    }/*}}}*/
    // public void onLongPress(MotionEvent e);/*{{{*/
    @Override
    public void onLongPress(MotionEvent e) {
        
        if (m_downTouchPosition < 0) {
            return;
        }
        
        performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
        long id = getItemIdAtPosition(m_downTouchPosition);
        dispatchLongPress(m_downTouchView, m_downTouchPosition, id);
    }/*}}}*/
    // public void onShowPress(MotionEvent e);/*{{{*/
    /**
     * Unused methods from GestureDetector.OnGestureListener below.
     **/
    @Override
    public void onShowPress(MotionEvent e) {
    }/*}}}*/
    // public void dispatchSetSelected(boolean selected);/*{{{*/
    @Override
    public void dispatchSetSelected(boolean selected) {
        /*
         * We don't want to pass the selected state given from its parent to its
         * children since this widget itself has a selected state to give to its
         * children.
         */
    }/*}}}*/
    // protected void dispatchSetPressed(boolean pressed);/*{{{*/
    @Override
    protected void dispatchSetPressed(boolean pressed) {
        
        // Show the pressed state on the selected child
        if (m_selectedChild != null) {
            m_selectedChild.setPressed(pressed);
        }
    }/*}}}*/
    // protected ContextMenuInfo getContextMenuInfo();/*{{{*/
    @Override
    protected ContextMenuInfo getContextMenuInfo() {
        return m_contextMenuInfo;
    }/*}}}*/
    // public boolean showContextMenuForChild(View originalView);/*{{{*/
    @Override
    public boolean showContextMenuForChild(View originalView) {

        final int longPressPosition = getPositionForView(originalView);
        if (longPressPosition < 0) {
            return false;
        }
        
        final long longPressId = mAdapter.getItemId(longPressPosition);
        return dispatchLongPress(originalView, longPressPosition, longPressId);
    }/*}}}*/
    // public boolean showContextMenu();/*{{{*/
    @Override
    public boolean showContextMenu() {
        
        if (isPressed() && mSelectedPosition >= 0) {
            int index = mSelectedPosition - mFirstPosition;
            View v = getChildAt(index);
            return dispatchLongPress(v, mSelectedPosition, mSelectedRowId);
        }        
        
        return false;
    }/*}}}*/
    // public boolean dispatchKeyEvent(KeyEvent event);/*{{{*/
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // Gallery steals all key events
        return event.dispatch(this, null, null);
    }/*}}}*/
    // public boolean onKeyDown(int keyCode, KeyEvent event);/*{{{*/
    /**
     * Handles left, right, and clicking
     * @see android.view.View#onKeyDown
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            
        case KeyEvent.KEYCODE_DPAD_LEFT:
            if (movePrevious()) {
                playSoundEffect(SoundEffectConstants.NAVIGATION_LEFT);
            }
            return true;

        case KeyEvent.KEYCODE_DPAD_RIGHT:
            if (moveNext()) {
                playSoundEffect(SoundEffectConstants.NAVIGATION_RIGHT);
            }
            return true;

        case KeyEvent.KEYCODE_DPAD_CENTER:
        case KeyEvent.KEYCODE_ENTER:
            m_receivedInvokeKeyDown = true;
            // fallthrough to default handling
        }
        
        return super.onKeyDown(keyCode, event);
    }/*}}}*/
    // public boolean onKeyUp(int keyCode, KeyEvent event);/*{{{*/
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
        case KeyEvent.KEYCODE_DPAD_CENTER:
        case KeyEvent.KEYCODE_ENTER: {
            
            if (m_receivedInvokeKeyDown) {
                if (mItemCount > 0) {
    
                    dispatchPress(m_selectedChild);
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dispatchUnpress();
                        }
                    }, ViewConfiguration.getPressedStateDuration());
    
                    int selectedIndex = mSelectedPosition - mFirstPosition;
                    performItemClick(getChildAt(selectedIndex), mSelectedPosition, mAdapter
                            .getItemId(mSelectedPosition));
                }
            }
            
            // Clear the flag
            m_receivedInvokeKeyDown = false;
            
            return true;
        }
        }

        return super.onKeyUp(keyCode, event);
    }/*}}}*/
    // void setSelectedPositionInt(int position);/*{{{*/
    @Override
    void setSelectedPositionInt(int position) {
        super.setSelectedPositionInt(position);

        // Updates any metadata we keep about the selected item.
        updateSelectedItemMetadata();
    }/*}}}*/
    // protected int getChildDrawingOrder(int childCount, int i);/*{{{*/
    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        int selectedIndex = mSelectedPosition - mFirstPosition;
        
        // Just to be safe
        if (selectedIndex < 0) return i;
        
        if (i == childCount - 1) {
            // Draw the selected child last
            return selectedIndex;
        } else if (i >= selectedIndex) {
            // Move the children after the selected child earlier one
            return i + 1;
        } else {
            // Keep the children before the selected child the same
            return i;
        }
    }/*}}}*/
    // protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect);/*{{{*/
    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        
        /*
         * The gallery shows focus by focusing the selected item. So, give
         * focus to our selected item instead. We steal keys from our
         * selected item elsewhere.
         */
        if (gainFocus && m_selectedChild != null) {
            m_selectedChild.requestFocus(direction);
            m_selectedChild.setSelected(true);
        }

    }/*}}}*/
    //@}

    /** \name LOCAL FUNCTIONS */ //@{
    // void trackMotionScroll(int deltaX);/*{{{*/
    /**
     * Tracks a motion scroll. In reality, this is used to do just about any
     * movement to items (touch scroll, arrow-key scroll, set an item as selected).
     * 
     * @param deltaX Change in X from the previous event.
     */
    void trackMotionScroll(int deltaX) {

        if (getChildCount() == 0) {
            return;
        }
        
        boolean toLeft = deltaX < 0; 
        
        int limitedDeltaX = getLimitedMotionScrollAmount(toLeft, deltaX);
        if (limitedDeltaX != deltaX) {
            // The above call returned a limited amount, so stop any scrolls/flings
            m_flingRunnable.endFling(false);
            onFinishedMovement();
        }
        
        offsetChildrenLeftAndRight(limitedDeltaX);
        
        detachOffScreenChildren(toLeft);
        
        if (toLeft) {
            // If moved left, there will be empty space on the right
            fillToGalleryRight();
        } else {
            // Similarly, empty space on the left
            fillToGalleryLeft();
        }
        
        // Clear unused views
        mRecycler.clear();
        
        setSelectionToCenterChild();

        onScrollChanged(0, 0, 0, 0); // dummy values, View's implementation does not use these.

        invalidate();
    }/*}}}*/
    // int getLimitedMotionScrollAmount(boolean motionToLeft, int deltaX);/*{{{*/
    int getLimitedMotionScrollAmount(boolean motionToLeft, int deltaX) {
        int extremeItemPosition = motionToLeft != m_isRtl ? mItemCount - 1 : 0;
        View extremeChild = getChildAt(extremeItemPosition - mFirstPosition);
        
        if (extremeChild == null) {
            return deltaX;
        }
        
        int extremeChildCenter = getCenterOfView(extremeChild);
        int galleryCenter = getCenterOfGallery();
        
        if (motionToLeft) {
            if (extremeChildCenter <= galleryCenter) {
                
                // The extreme child is past his boundary point!
                return 0;
            }
        } else {
            if (extremeChildCenter >= galleryCenter) {

                // The extreme child is past his boundary point!
                return 0;
            }
        }
        
        int centerDifference = galleryCenter - extremeChildCenter;

        return motionToLeft
                ? Math.max(centerDifference, deltaX)
                : Math.min(centerDifference, deltaX); 
    }/*}}}*/
    // void onUp();/*{{{*/
    /**
     * Called when a touch event's action is MotionEvent.ACTION_UP.
     */
    void onUp() {
        
        if (m_flingRunnable.mScroller.isFinished()) {
            scrollIntoSlots();
        }
        
        dispatchUnpress();
    }/*}}}*/
    // void onCancel();/*{{{*/
    /**
     * Called when a touch event's action is MotionEvent.ACTION_CANCEL.
     */
    void onCancel() {
        onUp();
    }/*}}}*/
    // boolean movePrevious();/*{{{*/
    /**
     * 
     **/
    boolean movePrevious() {
        if (mItemCount > 0 && mSelectedPosition > 0) {
            scrollToChild(mSelectedPosition - mFirstPosition - 1);
            return true;
        } else {
            return false;
        }
    }/*}}}*/
    // boolean moveNext();/*{{{*/
    /**
     * 
     **/
    boolean moveNext() {
        if (mItemCount > 0 && mSelectedPosition < mItemCount - 1) {
            scrollToChild(mSelectedPosition - mFirstPosition + 1);
            return true;
        } else {
            return false;
        }
    }/*}}}*/
    //@}

    /** \name PRIVATE OPERATIONS */ //@{
    // private void offsetChildrenLeftAndRight(int offset);/*{{{*/
    /**
     * Offset the horizontal location of all children of this view by the
     * specified number of pixels.
     * 
     * @param offset the number of pixels to offset
     */
    private void offsetChildrenLeftAndRight(int offset) {
        for (int i = getChildCount() - 1; i >= 0; i--) {
            getChildAt(i).offsetLeftAndRight(offset);
        }
    }/*}}}*/
    // private int getCenterOfGallery();/*{{{*/
    /**
     * @return The center of this Gallery.
     */
    private int getCenterOfGallery() {
        return (getWidth() - mPaddingLeft - mPaddingRight) / 2 + mPaddingLeft;
    }/*}}}*/
    // private static int getCenterOfView(View view);/*{{{*/
    /**
     * @return The center of the given view.
     */
    private static int getCenterOfView(View view) {
        return view.getLeft() + view.getWidth() / 2;
    }/*}}}*/
    // private void detachOffScreenChildren(boolean toLeft);/*{{{*/
    /**
     * Detaches children that are off the screen (i.e.: Gallery bounds).
     * 
     * @param toLeft Whether to detach children to the left of the Gallery, or
     *            to the right.
     */
    private void detachOffScreenChildren(boolean toLeft) {
        int numChildren = getChildCount();
        int firstPosition = mFirstPosition;
        int start = 0;
        int count = 0;

        if (toLeft) {
            final int galleryLeft = mPaddingLeft;
            for (int i = 0; i < numChildren; i++) {
                int n = m_isRtl ? (numChildren - 1 - i) : i;
                final View child = getChildAt(n);
                if (child.getRight() >= galleryLeft) {
                    break;
                } else {
                    start = n;
                    count++;
                    mRecycler.put(firstPosition + n, child);
                }
            }
            if (!m_isRtl) {
                start = 0;
            }
        } else {
            final int galleryRight = getWidth() - mPaddingRight;
            for (int i = numChildren - 1; i >= 0; i--) {
                int n = m_isRtl ? numChildren - 1 - i : i;
                final View child = getChildAt(n);
                if (child.getLeft() <= galleryRight) {
                    break;
                } else {
                    start = n;
                    count++;
                    mRecycler.put(firstPosition + n, child);
                }
            }
            if (m_isRtl) {
                start = 0;
            }
        }

        detachViewsFromParent(start, count);
        
        if (toLeft != m_isRtl) {
            mFirstPosition += count;
        }
    }/*}}}*/
    // private void scrollIntoSlots();/*{{{*/
    /**
     * Scrolls the items so that the selected item is in its 'slot' (its center
     * is the gallery's center).
     */
    private void scrollIntoSlots() {
        
        if (getChildCount() == 0 || m_selectedChild == null) return;
        
        int selectedCenter = getCenterOfView(m_selectedChild);
        int targetCenter = getCenterOfGallery();
        
        int scrollAmount = targetCenter - selectedCenter;
        if (scrollAmount != 0) {
            m_flingRunnable.startUsingDistance(scrollAmount);
        } else {
            onFinishedMovement();
        }
    }/*}}}*/
    // private void onFinishedMovement();/*{{{*/
    private void onFinishedMovement() {
        if (m_supressSelectionChanged) {
            m_supressSelectionChanged = false;
            
            // We haven't been callbacking during the fling, so do it now
            super.selectionChanged();
        }
        invalidate();
    }/*}}}*/
    // private void setSelectionToCenterChild();/*{{{*/
    /**
     * Looks for the child that is closest to the center and sets it as the
     * selected child.
     */
    private void setSelectionToCenterChild() {
        
        View selView = m_selectedChild;
        if (m_selectedChild == null) return;
        
        int galleryCenter = getCenterOfGallery();
        
        // Common case where the current selected position is correct
        if (selView.getLeft() <= galleryCenter && selView.getRight() >= galleryCenter) {
            return;
        }
        
        // TODO better search
        int closestEdgeDistance = Integer.MAX_VALUE;
        int newSelectedChildIndex = 0;
        for (int i = getChildCount() - 1; i >= 0; i--) {
            
            View child = getChildAt(i);
            
            if (child.getLeft() <= galleryCenter && child.getRight() >=  galleryCenter) {
                // This child is in the center
                newSelectedChildIndex = i;
                break;
            }
            
            int childClosestEdgeDistance = Math.min(Math.abs(child.getLeft() - galleryCenter),
                    Math.abs(child.getRight() - galleryCenter));
            if (childClosestEdgeDistance < closestEdgeDistance) {
                closestEdgeDistance = childClosestEdgeDistance;
                newSelectedChildIndex = i;
            }
        }
        
        int newPos = mFirstPosition + newSelectedChildIndex;
        
        if (newPos != mSelectedPosition) {
            setSelectedPositionInt(newPos);
            setNextSelectedPositionInt(newPos);
            checkSelectionChanged();
        }
    }/*}}}*/
    // private void fillToGalleryLeft();/*{{{*/
    private void fillToGalleryLeft() {
        if (m_isRtl) {
            fillToGalleryLeftRtl();
        } else {
            fillToGalleryLeftLtr();
        }
    }/*}}}*/
    // private void fillToGalleryLeftRtl();/*{{{*/
    /**
     * 
     **/
    private void fillToGalleryLeftRtl() {
        int itemSpacing = m_spacing;
        int galleryLeft = mPaddingLeft;
        int numChildren = getChildCount();
        int numItems = mItemCount;

        // Set state for initial iteration
        View prevIterationView = getChildAt(numChildren - 1);
        int curPosition;
        int curRightEdge;

        if (prevIterationView != null) {
            curPosition = mFirstPosition + numChildren;
            curRightEdge = prevIterationView.getLeft() - itemSpacing;
        } else {
            // No children available!
            mFirstPosition = curPosition = mItemCount - 1;
            curRightEdge = mRight - mLeft - mPaddingRight;
            m_shouldStopFling = true;
        }

        while (curRightEdge > galleryLeft && curPosition < mItemCount) {
            prevIterationView = makeAndAddView(curPosition, curPosition - mSelectedPosition,
                    curRightEdge, false);

            // Set state for next iteration
            curRightEdge = prevIterationView.getLeft() - itemSpacing;
            curPosition++;
        }
    }/*}}}*/
    // private void fillToGalleryLeftLtr();/*{{{*/
    /**
     * 
     **/
    private void fillToGalleryLeftLtr() {
        int itemSpacing = m_spacing;
        int galleryLeft = mPaddingLeft;
        
        // Set state for initial iteration
        View prevIterationView = getChildAt(0);
        int curPosition;
        int curRightEdge;
        
        if (prevIterationView != null) {
            curPosition = mFirstPosition - 1;
            curRightEdge = prevIterationView.getLeft() - itemSpacing;
        } else {
            // No children available!
            curPosition = 0; 
            curRightEdge = mRight - mLeft - mPaddingRight;
            m_shouldStopFling = true;
        }
                
        while (curRightEdge > galleryLeft && curPosition >= 0) {
            prevIterationView = makeAndAddView(curPosition, curPosition - mSelectedPosition,
                    curRightEdge, false);

            // Remember some state
            mFirstPosition = curPosition;
            
            // Set state for next iteration
            curRightEdge = prevIterationView.getLeft() - itemSpacing;
            curPosition--;
        }
    }/*}}}*/
    // private void fillToGalleryRight();/*{{{*/
    /**
     * 
     **/
    private void fillToGalleryRight() {
        if (m_isRtl) {
            fillToGalleryRightRtl();
        } else {
            fillToGalleryRightLtr();
        }
    }/*}}}*/
    // private void fillToGalleryRightRtl();/*{{{*/
    /**
     * 
     **/
    private void fillToGalleryRightRtl() {
        int itemSpacing = m_spacing;
        int galleryRight = mRight - mLeft - mPaddingRight;

        // Set state for initial iteration
        View prevIterationView = getChildAt(0);
        int curPosition;
        int curLeftEdge;

        if (prevIterationView != null) {
            curPosition = mFirstPosition -1;
            curLeftEdge = prevIterationView.getRight() + itemSpacing;
        } else {
            curPosition = 0;
            curLeftEdge = mPaddingLeft;
            m_shouldStopFling = true;
        }

        while (curLeftEdge < galleryRight && curPosition >= 0) {
            prevIterationView = makeAndAddView(curPosition, curPosition - mSelectedPosition,
                    curLeftEdge, true);

            // Remember some state
            mFirstPosition = curPosition;

            // Set state for next iteration
            curLeftEdge = prevIterationView.getRight() + itemSpacing;
            curPosition--;
        }
    }/*}}}*/
    // private void fillToGalleryRightLtr();/*{{{*/
    /**
     * 
     **/
    private void fillToGalleryRightLtr() {
        int itemSpacing = m_spacing;
        int galleryRight = mRight - mLeft - mPaddingRight;
        int numChildren = getChildCount();
        int numItems = mItemCount;
        
        // Set state for initial iteration
        View prevIterationView = getChildAt(numChildren - 1);
        int curPosition;
        int curLeftEdge;
        
        if (prevIterationView != null) {
            curPosition = mFirstPosition + numChildren;
            curLeftEdge = prevIterationView.getRight() + itemSpacing;
        } else {
            mFirstPosition = curPosition = mItemCount - 1;
            curLeftEdge = mPaddingLeft;
            m_shouldStopFling = true;
        }
                
        while (curLeftEdge < galleryRight && curPosition < numItems) {
            prevIterationView = makeAndAddView(curPosition, curPosition - mSelectedPosition,
                    curLeftEdge, true);

            // Set state for next iteration
            curLeftEdge = prevIterationView.getRight() + itemSpacing;
            curPosition++;
        }
    }/*}}}*/
    // private View makeAndAddView(int position, int offset, int x, boolean fromLeft);/*{{{*/
    /**
     * Obtain a view, either by pulling an existing view from the recycler or by
     * getting a new one from the adapter. If we are animating, make sure there
     * is enough information in the view's layout parameters to animate from the
     * old to new positions.
     * 
     * @param position Position in the gallery for the view to obtain
     * @param offset Offset from the selected position
     * @param x X-coordinate indicating where this view should be placed. This
     *        will either be the left or right edge of the view, depending on
     *        the fromLeft parameter
     * @param fromLeft Are we positioning views based on the left edge? (i.e.,
     *        building from left to right)?
     * @return A view that has been added to the gallery
     */
    private View makeAndAddView(int position, int offset, int x, boolean fromLeft) {

        View child;
        if (!mDataChanged) {
            child = mRecycler.get(position);
            if (child != null) {
                // Can reuse an existing view
                int childLeft = child.getLeft();
                
                // Remember left and right edges of where views have been placed
                m_rightMost = Math.max(m_rightMost, childLeft 
                        + child.getMeasuredWidth());
                m_leftMost = Math.min(m_leftMost, childLeft);

                // Position the view
                setUpChild(child, offset, x, fromLeft);

                return child;
            }
        }

        // Nothing found in the recycler -- ask the adapter for a view
        child = mAdapter.getView(position, null, this);

        // Position the view
        setUpChild(child, offset, x, fromLeft);

        return child;
    }/*}}}*/
    // private void setUpChild(View child, int offset, int x, boolean fromLeft);/*{{{*/
    /**
     * Helper for makeAndAddView to set the position of a view and fill out its
     * layout parameters.
     * 
     * @param child The view to position
     * @param offset Offset from the selected position
     * @param x X-coordinate indicating where this view should be placed. This
     *        will either be the left or right edge of the view, depending on
     *        the fromLeft parameter
     * @param fromLeft Are we positioning views based on the left edge? (i.e.,
     *        building from left to right)?
     */
    private void setUpChild(View child, int offset, int x, boolean fromLeft) {

        // Respect layout params that are already in the view. Otherwise
        // make some up...
        CAndroidScrollView.LayoutParams lp = (CAndroidScrollView.LayoutParams) child.getLayoutParams();
        if (lp == null) {
            lp = (CAndroidScrollView.LayoutParams) generateDefaultLayoutParams();
        }

        addViewInLayout(child, fromLeft != m_isRtl ? -1 : 0, lp);

        child.setSelected(offset == 0);

        // Get measure specs
        int childHeightSpec = ViewGroup.getChildMeasureSpec(mHeightMeasureSpec,
                mSpinnerPadding.top + mSpinnerPadding.bottom, lp.height);
        int childWidthSpec = ViewGroup.getChildMeasureSpec(mWidthMeasureSpec,
                mSpinnerPadding.left + mSpinnerPadding.right, lp.width);

        // Measure child
        child.measure(childWidthSpec, childHeightSpec);

        int childLeft;
        int childRight;

        // Position vertically based on gravity setting
        int childTop = calculateTop(child, true);
        int childBottom = childTop + child.getMeasuredHeight();

        int width = child.getMeasuredWidth();
        if (fromLeft) {
            childLeft = x;
            childRight = childLeft + width;
        } else {
            childLeft = x - width;
            childRight = x;
        }

        child.layout(childLeft, childTop, childRight, childBottom);
    }/*}}}*/
    // private int calculateTop(View child, boolean duringLayout);/*{{{*/
    /**
     * Figure out vertical placement based on m_gravity
     * 
     * @param child Child to place
     * @return Where the top of the child should be
     */
    private int calculateTop(View child, boolean duringLayout) {
        int myHeight = duringLayout ? getMeasuredHeight() : getHeight();
        int childHeight = duringLayout ? child.getMeasuredHeight() : child.getHeight(); 
        
        int childTop = 0;

        switch (m_gravity) {
        case Gravity.TOP:
            childTop = mSpinnerPadding.top;
            break;
        case Gravity.CENTER_VERTICAL:
            int availableSpace = myHeight - mSpinnerPadding.bottom
                    - mSpinnerPadding.top - childHeight;
            childTop = mSpinnerPadding.top + (availableSpace / 2);
            break;
        case Gravity.BOTTOM:
            childTop = myHeight - mSpinnerPadding.bottom - childHeight;
            break;
        }
        return childTop;
    }/*}}}*/
    // private void dispatchPress(View child);/*{{{*/
    /**
     * 
     **/
    private void dispatchPress(View child) {
        
        if (child != null) {
            child.setPressed(true);
        }
        
        setPressed(true);
    }/*}}}*/
    // private void dispatchUnpress();/*{{{*/
    /**
     * 
     **/
    private void dispatchUnpress() {
        
        for (int i = getChildCount() - 1; i >= 0; i--) {
            getChildAt(i).setPressed(false);
        }
        
        setPressed(false);
    }/*}}}*/
    // private boolean dispatchLongPress(View view, int position, long id);/*{{{*/
    /**
     * 
     **/
    private boolean dispatchLongPress(View view, int position, long id) {
        boolean handled = false;
        
        if (mOnItemLongClickListener != null) {
            handled = mOnItemLongClickListener.onItemLongClick(this, m_downTouchView,
                    m_downTouchPosition, id);
        }

        if (!handled) {
            m_contextMenuInfo = new AdapterContextMenuInfo(view, position, id);
            handled = super.showContextMenuForChild(this);
        }

        if (handled) {
            performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
        }
        
        return handled;
    }/*}}}*/
    // private boolean scrollToChild(int childPosition);/*{{{*/
    /**
     * 
     **/
    private boolean scrollToChild(int childPosition) {
        View child = getChildAt(childPosition);
        
        if (child != null) {
            int distance = getCenterOfGallery() - getCenterOfView(child);
            m_flingRunnable.startUsingDistance(distance);
            return true;
        }
        
        return false;
    }/*}}}*/
    // private void updateSelectedItemMetadata();/*{{{*/
    /**
     * 
     **/
    private void updateSelectedItemMetadata() {
        
        View oldSelectedChild = m_selectedChild;

        View child = m_selectedChild = getChildAt(mSelectedPosition - mFirstPosition);
        if (child == null) {
            return;
        }

        child.setSelected(true);
        child.setFocusable(true);

        if (hasFocus()) {
            child.requestFocus();
        }

        // We unfocus the old child down here so the above hasFocus check
        // returns true
        if (oldSelectedChild != null && oldSelectedChild != child) {

            // Make sure its drawable state doesn't contain 'selected'
            oldSelectedChild.setSelected(false);
            
            // Make sure it is not focusable anymore, since otherwise arrow keys
            // can make this one be focused
            oldSelectedChild.setFocusable(false);
        }
        
    }/*}}}*/
    //@}

    /** \name INNER CLASSES */ //@{
    // private class FlingRunnable implements Runnable { }/*{{{*/
    /**
     * Responsible for fling behavior.
     * Use #startUsingVelocity(int) to initiate a fling. Each frame of the
     * fling is handled in #run().  A FlingRunnable will keep re-posting
     * itself until the fling is done.
     */
    private class FlingRunnable implements Runnable {
        // private Scroller mScroller;/*{{{*/
        /**
         * Tracks the decay of a fling scroll
         */
        private Scroller mScroller;/*}}}*/
        // private int mLastFlingX;/*{{{*/
        /**
         * X value reported by mScroller on the previous fling
         */
        private int mLastFlingX;/*}}}*/

        // public FlingRunnable();/*{{{*/
        /**
         * Constructor.
         **/
        public FlingRunnable() {
            mScroller = new Scroller(getContext());
        }/*}}}*/

        // private void startCommon();/*{{{*/
        /**
         * 
         **/
        private void startCommon() {
            // Remove any pending flings
            removeCallbacks(this);
        }/*}}}*/
        // public void startUsingVelocity(int initialVelocity);/*{{{*/
        /**
         * 
         **/
        public void startUsingVelocity(int initialVelocity) {
            if (initialVelocity == 0) return;
            
            startCommon();
            
            int initialX = initialVelocity < 0 ? Integer.MAX_VALUE : 0;
            mLastFlingX = initialX;
            mScroller.fling(initialX, 0, initialVelocity, 0,
                    0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE);
            post(this);
        }/*}}}*/
        // public void startUsingDistance(int distance);/*{{{*/
        /**
         * 
         **/
        public void startUsingDistance(int distance) {
            if (distance == 0) return;
            
            startCommon();
            
            mLastFlingX = 0;
            mScroller.startScroll(0, 0, -distance, 0, m_animationDuration);
            post(this);
        }/*}}}*/
        // public void stop(boolean scrollIntoSlots);/*{{{*/
        /**
         * 
         **/
        public void stop(boolean scrollIntoSlots) {
            removeCallbacks(this);
            endFling(scrollIntoSlots);
        }/*}}}*/
        // private void endFling(boolean scrollIntoSlots);/*{{{*/
        /**
         * 
         **/
        private void endFling(boolean scrollIntoSlots) {
            /*
             * Force the scroller's status to finished (without setting its
             * position to the end)
             */
            mScroller.forceFinished(true);
            
            if (scrollIntoSlots) scrollIntoSlots();
        }/*}}}*/

        // public void run();/*{{{*/
        @Override
        public void run() {

            if (mItemCount == 0) {
                endFling(true);
                return;
            }

            m_shouldStopFling = false;
            
            final Scroller scroller = mScroller;
            boolean more = scroller.computeScrollOffset();
            final int x = scroller.getCurrX();

            // Flip sign to convert finger direction to list items direction
            // (e.g. finger moving down means list is moving towards the top)
            int delta = mLastFlingX - x;

            // Pretend that each frame of a fling scroll is a touch scroll
            if (delta > 0) {
                // Moving towards the left. Use leftmost view as m_downTouchPosition
                m_downTouchPosition = m_isRtl ? (mFirstPosition + getChildCount() - 1) :
                    mFirstPosition;

                // Don't fling more than 1 screen
                delta = Math.min(getWidth() - mPaddingLeft - mPaddingRight - 1, delta);
            } else {
                // Moving towards the right. Use rightmost view as m_downTouchPosition
                int offsetToLast = getChildCount() - 1;
                m_downTouchPosition = m_isRtl ? mFirstPosition :
                    (mFirstPosition + getChildCount() - 1);

                // Don't fling more than 1 screen
                delta = Math.max(-(getWidth() - mPaddingRight - mPaddingLeft - 1), delta);
            }

            trackMotionScroll(delta);

            if (more && !m_shouldStopFling) {
                mLastFlingX = x;
                post(this);
            } else {
               endFling(true);
            }
        }/*}}}*/
    }/*}}}*/
    // public static class LayoutParams extends ViewGroup.LayoutParams { }/*{{{*/
    /**
     * Gallery extends LayoutParams to provide a place to hold current
     * Transformation information along with previous position/transformation
     * info.
     */
    public static class LayoutParams extends ViewGroup.LayoutParams {
        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int w, int h) {
            super(w, h);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }/*}}}*/
    //@}

    /** \name DATA MEMBERS */ //@{
    // private int     m_spacing = 0;/*{{{*/
    /**
     * Horizontal spacing between items.
     */
    private int m_spacing = 0;/*}}}*/
    // private int     m_animationDuration = 400;/*{{{*/
    /**
     * How long the transition animation should run when a child view changes
     * position, measured in milliseconds.
     */
    private int m_animationDuration = 400;/*}}}*/
    // private float   m_unselectedAlpha;/*{{{*/
    /**
     * The alpha of items that are not selected.
     */
    private float m_unselectedAlpha;/*}}}*/
    // private int     m_leftMost;/*{{{*/
    /**
     * Left most edge of a child seen so far during layout.
     */
    private int m_leftMost;/*}}}*/
    // private int     m_rightMost;/*{{{*/
    /**
     * Right most edge of a child seen so far during layout.
     */
    private int m_rightMost;/*}}}*/
    private int     m_gravity;
    // private int     m_downTouchPosition;/*{{{*/
    /**
     * The position of the item that received the user's down touch.
     */
    private int m_downTouchPosition;/*}}}*/
    // private View    m_downTouchView;/*{{{*/
    /**
     * The view of the item that received the user's down touch.
     */
    private View m_downTouchView;/*}}}*/
    // private View    m_selectedChild;/*{{{*/
    /**
     * The currently selected item's child.
     */
    private View m_selectedChild;/*}}}*/
    // private boolean m_shouldStopFling;/*{{{*/
    /**
     * When fling runnable runs, it resets this to false. Any method along the
     * path until the end of its run() can set this to true to abort any
     * remaining fling. For example, if we've reached either the leftmost or
     * rightmost item, we will set this to true.
     */
    private boolean m_shouldStopFling;/*}}}*/
    // private boolean m_shouldCallbackDuringFling = true;/*{{{*/
    /**
     * Whether to continuously callback on the item selected listener during a
     * fling.
     */
    private boolean m_shouldCallbackDuringFling = true;/*}}}*/
    // private boolean m_shouldCallbackonUnselectedItemClick = true;/*{{{*/
    /**
     * Whether to callback when an item that is not selected is clicked.
     */
    private boolean m_shouldCallbackonUnselectedItemClick = true;/*}}}*/
    // private boolean m_supressSelectionChanged;/*{{{*/
    /**
     * If true, do not callback to item selected listener. 
     */
    private boolean m_supressSelectionChanged;/*}}}*/
    // private boolean m_receivedInvokeKeyDown;/*{{{*/
    /**
     * If true, we have received the "invoke" (center or enter buttons) key
     * down. This is checked before we action on the "invoke" key up, and is
     * subsequently cleared.
     */
    private boolean m_receivedInvokeKeyDown;/*}}}*/
    // private boolean m_isFirstScroll;/*{{{*/
    /**
     * If true, this onScroll is the first for this user's drag (remember, a
     * drag sends many onScrolls).
     */
    private boolean m_isFirstScroll;/*}}}*/
    // private boolean m_isRtl = true;/*{{{*/
    /**
     * If true, mFirstPosition is the position of the rightmost child, and
     * the children are ordered right to left.
     */
    private boolean m_isRtl = true;/*}}}*/
    // private GestureDetector m_gestureDetector;/*{{{*/
    /**
     * Helper for detecting touch gestures.
     */
    private GestureDetector m_gestureDetector;/*}}}*/
    // private FlingRunnable   m_flingRunnable = new FlingRunnable();/*{{{*/
    /**
     * Executes the delta scrolls from a fling or scroll movement. 
     */
    private FlingRunnable m_flingRunnable = new FlingRunnable();/*}}}*/
    // private Runnable        m_disableSupressSelectionChangedRunnable;/*{{{*/
    /**
     * Sets m_supressSelectionChanged = false. This is used to set it to false
     * in the future. It will also trigger a selection changed.
     */
    private Runnable m_disableSupressSelectionChangedRunnable = new Runnable() {
        @Override
        public void run() {
            m_supressSelectionChanged = false;
            selectionChanged();
        }
    };/*}}}*/
    private AdapterContextMenuInfo m_contextMenuInfo;
    //@}

    /** \name CONSTANTS */ //@{
    // private static final int SCROLL_TO_FLING_UNCERTAINTY_TIMEOUT = 250;/*{{{*/
    /**
     * Duration in milliseconds from the start of a scroll during which we're
     * unsure whether the user is scrolling or flinging.
     */
    private static final int SCROLL_TO_FLING_UNCERTAINTY_TIMEOUT = 250;/*}}}*/
    //@}
}
// vim:syntax=java.doxygen
