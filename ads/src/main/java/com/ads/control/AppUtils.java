package com.ads.control;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppUtils {

    private volatile static AppUtils mInstance;
    private SharedPreferences mPref;
    private Context mContext;

    private static SharedPreferences sharedPreferences;

    public static final String USE_NEW_VERSION = "USE_NEW_VERSION";
    public static final String SKU_REMOVE_ADS = "remove_ads";
    public static final String SKU_SUBS_A_YEAR = "subs_a_year";
    public static final String SKU_BUYALL = "buy_all";
    public static final String TIME_RELOAD = "TIME_RELOAD";
    public static boolean isRemoveAds(Context context){
        if(context == null){
            return false;
        }
        if(sharedPreferences == null){
            sharedPreferences = context.getSharedPreferences("Setting", Context.MODE_PRIVATE);
        }

        boolean isBuyAll = getValue(context, SKU_BUYALL, false);
        if(isBuyAll){
            return true;
        }

//        boolean isSubAYear = getValue(context, SKU_SUBS_A_YEAR, false);
//        if(isSubAYear){
//            return true;
//        }

        return getValue(context, SKU_REMOVE_ADS, false);
    }

    public boolean isPremium(){
        return getBoolean(SKU_BUYALL, false);
    }

    public static boolean isBuyAll(Context context){
        boolean isBuyAll = getValue(context, SKU_BUYALL, false);
        if(isBuyAll){
            return true;
        }

        return getValue(context, SKU_SUBS_A_YEAR, false);
    }

    private static boolean getValue(Context context, String key, Boolean defaultValue){
        if(sharedPreferences == null){
            sharedPreferences = context.getSharedPreferences("Setting", Context.MODE_PRIVATE);
        }
        return sharedPreferences.getBoolean(key, defaultValue);
    }



    private AppUtils() {

    }

    public static AppUtils getInstance() {
        if (null == mInstance) {
            synchronized (AppUtils.class) {
                if (null == mInstance) {
                    mInstance = new AppUtils();
                }
            }
        }
        return mInstance;
    }

    public void init(Context context) {
        if (mContext == null) {
            mContext = context;
        }
        if (mPref == null) {
            mPref = context.getSharedPreferences("Setting", Context.MODE_PRIVATE);;
        }
    }

    public void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public void putLong(String key, long value) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public boolean getBoolean(String key) {
        return mPref.getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean def) {
        return mPref.getBoolean(key, def);
    }

    public boolean useNewVersion() {
        return getBoolean(AppUtils.USE_NEW_VERSION, false);
    }

    public void setPremium(boolean isPremium) {
        putBoolean(AppUtils.SKU_BUYALL, isPremium);
    }

    public void setRemoveAds(boolean isRemoveAds) {
        putBoolean(AppUtils.SKU_REMOVE_ADS, isRemoveAds);
    }
}
