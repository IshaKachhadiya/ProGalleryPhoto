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
import com.facebook.ads.NativeAdListener;
import com.iten.tenoku.databinding.FacebookNativeBigAdBtnTopLayoutBinding;
import com.iten.tenoku.databinding.FacebookNativeBigAdLayoutBinding;
import com.iten.tenoku.listeners.AdsNativeAdListener;
import com.iten.tenoku.utils.AdConstant;
import com.iten.tenoku.utils.AdUtils;
import com.iten.tenoku.utils.LogUtils;
import com.iten.tenoku.utils.MyApplication;

import java.util.ArrayList;
import java.util.List;

public class FaceMetaNativeBigAd {

    @SuppressLint("StaticFieldLeak")
    private static FaceMetaNativeBigAd mInstance;
    @SuppressLint("StaticFieldLeak")
    private static Activity activity;
    String TAG = getClass().getSimpleName();
    AdUtils.NativeType nativeType;
    int CurrentNativeAdPosition = -1;
    AdsNativeAdListener nativeAdListener = null;
    NativeAd nativeAd = null;
    ViewGroup nativeAdViewGroup;
    int showedNativeAd = 0;

    Boolean adLoaded = false;
    boolean isNeedToLoadFbBig = true;

    public FaceMetaNativeBigAd(Activity activity) {
        FaceMetaNativeBigAd.activity = activity;
    }

    public static FaceMetaNativeBigAd getInstance(Activity adShowingActivity) {
        if (mInstance == null) {
            mInstance = new FaceMetaNativeBigAd(adShowingActivity);
        }
        return mInstance;
    }

    public void LoadFacebookNativeBigAds() {
        if (isNeedToLoadFbBig) {
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
                            LogUtils.logE(TAG, "FaceMetaNativeBigAd onAdFailedToLoad ===> " + CurrentNativeAdPosition + "  Error ==> " + adError.getErrorMessage());
                            firebaseEvent("FbNativeBigAd onAdFailed " + CurrentNativeAdPosition + "_Cd_" + adError.getErrorCode());
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
                            isNeedToLoadFbBig = true;
                            NativeAdCallBack(false);
                            nativeAd = null;
                        }

                        @Override
                        public void onAdLoaded(Ad ad) {
                            isNeedToLoadFbBig = true;
                            LogUtils.logE(TAG, "FaceMetaNativeBigAd onAdLoaded ===> " + CurrentNativeAdPosition + " ID ==> " + ad.getPlacementId());
                            firebaseEvent("FbNativeBigAd onAdLoaded " + CurrentNativeAdPosition);
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
                    isNeedToLoadFbBig = false;
                    nativeAd.loadAd(nativeAd.buildLoadAdConfig().withAdListener(nativeAdListener).build());
                    LogUtils.logD(TAG, "FaceMetaNativeBigAd adRequest ===> " + CurrentNativeAdPosition);
                    firebaseEvent("FbNativeBigAd adRequest " + CurrentNativeAdPosition);
                }
            } else {
//                NativeAdCallBack(false);
            }
        } else {
            LogUtils.logE(TAG, "LoadFacebookNativeBigAds Not Loaded");
        }
    }

    private void CheckNativeAd() {
        if (adLoaded) {
            if (nativeAdViewGroup != null && nativeAdListener != null) {
                ShowTypeWiseNativeAd(nativeAdViewGroup, nativeType, nativeAdListener);
            }
        }
    }

    public void ShowTypeWiseNativeAd(ViewGroup nativeAdContainer, AdUtils.NativeType mNativeType, AdsNativeAdListener adListener) {
        this.nativeAdListener = adListener;
        if (MyApplication.sharedPreferencesHelper.getFacebookAdShow()) {
            if (!facebookNativeAdModels.isEmpty()) {
                nativeAdViewGroup = nativeAdContainer;
                if (nativeAd != null) {
                    TypeWiseNativeAd(nativeAd, mNativeType);
                    nativeAd = null;
                } else {
                    nativeType = mNativeType;
                    LoadFacebookNativeBigAds();
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

    private void TypeWiseNativeAd(NativeAd nativeAd, AdUtils.NativeType madType) {
        nativeType = madType;
        switch (madType) {
            case NATIVE_BIG:
                FacebookNativeBigAdLayoutBinding facebookNativeBigAdLayoutBinding = FacebookNativeBigAdLayoutBinding.inflate(LayoutInflater.from(activity));
                displayBigNativeAd(nativeAd, facebookNativeBigAdLayoutBinding);
                break;
            case NATIVE_BIG_TOP:
                FacebookNativeBigAdBtnTopLayoutBinding facebookNativeBigAdBtnTopLayoutBinding = FacebookNativeBigAdBtnTopLayoutBinding.inflate(LayoutInflater.from(activity));
                displayBigNativeButtonTopAd(nativeAd, facebookNativeBigAdBtnTopLayoutBinding);
                break;
        }
    }

    private void displayBigNativeAd(NativeAd loadedNativeAd, FacebookNativeBigAdLayoutBinding adView) {
        adLoaded = false;
        showedNativeAd++;
        if (!loadedNativeAd.isAdLoaded()) {
            NativeAdCallBack(false);
        } else {
            firebaseEvent("FbNativeBigAd Display " + CurrentNativeAdPosition);
            LogUtils.logE(TAG, "FaceMetaNativeBigAd Display  --------------- " + CurrentNativeAdPosition);
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
                LoadFacebookNativeBigAds();
            }
            NativeAdCallBack(true);
        }

    }

    private void displayBigNativeButtonTopAd(NativeAd loadedNativeAd, FacebookNativeBigAdBtnTopLayoutBinding adView) {
        showedNativeAd++;
        if (!loadedNativeAd.isAdLoaded()) {
            NativeAdCallBack(false);
        } else {
            LogUtils.logE(TAG, "FaceMetaNativeBigAd Display --------------- " + CurrentNativeAdPosition);
            firebaseEvent("FbNativeBigAd Display " + CurrentNativeAdPosition);
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
                LoadFacebookNativeBigAds();
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
