/**
 * \file
 * Defines the RES class.
 *
 * \author Alessandro Antonello <aantonello@paralaxe.com.br>
 * \date   August 04, 2011
 *
 * \par Copyright
 * This file is provided in hope that it will be useful to someone. It is
 * offered in public domain. You may use, modify or distribute it freely.
 *
 * The code is provided "AS IS". There is no warranty at all, of any kind. You
 * may change it if you like. Or just use it as it is.
 */
package x.android.defs;

import android.os.Environment;

/**
 * \ingroup x_android_defs
 * Declares the published resource identifiers.
 *//* --------------------------------------------------------------------- */
public interface RES
{
    // public static final String ERRORS = "/x/android/[...]/errors.xml";/*{{{*/
    /**
     * Resource path for error description.
     **/
    public static final String ERRORS = "/x/android/[...]/errors.xml";
    /*}}}*/
    // public static final String GUI    = "/x/android/[...]/gui.xml";/*{{{*/
    /**
     * Resource path for strings used in the `x.android.ui` package.
     **/
    public static final String GUI    = "/x/android/[...]/gui.xml";
    /*}}}*/
    // public static final String CALEND = "/x/android/[...]/calendar.xml";/*{{{*/
    /**
     * Resource path for calendar and dates strings.
     **/
    public static final String CALEND = "/x/android/[...]/calendar.xml";
    /*}}}*/

    // public interface IDS; {{{
    /**
     * Declares all string resource IDs.
     * This list is used to load the string resources.
     * \see utils#res
     **/
    public interface IDS {
        /**
         * \name Interface Strings
         *///@{
        public static final int CANCEL = 1;
        public static final int OK     = 2;
        public static final int YES    = 3;
        public static final int NO     = 4;
        ///@} INTERFACE STRINGS 

        /**
         * \name Calendar Strings
         *///@{
        public static final int SUNDAY      = 1;
        public static final int MONDAY      = 2;
        public static final int THUSDAY     = 3;
        public static final int WEDNESDAY   = 4;
        public static final int TURSDAY     = 5;
        public static final int FRIDAY      = 6;
        public static final int SATURDAY    = 7;

        public static final int JAN = 11;
        public static final int FEB = 12;
        public static final int MAR = 13;
        public static final int APR = 14;
        public static final int MAY = 15;
        public static final int JUN = 16;
        public static final int JUL = 17;
        public static final int AUG = 18;
        public static final int SEP = 19;
        public static final int OCT = 20;
        public static final int NOV = 21;
        public static final int DEC = 22;
        ///@} CALENDAR STRINGS 
    } /*}}}*/
    // public interface STORAGE;/*{{{*/
    /**
     * Class that lists the names of directories in the public Android
     * storage.
     **/
    public interface STORAGE
    {
        // public static final String ALARMS = Environment.DIRECTORY_ALARMS;/*{{{*/
        /**
         * Directory to store audio files used as alarms rings.
         **/
        public static final String ALARMS = Environment.DIRECTORY_ALARMS;
        /*}}}*/
        // public static final String DCIM   = Environment.DIRECTORY_DCIM;/*{{{*/
        /**
         * Standard directory for photos when the device is mounted as a
         * câmera.
         **/
        public static final String DCIM   = Environment.DIRECTORY_DCIM;
        /*}}}*/
        // public static final String DOWNLOADS = Environment.DIRECTORY_DOWNLOADS;/*{{{*/
        /**
         * The user shared directory for downloaded files.
         **/
        public static final String DOWNLOADS = Environment.DIRECTORY_DOWNLOADS;
        /*}}}*/
        // public static final String MOVIES = Environment.DIRECTORY_MOVIES;/*{{{*/
        /**
         * Standard directory to place movies and video files.
         **/
        public static final String MOVIES = Environment.DIRECTORY_MOVIES;
        /*}}}*/
        // public static final String MUSIC  = Environment.DIRECTORY_MUSIC;/*{{{*/
        /**
         * Standard directory to put audio files intendend to be played with a
         * media player.
         **/
        public static final String MUSIC  = Environment.DIRECTORY_MUSIC;
        /*}}}*/
        // public static final String NOTIFIES = Environment.DIRECTORY_NOTIFICATIONS;/*{{{*/
        /**
         * Standard directory to place audio files intended as notifications.
         **/
        public static final String NOTIFIES = Environment.DIRECTORY_NOTIFICATIONS;
        /*}}}*/
        // public static final String PICTURES = Environment.DIRECTORY_PICTURES;/*{{{*/
        /**
         * Standard directory to place images that the user can select.
         **/
        public static final String PICTURES = Environment.DIRECTORY_PICTURES;
        /*}}}*/
        // public static final String PODCASTS = Environment.DIRECTORY_PODCASTS;/*{{{*/
        /**
         * Directory to find podcasts audio files.
         **/
        public static final String PODCASTS = Environment.DIRECTORY_PODCASTS;
        /*}}}*/
        // public static final String RINGTONES = Environment.DIRECTORY_RINGTONES;/*{{{*/
        /**
         * Directory to find ringtones audio files.
         **/
        public static final String RINGTONES = Environment.DIRECTORY_RINGTONES;
        /*}}}*/
    }/*}}}*/
}
// vim:syntax=java.doxygen
