/**
 * \file
 * Defines the AbstractView class.
 *
 * \author Alessandro Antonello <aantonello@paralaxe.com.br>
 * \date   outubro 26, 2012
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
import android.app.Activity;
import android.content.Context;

import android.view.*;

import android.util.AttributeSet;
import android.text.TextWatcher;

import android.graphics.drawable.Drawable;

import android.widget.*;

import x.android.utils.*;
/* }}} #imports */
/**
 * Abstracts a Androi View class.
 * The AbstractView can be used to bound to a class like an Activity or a View
 * building a common interface to these objects. Also, can be used to access
 * its children chain, issuing commands and operations to a big set of
 * widgets.
 * @param T A class that extends the AbstractView.
 *//* --------------------------------------------------------------------- */
public abstract class AbstractView<T extends AbstractView<T>>
{
    /** \name CONSTRUCTORS */ //@{
    // public AbstractView(Activity activity);/*{{{*/
    /**
     * Construct a new object based on an Activity.
     * \param activity The Activity with will be operated.
     **/
    public AbstractView(Activity activity) {
        m_activity = activity;
        m_view     = null;
        m_root     = null;
    }/*}}}*/
    // public AbstractView(View view);/*{{{*/
    /**
     * Builds an instance based on a View.
     * \param view The view base for this object.
     **/
    public AbstractView(View view) {
        m_activity = null;
        m_view     = view;
        m_root     = view;
    }/*}}}*/
    // public AbstractView(View root, View view);/*{{{*/
    /**
     * Builds the instance of this class.
     * \param root A View object used as base root of View chain.
     * \param view A View instance to be defined as current operating View.
     **/
    public AbstractView(View root, View view) {
        m_activity = null;
        m_root     = root;
        m_view     = view;
    }/*}}}*/
    //@}

    /** \name HELPER METHODS */ //@{
    // public View getView(int id);/*{{{*/
    /**
     * Returns a View having the specified identifier.
     * \param id The View identifier.
     * \returns The found View or \b null.
     **/
    public View getView(int id) {
        View result = null;
        if (m_view != null) {
            if (id == m_view.getId())
                result = m_view;
            else
                result = m_view.findViewById(id);
        }

        if (result != null) return result;
        if (m_root != null) {
            if (m_root.getId() == id)
                result = m_root;
            else
                result = m_root.findViewById(id);
        }
        if (result != null) return result;
        if (m_activity != null) {
            result = m_activity.findViewById(id);
        }
        return result;
    }/*}}}*/
    // public T    show();/*{{{*/
    /**
     * Shows the view.
     * This method is same as using @code display(true); @endcode.
     * \returns this.
     **/
    public T    show() {
        return this.display(true);
    }/*}}}*/
    // public T    hide();/*{{{*/
    /**
     * Hides the view.
     * This method is same as using @code visible(false); @endcode.
     * \returns this.
     **/
    public T    hide() {
        return this.visible(false);
    }/*}}}*/
    // public T    gone();/*{{{*/
    /**
     * Vanishes the view.
     * This method is the same as using @code display(false); @endcode.
     * \returns this.
     **/
    public T    gone() {
        return this.display(false);
    }/*}}}*/
    // public T    self();/*{{{*/
    /**
     * Returns the implementation of this class.
     **/
    @SuppressWarnings("unchecked")
    public T self() {
        return (T)this;
    }/*}}}*/
    //@}

    /** \name UTILITY ATTRIBUTES */ //@{
    // public boolean exists();/*{{{*/
    /**
     * Checks if the operating view exists.
     * \return \b true if it exists. \b false otherwise.
     **/
    public boolean exists() {
        return (m_view != null);
    }/*}}}*/
    // public boolean isChecked();/*{{{*/
    /**
     * Gets the current value of a CompoundButton.
     * \returns \b true if the button is checked or \b false if the button is
     * unchecked or if the current operating view is not a CompoundButton.
     **/
    public boolean isChecked() {
        if (m_view instanceof CompoundButton) {
            return ((CompoundButton)m_view).isChecked();
        }
        return false;
    }/*}}}*/
    // public boolean isVisible();/*{{{*/
    /**
     * Identifies when the current View is visible.
     * \returns \b true when the View is visible, otherwise \b false.
     **/
    public boolean isVisible() {
        if (m_view != null)
            return (boolean)(m_view.getVisibility() == View.VISIBLE);

        return false;
    }/*}}}*/
    // public boolean focus();/*{{{*/
    /**
     * Request the operating view to get the focus.
     * \return \b true if the operation succeeds. \b false otherwise.
     **/
    public boolean focus() {
        if (m_view != null) {
            return m_view.requestFocus();
        }
        return false;
    }/*}}}*/
    // public boolean is(String className);/*{{{*/
    /**
     * Check wether the current view is of the passed class name.
     * \param className String to test the current View. The test is done case
     * sensitivelly.
     * \return \b true if the current selected view is an instance of the
     * passed \a className. Otherwise \b false.
     **/
    public boolean is(String className) {
        if (m_view == null) return false;
        String simpleName = m_view.getClass().getSimpleName();

        return simpleName.equals(className);
    }/*}}}*/
    // public CRect   rect(CRect rect);/*{{{*/
    /**
     * Get the current View rectangle.
     * \param rect A \c CRect instance. May be \b null.
     * \return The function returns the \a rect parameter if it is passed or a
     * newly created \c CRect object otherwise. The returned rectangle will
     * have the current position and size of this View related to its parent.
     **/
    public CRect rect(CRect rect) {
        CRect rc = ((rect == null) ? new CRect() : rect);
        if (m_view == null) return rc;
        rc.box(m_view.getLeft(), m_view.getTop(), m_view.getWidth(), m_view.getHeight());
        return rc;
    }/*}}}*/
    // public CRect   padding(CRect rect);/*{{{*/
    /**
     * Gets the current View padding.
     * \param rect A \c CRect instance. May be \b null.
     * \return The function returns the \a rect parameter if it is passed or a
     * newly created \c CRect object otherwise. The returned rectangle will
     * have the current padding values on its members.
     **/
    public CRect padding(CRect rect) {
        CRect rc = ((rect == null) ? new CRect() : rect);
        if (m_view == null) return rc;
        rc.set(m_view.getPaddingLeft(), m_view.getPaddingTop(), m_view.getPaddingRight(), m_view.getPaddingBottom());
        return rc;
    }/*}}}*/
    // public CRect   measured(CRect rect);/*{{{*/
    /**
     * Gets the measured values for laying out this View.
     * \param rect A \c CRect instance. May be \b null.
     * \return The function returns the \a rect parameter, when it was passed,
     * filled with the current measured width and measured height. The \e left
     * and \e top coordinates of the rectangle are also filled with the
     * current values of the View. If the parameter \a rect is not passed in
     * the function creates a new one and returns it.
     **/
    public CRect measured(CRect rect) {
        CRect rc = ((rect != null) ? rect : new CRect());
        if (m_view == null) return rc;
        rc.box(m_view.getLeft(), m_view.getTop(), m_view.getMeasuredWidth(), m_view.getMeasuredHeight());
        return rc;
    }/*}}}*/
    // public CRect   bounds(CRect rect, boolean decreasePadding);/*{{{*/
    /**
     * Gets the client rectangle area (bounds) of the current operating View.
     * @param rect A reusable \c CRect instance. May be \b null.
     * @param decreasePadding When \b true the function will take the padding
     * configuration into account and will `deflate` the interal area bounds
     * according to the View's padding definition.
     * @return If \a rect is passed the function will set it with the current
     * operating View's bounds (client area). If \a rect is \b null a new \c
     * CRect instance will be created for that.
     **/
    public CRect bounds(CRect rect, boolean decreasePadding)
    {
        CRect rc = ((rect == null) ? new CRect() : rect);
        if (m_view == null) return rc;
        rc.box(0, 0, m_view.getWidth(), m_view.getHeight());
        if (decreasePadding) {
            CRect padd = padding(null);
            rc.deflate(padd.left, padd.top, padd.right, padd.bottom);
        }
        return rc;
    }/*}}}*/
    //@}

    /** \name PUBLIC METHODS */ //@{
    // public int id();/*{{{*/
    /**
     * Returns the current view identifier.
     **/
    public int id() {
        if (m_view != null) return m_view.getId();
        return 0;
    }/*}}}*/
    // public T id(int id);/*{{{*/
    /**
     * Changes the operating view to the one having the passed identifier.
     * \param id The View identifier.
     * \return this.
     **/
    public T id(int id) {
        m_view = getView(id);
        return self();
    }/*}}}*/
    // public T parent(int id);/*{{{*/
    /**
     * Changes the operating view to a parent of the current operating view.
     * \param id The parent identifier.
     * \return this.
     **/
    public T parent(int id) {
        View view = m_view;
        ViewParent p = null;

        while (view != null) {
            if (view.getId() == id) break;
            p = view.getParent();
            if (!(p instanceof View)) break;
            view = (View)p;
        }
        m_view = view;
        return self();
    }/*}}}*/
    // public T reset(Activity activity, View root, View view);/*{{{*/
    /**
     * Reset to this root View and Activity.
     * \param activity The activity to be set.
     * \param root The new root view.
     * \param view The operating view.
     * \returns this.
     **/
    public T reset(Activity activity, View root, View view) {
        m_activity = activity;
        m_root     = root;
        m_view     = view;
        return self();
    }/*}}}*/
    // public T reset(Activity activity, View root);/*{{{*/
    /**
     * Reset to this root View and Activity.
     * \param activity The activity to be set.
     * \param root The new root view. This also will set the current View.
     * \returns this.
     **/
    public T reset(Activity activity, View root) {
        return reset(m_activity, root, root);
    }/*}}}*/
    // public T reset(Activity activity, int rootID);/*{{{*/
    /**
     * Reset the root View and also the activity.
     * \param activity The Activity to be set.
     * \param rootID The root View to be ser based on its ID. This should be a
     * sub-view of the passed \a activity.
     * \returns this.
     **/
    public T reset(Activity activity, int rootID) {
        View view = activity.findViewById(rootID);
        return reset(activity, view);
    }/*}}}*/
    // public T reset(View view);/*{{{*/
    /**
     * Resets to this root view.
     * \param view The View used as a root of this Joker.
     * \return The function returns \b this.
     **/
    public T reset(View view) {
        return reset(m_activity, view);
    }/*}}}*/
    // public T set(int id);/*{{{*/
    /**
     * Changes the identifier of this view.
     * \param id The new identifier to use.
     * \return this.
     **/
    public T set(int id) {
        if (m_view != null) {
            m_view.setId(id);
        }
        return self();
    }/*}}}*/
    //@}

    /** \name TAG METHODS */ //@{
    // public Object tag();/*{{{*/
    /**
     * Gets the current value tag of the operating view.
     **/
    public Object tag() {
        return ((m_view == null) ? null : m_view.getTag());
    }/*}}}*/
    // public Object tag(int key);/*{{{*/
    /**
     * Gets the tag value under the given key of the operating view.
     * \param key The key of the value to return.
     * \return The object value or \b null.
     **/
    public Object tag(int key) {
        return ((m_view == null) ? null : m_view.getTag(key));
    }/*}}}*/
    // public T tag(Object tag);/*{{{*/
    /**
     * Sets a tag of a view.
     * \param tag The object to bound to the tag.
     * \return this.
     **/
    public T tag(Object tag) {
        if (m_view != null) {
            m_view.setTag(tag);
        }
        return self();
    }/*}}}*/
    // public T tag(int key, Object tag);/*{{{*/
    /**
     * Sets a tag key of a view.
     * \param key The key index of the tag object.
     * \param tag The tag object.
     * \returns this.
     **/
    public T tag(int key, Object tag) {
        if (m_view != null) {
            m_view.setTag(key, tag);
        }
        return self();
    }/*}}}*/
    //@}

    /** \name UTILITY METHODS */ //@{
    // public T enabled(boolean enable);/*{{{*/
    /**
     * Enable or disables a View.
     * \param enable \b true enables the view. \b false disables it.
     * \return this.
     **/
    public T enabled(boolean enable) {
        if (m_view != null) {
            m_view.setEnabled(enable);
        }
        return self();
    }/*}}}*/
    // public T visible(boolean visible);/*{{{*/
    /**
     * Sets the visibility of a view.
     * \param visible \b true shows the view. \b false hides it.
     * \return this.
     **/
    public T visible(boolean visible) {
        if (m_view != null) {
            if ((m_view.getVisibility() != View.VISIBLE) && visible)
                m_view.setVisibility(View.VISIBLE);
            else if ((m_view.getVisibility() != View.INVISIBLE) && !visible)
                m_view.setVisibility(View.INVISIBLE);
        }
        return self();
    }/*}}}*/
    // public T background(int resID);/*{{{*/
    /**
     * Changes the background drawable of the view.
     * \param resID The resource identifier of the drawable.
     * \return this.
     **/
    public T background(int resID) {
        if (m_view != null) {
            m_view.setBackgroundResource(resID);
        }
        return self();
    }/*}}}*/
    // public T backColor(int color);/*{{{*/
    /**
     * Sets the background color of a view.
     * \param color The color to be set.
     * \return this.
     **/
    public T backColor(int color) {
        if (m_view != null) {
            m_view.setBackgroundColor(color);
        }
        return self();
    }/*}}}*/
    // public T display(boolean display);/*{{{*/
    /**
     * Changes the visibility of a view.
     * The difference of this method and \c visible() method is that when the
     * display is \b false not even the visibility is false but also the view
     * will be removed from the Canvas and its current space will be occupied
     * by others views.
     * \param display \b true to show the view. \b false to hide it.
     * \return this.
     **/
    public T display(boolean display) {
        if (m_view != null) {
            if ((m_view.getVisibility() != View.VISIBLE) && display)
                m_view.setVisibility(View.VISIBLE);
            else if ((m_view.getVisibility() != View.GONE) && !display)
                m_view.setVisibility(View.GONE);
        }
        return self();
    }/*}}}*/
    // public T checked(boolean check);/*{{{*/
    /**
     * Changes the checked state of a CompoundButton.
     * \param check \b true to check the button. \b false to uncheck it.
     * \return this.
     * \remarks The state can be verified by the \c isChecked() function.
     **/
    public T checked(boolean check) {
        if (m_view instanceof CompoundButton) {
            ((CompoundButton)m_view).setChecked(check);
        }
        return self();
    }/*}}}*/
    // public T inflate(Menu m, int id);/*{{{*/
    /**
     * Load a Menu from a resource.
     * This only works if the Activity member was defined.
     * \param m The Menu instance to be populated.
     * \param id The resource identifier that has the menu.
     * \returns this.
     **/
    public T inflate(Menu m, int id) {
        if (m_activity != null) {
            MenuInflater mi = m_activity.getMenuInflater();
            mi.inflate(id, m);
        }
        return self();
    }/*}}}*/
    // public View inflate(int id, ViewGroup parent);/*{{{*/
    /**
     * Inflates a View from the specified resource identifier.
     * @param id The resource identifier to be inflated.
     * @param parent The ViewGroup where the new inflated View will be placed.
     * The View will not be added to this ViewGroup. This is needed only to
     * get default LayoutParams values. Can be \b null.
     * @return The View inflated from the layout resource. If the function
     * fails, the return will be \b null.
     **/
    public View inflate(int id, ViewGroup parent)
    {
        LayoutInflater li = null;

        if (m_activity != null)
            li = m_activity.getLayoutInflater();
        else if (m_view != null)
            li = (LayoutInflater)m_view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (li == null) return null;

        return li.inflate(id, parent, false);
    }/*}}}*/
    //@}

    /** \name TextView METHODS */ //@{
    // public String text();/*{{{*/
    /**
     * Gets the text of the operating view.
     * \return The text of the operating view or \b strings.EMPTY if it is not
     * a instance of a TextView.
     **/
    public String text() {
        if (m_view instanceof TextView) {
            return ((TextView)m_view).getText().toString();
        }
        return strings.EMPTY;
    }/*}}}*/
    // public String hint();/*{{{*/
    /**
     * Returns the text used as hint if this is a TextView or decendant.
     * Otherwise the function returns \c strings.EMPTY.
     **/
    public String hint() {
        if (m_view instanceof TextView) {
            ((TextView)m_view).getHint().toString();
        }
        return strings.EMPTY;
    }/*}}}*/
    // public T text(int resID);/*{{{*/
    /**
     * Sets the text of a TextView.
     * \param resID Identifier of the resource with the text.
     * \return this.
     **/
    public T text(int resID) {
        if (m_view instanceof TextView) {
            TextView tv = (TextView)m_view;
            tv.setText(resID);
        }
        return self();
    }/*}}}*/
    // public T text(CharSequence text);/*{{{*/
    /**
     * Sets the text of a TextView.
     * \param text Text to be set.
     * \return this.
     **/
    public T text(CharSequence text) {
        if (m_view instanceof TextView) {
            TextView tv = (TextView)m_view;
            tv.setText(text);
        }
        return self();
    }/*}}}*/
    // public T textColor(int color);/*{{{*/
    /**
     * Changes the text color of a TextView.
     * \param color Color of the text.
     * \return this.
     **/
    public T textColor(int color) {
        if (m_view instanceof TextView) {
            TextView tv = (TextView)m_view;
            tv.setTextColor(color);
        }
        return self();
    }/*}}}*/
    // public T textSize(float size);/*{{{*/
    /**
     * Changes the text size of a TextView.
     * \param size The text size, in \b sp.
     * \return this.
     **/
    public T textSize(float size) {
        if (m_view instanceof TextView) {
            TextView tv = (TextView)m_view;
            tv.setTextSize(size);
        }
        return self();
    }/*}}}*/
    // public T hint(int resID);/*{{{*/
    /**
     * Sets the hint of a TextView.
     * \param resID Resource identifier that has the hint string.
     * \returns this.
     **/
    public T hint(int resID) {
        if (m_view instanceof TextView) {
            ((TextView)m_view).setHint(resID);
        }
        return self();
    }/*}}}*/
    // public T hint(CharSequence text);/*{{{*/
    /**
     * Sets the hint of a TextView.
     * \param text Texto to be used as hint.
     * \returns this.
     **/
    public T hint(CharSequence text) {
        if (m_view instanceof TextView) {
            ((TextView)m_view).setHint(text);
        }
        return self();
    }/*}}}*/
    // public T inputType(int flags);/*{{{*/
    /**
     * Sets the input type of this view, if it is a TextView.
     * \param flags Set of InputType flags.
     * \return this.
     **/
    public T inputType(int flags) {
        if (m_view instanceof TextView) {
            ((TextView)m_view).setInputType(flags);
        }
        return self();
    }/*}}}*/
    // public T singleLine(boolean single);/*{{{*/
    /**
     * Sets this TextView as a single line one.
     * \param single \b true to set it as a single line. \b false will set it
     * as a multiline TextView.
     * \return this.
     **/
    public T singleLine(boolean single) {
        if (m_view instanceof TextView) {
            ((TextView)m_view).setSingleLine(single);
        }
        return self();
    }/*}}}*/
    // public T error(String errorText);/*{{{*/
    /**
     * Sets an error message for a TextView.
     * \param errorText The text to be set as error message.
     * \returns this.
     **/
    public T error(String errorText) {
        if (m_view instanceof TextView) {
            ((TextView)m_view).setError( errorText );
        }
        return self();
    }/*}}}*/
    // public T error(int errorID);/*{{{*/
    /**
     * Sets an error message for a TextView.
     * \param errorID Error message resource identifier.
     * \returns this.
     **/
    public T error(int errorID) {
        return this.error(CAndroidApp.loadString(errorID));
    }/*}}}*/
    //@}

    /** \name ImageView METHODS */ //@{
    // public T image(int resID);/*{{{*/
    /**
     * Sets the image of a ImageView.
     * \param resID Resource identifier of the image.
     * \returns this.
     **/
    public T image(int resID) {
        if (m_view instanceof ImageView) {
            ((ImageView)m_view).setImageResource(resID);
        }
        return self();
    }/*}}}*/
    // public T image(Drawable drawable);/*{{{*/
    /**
     * Sets the image of a ImageView.
     * \param drawable Drawable to set the ImageView.
     * \returns this.
     **/
    public T image(Drawable drawable) {
        if (m_view instanceof ImageView) {
            ((ImageView)m_view).setImageDrawable(drawable);
        }
        return self();
    }/*}}}*/
    // public T level(int levelNumber);/*{{{*/
    /**
     * Sets the image level of an ImageView constructed with a
     * LevelListDrawable.
     * @param levelNumber Index of the level to set.
     * @returns this.
     **/
    public T level(int levelNumber)
    {
        if (m_view instanceof ImageView) {
            ((ImageView)m_view).setImageLevel(levelNumber);
        }
        return self();
    }/*}}}*/
    //@}

    /** \name Adapter METHODS */ //@{
    // public T adapter(ListAdapter l);/*{{{*/
    /**
     * Sets a ListAdapter for the operating view.
     * The operating view needs to be a decendant of an AdapterView.
     * \param l The ListAdapter or decendant to set.
     * \returns this.
     **/
    public T adapter(ListAdapter l) {
        if (m_view instanceof AdapterView) {
            ((AdapterView)m_view).setAdapter(l);
        }
        return self();
    }/*}}}*/
    //@}

    /** \name ProgressBar/SeekBar METHODS */ //@{
    // public int max();/*{{{*/
    /**
     * Returns the maximum position of this ProgressBar.
     * \return An integer with the maximum position. If the function fails the
     * result will be -1.
     * \remarks The current View must be a ProgressBar or SeekBar for this
     * function to work properly.
     **/
    public int max() {
        if (m_view instanceof ProgressBar) {
            return ((ProgressBar)m_view).getMax();
        }
        return -1;
    }/*}}}*/
    // public T   max(int maximum);/*{{{*/
    /**
     * Sets the maximum value for a SeekBar or ProgressBar.
     * \param maximum Integer with maximum value.
     * \returns this.
     **/
    public T max(int maximum) {
        if (m_view instanceof ProgressBar) {
            ((SeekBar)m_view).setMax(maximum);
        }
        return self();
    }/*}}}*/
    // public int position();/*{{{*/
    /**
     * Gets the current position of a SeekBar or ProgressBar.
     * \return An integer with the current position or -1 if the function
     * fails.
     * \remarks The current View must be an instance of ProgressBar or
     * SeekBar.
     **/
    public int position() {
        if (m_view instanceof ProgressBar) {
            return ((ProgressBar)m_view).getProgress();
        }
        return -1;
    }/*}}}*/
    // public T   position(int pos);/*{{{*/
    /**
     * Sets the current position of a ProgressBar or SeekBar.
     * \param pos Position to set. Must be less than the maximum position.
     * \returns this.
     **/
    public T   position(int pos) {
        if (m_view instanceof ProgressBar) {
            ((ProgressBar)m_view).setProgress(pos);
        }
        return self();
    }/*}}}*/
    //@}

    /** \name CONVERSION METHODS */ //@{
    // public Button    getButton();/*{{{*/
    /**
     * Gets the operating view instance as a Button.
     * \remarks This method doesn't check if the view is valid nor even if
     * it's a Button.
     **/
    public Button    getButton() {
        return (Button)m_view;
    }/*}}}*/
    // public CheckBox  getCheckBox();/*{{{*/
    /**
     * Gets the operating view instance as a CheckBox.
     * \remarks This method doesn't check if the view is valid nor even if
     * it's a CheckBox.
     **/
    public CheckBox  getCheckBox() {
        return (CheckBox)m_view;
    }/*}}}*/
    // public CompoundButton getCompoundButton();/*{{{*/
    /**
     * Gets the operating view instance as a CompoundButton.
     * \remarks This method doesn't check if the view is valid nor even if
     * it's a CompoundButton.
     **/
    public CompoundButton getCompoundButton() {
        return (CompoundButton)m_view;
    }/*}}}*/
    // public ImageView getImageView();/*{{{*/
    /**
     * Gets the operating view instance as an ImageView.
     * \remarks This method doesn't check if the view is valid nor even if
     * it's an ImageView.
     **/
    public ImageView getImageView() {
        return (ImageView)m_view;
    }/*}}}*/
    // public TextView  getTextView();/*{{{*/
    /**
     * Gets the operating view instance as a TextView.
     * \remarks This method doesn't check if the view is valid nor even if
     * it's a TextView.
     **/
    public TextView  getTextView() {
        return (TextView)m_view;
    }/*}}}*/
    // public EditText  getEditText();/*{{{*/
    /**
     * Gets the operating view instance as an EditText widget.
     * \remarks This method doesn't check if the view is valid nor even if
     * it's an EditView.
     **/
    public EditText  getEditText() {
        return (EditText)m_view;
    }/*}}}*/
    // public SeekBar   getSeekBar();/*{{{*/
    /**
     * Gets the operating view instance as a SeekBar widget.
     * \remarks This method doesn't check if the view is valid nor even if
     * it's a SeekBar.
     **/
    public SeekBar   getSeekBar() {
        return (SeekBar)m_view;
    }/*}}}*/
    // public ListView  getListView();/*{{{*/
    /**
     * Gets the operating view instance as a ListView widget.
     * \remarks The method doesn't check if the view is a valid ListView.
     **/
    public ListView  getListView() {
        return (ListView)m_view;
    }/*}}}*/
    // public GridView  getGridView();/*{{{*/
    /**
     * Gets the operating View as a GridView widget.
     * @remarks If the operating View is not of type GridView a
     * ClassCastException will be thrown.
     **/
    public GridView getGridView() {
        return (GridView)m_view;
    }/*}}}*/
    // public Spinner   getSpinner();/*{{{*/
    /**
     * Cast the operating View as an Spinner widget.
     * @remarks If the operating View is not of type Spinner a
     * ClassCastException will be thrown.
     **/
    public Spinner getSpinner() {
        return (Spinner)m_view;
    }/*}}}*/
    //@}

    /** \name LAYOUT METHODS */ //@{
    // public T layout();/*{{{*/
    /**
     * Request the operating view to relayout it self and all its children.
     * \return this.
     **/
    public T layout() {
        if (m_view != null) {
            m_view.requestLayout();
        }
        return self();
    }/*}}}*/
    // public T force();/*{{{*/
    /**
     * Forces the operating View to relayout it self and it's children.
     * @returns \c this.
     * @remarks The function will call \c forceLayout() and \c requestLayout()
     * in the operating View.
     **/
    public T force()
    {
        if (m_view != null) {
            m_view.forceLayout();
            m_view.requestLayout();
        }
        return self();
    }/*}}}*/
    // public T margins(int l, int t, int r, int b);/*{{{*/
    /**
     * Changes the margins of the operating view.
     * \param l Margin to apply in the left edge.
     * \param t Margin to apply in the top edge.
     * \param r Margin to apply in the right edge.
     * \param b Margin to apply in the bottom edge.
     * \return this.
     **/
    public T margins(int l, int t, int r, int b) {
        if (m_view != null) {
            ViewGroup.LayoutParams lp = m_view.getLayoutParams();

            if (lp instanceof ViewGroup.MarginLayoutParams) {
                ((ViewGroup.MarginLayoutParams)lp).setMargins(l, t, r, b);
                m_view.setLayoutParams(lp);
            }
        }
        return self();
    }/*}}}*/
    // public T size(int width, int height);/*{{{*/
    /**
     * Changes the size of the operating view.
     * \param width New width.
     * \param height New height.
     * \return this.
     **/
    public T size(int width, int height) {
        if (m_view != null) {
            ViewGroup.LayoutParams lp = m_view.getLayoutParams();
            lp.width  = width;
            lp.height = height;
            m_view.setLayoutParams(lp);
        }
        return self();
    }/*}}}*/
    //@}

    /** \name EVENT HANDLERS ATTACHMENT */ //@{
    // public T click(View.OnClickListener listener);/*{{{*/
    /**
     * Attaches a listener to the OnClick event of the operating view.
     * \param listener OnClickListener instance to attach.
     * \return this.
     **/
    public T click(View.OnClickListener listener) {
        if (m_view != null) {
            m_view.setOnClickListener(listener);
        }
        return self();
    }/*}}}*/
    // public T longClick(View.OnLongClickListener listener);/*{{{*/
    /**
     * Attaches a listener to the OnLongClick event of the operating view.
     * \param listener OnLongClickListener instance to attach.
     * \return this.
     **/
    public T longClick(View.OnLongClickListener listener) {
        if (m_view != null) {
            m_view.setOnLongClickListener(listener);
        }
        return self();
    }/*}}}*/
    // public T itemClick(AdapterView.OnItemClickListener listener);/*{{{*/
    /**
     * Attaches a listener to the OnItemClick event os an AdapterView.
     * \param listener The OnItemClickListener implementation to attach.
     * \returns this.
     **/
    public T itemClick(AdapterView.OnItemClickListener listener) {
        if (m_view != null) {
            ((AdapterView)m_view).setOnItemClickListener(listener);
        }
        return self();
    }/*}}}*/
    // public T touch(View.OnTouchListener listener);/*{{{*/
    /**
     * Attaches a listener to the OnTouchListener event of the operating view.
     * \param listener OnTouchListener instance to attach.
     * \return this.
     **/
    public T touch(View.OnTouchListener listener) {
        if (m_view != null) {
            m_view.setOnTouchListener(listener);
        }
        return self();
    }/*}}}*/
    // public T contextMenu(View.OnCreateContextMenuListener listener);/*{{{*/
    /**
     * Attaches a listener to the OnCreateContextMenuListener event of the
     * operating view.
     * \param listener OnCreateContextMenuListener instance to attach.
     * \return this.
     **/
    public T contextMenu(View.OnCreateContextMenuListener listener) {
        if (m_view != null) {
            m_view.setOnCreateContextMenuListener(listener);
        }
        return self();
    }/*}}}*/
    // public T focusChange(View.OnFocusChangeListener listener);/*{{{*/
    /**
     * Attaches a listener to the OnFocusChangeListener event of the operating
     * view.
     * \param listener OnFocusChangeListener instance to attach.
     * \return this.
     **/
    public T focusChange(View.OnFocusChangeListener listener) {
        if (m_view != null) {
            m_view.setOnFocusChangeListener(listener);
        }
        return self();
    }/*}}}*/
    // public T textChanged(TextWatcher watcher);/*{{{*/
    /**
     * Adds a handler to all text changes events.
     * \param watcher Instance of TextWatcher interface.
     * \return this.
     **/
    public T textChanged(TextWatcher watcher) {
        if (m_view instanceof TextView) {
            ((TextView)m_view).addTextChangedListener(watcher);
        }
        return self();
    }/*}}}*/
    // public T seekChanged(SeekBar.OnSeekBarChangeListener listener);/*{{{*/
    /**
     * Adds a handler to all changes in a seek bar.
     * \param listener Instance of OnSeekBarChangeListener.
     * \returns this.
     **/
    public T seekChanged(SeekBar.OnSeekBarChangeListener listener) {
        if (m_view instanceof SeekBar) {
            ((SeekBar)m_view).setOnSeekBarChangeListener(listener);
        }
        return self();
    }/*}}}*/
    //@}

    /** \name PROTECTED FIELDS */ //@{
    protected Activity m_activity;      /**< The Activity base class.       */
    protected View     m_view;          /**< The current working view.      */
    protected View     m_root;          /**< The self root view.            */
    //@}
}
// vim:syntax=java.doxygen
