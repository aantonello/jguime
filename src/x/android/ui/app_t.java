/**
 * \file
 * Defines the app_t class.
 *
 * \author Alessandro Antonello <aantonello@paralaxe.com.br>
 * \date   Outubro 26, 2012
 *
 * \par Copyright
 * This file is provided in hope that it will be useful to someone. It is
 * offered in public domain. You may use, modify or distribute it freely.
 *
 * The code is provided "AS IS". There is no warranty at all, of any kind. You
 * may change it if you like. Or just use it as it is.
 */
package x.android.ui;

import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.os.Handler;
import android.os.Message;

import android.content.res.Resources;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.app.Application;
import android.app.AlertDialog;

import android.util.DisplayMetrics;

/**
 * This class is a wrapper implementation of Android Application class.
 * Notice that this class will be available only if there is an specification
 * for that in the Android manifest file.
 *
 * The Application class has a single instance. You can get the built instance
 * for your app using the #get() function. Most of the functionality of this
 * class can be achieved using static functions.
 *//* --------------------------------------------------------------------- */
public class app_t extends Application
{
    /** \name CONSTRUCTOR */ //@{
    // public app_t();/*{{{*/
    /**
     * Default constructor.
     **/
    public app_t() {
        super();
        app_t.__self = this;
    }/*}}}*/
    //@}

    /** \name STATIC FUNCTIONS */ //@{
    // public static app_t get();/*{{{*/
    /**
     * Recover the single instance of this class and returns it.
     **/
    public static app_t get() {
        return app_t.__self;
    }/*}}}*/
    // public static DisplayMetrics displayMetrics();/*{{{*/
    /**
     * Gets the display metrics instance.
     **/
    public static DisplayMetrics displayMetrics() {
        Resources rsc = __self.getResources();
        return rsc.getDisplayMetrics();
    }/*}}}*/
    // public static Resources      resources();/*{{{*/
    /**
     * Get the instance of the Resources for this application.
     **/
    public static Resources resources() {
        return __self.getResources();
    }/*}}}*/
    //@}

    /** \name STATIC FILE OPERATIONS */ //@{
    // public static FileInputStream inputStream(String name);/*{{{*/
    /**
     * Opens a private file for reading.
     * \param name String with file name. This can not have any path
     * specification on it. Private files are always created in the same
     * folder of the application.
     * \return An instance of the \c FileInputStream class means success. \b
     * null indicates that an error occurred.
     **/
    public static FileInputStream inputStream(String name) {
        FileInputStream fis = null;

        try { fis = __self.openFileInput(name); }
        catch (Exception ex) {
            debug.w("app_t.inputStream(%s) error:", name);
            debug.e("=> %s", ex);
            return null;
        }
        return fis;
    }/*}}}*/
    // public static FileOutputStream ouputStream(String name);/*{{{*/
    /**
     * Opens a private file for write.
     * \param name String with file name. This can not have any path
     *      specification on it. Private files are always created in the same
     *      folder of the application.
     * \return An instance of the \c FileOutputStream class means success. \b
     * null indicates that an error occurred.
     **/
    public static FileOutputStream ouputStream(String name) {
        FileOutputStream fos = null;

        try { fos = __self.openFileOutput(name, Context.MODE_PRIVATE); }
        catch (Exception ex) {
            debug.w("app_t::outputStream(%s) error:", name);
            debug.e("=> %s", ex);
            return null;
        }
        return fos;
    }/*}}}*/
    //@}

    /** \name STATIC ALERT FUNCTIONS */ //@{
    // public static void alert(view_t view, String msg, int dlgID, int type);/*{{{*/
    /**
     * Builds and shows a simple Alert dialog.
     * \param view View wrapper to notify when the message dismiss.
     * \param msg Text of the message to show.
     * \param dlgID A dialog identifider. This can be any value if the
     *      view needs to uniquely identify the alert. This value is
     *      passed at the \c Message.arg1 field of the notification message.
     * \param type The alert type. Can be any constant in the \c ALERT#TYPE
     *      class. This value is passed in the \c Message.what member.
     * \remarks The dialog is show to the user and the function returns. The
     * client will be notified of the dialog result through a notification
     * message.
     **/
    public static void alert(view_t view, String msg, int dlgID, int type) {
        Context context = __self;
        final view_t target = view;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg).setCancelable(false);

        if ((type == ALERT.TYPE.INFO) || (type == ALERT.TYPE.CONFIRM)) {
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    if (target != null) post(target, type, dlgID, ALERT.BUTTON.OK);
                }
            });
            if (type == ALERT.TYPE.CONFIRM) {
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        if (target != null) post(target, type, dlgID, ALERT.BUTTON.CANCEL);
                    }
                });
            }
        }
        else if (type == ALERT.TYPE.QUESTION) {
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    postMsg(handler, MSG.DIALOG.QUESTION, dlgID, ALERT.BUTTON.YES);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    postMsg(handler, MSG.DIALOG.QUESTION, dlgID, ALERT.BUTTON.NO);
                }
            });
        }
        builder.show();
    }/*}}}*/
    //@}

    /** \name RESOURCES FUNCTIONS */ //@{
    // public final String resString(int stringID);/*{{{*/
    /**
     * Load a string from the application resources.
     * \param stringID The string identifier to load.
     * \return The string loaded or \b null, if the string is not found.
     **/
    public final String resString(int stringID) {
        Resources rsc = this.getResources();
        try { return rsc.getString(stringID); }
        catch (Exception ex) { return ""; }
    }/*}}}*/
    //@}

    /** \name FIELDS */ //@{
    protected static app_t __self;      /**< Holds the single instance.     */
    //@}
}
// vim:syntax=java.doxygen
