package com.iten.tenoku.ad.AdMob;

import static com.google.android.gms.ads.nativead.NativeAdOptions.ADCHOICES_TOP_RIGHT;
import static com.iten.tenoku.activity.AppSettingActivity.AD_TYPE;
import static com.iten.tenoku.utils.AdConstant.adMobNativeAdBannerModels;
import static com.iten.tenoku.utils.AdUtils.firebaseEvent;
import static com.iten.tenoku.utils.AdUtils.getOpacityColor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MediaAspectRatio;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.iten.tenoku.databinding.AdmobNativeBannerAdLayoutBinding;
import com.iten.tenoku.listeners.AdsNativeAdListener;
import com.iten.tenoku.utils.AdConstant;
import com.iten.tenoku.utils.AdUtils;
import com.iten.tenoku.utils.LogUtils;
import com.iten.tenoku.utils.MyApplication;

import java.util.Objects;

public class GoogleNativeBannerAd {

    @SuppressLint("StaticFieldLeak")
    public static GoogleNativeBannerAd mInstance;
    @SuppressLint("StaticFieldLeak")
    public static Activity activity;
    String TAG = getClass().getSimpleName();
    Boolean adLoaded = false;
    NativeAd nativeAd = null;
    int CurrentNativeAdSmallPosition = -1;
    AdsNativeAdListener nativeAdListener = null;
    ViewGroup nativeAdViewGroup;
    AdLoader adLoader;
    int showedNativeAd = 0;

    boolean isNeedToLoadBanner = true;

    public GoogleNativeBannerAd(Activity activity) {
        GoogleNativeBannerAd.activity = activity;
    }

    public static GoogleNativeBannerAd getInstance(Activity adShowingActivity) {
        GoogleNativeBannerAd.activity = adShowingActivity;
        if (mInstance == null) {
            mInstance = new GoogleNativeBannerAd(adShowingActivity);
        }
        return mInstance;
    }

    public void loadNativeBannerAds() {
        if (isNeedToLoadBanner) {
            if (MyApplication.sharedPreferencesHelper.getAdMobAdShow()) {
                if (nativeAd == null) {
                    if (!adMobNativeAdBannerModels.isEmpty()) {
                        if (CurrentNativeAdSmallPosition == adMobNativeAdBannerModels.size() - 1)
                            CurrentNativeAdSmallPosition = 0;
                        else CurrentNativeAdSmallPosition++;

                        adLoader = new AdLoader.Builder(activity, adMobNativeAdBannerModels.get(CurrentNativeAdSmallPosition).getAdUnit())
                                .forNativeAd(nativeAd -> {
                                    LogUtils.logE(TAG, "GoogleNativeBannerAd onAdLoaded ===> " + CurrentNativeAdSmallPosition + " ID ==> " + adMobNativeAdBannerModels.get(CurrentNativeAdSmallPosition).getAdUnit());
                                    firebaseEvent("GoogleNativeBannerAd onAdLoaded " + CurrentNativeAdSmallPosition);
                                    isNeedToLoadBanner = true;
                                    if (this.nativeAd == null) {
                                        this.nativeAd = nativeAd;
                                    }
                                    MyApplication.sharedPreferencesHelper.setTotalFailedCount(AdConstant.ResetTotalCount);
                                    MyApplication.sharedPreferencesHelper.setAdmobFailedCount(AdConstant.ResetTotalCount);
                                    MyApplication.sharedPreferencesHelper.setFbFailedCount(AdConstant.ResetTotalCount);
                                    CheckNativeAd();
                                })
                                .withAdListener(new AdListener() {
                                    @Override
                                    public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                                        LogUtils.logE(TAG, "GoogleNativeBannerAd onAdFailedToLoad ===> " + CurrentNativeAdSmallPosition + " Error Code ==> " + adError.getCode());
                                        LogUtils.logE(TAG, "GoogleNativeBannerAd onAdFailedToLoad ===> " + CurrentNativeAdSmallPosition + " Error ==> " + adError.getResponseInfo());
                                        firebaseEvent("GoogleNativeBannerAd onAdFailed " + CurrentNativeAdSmallPosition + "_Cd_" + adError.getCode());
                                        isNeedToLoadBanner = true;
                                        if (MyApplication.sharedPreferencesHelper.getTotalFailedCount() != MyApplication.sharedPreferencesHelper.getFailedCount()) {
                                            MyApplication.sharedPreferencesHelper.setAdmobFailedCount(MyApplication.sharedPreferencesHelper.getAdmobFailedCount() + 1);
                                            MyApplication.sharedPreferencesHelper.setTotalFailedCount(MyApplication.sharedPreferencesHelper.getTotalFailedCount() + 1);

                                            LogUtils.logE(TAG, "onAdFailedToLoad: ----> " + MyApplication.sharedPreferencesHelper.getTotalFailedCount());
                                            LogUtils.logE(TAG, "AdMob Count: ----> " + MyApplication.sharedPreferencesHelper.getAdmobFailedCount());
                                        } else {
                                            if (!MyApplication.sharedPreferencesHelper.getRequestDataSend()) {
                                                AdUtils.getInstance(activity).sendRequestData();
                                                MyApplication.sharedPreferencesHelper.setRequestDataSend(true);
                                            }

                                            MyApplication.sharedPreferencesHelper.setOwnAdShow(true);
                                            AD_TYPE = AdUtils.AdType.OWNADS;
                                        }

                                        NativeAdCallBack(false);
                                    }

                                })
                                .withNativeAdOptions(new NativeAdOptions.Builder().setRequestMultipleImages(true).setAdChoicesPlacement(ADCHOICES_TOP_RIGHT).setMediaAspectRatio(MediaAspectRatio.LANDSCAPE).build())
                                .build();

                        isNeedToLoadBanner = false;
                        adLoader.loadAd(new AdRequest.Builder().build());
                        LogUtils.logD(TAG, "GoogleNativeBannerAd adRequest ===> " + CurrentNativeAdSmallPosition);
                        firebaseEvent("GoogleNativeBannerAd adRequest " + CurrentNativeAdSmallPosition);
                    }
                }
            } else {
//                NativeAdCallBack(false);
            }
        } else {
            LogUtils.logE(TAG, "loadNativeBannerAds Not Loaded");
        }
    }

    private void CheckNativeAd() {
        if (adLoaded) {
            if (nativeAdViewGroup != null && nativeAdListener != null) {
                ShowNativeAdSmall(nativeAdViewGroup, nativeAdListener);
            }
        }
    }

    public void ShowNativeAdSmall(ViewGroup nativeAdContainer, AdsNativeAdListener adListener) {
        this.nativeAdListener = adListener;
        if (MyApplication.sharedPreferencesHelper.getAdMobAdShow()) {
            if (!AdConstant.adMobNativeAdBannerModels.isEmpty()) {
                nativeAdViewGroup = nativeAdContainer;
                if (nativeAd != null) {
                    SmallNativeAd(nativeAd);
                    nativeAd = null;
                } else {
                    loadNativeBannerAds();
                    adLoaded = true;
                }
            } else {
                nativeAdContainer.setVisibility(View.GONE);
                NativeAdCallBack(false);
            }
        } else {
            NativeAdCallBack(false);
        }
    }

    private void SmallNativeAd(NativeAd nativeAd) {
        nativeAdViewGroup.setVisibility(View.VISIBLE);
        adLoaded = false;
        nativeAdViewGroup.removeAllViews();
        AdmobNativeBannerAdLayoutBinding admobSmallNativeAdLayoutBinding = AdmobNativeBannerAdLayoutBinding.inflate(LayoutInflater.from(activity));
        displaySmallNativeAd(nativeAd, admobSmallNativeAdLayoutBinding);
        nativeAdViewGroup.addView(admobSmallNativeAdLayoutBinding.getRoot());
    }

    private void displaySmallNativeAd(NativeAd loadedNativeAd, AdmobNativeBannerAdLayoutBinding adView) {
        LogUtils.logE(TAG, "GoogleNativeBannerAd Display --------------- " + CurrentNativeAdSmallPosition);
        firebaseEvent("GoogleNativeBannerAd Display " + CurrentNativeAdSmallPosition);
        showedNativeAd++;
        adView.getRoot().setIconView(adView.imageAdIcon);
        adView.getRoot().setHeadlineView(adView.textAdTitle);
        adView.getRoot().setBodyView(adView.textAdBody);
        adView.getRoot().setCallToActionView(adView.buttonCallToAction);
        if (loadedNativeAd.getIcon() == null) {
            adView.imageAdIcon.setVisibility(View.GONE);
        } else {
            adView.imageAdIcon.setVisibility(View.VISIBLE);
            adView.imageAdIcon.setImageDrawable(loadedNativeAd.getIcon().getDrawable());
        }
        if (loadedNativeAd.getHeadline() == null) {
            adView.textAdTitle.setVisibility(View.GONE);
        } else {
            adView.textAdTitle.setVisibility(View.VISIBLE);
            adView.textAdTitle.setText(loadedNativeAd.getHeadline());
        }

        if (loadedNativeAd.getBody() == null) {
            adView.textAdBody.setVisibility(View.GONE);
        } else {
            adView.textAdBody.setVisibility(View.VISIBLE);
            adView.textAdBody.setText(loadedNativeAd.getBody());
        }


        if (loadedNativeAd.getCallToAction() == null) {
            adView.buttonCallToAction.setVisibility(View.GONE);
        } else {
            adView.buttonCallToAction.setVisibility(View.VISIBLE);
            adView.buttonCallToAction.setText(loadedNativeAd.getCallToAction());
        }

        adView.getRoot().setNativeAd(loadedNativeAd);
        VideoController videoController = Objects.requireNonNull(loadedNativeAd.getMediaContent()).getVideoController();
        if (videoController.hasVideoContent()) {
            videoController.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                public void onVideoEnd() {
                    super.onVideoEnd();
                }
            });
        }

        if (!MyApplication.sharedPreferencesHelper.getAdTitleColor().isEmpty()) {
            adView.textAdTitle.setTextColor(Color.parseColor(MyApplication.sharedPreferencesHelper.getAdTitleColor()));
        }
        if (!MyApplication.sharedPreferencesHelper.getAdDescriptionColor().isEmpty()) {
            adView.textAdBody.setTextColor(Color.parseColor(MyApplication.sharedPreferencesHelper.getAdDescriptionColor()));
        }
        if (!MyApplication.sharedPreferencesHelper.getAdButtonTextColor().isEmpty()) {
            adView.buttonCallToAction.setTextColor(Color.parseColor(MyApplication.sharedPreferencesHelper.getAdButtonTextColor()));
            adView.textTitle.setTextColor(Color.parseColor(MyApplication.sharedPreferencesHelper.getAdButtonTextColor()));
        }

        if (!MyApplication.sharedPreferencesHelper.getAdButtonColor().isEmpty()) {
            getOpacityColor(adView.buttonCallToAction.getBackground(), MyApplication.sharedPreferencesHelper.getAdButtonColorOpacity(), false);
            getOpacityColor(adView.textTitle.getBackground(), MyApplication.sharedPreferencesHelper.getAdButtonColorOpacity(), false);
        }

        if (!MyApplication.sharedPreferencesHelper.getAddBgColor().isEmpty()) {
            getOpacityColor(adView.smallNativeView.getBackground(), MyApplication.sharedPreferencesHelper.getAdBgColorOpacity(), true);
        }
        if (MyApplication.sharedPreferencesHelper.getPreload()) {
            nativeAd = null;
            loadNativeBannerAds();
        }
        NativeAdCallBack(true);
    }

    public void NativeAdCallBack(Boolean aBoolean) {
        if (nativeAdListener != null) {
            nativeAdListener.onAdShown(aBoolean);
            nativeAdListener = null;
        }
    }
}