/**
 * \file
 * Defines the CAndroidListView class.
 *
 * \author Alessandro Antonello <aantonello@paralaxe.com.br>
 * \date   Novembro 30, 2012
 *
 * \par Copyright
 * This file is provided in hope that it will be useful to someone. It is
 * offered in public domain. You may use, modify or distribute it freely.
 *
 * The code is provided "AS IS". There is no warranty at all, of any kind. You
 * may change it if you like. Or just use it as it is.
 */
package x.android.ui;
/* #imports {{{ */
import android.util.AttributeSet;

import android.content.Context;

import android.view.*;
import android.widget.*;
import android.view.animation.*;

import x.android.utils.debug;

/* }}} #imports */
/**
 * \ingroup x_android_ui
 * A ListView based on the most common usage of lists.
 * The rules to use this ListView implementation are simple. All rows must
 * have the same type of View.
 *
 * The application using this class can supply a class that implements
 * IAndroidListDelegate. If not you can extend this class overriding the
 * virtual methods declared.
 *//* --------------------------------------------------------------------- */
public class CAndroidListView extends ListView
    implements IAndroidListDelegate, AdapterView.OnItemClickListener
{
    /** \name CONSTRUCTOR */ //@{
    // public CAndroidListView(Context context);/*{{{*/
    /**
     * Context constructor (every view needs a context).
     * \param context The context where this view will be running. Usually its
     * the parent Activities Context.
     **/
    public CAndroidListView(Context context) {
        super(context);
        _internal_init();
    }/*}}}*/
    // public CAndroidListView(Context context, AttributeSet attrs);/*{{{*/
    /**
     * Attributes constructor.
     * This constructor is called when the view is built from the layout file.
     * \param context The application context.
     * \param attrs Attributes of the XML file defining the view.
     **/
    public CAndroidListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        _internal_init();
    }/*}}}*/
    // public CAndroidListView(Context context, AttributeSet attrs, int style);/*{{{*/
    /**
     * Constructs a view from attributes and style.
     * Used when creating special view that has extended styles.
     * \param context The application context.
     * \param attrs Attributes of the XML file defining the view.
     * \param style The style to be set. Can be 0 if not needed. Can be the
     * identifier of a style in resource.
     **/
    public CAndroidListView(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);
        _internal_init();
    }/*}}}*/
    //@}

    /** \name OPERATIONS */ //@{
    // public void setDelegate(IAndroidListDelegate delegate);/*{{{*/
    /**
     * Sets or changes the delegate that will populate this list.
     * \param delegate The IAndroidListDelegate implementation.
     * \remarks If the delegate is not set, the application is obligated to
     * override the virtual functions of this class to implement the list.
     **/
    public void setDelegate(IAndroidListDelegate delegate) {
        if (m_adapter == null) return;      /* Unkown error. */
        m_adapter.setDelegate(delegate);
    }/*}}}*/
    // public void setDeleteRowFinishedHandler(IDeleteRowFinished handler);/*{{{*/
    /**
     * Sets the handler of all delete row animations.
     * @param handler Instance of the handler. Can be \b null to eliminate a
     * previous defined handler.
     * @remarks When set by this method the \a handler is kept and used in all
     * calls to \c deleteRow(View,int,boolean) when \e animated is \b true.
     * A different handler still can be passed through
     * \c deleteRow(View,int,IDeleteRowFinished) method for a specific type
     * of row if needed. The specific handler will be used only in that
     * particular execution.
     * @since 2.4
     **/
    public void setDeleteRowFinishedHandler(IDeleteRowFinished handler)
    {
        m_handleDel = handler;
    }/*}}}*/
    // public void reloadData();/*{{{*/
    /**
     * Reloads all data and Views for this list.
     **/
    public void reloadData() {
        m_adapter.reloadData();
    }/*}}}*/
    // public View loadView(int layoutID, ViewGroup parent);/*{{{*/
    /**
     * Load a view from a XML resource layout.
     * \param layoutID The identifier of the resource.
     * \param parent A ViewGroup used to obtain default layout parameters. The
     * loaded View will not be added to the parent.
     * \return On success the loaded View. On failure the result will be \b
     * null.
     **/
    public View loadView(int layoutID, ViewGroup parent) {
        LayoutInflater li = LayoutInflater.from( getContext() );
        return li.inflate(layoutID, parent, false);
    }/*}}}*/
    // public void deleteRow(View view, int position, boolean animated);/*{{{*/
    /**
     * Delete a row in the specified index.
     * Since this class doesn't hold the list of itens you will need to delete
     * the item manualy.
     * \param view View that represents the row to be deleted.
     * \param position The position of the deleted row.
     * \param animated If the deletion of the row should be animated or not.
     * The animation collapses the list over the deleted item. Notice that the
     * list cannot be updated while the animation is running. This means that
     * the \c reloadData() must not be called while the animation is running.
     * The item should only be removed after the animation ends. If you are
     * extending this class, when the animation ends the method
     * \c onDeleteRowAnimateEnd() will be called. If you are not extending this
     * class you can set a handler in the method
     * \c setDeleteRowFinishedHandler().
     **/
    public void deleteRow(View view, int position, boolean animated)
    {
        if (animated)
            deleteRow(view, position, m_handleDel);
        else
            onDeleteRowAnimateEnd(view, position);
    }/*}}}*/
    // public void deleteRow(View view, int position, IDeleteRowFinished handler);/*{{{*/
    /**
     * Delete a row in the specified index.
     * Since this class doesn't hold the list of items you will need to delete
     * the item manually.
     * @param view View that represents the row to be deleted.
     * @param position The position of the deleted row.
     * @param handler Instance of \c IDeleteRowFinished to handle the
     * animation termination. If you setted a handler in
     * \c setDeleteRowFinishedHandler() the instance passed here will be used
     * instead, but will not overwrite the handler defined in that method. If
     * you pass \b null here no handler will be called but the
     * \c onDeleteRowAnimateEnd() method will still be called and the list
     * will be updated automatically.
     * @since 2.4
     **/
    public void deleteRow(View view, int position, IDeleteRowFinished handler)
    {
        long rowID = m_adapter.getItemId(position);
        RowAnimationHandler rah  = new RowAnimationHandler(view, position, rowID, handler);
        CollapseRowAnimation cra = new CollapseRowAnimation(view, rah);
        cra.setDuration( 300 );
        view.startAnimation( cra );
    }/*}}}*/
    //@}

    /** \name OVERRIDABLES */ //@{
    // public void onDeleteRowAnimateEnd(View view, int position);/*{{{*/
    /**
     * Called when the animation of a row deletion is ended.
     * \param view The View passed passed to the #deleteRow() function.
     * \param position The position passed to the #deleteRow() function.
     **/
    public void onDeleteRowAnimateEnd(View view, int position)
    {
        reloadData();
    }/*}}}*/
    //@} OVERRIDABLES

    /** \name IAndroidListDelegate IMPLEMENTATION */ //@{
    // public int    numberOfRows();/*{{{*/
    /**
     * Returns the total number of rows in the list.
     * The application must implement this function or set a delegate to be
     * called. If a delegate is set this function will never be called.
     * \return This default implementation always returns 0.
     **/
    public int numberOfRows() {
        return 0;
    }/*}}}*/
    // public Object objectForRow(int row);/*{{{*/
    /**
     * Return the object associated with a row.
     * \param row The row index. Zero based.
     * \remarks If the application doesn't set a delegate implementation it
     * should override this method to return the object required. Otherwise
     * this function will never be called.
     * \note This default implementation will always return \b null.
     **/
    public Object objectForRow(int row) {
        return null;
    }/*}}}*/
    // public long   idForRow(int row);/*{{{*/
    /**
     * Each row must have a unique identifier.
     * \param row Zero based index of the row.
     * \return This default implementation returns 0.
     * \remarks The application must override this method or set a delegate
     * implementation to answer this question from the list.
     **/
    public long idForRow(int row) {
        return 0L;
    }/*}}}*/
    // public View   viewForRow(int row, View reuseView, ViewGroup parent);/*{{{*/
    /**
     * Must return the View used to show the row's data.
     * \param row Zero based index of the row.
     * \param reuseView A View that can be reused. This parameter can be \b
     * null, so, you should always check if it's valid.
     * \param parent The parent ViewGroup where the returned View will be
     * placed. This is usefull when the returning view will be created from a
     * layout resource.
     * \return A view to be used in the list. Against all other virtual
     * functions in this class, if this function is not overroden, the default
     * value is a simple TextView.
     **/
    public View viewForRow(int row, View reuseView, ViewGroup parent) {
        TextView tv = null;
        Object item = this.objectForRow(row);

        if (item == null) return null;

        if (reuseView instanceof TextView)
            tv = (TextView)reuseView;
        else
            tv = new TextView(this.getContext());

        tv.setText(item.toString());
        return tv;
    }/*}}}*/
    //@}

    /** \name AdapterView.OnItemClickListener IMPLEMENTATION */ //@{
    // public void onItemClick(AdapterView<?> self, View view, int position, long id);/*{{{*/
    /**
     * Invoked when an item in this list has been clicked.
     * \param self This AdapterView implementation.
     * \param view The View the was clicked.
     * \param position Zero based position of the item.
     * \param id The unique identifier of the item.
     * \remarks The application can choose to override this method or define a
     * different \c AdapterView.OnItemClickListener interface.
     **/
    public void onItemClick(AdapterView<?> self, View view, int position, long id) {
        debug.w("CAndroidListView::onItemClick(position:%d, id:%d)\n", position, id);
    }/*}}}*/
    //@}

    /** \name LOCAL IMPLEMENTATION */ //@{
    // void _internal_init();/*{{{*/
    /**
     * Initializes this class instance.
     * This function is called from the constructors, after the view is
     * constructed.
     **/
    final void _internal_init()
    {
        m_adapter = new CAndroidAdapter();
        m_handleDel = null;

        /* The initial delegate is our selves. */
        m_adapter.setDelegate(this);
        super.setAdapter(m_adapter);
        super.setOnItemClickListener(this);
    }/*}}}*/
    //@}

    /** \name INNER CLASSES */ //@{
    // private class RowAnimationHandler;/*{{{*/
    /**
     * Implements the AnimationListener for the animated deletion of a row.
     **/
    private class RowAnimationHandler implements Animation.AnimationListener
    {
        /** \name CONSTRUCTOR */ //@{
        // public RowAnimationHandler(View animatedView, int rowPos, long id, IDeleteRowFinished handler);/*{{{*/
        /**
         * Parametrized constructor.
         * @param animatedView View that will be animated.
         * @param rowPos Row position of the \a animatedView.
         * @param id The identifier of the item.
         * @param handler \c IDeleteRowFinished instance to be called when the
         * animation ends.
         * @since 2.4
         **/
        public RowAnimationHandler(View animatedView, int rowPos, long id, IDeleteRowFinished handler)
        {
            m_animatedView = animatedView;
            m_rowPosition  = rowPos;
            m_rowID        = id;
            m_handler      = handler;
        }/*}}}*/
        //@} CONSTRUCTOR

        /** \name AnimationListener IMPLEMENTATION */ //@{
        // public void onAnimationStart(Animation animation);/*{{{*/
        /**
         * Called when the animation starts.
         * \param animation The animation object.
         **/
        public void onAnimationStart(Animation animation) {
            /* Nothing to do. */
        }/*}}}*/
        // public void onAnimationRepeat(Animation animation);/*{{{*/
        /**
         * Called when the animation repeats.
         * \param animation The animation object.
         **/
        public void onAnimationRepeat(Animation animation) {
            /* Nothing to do. */
        }/*}}}*/
        // public void onAnimationEnd(Animation animation);/*{{{*/
        /**
         * Called when the animation ends.
         * \param animation The animation object.
         **/
        public void onAnimationEnd(Animation animation)
        {
            if (m_handler != null)
                m_handler.deleteAnimationEnded(m_animatedView, m_rowPosition, m_rowID);

            onDeleteRowAnimateEnd(m_animatedView, m_rowPosition);
        }/*}}}*/
        //@} AnimationListener IMPLEMENTATION

        /** \name DATA MEMBERS */ //@{
        private IDeleteRowFinished m_handler;   /**< Called when finished.      */
        private View    m_animatedView;         /**< The animated view.         */
        private int     m_rowPosition;          /**< Animated View position.    */
        private long    m_rowID;                /**< Row identifier.            */
        //@} DATA MEMBERS
    }/*}}}*/
    // private class CollapseRowAnimation;/*{{{*/
    /**
     * Class used to animate the deletion of a row.
     **/
    private class CollapseRowAnimation extends Animation
    {
        /** \name CONSTRUCTOR */ //@{
        // public CollapseRowAnimation(View rowView, Animation.AnimationListener listener);/*{{{*/
        /**
         * Creates a new object.
         * \param rowView The View to be animated.
         * \param listener Listener to get the end of the animation. Can be \b
         * null.
         **/
        public CollapseRowAnimation(View rowView, Animation.AnimationListener listener) {
            super();
            m_view = rowView;
            m_height = rowView.getMeasuredHeight();
            if (listener != null) this.setAnimationListener(listener);
        }/*}}}*/
        //@} CONSTRUCTOR

        /** \name OVERRIDES */ //@{
        // protected void applyTransformation(float interpolatedTime, Transformation t);/*{{{*/
        /**
         * Applies the transformation to the view at time.
         **/
        protected void applyTransformation(float interpolatedTime, Transformation t) {
//            if (interpolatedTime == 1.0)
//                m_view.setVisibility( View.GONE );
//            else
//            {
                m_view.getLayoutParams().height = m_height - ((int)(m_height * interpolatedTime));
                m_view.requestLayout();
//            }
        }/*}}}*/
        // public boolean willChangeBounds();/*{{{*/
        /**
         * Indicates whether or not this animation will affect the bounds of
         * the animated view.
         **/
        public boolean willChangeBounds() {
            return true;
        }/*}}}*/
        //@} OVERRIDES

        /** \name DATA MEMBERS */ //@{
        private View m_view;                /**< Animated view. */
        private int  m_height;              /**< Initial height. */
        //@} DATA MEMBERS
    }/*}}}*/
    //@} INNER CLASSES

    /** \name INNER INTERFACES */ //@{
    // public interface IDeleteRowFinished;/*{{{*/
    /**
     * Interface called when the deletion of a row finishes.
     * This interface is called only when the deletion is animated. The
     * interface can be set in the
     * CAndroidListView#setDeleteRowFinishedHandler() method or directly in
     * the CAndroidListView#deleteRow(View,int,IDeleteRowFinished) method.
     * @since 2.4
     **/
    public interface IDeleteRowFinished
    {
        // public void deleteAnimationEnded(View view, int itemPosition, long itemID);/*{{{*/
        /**
         * Called when the delete animation ends.
         * @param view \c View removed from the list.
         * @param itemPosition Zero based position of the item in the list.
         * @param itemID Identifier of the item as queried from
         * CAndroidListView#idForRow().
         * @remarks When this method is called you should remove the element
         * used to populate the item in the list. When this method returns the
         * list is updated automaticaly. You does not need to call
         * CAndroidListView#reloadData().
         * @since 2.4
         **/
        public void deleteAnimationEnded(View view, int itemPosition, long itemID);/*}}}*/
    }/*}}}*/
    //@}

    /** \name DATA MEMBERS */ //@{
    CAndroidAdapter m_adapter;          /**< Used internally.           */
    IDeleteRowFinished m_handleDel;     /**< Delete animation handler.  */
    //@}
}
// vim:syntax=java.doxygen
