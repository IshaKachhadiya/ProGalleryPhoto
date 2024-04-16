package com.iten.tenoku.ad.Facebook;

import static com.iten.tenoku.activity.AppSettingActivity.AD_TYPE;
import static com.iten.tenoku.utils.AdConstant.facebookNativeBannerAdModels;
import static com.iten.tenoku.utils.AdUtils.firebaseEvent;
import static com.iten.tenoku.utils.AdUtils.getOpacityColor;
import static com.iten.tenoku.utils.MyApplication.sharedPreferencesHelper;

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
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeBannerAd;
import com.iten.tenoku.databinding.FacebookNativeBannerAdLayoutBinding;
import com.iten.tenoku.listeners.AdsNativeAdListener;
import com.iten.tenoku.utils.AdConstant;
import com.iten.tenoku.utils.AdUtils;
import com.iten.tenoku.utils.LogUtils;
import com.iten.tenoku.utils.MyApplication;

import java.util.ArrayList;
import java.util.List;

public class FaceMetaNativeBannerAd {

    @SuppressLint("StaticFieldLeak")
    private static FaceMetaNativeBannerAd mInstance;
    @SuppressLint("StaticFieldLeak")
    private static Activity activity;
    String TAG = getClass().getSimpleName();
    int CurrentNativeAdPosition = -1;
    AdsNativeAdListener nativeAdListener = null;
    NativeBannerAd nativeAd = null;
    ViewGroup nativeAdViewGroup;
    int showedNativeAd = 0;
    boolean isNeedToLoadFbBanner = true;
    Boolean adLoaded = false;

    public FaceMetaNativeBannerAd(Activity activity) {
        FaceMetaNativeBannerAd.activity = activity;

    }

    public static FaceMetaNativeBannerAd getInstance(Activity adShowingActivity) {
        if (mInstance == null) {
            mInstance = new FaceMetaNativeBannerAd(adShowingActivity);
        }
        return mInstance;
    }

    public void loadFacebookNativeBannerAds() {
        if (isNeedToLoadFbBanner) {
            if (MyApplication.sharedPreferencesHelper.getFacebookAdShow()) {
                if (!facebookNativeBannerAdModels.isEmpty()) {
                    if (CurrentNativeAdPosition == facebookNativeBannerAdModels.size() - 1)
                        CurrentNativeAdPosition = 0;
                    else CurrentNativeAdPosition++;

                    nativeAd = new NativeBannerAd(activity, facebookNativeBannerAdModels.get(CurrentNativeAdPosition).getAdUnit());
                    NativeAdListener nativeAdListener = new NativeAdListener() {
                        @Override
                        public void onMediaDownloaded(Ad ad) {
                        }

                        @Override
                        public void onError(Ad ad, AdError adError) {
                            LogUtils.logE(TAG, "FaceMetaNativeBannerAd onAdFailedToLoad ===> " + CurrentNativeAdPosition + " Error ==> " + adError.getErrorMessage());
                            firebaseEvent("FbNativeBannerAd onAdFailed " + CurrentNativeAdPosition + "_Cd_" + adError.getErrorCode());
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
                            isNeedToLoadFbBanner = true;
                            NativeAdCallBack(false);
                            nativeAd = null;

                        }

                        @Override
                        public void onAdLoaded(Ad ad) {
                            isNeedToLoadFbBanner = true;
                            if (nativeAd == null) {
                                nativeAd = (NativeBannerAd) ad;
                            }
                            LogUtils.logE(TAG, "FaceMetaNativeBannerAd onAdLoaded ===> " + CurrentNativeAdPosition + " ID ==> " + ad.getPlacementId());
                            firebaseEvent("FbNativeBannerAd onAdLoaded " + CurrentNativeAdPosition);
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
                    isNeedToLoadFbBanner = false;
                    nativeAd.loadAd(nativeAd.buildLoadAdConfig().withAdListener(nativeAdListener).build());
                    LogUtils.logD(TAG, "FaceMetaNativeBannerAd adRequest ===> " + CurrentNativeAdPosition);
                    firebaseEvent("FbNativeBannerAd adRequest " + CurrentNativeAdPosition);
                } else {
                    NativeAdCallBack(false);
                }
            } else {
//                NativeAdCallBack(false);
            }

        } else {
            LogUtils.logE(TAG, "loadFacebookNativeBannerAds Not Loaded");
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
        if (MyApplication.sharedPreferencesHelper.getFacebookAdShow()) {
            if (!facebookNativeBannerAdModels.isEmpty()) {
                nativeAdViewGroup = nativeAdContainer;
                if (nativeAd != null) {
                    SmallNativeAd(nativeAd);
                    nativeAd = null;
                } else {
                    loadFacebookNativeBannerAds();
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

    private void SmallNativeAd(NativeBannerAd nativeAd) {
        FacebookNativeBannerAdLayoutBinding facebookNativeBannerAdLayoutBinding = FacebookNativeBannerAdLayoutBinding.inflate(LayoutInflater.from(activity));
        displaySmallNativeAd(nativeAd, facebookNativeBannerAdLayoutBinding);
    }

    private void displaySmallNativeAd(NativeBannerAd loadedNativeAd, FacebookNativeBannerAdLayoutBinding adView) {
        adLoaded = false;
        showedNativeAd++;
        if (!loadedNativeAd.isAdLoaded()) {
            NativeAdCallBack(false);
        } else {
            LogUtils.logE(TAG, "FaceMetaNativeBannerAd Display --------------- " + CurrentNativeAdPosition);
            firebaseEvent("FbNativeBannerAd Display " + CurrentNativeAdPosition);
            nativeAdViewGroup.setVisibility(View.VISIBLE);
            nativeAd.unregisterView();
            nativeAdViewGroup.removeAllViews();
            nativeAdViewGroup.addView(adView.getRoot());

            adView.textAdTitle.setText(loadedNativeAd.getAdvertiserName());
            adView.textAdBody.setText(loadedNativeAd.getAdBodyText());
            adView.buttonCallToAction.setVisibility(loadedNativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
            adView.buttonCallToAction.setText(loadedNativeAd.getAdCallToAction());

            AdOptionsView adOptionsView = new AdOptionsView(activity, nativeAd, adView.smallNativeView);
            adView.layoutAdChoicesContainer.removeAllViews();
            adView.layoutAdChoicesContainer.addView(adOptionsView, 0);

            List<View> clickableViews = new ArrayList<>();
            clickableViews.add(adView.textTitle);
            clickableViews.add(adView.textAdTitle);
            clickableViews.add(adView.textAdBody);
            clickableViews.add(adView.imageAdIcon);
            clickableViews.add(adView.buttonCallToAction);

            if (loadedNativeAd.isAdLoaded()) {
                loadedNativeAd.registerViewForInteraction(adView.getRoot(), adView.imageAdIcon, clickableViews);
            } else {
                NativeAdCallBack(false);
                return;
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

            if (sharedPreferencesHelper.getPreload()) {
                loadFacebookNativeBannerAds();
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
