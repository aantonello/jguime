/**
 * \file
 * Defines the IAndroidListDelegate class.
 *
 * \author Alessandro Antonello <aantonello@paralaxe.com.br>
 * \date   Dezembro 01, 2012
 *
 * \par Copyright
 * This file is provided in hope that it will be useful to someone. It is
 * offered in public domain. You may use, modify or distribute it freely.
 *
 * The code is provided "AS IS". There is no warranty at all, of any kind. You
 * may change it if you like. Or just use it as it is.
 */
package x.android.ui;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

/**
 * Defines an interface to be implemented by a ListView simple adapter.
 *//* --------------------------------------------------------------------- */
public interface IAndroidListDelegate
{
    // public int    numberOfRows();/*{{{*/
    /**
     * Returns the total number of rows in the list.
     **/
    public int numberOfRows();/*}}}*/
    // public Object objectForRow(int row);/*{{{*/
    /**
     * Return the object associated with a row.
     * \param row The row index. Zero based.
     **/
    public Object objectForRow(int row);/*}}}*/
    // public long   idForRow(int row);/*{{{*/
    /**
     * Each row must have a unique identifier.
     * \param row Zero based index of the row.
     **/
    public long   idForRow(int row);/*}}}*/
    // public View   viewForRow(int row, View reuseView, ViewGroup parent);/*{{{*/
    /**
     * Must return the View used to show the row's data.
     * \param row Zero based index of the row.
     * \param reuseView A View that can be reused. This parameter can be \b
     * null, so, you should always check if it's valid.
     * \param parent The parent ViewGroup where the returned View will be
     * placed. This is usefull when the returning view will be created from a
     * layout resource.
     **/
    public View   viewForRow(int row, View reuseView, ViewGroup parent);/*}}}*/
}
// vim:syntax=java.doxygen
