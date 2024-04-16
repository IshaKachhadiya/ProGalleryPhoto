package com.iten.tenoku.ad.AdMob;


import static com.iten.tenoku.utils.AdUtils.firebaseEvent;
import static com.iten.tenoku.utils.MyApplication.sharedPreferencesHelper;
import static com.iten.tenoku.activity.AppSettingActivity.AD_TYPE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.iten.tenoku.activity.AppSettingActivity;
import com.iten.tenoku.ad.HandleClick.HandleInterstitialAd;
import com.iten.tenoku.model.GoogleInterstitialAdModel;
import com.iten.tenoku.utils.AdConstant;
import com.iten.tenoku.utils.AdUtils;
import com.iten.tenoku.utils.LogUtils;
import com.iten.tenoku.utils.MyApplication;

import java.util.ArrayList;

public class GoogleInterstitialAd {

    public static ArrayList<GoogleInterstitialAdModel> adMobInterstitialAds = new ArrayList<>();
    public static int CurrentInterstitialAdPosition = -1;
    @SuppressLint("StaticFieldLeak")
    private static Activity activity;
    @SuppressLint("StaticFieldLeak")
    private static GoogleInterstitialAd mInstance;
    String TAG = getClass().getSimpleName();
    Boolean adLoaded = false;

    boolean isNeedToLoad = true;
    HandleInterstitialAd handleInterstitialAd = null;


    public GoogleInterstitialAd(Activity activity) {
        GoogleInterstitialAd.activity = activity;
    }

    public static GoogleInterstitialAd getInstance(Activity adShowingActivity) {
        GoogleInterstitialAd.activity = adShowingActivity;
        if (mInstance == null) {
            mInstance = new GoogleInterstitialAd(adShowingActivity);
        }
        return mInstance;
    }

    public void ShowInterstitialAd(HandleInterstitialAd handleInterstitialAd) {
        this.handleInterstitialAd = handleInterstitialAd;
        if (!sharedPreferencesHelper.getPreload()) {
            LoadInterstitialAd();
            adLoaded = true;
        } else {
            if (!adMobInterstitialAds.isEmpty()) {
                for (int i = 0; i < adMobInterstitialAds.size(); i++) {
                    if (adMobInterstitialAds.get(i).getInterstitialAd() != null) {
                        ShowAd(adMobInterstitialAds.get(i).getInterstitialAd(), i);
                        return;
                    }
                }
                HandleInterstitialAd(false);
            } else {
                HandleInterstitialAd(false);
            }
        }
    }

    private void CheckNativeAd(InterstitialAd interstitialAd, int currentInterstitialAdPosition) {
        if (adLoaded) {
            if (handleInterstitialAd != null) {
                ShowAd(interstitialAd, currentInterstitialAdPosition);
            }
        }
    }

    private void ShowAd(InterstitialAd interstitialAd, int i) {
        if (interstitialAd != null) {
            interstitialAd.show(activity);
            interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    GoogleInterstitialAdModel googleInterstitialAdModel = new GoogleInterstitialAdModel(adMobInterstitialAds.get(i).getAdUnit(), null);
                    adMobInterstitialAds.set(i, googleInterstitialAdModel);
                    HandleInterstitialAd(false);
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    LogUtils.logE(TAG, "GoogleInterstitialAd Displayed ===> " + i + " ID ==> " + adMobInterstitialAds.get(i).getAdUnit());
                    firebaseEvent("GoogleInterstitialAd Display " + i);
                    GoogleInterstitialAdModel googleInterstitialAdModel = new GoogleInterstitialAdModel(adMobInterstitialAds.get(i).getAdUnit(), null);
                    adMobInterstitialAds.set(i, googleInterstitialAdModel);

                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    LogUtils.logE(TAG, "GoogleInterstitialAd Dismissed ===> " + i + " ID ==> " + adMobInterstitialAds.get(i).getAdUnit());
                    firebaseEvent("GoogleInterstitialAd Dismiss " + i);
                    if (sharedPreferencesHelper.getOnAdsResume()) {
                        AdConstant.isResume = false;
                    } else {
                        AdConstant.isResume = true;
                    }
                    adLoaded = false;
                    if (sharedPreferencesHelper.getPreload()) {
                        LoadInterstitialAd();
                    }
                    HandleInterstitialAd(true);
                }

                @Override
                public void onAdImpression() {
                    super.onAdImpression();
                    firebaseEvent("GoogleInterstitialAd onAdImpression " + i);
                    LogUtils.logE(TAG, "GoogleInterstitialAd onAdImpression ===> " + i + " ID ==> " + adMobInterstitialAds.get(i).getAdUnit());
                }
            });
        } else {
            HandleInterstitialAd(false);
        }
    }

    void HandleInterstitialAd(Boolean AdShowed) {
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
            handleInterstitialAd.Show(AdShowed);
            handleInterstitialAd = null;
        }
    }

    public void LoadInterstitialAd() {
        if (isNeedToLoad) {
            if (!adMobInterstitialAds.isEmpty()) {
                if (CurrentInterstitialAdPosition == adMobInterstitialAds.size() - 1)
                    CurrentInterstitialAdPosition = 0;
                else CurrentInterstitialAdPosition++;

                if (adMobInterstitialAds.get(CurrentInterstitialAdPosition).getInterstitialAd() == null) {
                    AdRequest adRequest = new AdRequest.Builder().build();
                    LogUtils.logD(TAG, "GoogleInterstitialAd adRequest ===> " + CurrentInterstitialAdPosition);
                    firebaseEvent("GoogleInterstitialAd adRequest " + CurrentInterstitialAdPosition);
                    InterstitialAd.load(activity, adMobInterstitialAds.get(CurrentInterstitialAdPosition).getAdUnit(), adRequest, new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            GoogleInterstitialAdModel googleInterstitialAdModel = new GoogleInterstitialAdModel(adMobInterstitialAds.get(CurrentInterstitialAdPosition).getAdUnit(), interstitialAd);
                            adMobInterstitialAds.set(CurrentInterstitialAdPosition, googleInterstitialAdModel);
                            isNeedToLoad = true;

                            LogUtils.logE(TAG, "GoogleInterstitialAd onAdLoaded ===> " + CurrentInterstitialAdPosition + " ID ==> " + interstitialAd.getAdUnitId());
                            firebaseEvent("GoogleInterstitialAd OnAdLoaded " + CurrentInterstitialAdPosition);

                            MyApplication.sharedPreferencesHelper.setTotalFailedCount(AdConstant.ResetTotalCount);
                            MyApplication.sharedPreferencesHelper.setAdmobFailedCount(AdConstant.ResetTotalCount);
                            MyApplication.sharedPreferencesHelper.setFbFailedCount(AdConstant.ResetTotalCount);
                            if (!sharedPreferencesHelper.getPreload()) {
                                ShowAd(interstitialAd, CurrentInterstitialAdPosition);
                            } else {
                                CheckNativeAd(interstitialAd, CurrentInterstitialAdPosition);
                            }

                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            isNeedToLoad = true;
                            LogUtils.logE(TAG, "GoogleInterstitialAd onAdFailedToLoad ===> " + CurrentInterstitialAdPosition + " Error ==> " + loadAdError.getResponseInfo());
                            firebaseEvent("GoogleInterstitialAd onAdFailed " + CurrentInterstitialAdPosition + "_Cd_" + loadAdError.getCode());
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
                            HandleInterstitialAd(false);

                        }

                    });
                    isNeedToLoad = false;
                }
            } else {
//                HandleInterstitialAd(false);
            }
        }
    }
}
