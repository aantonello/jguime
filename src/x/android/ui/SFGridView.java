/**
 * \file
 * Defines the SFGridView class.
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
 * \ingroup x_android_ui
 * Extends GridView allowing different types of Views in the grid.
 * Classes extending this class can override the \c SFTableDelegate
 * implementation specifying their custom Views to be used in the list. Also,
 * if this class is used as-is, a different implementation of \c
 * SFTableDelegate can be defined allowing the same behavior.
 *//* --------------------------------------------------------------------- */
public class SFGridView extends GridView implements SFTableDelegate,
       AdapterView.OnItemClickListener
{
    /** \name Constructors */ //@{
    // public SFGridView(Context context);/*{{{*/
    /**
     * Context constructor (every view needs a context).
     * \param context The context where this view will be running. Usually its
     * the parent Activities Context.
     **/
    public SFGridView(Context context) {
        super(context);
        _internal_init();
    }/*}}}*/
    // public SFGridView(Context context, AttributeSet attrs);/*{{{*/
    /**
     * Attributes constructor.
     * This constructor is called when the view is built from the layout file.
     * \param context The application context.
     * \param attrs Attributes of the XML file defining the view.
     **/
    public SFGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        _internal_init();
    }/*}}}*/
    // public SFGridView(Context context, AttributeSet attrs, int style);/*{{{*/
    /**
     * Constructs a view from attributes and style.
     * Used when creating special view that has extended styles.
     * \param context The application context.
     * \param attrs Attributes of the XML file defining the view.
     * \param style The style to be set. Can be 0 if not needed. Can be the
     * identifier of a style in resource.
     **/
    public SFGridView(Context context, AttributeSet attrs, int style) {
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
        debug.w("SFGridView::onItemClick(position:%d, id:%d)\n", position, id);
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

    /** \name Local Members */ //@{
    SFTableAdapter m_adapter;           /**< Internally used. */
    //@}
}
// vim:syntax=java.doxygen
