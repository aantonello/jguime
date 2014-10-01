/**
 * \file
 * Defines the CAndroidPasswordDialog class.
 *
 * \author Alessandro Antonello <aantonello@paralaxe.com.br>
 * \date   junho 03, 2013
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
import android.app.*;
import android.content.*;
import android.content.res.*;

import android.view.*;
import android.widget.*;
import android.text.*;
import android.text.method.*;
import android.view.inputmethod.*;

import x.android.defs.*;
import x.android.utils.*;
import x.android.nms.*;
/* }}} #imports */

/**
 * \ingroup x_android_ui
 * Builds a password dialog.
 * This dialog sends 3 messages to its handler, defined in the constructor.
 * All messages are defined with the code \c IMSG.MSG_DIALOG in the \a msgID
 * parameter. The \a nParam argument will carry the dialog identifier. This
 * identifier is used to uniquely identify the dialog. The identifier is
 * defined in the #show() method so you can use the same object several
 * differente times with different identifiers. In all messages, the \a lParam
 * value inform about the dialog changes:
 * -# \c IMSG.DIALOG.CANCEL: Will be sent if the 'Cancel' button was touched.
 * -# \c IMSG.DIALOG.OK: Will be sent if the 'Ok' button was touched.
 * -# \c IMSG.DIALOG.DISMISS: This message is always sent after one of the
 * previous buttons has been touched. It is sent only when the dialog
 * dismisses. It will also be sent if the user cancels the dialog through the
 * 'Back' hardware button.
 * .
 * In all that messages the \a extra argument will carry this object instance.
 * So you can query about what was the answer of the user within its
 * properties.
 *//* --------------------------------------------------------------------- */
public class CAndroidPasswordDialog
    implements DialogInterface.OnCancelListener
{
    /** \name CONSTRUCTOR */ //@{
    // public CAndroidPasswordDialog(Activity owner, INHandler handler);/*{{{*/
    /**
     * Default constructor.
     * \param owner The Activity that owns this dialog.
     * \param handler The object that will handle this dialog messages.
     **/
    public CAndroidPasswordDialog(Activity owner, INHandler handler) {
        m_owner    = owner;
        m_target   = handler;
        m_typedPwd = null;
        m_dialogID = 0;
        m_button   = (int)IMSG.DIALOG.CANCEL;
    }/*}}}*/
    //@}

    /** \name ATTRIBUTES */ //@{
    // public final String getPassword();/*{{{*/
    /**
     * Gets the user typed password.
     * \note The result is never \b null. If the user didn't type anything
     * this function will return an empty string.
     **/
    public final String getPassword() {
        return ((m_typedPwd != null) ? m_typedPwd.getText().toString() : strings.EMPTY);
    }/*}}}*/
    // public final int    getButton();/*{{{*/
    /**
     * Gets the button touched by the user.
     * The result can have two codes:
     * - \c IMSG.DIALOG.OK: When the user closed the dialog by pressing the
     * 'OK' button.
     * - \c IMSG.DIALOG.CANCEL: When the user closed the dialog by pressing
     * the 'Cancel' button or the 'Back' hardware button.
     * .
     **/
    public final int getButton() {
        return m_button;
    }/*}}}*/
    // public final CAndroidPasswordDialog setCancelText(int resourceID);/*{{{*/
    /**
     * Set the text of the 'Cancel' button.
     * \param resourceID Identifier of the string resource with the text.
     * \return This function returns \c this, allowing you to chaining
     * anothers methods.
     * \note This method must be called before the #show() method.
     **/
    public final CAndroidPasswordDialog setCancelText(int resourceID) {
        return this.setCancelText(CAndroidApp.loadString(resourceID));
    }/*}}}*/
    // public final CAndroidPasswordDialog setCancelText(String text);/*{{{*/
    /**
     * Set the text of the 'Cancel' button.
     * \param text String with the button text.
     * \return This function returns \c this, allowing you to chaining
     * anothers methods.
     * \note This method must be called before the #show() method.
     **/
    public final CAndroidPasswordDialog setCancelText(String text) {
        m_cancelText = text;
        return this;
    }/*}}}*/
    // public final CAndroidPasswordDialog setOkText(int resourceID);/*{{{*/
    /**
     * Set the text of the 'Ok' button.
     * \param resourceID Identifier of the string resource with the text.
     * \return This function returns \c this, allowing you to chaining
     * anothers methods.
     * \note This method must be called before the #show() method.
     **/
    public final CAndroidPasswordDialog setOkText(int resourceID) {
        return this.setOkText(CAndroidApp.loadString(resourceID));
    }/*}}}*/
    // public final CAndroidPasswordDialog setOkText(String text);/*{{{*/
    /**
     * Set the text of the 'Ok' button.
     * \param text String with the button text.
     * \return This function returns \c this, allowing you to chaining
     * anothers methods.
     * \note This method must be called before the #show() method.
     **/
    public final CAndroidPasswordDialog setOkText(String text) {
        m_okText = text;
        return this;
    }/*}}}*/
    // public final CAndroidPasswordDialog setMessageText(int resourceID);/*{{{*/
    /**
     * Set the text shown above the password field.
     * \param resourceID Identifier of the string resource with the text.
     * \return This function returns \c this, allowing you to chaining
     * anothers methods.
     * \note This method must be called before the #show() method.
     **/
    public final CAndroidPasswordDialog setMessageText(int resourceID) {
        return this.setMessageText(CAndroidApp.loadString(resourceID));
    }/*}}}*/
    // public final CAndroidPasswordDialog setMessageText(String text);/*{{{*/
    /**
     * Set the text shown above the password field.
     * \param text String with the text to show.
     * \return This function returns \c this, allowing you to chaining
     * anothers methods.
     * \note This method must be called before the #show() method.
     **/
    public final CAndroidPasswordDialog setMessageText(String text) {
        m_msgText = text;
        return this;
    }/*}}}*/
    // public final CAndroidPasswordDialog setDialogTitle(int resourceID);/*{{{*/
    /**
     * Set the dialog title text.
     * \param resourceID Identifier of the string resource with the text.
     * \return This function returns \c this, allowing you to chaining
     * anothers methods.
     * \note This method must be called before the #show() method.
     **/
    public final CAndroidPasswordDialog setDialogTitle(int resourceID) {
        return this.setDialogTitle(CAndroidApp.loadString(resourceID));
    }/*}}}*/
    // public final CAndroidPasswordDialog setDialogTitle(String text);/*{{{*/
    /**
     * Set the dialog title text.
     * \param text String with the text to set.
     * \return This function returns \c this, allowing you to chaining
     * anothers methods.
     * \note This method must be called before the #show() method.
     **/
    public final CAndroidPasswordDialog setDialogTitle(String text) {
        m_titleText = text;
        return this;
    }/*}}}*/
    //@}

    /** \name OPERATIONS */ //@{
    // public final CAndroidPasswordDialog show(int dialogID);/*{{{*/
    /**
     * Show the dialog to the user.
     * \param dialogID The identifier of this dialog instance.
     * \return The function returns \c this.
     **/
    public final CAndroidPasswordDialog show(int dialogID) {
        final CAndroidPasswordDialog dialog = this;
        AlertDialog.Builder builder = new AlertDialog.Builder((Context)m_owner);

        if (strings.length(m_cancelText) == 0) m_cancelText = "Cancel";
        if (strings.length(m_okText) == 0) m_okText = "Ok";
        if (strings.length(m_titleText) == 0) m_titleText = "Password";
        m_dialogID = dialogID;

        builder.setTitle( m_titleText );
        builder.setNegativeButton(m_cancelText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int id) {
                debug.w("CPasswordDialog::onClick(): IMSG.DIALOG.CANCEL\n");
                handleButton( (int)IMSG.DIALOG.CANCEL );
            }
        });
        builder.setPositiveButton(m_okText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int id) {
                debug.w("CPasswordDialog::onClick(): IMSG.DIALOG.OK\n");
                handleButton( (int)IMSG.DIALOG.OK );
            }
        });
        builder.setOnCancelListener(this);
        builder.setView( buildView() );
        builder.show();
        return this;
    }/*}}}*/
    //@}

    /** \name DialogInterface IMPLEMENTATION */ //@{
    // public void onCancel(DialogInterface dialog);/*{{{*/
    /**
     * Handles the cancelation of the dialog, in general.
     **/
    public void onCancel(DialogInterface dialog) {
        this.handleButton( (int)IMSG.DIALOG.CANCEL );
    }/*}}}*/
    //@} DialogInterface IMPLEMENTATION

    /** \name IMPLEMENTATION */ //@{
    // private View buildView();/*{{{*/
    /**
     * Build the View with controls.
     **/
    private View buildView() {
        LinearLayout layout = new LinearLayout((Context)m_owner);
        int wrapContent     = ViewGroup.LayoutParams.WRAP_CONTENT;
        int fillParent      = ViewGroup.LayoutParams.MATCH_PARENT;
        layout.setOrientation( LinearLayout.VERTICAL );
        layout.setLayoutParams(new LinearLayout.LayoutParams(wrapContent, wrapContent));
        layout.setPadding(CAndroidApp.dp2px(8), 0, CAndroidApp.dp2px(8), CAndroidApp.dp2px(4));

        if (strings.length(m_msgText) > 0)
        {
            TextView textView = new TextView((Context)m_owner);
            textView.setMaxLines(4);
            textView.setText( m_msgText );

            layout.addView(textView, new LinearLayout.LayoutParams(wrapContent, wrapContent));
        }

        m_typedPwd = new EditText((Context)m_owner);
        layout.addView(m_typedPwd, new LinearLayout.LayoutParams(fillParent, wrapContent));

        m_typedPwd.setSingleLine();
        m_typedPwd.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        m_typedPwd.setImeOptions(EditorInfo.IME_ACTION_DONE);
        m_typedPwd.setTransformationMethod(new PasswordTransformationMethod());


        return layout;
    }/*}}}*/
    // private void handleButton(int buttonID);/*{{{*/
    /**
     * Handle the click on one of the dialog's buttons.
     * \param buttonID The button that was touched.
     **/
    private void handleButton(int buttonID) {
        m_button = buttonID;
        if (m_target != null) {
            debug.w("CAndroidPasswordDialog::handleButton(%d) posting message!\n", buttonID);
            issuer.post(m_target, IMSG.MSG_DIALOG, m_dialogID, buttonID, this);
        }
    }/*}}}*/
    //@}

    /** \name DATA FIELDS */ //@{
    private Activity  m_owner;          /**< The owner of this dialog.  */
    private INHandler m_target;         /**< Handler of the messages.   */
    private EditText  m_typedPwd;       /**< Password entered.          */
    private String    m_cancelText;     /**< Text of Cancel button.     */
    private String    m_okText;         /**< Text of Ok button.         */
    private String    m_titleText;      /**< Dialog title.              */
    private String    m_msgText;        /**< Message above EditView.    */
    private int       m_dialogID;       /**< Dialog identifier.         */
    private int       m_button;         /**< The user choice.           */
    //@}
}
// vim:syntax=java.doxygen
