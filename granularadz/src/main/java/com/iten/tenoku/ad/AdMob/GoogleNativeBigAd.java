package com.iten.tenoku.ad.AdMob;

import static com.google.android.gms.ads.nativead.NativeAdOptions.ADCHOICES_TOP_RIGHT;
import static com.iten.tenoku.activity.AppSettingActivity.AD_TYPE;
import static com.iten.tenoku.utils.AdConstant.googleNativeAdModels;
import static com.iten.tenoku.utils.AdUtils.firebaseEvent;
import static com.iten.tenoku.utils.AdUtils.getOpacityColor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MediaAspectRatio;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.iten.tenoku.R;
import com.iten.tenoku.listeners.AdsNativeAdListener;
import com.iten.tenoku.utils.AdConstant;
import com.iten.tenoku.utils.AdUtils;
import com.iten.tenoku.utils.LogUtils;
import com.iten.tenoku.utils.MyApplication;

public class GoogleNativeBigAd {
    public static boolean isNeedToLoadBig = true;
    @SuppressLint("StaticFieldLeak")
    private static GoogleNativeBigAd mInstance;
    @SuppressLint("StaticFieldLeak")
    private static Activity activity;
    String TAG = getClass().getSimpleName();
    AdUtils.NativeType nativeType;
    Boolean adLoaded = false;
    NativeAd nativeAd = null;
    int CurrentNativeAdPosition = -1;
    AdsNativeAdListener nativeAdListener = null;
    ViewGroup nativeAdViewGroup;
    AdLoader adLoader;
    int showedNativeAd = 0;

    public GoogleNativeBigAd(Activity activity) {
        GoogleNativeBigAd.activity = activity;
    }

    public static GoogleNativeBigAd getInstance(Activity adShowingActivity) {
        GoogleNativeBigAd.activity = adShowingActivity;
        if (mInstance == null) {
            mInstance = new GoogleNativeBigAd(adShowingActivity);
        }
        return mInstance;
    }

    public void LoadNativeBigAds() {
        if (isNeedToLoadBig) {
            if (MyApplication.sharedPreferencesHelper.getAdMobAdShow()) {
                if (nativeAd == null) {
                    if (!googleNativeAdModels.isEmpty()) {
                        if (CurrentNativeAdPosition == googleNativeAdModels.size() - 1) {
                            CurrentNativeAdPosition = 0;
                        } else CurrentNativeAdPosition++;
                        adLoader = new AdLoader.Builder(activity, googleNativeAdModels.get(CurrentNativeAdPosition).getAdUnit()).forNativeAd(nativeAd -> {
                            LogUtils.logE(TAG, "GoogleNativeBigAd onAdLoaded ===> " + CurrentNativeAdPosition + " ID ==> " + googleNativeAdModels.get(CurrentNativeAdPosition).getAdUnit());
                            firebaseEvent("GoogleNativeBigAd onAdLoaded " + CurrentNativeAdPosition);
                            isNeedToLoadBig = true;
                            if (this.nativeAd == null) {
                                this.nativeAd = nativeAd;
                            }
                            CheckNativeAd();
                            LogUtils.logE(TAG, "onAdFailedToLoad: ----> " + MyApplication.sharedPreferencesHelper.getTotalFailedCount());
                            MyApplication.sharedPreferencesHelper.setTotalFailedCount(AdConstant.ResetTotalCount);
                            MyApplication.sharedPreferencesHelper.setAdmobFailedCount(AdConstant.ResetTotalCount);
                            MyApplication.sharedPreferencesHelper.setFbFailedCount(AdConstant.ResetTotalCount);

                        }).withAdListener(new AdListener() {
                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                                LogUtils.logE(TAG, "GoogleNativeBigAd onAdFailedToLoad ===> " + CurrentNativeAdPosition + " Error Code ==> " + adError.getCode());
                                LogUtils.logE(TAG, "GoogleNativeBigAd onAdFailedToLoad ===> " + CurrentNativeAdPosition + " Error ==> " + adError.getResponseInfo());
                                firebaseEvent("GoogleNativeBigAd onAdFailed " + CurrentNativeAdPosition + "_Cd_" + adError.getCode());
                                isNeedToLoadBig = true;
                                if (MyApplication.sharedPreferencesHelper.getTotalFailedCount() != MyApplication.sharedPreferencesHelper.getFailedCount()) {
                                    MyApplication.sharedPreferencesHelper.setTotalFailedCount(MyApplication.sharedPreferencesHelper.getTotalFailedCount() + 1);
                                    MyApplication.sharedPreferencesHelper.setAdmobFailedCount(MyApplication.sharedPreferencesHelper.getAdmobFailedCount() + 1);
                                    LogUtils.logE(TAG, "onAdFailedToLoad: ----> " + MyApplication.sharedPreferencesHelper.getTotalFailedCount());
                                    LogUtils.logE(TAG, "AdMob Count: ----> " + MyApplication.sharedPreferencesHelper.getAdmobFailedCount());
                                } else {
                                    if (!MyApplication.sharedPreferencesHelper.getRequestDataSend()) {
                                        AdUtils.getInstance(activity).sendRequestData();
                                        MyApplication.sharedPreferencesHelper.setRequestDataSend(true);
                                    }

                                    MyApplication.sharedPreferencesHelper.setOwnAdShow(true);
                                    AD_TYPE= AdUtils.AdType.OWNADS;
                                }

                                NativeAdCallBack(false);
                            }
                        }).withNativeAdOptions(new NativeAdOptions.Builder().setRequestMultipleImages(true).setAdChoicesPlacement(ADCHOICES_TOP_RIGHT).setMediaAspectRatio(MediaAspectRatio.LANDSCAPE).build()).build();
                        isNeedToLoadBig = false;
                        adLoader.loadAd(new AdRequest.Builder().build());
                        LogUtils.logD(TAG, "GoogleNativeBigAd adRequest ===> " + CurrentNativeAdPosition);
                        firebaseEvent("GoogleNativeBigAd adRequest " + CurrentNativeAdPosition);
                    }
                }
            } else {
//                NativeAdCallBack(false);
            }
        } else {
            LogUtils.logE(TAG, "LoadNativeBigAds Not Loaded");
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
        if (MyApplication.sharedPreferencesHelper.getAdMobAdShow()) {
            if (!googleNativeAdModels.isEmpty()) {
                nativeAdViewGroup = nativeAdContainer;
                if (nativeAd != null) {
                    TypeWiseNativeAd(nativeAd, mNativeType);
                    nativeAd = null;
                } else {
                    nativeType = mNativeType;
                    LoadNativeBigAds();
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
        adLoaded = false;
        nativeType = madType;
        nativeAdViewGroup.setVisibility(View.VISIBLE);
        nativeAdViewGroup.removeAllViews();
        NativeAdView adView = null;
        switch (madType) {
            case NATIVE_BIG:
                adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.admob_native_big_ad_layout, nativeAdViewGroup, false);
                break;
            case NATIVE_BIG_TOP:
                adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.admob_native_big_ad_btn_top_layout, nativeAdViewGroup, false);
                break;
        }
        if (adView != null) {
            displayTypeWiseBigNativeAd(nativeAd, adView);
            nativeAdViewGroup.addView(adView);
        }
    }

    private void displayTypeWiseBigNativeAd(NativeAd loadedNativeAd, NativeAdView adView) {
        LogUtils.logE(TAG, "GoogleNativeBigAd Display --------------- " + CurrentNativeAdPosition);
        firebaseEvent("GoogleNativeBigAd Display " + CurrentNativeAdPosition);
        showedNativeAd++;

        adView.setIconView(adView.findViewById(R.id.image_ad_icon));
        adView.setHeadlineView(adView.findViewById(R.id.text_ad_title));
        adView.setBodyView(adView.findViewById(R.id.text_ad_body));
        adView.setMediaView(adView.findViewById(R.id.native_ad_MediaView));
        adView.setCallToActionView(adView.findViewById(R.id.button_call_to_action));

        if (loadedNativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            adView.getIconView().setVisibility(View.VISIBLE);
            ((ImageView) adView.getIconView()).setImageDrawable(loadedNativeAd.getIcon().getDrawable());
        }
        if (loadedNativeAd.getHeadline() == null) {
            adView.getHeadlineView().setVisibility(View.GONE);
        } else {
            adView.getHeadlineView().setVisibility(View.VISIBLE);
            ((TextView) adView.getHeadlineView()).setText(loadedNativeAd.getHeadline());
        }
        if (loadedNativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.GONE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(loadedNativeAd.getBody());
        }
        if (loadedNativeAd.getMediaContent() == null) {
            adView.getMediaView().setVisibility(View.GONE);
        } else {
            adView.getMediaView().setVisibility(View.VISIBLE);
            adView.getMediaView().setMediaContent(loadedNativeAd.getMediaContent());
            adView.getMediaView().setImageScaleType(ImageView.ScaleType.FIT_CENTER);
        }
        if (loadedNativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.GONE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((AppCompatButton) adView.getCallToActionView()).setText(loadedNativeAd.getCallToAction());
        }

        adView.setNativeAd(loadedNativeAd);
        VideoController videoController = loadedNativeAd.getMediaContent().getVideoController();
        if (videoController.hasVideoContent()) {
            videoController.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                public void onVideoEnd() {
                    super.onVideoEnd();
                }
            });
        }
        if (!MyApplication.sharedPreferencesHelper.getAdTitleColor().isEmpty()) {
            ((TextView) adView.getHeadlineView()).setTextColor(Color.parseColor(MyApplication.sharedPreferencesHelper.getAdTitleColor()));
        }
        if (!MyApplication.sharedPreferencesHelper.getAdDescriptionColor().isEmpty()) {
            ((TextView) adView.getBodyView()).setTextColor(Color.parseColor(MyApplication.sharedPreferencesHelper.getAdDescriptionColor()));
        }
        if (!MyApplication.sharedPreferencesHelper.getAdButtonTextColor().isEmpty()) {
            ((AppCompatButton) adView.getCallToActionView()).setTextColor(Color.parseColor(MyApplication.sharedPreferencesHelper.getAdButtonTextColor()));
            ((TextView) adView.findViewById(R.id.text_title)).setTextColor(Color.parseColor(MyApplication.sharedPreferencesHelper.getAdButtonTextColor()));
        }
        if (!MyApplication.sharedPreferencesHelper.getAdButtonColor().isEmpty()) {
            getOpacityColor(adView.getCallToActionView().getBackground(), MyApplication.sharedPreferencesHelper.getAdButtonColorOpacity(), false);
            getOpacityColor(adView.findViewById(R.id.text_title).getBackground(), MyApplication.sharedPreferencesHelper.getAdButtonColorOpacity(), false);
        }
        if (!MyApplication.sharedPreferencesHelper.getAddBgColor().isEmpty()) {
            getOpacityColor((adView.findViewById(R.id.bigNative)).getBackground(), MyApplication.sharedPreferencesHelper.getAdBgColorOpacity(), true);
        }
        if (MyApplication.sharedPreferencesHelper.getPreload()) {
            nativeAd = null;
            LoadNativeBigAds();
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
