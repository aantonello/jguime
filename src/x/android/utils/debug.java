/**
 * \file
 * Defines the debug class.
 *
 * \author Alessandro Antonello <aantonello@paralaxe.com.br>
 * \date   September 30, 2010
 *
 * \par Copyright
 * This file is provided in hope that it will be useful to someone. It is
 * offered in public domain. You may use, modify or distribute it freely.
 *
 * The code is provided "AS IS". There is no warranty at all, of any kind. You
 * may change it if you like. Or just use it as it is.
 */
package x.android.utils;

import java.io.PrintStream;

import android.util.Log;

/**
 * \ingroup x_android_utils
 * Contain functions to write messages and data in the logging output.
 * In the Android environment, the logging facility is provided by the \c Log
 * object.
 *//* --------------------------------------------------------------------- */
public final class debug
{
    /** \name WRITE METHODS */ //@{
    // public static final void w(String text);/*{{{*/
    /**
     * Writes a string to the logging output.
     * \param text Text to be written. A new line will be appended at its end.
     **/
    public static final void w(String text) {
        if (!debug.enabled || (text == null)) return;
        if (debug.timestamp)
        {
            StringBuilder sb = new StringBuilder(13 + text.length());
            time_t tm = new time_t();
            
            sb.append('[');
            if (tm.hour() < 10) sb.append('0');
            sb.append(tm.hour()).append(':');
            if (tm.minute() < 10) sb.append('0');
            sb.append(tm.minute()).append(':');
            if (tm.seconds() < 10) sb.append('0');
            sb.append(tm.seconds()).append('.');
            if (tm.milliseconds() < 1000) sb.append('0');
            if (tm.milliseconds() < 100) sb.append('0');
            if (tm.milliseconds() < 10) sb.append('0');
            sb.append(tm.milliseconds());
            sb.append("] ").append(text);

            text = sb.toString();
        }
        Log.v(debug.TAG, text);
    }/*}}}*/
    // public static final void w(String fmt, Object... vList);/*{{{*/
    /**
     * Formats a list of arguments and write in the logging output.
     * \param fmt A string with format specification. This string must have
     *      one specification for argument in the list.
     * \param vList The list of arguments to be formated.
     * \remarks A line break will be appended to the end of the string when
     * written in the logging output.
     **/
    public static final void w(String fmt, Object... vList) {
        if (!debug.enabled) return;
        debug.w(String.format(fmt, vList));
    }/*}}}*/
    //@}

    /** \name EXCEPTION METHODS */ //@{
    // public static final void e(Exception e, String msg, Object... vList);/*{{{*/
    /**
     * Outputs a message of a exception occurred.
     * \param e The exception throwed.
     * \param msg A string with text to output and optional format specifiers.
     * The format follows the same rules of `String::format()` function with
     * some additions:
     * - \b $n: Will be replaced by the name of the exception class.
     * - \b $s: Will be replaced by the message of the exception.
     * - \b $t: Will be replaced by the stack trace output. The stack trace
     * output will have the following format:
     * <pre>
     * "file 'file_name' at line 'line_number' in 'class_name::method_name'"
     * </pre>
     * .
     * Notice that the marking character is a *dollar* ($) and not a percent
     * sign (%).
     * \param vList List of arguments to be formated inside the *msg* format
     * string. This list will follow the rules in `String::format()` function.
     **/
    public static final void e(Exception e, String msg, Object... vList) {
        if (!debug.enabled) return;

        msg = strings.replace(msg, "$t", "file '$f' at line '$l' in '$c::$m'");
        msg = debug.formatE(e, msg);
        debug.w(msg, vList);
    }/*}}}*/
    // public static final void e(Exception e, String msg);/*{{{*/
    /**
     * Outputs a message of a exception occurred.
     * \param e The exception throwed.
     * \param msg A string with text to output and optional format specifiers.
     * The format follows the same rules of `String::format()` function with
     * some additions:
     * - \b $n: Will be replaced by the name of the exception class.
     * - \b $s: Will be replaced by the message of the exception.
     * - \b $t: Will be replaced by the stack trace output. The stack trace
     * output will have the following format:
     * <pre>
     * "file 'file_name' at line 'line_number' in 'class_name::method_name'"
     * </pre>
     * .
     * Notice that the marking character is a *dollar* ($) and not a percent
     * sign (%).
     **/
    public static final void e(Exception e, String msg) {
        if (!debug.enabled) return;

        msg = strings.replace(msg, "$t", "file '$f' at line '$l' in '$c::$m'");
        msg = debug.formatE(e, msg);
        debug.w(msg);
    }/*}}}*/
    // public static final void e(String fmt, Exception ex);/*{{{*/
    /**
     * Writes an exception description at the logging output.
     * The output is marked as DEBUG so its kept in the release build.
     * \param fmt The format string. The only possible format incursions are:
     * - '%s': Inserts the description of the exception in the output.
     * - '%e': Inserts the exception class name int the output.
     * - '%m': Method name where the exception occurred.
     * - '%f': The file name where the exception occurred.
     * - '%l': The line number.
     * .
     * \param ex The Exception object.
     * \deprecated Please do not use this function. Instead use
     * debug#e(Exception,String,Object).
     **/
    public static final void e(String fmt, Exception ex)
    {
        if (!debug.enabled) return;

        char[] formatChars = fmt.toCharArray();
        char   c;
        int i = 0, limit = formatChars.length;
        StringBuilder sb = new StringBuilder(fmt.length() + 128);
        StackTraceElement[] ste = ex.getStackTrace();

        while (i < limit)
        {
            c = formatChars[i++];
            if (c != '%')
                sb.append(c);
            else if (i < limit)
            {
                switch (c = formatChars[i++])
                {
                case 's':
                    sb.append(ex.getMessage());
                    break;
                case 'e':
                    sb.append(ex.getClass().getSimpleName());
                    break;
                case 'm':
                    if ((ste != null) && (ste[0] != null))
                        sb.append(ste[0].getMethodName());
                    break;
                case 'f':
                    if ((ste != null) && (ste[0] != null))
                        sb.append(ste[0].getFileName());
                    break;
                case 'l':
                    if ((ste != null) && (ste[0] != null))
                        sb.append(ste[0].getLineNumber());
                    break;
                }
            }
        }
        debug.w(sb.toString());
    }/*}}}*/
    // public static final void p(String fmt, Exception ex);/*{{{*/
    /**
     * Writes a formated message and prints the stack trace.
     * The output is marked as VERBOSE so it isn't kept in the release build.
     * \param fmt The format string. The only possible format incursions are:
     * - '%s': Inserts the description of the exception in the output.
     * .
     * \param ex The Exception object.
     **/
    public static final void p(String fmt, Exception ex) {
        if (!debug.enabled) return;
        debug.w(String.format(fmt, ex.getMessage()) + "\n" + Log.getStackTraceString(ex));
    }/*}}}*/
    //@}

    /** \name DUMP METHODS */ //@{
    // public static final void d(byte[] vector, int start, int count);/*{{{*/
    /**
     * Dumps a byte array in the standard output.
     * \param vector the byte array to dump.
     * \param start the first byte of the array to dump.
     * \param count the number of bytes to dump starting from \a start.
     **/
    public static final void d(byte[] vector, int start, int count) {
        if (!debug.enabled) return;
        if (vector == null) {
            debug.w("[null]");
            return;
        } else if (vector.length == 0) {
            debug.w("[]0");
            return;
        }

        if (start < 0) start = 0;
        if (start >= vector.length) start = 0;

        int limit = (vector.length - start);

        if ((count > 0) && ((start + count) <= limit)) {
            limit = start + count;
        }

        /* Create a buffer. We do not write directly to the log. */
        StringBuffer sb = new StringBuffer(count * 3 + 3);
        sb.append('[');

        while (start < limit) {
            sb.append(String.format(" %02X", vector[start]));
            start++;
        }
        sb.append(" ]");

        debug.w(sb.toString());
    }/*}}}*/
    // public static final void d(String pre, byte[] vector, int start, int count, String pos);/*{{{*/
    /**
     * Dumps a byte array in the standard output.
     * \param pre A string to be writen before the dump.
     * \param vector the byte array to dump.
     * \param start the first byte of the array to dump.
     * \param count the number of bytes to dump starting from \a start.
     * \param pos A string to be writen after the dump.
     **/
    public static final void d(String pre, byte[] vector, int start, int count, String pos) {
        if (!debug.enabled) return;

        if (pre == null) pre = strings.EMPTY;
        if (pos == null) pos = strings.EMPTY;

        if (vector == null) {
            debug.w(pre + " null" + pos);
            return;
        } else if (vector.length == 0) {
            debug.w(pre + " zero" + pos);
            return;
        }

        if (start < 0) start = 0;
        if (start >= vector.length) start = 0;

        int limit = (vector.length - start);

        if ((count > 0) && ((start + count) <= limit)) {
            limit = start + count;
        }

        /* Create a buffer. We do not write directly to the log. */
        StringBuffer sb = new StringBuffer(strings.length(pre) + count * 3 + 3 + strings.length(pos));
        sb.append(pre);

        while (start < limit) {
            sb.append(String.format(" %02X", vector[start]));
            start++;
        }
        sb.append(pos);

        debug.w(sb.toString());
    }/*}}}*/
    //@}

    /** \name DIRECT OUTPUT METHODS */ //@{
    // protected static final void out(String fmt, Object... args);/*{{{*/
    /**
     * Output a series of arguments formatted in the given string.
     * \param fmt The string specifying the format.
     * \param args The agument list to be formated.
     * \remarks The list of arguments will be output directly using \c
     * System.out stream.
     **/
    protected static final void out(String fmt, Object... args) {
        out(strings.format(fmt, args));
    }/*}}}*/
    // protected static final void out(String text);/*{{{*/
    /**
     * Sends a text for output in the system output stream.
     * \param text Text to be written.
     **/
    protected static final void out(String text) {
        System.out.print(text);
    }/*}}}*/
    // protected static final void err(String fmt, Object... args);/*{{{*/
    /**
     * Sends a formated text in the system error output stream.
     * \param fmt The string specifying the format.
     * \param args The agument list to be formated.
     **/
    protected static final void err(String fmt, Object... args) {
        err( strings.format(fmt, args) );
    }/*}}}*/
    // protected static final void err(String text);/*{{{*/
    /**
     * Sends output to the default error stream.
     * \param text Text to be written.
     **/
    protected static final void err(String text) {
        System.err.print(text);
    }/*}}}*/
    //@}

    /** \name PRIVATE METHODS */ //@{
    // static String formatE(Exception ex, String fmt);/*{{{*/
    /**
     * Formats an exception class int a string.
     * \param ex Exception to format.
     * \param fmt String with format specifiers. The specifiers must have the
     * following format:
     * - **$n**: Name of the exception class.
     * - **$s**: Exception class message, as obtained with the `getMessage()`
     * method invocation.
     * - **$f**: File name where the exception was thrown.
     * - **$l**: Line number where the exception was thrown.
     * - **$m**: Method name where the exception was thrown.
     * - **$c**: Class name where the exception was thrown.
     * .
     * \returns The function returns a string with all values replaced.
     **/
    static String formatE(Exception ex, String fmt) {
        StackTraceElement[] elements;
        StringBuilder sb;
        int limit = strings.length(fmt);
        int index = 0;
        char c;

        elements = ex.getStackTrace();
        if (arrays.length(elements) == 0)
            elements = null;

        sb = new StringBuilder(128 + limit);
        while (index < limit)
        {
            c = fmt.charAt(index++);
            if (c != '$')
            {
                sb.append(c);
                continue;
            }

            switch ( (c = fmt.charAt(index++)) )
            {
            case 'n':
                sb.append(ex.getClass().getSimpleName());
                break;
            case 's':
                sb.append(ex.getMessage());
                break;
            case 'f':
                if (elements != null)
                    sb.append(elements[0].getFileName());
                break;
            case 'l':
                if (elements != null)
                    sb.append(elements[0].getLineNumber());
                break;
            case 'm':
                if (elements != null)
                    sb.append(elements[0].getMethodName());
                break;
            case 'c':
                if (elements != null)
                    sb.append(elements[0].getClassName());
                break;
            }
        }
        return sb.toString();
    }/*}}}*/
    //@}

    /** \name PUBLIC STATIC VARIABLES */ //@{
    public static boolean enabled = true;       /**< Debugging enabled by default. */
    public static boolean timestamp = true;     /**< Preceed output with timestamp. */
    //@}

    /** \name PUBLIC CONSTANTS */ //@{
    public static final String TAG = "X__X";    /**< Logging tag.   */
    //@}
}
// vim:syntax=java.doxygen
