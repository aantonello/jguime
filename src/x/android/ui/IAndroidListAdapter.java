/**
 * \file
 * Defines the IAndroidListAdapter class.
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
 * Sets the interface for a basic ListAdapter implementation in this library.
 *//* --------------------------------------------------------------------- */
public interface IAndroidListAdapter extends ListAdapter
{
    // public void setDelegate(IAndroidListDelegate delegate);/*{{{*/
    /**
     * Set the delegate implementation.
     * \param delegate Object that implements IAndroidListDelegate. This
     * object will be queried every time the list needs information.
     **/
    public void setDelegate(IAndroidListDelegate delegate);/*}}}*/
    // public void reloadData();/*{{{*/
    /**
     * Called when the list of data shown was changed.
     * The result will cause the list to be reloaded in the ListView.
     **/
    public void reloadData();/*}}}*/
}
// vim:syntax=java.doxygen
