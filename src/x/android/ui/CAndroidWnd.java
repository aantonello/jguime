/**
 * \file CAndroidWnd.java
 * Defines the CAndroidWnd class.
 *
 * \author Alessandro Antonello <aantonello@paralaxe.com.br>
 * \date   October 07, 2010
 *
 * \par Copyright
 * This file is provided in hope that it will be useful to someone. It is
 * offered in public domain. You may use, modify or distribute it freely.
 *
 * The code is provided "AS IS". There is no warranty at all, of any kind. You
 * may change it if you like. Or just use it as it is.
 */
package x.android.ui;

import android.app.Activity;

import android.content.Intent;
import android.content.Context;
import android.content.ContextWrapper;

import android.os.Handler;
import android.os.Message;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;

import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.SeekBar;

/**
 * Base class for creating and manipulating Activities.
 * The goals of this base class is to provide basic general functionality. In
 * most cases all communications done from threads and activities are based on
 * callbacks. These callbacks are implemented through the Handler class and
 * the Handler.Callback interface available in the Android OS. The
 * CAndroidWnd class implements the interface and provides a field for the
 * Handler class instance.
 *//* --------------------------------------------------------------------- */
public class CAndroidWnd extends Activity implements Handler.Callback,
       View.OnClickListener, View.OnFocusChangeListener,
       AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener,
       CompoundButton.OnCheckedChangeListener,
       SeekBar.OnSeekBarChangeListener
{
    /** \name CONSTRUCTOR */ //@{
    // public CAndroidWnd();/*{{{*/
    /**
     * Default constructor.
     **/
    public CAndroidWnd() {
        super();
        m_handler = null;
    }/*}}}*/
    //@}

    /** \name PUBLIC STATIC FUNCTIONS */ //@{
    // public static void createChildWnd(Context parent, Class<?> wndClass, int wndID);/*{{{*/
    /**
     * Creates a new Intent to show a child Activity.
     * \param parent The parent Activity to create the Intent.
     * \param wndClass The Activity class. Should be based on CAndroidWnd, but
     * this is not required.
     * \param wndID The identifier of the Activity. This can be used to
     * recognize the Activity when it is closed. The result will be passed
     * through the \c onActivityResult() function.
     **/
    public static void createChildWnd(Activity parent, Class<?> wndClass, int wndID) {
        Intent it = new Intent(parent, wndClass);
        parent.startActivityForResult(it, wndID);
    }/*}}}*/
    //@}

    /** \name ATTRIBUTES */ //@{
    // public final Handler getViewHandler();/*{{{*/
    /**
     * Gets a reference to the local handler of this view.
     * This handler is particular to this view. It's server to handle special
     * messages. Don't get confused with the global handler obtained through
     * the \c View.getHandler() function.
     **/
    public final Handler getViewHandler() {
        if (m_handler == null) {
            m_handler = new Handler((Handler.Callback)this);
        }
        return m_handler;
    }/*}}}*/
    // public final Message getMessage(int what, int arg1, int arg2, Object obj);/*{{{*/
    /**
     * Gets a \c Message instance from the \c Handler repository.
     * \param what The message code.
     * \param arg1 First message argument.
     * \param arg2 Second message argument.
     * \param obj An object. Can be anything, including \b null.
     **/
    public final Message getMessage(int what, int arg1, int arg2, Object obj) {
        Handler handler = getViewHandler();
        return handler.obtainMessage(what, arg1, arg2, obj);
    }/*}}}*/
    // public View     getView(int viewID);/*{{{*/
    /**
     * Finds a view through it's identifier.
     * This function is same as \c View.findViewById(), but shorter.
     * \param viewID The required View identifier.
     **/
    public View getView(int viewID) {
        return super.findViewById(viewID);
    }/*}}}*/
    // public TextView getTextView(int viewID);/*{{{*/
    /**
     * Gets a TextView from a child View.
     * \param viewID The required View identifier.
     **/
    public TextView getTextView(int viewID) {
        return (TextView)getView(viewID);
    }/*}}}*/
    // public EditText getEditView(int viewID);/*{{{*/
    /**
     * Gets a EditText from a child View.
     * \param viewID The required View identifier.
     **/
    public EditText getEditView(int viewID) {
        return (EditText)getView(viewID);
    }/*}}}*/
    // public Button   getButtonView(int viewID);/*{{{*/
    /**
     * Gets a Button from a child View.
     * \param viewID The required View identifier.
     **/
    public Button getButtonView(int viewID) {
        return (Button)getView(viewID);
    }/*}}}*/
    // public String   getViewString(int widgetID);/*{{{*/
    /**
     * Returns a string from an EditText or TextView widget.
     * \param widgetID The identifier of the view.
     * \return A string with the text of the widget or \b null, if the widget
     * does not have any text or was not found.
     **/
    public String  getViewString(int widgetID) {
        TextView tv = (TextView)getView( widgetID );
        if (tv == null) return null;
        return tv.getText().toString();
    }/*}}}*/
    // public boolean  getViewCheckState(int widgetID);/*{{{*/
    /**
     * Returns the checked state of a \c CheckBox widget.
     * \param widgetID Identifier of the view.
     * \return \b true if the checkbox is checked. Otherwise \b false.
     **/
    public boolean getViewCheckState(int widgetID) {
        CheckBox cb = (CheckBox)getView( widgetID );
        return cb.isChecked();
    }/*}}}*/
    // public void     setViewString(int widgetID, String text);/*{{{*/
    /**
     * Changes the text of a TextView or EditText widget.
     * \param widgetID The identifier of the view.
     * \param text The string to set in the widget.
     **/
    public void setViewString(int widgetID, String text) {
        TextView tv = (TextView)getView( widgetID );
        if (tv == null) return;
        tv.setText( text );
    }/*}}}*/
    // public void     setViewCheckState(int widgetID, boolean check);/*{{{*/
    /**
     * Sets the checked state of a CheckBox widget.
     * \param widgetID The identifier of the view.
     * \param check The state to set.
     **/
    public void setViewCheckState(int widgetID, boolean check) {
        CheckBox cb = (CheckBox)getView(widgetID);
        if (cb == null) return;
        cb.setChecked( check );
    }/*}}}*/
    // public void     setFocus(int widgetID);/*{{{*/
    /**
     * Change the current focus to the specified view.
     * \param widgetID The identifier of the view.
     **/
    public void setFocus(int widgetID) {
        View v = getView(widgetID);
        if (v == null) return;
        v.requestFocus();
    }/*}}}*/
    // public void     setFieldError(int viewID, int errID, boolean focus);/*{{{*/
    /**
     * Sets the field error state.
     * \param viewID The view identifier.
     * \param errID The identifier of the error message in the resource. This
     * is a standard Android resource.
     * \param focus Set to \b true if the function should move the focus to
     * the field. Otherwise set to \b false.
     **/
    public void setFieldError(int viewID, int errID, boolean focus) {
        TextView tv = (TextView)getView(viewID);
        if (tv == null) return;
        tv.setError( CAndroidApp.loadString(errID) );
        if (focus) tv.requestFocus();
    }/*}}}*/
    //@}

    /** \name ALERT FUNCTIONS */ //@{
    // public final void showAlert(String msg, int dlgID);/*{{{*/
    /**
     * Builds and shows an Alert dialog of type \c ALERT.TYPE.INFO to the user.
     * \param msg The string with the message to show.
     * \param dlgID Optional. The identifier of the dialog. If zero a general
     * identifier will be used (ALERT#TYPE#INFO).
     * \sa CAndroidApp.showAlert(Context,Handler,String,int,int)
     **/
    public final void showAlert(String msg, int dlgID) {
        if (dlgID == 0) dlgID = ALERT.TYPE.INFO;
        CAndroidApp.showAlert((Context)this, getViewHandler(), msg, dlgID, ALERT.TYPE.INFO);
    }/*}}}*/
    // public final void showConfirm(String msg, int dlgID);/*{{{*/
    /**
     * Builds and show an Alert dialog of type \c ALERT.TYPE.CONFIRM to the user.
     * \param msg The string with the message to show.
     * \param dlgID Optional. The identifier of the dialog. If zero a general
     * identifier will be used (ALERT#TYPE#CONFIRM).
     * \sa CAndroidApp.showAlert(Context,Handler,String,int,int)
     **/
    public final void showConfirm(String msg, int dlgID) {
        if (dlgID == 0) dlgID = ALERT.TYPE.CONFIRM;
        CAndroidApp.showAlert((Context)this, getViewHandler(), msg, dlgID, ALERT.TYPE.CONFIRM);
    }/*}}}*/
    // public final void showQuery(String msg, int dlgID);/*{{{*/
    /**
     * Builds and show an Alert dialog of type \c ALERT.TYPE.QUESTION to the user.
     * \param msg The string with the message to show.
     * \param dlgID Optional. The identifier of the dialog. If zero a general
     * identifier will be used (ALERT#TYPE#QUESTION).
     * \sa CAndroidApp.showAlert(Context,Handler,String,int,int)
     **/
    public final void showQuery(String msg, int dlgID) {
        if (dlgID == 0) dlgID = ALERT.TYPE.QUESTION;
        CAndroidApp.showAlert((Context)this, getViewHandler(), msg, dlgID, ALERT.TYPE.QUESTION);
    }/*}}}*/
    // public final void showAlert(int msgID, int dlgID);/*{{{*/
    /**
     * Builds and show an Alert dialog of type \c ALERT.TYPE.INFO to the user.
     * \param msgID The string identifier of the message in the application
     * resource.
     * \param dlgID Optional. The identifier of the dialog. If zero a general
     * identifier will be used (ALERT#TYPE#INFO).
     * \sa CAndroidApp.showAlert(Context,Handler,String,int,int)
     **/
    public final void showAlert(int msgID, int dlgID) {
        if (dlgID == 0) dlgID = ALERT.TYPE.INFO;
        CAndroidApp.showAlert((Context)this, getViewHandler(), msgID, dlgID, ALERT.TYPE.INFO);
    }/*}}}*/
    // public final void showConfirm(int msgID, int dlgID);/*{{{*/
    /**
     * Builds and show an Alert dialog of type \c ALERT.TYPE.CONFIRM to the user.
     * \param msgID The string identifier of the message in the application
     * resource.
     * \param dlgID Optional. The identifier of the dialog. If zero a general
     * identifier will be used (ALERT#TYPE#CONFIRM).
     * \sa CAndroidApp.showAlert(Context,Handler,String,int,int)
     **/
    public final void showConfirm(int msgID, int dlgID) {
        if (dlgID == 0) dlgID = ALERT.TYPE.CONFIRM;
        CAndroidApp.showAlert((Context)this, getViewHandler(), msgID, dlgID, ALERT.TYPE.CONFIRM);
    }/*}}}*/
    // public final void showQuery(int msgID, int dlgID);/*{{{*/
    /**
     * Builds and show an Alert dialog of type \c ALERT.TYPE.QUESTION to the user.
     * \param msgID The string identifier of the message in the application
     * resource.
     * \param dlgID Optional. The identifier of the dialog. If zero a general
     * identifier will be used (ALERT#TYPE#QUESTION).
     * \sa CAndroidApp.showAlert(Context,Handler,String,int,int)
     **/
    public final void showQuery(int msgID, int dlgID) {
        if (dlgID == 0) dlgID = ALERT.TYPE.QUESTION;
        CAndroidApp.showAlert((Context)this, getViewHandler(), msgID, dlgID, ALERT.TYPE.QUESTION);
    }/*}}}*/
    //@}

    /** \name MESSAGE FUNCTIONS */ //@{
    // public final void sendMsg(int what, int arg1, int arg2);/*{{{*/
    /**
     * Sends a message to the handler of this window.
     * \param what A code to identify the message.
     * \param arg1 Value that will be passed at the \c Message.arg1 member.
     * \param arg2 Value that will be passed at the \c Message.arg2 member.
     **/
    public final void sendMsg(int what, int arg1, int arg2) {
        CAndroidApp.sendMsg(getViewHandler(), what, arg1, arg2);
    }/*}}}*/
    // public final void sendMsg(int what, int arg1, int arg2, Object extra);/*{{{*/
    /**
     * Sends a message to the handler of this window.
     * \param what A code to identify the message.
     * \param arg1 Value that will be passed at the \c Message.arg1 member.
     * \param arg2 Value that will be passed at the \c Message.arg2 member.
     * \param extra Any additional object value to be passed in the \c
     * Message#obj member. This argument is optional and can be \b null.
     **/
    public final void sendMsg(int what, int arg1, int arg2, Object extra) {
        CAndroidApp.sendMsg(getViewHandler(), what, arg1, arg2, extra);
    }/*}}}*/
    // public final void postMsg(int what, int arg1, int arg2);/*{{{*/
    /**
     * Posts a message to the end of the queue of the handler of this window.
     * \param what A code to identify the message.
     * \param arg1 Value that will be passed at the \c Message.arg1 member.
     * \param arg2 Value that will be passed at the \c Message.arg2 member.
     **/
    public final void postMsg(int what, int arg1, int arg2) {
        CAndroidApp.postMsg(getViewHandler(), what, arg1, arg2);
    }/*}}}*/
    // public final void postMsg(int what, int arg1, int arg2, Object extra);/*{{{*/
    /**
     * Posts a message to the end of the queue of the handler of this window.
     * \param what A code to identify the message.
     * \param arg1 Value that will be passed at the \c Message.arg1 member.
     * \param arg2 Value that will be passed at the \c Message.arg2 member.
     * \param extra Any additional object value to be passed in the \c
     * Message#obj member. This argument is optional and can be \b null.
     **/
    public final void postMsg(int what, int arg1, int arg2, Object extra) {
        CAndroidApp.postMsg(getViewHandler(), what, arg1, arg2, extra);
    }/*}}}*/
    // public final void scheduleCall(int callID, long timeout);/*{{{*/
    /**
     * Schedules a call to the \c handleMessage() function.
     * The message is arbitrary. That is, the \a callID value is used as
     * message identifier, in the \c Message.what member. Care to not colid
     * the value with an predefined message identifier. Use a value equal or
     * above the \c MSG.APP constant.
     * \param callID The message identifier. After the time ellapses this
     * value will be used as message identifier, in the \c Message.what member
     * field.
     * \param timeout The interval to call the function, in milliseconds.
     * \remarks The member \c Message.arg1 will have the value of \c MSG.TIMER
     * constant. Others members are not used.
     **/
    public final void scheduleCall(int callID, long timeout) {
        CAndroidApp.setTimeout(getViewHandler(), callID, MSG.TIMER, 0, timeout);
    }/*}}}*/
    // public final void cancelSchedule(int callID);/*{{{*/
    /**
     * Cancel a previously scheduled call.
     * This function will cancel a scheduled call made using the method \c
     * scheduleCall().
     * \param callID The identifier of the call scheduled.
     **/
    public final void cancelSchedule(int callID) {
        CAndroidApp.cancelTimeout(getViewHandler(), callID);
    }/*}}}*/
    //@}

    /** \name HANDLING HELPER FUNCTIONS */ //@{
    // protected final boolean attach_onClick(int viewID);/*{{{*/
    /**
     * Sets a handler to the \c onClick callback of a view.
     * \param viewID The view identifier to handle the event. When the view
     * represented by this identifier is clicked the \c handleMessage()
     * function of this object will be called.
     * \return \b true means success. \b false indicates that the identifier
     * was not found in the view hierarchy.
     * \remarks Then \c handleMessage() function will be called with the
     * following members values:
     * - \c Message.what: Will have the value MSG#CLICK informing the kind of
     *      event.
     * - \c Message.arg1: Will carry the view identifier. The same value
     *      passed in the argument \a viewID.
     * - \c Message.arg2: This is not used. Its value will be zero.
     * - \c Message.obj: Will have a reference to the clicked view.
     * .
     **/
    protected final boolean attach_onClick(int viewID) {
        return attach_onClick( getView(viewID) );
    }/*}}}*/
    // protected final boolean attach_onClick(View view);/*{{{*/
    /**
     * Sets a handler to the \c onClick callback of a view.
     * \param view View to attach the handler. When this View is clicked the
     * \c handleMessage() function will be called.
     * \return \b true means success. \b false if the \a view argument isn't
     * valid.
     * \remarks Then \c handleMessage() function will be called with the
     * following members values:
     * - \c Message.what: Will have the value MSG#CLICK informing the kind of
     *      event.
     * - \c Message.arg1: Will carry the \a view identifier.
     * - \c Message.arg2: This is not used. Its value will be zero.
     * - \c Message.obj: Will have a reference to the clicked view.
     * .
     **/
    protected final boolean attach_onClick(View view) {
        if (view == null) return false;
        view.setOnClickListener((View.OnClickListener)this);
        return true;
    }/*}}}*/
    // protected final boolean attach_onFocusChange(int viewID);/*{{{*/
    /**
     * Sets a handler to the \c onFocusChange callback.
     * \param viewID Identifier of the view to install this handler. When that
     * view receiver or looses its focus then \c handleMessage() function will
     * be called with \c MSG.FOCUS value on its \c Message.what member.
     * \return \b true means success. \b false indicates that the identifier
     * was not found in the view hierarchy.
     * \remarks The following values will be passed through \c Message
     * argument:
     * - \c Message.what: Will have the \c MSG.FOCUS value.
     * - \c Message.arg1: Will have the view identifier. The same value passed
     *      in the \a viewID parameter.
     * - \c Message.arg2: Will have the value \c MSG.STATE.ACTIVE if the view
     *      just received the keyboard focus. Otherwise the value will be \c
     *      MSG.STATE.INACTIVE.
     * - \c Message.obj: Will carry a view reference.
     * .
     **/
    protected final boolean attach_onFocusChange(int viewID) {
        return attach_onFocusChange( getView(viewID) );
    }/*}}}*/
    // protected final boolean attach_onFocusChange(View view);/*{{{*/
    /**
     * Sets a handler to the \c onFocusChange callback.
     * \param view View to attach the handler. When this View is clicked the
     * \c handleMessage() function will be called.
     * \return \b true means success. \b false if the \a view argument isn't
     * valid.
     * \remarks The following values will be passed through \c Message
     * argument:
     * - \c Message.what: Will have the \c MSG.FOCUS value.
     * - \c Message.arg1: Will carry the \a view identifier.
     * - \c Message.arg2: Will have the value \c MSG.STATE.ACTIVE if the view
     *      just received the keyboard focus. Otherwise the value will be \c
     *      MSG.STATE.INACTIVE.
     * - \c Message.obj: Will carry a view reference.
     * .
     **/
    protected final boolean attach_onFocusChange(View view) {
        if (view == null) return false;
        view.setOnFocusChangeListener((View.OnFocusChangeListener)this);
        return true;
    }/*}}}*/
    // protected final boolean attach_onItemClick(int viewID);/*{{{*/
    /**
     * Sets a handler to a \c OnItemClick() callback of an \c AdapterView.
     * \param viewID The identifier of the view to attach the handler.
     * \return \b true if the function succeeds. Otherwise \b false.
     * \remarks The attached handler will call the \c handleMessage() function
     * passing the following parameters through the \c Message object:
     * - \c Message.what: \c MSG.ITEMCLICK constant value.
     * - \c Message.arg1: The AdapterView identifier.
     * - \c Message.arg2: The position of the clicked item.
     * - \c Message.obj: A reference to the View representing the clicked
     *      item. This View instance is provided by the \c Adapter used to
     *      fill the list.
     * .
     **/
    protected final boolean attach_onItemClick(int viewID) {
        return attach_onItemClick( (AdapterView<?>)getView(viewID) );
    }/*}}}*/
    // protected final boolean attach_onItemClick(AdapterView<?> view);/*{{{*/
    /**
     * Sets a handler to a \c OnItemClick() callback of an \c AdapterView.
     * \param view View to attach the handler. When this View is clicked the
     * \c handleMessage() function will be called.
     * \return \b true if the function succeeds. Otherwise \b false.
     * \remarks The attached handler will call the \c handleMessage() function
     * passing the following parameters through the \c Message object:
     * - \c Message.what: \c MSG.ITEMCLICK constant value.
     * - \c Message.arg1: The AdapterView identifier.
     * - \c Message.arg2: The position of the clicked item.
     * - \c Message.obj: A reference to the View representing the clicked
     *      item. This View instance is provided by the \c Adapter used to
     *      fill the list.
     * .
     **/
    protected final boolean attach_onItemClick(AdapterView<?> view) {
        if (view == null) return false;
        view.setOnItemClickListener((AdapterView.OnItemClickListener)this);
        return true;
    }/*}}}*/
    // protected final boolean attach_onItemSelected(int viewID);/*{{{*/
    /**
     * Attachs this View to an \c AdapterView.OnItemSelectedListener
     * interface.
     * \param viewID The identifier of the view to attach the handler.
     * \return \b true if the function succeeds. Otherwise \b false.
     * \remarks The attached handler will call the \c handleMessage() function
     * passing the following parameters through the \c Message object:
     * - \c Message.what: \c MSG.SELECTED constant value.
     * - \c Message.arg1: The AdapterView identifier.
     * - \c Message.arg2: The position of the selected item or -1, if no item
     *      is selected.
     * - \c Message.obj: A reference to the View representing the selected
     *      item or \b null, if no item is selected.
     * .
     **/
    protected final boolean attach_onItemSelected(int viewID) {
        return attach_onItemSelected((AdapterView<?>)getView(viewID));
    }/*}}}*/
    // protected final boolean attach_onItemSelected(AdapterView<?> view);/*{{{*/
    /**
     * Attachs this View to an \c AdapterView.OnItemSelectedListener
     * interface.
     * \param view View to attach the handler. When this View is clicked the
     * \c handleMessage() function will be called.
     * \return \b true if the function succeeds. Otherwise \b false.
     * \remarks The attached handler will call the \c handleMessage() function
     * passing the following parameters through the \c Message object:
     * - \c Message.what: \c MSG.SELECTED constant value.
     * - \c Message.arg1: The AdapterView identifier.
     * - \c Message.arg2: The position of the selected item or -1, if no item
     *      is selected.
     * - \c Message.obj: A reference to the View representing the selected
     *      item or \b null, if no item is selected.
     * .
     **/
    protected final boolean attach_onItemSelected(AdapterView<?> view) {
        if (view == null) return false;
        view.setOnItemSelectedListener((AdapterView.OnItemSelectedListener)this);
        return true;
    }/*}}}*/
    // protected final boolean attach_onCheckedChanged(int viewID);/*{{{*/
    /**
     * Attaches this view to a \c CompoundButton.OnCheckedChangeListener.
     * \param viewID The identifier of the CompoundButton to attach the
     * handler.
     * \return \b true means success. \b false if the \a viewID isn't valid.
     * \sa onCheckedChanged()
     **/
    protected final boolean attach_onCheckedChanged(int viewID) {
        return attach_onCheckedChanged((CompoundButton)getView(viewID));
    }/*}}}*/
    // protected final boolean attach_onCheckedChanged(CompoundButton view);/*{{{*/
    /**
     * Attaches this view to a \c CompoundButton.OnCheckedChangeListener.
     * \param view The CompoundButton instance to attach the handler.
     * \return \b true means success. \b false if the \a viewID isn't valid.
     * \sa onCheckedChanged()
     **/
    protected final boolean attach_onCheckedChanged(CompoundButton view) {
        if (view == null) return false;
        view.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener)this);
        return true;
    }/*}}}*/
    // protected final boolean attach_onTrackingTouch(int viewID);/*{{{*/
    /**
     * Attaches this view to a \c SeekBar.OnSeekBarChangeListener.
     * \param viewID The identifier of the SeekBar to attach the
     * handler.
     * \return \b true means success. \b false if the \a viewID isn't valid.
     * \sa onProgressChanged()
     * \sa onStartTrackingChanged()
     * \sa onStopTrackingChanged()
     **/
    protected final boolean attach_onTrackingTouch(int viewID) {
        return attach_onTrackingTouch((SeekBar)getView(viewID));
    }/*}}}*/
    // protected final boolean attach_onTrackingTouch(SeekBar view);/*{{{*/
    /**
     * Attaches this view to a \c SeekBar.OnSeekBarChangeListener.
     * \param view The SeekBar instance to attach the handler.
     * \return \b true means success. \b false if the \a viewID isn't valid.
     * \sa onProgressChanged()
     * \sa onStartTrackingChanged()
     * \sa onStopTrackingChanged()
     **/
    protected final boolean attach_onTrackingTouch(SeekBar view) {
        if (view == null) return false;
        view.setOnSeekBarChangeListener((SeekBar.OnSeekBarChangeListener)this);
        return true;
    }/*}}}*/
    //@}

    /** \name MENU FUNCTIONS */ //@{
    // protected final boolean attachContextMenu(int viewID);/*{{{*/
    /**
     * Attaches a context menu handler with the specified view.
     * \param viewID The identifier of the view that shows a menu. Every time
     * the system needs to show the menu the \c Activity.onCreateContextMenu()
     * function will be called.
     * \returns \b true on success. \b false if the specfied identifier is not
     * valid.
     **/
    protected final boolean attachContextMenu(int viewID) {
        View v = getView( viewID );

        if (v == null) return false;
        registerForContextMenu( v );
        v.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                v.showContextMenu();
            }
        });
        return true;
    }/*}}}*/
    // protected final Menu    loadMenu(int menuID, Menu menu);/*{{{*/
    /**
     * Loads a menu from a resource.
     * \param menuID The identifier of the menu to load.
     * \param menu The \c Menu object to populate.
     * \returns The \c Menu populated or \b null if the function fails.
     **/
    protected final Menu    loadMenu(int menuID, Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(menuID, menu);

        return menu;
    }/*}}}*/
    //@}

    /** \name PUBLIC OVERRIDES */ //@{
    // public boolean onContextItemSelected(MenuItem item);/*{{{*/
    /**
     * Handles the selecion of a context menu item.
     * \param item The context menu item selected.
     * \returns \b true if the selection was handles by this Intent. Otherwise
     * \b false.
     * \remarks The default implementation will pass the item ID to the \c
     * handleMessage() function. The \c Message object will have the following
     * values defined:
     * - \b what: Will have the \c MSG.COMMAND value.
     * - \b arg1: Will have the menu item identifier.
     * - \b arg2: Will be zero.
     * - \b obj: Will carry the MenuItem object instance.
     * .
     * The result of this function will be returned by this operation.
     * \note The \c handleMessage() is called directly and not through the
     * message queue.
     **/
    public boolean onContextItemSelected(MenuItem item) {
        Message msg = getMessage(MSG.COMMAND, item.getItemId(), 0, item);
        boolean ret = handleMessage(msg);
        msg.recycle();
        return ret;
    }/*}}}*/
    // public boolean onOptionsItemSelected(MenuItem item);/*{{{*/
    /**
     * Handles the selecion of a options menu item.
     * \param item The options menu item selected.
     * \returns \b true if the selection was handled by this Intent. Otherwise
     * \b false.
     * \remarks The default implementation will pass the item ID to the \c
     * handleMessage() function. The \c Message object will have the following
     * values defined:
     * - \b what: Will have the \c MSG.COMMAND value set.
     * - \b arg1: Will have the menu item identifier set.
     * - \b arg2: Will be zero.
     * - \b obj: Will carry the MenuItem object instance.
     * .
     * The result of this function will be returned by this operation.
     * \note The \c handleMessage() is called directly and not through the
     * message queue.
     **/
    public boolean onOptionsItemSelected(MenuItem item) {
        Message msg = getMessage(MSG.COMMAND, item.getItemId(), 0, item);
        boolean ret = handleMessage(msg);
        msg.recycle();
        return ret;
    }/*}}}*/
    // public void onClick(View v);/*{{{*/
    /**
     * Implementation of View.OnClickListener interface.
     * Calls the \c handleMessage() function of this with the following
     * values:
     * - \c Message.what: The \c MSG.CLICK constant value.
     * - \c Message.arg1: The view identifier that was clicked.
     * - \c Message.arg2: This is not used. Its value will be zero.
     * - \c Message.obj: Will have a reference to the clicked view.
     * .
     **/
    public void onClick(View v) {
        handleMessage(getMessage(MSG.CLICK, v.getId(), 0, v));
    }/*}}}*/
    // public void onFocusChange(View v, boolean hasFocus);/*{{{*/
    /**
     * Implements the \c View.OnFocusChangeListener interface.
     * Calls the \c handleMessage() function of this with the following
     * values:
     * - \c Message.what: Will have the \c MSG.FOCUS value.
     * - \c Message.arg1: The identifier of the View that is receiving or
     *      loosing focus.
     * - \c Message.arg2: Will have the value \c MSG.STATE.ACTIVE if the view
     *      just received the keyboard focus. Otherwise the value will be \c
     *      MSG.STATE.INACTIVE.
     * - \c Message.obj: Will carry a view reference.
     * .
     **/
    public void onFocusChange(View v, boolean hasFocus) {
        handleMessage(getMessage(MSG.FOCUS, v.getId(),
                                 (hasFocus ? MSG.STATE.ACTIVE : MSG.STATE.INACTIVE),
                                 v));
    }/*}}}*/
    // public void onItemClick(AdapterView<?> parent, View view, int position, long id);/*{{{*/
    /**
     * Implements the \c AdapterView.OnItemClickListener interface.
     * Calls the \c handleMessage() function of this with the following
     * values:
     * - \c Message.what: \c MSG.ITEMCLICK constant value.
     * - \c Message.arg1: The AdapterView identifier.
     * - \c Message.arg2: The position of the clicked item.
     * - \c Message.obj: A reference to the View representing the clicked
     * item. This View instance is provided by the \c Adapter used to fill the
     * list.
     **/
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        handleMessage(getMessage(MSG.ITEMCLICK, parent.getId(), position, view));
    }/*}}}*/
    // public void onItemSelected(AdapterView<?> parent, View view, int position, long id);/*{{{*/
    /**
     * Implements the \c AdapterView.OnItemSelectedListener interface.
     * Calls the \c handleMessage() function of this with the following
     * values:
     * - \c Message.what: \c MSG.SELECTED constant value.
     * - \c Message.arg1: The AdapterView identifier.
     * - \c Message.arg2: The position of the clicked item.
     * - \c Message.obj: A reference to the View representing the clicked
     * item. This View instance is provided by the \c Adapter used to fill the
     * list.
     **/
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        handleMessage(getMessage(MSG.SELECTED, parent.getId(), position, view));
    }/*}}}*/
    // public void onNothingSelected(AdapterView<?> parent);/*{{{*/
    /**
     * Implements the \c AdapterView.OnItemSelectedListener interface.
     * Calls the \c handleMessage() function of this with the following
     * values:
     * - \c Message.what: \c MSG.SELECTED constant value.
     * - \c Message.arg1: The AdapterView identifier.
     * - \c Message.arg2: -1, no item is selected.
     * - \c Message.obj: \b null, no item is selected.
     **/
    public void onNothingSelected(AdapterView<?> parent) {
        handleMessage(getMessage(MSG.SELECTED, parent.getId(), -1, null));
    }/*}}}*/
    // public void onCheckedChanged(CompoundButton buttonView, boolean isChecked);/*{{{*/
    /**
     * Implements the \c CompoundButton.OnCheckedChangeListener interface.
     * Calls the \c handleMessage() function of this with the following
     * values:
     * - \c Message.what: The \c MSG.CHECKED constant value.
     * - \c Message.arg1: The view identifier the was changed.
     * - \c Message.arg2: The \c MSG.STATE.CHECKED value if the view is
     *      checked. \c MSG.STATE.UNCHECKED value if it isn't checked.
     * - \c Message.obj: A reference to the changed view.
     * .
     **/
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        handleMessage(getMessage(MSG.CHECKED, buttonView.getId(),
                                 (isChecked ? MSG.STATE.CHECKED : MSG.STATE.UNCHECKED),
                                 buttonView));
    }/*}}}*/
    // public void onStartTrackingTouch(SeekBar seekBar);/*{{{*/
    /**
     * Implements the \c SeekBar.OnSeekBarChangeListener interface.
     * Calls the \c handleMessage() function with the following values:
     * - \c Message.what: \c MSG.SLIDE constant value.
     * - \c Message.arg1: The seekbar view identifier.
     * - \c Message.arg2: \c MSG.STATE.START constant value.
     * - \c Message.obj: Not used. The value will be \b null.
     * .
     **/
    public void onStartTrackingTouch(SeekBar seekBar) {
        handleMessage(getMessage(MSG.SLIDE, seekBar.getId(), MSG.STATE.START, null));
    }/*}}}*/
    // public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser);/*{{{*/
    /**
     * Implements the \c SeekBar.OnSeekBarChangeListener interface.
     * Calls the \c handleMessage() function with the following values:
     * - \c Message.what: \c MSG.SLIDE constant value.
     * - \c Message.arg1: The seekbar view identifier.
     * - \c Message.arg2: The current \c SeekBar position.
     * - \c Message.obj: A reference to the \c SeekBar view.
     * .
     * \note Only changes made by the user are passed to the \c
     * handleMessage() function. Changes made by the application will be
     * silently ignored.
     **/
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (!fromUser) return;
        handleMessage(getMessage(MSG.SLIDE, seekBar.getId(), progress, seekBar));
    }/*}}}*/
    // public void onStopTrackingTouch(SeekBar seekBar);/*{{{*/
    /**
     * Implements the \c SeekBar.OnSeekBarChangeListener interface.
     * Calls the \c handleMessage() function with the following values:
     * - \c Message.what: \c MSG.SLIDE constant value.
     * - \c Message.arg1: The seekbar view identifier.
     * - \c Message.arg2: \c MSG.STATE.STOP constant value.
     * - \c Message.obj: Not used. The value will be \b null.
     * .
     **/
    public void onStopTrackingTouch(SeekBar seekBar) {
        handleMessage(getMessage(MSG.SLIDE, seekBar.getId(), MSG.STATE.STOP, null));
    }/*}}}*/
    //@}

    /** \name MESSAGE SYSTEM */ //@{
    // public boolean handleMessage(Message msg);/*{{{*/
    /**
     * Handles messages sent to this object.
     * \param msg the \c Message object.
     * \returns \b true if the message was processed. Otherwise \b false.
     * \remarks This implementation calls the \c onMessage() function only.
     **/
    public boolean handleMessage(Message msg) {
        return onMessage(msg.what, msg.arg1, msg.arg2, msg.obj);
    }/*}}}*/
    // public boolean onMessage(int what, int arg1, int arg2, Object obj);/*{{{*/
    /**
     * Handles messages sent to this object.
     * \param what Message value.
     * \param arg1 Message code.
     * \param arg2 Message argument.
     * \param obj Message extra data.
     * \returns \b true if the message was processed. Otherwise \b false.
     * \remarks This is a facility function where the \c Message object
     * members are dismembered.
     *
     * This implementation always returns \b false.
     **/
    public boolean onMessage(int what, int arg1, int arg2, Object obj) {
        return false;
    }/*}}}*/
    //@}

    /** \name RESOURCE FACILITIES */ //@{
    // public final String resString(int resID);/*{{{*/
    /**
     * Loads a string from the application resource.
     * \param resID The string identifier.
     * \return The string object loaded or an empty string, if the specified
     * ID isn't found.
     **/
    public final String resString(int resID) {
        return CAndroidApp.loadString(resID);
    }/*}}}*/
    //@}

    /** \name DATA MEMBERS */ //@{
    private Handler m_handler;        /**< To work with messages.         */
    //@}
}
// vim:syntax=java.doxygen
