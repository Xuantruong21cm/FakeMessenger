package com.merryblue.fakemessenger.ui.splash;

import static com.merryblue.fakemessenger.Config.CHANNEL_ID;
import static com.merryblue.fakemessenger.Config.NOTIFICATION_ON;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.ads.control.AdmobHelp;
import com.merryblue.fakemessenger.R;
import com.merryblue.fakemessenger.databinding.ActivitySplashBinding;
import com.merryblue.fakemessenger.dialog.DialogEditProfile;
import com.merryblue.fakemessenger.dialog.DialogPolicy;
import com.merryblue.fakemessenger.service.NotificationBroadCast;
import com.merryblue.fakemessenger.ui.BaseActivity;
import com.merryblue.fakemessenger.ui.mainScreen.MainActivity;
import com.merryblue.fakemessenger.PreferencesHelper;

import java.util.Calendar;

public class SplashActivity extends BaseActivity {

    private ActivitySplashBinding splashBinding;
    private View pr_start;

    @Override
    protected View getLayoutResource() {

        splashBinding = ActivitySplashBinding.inflate(LayoutInflater.from(this));
//        PreferencesHelper.putBoolean(NOTIFICATION_ON, true);
//        if (PreferencesHelper.getBoolean(NOTIFICATION_ON, false)) {
        Intent intent = new Intent(SplashActivity.this, NotificationBroadCast.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 19);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
//            PreferencesHelper.putBoolean(NOTIFICATION_ON, false);
//        }


        return splashBinding.getRoot();
    }


    @Override
    protected void initView() {
        pr_start = findViewById(R.id.pr_start);
        pr_start.setAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate));
        AdmobHelp.getInstance().init(this);
        new Handler().postDelayed(() -> {
            if (PreferencesHelper.getBoolean(PreferencesHelper.ACCEPT_POLICY, false)) {
                nextScreen();
            } else {
                new DialogPolicy.ExtendBuilder()
                        .onSetPositiveButton(getString(R.string.accept_and_continue), (baseDialog, data) -> {
                            baseDialog.dismiss();
                            PreferencesHelper.putBoolean(PreferencesHelper.ACCEPT_POLICY, true);
                            nextScreen();
                        })
                        .onSetNegativeButton(getString(R.string.exit), baseDialog -> {
                            baseDialog.dismiss();
                            finishAffinity();
                        })
                        .build()
                        .show(getSupportFragmentManager(), DialogEditProfile.class.getName());
            }
        }, 2000);
    }

    @Override
    protected void initData() {

    }

    private void nextScreen() {
        AdmobHelp.getInstance().showInterstitialAd(() -> {
            PreferencesHelper.putBoolean(PreferencesHelper.ACCEPT_POLICY, true);
            Intent mIntent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(mIntent);
            finish();
        });

    }

    @Override
    protected void initControl() {

    }


}
