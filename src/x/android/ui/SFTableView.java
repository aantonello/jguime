/**
 * \file
 * Defines the SFTableView class.
 *
 * \author Alessandro Antonello <aantonello@paralaxe.com.br>
 * \date   novembro 19, 2013
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
import android.util.*;
import android.content.*;
import android.view.*;
import android.view.animation.*;
import android.widget.*;

import x.android.defs.*;
import x.android.nms.*;
import x.android.utils.*;
/* }}} #imports */
/**
 * Extends ListView allowing different types of Views in a list.
 * Classes extending this class can override the \c SFTableDelegate
 * implementation specifying their custom Views to be used in the list. Also,
 * if this class is used as-is, a different implementation of \c
 * SFTableDelegate can be defined allowing the same behavior.
 *//* --------------------------------------------------------------------- */
public class SFTableView extends ListView implements SFTableDelegate,
       AdapterView.OnItemClickListener
{
    /** \name Constructors */ //@{
    // public SFTableView(Context context);/*{{{*/
    /**
     * Context constructor (every view needs a context).
     * \param context The context where this view will be running. Usually its
     * the parent Activities Context.
     **/
    public SFTableView(Context context) {
        super(context);
        _internal_init();
    }/*}}}*/
    // public SFTableView(Context context, AttributeSet attrs);/*{{{*/
    /**
     * Attributes constructor.
     * This constructor is called when the view is built from the layout file.
     * \param context The application context.
     * \param attrs Attributes of the XML file defining the view.
     **/
    public SFTableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        _internal_init();
    }/*}}}*/
    // public SFTableView(Context context, AttributeSet attrs, int style);/*{{{*/
    /**
     * Constructs a view from attributes and style.
     * Used when creating special view that has extended styles.
     * \param context The application context.
     * \param attrs Attributes of the XML file defining the view.
     * \param style The style to be set. Can be 0 if not needed. Can be the
     * identifier of a style in resource.
     **/
    public SFTableView(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);
        _internal_init();
    }/*}}}*/
    //@}

    /** \name Operations */ //@{
    // public void setDelegate(SFTableDelegate delegate);/*{{{*/
    /**
     * Sets or changes the delegate that will populate this list.
     * \param delegate The SFTableDelegate implementation.
     * \remarks If the delegate is not set, the application is obligated to
     * override the virtual functions of this class to implement the list.
     **/
    public void setDelegate(SFTableDelegate delegate) {
        if (m_adapter == null) return;      /* Unkown error. */
        m_adapter.setDelegate(delegate);
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
     **/
    public void deleteRow(View view, int position, boolean animated) {
        if (!animated) {
            this.reloadData();
            return;
        }

        DeleteRowAnimation   dra      = new DeleteRowAnimation(view, position, this);
        CollapseRowAnimation collapse = new CollapseRowAnimation(view, dra);
        collapse.setDuration( 300 );
        view.startAnimation( collapse );
    }/*}}}*/
    //@}

    /** \name Overridables */ //@{
    // public void onDeleteAnimationFinished(View view, int position);/*{{{*/
    /**
     * Called when the animation of a row deletion is ended.
     * \param view The View passed passed to the #deleteRow() function.
     * \param position The position passed to the #deleteRow() function.
     **/
    public void onDeleteAnimationFinished(View view, int position) {
        /* Does nothing. */
    }/*}}}*/
    //@}

    /** \name SFTableAdapter: Implementation */ //@{
    // public int numberOfTypes();/*{{{*/
    /**
     * Called when the system needs to know the number of different view types
     * that will be placed in the list.
     * @returns The number of different view types.
     * @remarks This implementation returns 1.
     **/
    public int numberOfTypes() {
        return 1;
    }/*}}}*/
    // public int typeOfViewAtPosition(int position);/*{{{*/
    /**
     * Called when the system needs to know the type of a View that will be
     * placed at the specified position.
     * @param position An integer defining the position where the view will be
     * placed in the list.
     * @return A code specifing the type of the View for the passed position.
     * @remarks This implementation returns 0.
     **/
    public int typeOfViewAtPosition(int position) {
        return 0;
    }/*}}}*/
    // public boolean typeRespondsToUserInput(int type);/*{{{*/
    /**
     * Called by the system when it needs to know if this kind of View accepts
     * user input.
     * @return the function should return \b true when the type of the view
     * accepts user input in a generalized way. When this function returns \b
     * false touch and input events will not be available in this type of
     * View.
     * @remarks This implementation returns \b true.
     **/
    public boolean typeRespondsToUserInput(int type) {
        return true;
    }/*}}}*/
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

    /** \name AdapterView.OnItemClickListener: Implementation */ //@{
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
        debug.w("SFTableView::onItemClick(position:%d, id:%d)\n", position, id);
    }/*}}}*/
    //@}

    /** \name Local Implementation */ //@{
    // void _internal_init();/*{{{*/
    /**
     * Initializes this class instance.
     * This function is called from the constructors, after the view is
     * constructed.
     **/
    final void _internal_init()
    {
        m_adapter = new SFTableAdapter();

        /* The initial delegate is our selves. */
        m_adapter.setDelegate(this);
        super.setAdapter(m_adapter);
        super.setOnItemClickListener(this);
    }/*}}}*/
    //@}

    /** \name Internal Classes */ //@{
    // private class DeleteRowAnimation;/*{{{*/
    /**
     * Implements the AnimationListener for the animated deletion of a row.
     **/
    private class DeleteRowAnimation implements Animation.AnimationListener
    {
        /** \name CONSTRUCTOR */ //@{
        // public DeleteRowAnimation(View animatedView, int rowPos, SFTableView tableView);/*{{{*/
        /**
         * Constructor
         * \param animatedView View that will be animated.
         * \param rowPos Row position of the \a animatedView.
         * \param tableView Pass the SFTableView instance to this object.
         **/
        public DeleteRowAnimation(View animatedView, int rowPos, SFTableView tableView)
        {
            m_animatedView = animatedView;
            m_rowPosition  = rowPos;
            m_tableView    = tableView;
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
        public void onAnimationEnd(Animation animation) {
            m_tableView.onDeleteAnimationFinished(m_animatedView, m_rowPosition);
        }/*}}}*/
        //@} AnimationListener IMPLEMENTATION

        /** \name DATA MEMBERS */ //@{
        private SFTableView m_tableView = null;     /**< Holds the list view instance. */
        private View        m_animatedView = null;  /**< The animated view.            */
        private int         m_rowPosition  = -1;    /**< Animated View position.       */
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
        public CollapseRowAnimation(View rowView, Animation.AnimationListener listener)
        {
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
        protected void applyTransformation(float interpolatedTime, Transformation t)
        {
            m_view.getLayoutParams().height = m_height - ((int)(m_height * interpolatedTime));
            m_view.requestLayout();
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
    //@}

    /** \name Local Members */ //@{
    SFTableAdapter m_adapter;           /**< Internally used. */
    //@}
}
// vim:syntax=java.doxygen
