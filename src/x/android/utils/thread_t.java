/**
 * \file
 * Defines the thread_t class.
 *
 * \author Alessandro Antonello <aantonello@paralaxe.com.br>
 * \date   October 01, 2010
 *
 * \par Copyright
 * This file is provided in hope that it will be useful to someone. It is
 * offered in public domain. You may use, modify or distribute it freely.
 *
 * The code is provided "AS IS". There is no warranty at all, of any kind. You
 * may change it if you like. Or just use it as it is.
 */
package x.android.utils;

import x.android.defs.ERROR;

/**
 * \ingroup x_android_utils
 * Class that represents a working thread.
 * A working thread is a background thread. This class exists only because the
 * lack of execution control in the current J2ME Thread class implementation.
 * Is virtually impossible to interrupt a thread in the MIDP 2.0
 * specification. Because of that we create this class that exports methods
 * enabling the control application to start, stop and restart the thread
 * execution.
 *//* --------------------------------------------------------------------- */
public class thread_t implements Runnable
{
    /** \name CONSTRUCTORS */ //@{
    // protected thread_t();/*{{{*/
    /**
     * Default constructor.
     * Protected because this object must be extended. Not used alone.
     **/
    protected thread_t() {
        m_thread   = new Thread(this);
        m_exitCode = 0;
        m_stop   = 0;
    }/*}}}*/
    // thread_t(Thread other);/*{{{*/
    /**
     * Impersonate constructor.
     * \param other The thread to be impersonated.
     **/
    thread_t(Thread other) {
        m_thread   = other;
        m_exitCode = 0;
        m_stop   = 0;
    }/*}}}*/
    //@}

    /** \name STATIC FUNCTIONS */ //@{
    // public static thread_t getCurrent();/*{{{*/
    /**
     * Returns a reference to the current running thread.
     * This method is a \b noop. It returns a thread_t object but there is no
     * relationship with the real object instance of the running thread. The
     * result value is a new implementation. This method is 
     **/
    public static thread_t getCurrent() {
        return new thread_t(Thread.currentThread());
    }/*}}}*/
    // public static void     sleep(long interval);/*{{{*/
    /**
     * Stops the execution of this thread for the specified interval.
     * \param interval The period, in milliseconds, for the thread to be
     * interrupted.
     **/
    public static void sleep(long interval) {
        try { Thread.sleep(interval); }
        catch (Exception ex) {
            debug.e("thread_t::sleep() Exception: %s", ex);
        }
    }/*}}}*/
    //@}

    /** \name ATTRIBUTES */ //@{
    // public final boolean isActive();/*{{{*/
    /**
     * Returns a value indicating if this thread is active.
     * \returns \b true if the thread is active. \b false if the thread is
     * stopped.
     **/
    public final boolean isActive() {
        return m_thread.isAlive();
    }/*}}}*/
    // public final int getPriority();/*{{{*/
    /**
     * Returns the current priority of execution.
     * \returns One of the following constants:
     * - \c PRIORITY_CRITICAL: Critical thread execution time.
     * - \c PRIORITY_NORMAL: Normal thread execution time.
     * - \c PRIORITY_IDLE: Low thread execution time.
     * .
     **/
    public final int getPriority() {
        try { return m_thread.getPriority(); }
        catch (Exception ex) {
            debug.e("thread_t::getPriority() Exception:\n\t%s", ex);
        }
        return PRIORITY_NORMAL;
    }/*}}}*/
    // public final int getExitCode();/*{{{*/
    /**
     * Gets the code of the thread finalization.
     * If the thread is active, the return value will be \c ERROR.ACTIVE.
     **/
    public final int getExitCode() {
        if (m_thread.isAlive())
            return ERROR.ACTIVE;
        return m_exitCode;
    }/*}}}*/
    //@}

    /** \name OPERATIONS */ //@{
    // public final int  setPriority(int priority);/*{{{*/
    /**
     * Sets the priority of this thread.
     * \param priority One of the following values:
     *      - \c PRIORITY_CRITICAL: The maximum thread execution time.
     *      - \c PRIORITY_NORMAL: Normal execution time.
     *      - \c PRIORITY_IDLE: Idle execution time.
     *      .
     *      The priority can be changed any time. Even after the thread is
     *      started.
     * \return The normal result is \c ERROR.SUCCESS. If the \a priority value
     * is not one of the above the result will be \c ERROR.PARM.
     **/
    public final int  setPriority(int priority) {
        try { m_thread.setPriority(priority); }
        catch (Exception ex) {
            debug.e("thread_t::setPriority() exception:\n\t%s", ex);
            return ERROR.PARM;
        }
        return ERROR.SUCCESS;
    }/*}}}*/
    // public final int  start();/*{{{*/
    /**
     * Starts the execution of this thread.
     * The \c run() method is called immediately.
     * \return \c ERROR.SUCCESS or, if this thread is already running \c
     * ERROR.RUNNING. If there is an issue in security level, \c ERROR.ACCESS
     * will be returned.
     **/
    public final int start()
    {
        if (m_thread.isAlive())
            return ERROR.RUNNING;

        m_exitCode = 0;
        m_stop = 0;

        /* We always need to create a new thread. */
        Thread thread = new Thread(this);

        try {
            thread.setPriority( m_thread.getPriority() );
            m_thread = thread;
            m_thread.start();
        }
        catch (IllegalThreadStateException itse) {
            debug.e("thread_t::start(): state exception:\n\t%s", itse);
            m_exitCode = ERROR.RUNNING;
        }
        catch (SecurityException se) {
            debug.e("thread_t::start(): security exception:\n\t%s", se);
            m_exitCode = ERROR.ACCESS;
        }
        catch (Exception ex) {
            debug.e("thread_t::start() exception:\n\t%s", ex);
            m_exitCode = ERROR.EXCEPTION;
        }
        return m_exitCode;
    }/*}}}*/
    // public final int  abort();/*{{{*/
    /**
     * Signals this thread to end its operations.
     * The actual result is produced by the implementation. It should monitor
     * the state of the thread using the \c aborted() method and returns from
     * the main function as soon as possible.
     * \return The normal result is \c ERROR.SUCCESS. If the thread is already
     * stoped the return code will be \c ERROR.FAILED.
     **/
    public final int  abort() {
        if (!isActive()) return ERROR.FAILED;
        m_stop = SIGNAL_ABORT;
        return ERROR.SUCCESS;
    }/*}}}*/
    // public final int  join(long interval);/*{{{*/
    /**
     * Signals the thread to abort its execution and waits.
     * The function will wait until the thread fully stops or the interval
     * passed elapses, which comes first.
     * \param interval The maximum interval, in milliseconds, for the function
     *      to wait. If this value is zero the function will act as the \c
     *      abort() method except in the return value. That is, the thread will
     *      be signaled and the function will return \c ERROR.SUCCESS
     *      invariably. If this value is \c INFINITE (-1L) the function will
     *      never return until the thread fully stops. Notice that this is the
     *      only negative value allowed. If another negative value is passed
     *      the function will fail.
     * \return If the thread stops before the interval elapsed the result will
     * be \c ERROR.SUCCESS. If the interval elapses before the thread stops
     * the result will be \c ERROR.EXPIRED. If the thread is not running when
     * the function is called the result will also be \c ERROR.SUCCESS. If an
     * invalid value is passed through \a interval, the return will be \c
     * ERROR.PARM even if the thread is aborted and stopped successfully.
     **/
    public final int join(long interval) {
        if ((abort() == ERROR.FAILED) || (interval == 0L)) {
            return ERROR.SUCCESS;
        }

        if (interval == INFINITE) {
            while (isActive()) {
                sleep(40);
            }
            return ERROR.SUCCESS;
        }
        else if (interval < 0L) {
            return ERROR.PARM;    /* Traps invalid intervals. */
        }

        long counter = 0;
        while (isActive()) {
            sleep(40);
            counter += 40;
            if (counter >= interval) {
                return ERROR.EXPIRED;
            }
        }
        return ERROR.SUCCESS;
    }/*}}}*/
    //@}

    /** \name OVERRIDABLE */ //@{
    // public    void run();/*{{{*/
    /**
     * The thread execution function.
     * When the thread starts this method is called immediately. This
     * implementation calls the internal execute() method for derived
     * classes. The main difference is the returning type. The \c run() method
     * has no return type and the derived class must not forget to set the exit
     * code through \c setExitCode() method. The \c execute() method has an \b
     * int return type where the derived classes can return the exit code. In
     * this case the base \c thread_t class is responsible to fill the exit
     * code value.
     *
     * The execution of \c execute() method is also protected against
     * exceptions. That is, if a not handled exception occurs it will not
     * interrupt the main application, just this thread will be interrupted.
     * When the thread is interrupted because a not handled exception the exit
     * code will be a relative error code or an generic \c ERROR.EXCEPTION
     * code.
     **/
    public void run() {
        int exitCode = 0;
        try {
            exitCode = execute();
            m_exitCode = exitCode;
        }
        catch (Exception ex) {
            debug.p("thread_t::run(): Exception:\n%s", ex);
            m_exitCode = ERROR.EXCEPTION;
        }
    }/*}}}*/
    // protected int  execute();/*{{{*/
    /**
     * The thread execution function.
     * This function can be overrode by derived classes to execute their jobs.
     * The returning value will be used to set the exit code that will be
     * available through \c getExitCode() method.
     *
     * The execution of this method is protected against unhandled exceptions.
     * That is, an unhandled exception will only stops the execution of this
     * thread and not stop the main application thread or others threads.
     *
     * This method is called from the base \c run() implementation.
     * \return This implementation does nothing and returns \c ERROR.SUCCESS.
     **/
    protected int execute() {
        return ERROR.SUCCESS;
    }/*}}}*/
    //@}

    /** \name OVERRIDES */ //@{
    // public String toString();/*{{{*/
    /**
     * Overrides the \c toString() Object method.
     * \returns The name and priority of this thread.
     **/
    public String toString() {
        return m_thread.toString();
    }/*}}}*/
    //@}

    /** \name IMPLEMENTATION */ //@{
    // protected final boolean aborted();/*{{{*/
    /**
     * Checks if this thread was signaled to abort.
     * \return \b true if another thread has signaled this thread to abort.
     * Otherwise \b false.
     **/
    protected final boolean aborted() {
        return (m_stop == SIGNAL_ABORT);
    }/*}}}*/
    // protected final int     interval(long howLong);/*{{{*/
    /**
     * Pauses the execution of this thread for the specified.
     * \param howLong The time, in milliseconds, for the interval. This
     *      parameter can not be less than zero. Which means that even the \c
     *      INFINITE value is invalid.
     * \return If the thread is aborted while in this interval the result will
     * be \c ERROR.ABORTED. Notice that when the thread is aborted, the
     * interval period passed through \a howLong is ignored. Otherwise the
     * function returns \b ERROR.SUCCESS that means, actualy, the period of
     * time has expired. If \a howLong is invalid the function does nothing and
     * returns \c ERROR.PARM.
     **/
    protected final int interval(long howLong) {
        if (howLong < 0L) return ERROR.PARM;

        long counter = 0;

        do {
            if (aborted()) return ERROR.ABORTED;
            sleep(10L);
        } while ((counter += 10L) < howLong);
        return ERROR.SUCCESS;
    }/*}}}*/
    //@}

    /** \name DATA MEMBERS */ //@{
    private Thread m_thread;        /**< The thread object.     */
    private int    m_exitCode;      /**< The finalization code. */
    private volatile int m_stop;    /**< The abort signal.      */
    //@}

    /** \name CONSTANTS */ //@{
    public static final int PRIORITY_IDLE     = Thread.MIN_PRIORITY;
    public static final int PRIORITY_NORMAL   = Thread.NORM_PRIORITY;
    public static final int PRIORITY_CRITICAL = Thread.MAX_PRIORITY;
    public static final long INFINITE         = -1L;
    private static final int SIGNAL_ABORT     = -1;
    //@}
}
// vim:syntax=java.doxygen
