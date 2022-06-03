package com.ads.control;

import android.content.Context;
import android.util.DisplayMetrics;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;

public class AdBannerUtil {
    private static AdBannerUtil adBannerUtil;
    private AdView mAdView;
    private boolean isReloaded = false;

    //private Context context;

    private AdBannerUtil() {
        //this.context = context;
    }

    public static AdBannerUtil getShareIntance() {
        if (adBannerUtil == null) {
            synchronized (AdBannerUtil.class) {
                if (adBannerUtil == null) {
                    adBannerUtil = new AdBannerUtil();
                }
            }

        }
        return adBannerUtil;
    }

    public AdView getAdView(Context context) {
        if (mAdView == null) {
            synchronized (AdBannerUtil.class) {
                init(context);
            }
        }
        else loadAD();
        return mAdView;
    }

    //
    public void init(final Context context) {

        if (mAdView == null) {
            mAdView = new AdView(context);
            mAdView.setAdUnitId(context.getString(R.string.admob_banner));
        }

        if (mAdView.getAdSize() == null){
            AdSize adSize = getAdSize(context);
            mAdView.setAdSize(adSize);
        }
        //if (mAdView.getAdUnitId() == null) mAdView.setAdUnitId(Defines.ADMOB_BANNER);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                super.onAdFailedToLoad(adError);
                // Log.d("Kiennt", "Loi load quang cao banner: " + adError.getMessage() + " Error code:" + adError.getCode());
                int iCode = adError.getCode();
                if (!isReloaded && (iCode == AdRequest.ERROR_CODE_INTERNAL_ERROR || iCode == AdRequest.ERROR_CODE_NO_FILL)) {
                    isReloaded = true;
                    loadAD();
                }
            }
        });
        loadAD();
    }


    private void loadAD() {
        if (mAdView != null) {

            AdRequest adRequest = new AdRequest.Builder()
                    .build();
            // Start loading the ad in the background.
            mAdView.loadAd(adRequest);
        }
    }

    private AdSize getAdSize(Context context) {
        DisplayMetrics outMetrics = context.getResources().getDisplayMetrics();
        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int)  (widthPixels / density);
        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        //return AdSize.getCurrentOrientationBannerAdSizeWithWidth(context, adWidth);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth);
    }
}
