package com.lubuteam.fakemessenger.fakeconversation.ui.splash;

import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;

import com.ads.control.AdmobHelp;
import com.lubuteam.fakemessenger.fakeconversation.R;
import com.lubuteam.fakemessenger.fakeconversation.databinding.ActivitySplashBinding;
import com.lubuteam.fakemessenger.fakeconversation.dialog.DialogEditProfile;
import com.lubuteam.fakemessenger.fakeconversation.dialog.DialogPolicy;
import com.lubuteam.fakemessenger.fakeconversation.ui.BaseActivity;
import com.lubuteam.fakemessenger.fakeconversation.ui.mainScreen.MainActivity;
import com.lubuteam.fakemessenger.fakeconversation.utils.PreferencesHelper;

public class SplashActivity extends BaseActivity {

    private ActivitySplashBinding splashBinding;

    @Override
    protected View getLayoutResource() {
        splashBinding = ActivitySplashBinding.inflate(LayoutInflater.from(this));
        return splashBinding.getRoot();
    }

    @Override
    protected void initView() {
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
