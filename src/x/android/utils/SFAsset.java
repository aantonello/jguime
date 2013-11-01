/**
 * \file
 * Defines the SFAsset class.
 *
 * \author Alessandro Antonello <aantonello@paralaxe.com.br>
 * \date   novembro 01, 2013
 * \since  jguime 2.4
 * \version 1.0
 *
 * \copyright
 * Paralaxe Tecnologia, 2013. All rights reserved.
 */
package x.android.utils;
/* Imports {{{ */
import java.util.*;
import java.io.InputStream;

import android.app.Application;
import android.content.*;
import android.content.res.*;

import x.android.utils.*;
import x.android.ui.CAndroidApp;
/* }}} Imports */
/**
 * \ingroup x_android_utils
 * Static class to help work with 'assets' resources in Android projects.
 * This class is used to load resource files from the 'assets' directory on a
 * project. This library has some resource files that can be put in this
 * directory so the application have access to it. The resource files are
 * available in the distribution directory under the folder 'res/jguime'.
 *
 * This class will use the `AssetManager` Android class to find the resources
 * you need. Read careful the functions documentation so you know how to
 * identify the required resource correctly.
 *//* --------------------------------------------------------------------- */
public final class SFAsset
{
    /** \name Static Operations */ //@{
    // public static InputStream Load(String resourcePath);/*{{{*/
    /**
     * This function loads the resource specified.
     * The resource must be in the assets directory of the Android project.
     * @param resourcePath Absolute or relative path of the resource to load.
     * When a relative path is given the function assumes that it is inside
     * the `jguime` folder, making ease to load a resource like the table of
     * errors descriptions. An absolute path will break this rule and make the
     * function load a file directly from the `assets` directory. For example,
     * if you need to load the list of errors descriptions you could simply
     * pass "errors.xml" in this parameter. Otherwise, if you need to load a
     * file from another directory you must pass the entire path, like
     * "/folder/of/files/file.xml".
     *
     * This argument also supports localization. Localization is separated in
     * sub-directories with identified by the ISO 639-1 two letter language
     * identifier ("en" for english, "pt" for portuguese). These directory
     * also may have the the ISO 3166-1 country code appended. In this case an
     * underscore is used to separate language identifiers from country codes
     * ("en_US" for United States english, "en_UK" for United Kingdom
     * english). The chosen language will be that defined in the system
     * configuration returned by `Locale::getDefault()`. To specify a
     * localized resource embed the string "[...]" in the path. For example:
     * @code
     
       InputStream is = SFAsset.Load("/some/folder/[...]/messages.xml");
       @endcode
     * Will cause the function to search a directory formed by the current
     * language and current country. If the current language is english and
     * the current country is United Kingdom the path where the file will be
     * searched is: "/some/folder/en_UK/messages.xml". If this directory is
     * not found the function will try only the language code (e.g.:
     * "/some/folder/en/messages.xml"). If none were found the fall
     * through will be remove the language notation:
     * "/some/folder/messages.xml".
     * @return On success this function returns an InputStream for reading the
     * file. On failure the result will be \b null.
     **/
    public static InputStream Load(String resourcePath)
    {
        if (strings.length(resourcePath) == 0)
            return null;

        if (resourcePath.charAt(0) == '/')
            resourcePath = strings.substr(resourcePath, 1, -1);
        else
            resourcePath = "jguime/[...]/" + resourcePath;

        InputStream is;

        if (resourcePath.indexOf("[...]") < 0)
            return _internal_open(resourcePath);
        else
        {
            String tempPath = _internal_applyLocale(resourcePath, true, true);

            if ((is = _internal_open(tempPath)) != null)
                return is;
            else
            {
                /* Try only the language code. */
                tempPath = _internal_applyLocale(resourcePath, false, true);
                if ((is = _internal_open(tempPath)) != null)
                    return is;
                else
                {
                    /* Try without both. */
                    tempPath = _internal_applyLocale(resourcePath, false, false);
                    return _internal_open(tempPath);
                }
            }
        }
    }/*}}}*/
    //@}

    /** \name Local Operations */ //@{
    // private static String _internal_applyLocale(String path, boolean country, boolean lang);/*{{{*/
    /**
     * Apply the language and country code in an asset file path.
     * @param path The path to change.
     * @param country Set to \b true to add the country code.
     * @param lang Set to \b true to add the language code.
     * @returns The string with localized path information. If both \a country
     * and \a lang are set to \b false, the string representing the localized
     * folder ("[...]") will be removed.
     **/
    private static String _internal_applyLocale(String path, boolean country, boolean lang)
    {
        Locale locale   = Locale.getDefault();
        String localizedFolder;

        if (lang)
        {
            localizedFolder = "/" + locale.getLanguage();
            if (country)
                localizedFolder = strings.format("%s_%s", localizedFolder, locale.getCountry());

            localizedFolder = localizedFolder + "/";
        }
        else if (country)
            localizedFolder = "/" + locale.getCountry() + "/";
        else
            localizedFolder = "/";

        return strings.replace(path, "/[...]/", localizedFolder);
    }/*}}}*/
    // private static InputStream _internal_open(String assetFile);/*{{{*/
    /**
     * Opens a file in the assets directory.
     * @param assetFile Path of the file.
     * @returns An InputStream instance on success. \b null on failure.
     **/
    private static InputStream _internal_open(String assetFile)
    {
        Resources   res = CAndroidApp.currentApp().getResources();
        AssetManager am = res.getAssets();

        try {
            return am.open(assetFile);
        }
        catch (Exception ex) {
            return null;
        }
    }/*}}}*/
    //@}
}
// vim:syntax=java.doxygen
