/**
 * \file
 * Defines the SFTableDelegate interface.
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
 * \ingroup x_android_ui
 * Interface defining a delegate for a table view.
 * A table view is a list view with several kinds of different items. This is
 * an extension of the \c IAndroidListDelegate allowing to have different list
 * items.
 *
 * Operating with different items on Android is not simple. You must define
 * the number of differente items that will be shown in the view and inform
 * the system where that different items are placed.
 *
 * The system will call the function #numberOfTypes() when it needs to know
 * the number of different view types. Also, when a view needs to placed on
 * the screen, the system will call #typeOfViewAtPosition(). This function
 * must return a code (can be an arbritaty code) to differentiate the type of
 * the view to be placed in that position.
 *
 * Also, the system needs to known when a certain kind of item will respond
 * to user inputs like touches or clicks. So the #typeRespondsToUserInput()
 * method will be called with the type code returned by \c
 * typeOfViewAtPosition().
 *//* --------------------------------------------------------------------- */
public interface SFTableDelegate extends IAndroidListDelegate
{
    // public int numberOfTypes();/*{{{*/
    /**
     * Called when the system needs to know the number of different view types
     * that will be placed in the list.
     * @returns The number of different view types.
     **/
    public int numberOfTypes();/*}}}*/
    // public int typeOfViewAtPosition(int position);/*{{{*/
    /**
     * Called when the system needs to know the type of a View that will be
     * placed at the specified position.
     * @param position An integer defining the position where the view will be
     * placed in the list.
     * @return A code specifing the type of the View for the passed position.
     **/
    public int typeOfViewAtPosition(int position);/*}}}*/
    // public boolean typeRespondsToUserInput(int type);/*{{{*/
    /**
     * Called by the system when it needs to know if this kind of View accepts
     * user input.
     * @return the function should return \b true when the type of the view
     * accepts user input in a generalized way. When this function returns \b
     * false touch and input events will not be available in this type of
     * View.
     **/
    public boolean typeRespondsToUserInput(int type);/*}}}*/
}
// vim:syntax=java.doxygen
