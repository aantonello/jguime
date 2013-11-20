/**
 * \file
 * Defines the SFTableAdapter class.
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

/**
 * An adapter for a list view with different kinds of items.
 * This adapter extends \c CAndroidAdapter allowing a list to have different
 * types of views for its items.
 *
 * This class is used internally only.
 *//* --------------------------------------------------------------------- */
class SFTableAdapter extends CAndroidAdapter
{
    /** \name Constructor */ //@{
    // public SFTableAdapter();/*{{{*/
    /**
     * Default constructor.
     **/
    public SFTableAdapter() {
        super();
    }/*}}}*/
    //@}

    /** \name ListAdapter Implementation */ //@{
    // public boolean areAllItemsEnabled();/*{{{*/
    /**
     * Indicates whether all the items in this adapter are enabled.
     * @returns We return \b false in this implementation becouse the client
     * delegate must implement the \c
     * SFTableDelegate::typeRespondsToUserInput() function specifing what kind
     * of View is enabled individually.
     **/
    public boolean areAllItemsEnabled() {
        return false;
    }/*}}}*/
    // public boolean isEnabled(int position);/*{{{*/
    /**
     * Returns true if the item at the specified position is not a separator.
     * @returns \b true or \b false. If the \e delegate is specified, this
     * function will ask its method \c typeRespondsToUserInput() passing the
     * type code of the View in the specified position. Otherwise the function
     * returns \b true.
     **/
    public boolean isEnabled(int position)
    {
        if (m_target == null) return super.isEnabled(position);
        final SFTableDelegate delegate = (SFTableDelegate)m_target;
        final int typeCode = delegate.typeOfViewAtPosition(position);
        return delegate.typeRespondsToUserInput( typeCode );
    }/*}}}*/
    //@}

    /** \name CAndroidAdpater: Overrides */ //@{
    // public int    getItemViewType(int position);/*{{{*/
    /**
     * Get the type of View that will be created by \c
     * getView(int,View,ViewGroup) for the specified item.
     * @param position Position of the View.
     * @return The type code of the View.
     * @remarks This function calls \c SFTableDelegate::typeOfViewAtPosition()
     * method if the delegate was specified. Otherwise the function returns 0.
     **/
    public int getItemViewType(int position)
    {
        if (m_target == null) return super.getItemViewType(position);
        final SFTableDelegate delegate = (SFTableDelegate)m_target;
        return delegate.typeOfViewAtPosition(position);
    }/*}}}*/
    // public int    getViewTypeCount();/*{{{*/
    /**
     * Returns the number of types of Views that will be created by \c
     * getView(int,View,ViewGroup).
     * @return The number of different View types in this list.
     * @remarks The function calls \c SFTableDelegate::numberOfTypes() if the
     * delegate was specified. Otherwise the result will be 1.
     **/
    public int getViewTypeCount()
    {
        if (m_target == null) return super.getViewTypeCount();
        final SFTableDelegate delegate = (SFTableDelegate)m_target;
        return delegate.numberOfTypes();
    }/*}}}*/
    //@}

    /** \name CAndroidAdapter: Overloads */ //@{
    // public void setDelegate(SFTableDelegate delegate);/*{{{*/
    /**
     * Sets or changes the delegate to be called from this adapter.
     * \param delegate SFTableDelegate implementation. Can be \b null.
     **/
    public void setDelegate(SFTableDelegate delegate) {
        m_target = delegate;
    }/*}}}*/
    //@}
}
// vim:syntax=java.doxygen
