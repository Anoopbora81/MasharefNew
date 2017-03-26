/*
 * LocaleManager.java
 *
 * Created By Zuhair on Jan, 9, 2017
 */

package com.sr.masharef.masharef.manager.locale;

import android.content.Context;

import com.sr.masharef.masharef.preferences.Preferences;

import java.util.Locale;

public class LocaleManager {


    public static void setLocale(Context context, Locale locale){
        if (locale != null) {
            Preferences prefs = new Preferences(context);
            prefs.set("language", locale.getLanguage());
            prefs.set("country", locale.getCountry());
            prefs.commit();
        }
    }

    public static boolean isLocaleExist(Context context){
        return getLocale(context)!=null;
    }

    public static Locale getLocale(Context context){

        Preferences prefs   = new Preferences(context);
        String lang         = prefs.get("language");
        String country      = prefs.get("country");
        Locale currLocale   = null;

        if (lang.isEmpty()){
           //TODO nothing here.
        }
        else{
            currLocale = new Locale(lang, country);
        }

        return  currLocale;

    }

    public static void resetLocale(Context context){
        Preferences prefs = new Preferences(context);
        prefs.removeKey("language");
        prefs.removeKey("country");
        prefs.commit();
    }

}
