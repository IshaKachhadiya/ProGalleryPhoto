package com.iten.tenoku.ad.Facebook;

import static com.iten.tenoku.activity.AppSettingActivity.AD_TYPE;
import static com.iten.tenoku.utils.AdConstant.facebookNativeAdModels;
import static com.iten.tenoku.utils.AdUtils.firebaseEvent;
import static com.iten.tenoku.utils.AdUtils.getOpacityColor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdBase;
import com.facebook.ads.NativeAdListener;
import com.iten.tenoku.databinding.FacebookNativeMediumAdLayoutBinding;
import com.iten.tenoku.listeners.AdsNativeAdListener;
import com.iten.tenoku.utils.AdConstant;
import com.iten.tenoku.utils.AdUtils;
import com.iten.tenoku.utils.LogUtils;
import com.iten.tenoku.utils.MyApplication;

import java.util.ArrayList;
import java.util.List;

public class FaceMetaNativeMediumAd {

    @SuppressLint("StaticFieldLeak")
    private static FaceMetaNativeMediumAd mInstance;
    @SuppressLint("StaticFieldLeak")
    private static Activity activity;
    String TAG = getClass().getSimpleName();
    int CurrentNativeAdPosition = -1;
    Boolean adLoaded = false;
    AdsNativeAdListener nativeAdListener = null;
    NativeAd nativeAd = null;
    ViewGroup nativeAdViewGroup;
    int showedNativeAd = 0;
    boolean isNeedToLoadFbMedium = true;

    public FaceMetaNativeMediumAd(Activity activity) {
        FaceMetaNativeMediumAd.activity = activity;
    }

    public static FaceMetaNativeMediumAd getInstance(Activity adShowingActivity) {
        if (mInstance == null) {
            mInstance = new FaceMetaNativeMediumAd(adShowingActivity);
        }
        return mInstance;
    }

    public void LoadNativeMediumAds() {
        if (isNeedToLoadFbMedium) {
            if (MyApplication.sharedPreferencesHelper.getFacebookAdShow()) {
                if (!facebookNativeAdModels.isEmpty()) {
                    if (CurrentNativeAdPosition == facebookNativeAdModels.size() - 1)
                        CurrentNativeAdPosition = 0;
                    else CurrentNativeAdPosition++;

                    nativeAd = new NativeAd(activity, facebookNativeAdModels.get(CurrentNativeAdPosition).getAdUnit());
                    NativeAdListener nativeAdListener = new NativeAdListener() {
                        @Override
                        public void onMediaDownloaded(Ad ad) {
                        }

                        @Override
                        public void onError(Ad ad, AdError adError) {
                            LogUtils.logE(TAG, "FaceMetaNativeMediumAd onAdFailedToLoad ===> " + CurrentNativeAdPosition + " Error ==> " + adError.getErrorMessage());
                            firebaseEvent("FbNativeMediumAd onAdFailed " + CurrentNativeAdPosition + "_Cd_" + adError.getErrorCode());
                            if (MyApplication.sharedPreferencesHelper.getTotalFailedCount() != MyApplication.sharedPreferencesHelper.getFailedCount()) {
                                MyApplication.sharedPreferencesHelper.setTotalFailedCount(MyApplication.sharedPreferencesHelper.getTotalFailedCount() + 1);
                                MyApplication.sharedPreferencesHelper.setFbFailedCount(MyApplication.sharedPreferencesHelper.getFbFailedCount() +1);
                                LogUtils.logE(TAG, "onAdFailedToLoad: ----> " + MyApplication.sharedPreferencesHelper.getTotalFailedCount());
                                LogUtils.logE(TAG, "Facebook Count: ----> " + MyApplication.sharedPreferencesHelper.getFbFailedCount());
                            } else {
                                if (!MyApplication.sharedPreferencesHelper.getRequestDataSend()) {
                                    AdUtils.getInstance(activity).sendRequestData();
                                    MyApplication.sharedPreferencesHelper.setRequestDataSend(true);
                                }
                                MyApplication.sharedPreferencesHelper.setOwnAdShow(true);
                                AD_TYPE= AdUtils.AdType.OWNADS;
                            }
                            isNeedToLoadFbMedium = true;
                            NativeAdCallBack(false);
                            nativeAd = null;
                        }

                        @Override
                        public void onAdLoaded(Ad ad) {
                            isNeedToLoadFbMedium = true;
                            LogUtils.logE(TAG, "FaceMetaNativeMediumAd onAdLoaded ===> " + CurrentNativeAdPosition + " ID ==> " + ad.getPlacementId());
                            firebaseEvent("FbNativeMediumAd onAdLoaded " + CurrentNativeAdPosition);
                            if (nativeAd == null) {
                                nativeAd = (NativeAd) ad;
                            }
                            MyApplication.sharedPreferencesHelper.setTotalFailedCount(AdConstant.ResetTotalCount);
                            MyApplication.sharedPreferencesHelper.setAdmobFailedCount(AdConstant.ResetTotalCount);
                            MyApplication.sharedPreferencesHelper.setFbFailedCount(AdConstant.ResetTotalCount);
                            CheckNativeAd();
                        }

                        @Override
                        public void onAdClicked(Ad ad) {
                        }

                        @Override
                        public void onLoggingImpression(Ad ad) {
                        }
                    };
                    isNeedToLoadFbMedium = false;
                    nativeAd.loadAd(nativeAd.buildLoadAdConfig().withMediaCacheFlag(NativeAdBase.MediaCacheFlag.ALL).withAdListener(nativeAdListener).build());
                    LogUtils.logD(TAG, "FaceMetaNativeMediumAd adRequest ===> " + CurrentNativeAdPosition);
                    firebaseEvent("FbNativeMediumAd adRequest " + CurrentNativeAdPosition);
                }
            } else {
//                NativeAdCallBack(false);
            }
        } else {
            LogUtils.logE(TAG, "LoadFacebookNativeMediumAds Not Loaded");
        }
    }

    private void CheckNativeAd() {
        if (adLoaded) {
            if (nativeAdViewGroup != null && nativeAdListener != null) {
                ShowMediumNativeAd(nativeAdViewGroup, nativeAdListener);
            }
        }
    }

    public void ShowMediumNativeAd(ViewGroup nativeAdContainer, AdsNativeAdListener adListener) {
        this.nativeAdListener = adListener;
        if (MyApplication.sharedPreferencesHelper.getFacebookAdShow()) {
            if (!facebookNativeAdModels.isEmpty()) {
                nativeAdViewGroup = nativeAdContainer;
                if (nativeAd != null) {
                    mediumNativeAd(nativeAd);
                    nativeAd = null;
                } else {
                    LoadNativeMediumAds();
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

    private void mediumNativeAd(NativeAd nativeAd) {
        FacebookNativeMediumAdLayoutBinding facebookNativeMediumAdLayoutBinding = FacebookNativeMediumAdLayoutBinding.inflate(LayoutInflater.from(activity));
        displayMediumNativeAd(nativeAd, facebookNativeMediumAdLayoutBinding);
    }

    private void displayMediumNativeAd(NativeAd loadedNativeAd, FacebookNativeMediumAdLayoutBinding adView) {
        adLoaded = false;
        showedNativeAd++;
        if (!loadedNativeAd.isAdLoaded()) {
            NativeAdCallBack(false);
        } else {
            LogUtils.logE(TAG, "FaceMetaNativeMediumAd Display --------------- " + CurrentNativeAdPosition);
            firebaseEvent("FbNativeMediumAd Display " + CurrentNativeAdPosition);
            nativeAdViewGroup.setVisibility(View.VISIBLE);
            nativeAd.unregisterView();
            nativeAdViewGroup.removeAllViews();
            nativeAdViewGroup.addView(adView.getRoot());

            AdOptionsView adOptionsView = new AdOptionsView(activity, nativeAd, adView.layoutFacebook);
            adView.layoutAdChoicesContainer.removeAllViews();
            adView.layoutAdChoicesContainer.addView(adOptionsView, 0);

            adView.textAdTitle.setText(loadedNativeAd.getAdvertiserName());
            adView.textAdBody.setText(loadedNativeAd.getAdBodyText());
            adView.buttonCallToAction.setVisibility(loadedNativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
            adView.buttonCallToAction.setText(loadedNativeAd.getAdCallToAction());

            List<View> clickableViews = new ArrayList<>();
            clickableViews.add(adView.textAdTitle);
            clickableViews.add(adView.textAdBody);
            clickableViews.add(adView.buttonCallToAction);
            clickableViews.add(adView.imageAdIcon);
            clickableViews.add(adView.nativeAdMediaView);
            loadedNativeAd.registerViewForInteraction(adView.getRoot(), adView.nativeAdMediaView, adView.imageAdIcon, clickableViews);

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
                getOpacityColor(adView.layoutFacebook.getBackground(), MyApplication.sharedPreferencesHelper.getAdBgColorOpacity(), true);
            }

            if (MyApplication.sharedPreferencesHelper.getPreload()) {
                LoadNativeMediumAds();
            }
            NativeAdCallBack(true);
        }
    }

    public void NativeAdCallBack(Boolean aBoolean) {
        if (nativeAdListener != null) {
            nativeAdListener.onAdShown(aBoolean);
            nativeAdListener = null;
        }
    }

}