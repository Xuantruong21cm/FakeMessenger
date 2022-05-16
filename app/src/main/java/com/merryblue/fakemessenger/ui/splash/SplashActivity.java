package com.merryblue.fakemessenger.ui.splash;

import static com.merryblue.fakemessenger.Config.CHANNEL_ID;
import static com.merryblue.fakemessenger.Config.NOTIFICATION_ON;
import static com.merryblue.fakemessenger.Config.SHOW_SLIDE_HINT;

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
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.ads.control.AdmobHelp;
import com.merryblue.fakemessenger.R;
import com.merryblue.fakemessenger.adapter.ViewPagerAdapter;
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
    private int dotscount;
    private ImageView[] dots;
    int currPos;
    int count = 0;

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
        calendar.set(Calendar.MINUTE, 1);
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
            splashBinding.guideView.setVisibility(View.VISIBLE);
            if (PreferencesHelper.getInt(SHOW_SLIDE_HINT, 0) < 3) {
                initSlideShow();
                count = PreferencesHelper.getInt(SHOW_SLIDE_HINT, 0) + 1;
                PreferencesHelper.putInt(SHOW_SLIDE_HINT, count);
                Log.e("SHOW_SLIDE_HINT", String.valueOf(PreferencesHelper.getInt(SHOW_SLIDE_HINT, 0)));

            } else {
                splashBinding.guideView.setVisibility(View.GONE);
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

    private void initPolicy() {
//        new Handler().postDelayed(() -> {
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
//        }, 1000);
    }

    private void initSlideShow() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(SplashActivity.this);
        splashBinding.slideView.setAdapter(viewPagerAdapter);
        splashBinding.slideView.setAdapter(viewPagerAdapter);
        dotscount = viewPagerAdapter.getCount();
        dots = new ImageView[dotscount];

        for (int i = 0; i < dotscount; i++) {

            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(10, 0, 10, 0);

            splashBinding.SliderDots.addView(dots[i], params);
        }
        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
        splashBinding.slideView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currPos = position;
                for (int i = 0; i < dotscount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        currPos = splashBinding.slideView.getCurrentItem();
        splashBinding.btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                splashBinding.guideView.setVisibility(View.GONE);
//                checkPermiss();
                initPolicy();
            }
        });

        splashBinding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currPos == dotscount - 1) {
                    splashBinding.guideView.setVisibility(View.GONE);
//                    checkPermiss();
                    initPolicy();
                } else {
                    splashBinding.slideView.setCurrentItem(currPos + 1);
                }
                Log.e("currPos == dotscount", String.valueOf(currPos) + " : " + dotscount);
            }
        });

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
