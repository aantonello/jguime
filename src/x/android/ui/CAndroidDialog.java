/**
 * \file
 * Defines the CAndroidDialog class.
 *
 * \author Alessandro Antonello <aantonello@paralaxe.com.br>
 * \date   Março 04, 2013
 *
 * \copyright
 * This file is provided in hope that it will be useful to someone. It is
 * offered in public domain. You may use, modify or distribute it freely.
 *
 * The code is provided "AS IS". There is no warranty at all, of any kind. You
 * may change it if you like. Or just use it as it is.
 */
package x.android;

/**
 * \ingroup x_android_ui
 * A base class for dialogs construction.
 *//* --------------------------------------------------------------------- */
public class CAndroidDialog
{
    /** \name CONSTRUCTOR */ //@{
    // public CAndroidDialog(Activity owner);/*{{{*/
    /**
     * Default constructor.
     * \param owner the \c Activity instance that owns this dialog.
     **/
    public CAndroidDialog(Activity owner) {
        m_owner = owner;
    }/*}}}*/
    //@}

    /** \name OPERATONS */ //@{
    // public CAndroidDialog setTitle(int stringID);/*{{{*/
    /**
     * Sets the title of the dialog using a string in the application
     * resources.
     * \param stringID Identifier os the string in the resource.
     * \return This object instance.
     **/
    public CAndroidDialog setTitle(int stringID) {
        return setTitle(CAndroidApp.loadString(stringID));
    }/*}}}*/
    // public CAndroidDialog setTitle(String title);/*{{{*/
    /**
     * Sets the title of this dialog.
     * \param title String with the dialog's title.
     * \returns This current instance.
     **/
    public CAndroidDialog setTitle(String title) {
        m_dialog.setTitle(title);
        return this;
    }/*}}}*/
    //@}

    /** \name DATA MEMBERS */ //@{
    private Activity    m_owner;            /**< Owner of this dialog.  */
    private AlertDialog m_dialog;           /**< The 'real' dialog.     */
    //@}
}
// vim:syntax=java.doxygen
