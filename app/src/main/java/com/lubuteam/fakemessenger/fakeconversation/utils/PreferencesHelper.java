package com.lubuteam.fakemessenger.fakeconversation.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesHelper {

    private static SharedPreferences sharedPreferences;
    private static final String NAME = "MyPref";
    public static final String PHOTO_AVATAR_PATH = "photo avatar path";
    public static final String ACCEPT_POLICY = "accept policy";

    public static void init(Context mContext) {
        sharedPreferences = mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    private static SharedPreferences.Editor editor() {
        return sharedPreferences.edit();
    }

    public static void putBoolean(String key, boolean value) {
        editor().putBoolean(key, value).apply();
    }

    public static void putString(String key, String value) {
        editor().putString(key, value).apply();
    }

    public static void putInt(String key, int value) {
        editor().putInt(key, value).apply();
    }

    public static void putLong(String key, long value) {
        editor().putLong(key, value).apply();
    }

    public static boolean getBoolean(String key, boolean defaultvalue) {
        return sharedPreferences.getBoolean(key, defaultvalue);
    }

    public static String getString(String key) {
        return sharedPreferences.getString(key, "");
    }

    public static int getInt(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    public static long getLong(String key, long defaultValue) {
        return sharedPreferences.getLong(key, defaultValue);
    }

}
