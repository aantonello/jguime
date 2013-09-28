/**
 * \file
 * Defines the CAndroidProgressDialog class.
 *
 * \author Alessandro Antonello <aantonello@paralaxe.com.br>
 * \date   Dezembro 14, 2012
 *
 * \par Copyright
 * This file is provided in hope that it will be useful to someone. It is
 * offered in public domain. You may use, modify or distribute it freely.
 *
 * The code is provided "AS IS". There is no warranty at all, of any kind. You
 * may change it if you like. Or just use it as it is.
 */
package x.android.ui;

import android.app.*;
import android.content.*;
import android.content.res.*;

import android.view.*;
import android.widget.*;

import x.android.defs.*;
import x.android.utils.*;
import x.android.nms.*;

/**
 * Implements a dialog with a progress meter and a text message.
 * The dialog can be shown in two ways: First, you construct in instance of
 * the class using the solely available constructor. After construct you can
 * use one of #setText() methods to define the message to be shown, and then,
 * call the #show() function. Or, you can call the #show(Context,int,INHandler)
 * static method directly. This method builds the class instance and show it,
 * returning the instance created. You can set the message text after the
 * dialog is shown. By the way, you can change the text shown at any time
 * between creation and deletion of the dialog class object.
 *
 * The progress is indeterminate, by default. In this implementation it cannot
 * be changed, yet.
 *
 * If the **INHandler** instance is not null, the class sends some information
 * about its events to the target handler. When the dialog starts it sends a
 * notification message using \ref IMSG::MSG_DIALOG as identifier. The
 * *nParam* parameter will carrie the dialog identifier, as defined in the
 * constructor or in the `show()` static method. The *lParam* parater will
 * have the constant value \ref IMSG::DIALOG::STARTED and the *extra*
 * parameter will have a reference to the class `CAndroidProgressDialog`
 * starting.
 *
 * The dialog doesn't show any button to the user. But it can be canceled
 * using the hardware **back** button. When it is canceled, the target handler
 * will receive a notification message identified by the constant value
 * `IMSG::MSG_DIALOG`. The *nParam* parameter will be set with the dialog
 * identifier, the *lParam* parameter will have the value of \ref
 * IMSG::DIALOG::CANCEL and the *extra* parameter a reference to the
 * CAndroidProgressDialog object.
 *//* --------------------------------------------------------------------- */
public class CAndroidProgressDialog
    implements DialogInterface.OnDismissListener
{
    /** \name CONSTRUCTOR */ //@{
    // public CAndroidProgressDialog(Context context, int id, INHandler target);/*{{{*/
    /**
     * Initializes this object.
     * \param context The current Context. Usually this is the Activity
     * showing this progress dialog.
     * \param id A value that identifies this dialog among others.
     * \param target An object implementing \c INHandler to receive
     * notifications.
     **/
    public CAndroidProgressDialog(Context context, int id, INHandler target) {
        m_dialogID = id;
        m_target   = target;
        m_cancelDismiss = true;
        m_dialog   = new CAndroidProgressDialog.Implementation(context);

        m_dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        m_dialog.setIndeterminate(true);
        m_dialog.setCancelable(true);
        m_dialog.setOnDismissListener(this);
    }/*}}}*/
    //@}

    /** \name PUBLIC STATIC FUNCTIONS */ //@{
    // public static CAndroidProgressDialog show(Context context, int id, INHandler target);/*{{{*/
    /**
     * Builds and show a progress dialog.
     * \param context The current Context. Usually this is the Activity
     * showing this progress dialog.
     * \param id A value that identifies this dialog among others.
     * \param target An object implementing \c INHandler to receive
     * notifications.
     * \returns A \c CAndroidProgressDialog instance.
     **/
    public static CAndroidProgressDialog show(Context context, int id, INHandler target) {
        CAndroidProgressDialog self = new CAndroidProgressDialog(context, id, target);

        self.show();
        return self;
    }/*}}}*/
    //@}

    /** \name ATTRIBUTES */ //@{
    // public final void setText(String textMessage);/*{{{*/
    /**
     * Sets or changes the text showing of the dialog.
     * \param textMessage The text to be shown.
     **/
    public final void setText(String textMessage) {
        m_dialog.setMessage(textMessage);
    }/*}}}*/
    // public final void setText(int textID);/*{{{*/
    /**
     * Sets or changes the text shoing in the dialog.
     * \param textID Identifier of a resource string.
     **/
    public final void setText(int textID) {
        this.setText(CAndroidApp.loadString(textID));
    }/*}}}*/
    // public final int  style(int st);/*{{{*/
    /**
     * Sets the style of the progress in the dialog.
     * \param st The style to be used. Can be:
     * - **CAndroidProgressDialog.INDETERMINATE**: This is the default.
     * - **CAndroidProgressDialog.PROGRESS**: Standard progress bar. This
     *   style needs the application to update the current position.
     * .
     * \returns The result is the style before the change be made.
     **/
    public final int  style(int st) {
        int result = this.style();
        if (st == PROGRESS)
        {
            m_dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            m_dialog.setIndeterminate( false );
        }
        else
        {
            m_dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            m_dialog.setIndeterminate( true );
        }
        return result;
    }/*}}}*/
    // public final int  style();/*{{{*/
    /**
     * Just gets the current dialog progress style.
     * \returns The result can be:
     * \retval INDETERMINATE This is the default progress style.
     * \retval PROGRESS A standard progress bar.
     **/
    public final int  style() {
        return (m_dialog.isIndeterminate() ? INDETERMINATE : PROGRESS);
    }/*}}}*/
    // public final int  maxValue(int max);/*{{{*/
    /**
     * Sets the maximum value when the progress style is `PROGRESS`.
     * \param max Maximum value to the progress bar.
     * \returns The maximum value before the change been made.
     **/
    public final int maxValue(int max) {
        int value = m_dialog.getMax();
        m_dialog.setMax(max);
        return value;
    }/*}}}*/
    // public final int  maxValue();/*{{{*/
    /**
     * Maximum value when the dialog style is `PROGRESS`.
     * \returns The current defined maximum progress value.
     **/
    public final int maxValue() {
        return m_dialog.getMax();
    }/*}}}*/
    // public final boolean dismissOnCancel(boolean dismiss);/*{{{*/
    /**
     * Defines if the hardware *back* button will dismiss the dialog.
     * The default value of this property is **true**. That is, when created,
     * the dialog is configured to be automatically dismissed when the *back*
     * button is pressed.
     * \param dismiss When **true** the dialog will be automaticaly dismissed
     * when the *back* button is pressed. If **false** the application will be
     * resposible to dismiss the dialog using the \ref dismiss() method.
     * \returns Returns **true** if the dialog was defined to be automaticaly
     * dismissed before the change. Otherwise **false**.
     **/
    public final boolean dismissOnCancel(boolean dismiss) {
        boolean value = m_cancelDismiss;
        m_cancelDismiss = dismiss;
        return value;
    }/*}}}*/
    // public final boolean dismissOnCancel();/*{{{*/
    /**
     * Gets the current configuration of automaticaly dismiss funcionality.
     * \returns **true** if the dialog is configured to be dismissed when the
     * *back* hardware button is pressed. This is the default behavior.
     * Otherwise this method returns **false**.
     **/
    public final boolean dismissOnCancel() {
        return m_cancelDismiss;
    }/*}}}*/
    //@}

    /** \name OPERATIONS */ //@{
    // public final int  position(int pos);/*{{{*/
    /**
     * Changes the position of the progress bar.
     * This method can be used only when the style of the dialog is
     * `PROGRESS`.
     * \param pos The position to be set. This value must be between **0** and
     * the maximum value defined with the method \ref maxValue(int). If no
     * maximum value was set, the default is **100**.
     * \returns The value of the progress bar before the change.
     **/
    public final int position(int pos) {
        int value = m_dialog.getProgress();
        m_dialog.incrementProgressBy(pos - value);
        return value;
    }/*}}}*/
    // public final void show();/*{{{*/
    /**
     * Starts the dialog and show it on screen.
     **/
    public final void show() {
        m_dialog.show();
    }/*}}}*/
    // public final void dismiss();/*{{{*/
    /**
     * Dismisses the dialog, closing it.
     **/
    public final void dismiss() {
        m_dialog.dismiss();
    }/*}}}*/
    //@}

    /** \name DialogInterface.OnDismissListener IMPLEMENTATION */ //@{
    // public void onDismiss(DialogInterface dialog);/*{{{*/
    /**
     * Intercepta a finalização do diálogo.
     **/
    public void onDismiss(DialogInterface dialog) {
        if (m_target == null) return;
        issuer.post(m_target, IMSG.MSG_DIALOG, m_dialogID, IMSG.DIALOG.DISMISS, this);
    }/*}}}*/
    //@}

    /** \name DATA MEMBERS */ //@{
    protected int          m_dialogID;  /**< Dialog identifier.              */
    protected boolean m_cancelDismiss;  /**< Dismiss when *back* is pressed. */
    protected INHandler      m_target;  /**< Notifications messages target.  */
    protected Implementation m_dialog;  /**< Dialog implementation instance. */
    //@}

    /** \name PUBLIC CONSTANTS */ //@{
    public static final int INDETERMINATE = 0;  /**< Progress style indeterminate. */
    public static final int PROGRESS      = 1;  /**< Standar progress bar.         */
    //@}

    /** \name INNER CLASSES */ //@{
    // public class Implementation extends ProgressDialog;/*{{{*/
    /**
     * Implements the progress dialog it self.
     **/
    public class Implementation extends ProgressDialog
    {
        /** \name CONSTRUCTOR */ //@{
        // public Implementation(Context context);/*{{{*/
        /**
         * Constructs the dialog.
         **/
        public Implementation(Context context) {
            super(context);
        }/*}}}*/
        //@}

        /** \name Dialog OVERRIDES */ //@{
        // public void onStart();/*{{{*/
        /**
         * Called when the dialog is starting.
         **/
        public void onStart() {
            if (m_target != null) {
                issuer.post(m_target, IMSG.MSG_DIALOG, m_dialogID, IMSG.DIALOG.STARTED, CAndroidProgressDialog.this);
            }
            super.onStart();
        }/*}}}*/
        // public void onBackPressed();/*{{{*/
        /**
         * Called when the **back** hardware button is pressed.
         * Dismiss the dialog when `m_cancelDismiss` is **true**. Otherwise
         * only sends the notification message.
         **/
        public void onBackPressed() {
            if (m_target != null) {
                issuer.post(m_target, IMSG.MSG_DIALOG, m_dialogID, IMSG.DIALOG.CANCEL, CAndroidProgressDialog.this);
            }
            if (m_cancelDismiss) {
                super.onBackPressed();
            }
        }/*}}}*/
        //@}
    }/*}}}*/
    //@}
}
// vim:syntax=java.doxygen
