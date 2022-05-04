package com.ads.control.funtion;

import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Locale;

public class UtilsApp {
    public static void ShowToastShort(Context mContext,String mtext) {
        Toast.makeText(mContext, mtext,
                Toast.LENGTH_SHORT).show();
    }
    public static void ShowToastLong(Context mContext, String mtext) {
        Toast.makeText(mContext, mtext,
                Toast.LENGTH_LONG).show();
    }
    public static void OpenMoreApp(Context mContext,String mStore) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://search?q=pub:"+mStore));
        mContext.startActivity(intent);
    }
    public static void OpenBrower(Context mContext,String mLink){
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(mLink));
            mContext.startActivity(intent);
        }  catch (Exception e) {
    }
    }
    public static boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            return packageManager.getApplicationInfo(packageName, 0).enabled;
        }
        catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
    public static void RateApp(Context mContext){
        mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" +mContext.getPackageName() )));
    }
    public static void SendFeedBack(Context mContext,String mEmail,String mEmailTitle) {
        String[] TO = { mEmail };
        Intent intentEmail = new Intent(Intent.ACTION_SEND);
        intentEmail.setData(Uri.parse("mailto:"));
        intentEmail.setType("message/rfc822");

        intentEmail.putExtra(Intent.EXTRA_EMAIL, TO);
        intentEmail.putExtra(Intent.EXTRA_SUBJECT, mEmailTitle);
        intentEmail.putExtra(Intent.EXTRA_TEXT, "Enter your FeedBack");

        try {
            mContext.startActivity(Intent.createChooser(intentEmail, "Send FeedBack..."));
        } catch (ActivityNotFoundException e) {
            ShowToastShort(mContext,"There is no email client installed.");
        }
    }

    public static boolean isConnectionAvailable(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable();
        }
        return false;
    }
    public static void shareApp(Context context)
    {
        final String appPackageName = context.getPackageName();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out the App at: https://play.google.com/store/apps/details?id=" + appPackageName);
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
    }

    public static boolean isMyServiceRunning(Class<?> serviceClass,Context mContext) {
        ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("isMyServiceRunning?", true + "");
                return true;
            }
        Log.i ("isMyServiceRunning?", false+"");
        return false;
    }
    public static void setLanguageAds(Context mContext){

            if (PreferenceManager.getDefaultSharedPreferences(mContext).getBoolean("Frist_ads",true)) {
                String language = PreferenceManager.getDefaultSharedPreferences(mContext).getString("lgAds","en");
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("Frist_ads",false);
                language = Locale.getDefault().getLanguage();
                if (language.equalsIgnoreCase("vi")) {
                    editor.putString("lgAds","vi");

                }
                editor.commit();
            }


    }
    public static String getTextLanguage(String mText, String key){
        String [] temp =null;
        temp = mText.split(key+":");
        if(temp.length==1){
           temp = mText.split("en"+":");
        }
        if(temp.length==1){
            return temp[0];
        }else{
            String [] text = temp[1].split(",,");
            return text[0];
        }




    }



}
