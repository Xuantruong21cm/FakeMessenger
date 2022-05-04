package com.ads.control;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.ads.control.funtion.UtilsApp;



public class RateApp extends Dialog{
        Context mContext;
        String mEmail , mTitleEmail;
        int mStyle = 0;
        public RateApp(Context context, String email, String TitleEmail, int style){
            super(context);
            mContext = context;
            mEmail = email;
            mTitleEmail = TitleEmail;
            mStyle = style;

        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            if(mStyle==0){
                setContentView(R.layout.dialog_rate_app);
            }
            if(mStyle==1){
                setContentView(R.layout.dialog_rate_app);
            }
            if(mStyle==2){
                setContentView(R.layout.dialog_rate_app);
            }
            setContentView(R.layout.dialog_rate_app);
            TextView btnRate = findViewById(R.id.btn_good);
            TextView btn_not_good = findViewById(R.id.btn_not_good);
            TextView btn_late = findViewById(R.id.btn_late);
            btnRate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("Show_rate",true);
                    editor.commit();
                    UtilsApp.RateApp(mContext);
                    UtilsApp.ShowToastLong(mContext,"Thanks for rate and review ^^ ");
                    dismiss();
                    ((Activity)(mContext)).finish();
                }
            });
            btn_not_good.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("Show_rate",true);
                    editor.commit();
                    showFeedBackDialog();
                    dismiss();
                }
            });
            btn_late.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                    ((Activity)(mContext)).finish();
                }
            });



        }


    private void showFeedBackDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext,R.style.DialogTheme);
        builder.setTitle(mContext.getString(R.string.title_dialog_feed_back));
        builder.setMessage(mContext.getString(R.string.message_dialog_feed_back));

        String positiveText = mContext.getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        UtilsApp.SendFeedBack(mContext,mEmail,mTitleEmail);
                        ((Activity)(mContext)).finish();
                    }
                });

        String negativeText = mContext.getString(R.string.exit_app);
        AlertDialog.Builder builder1 = builder.setNegativeButton(negativeText,
                new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((Activity)(mContext)).finish();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        // display dialog
        dialog.show();
    }








}
