package com.ads.control;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;

public class AdmobHelp {
    private static AdmobHelp instance;
    InterstitialAd mInterstitialAd;

    private String popupUnit;
    private AdCloseListener adCloseListener;
    private boolean isReloaded = false;
    public static NativeAd mNativeAd;

    public static long timeLoad = 0;
    public static long TimeReload = 60*1000;
    public static int count = 0;
    private LoadAdsSuccess loadAdsSuccess;

    public static AdmobHelp getInstance() {
        if (instance == null) {
            instance = new AdmobHelp();
        }
        return instance;
    }

    private AdmobHelp() {
        List<String> testDeviceIds = Arrays.asList("685AF7B0FB12253EC1C4D549F5C5DB70");
        RequestConfiguration configuration =
                new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
        MobileAds.setRequestConfiguration(configuration);
    }

    public static boolean isDebug = false;
    private Context mContext;
    private WeakReference<Activity> activityWeakReference;

    public void init(Context context, boolean isDebuge, LoadAdsSuccess loadAdsSuccess) {
        AdmobHelp.isDebug = isDebuge;
        this.mContext = context;
        this.loadAdsSuccess = loadAdsSuccess;

        RequestConfiguration configuration = new RequestConfiguration.Builder()
                .setTestDeviceIds(Arrays.asList("685AF7B0FB12253EC1C4D549F5C5DB70", "95E7F255A57F142EC9E0C5EF801A7525"))
                .build();
        MobileAds.setRequestConfiguration(configuration);

        if (isDebug) {
            popupUnit = context.getString(R.string.admob_full_test);
        } else {
            popupUnit = context.getString(R.string.admob_full);
        }

        loadInterstitialAd();
        AdBannerUtil.getShareIntance().init(context);
    }

    public void setActivity(Activity activity) {
        this.activityWeakReference = new WeakReference<Activity>(activity);
    }

    private void loadInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this.mContext, popupUnit, adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        Log.e("DEBUG", "loadInterstitialAd - onAdLoaded");
                        mInterstitialAd = interstitialAd;
                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                if (adCloseListener != null) {
                                    adCloseListener.onAdClosed();
                                    loadInterstitialAd();
                                }
                            }
                        });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.e("DEBUG", "loadInterstitialAd - onAdFailedToLoad - " + loadAdError.getMessage());

                        mInterstitialAd = null;
                        if (isReloaded == false) {
                            isReloaded = true;
                            loadInterstitialAd();
                        }
                    }
                });
    }


    public void showInterstitialAd(AdCloseListener adCloseListener) {
        if ((timeLoad + TimeReload) < System.currentTimeMillis() && !AppUtils.isRemoveAds(mContext)) {
            count = count + 1;
            if (canShowInterstitialAd() && count == 3) {
                count = 0;
                this.adCloseListener = adCloseListener;
                mInterstitialAd.show(activityWeakReference.get());
                timeLoad = System.currentTimeMillis();
            } else {

                adCloseListener.onAdClosed();
            }
        } else {
            adCloseListener.onAdClosed();
        }

    }

    public void showInterstitialAdForNow(AdCloseListener adCloseListener) {
        if ((timeLoad + TimeReload) < System.currentTimeMillis() && !AppUtils.isRemoveAds(mContext)) {
//            count = count + 1;
            if (canShowInterstitialAd()) {
                this.adCloseListener = adCloseListener;
                mInterstitialAd.show(activityWeakReference.get());
                timeLoad = System.currentTimeMillis();
            } else {

                adCloseListener.onAdClosed();
            }
        } else {
            adCloseListener.onAdClosed();
        }

    }

    private boolean canShowInterstitialAd() {
        if (mInterstitialAd == null) {
            loadInterstitialAd();
            return false;
        }

        return mInterstitialAd != null && activityWeakReference != null;
    }

    public interface AdCloseListener {
        void onAdClosed();
    }

    public void loadBanner(final Activity mActivity) {
        if (AppUtils.isRemoveAds(mActivity)) {
            return;
        }
        final ShimmerFrameLayout containerShimmer =
                mActivity.findViewById(R.id.shimmer_container);

        containerShimmer.setVisibility(View.VISIBLE);
        containerShimmer.startShimmer();
        final LinearLayout adContainer = mActivity.findViewById(R.id.banner_container);

        try {
            AdView adView = new AdView(mActivity);
            String bannerID = mActivity.getString(R.string.admob_banner);
            if (isDebug) {
                bannerID = mActivity.getString(R.string.admob_banner_test);
            }
            adView.setAdUnitId(bannerID);
            Log.e("BANNER", "Banner ID: " + bannerID);


            adContainer.addView(adView);
            AdSize adSize = getAdSize(mActivity);
            // Step 4 - Set the adaptive ad size on the ad view.
            adView.setAdSize(adSize);


            adView.loadAd(new AdRequest.Builder().build());
            adView.setAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    Log.e("BANNER", "onAdFailedToLoad: " + loadAdError.getMessage());
                    adContainer.setVisibility(View.GONE);
                    containerShimmer.stopShimmer();
                    containerShimmer.setVisibility(View.GONE);
                }

                @Override
                public void onAdLoaded() {
                    Log.e("BANNER", "onAdLoaded");
                    containerShimmer.stopShimmer();
                    containerShimmer.setVisibility(View.GONE);
                    adContainer.setVisibility(View.VISIBLE);

                }
            });


        } catch (Exception e) {
        }
    }

    public interface LoadAdsSuccess {
        void loadAdsSuccess(boolean b);
    }


    private AdSize getAdSize(Activity mActivity) {
        // Step 2 - Determine the screen width (less decorations) to use for the ad width.
        Display display = mActivity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(mActivity, adWidth);

    }

    public void loadBannerFragment(final Activity mActivity, final View rootView) {
        final ShimmerFrameLayout containerShimmer =
                (ShimmerFrameLayout) rootView.findViewById(R.id.shimmer_container);

        containerShimmer.setVisibility(View.VISIBLE);
        containerShimmer.startShimmer();
        final LinearLayout adContainer = (LinearLayout) rootView.findViewById(R.id.banner_container);

        try {
            AdView adView = new AdView(mActivity);
            if (isDebug) {
                adView.setAdUnitId(mActivity.getString(R.string.admob_banner_test));
            } else {
                adView.setAdUnitId(mActivity.getString(R.string.admob_banner));
            }
            adContainer.addView(adView);
            AdSize adSize = getAdSize(mActivity);
            // Step 4 - Set the adaptive ad size on the ad view.
            adView.setAdSize(adSize);


            adView.loadAd(new AdRequest.Builder().build());
            adView.setAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    adContainer.setVisibility(View.GONE);
                    containerShimmer.stopShimmer();
                    containerShimmer.setVisibility(View.GONE);
                }

                @Override
                public void onAdLoaded() {
                    containerShimmer.stopShimmer();
                    containerShimmer.setVisibility(View.GONE);
                    adContainer.setVisibility(View.VISIBLE);
                }
            });


        } catch (Exception e) {
        }
    }

    public void loadNative(final Activity mActivity) {
        if (AppUtils.isRemoveAds(mActivity)) {
            return;
        }
        final ShimmerFrameLayout containerShimmer =
                (ShimmerFrameLayout) mActivity.findViewById(R.id.shimmer_container);
        containerShimmer.setVisibility(View.VISIBLE);
        containerShimmer.startShimmer();
        VideoOptions videoOptions = new VideoOptions.Builder()
                .setStartMuted(false)
                .build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();


        String nativeUnitAd = mActivity.getString(R.string.admob_native);
        if (isDebug) {
            nativeUnitAd = "/6499/example/native";
        }

        Log.e("#loadNative", " - nativeUnitAd -  " + nativeUnitAd);
        AdLoader adLoader = new AdLoader.Builder(mActivity, nativeUnitAd)
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAd) {
                        if (mNativeAd != null) {
                            mNativeAd.destroy();
                        }
                        containerShimmer.stopShimmer();
                        containerShimmer.setVisibility(View.GONE);

                        mNativeAd = nativeAd;
                        FrameLayout frameLayout =
                                mActivity.findViewById(R.id.fl_adplaceholder);

                        if (frameLayout != null) {
                            frameLayout.setVisibility(View.VISIBLE);
                            NativeAdView adView = (NativeAdView) mActivity.getLayoutInflater()
                                    .inflate(R.layout.native_admob_ad, null);
                            populateUnifiedNativeAdView(nativeAd, adView);
                            frameLayout.removeAllViews();
                            frameLayout.addView(adView);
                        }
                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(LoadAdError adError) {
                        // Handle the failure by logging, altering the UI, and so on.
                        containerShimmer.stopShimmer();
                        containerShimmer.setVisibility(View.GONE);
                    }
                })
                .withNativeAdOptions(adOptions)
                .build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }

    public void loadNativeFragment(final Activity mActivity, final View rootView) {
        if (AppUtils.isRemoveAds(mActivity)) {
            return;
        }

        final ShimmerFrameLayout containerShimmer =
                (ShimmerFrameLayout) rootView.findViewById(R.id.shimmer_container);
        containerShimmer.setVisibility(View.VISIBLE);
        containerShimmer.startShimmer();
        VideoOptions videoOptions = new VideoOptions.Builder()
                .setStartMuted(false)
                .build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();

        String nativeUnitAd = mActivity.getString(R.string.admob_native);
        if (isDebug) {
//            nativeUnitAd = mActivity.getString(R.string.native_test);
            nativeUnitAd = "/6499/example/native";
        }

        AdLoader adLoader = new AdLoader.Builder(mActivity, nativeUnitAd)
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAd) {
                        // Show the ad.
                        if (mNativeAd != null) {
                            mNativeAd.destroy();
                        }
                        containerShimmer.stopShimmer();
                        containerShimmer.setVisibility(View.GONE);

                        mNativeAd = nativeAd;
                        FrameLayout frameLayout =
                                rootView.findViewById(R.id.fl_adplaceholder);
                        if (frameLayout != null) {
                            frameLayout.setVisibility(View.VISIBLE);
                            NativeAdView adView = (NativeAdView) mActivity.getLayoutInflater()
                                    .inflate(R.layout.native_admob_ad, null);
                            populateUnifiedNativeAdView(nativeAd, adView);
                            frameLayout.removeAllViews();
                            frameLayout.addView(adView);
                        }

                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(LoadAdError adError) {
                        // Handle the failure by logging, altering the UI, and so on.
                        // Handle the failure by logging, altering the UI, and so on.
                        containerShimmer.stopShimmer();
                        containerShimmer.setVisibility(View.GONE);
                    }
                })
                .withNativeAdOptions(adOptions)
                .build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }

    private void populateUnifiedNativeAdView(NativeAd nativeAd, NativeAdView adView) {

        MediaView mediaView = adView.findViewById(R.id.ad_media);
        adView.setMediaView(mediaView);

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));

        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // The headline is guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((TextView) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad. The SDK will populate the adView's MediaView
        // with the media content from this native ad.
        adView.setNativeAd(nativeAd);

    }

    public void destroyNative() {
        if (mNativeAd != null) {
            mNativeAd.destroy();
        }
    }
}
