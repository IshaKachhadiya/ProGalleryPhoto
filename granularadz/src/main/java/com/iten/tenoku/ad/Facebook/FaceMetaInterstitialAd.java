package com.iten.tenoku.ad.Facebook;


import static com.iten.tenoku.activity.AppSettingActivity.AD_TYPE;
import static com.iten.tenoku.utils.AdUtils.firebaseEvent;
import static com.iten.tenoku.utils.MyApplication.sharedPreferencesHelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.iten.tenoku.ad.HandleClick.HandleInterstitialAd;
import com.iten.tenoku.model.FacebookInterstitialAdModel;
import com.iten.tenoku.utils.AdConstant;
import com.iten.tenoku.utils.AdUtils;
import com.iten.tenoku.utils.LogUtils;
import com.iten.tenoku.utils.MyApplication;

import java.util.ArrayList;


public class FaceMetaInterstitialAd {

    public static ArrayList<FacebookInterstitialAdModel> facebookInterstitialAdModels = new ArrayList<>();
    public static int currentAd = -1;
    @SuppressLint("StaticFieldLeak")
    private static Activity activity;
    @SuppressLint("StaticFieldLeak")
    private static FaceMetaInterstitialAd mInstance;
    String TAG = getClass().getSimpleName();
    Boolean adLoaded = false;

    boolean isNeedToLoad = true;
    HandleInterstitialAd handleInterstitialAd = null;


    public FaceMetaInterstitialAd(Activity activity) {
        FaceMetaInterstitialAd.activity = activity;
    }

    public static FaceMetaInterstitialAd getInstance(Activity activity) {
        FaceMetaInterstitialAd.activity = activity;
        if (mInstance == null) {
            mInstance = new FaceMetaInterstitialAd(activity);
        }
        return mInstance;
    }

    public void showInterstitialAd(HandleInterstitialAd handleInterstitialAd) {
        this.handleInterstitialAd = handleInterstitialAd;
        if (!sharedPreferencesHelper.getPreload()) {
            LoadInterstitialAd();
        } else {
            if (MyApplication.sharedPreferencesHelper.getFacebookAdShow()) {
                if (!facebookInterstitialAdModels.isEmpty()) {
                    for (int i = 0; i < facebookInterstitialAdModels.size(); i++) {
                        if (facebookInterstitialAdModels.get(i).getInterstitialAd() != null) {
                            ShowAd(facebookInterstitialAdModels.get(i).getInterstitialAd());
                            return;
                        }
                    }
                    HandleInterstitialAd(false);
                } else {
                    HandleInterstitialAd(false);
                }
            } else {
                HandleInterstitialAd(false);
            }
        }
    }

    private void CheckNativeAd(InterstitialAd interstitialAd) {
        if (adLoaded) {
            if (handleInterstitialAd != null) {
                ShowAd(interstitialAd);
            }
        }
    }

    private void ShowAd(InterstitialAd interstitialAd) {
        if (interstitialAd != null && interstitialAd.isAdLoaded()) {
            interstitialAd.show();
        } else {
            if (facebookInterstitialAdModels.get(currentAd).getInterstitialAd() != null && facebookInterstitialAdModels.get(currentAd).getInterstitialAd().isAdLoaded()) {
                facebookInterstitialAdModels.get(currentAd).getInterstitialAd().show();
            } else {
                HandleInterstitialAd(false);
            }
        }
    }

    void HandleInterstitialAd(Boolean adShowed) {
        try {
            if (sharedPreferencesHelper.getAdShowingPopup()) {
                AdUtils.dismissAdLoading();
            } else {
                AdUtils.dismissAdProgress();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (handleInterstitialAd != null) {
            handleInterstitialAd.Show(adShowed);
            handleInterstitialAd = null;
        }

    }

    public void LoadInterstitialAd() {
        if (isNeedToLoad) {
            if (!facebookInterstitialAdModels.isEmpty()) {
                if (currentAd == facebookInterstitialAdModels.size() - 1)
                    currentAd = 0;
                else currentAd++;

                InterstitialAd interstitialAd = new InterstitialAd(activity, facebookInterstitialAdModels.get(currentAd).getAdUnit());
                LogUtils.logD(TAG, "FaceMetaInterstitialAd adRequest ===> " + currentAd);
                firebaseEvent("FbInterstitialAd adRequest " + currentAd);
                interstitialAd.loadAd(interstitialAd.buildLoadAdConfig().withAdListener(new InterstitialAdListener() {
                    @Override
                    public void onAdLoaded(Ad ad) {
                        FacebookInterstitialAdModel facebookInterstitialAdModel = new FacebookInterstitialAdModel(facebookInterstitialAdModels.get(currentAd).getAdUnit(), interstitialAd);
                        facebookInterstitialAdModels.set(currentAd, facebookInterstitialAdModel);
                        isNeedToLoad = true;
                        LogUtils.logE(TAG, "FaceMetaInterstitialAd onAdLoaded ===> " + currentAd + " ID ==> " + ad.getPlacementId());
                        firebaseEvent("FbInterstitialAd onAdLoaded " + currentAd);
                        MyApplication.sharedPreferencesHelper.setTotalFailedCount(AdConstant.ResetTotalCount);
                        MyApplication.sharedPreferencesHelper.setAdmobFailedCount(AdConstant.ResetTotalCount);
                        MyApplication.sharedPreferencesHelper.setFbFailedCount(AdConstant.ResetTotalCount);
                        if (!sharedPreferencesHelper.getPreload()) {
                            ShowAd(interstitialAd);
                        } else {
                            CheckNativeAd(interstitialAd);
                        }
                    }

                    @Override
                    public void onError(Ad ad, AdError adError) {
                        isNeedToLoad = true;
                        LogUtils.logE(TAG, "FaceMetaInterstitialAd onAdFailedToLoad ===> " + currentAd + " Error ==> " + adError.getErrorMessage());
                        firebaseEvent("FbInterstitialAd onAdFailed " + currentAd + "_Cd_" + adError.getErrorCode());

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
                        HandleInterstitialAd(false);
                    }

                    @Override
                    public void onInterstitialDisplayed(Ad ad) {
                        LogUtils.logE(TAG, "FaceMetaInterstitialAd Displayed ===> " + currentAd + " ID ==> " + ad.getPlacementId());
                        firebaseEvent("FbInterstitialAd Display " + currentAd);

                    }

                    @Override
                    public void onInterstitialDismissed(Ad ad) {
                        LogUtils.logE(TAG, "FaceMetaInterstitialAd Dismissed ===> " + currentAd + " ID ==> " + ad.getPlacementId());
                        firebaseEvent("FbInterstitialAd Dismiss " + currentAd);

                        if (sharedPreferencesHelper.getOnAdsResume()) {
                            AdConstant.isResume = false;
                        } else {
                            AdConstant.isResume = true;
                        }
                        if (sharedPreferencesHelper.getPreload()) {
                            LoadInterstitialAd();
                        }
                        HandleInterstitialAd(true);
                        adLoaded = false;
                    }

                    @Override
                    public void onAdClicked(Ad ad) {

                    }

                    @Override
                    public void onLoggingImpression(Ad ad) {

                    }
                }).build());
                isNeedToLoad = false;
            } else {
//                HandleInterstitialAd(false);
            }
        }
    }
}