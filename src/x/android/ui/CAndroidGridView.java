/**
 * \file
 * Defines the CAndroidGridView class.
 *
 * \author Alessandro Antonello <aantonello@paralaxe.com.br>
 * \date   novembro 01, 2013
 * \since  jguime 2.4
 * \version 1.0
 *
 * \copyright
 * Paralaxe Tecnologia, 2013. All rights reserved.
 */
package x.android.ui;
/* Imports {{{ */
import android.util.AttributeSet;

import android.content.Context;

import android.view.*;
import android.widget.*;
import android.view.animation.*;

import x.android.utils.*;
/* }}} Imports */
/**
 * Based in the most common usage of a grid view.
 * Rules to use this implementation:
 * -# All cells must have the same size.
 * -# Events can be intercepted by overloading functions of this class or
 *    adding an implementation of IAndroidListDelegate.
 * -# No Adapter is really needed.
 * .
 * This implementation uses IAndroidListDelegate, which names its methods as
 * `numberOfRows()` or `objectForRow()`. In the case of the GridView, you must
 * understand that the word `Row` means `Cell` for this particular case.
 *//* --------------------------------------------------------------------- */
public class CAndroidGridView extends GridView
    implements IAndroidListDelegate, AdapterView.OnItemClickListener
{
    /** \name Constructors */ //@{
    // public CAndroidGridView(Context context);/*{{{*/
    /**
     * Constructs the view.
     * @param context The current application context.
     **/
    public CAndroidGridView(Context context) {
        super(context);
        _internal_init();
    }/*}}}*/
    // public CAndroidGridView(Context context, AttributeSet attrs);/*{{{*/
    /**
     * Constructs the view.
     * @param context The application context.
     * @param attrs Attributes defined in the layout file.
     **/
    public CAndroidGridView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
        _internal_init();
    }/*}}}*/
    // public CAndroidGridView(Context context, AttributeSet attrs, int style);/*{{{*/
    /**
     * Constructs the view.
     * @param context The application context.
     * @param attrs Attributes defined in the layout file.
     * @param style The current Android UI style.
     **/
    public CAndroidGridView(Context context, AttributeSet attrs, int style)
    {
        super(context, attrs, style);
        _internal_init();
    }/*}}}*/
    //@}

    /** \name Operations */ //@{
    // public final CAndroidGridView delegate(IAndroidListDelegate delegate);/*{{{*/
    /**
     * Sets the class implementation of IAndroidListDelegate.
     * This implementation will receive and control this grid behavior.
     * @param delegate The interface implementation. If \b null is passed the
     * current delegate will be unbound.
     * @returns This function returns \b this.
     **/
    public final CAndroidGridView delegate(IAndroidListDelegate delegate)
    {
        if (m_adapter != null)
            m_adapter.setDelegate(delegate);

        return this;
    }/*}}}*/
    // public final CAndroidGridView reload();/*{{{*/
    /**
     * Reload all data into this view.
     * @returns \b this.
     **/
    public final CAndroidGridView reload()
    {
        m_adapter.reloadData();
        return this;
    }/*}}}*/
    // public final View loadView(int layoutID, ViewGroup parent);/*{{{*/
    /**
     * Helper function to load a View from a layout resource.
     * @param layoutID Identifier of the layout resource to load.
     * @param parent ViewGroup where the new loaded View will be put in.
     * @return The loaded View means success. \b null if this function fails.
     **/
    public final View loadView(int layoutID, ViewGroup parent)
    {
        LayoutInflater li = LayoutInflater.from( getContext() );
        return li.inflate(layoutID, parent, false);
    }/*}}}*/
    //@}

    /** \name IAndroidListDelegate Implementation */ //@{
    // public int    numberOfRows();/*{{{*/
    /**
     * Returns the total number of cells that will be put on the grid.
     * @return The total number of cells that will be put on the grid.
     * @remarks The default implementation always returns 0. The application
     * must overload this method or set a delegate to respond to this
     * question.
     **/
    public int numberOfRows()
    {
        return 0;
    }/*}}}*/
    // public Object objectForRow(int row);/*{{{*/
    /**
     * Get the object associated with a cell in the grid.
     * @param row The cell index. The first index is 0.
     * @returns The function returns the object associated with the cell or \b
     * null if there is no one.
     * @remarks The application must overload this method or se a delegate to
     * respond to this question. This implementation always returns \b null.
     **/
    public Object objectForRow(int row)
    {
        return null;
    }/*}}}*/
    // public long   idForRow(int row);/*{{{*/
    /**
     * Asks for the identifier of a cell in the grid.
     * Each cell must have an unique identifier.
     * @param row The position of the cell. Zero based.
     * @returns The unique identifier of the cell.
     * @remarks The application must overload this method or se a delegate to
     * respond to this question. This implementation always returns \b 0L.
     **/
    public long idForRow(int row)
    {
        return 0L;
    }/*}}}*/
    // public View   viewForRow(int row, View reuseView, ViewGroup parent);/*{{{*/
    /**
     * Gets the View that represents the cell value in the grid.
     * @param row The cell index. Zero based.
     * @param reuseView A cached View instance to be reused. This is a View
     * that was previously returned by this function and was cached becouse it
     * is out of view space. If there was no View in the cache. This parameter
     * will be \b null.
     * @param parent The parent View where the returned View will be placed.
     * @return The View that represents the cell content.
     * @remarks The application must overload this method or set an instance
     * of IAndroidListDelegate to return the correct View for the cell. This
     * implementation will build and return an instance of a TextView.
     **/
    public View viewForRow(int row, View reuseView, ViewGroup parent)
    {
        TextView tv = null;
        Object item = this.objectForRow(row);

        if (item == null) return null;
        if (reuseView instanceof TextView)
            tv = (TextView)reuseView;
        else
            tv = new TextView( getContext() );

        tv.setText(item.toString());
        return tv;
    }/*}}}*/
    //@}

    /** \name AdapterView.OnItemClickListener Implementation */ //@{
    // public void onItemClick(AdapterView<?> self, View view, int position, long id);/*{{{*/
    /**
     * Invoked when an item in the grid has been clicked.
     * @param self This AdapterView implementation.
     * @param view The View the was clicked.
     * @param position Zero based position of the item.
     * @param id The unique identifier of the item.
     * @remarks The application can choose to override this method or define a
     * different \c AdapterView.OnItemClickListener interface. This
     * implementation does nothing.
     **/
    public void onItemClick(AdapterView<?> self, View view, int position, long id)
    {
        debug.w("CAndroidListView::onItemClick(position:%d, id:%d)\n", position, id);
    }/*}}}*/
    //@}

    /** \name Local Functions */ //@{
    // final void _internal_init();/*{{{*/
    /**
     * Initializes this class for the basic usage.
     * Instantiates the adapter and sets the default delegate implementation.
     **/
    final void _internal_init()
    {
        m_adapter = new CAndroidAdapter();

        m_adapter.setDelegate(this);
        super.setAdapter(m_adapter);
        super.setOnItemClickListener(this);
    }/*}}}*/
    //@}

    /** \name Local Members */ //@{
    CAndroidAdapter m_adapter;          /**< Generic adapter implementation. */
    //@}
}
// vim:syntax=java.doxygen
