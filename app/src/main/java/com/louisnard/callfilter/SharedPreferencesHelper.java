package com.louisnard.callfilter;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by louisnard on 12/01/2018.
 */

public class SharedPreferencesHelper {

    // File
    private final static String SHARED_PREFERENCES_FILE = "call_filter_shared_preferences";

    // Keys
    private final static String KEY_BOOLEAN_CALL_FILTERING_ACTIVATION = "KEY_BOOLEAN_CALL_FILTERING_ACTIVATION";
    private final static String KEY_BOOLEAN_FILTER_ALL_CONTACTS = "KEY_BOOLEAN_FILTER_ALL_CONTACTS";
    private final static String KEY_BOOLEAN_FILTER_UNKWNOWN_CONTACTS = "KEY_BOOLEAN_FILTER_UNKWNOWN_CONTACTS";
    private final static String KEY_STRING_GROUPS_TO_FILTER = "KEY_STRING_GROUPS_TO_FILTER";

    // Basic getters and setters
    private static String getSharedPreferenceString(Context context, String key, String defValue) {
        final SharedPreferences sharedPrefs = context.getSharedPreferences(SHARED_PREFERENCES_FILE, 0);
        return sharedPrefs.getString(key, defValue);
    }

    private static void setSharedPreferenceString(Context context, String key, String value) {
        final SharedPreferences sharedPrefs = context.getSharedPreferences(SHARED_PREFERENCES_FILE, 0);
        final SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private static int getSharedPreferenceInt(Context context, String key, int defValue) {
        final SharedPreferences sharedPrefs = context.getSharedPreferences(SHARED_PREFERENCES_FILE, 0);
        return sharedPrefs.getInt(key, defValue);
    }

    private static void setSharedPreferenceInt(Context context, String key, int value) {
        final SharedPreferences sharedPrefs = context.getSharedPreferences(SHARED_PREFERENCES_FILE, 0);
        final SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    private static boolean getSharedPreferenceBoolean(Context context, String key, boolean defValue) {
        final SharedPreferences sharedPrefs = context.getSharedPreferences(SHARED_PREFERENCES_FILE, 0);
        return sharedPrefs.getBoolean(key, defValue);
    }

    private static void setSharedPreferenceBoolean(Context context, String key, boolean value) {
        final SharedPreferences sharedPrefs = context.getSharedPreferences(SHARED_PREFERENCES_FILE, 0);
        final SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    // Specific getters and setters
    public static boolean isCallFilteringActivated(Context context) {
        return getSharedPreferenceBoolean(context, KEY_BOOLEAN_CALL_FILTERING_ACTIVATION, false);
    }

    public static void isCallFilteringActivated(Context context, boolean isActivated) {
        setSharedPreferenceBoolean(context, KEY_BOOLEAN_CALL_FILTERING_ACTIVATION, isActivated);
    }

    public static boolean filterAllContacts(Context context) {
        return getSharedPreferenceBoolean(context, KEY_BOOLEAN_FILTER_ALL_CONTACTS, false);
    }

    public static void filterAllContacts(Context context, boolean filter) {
        setSharedPreferenceBoolean(context, KEY_BOOLEAN_FILTER_ALL_CONTACTS, filter);
    }

    public static boolean filterUnkwnownContacts(Context context) {
        return getSharedPreferenceBoolean(context, KEY_BOOLEAN_FILTER_UNKWNOWN_CONTACTS, false);
    }

    public static void filterUnkwnownContacts(Context context, boolean filter) {
        setSharedPreferenceBoolean(context, KEY_BOOLEAN_FILTER_UNKWNOWN_CONTACTS, filter);
    }

    public static HashSet<String> getGroupIdsToFilter(Context context) {
        HashSet<String> groupIds;
        final String groupIdsString = getSharedPreferenceString(context, KEY_STRING_GROUPS_TO_FILTER, "");
        if (groupIdsString != null) {
            groupIds = new HashSet<>(Arrays.asList(groupIdsString.split(",")));
        } else {
            groupIds = new HashSet<>();
        }
        return groupIds;
    }

    public static void setGroupIdsToFilter(Context context, HashSet<String> groupIdsToFilter) {
        final StringBuilder sb = new StringBuilder();
        for (String groupId : groupIdsToFilter) {
            sb.append(groupId).append(",");
        }
        setSharedPreferenceString(context, KEY_STRING_GROUPS_TO_FILTER, sb.toString());
    }
}
