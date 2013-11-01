/**
 * \file
 * Defines the time_t class.
 *
 * \author Alessandro Antonello <aantonello@paralaxe.com.br>
 * \date   November 10, 2010
 *
 * \par Copyright
 * This file is provided in hope that it will be useful to someone. It is
 * offered in public domain. You may use, modify or distribute it freely.
 *
 * The code is provided "AS IS". There is no warranty at all, of any kind. You
 * may change it if you like. Or just use it as it is.
 */
package x.android.utils;

import java.util.TimeZone;
import java.util.Calendar;
import java.util.Date;
import java.text.DateFormat;

import x.android.defs.ERROR;
import x.android.defs.ENC;

/**
 * \ingroup x_android_utils
 * Represents a period in time.
 * Easy to use class to work with date and time, formating and calculations.
 *//* --------------------------------------------------------------------- */
public final class time_t
{
    /** \name CONSTRUCTOR */ //@{
    // public time_t();/*{{{*/
    /**
     * Default constructor.
     * Builds a time value with the current date and time.
     **/
    public time_t() {
        set( time_t.now() );
    }/*}}}*/
    // public time_t(long millisenconds);/*{{{*/
    /**
     * Parametrized constructor.
     * Build a date and time from the defined milliseconds.
     * \param milliseconds Value to initialize the date and time.
     **/
    public time_t(long milliseconds) {
        set( milliseconds );
    }/*}}}*/
    // public time_t(final time_t other);/*{{{*/
    /**
     * Copy constructor.
     * \param other The other instance to copy data.
     **/
    public time_t(final time_t other) {
        set( other );
    }/*}}}*/
    //@}

    /** \name PUBLIC STATIC OPERATIONS */ //@{
    // public static long    now();/*{{{*/
    /**
     * Gets the current date and time in milliseconds.
     **/
    public static long now() {
        return System.currentTimeMillis();
    }/*}}}*/
    // public static boolean isLeapYear(int year);/*{{{*/
    /**
     * Checks if the given year is a leap year.
     * \param year The year number with four digits. Values less the 100 will
     * be ignored and the function returns \b false.
     * \return \b true if the given year is a leap year. Otherwise \b false.
     **/
    public static boolean isLeapYear(int year) {
        if ((year < 100) || (year > 9999)) return false;
        return ((year & 3) == 0);
    }/*}}}*/
    // public static String  format(String spec, long time);/*{{{*/
    /**
     * Formats a date and time into a string.
     * \param spec The format specification. The accepted specification is
     * equal the \c strftime() C standard function.
     * \param time The time to format as a long value.
     * \return A string with the data formated as specified.
     **/
    public static String  format(String spec, long time) {
        time_t tm = new time_t(time);
        return tm.toString(spec);
    }/*}}}*/
    //@}

    /** \name PUBLIC ATTRIBUTES */ //@{
    // public int year();/*{{{*/
    /**
     * The number of the year in the represented date/time.
     **/
    public int year() {
        return (int)(m_year & 0x0000FFFF);
    }/*}}}*/
    // public int dayOfYear();/*{{{*/
    /**
     * Gets the number of the day in the year.
     * 1 is January 1th.
     **/
    public int dayOfYear() {
        return (int)(m_yearDay & 0x0000FFFF);
    }/*}}}*/
    // public int month();/*{{{*/
    /**
     * Gets the month number.
     * 1 is January, 2 is February and so on.
     **/
    public int month() {
        return (int)(m_month & 0x000000FF);
    }/*}}}*/
    // public int day();/*{{{*/
    /**
     * Gets the day in the month.
     **/
    public int day() {
        return (int)(m_day & 0x000000FF);
    }/*}}}*/
    // public int dayOfWeek();/*{{{*/
    /**
     * Gets the day number of the current week.
     * 1 is Sunday. 2 is Monday and so on.
     **/
    public int dayOfWeek() {
        return (int)(m_weekDay & 0x000000FF);
    }/*}}}*/
    // public int hour();/*{{{*/
    /**
     * Gets the hour in the day.
     **/
    public int hour() {
        return (int)(m_hour & 0x000000FF);
    }/*}}}*/
    // public int minute();/*{{{*/
    /**
     * Gets the minute in the hour.
     **/
    public int minute() {
        return (int)(m_minute & 0x000000FF);
    }/*}}}*/
    // public int seconds();/*{{{*/
    /**
     * Gets the seconds in the minute.
     **/
    public int seconds() {
        return (int)(m_seconds & 0x000000FF);
    }/*}}}*/
    // public int milliseconds();/*{{{*/
    /**
     * Gets the milliseconds of the second.
     **/
    public int milliseconds() {
        return (int)(m_millis & 0x0000FFFF);
    }/*}}}*/
    //@}

    /** \name PUBLIC OPERATIONS */ //@{
    // public int  set(long time);/*{{{*/
    /**
     * Sets the current date and time.
     * \param time A \b long value with the current date and time to set. This
     * can be retrived from the \c System.currentTimeMillis() method.
     * \return An error code. \c ERROR.SUCCESS when the date is set. \c
     * ERROR.PARM if the argument is not a valid date/time value.
     **/
    public int set(long time) {
        Calendar cal = Calendar.getInstance();

        cal.setTimeInMillis(time);
        try {
            m_year    = (short)(cal.get(Calendar.YEAR) & 0x0000FFFF);
            m_yearDay = (short)(cal.get(Calendar.DAY_OF_YEAR) & 0x0000FFFF);
            m_month   = (byte)((cal.get(Calendar.MONTH) + 1) & 0x000000FF);
            m_day     = (byte)(cal.get(Calendar.DATE) & 0x000000FF);
            m_weekDay = (byte)(cal.get(Calendar.DAY_OF_WEEK) & 0x000000FF);
            m_hour    = (byte)(cal.get(Calendar.HOUR_OF_DAY) & 0x000000FF);
            m_minute  = (byte)(cal.get(Calendar.MINUTE) & 0x000000FF);
            m_seconds = (byte)(cal.get(Calendar.SECOND) & 0x000000FF);
            m_millis  = (short)(cal.get(Calendar.MILLISECOND) & 0x0000FFFF);
        }
        catch (Exception ex) {
            debug.w("droid: time_t::set([long] %d) exception!", time);
            debug.e(" => %s", ex);
            return ERROR.PARM;
        }
        return ERROR.SUCCESS;
    }/*}}}*/
    // public int  set(int year, int month, int day, int hour, int minute, int second, int millis);/*{{{*/
    /**
     * Sets the current date and time.
     * \param year The year to be used.
     * \param month The month to be set.
     * \param day The day of the month.
     * \param hour The hour in the day.
     * \param minute The minute in the hour.
     * \param second The second in the minute.
     * \param millis The milliseconds in the second.
     * \return An error code. \c ERROR.SUCCESS when the date is set. \c
     * ERROR.PARM if one or more arguments are invalid.
     **/
    public int  set(int year, int month, int day, int hour, int minute, int second, int millis) {
        if ((year < 0) || (year > 9999)) return ERROR.PARM;
        if ((month < 1) || (month > 12)) return ERROR.PARM;

        if ((day < 1) || (day > 31))     return ERROR.PARM;
        if ((day > 29) && (month == 2))  return ERROR.PARM;
        if ((day > 30) && (month < 8) && ((month & 1) == 0)) return ERROR.PARM;
        if ((day > 30) && (month > 7) && ((month & 1) != 0)) return ERROR.PARM;

        if ((hour < 0) || (hour > 23))     return ERROR.PARM;
        if ((minute < 0) || (minute > 59)) return ERROR.PARM;
        if ((second < 0) || (second > 59)) return ERROR.PARM;
        if ((millis < 0) || (millis > 9999)) return ERROR.PARM;

        boolean leapYear = time_t.isLeapYear(year);
        if ((day > 28) && (month == 2) && !leapYear) return ERROR.PARM;

        final int[] yearDays  = {0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334, 365};
        final int weekDayBase = 4;      /* 1970-01-01 was a tursday. */
        final int leapYears   = 17;     /* Leap years from 1900 to 1970. */
        int yearDay = 0;
        int weekDay = 0;

        m_year    = (short)(year  & 0x0000FFFF);
        m_month   = (byte)(month  & 0x000000FF);
        m_day     = (byte)(day    & 0x000000FF);
        m_hour    = (byte)(hour   & 0x000000FF);
        m_minute  = (byte)(minute & 0x000000FF);
        m_seconds = (byte)(second & 0x000000FF);
        m_millis  = (short)(millis & 0x0000FFFF);

        /* Calculate the day of year. */
        yearDay = day + yearDays[month-1] + ((leapYear && (month > 2)) ? 1 : 0);
        m_yearDay = (short)(yearDay & 0x0000FFFF);

        /* Calculate the day of week */
        weekDay = (yearDay + ((year - 70) * 365) + ((year - 1) >> 2) - leapYears + weekDayBase) % 7;
        m_weekDay = (byte)(weekDay & 0x000000FF);

        return ERROR.SUCCESS;
    }/*}}}*/
    // public int  set(final time_t other);/*{{{*/
    /**
     * Sets this object with data from another instance.
     * \param other Another instance to use.
     * \return An error code. \c ERROR.SUCCESS when the date is set. \c
     * ERROR.PARM if the argument is not a valid date/time value.
     **/
    public int  set(final time_t other) {
        if (other == null) return ERROR.PARM;
        return set(other.year(), other.month(), other.day(),
                   other.hour(), other.minute(), other.seconds(),
                   other.milliseconds());
    }/*}}}*/
    // public long get();/*{{{*/
    /**
     * Gets the time value as the number of milliseconds past January 1, 1970.
     **/
    public long get() {
        Calendar cal = Calendar.getInstance();

        try {
            cal.set(Calendar.YEAR, (m_year & 0x0000FFFF));
            cal.set(Calendar.MONTH, ((m_month - 1) & 0x000000FF));
            cal.set(Calendar.DATE, (m_day & 0x000000FF));
            cal.set(Calendar.HOUR_OF_DAY, (m_hour & 0x000000FF));
            cal.set(Calendar.MINUTE, (m_minute & 0x000000FF));
            cal.set(Calendar.SECOND, (m_seconds & 0x000000FF));
            cal.set(Calendar.MILLISECOND, (m_millis & 0x0000FFFF));
        }
        catch (Exception ex) {
            debug.e("droid: time_t::get() exception: %s", ex);
            return 0L;
        }
        return cal.getTimeInMillis();
    }/*}}}*/
    //@}

    /** \name FORMARTERS */ //@{
    // public String getWeekDayName();/*{{{*/
    /**
     * Retrieves the name of the week day.
     * The name is provided internally. No system call is made. They are
     * provided by localized resources in the library.
     **/
    public String getWeekDayName()
    {
        CStringTable table = CStringTable.LoadAsset("calendar.xml");
        return table.get(m_weekDay);
    }/*}}}*/
    // public String getMonthName();/*{{{*/
    /**
     * Gets the name of the month.
     * The name is provided internally. No system call is made. They are
     * provided by localized resources in the library.
     **/
    public String getMonthName()
    {
        CStringTable table = CStringTable.LoadAsset("calendar.xml");
        return table.get(m_month + 10);
    }/*}}}*/
    //@}

    /** \name OVERRIDES */ //@{
    // public boolean equals(Object obj);/*{{{*/
    /**
     * Checks for equality with another object.
     * \param obj Object to check.
     * \returns \b true only if the passed object is a \c time_t instance
     * representing the exact period of time. Otherwise \b false.
     **/
    public boolean equals(Object obj) {
        if (!(obj instanceof time_t)) return false;
        return (((time_t)obj).get() == this.get());
    }/*}}}*/
    // public String  toString();/*{{{*/
    /**
     * Gets a string representation of this object.
     * In this implementation the string returned contains the formated date
     * and time as of passed the specifier '%c'.
     **/
    public String toString() {
        return _internal_formatDateTime();
    }/*}}}*/
    // public String  toString(String spec);/*{{{*/
    /**
     * Formats a string with the internal date/time.
     * \param spec A string with formating specifiers.
     * \return A sting the the date/time formated as requested.
     * \remarks The function works like the \c strftime() standard C function.
     * That means a formating specifier is a sequence of a percent sign and a
     * single character (e.g.: '%c'). The folloing fields are recognized:
     * - \b B: The month name.
     * - \b c: Formats the current date and time according to the current
     *         locale specification.
     * - \b d: The month day with 2 digits.
     * - \b H: The hour value with 2 digits.
     * - \b m: The month number with 2 digits.
     * - \b M: The minute in the hour with 2 digits.
     * - \b S: The second in the minute with 2 digits.
     * - \b w: The week day number with 1 digit (from 1 to 7).
     * - \b W: The week day name.
     * - \b x: The date according to the locale specification.
     * - \b X: The time according to the locale specification.
     * - \b y: The year number with 2 digits. Avoid using this.
     * - \b Y: The yera number with 4 digits.
     * .
     **/
    public String toString(String spec) {
        if (spec == null) return null;

        CStringTable str = CStringTable.LoadAsset("calendar.xml");
        StringBuffer sb  = new StringBuffer(spec.length());
        char[] chars = spec.toCharArray();
        int i = 0, limit = chars.length;

        while (i < limit) {
            if ((chars[i] != '%') || (i == (limit - 1))) {
                sb.append(chars[i++]);
                continue;
            }
            
            switch (chars[++i]) {
            case 'B':
                sb.append(str.get(m_month));
                break;
            case 'c':
                sb.append(_internal_formatDateTime());
                break;
            case 'd':
                sb.append(String.format("%02d", m_day));
                break;
            case 'H':
                sb.append(String.format("%02d", m_hour));
                break;
            case 'm':
                sb.append(String.format("%02d", m_month));
                break;
            case 'M':
                sb.append(String.format("%02d", m_minute));
                break;
            case 'S':
                sb.append(String.format("%02d", m_seconds));
                break;
            case 'w':
                sb.append(String.format("%d", m_weekDay));
                break;
            case 'W':
                sb.append(str.get(m_weekDay));
                break;
            case 'x':
                sb.append(_internal_formatDate());
                break;
            case 'X':
                sb.append(_internal_formatTime());
                break;
            case 'y':
                sb.append(String.format("%02d", m_year % 100));
                break;
            case 'Y':
                sb.append(String.format("%04d", m_year));
                break;
            default:
                sb.append(chars[i]);
            }
            i++;
        }
        return sb.toString();
    }/*}}}*/
    //@}

    /** \name INTERNAL OPERATIONS */ //@{
    // private String _internal_formatDateTime();/*{{{*/
    /**
     * Function used when to format the date and time in the current locale.
     * \returns A string with the date formated as in the current locale.
     **/
    private String _internal_formatDateTime() {
        Date dt = new Date(this.get());
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
        return df.format(dt);
    }/*}}}*/
    // private String _internal_formatDate();/*{{{*/
    /**
     * Function used when to format the date in the current locale.
     * \returns A string with the date formated as in the current locale.
     **/
    private String _internal_formatDate() {
        Date dt = new Date(get());
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
        return df.format(dt);
    }/*}}}*/
    // private String _internal_formatTime();/*{{{*/
    /**
     * Function used when to format the time in the current locale.
     * \returns A string with the time formated as in the current locale.
     **/
    private String _internal_formatTime() {
        Date dt = new Date(get());
        DateFormat df = DateFormat.getTimeInstance(DateFormat.SHORT);
        return df.format(dt);
    }/*}}}*/
    //@}

    /** \name DATA MEMBERS */ //@{
    private short m_year;           /**< The year from 0 to 9999. */
    private short m_yearDay;        /**< The day in the year. */
    private byte  m_month;          /**< Month number, 1 is January. */
    private byte  m_day;            /**< The day number. */
    private byte  m_weekDay;        /**< Week day. */
    private byte  m_hour;           /**< The day hour from 0 to 23. */
    private byte  m_minute;         /**< Minutes in current hour. */
    private byte  m_seconds;        /**< Seconds in the minute. */
    private short m_millis;         /**< Milliseconds in the second. */
    //@}
}
// vim:syntax=java.doxygen
