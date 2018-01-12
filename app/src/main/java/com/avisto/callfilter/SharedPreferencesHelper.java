package com.avisto.callfilter;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by louisnard on 12/01/2018.
 */

public class SharedPreferencesHelper {

    // File
    public final static String SHARED_PREFERENCES_FILE = "call_filter_shared_preferences";

    // Keys
    public final static String KEY_BOOLEAN_GLOBAL_FILTERING_ACTIVATION = "KEY_BOOLEAN_GLOBAL_FILTERING_ACTIVATION";

    public static String getSharedPreferenceString(Context context, String key, String defValue){
        final SharedPreferences sharedPrefs = context.getSharedPreferences(SHARED_PREFERENCES_FILE, 0);
        return sharedPrefs.getString(key, defValue);
    }

    public static void setSharedPreferenceString(Context context, String key, String value){
        final SharedPreferences sharedPrefs = context.getSharedPreferences(SHARED_PREFERENCES_FILE, 0);
        final SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static int getSharedPreferenceInt(Context context, String key, int defValue){
        final SharedPreferences sharedPrefs = context.getSharedPreferences(SHARED_PREFERENCES_FILE, 0);
        return sharedPrefs.getInt(key, defValue);
    }

    public static void setSharedPreferenceInt(Context context, String key, int value){
        final SharedPreferences sharedPrefs = context.getSharedPreferences(SHARED_PREFERENCES_FILE, 0);
        final SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static boolean getSharedPreferenceBoolean(Context context, String key, boolean defValue){
        final SharedPreferences sharedPrefs = context.getSharedPreferences(SHARED_PREFERENCES_FILE, 0);
        return sharedPrefs.getBoolean(key, defValue);
    }

    public static void setSharedPreferenceBoolean(Context context, String key, boolean value){
        final  SharedPreferences sharedPrefs = context.getSharedPreferences(SHARED_PREFERENCES_FILE, 0);
        final SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }
}
