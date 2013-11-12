/**
 * \file
 * Defines the CAndroidAdapter class.
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
import java.util.*;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

/* }}} #imports */
/**
 * An adapter used to populate the CAndroidListView class.
 * This adapter has the difference that is does not need a list. The developer
 * can manage the list of itens the way he wants. When the list needs an item
 * it will ask to the CAndroidListView class through one of its virtual
 * functions.
 *
 * This class is not exposed. It is used inside this library only.
 *//* --------------------------------------------------------------------- */
class CAndroidAdapter implements IAndroidListAdapter
{
    /** \name CONSTRUCTOR */ //@{
    // public CAndroidAdapter();/*{{{*/
    /**
     * Default constructor.
     **/
    public CAndroidAdapter() {
        m_target = null;
        m_lists  = null;
    }/*}}}*/
    //@}

    /** \name ListAdapter IMPLEMENTATION */ //@{
    // public boolean areAllItemsEnabled();/*{{{*/
    /**
     * Indicates whether all the items in this adapter are enabled.
     * This implementation always returns \b true.
     **/
    public boolean areAllItemsEnabled() {
        return true;
    }/*}}}*/
    // public boolean isEnabled(int position);/*{{{*/
    /**
     * Returns true if the item at the specified position is not a separator.
     * This simple adapter doesn't support separators so, all items must be
     * enabled.
     **/
    public boolean isEnabled(int position) {
        return true;
    }/*}}}*/
    //@}

    /** \name Adapter IMPLEMENTATION */ //@{
    // public int    getCount();/*{{{*/
    /**
     * How many items are in the data set represented by this Adapter.
     * This will query the \c numberOfRows() delegate function.
     **/
    public int getCount() {
        if (m_target == null) return 0;
        return m_target.numberOfRows();
    }/*}}}*/
    // public Object getItem(int position);/*{{{*/
    /**
     * Get the data item associated with the specified position in the data
     * set.
     * This will call the \c objectForRow() delegate function.
     **/
    public Object getItem(int position) {
        if (m_target == null) return null;
        return m_target.objectForRow(position);
    }/*}}}*/
    // public long   getItemId(int position);/*{{{*/
    /**
     * Get the row id associated with the specified position in the list.
     * This ID must be unique. This operation will call \c idForRow() in the
     * delegate implementation.
     **/
    public long getItemId(int position) {
        if (m_target == null) return 0L;
        return m_target.idForRow(position);
    }/*}}}*/
    // public int    getItemViewType(int position);/*{{{*/
    /**
     * Get the type of View that will be created by \c
     * getView(int,View,ViewGroup) for the specified item.
     * As of this adapter implementation is very simple, only one type of view
     * is available. This function always returns 0.
     **/
    public int getItemViewType(int position) {
        return 0;
    }/*}}}*/
    // public View   getView(int position, View convertView, ViewGroup parent);/*{{{*/
    /**
     * Get a View that displays the data at the specified position in the data
     * set.
     * \param position Zero based position of the item in the list.
     * \param convertView This can be a previous used View or \b null. The
     * application can decide if it can reuse this View or not.
     * \param parent The parent View of the returned View. This argument is
     * useful when a View will be loaded from XML layout resource.
     * \remarks This function calls \c viewForRow() at the delegate
     * implementation.
     **/
    public View getView(int position, View convertView, ViewGroup parent) {
        if (m_target == null) return convertView;
        return m_target.viewForRow(position, convertView, parent);
    }/*}}}*/
    // public int    getViewTypeCount();/*{{{*/
    /**
     * Returns the number of types of Views that will be created by \c
     * getView(int,View,ViewGroup).
     * As of this is a simple list adapter as possible this function will not
     * be asked to the delegate and will always returns 1.
     **/
    public int getViewTypeCount() {
        return 1;
    }/*}}}*/
    // public boolean hasStableIds();/*{{{*/
    /**
     * Indicates whether the item ids are stable across changes to the
     * underlying data.
     * This is a prerequisite for the lists using CAndroidListView. All itens
     * must have an stable identifier. So, this implementation always returns
     * \b true.
     **/
    public boolean hasStableIds() {
        return true;
    }/*}}}*/
    // public boolean isEmpty();/*{{{*/
    /**
     * Checks if the list is empty.
     * This function calls #getCount() that will ask the delegate about how
     * many rows the list has.
     **/
    public boolean isEmpty() {
        return (this.getCount() == 0);
    }/*}}}*/
    // public void   registerDataSetObserver(DataSetObserver observer);/*{{{*/
    /**
     * Register an observer that is called when changes happen to the data
     * used by this adapter.
     **/
    public void registerDataSetObserver(DataSetObserver observer) {
        if (m_lists == null) {
            m_lists = new ArrayList<DataSetObserver>(1);
        }
        m_lists.add(observer);
    }/*}}}*/
    // public void   unregisterDataSetObserver(DataSetObserver observer);/*{{{*/
    /**
     * Unregister an observer that has previously been registered with this
     * adapter via #registerDataSetObserver(DataSetObserver).
     **/
    public void unregisterDataSetObserver(DataSetObserver observer) {
        if (m_lists == null) return;
        m_lists.remove(observer);
    }/*}}}*/
    //@}

    /** \name IAndroidListAdapter IMPLEMENTATION */ //@{
    // public void setDelegate(IAndroidListDelegate delegate);/*{{{*/
    /**
     * Sets or changes the delegate to be called from this adapter.
     * \param delegate IAndroidListDelegate implementation. Can be \b null.
     **/
    public void setDelegate(IAndroidListDelegate delegate) {
        m_target = delegate;
    }/*}}}*/
    // public void reloadData();/*{{{*/
    /**
     * Tell to reload the list data.
     * This will notify all observers that the underlined data was changed.
     **/
    public void reloadData() {
        if (m_lists == null) return;

        for (DataSetObserver observer : m_lists)
            observer.onChanged();
    }/*}}}*/
    //@}

    /** \name DATA MEMBERS */ //@{
    protected ArrayList<DataSetObserver> m_lists;   /**< List of observers. */
    protected IAndroidListDelegate       m_target;  /**< Delegate implementation. */
    //@}
}
// vim:syntax=java.doxygen
