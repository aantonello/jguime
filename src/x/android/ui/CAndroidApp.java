/**
 * \file
 * Defines the CAndroidApp class.
 *
 * \author Alessandro Antonello <aantonello@paralaxe.com.br>
 * \date   October 04, 2010
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
import java.io.*;
import java.nio.channels.*;
import java.util.*;

import android.os.*;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.content.res.Configuration;
import android.content.pm.PackageManager;
import android.content.pm.PackageInfo;

import android.graphics.drawable.Drawable;

import android.view.View;
import android.widget.Toast;

import android.app.Application;
import android.app.AlertDialog;

import android.util.DisplayMetrics;

import x.android.defs.*;
import x.android.utils.*;
import x.android.nms.*;

/* }}} #imports */
/**
 * \ingroup x_android_ui
 * Represents an Android application.
 * There must be only one android application in a package bundle. For this
 * reason, this class has an static member of it self. This reference can be
 * retrieved from the \c currentApp() static function.
 *//* --------------------------------------------------------------------- */
public class CAndroidApp extends android.app.Application
{
    /** \name CONSTRUCTOR */ //@{
    // public CAndroidApp();/*{{{*/
    /**
     * Default constructor.
     **/
    public CAndroidApp() {
        super();
        CAndroidApp.__this = this;
    }/*}}}*/
    //@}

    /** \name PUBLIC STATIC FUNCTIONS */ //@{
    // public static CAndroidApp currentApp();/*{{{*/
    /**
     * Gets the only instance of the Android Application.
     **/
    public static CAndroidApp currentApp() {
        return CAndroidApp.__this;
    }/*}}}*/
    // public static DisplayMetrics getDisplayMetrics();/*{{{*/
    /**
     * Retrieves the current display metrics for the application.
     * \returns An instance of the only \c DisplayMetrics object.
     **/
    public static DisplayMetrics getDisplayMetrics() {
        Resources res = __this.getResources();
        return res.getDisplayMetrics();
    }/*}}}*/
    // public static int dp2px(int dbValue);/*{{{*/
    /**
     * Calculates a value from dp units to px units.
     * The calculation take account the current density of the screen.
     * \param dpValue The value in dp units to be converted.
     **/
    public static int dp2px(int dpValue) {
        return (int)(dpValue * __this.density);
    }/*}}}*/
    //@}

    /** \name PRIVATE FILE OPERATIONS */ //@{
    // public static FileInputStream openForInput(String name);/*{{{*/
    /**
     * Opens a private file for reading.
     * \param name String with file name. This can not have any path
     *      specification on it. Private files are always created in the same
     *      folder of the application.
     * \return An instance of the \c FileInputStream class means success. \b
     * null indicates that an error occurred. Usually this happens when the
     * file does not exists.
     **/
    public static FileInputStream openForInput(String name) {
        FileInputStream fis = null;

        try { fis = __this.openFileInput(name); }
        catch (Exception ex) {
            debug.w("CAndroidApp::openForInput(%s) error:", name);
            debug.e("=> %s", ex);
            return null;
        }
        return fis;
    }/*}}}*/
    // public static FileOutputStream openForWrite(String name);/*{{{*/
    /**
     * Opens a private file for write.
     * \param name String with file name. This can not have any path
     *      specification on it. Private files are always created in the same
     *      folder of the application.
     * \return An instance of the \c FileOutputStream class means success. \b
     * null indicates that an error occurred. Usually this happens when the
     * file could not be created for some reason. There is no way to recover
     * from this error without the user help.
     **/
    public static FileOutputStream openForWrite(String name)
    {
        FileOutputStream fos = null;

        try { fos = __this.openFileOutput(name, Context.MODE_PRIVATE); }
        catch (Exception ex) {
            debug.e(ex, "$n in CAndroidApp::openForWrite('%s'): $s\n", name);
        }
        return fos;
    }/*}}}*/
    // public static FileOutputStream openForAppend(String name);/*{{{*/
    /**
     * Opens a file for appending data.
     * When the file doesn't exsits it will be created. If the file already
     * exists, data written will be appended to the current file content.
     * @param name Name of the file. Just name, without path information.
     * @returns FileOutputStream object or \b null, if the function fails.
     **/
    public static FileOutputStream openForAppend(String name)
    {
        FileOutputStream fos = null;

        try { fos = __this.openFileOutput(name, Context.MODE_APPEND); }
        catch (Exception ex) {
            debug.e(ex, "$n in CAndroidApp::openForAppend('%s'): $s\n", name);
        }
        return fos;
    }/*}}}*/
    // public static boolean deletePrivateFile(String name);/*{{{*/
    /**
     * Deletes a private file created in the application context.
     * @param name Name of the file to delete.
     * @returns \b true when the file was deleted successful. \b false
     * otherwise.
     **/
    public static boolean deletePrivateFile(String name)
    {
        if (name == null || name.length() == 0) return false;
        return __this.deleteFile(name);
    }/*}}}*/
    // public static boolean movePrivateFile(String fromName, String toName);/*{{{*/
    /**
     * Move files in the private area of the application.
     * @param fromName Original file name.
     * @param toName New name for the file.
     * @return \b true on success. \b false otherwise. If the function
     * succeeds, the original file will be deleted.
     **/
    public static boolean movePrivateFile(String fromName, String toName)
    {
        FileOutputStream fos = openForWrite(toName);
        FileInputStream  fis = openForInput(fromName);
        FileChannel channelOut = ((fos != null) ? fos.getChannel() : null);
        FileChannel channelIn  = ((fis != null) ? fis.getChannel() : null);
        boolean result = false;

        if ((channelOut != null) && (channelIn != null))
        {
            try {
                channelIn.transferTo(0, channelIn.size(), channelOut);
                result = true;
            } catch (Exception ex) {
                debug.e(ex, "$n in CAndroidApp::movePrivateFile('%s', '%s'): $s\n", fromName, toName);
            }

            try { channelIn.close(); }
            catch (Exception ex) { /* Ignored. Stupid exception. */ }

            try { channelOut.close(); }
            catch (Exception ex) { /* Ignored too. More stupid exception. */ }
        }

        if (fis != null)
        {
            try { fis.close(); }
            catch (Exception ex) { /* Ignored. What is the meaning of this? */ }
        }

        if (fos != null)
        {
            try { fos.close(); }
            catch (Exception ex) { /* Ignored work of a stupid developer. */ }
        }

        if (result) deletePrivateFile(fromName);
        return result;
    }/*}}}*/
    //@}

    /** \name OVERRIDES */ //@{
    // public void onCreate();/*{{{*/
    /**
     * Called when the application is created.
     **/
    public void onCreate()
    {
        super.onCreate();

        debug.w("\nAndroid Version:\n");
        debug.w("@@ CODENAME:'%s' INCREMENTAL:'%s' RELEASE:'%s'\n",
                Build.VERSION.CODENAME, Build.VERSION.INCREMENTAL,
                Build.VERSION.RELEASE);
        debug.w("@@ BOARD: '%s'\n", Build.BOARD);
        debug.w("@@ BRAND: '%s'\n", Build.BRAND);
        debug.w("@@ DEVICE: '%s'\n", Build.DEVICE);

        CStringTable strt = CStringTable.LoadAsset("version.xml");
        debug.w("@@ SDK: '%s'\n", strt.get(Build.VERSION.SDK_INT));

        DisplayMetrics dm = this.getDisplayMetrics();

        this.displayWidth  = dm.widthPixels;
        this.displayHeight = dm.heightPixels;
        this.density       = dm.density;
        this.fontScale     = dm.scaledDensity;
        this.xdpi          = dm.xdpi;
        this.ydpi          = dm.ydpi;

        debug.w("\nDisplay properties dump:\n");
        debug.w("@@ Logical density:....: %01.2f\n", this.density);
        debug.w("@@ Font scaling factor.: %01.2f\n", this.fontScale);
        debug.w("@@ Horizontal DPI......: %01.2f\n", this.xdpi);
        debug.w("@@ Vertical DPI........: %01.2f\n", this.ydpi);
        debug.w("@@ Width in pixels.....: %d\n", this.displayWidth);
        debug.w("@@ Height in pixels....: %d\n\n", this.displayHeight);

        debug.w("CAndroidApp::onCreate()\n");

//        Properties properties = System.getProperties();
//        Enumeration<String> keys = (Enumeration<String>)properties.propertyNames();
//        String name;
//
//        while (keys.hasMoreElements()) {
//            name = keys.nextElement();
//            debug.w("Property: '%s' = '%s'\n", name, properties.getProperty(name));
//        }

    }/*}}}*/
    //@}

    /** \name ALERTS */ //@{
    // public static void info(Context ctx, String msg, boolean longDuration);/*{{{*/
    /**
     * Shows an info dialog to the user.
     * As info dialog Android calls it Toast.
     * \param ctx The current Context. Usually this is an Activity reference.
     * If \b null, the Application context will be used.
     * \param msg The string message.
     * \param longDuration There are two kinds of duration available for a
     * Toast: short or long. If you want a short duration Toast, pass \c false
     * to this parameter. Otherwise, pass \b true.
     **/
    public static void info(Context ctx, String msg, boolean longDuration)
    {
        if (ctx == null) ctx = (Context)currentApp();
        Toast toast = Toast.makeText(ctx, msg, (longDuration ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT));
        toast.show();
    }/*}}}*/
    // public static void info(Context ctx, int msg, boolean longDuration);/*{{{*/
    /**
     * Shows an info dialog to the user.
     * As info dialog Android calls it Toast.
     * \param ctx The current Context. Usually this is an Activity reference.
     * If \b null, the Application context will be used.
     * \param msg The string message identifier in the resource.
     * \param longDuration There are two kinds of duration available for a
     * Toast: short or long. If you want a short duration Toast, pass \c false
     * to this parameter. Otherwise, pass \b true.
     **/
    public static void info(Context ctx, int msg, boolean longDuration) {
        info(ctx, loadString(msg), longDuration);
    }/*}}}*/
    // public static void alert(Context ctx, String msg, int nParam, INHandler handler);/*{{{*/
    /**
     * Shows an Alert Dialog to the user.
     * An alert message shows the information and only one 'OK' button.
     * \param ctx The Activity's Context. If \b null the Application Context
     * will be used.
     * \param msg String with the message to be shown.
     * \param nParam Value to use in the parameter \c nParam of the
     * notification message.
     * \param handler Optional. The \c INHandler instance which will
     * receive the notification when the dialog is dismissed. If \b null no
     * message will be sent.
     * \remarks When the \a handler argument is given, a notification message
     * with code \c IMSG#MSG_INFO will be sent. The \c nParam parameter of the
     * message will carrie the value of the \a nParam argument.
     **/
    public static void alert(Context ctx, String msg, final int nParam, final INHandler handler)
    {
        if (ctx == null) ctx = (Context)currentApp();

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setMessage(msg).setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                if (handler != null) {
                    issuer.post(handler, IMSG.MSG_INFO, nParam, 0, null);
                }
            }
        });
        builder.show();
    }/*}}}*/
    // public static void alert(Context ctx, int msgID, int nParam, INHandler handler);/*{{{*/
    /**
     * Shows an AlertDialog with a message.
     * \param ctx Context to create the Dialog. If \b null the Application
     * Context will be used.
     * \param msgID Identifier of the string, in the application resources,
     * with the message to show.
     * \param nParam Value to use in the parameter \c nParam of the
     * notification message.
     * \param handler Optional. The \c INHandler instance which will
     * receive the notification when the dialog is dismissed. If \b null no
     * message will be sent.
     * \remarks When the \a handler argument is given, a notification message
     * with code \c IMSG#MSG_INFO will be sent. The \c nParam parameter of the
     * message will carrie the value of the \a nParam argument.
     **/
    public static void alert(Context ctx, int msgID, int nParam, final INHandler handler) {
        alert(ctx, loadString(msgID), nParam, handler);
    }/*}}}*/
    // public static void query(Context ctx, String msg, int nParam, INHandler handler);/*{{{*/
    /**
     * Presents a Dialog with a query to the user.
     * The query will show the message and include two buttons: 'Yes' and
     * 'No'.
     * \param ctx The Activity's Context. If \b null the Application Context
     * will be used.
     * \param msg String with the query to show to the user.
     * \param nParam Value to be passed in the \c nParam parameter of the
     * notification message.
     * \param handler Target of the notification message. When the user
     * chooses a button, yes or no, a notification is posted to this target.
     * The value of \c msgID parameter will be \c IMSG#MSG_QUERY. The value of
     * \c nParam parameter will be the value of \a nParam argument and the
     * value of \c lParam parameter will be \c ALERT.BUTTON.YES or \c
     * ALERT.BUTTON.NO, according to the users choice.
     **/
    public static void query(Context ctx, String msg, final int nParam, final INHandler handler)
    {
        if (ctx == null) ctx = (Context)currentApp();

        String yesLabel = CAndroidApp.loadString(android.R.string.yes);
        String noLabel  = CAndroidApp.loadString(android.R.string.no);

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setMessage(msg);
        builder.setPositiveButton(yesLabel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                issuer.post(handler, IMSG.MSG_QUERY, nParam, ALERT.BUTTON.YES, null);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(noLabel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                issuer.post(handler, IMSG.MSG_QUERY, nParam, ALERT.BUTTON.NO, null);
                dialog.dismiss();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                issuer.post(handler, IMSG.MSG_QUERY, nParam, ALERT.BUTTON.NO, null);
            }
        });
        builder.show();
    }/*}}}*/
    // public static void query(Context ctx, int msgID, int nParam, INHandler handler);/*{{{*/
    /**
     * Presents a Dialog with a query to the user.
     * The query will be shown with the buttons 'Ok' and 'Cancel'.
     * \param ctx The Activity's Context. If \b null the Application Context
     * will be used.
     * \param msgID Identifier of the string query in the application
     * resource.
     * \param nParam Value to be passed in the \c nParam parameter of the
     * notification message.
     * \param handler Target of the notification message. When the user
     * chooses a button, yes or no, a notification is posted to this target.
     * The value of \c msgID parameter will be \c IMSG#MSG_QUERY. The value of
     * \c nParam parameter will be the value of \a nParam argument and the
     * value of \c lParam parameter will be \c ALERT.BUTTON.YES or \c
     * ALERT.BUTTON.NO, according to the users choice.
     **/
    public static void query(Context ctx, int msgID, int nParam, INHandler handler) {
        query(ctx, loadString(msgID), nParam, handler);
    }/*}}}*/
    //@}

    /** \name RESOURCES */ //@{
    // public static String getMessage(int errCode);/*{{{*/
    /**
     * Gets an error description of an error code.
     * \param errCode Error code.
     * \return When succeeded the function returns a string with the error
     * code. If the description message is not found, the function returns a
     * generic message.
     **/
    public static String getMessage(int errCode)
    {
        CStringTable table = CStringTable.LoadAsset("errors.xml");
        String     message = table.get(errCode);
        if (message == null) {
            message = table.get(ERROR.FAILED);
        }
        return message;
    }/*}}}*/
    // public static String loadString(int resID);/*{{{*/
    /**
     * Loads a simple string form the application resources.
     * \param resID The string identifier to load.
     * \returns The string object or an empty string if the specified
     * identifier is not found.
     **/
    public static String loadString(int resID) {
        Resources rsc = __this.getResources();
        try { return rsc.getString(resID); }
        catch (Exception ex) { return ""; }
    }/*}}}*/
    // public static String Format(int resID, Object... vList);/*{{{*/
    /**
     * Formats a series of values into a string loaded from a resource.
     * \param resID The string to load from a resource. This string must have
     * format specifications according to the rules defined by the
     * `String.format()` functions.
     * \param vList List of values to be formated in the loaded string.
     * \returns The result string will have the values formated according the
     * specification.
     **/
    public static String Format(int resID, Object... vList) {
        return strings.format(loadString(resID), vList);
    }/*}}}*/
    // public static String appVersionInfo();/*{{{*/
    /**
     * Gets the version number defined in the Manifest file.
     **/
    public static String appVersionInfo() {
        PackageManager pm = __this.getPackageManager();
        PackageInfo    pi = null;

        try { pi = pm.getPackageInfo(__this.getPackageName(), 0); }
        catch (Exception ex) { return "0.0.0.0"; }

        return pi.versionName;
    }/*}}}*/
    // public static String countryCode();/*{{{*/
    /**
     * Gets the country code for the current locale.
     **/
    public static String countryCode() {
        return __this.getResources().getConfiguration().locale.getCountry();
    }/*}}}*/
    // public static Drawable loadDrawable(int resID);/*{{{*/
    /**
     * Loads a Drawable from the application resource.
     * @param resID The drawable resource identifier.
     * @returns The object loaded or \b null if the identifier is not valid or
     * do not point to a valid drawable resource.
     **/
    public static Drawable loadDrawable(int resID)
    {
        Resources rsc = __this.getResources();
        try { return rsc.getDrawable(resID); }
        catch (Exception ex) { return null; }
    }/*}}}*/
    //@}

    /** \name EXTERNAL STORAGE ACCESS */ //@{
    // public static boolean isStorageReadable();/*{{{*/
    /**
     * Get a value defining if the external storage path is avaiable for
     * reading access.
     * @returns \b true when the external storage can be read. Otherwise \b
     * false.
     **/
    public static boolean isStorageReadable()
    {
        String state = Environment.getExternalStorageState();
        return (state.equals(Environment.MEDIA_MOUNTED) ||
                state.equals(Environment.MEDIA_MOUNTED_READ_ONLY));
    }/*}}}*/
    // public static boolean isStorageWriteable();/*{{{*/
    /**
     * Gets a value defining if the external storage path can be written.
     * @return \b true if the external storage is available for written.
     * Otherwise \b false.
     **/
    public static boolean isStorageWriteable()
    {
        String state = Environment.getExternalStorageState();
        return (state.equals(Environment.MEDIA_MOUNTED));
    }/*}}}*/
    // public static File    openDirectory(String type);/*{{{*/
    /**
     * Opens an external storage directory.
     * The storage must be available. Check the values of #isStorageReadable()
     * or #isStorageWriteable() as of you need.
     * @param type Type of the directory to open. There are several
     * directories available. The list if found at \ref x_android_defs group
     * in the RES::STORAGE class.
     * @return On success a \b File object pointing to the directory.
     * Otherwise \b null.
     **/
    public static File openDirectory(String type)
    {
        return Environment.getExternalStoragePublicDirectory(type);
    }/*}}}*/
    //@}

    /** \name PUBLIC FIELDS */ //@{
    public float fontScale;         /**< Font size scale in this device.    */
    public float xdpi;              /**< Horizontal DPI of the display.     */
    public float ydpi;              /**< Vertical DPI of the display.       */
    public float density;           /**< Logical screen density.            */
    public int   displayWidth;      /**< Display width, in pixels.          */
    public int   displayHeight;     /**< Display height, in pixels.         */
    //@}

    /** \name PRIVATE MEMBERS */ //@{
    protected static CAndroidApp __this;    /**< This application instance. */
    //@}
}
// vim:syntax=java.doxygen
