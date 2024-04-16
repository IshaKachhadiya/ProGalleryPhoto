package com.iten.tenoku.ad;

import static androidx.lifecycle.Lifecycle.Event.ON_START;
import static androidx.lifecycle.Lifecycle.Event.ON_STOP;
import static com.iten.tenoku.activity.AppSettingActivity.AD_TYPE;
import static com.iten.tenoku.ad.Facebook.FaceMetaInterstitialAd.facebookInterstitialAdModels;
import static com.iten.tenoku.utils.AdConstant.festumAppOpenAdModels;
import static com.iten.tenoku.utils.AdConstant.isInterstitialShown;
import static com.iten.tenoku.utils.AdConstant.isResume;
import static com.iten.tenoku.utils.AdUtils.firebaseEvent;
import static com.iten.tenoku.utils.MyApplication.sharedPreferencesHelper;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.bumptech.glide.Glide;
import com.facebook.ads.Ad;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.iten.tenoku.BuildConfig;
import com.iten.tenoku.R;
import com.iten.tenoku.ad.Facebook.FaceMetaInterstitialAd;
import com.iten.tenoku.ad.HandleClick.HandleOpenAd;
import com.iten.tenoku.ad.Qureka.QuerkaNative;
import com.iten.tenoku.databinding.DialogDeveloperBinding;
import com.iten.tenoku.databinding.FestumOpenAdBinding;
import com.iten.tenoku.databinding.QurekaOpenAdBinding;
import com.iten.tenoku.model.GoogleOpenAdModel;
import com.iten.tenoku.utils.AdConstant;
import com.iten.tenoku.utils.AdUtils;
import com.iten.tenoku.utils.LogUtils;
import com.iten.tenoku.utils.MyApplication;

import java.util.ArrayList;
import java.util.Date;

public class AppOpenManager implements Application.ActivityLifecycleCallbacks, LifecycleObserver {
    public int OwnAppOpenCounter = 0;

    public static ArrayList<GoogleOpenAdModel> adMobOpenAdModels = new ArrayList<>();
    public static boolean Splash_Start = false;
    public static int CurrentOpenAdPosition = 0;
    public static boolean needToShowOpenAd = false;
    private static String TAG = "AppOpenManager";
    private static boolean isShowingAd = false;
    private static Dialog dialog;
    private static Dialog festumDialog;
    private static HandleOpenAd handleOpenAd = null;
    private static Activity currentActivity;
    private static int mPosition = -1;
    private static AppOpenManager mInstance;
    boolean isMyAdLoaded = false;
    boolean isNeedToLoad = true;
    private int activityStarted = 0;
    private InterstitialAd interstitialAd;

    public AppOpenManager(MyApplication myApplication) {
        myApplication.registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    public AppOpenManager(Activity activity) {
        AppOpenManager.currentActivity = activity;
    }

    public static AppOpenManager getInstance(Activity adShowingActivity) {
        if (mInstance == null) {
            mInstance = new AppOpenManager(adShowingActivity);
        }
        return mInstance;
    }

    private static AdRequest getAdRequest() {
        return new AdRequest.Builder().build();
    }

    private static boolean wasLoadTimeLessThanNHoursAgo() {
        long dateDifference = (new Date()).getTime() - adMobOpenAdModels.get(CurrentOpenAdPosition).getTime();
        long numMilliSecondsPerHour = 3600000;
        return (dateDifference < (numMilliSecondsPerHour));
    }

    public boolean isAdAvailable() {
        return adMobOpenAdModels.get(CurrentOpenAdPosition).getAppOpenAd() != null && wasLoadTimeLessThanNHoursAgo();
    }

    private void HandleOpenAd(Boolean AdShowed) {
        if (handleOpenAd != null) {
            handleOpenAd.Show(AdShowed);
            handleOpenAd = null;
        }
    }

    private void QurekaOpenAd() {
        try {
            if (dialog != null && dialog.isShowing()) {
                DismissDialog();
                dialog = null;
            }
            if (dialog == null) {
                dialog = new Dialog(currentActivity);
                QurekaOpenAdBinding layoutQurekaOpenAdBinding = QurekaOpenAdBinding.inflate(LayoutInflater.from(currentActivity));
                dialog.setContentView(layoutQurekaOpenAdBinding.getRoot());
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Glide.with(currentActivity).load(AdConstant.getInterstitialRandomImage()).error(R.drawable.ic_qureka_interstital).into(layoutQurekaOpenAdBinding.imageViewAd);
                layoutQurekaOpenAdBinding.imageViewAd.setOnClickListener(view -> {
                    DismissDialog();
                    HandleOpenAd(true);
                    QuerkaNative.getInstance(currentActivity).ShowQuerkaAd();
                });
                layoutQurekaOpenAdBinding.layoutContinueApp.setOnClickListener(view -> {
                    isResume = true;
                    if (sharedPreferencesHelper.getQurekaClose()) {
                        DismissDialog();
                        HandleOpenAd(true);
                    } else {
                        DismissDialog();
                        HandleOpenAd(true);
                        QuerkaNative.getInstance(currentActivity).ShowQuerkaAd();
                    }
                });

                if (!currentActivity.isFinishing() && !dialog.isShowing()) {
                    dialog.show();
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void FestumOpenAd() {
        try {
            if (festumDialog != null && festumDialog.isShowing()) {
                DismissFestumOpenAd();
                festumDialog = null;
            }
            if (festumDialog == null) {
                festumDialog = new Dialog(currentActivity);
                FestumOpenAdBinding layoutQurekaOpenAdBinding = FestumOpenAdBinding.inflate(LayoutInflater.from(currentActivity));
                festumDialog.setContentView(layoutQurekaOpenAdBinding.getRoot());
                festumDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                festumDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                festumDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
                festumDialog.setCancelable(false);
                festumDialog.setCanceledOnTouchOutside(false);
                Glide.with(currentActivity).load(festumAppOpenAdModels.get(sharedPreferencesHelper.getOwnAppOpenPosition()).getvNativeBannerUrl()).error(R.drawable.ic_qureka_interstital).into(layoutQurekaOpenAdBinding.imageViewFestumad);
                layoutQurekaOpenAdBinding.imageViewFestumad.setOnClickListener(view -> {
                    DismissFestumOpenAd();
                    HandleOpenAd(true);
                    QuerkaNative.getInstance(currentActivity).ShowQuerkaAd();
                });
                layoutQurekaOpenAdBinding.layoutContinueApp.setOnClickListener(view -> {
                    isResume = true;
                    if (sharedPreferencesHelper.getQurekaClose()) {
                        DismissFestumOpenAd();
                        HandleOpenAd(true);
                    } else {
                        DismissFestumOpenAd();
                        HandleOpenAd(true);
                        QuerkaNative.getInstance(currentActivity).ShowQuerkaAd();
                    }
                });

                if (!currentActivity.isFinishing() && !festumDialog.isShowing()) {
                    festumDialog.show();
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void DismissDialog() {
        isResume = true;
        try {
            if (dialog != null && dialog.isShowing()) dialog.dismiss();
            dialog = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void DismissFestumOpenAd() {
        isResume = true;
        try {
            if (festumDialog != null && festumDialog.isShowing()) festumDialog.dismiss();
            festumDialog = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ShowOpenAd(boolean isFromSplash, HandleOpenAd handleOpenAdActivity) {
        this.handleOpenAd = handleOpenAdActivity;
        LogUtils.logD(TAG, "isInterstitialShown => " + isInterstitialShown);
        if (!isInterstitialShown) {
            if (sharedPreferencesHelper.getShowAdinApp()) {
                if (sharedPreferencesHelper.getAppOpenAd()) {
                    if (!sharedPreferencesHelper.getShowAdinApp()) {
                        HandleOpenAd(false);
                    }
                    LogUtils.logD(TAG, "PerformNext => isFromSplash " + isFromSplash);
                    if (isFromSplash) {
                        switch (AD_TYPE) {
                            case ADMOB:
                            case ADMOB_FACEBOOK:
//                                showAdmobAdFromSplash();
                                showAdmobAd();
                                break;
                            case FACEBOOK:
                            case FACEBOOK_ADMOB:
                                LogUtils.logD(TAG, "FacebookInterstitialAd activity ==> " + currentActivity);
                                FaceMetaInterstitialAd.getInstance(currentActivity).showInterstitialAd(adShow -> {
                                    if (adShow) {
                                        isResume = true;
                                        LogUtils.logW(TAG, "Facebook OpenAd Dismissed ===> ");
                                        HandleOpenAd(false);
                                    } else {
                                        isResume = true;
                                        if (sharedPreferencesHelper.getQurekaLite()) {
                                            QurekaOpenAd();
                                        } else {
                                            HandleOpenAd(false);
                                        }
                                    }
                                });
//                                showFbAdOnly();
                                break;

                            case OWNADS:
                            case OWNADS_QUREKA:
                                LogUtils.logE(TAG, "OWNADS_AUREKA OPEN AD === >  is FROM SPLASH---> " + isFromSplash);
                                FestumOpenAd();
                                break;
                        }
                    } else {
                        switch (AD_TYPE) {
                            case FACEBOOK_ADMOB:
                            case ADMOB_FACEBOOK:
                            case ADMOB:
                                showAdmobAd();
                                break;
                            case FACEBOOK:
                                if (sharedPreferencesHelper.getQurekaLite()) {
                                    QurekaOpenAd();
                                }
                                break;
                            case OWNADS:
                                FestumOpenAd();
                                break;
                            case OWNADS_QUREKA:
                                if (OwnAppOpenCounter % 2 == 1) {
                                    FestumOpenAd();
                                } else {
                                    QurekaOpenAd();
                                }
                                LogUtils.logE(TAG, "OWNADS_AUREKA OPEN AD === >  NOT FROM SPLASH---> " + isFromSplash);

                                break;
                        }
                    }

                } else {
                    HandleOpenAd(false);
                }
            } else {
                HandleOpenAd(false);
            }
        } else {
            HandleOpenAd(false);
        }
    }


    public void LoadAd() {
        if (isNeedToLoad) {
            if (!adMobOpenAdModels.isEmpty()) {
                if (CurrentOpenAdPosition == adMobOpenAdModels.size() - 1)
                    CurrentOpenAdPosition = 0;
                else CurrentOpenAdPosition++;

                if (isAdAvailable()) {
                    return;
                }
                AppOpenAd.AppOpenAdLoadCallback loadCallback = new AppOpenAd.AppOpenAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull AppOpenAd ad) {
                        isNeedToLoad = true;
                        LogUtils.logD(TAG, "AdMobOpenAd onAdLoaded ===> " + CurrentOpenAdPosition + " ID ==> " + ad.getAdUnitId());
                        firebaseEvent("AdMobOpenAd OnAdLoaded " + CurrentOpenAdPosition);
                        GoogleOpenAdModel adMobOpenAdModel = new GoogleOpenAdModel(adMobOpenAdModels.get(CurrentOpenAdPosition).getAdUnit(), ad, (new Date()).getTime());
                        adMobOpenAdModels.set(CurrentOpenAdPosition, adMobOpenAdModel);
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        isNeedToLoad = true;
                        LogUtils.logE(TAG, "AdMobOpenAd onAdFailedToLoad ===> " + CurrentOpenAdPosition + " Error ==> " + loadAdError.getResponseInfo());
                        firebaseEvent("AdMobOpenAd onAdFailed " + CurrentOpenAdPosition + "_Cd_" + loadAdError.getCode());
                    }
                };
                AdRequest request = getAdRequest();
                LogUtils.logE(TAG, "AdMobOpenAd adRequest ===> " + CurrentOpenAdPosition);
                firebaseEvent("AdMobOpenAd adRequest " + CurrentOpenAdPosition);
                AppOpenAd.load(currentActivity, adMobOpenAdModels.get(CurrentOpenAdPosition).getAdUnit(), request, AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback);
                isNeedToLoad = false;
            } else {
                isResume = true;
            }
        }
    }

    private void showAdmobAd() {
        if (sharedPreferencesHelper.getAdMobAdShow()) {
            if (!adMobOpenAdModels.isEmpty()) {
                if (!isShowingAd && isAdAvailable()) {
                    try {
                        if (sharedPreferencesHelper.getAdShowingPopup()) {
                            AdUtils.dismissAdLoading();
                        } else {
                            AdUtils.dismissAdProgress();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    FullScreenContentCallback fullScreenContentCallback = new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            LogUtils.logE(TAG, "AdMobOpenAd Dismissed ===> " + CurrentOpenAdPosition + " ID ==> " + adMobOpenAdModels.get(CurrentOpenAdPosition).getAdUnit());
                            firebaseEvent("AdMobOpenAd Dismiss " + CurrentOpenAdPosition);
                            isResume = true;
                            isShowingAd = false;
                            GoogleOpenAdModel adMobOpenAdModel = new GoogleOpenAdModel(adMobOpenAdModels.get(CurrentOpenAdPosition).getAdUnit(), null, adMobOpenAdModels.get(CurrentOpenAdPosition).getTime());
                            adMobOpenAdModels.set(CurrentOpenAdPosition, adMobOpenAdModel);
                            LoadAd();
                            HandleOpenAd(true);

                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                            isResume = true;
                            GoogleOpenAdModel adMobOpenAdModel = new GoogleOpenAdModel(adMobOpenAdModels.get(CurrentOpenAdPosition).getAdUnit(), null, adMobOpenAdModels.get(CurrentOpenAdPosition).getTime());
                            adMobOpenAdModels.set(CurrentOpenAdPosition, adMobOpenAdModel);
                            LoadAd();
                            HandleOpenAd(false);
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            LogUtils.logE(TAG, "AdMobOpenAd Displayed ===> " + CurrentOpenAdPosition + " ID ==> " + adMobOpenAdModels.get(CurrentOpenAdPosition).getAdUnit());
                            firebaseEvent("AdMobOpenAd Display " + CurrentOpenAdPosition);
                            isShowingAd = true;
                        }
                    };
                    adMobOpenAdModels.get(CurrentOpenAdPosition).getAppOpenAd().setFullScreenContentCallback(fullScreenContentCallback);
                    adMobOpenAdModels.get(CurrentOpenAdPosition).getAppOpenAd().show(currentActivity);

                } else {
                    if (!adMobOpenAdModels.isEmpty()) {
                        if (CurrentOpenAdPosition == adMobOpenAdModels.size() - 1)
                            CurrentOpenAdPosition = 0;
                        else CurrentOpenAdPosition++;
                        if (isAdAvailable()) {
                            HandleOpenAd(false);
                            return;
                        }
                        AppOpenAd.AppOpenAdLoadCallback loadCallback = new AppOpenAd.AppOpenAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull AppOpenAd ad) {
                                LogUtils.logE(TAG, "AdMobOpenAd New onAdLoaded  ===> " + CurrentOpenAdPosition + " ID ==> " + ad.getAdUnitId());
                                firebaseEvent("AdMobOpenAd New OnAdLoaded " + CurrentOpenAdPosition);

                                GoogleOpenAdModel adMobOpenAdModel = new GoogleOpenAdModel(adMobOpenAdModels.get(CurrentOpenAdPosition).getAdUnit(), ad, (new Date()).getTime());
                                adMobOpenAdModels.set(CurrentOpenAdPosition, adMobOpenAdModel);
                                if (!isShowingAd && isAdAvailable()) {
                                    try {
                                        if (sharedPreferencesHelper.getAdShowingPopup()) {
                                            AdUtils.dismissAdLoading();
                                        } else {
                                            AdUtils.dismissAdProgress();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    FullScreenContentCallback fullScreenContentCallback = new FullScreenContentCallback() {
                                        @Override
                                        public void onAdDismissedFullScreenContent() {
                                            LogUtils.logE(TAG, "AdMobOpenAd New Dismissed ===> " + CurrentOpenAdPosition + " ID ==> " + adMobOpenAdModels.get(CurrentOpenAdPosition).getAdUnit());
                                            firebaseEvent("AdMobOpenAd New Dismiss " + CurrentOpenAdPosition);
                                            isResume = true;
                                            isShowingAd = false;
                                            GoogleOpenAdModel adMobOpenAdModel = new GoogleOpenAdModel(adMobOpenAdModels.get(CurrentOpenAdPosition).getAdUnit(), null, adMobOpenAdModels.get(CurrentOpenAdPosition).getTime());
                                            adMobOpenAdModels.set(CurrentOpenAdPosition, adMobOpenAdModel);
                                            LoadAd();
                                            HandleOpenAd(true);
                                        }

                                        @Override
                                        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                            isResume = true;
                                            GoogleOpenAdModel adMobOpenAdModel = new GoogleOpenAdModel(adMobOpenAdModels.get(CurrentOpenAdPosition).getAdUnit(), null, adMobOpenAdModels.get(CurrentOpenAdPosition).getTime());
                                            adMobOpenAdModels.set(CurrentOpenAdPosition, adMobOpenAdModel);
                                            LoadAd();
                                            HandleOpenAd(false);
                                        }

                                        @Override
                                        public void onAdShowedFullScreenContent() {
                                            LogUtils.logE(TAG, "AdMobOpenAd New Displayed ===> " + CurrentOpenAdPosition + " ID ==> " + adMobOpenAdModels.get(CurrentOpenAdPosition).getAdUnit());
                                            firebaseEvent("AdMobOpenAd New Display " + CurrentOpenAdPosition);
                                            isShowingAd = true;

                                        }
                                    };

                                    adMobOpenAdModels.get(CurrentOpenAdPosition).getAppOpenAd().setFullScreenContentCallback(fullScreenContentCallback);
                                    adMobOpenAdModels.get(CurrentOpenAdPosition).getAppOpenAd().show(currentActivity);

                                } else {
                                    isResume = true;
                                    LoadAd();
                                    HandleOpenAd(false);

                                }
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                isResume = true;
                                LogUtils.logE(TAG, "AdMobOpenAd New onAdFailedToLoad ===> " + CurrentOpenAdPosition + " Error ==> " + loadAdError.getResponseInfo());
                                firebaseEvent("AdMobOpenAd New onAdFailed " + CurrentOpenAdPosition + "_Cd_" + loadAdError.getCode());
                                if (sharedPreferencesHelper.getQurekaLite()) {
                                    QurekaOpenAd();
                                } else {
                                    HandleOpenAd(false);
                                }

                            }

                        };
                        AdRequest request = getAdRequest();
                        LogUtils.logV(TAG, "AdMobOpenAd New adRequest ===> " + CurrentOpenAdPosition);
                        firebaseEvent("AdMobOpenAd New adRequest " + CurrentOpenAdPosition);
                        AppOpenAd.load(currentActivity, adMobOpenAdModels.get(CurrentOpenAdPosition).getAdUnit(), request, AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback);
                    } else {
                        isResume = true;
                        HandleOpenAd(false);
                    }
                }
            } else if (sharedPreferencesHelper.getQurekaLite()) {
                QurekaOpenAd();
            } else {
                isResume = true;
                HandleOpenAd(false);
            }
        } else if (sharedPreferencesHelper.getQurekaLite()) {
            QurekaOpenAd();
        } else {
            HandleOpenAd(false);
        }

    }

    private void showFbAdOnly() {
        if (sharedPreferencesHelper.getFacebookAdShow()) {
            if (!facebookInterstitialAdModels.isEmpty()) {

                if (mPosition == facebookInterstitialAdModels.size() - 1) {
                    mPosition = 0;
                } else {
                    mPosition++;
                }
                interstitialAd = new InterstitialAd(currentActivity, facebookInterstitialAdModels.get(mPosition).getAdUnit());
                InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
                    @Override
                    public void onInterstitialDisplayed(Ad ad) {

                        LogUtils.logI(TAG, "Facebook OpenAd Displayed ===> " + mPosition + " ID ==> " + ad.getPlacementId());
                        firebaseEvent("FbOpenAd Display " + mPosition);
                    }

                    @Override
                    public void onInterstitialDismissed(Ad ad) {
                        isResume = true;
                        LogUtils.logW(TAG, "Facebook OpenAd Dismissed ===> " + mPosition + " ID ==> " + ad.getPlacementId());
                        firebaseEvent("FbOpenAd Dismiss " + mPosition);
                        HandleOpenAd(false);

                    }

                    @Override
                    public void onError(Ad ad, com.facebook.ads.AdError adError) {
                        LogUtils.logE(TAG, "Facebook OpenAd onAdFailedToLoad ===> " + mPosition + " Error ==> " + adError.getErrorMessage());
                        firebaseEvent("FbOpenAd onAdFailed " + mPosition + "_Cd_" + adError.getErrorCode());
                        isMyAdLoaded = true;
                        isResume = true;
                        if (sharedPreferencesHelper.getQurekaLite()) {
                            QurekaOpenAd();
                        } else {
                            HandleOpenAd(false);
                        }
                    }

                    @Override
                    public void onAdLoaded(Ad ad) {
                        isMyAdLoaded = true;
                        LogUtils.logD(TAG, "Facebook OpenAd onAdLoaded ===> " + mPosition + " ID ==> " + ad.getPlacementId());
                        firebaseEvent("FbOpenAd onAdLoaded " + mPosition);

                        if (interstitialAd.isAdLoaded() && interstitialAd != null) {
                            if (!needToShowOpenAd) {
                                interstitialAd.show();
                            }
                        }
                    }

                    @Override
                    public void onAdClicked(Ad ad) {

                    }

                    @Override
                    public void onLoggingImpression(Ad ad) {

                    }
                };

                interstitialAd.loadAd(interstitialAd.buildLoadAdConfig().withAdListener(interstitialAdListener).build());
                LogUtils.logV(TAG, "Facebook OpenAd adRequest ===> " + CurrentOpenAdPosition);
                firebaseEvent("FbOpenAd adRequest " + CurrentOpenAdPosition);
                new Handler().postDelayed(() -> {
                    needToShowOpenAd = true;
                    if (!isMyAdLoaded) {
                        if (sharedPreferencesHelper.getQurekaLite()) {
                            QurekaOpenAd();
                        } else {
                            HandleOpenAd(false);
                        }
                    }
                }, 5000);
            } else {
                HandleOpenAd(false);
            }
        } else {
            if (sharedPreferencesHelper.getQurekaLite()) {
                QurekaOpenAd();
            } else {
                HandleOpenAd(false);
            }
        }

    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, Bundle savedInstanceState) {
        LogUtils.logW(TAG, "onActivityCreated => " + activity);
        currentActivity = activity;
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        LogUtils.logE(TAG, "onActivityStarted => " + activity);
        currentActivity = activity;
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        LogUtils.logE(TAG, "onActivityResumed => " + activity);
        currentActivity = activity;
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        OwnAppOpenCounter++;
        LogUtils.logE(TAG, "onActivityStopped => " + activity);
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
        LogUtils.logE(TAG, "onActivityPaused => " + activity);
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        LogUtils.logE(TAG, "AppOpenManager Splash_Start onActivityDestroyed => " + activity);
    }

    @OnLifecycleEvent(ON_START)
    public void onStart() {
        isResume = !sharedPreferencesHelper.getAppOpenAd();
        Dialog dialog = new Dialog(currentActivity);
        DialogDeveloperBinding binding = DialogDeveloperBinding.inflate(dialog.getLayoutInflater());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(binding.getRoot());
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        if (BuildConfig.DEBUG) {
            LogUtils.logV(TAG, "activityStarted => " + activityStarted);
            if (activityStarted == 0) {
                LogUtils.logV(TAG, "Splash_Start ==========> " + (!Splash_Start));
                if (!Splash_Start) {
                    LogUtils.logD(TAG, "getShowAdinApp => " + sharedPreferencesHelper.getShowAdinApp());
                    if (sharedPreferencesHelper.getShowAdinApp()) {
                        LogUtils.logE(TAG, "getAppOpenAd => " + sharedPreferencesHelper.getAppOpenAd());
                        if (sharedPreferencesHelper.getAppOpenAd()) {
                            LogUtils.logE(TAG, "getAdMobAdShow => " + sharedPreferencesHelper.getAdMobAdShow());
                            if (sharedPreferencesHelper.getAdMobAdShow()) {
                                LogUtils.logE(TAG, "ShowOpenAd => ");
                                ShowOpenAd(false, null);
                            } else if (sharedPreferencesHelper.getQurekaLite()) {
                                QurekaOpenAd();
                            } else {
                                HandleOpenAd(false);
                            }
                        }

                    }
                }
            }
            activityStarted = activityStarted + 1;
        } else {
            if (isDeveloperOptionEnable(currentActivity)) {
                dialog.show();
            } else {
                if (activityStarted == 0) {
                    if (!Splash_Start) {
                        if (sharedPreferencesHelper.getShowAdinApp()) {
                            if (sharedPreferencesHelper.getAppOpenAd()) {
                                if (sharedPreferencesHelper.getAdMobAdShow()) {
                                    ShowOpenAd(false, null);
                                } else if (sharedPreferencesHelper.getQurekaLite()) {
                                    QurekaOpenAd();
                                } else {
                                    HandleOpenAd(false);
                                }
                            } else {
                                HandleOpenAd(false);
                            }

                        }
                    }
                }
                activityStarted = activityStarted + 1;
            }
        }


        binding.btnRetry.setOnClickListener(V -> {
            if (isDeveloperOptionEnable(currentActivity)) {
                currentActivity.startActivity(new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS));
            } else {
                if (activityStarted == 0) {
                    if (!Splash_Start) {
                        if (sharedPreferencesHelper.getShowAdinApp()) {
                            if (sharedPreferencesHelper.getAppOpenAd()) {
                                if (sharedPreferencesHelper.getAdMobAdShow()) {
                                    ShowOpenAd(false, null);
                                } else if (sharedPreferencesHelper.getQurekaLite()) {
                                    QurekaOpenAd();
                                } else {
                                    HandleOpenAd(false);
                                }
                            }

                        }
                    }
                }
                activityStarted = activityStarted + 1;
                dialog.dismiss();
            }
        });

        binding.btnOff.setOnClickListener(V -> {
            if (isDeveloperOptionEnable(currentActivity)) {
                currentActivity.startActivity(new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS));
            } else {
                if (activityStarted == 0) {
                    if (!Splash_Start) {
                        if (sharedPreferencesHelper.getShowAdinApp()) {
                            if (sharedPreferencesHelper.getAppOpenAd()) {
                                if (sharedPreferencesHelper.getAdMobAdShow()) {
                                    ShowOpenAd(false, null);
                                } else if (sharedPreferencesHelper.getQurekaLite()) {
                                    QurekaOpenAd();
                                } else {
                                    HandleOpenAd(false);
                                }
                            }

                        }
                    }
                }
                activityStarted = activityStarted + 1;
                dialog.dismiss();
            }
        });

    }

    private boolean isDeveloperOptionEnable(Activity activity) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            return Settings.Secure.getInt(activity.getContentResolver(), Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0) != 0;
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN) {
            return Settings.Secure.getInt(activity.getContentResolver(), Settings.Secure.DEVELOPMENT_SETTINGS_ENABLED, 0) != 0;
        } else {
            return false;
        }
    }

    @OnLifecycleEvent(ON_STOP)
    public void onStopped() {
        activityStarted = activityStarted - 1;
    }

}
