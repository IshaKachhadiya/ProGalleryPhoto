package com.iten.tenoku.ad;


import static com.iten.tenoku.activity.AppSettingActivity.AD_TYPE;
import static com.iten.tenoku.utils.AdConstant.isResume;
import static com.iten.tenoku.utils.AdUtils.firebaseEvent;
import static com.iten.tenoku.utils.AdUtils.getOpacityColor;
import static com.iten.tenoku.utils.MyApplication.sharedPreferencesHelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.iten.tenoku.ad.AdMob.GoogleCollapseBanner;
import com.iten.tenoku.ad.AdMob.GoogleRewardedAd;
import com.iten.tenoku.ad.AdMob.GoogleInterstitialAd;
import com.iten.tenoku.ad.AdMob.GoogleNativeBannerAd;
import com.iten.tenoku.ad.AdMob.GoogleNativeBigAd;
import com.iten.tenoku.ad.AdMob.GoogleNativeMediumAd;
import com.iten.tenoku.ad.Facebook.FaceMetaInterstitialAd;
import com.iten.tenoku.ad.Facebook.FaceMetaNativeBannerAd;
import com.iten.tenoku.ad.Facebook.FaceMetaNativeBigAd;
import com.iten.tenoku.ad.Facebook.FaceMetaNativeMediumAd;
import com.iten.tenoku.ad.OwnAdz.OwnInterstitailAd;
import com.iten.tenoku.ad.OwnAdz.OwnNative;
import com.iten.tenoku.ad.HandleClick.HandleClick;
import com.iten.tenoku.ad.HandleClick.HandleRewardedAd;
import com.iten.tenoku.ad.Qureka.QuerkaNative;
import com.iten.tenoku.ad.Qureka.QurekaInterstitalAd;
import com.iten.tenoku.databinding.DialogInternetBinding;
import com.iten.tenoku.utils.AdConstant;
import com.iten.tenoku.utils.AdUtils;
import com.iten.tenoku.utils.LogUtils;
import com.iten.tenoku.utils.MyApplication;


public class AdShow {

    private static final String TAG = AdShow.class.getSimpleName();
    public static int MainClickCount = -1;
    public static int BackClickCount = -1;
    public static Boolean MainAlternatives = false;
    @SuppressLint("StaticFieldLeak")
    private static Activity activity;
    @SuppressLint("StaticFieldLeak")
    private static AdShow mInstance;
    HandleClick handleClick = null;
    int interClickCount = 0;
    int nativeBannerCount = 0;
    int nativeMediumCount = 0;
    int nativeBigCount = 0;
    int nativeBigTopCount = 0;

    int OwnBannerCount = 0;
    int OwnMediumCount = 0;
    int OwnBigCount = 0;
    int OwnBigTopCount = 0;
    int OwnInterstitialCount = 0;
    boolean isAdClick = false;
    HandleRewardedAd handleRewardedAd = null;

    public AdShow(Activity activity) {
        AdShow.activity = activity;
    }

    public static AdShow getInstance(Activity adShowingActivity) {
        AdShow.activity = adShowingActivity;
        if (mInstance == null) {
            mInstance = new AdShow(adShowingActivity);
        }
        return mInstance;
    }

    public void ShowAd(HandleClick adShowingActivity, AdUtils.ClickType type) {
        if (isAdClick) {
            return;
        }
        isAdClick = true;

        if (!AdUtils.isNetworkAvailable(activity)) {
            ShowNetworkDialog();
            HandleClick(false);
            return;
        }
        handleClick = adShowingActivity;
        try {
            if (MyApplication.sharedPreferencesHelper.getShowAdinApp()) {
                switch (type) {
                    case MAIN_CLICK:
                        MainClickCount++;
                        int mainClickOnline;
                        mainClickOnline = MyApplication.sharedPreferencesHelper.getMainClick();
                        if (MyApplication.sharedPreferencesHelper.getAMDStatus()) {
                            if (mainClickOnline == 0) {
                                HandleClick(false);
                            } else {
                                if (MainClickCount == 0 && mainClickOnline != 1) {
                                    MainClickCount++;
                                    HandleClick(false);
                                } else {
                                    if (MainClickCount % mainClickOnline == 0) {
                                        HandleClick(false);
                                    } else {
                                        ShowAd();
                                    }
                                }
                            }

                        } else {
                            if (mainClickOnline == 0) {
                                HandleClick(false);
                            } else {
                                if (MainClickCount == 0 && mainClickOnline != 1) {
                                    MainClickCount++;
                                    HandleClick(false);
                                } else {
                                    if (MainClickCount % mainClickOnline != 0) {
                                        HandleClick(false);
                                    } else {
                                        ShowAd();
                                    }
                                }
                            }
                        }
                        break;
                    case BACK_CLICK:
                        BackClickCount++;
                        int backClickOnline;
                        backClickOnline = MyApplication.sharedPreferencesHelper.getBackClick();
                        if (backClickOnline == 0) {
                            HandleClick(false);
                        } else if (BackClickCount == 0 && backClickOnline != 1) {
                            BackClickCount++;
                            HandleClick(false);
                        } else if (BackClickCount % backClickOnline != 0) {
                            if (MyApplication.sharedPreferencesHelper.getShowCountyClick()) {
                                HandleClick(false);
                            } else {
                                if (MyApplication.sharedPreferencesHelper.getQurekaLite()) {
                                    QurekaInterstitalAd.getInstance(activity).ShowQurekaAd(activity, this::HandleClick);
                                } else {
                                    HandleClick(false);
                                }
                            }
                        } else {
                            ShowAd();
                        }
                        break;
                    case EVERY_CLICK:
                        ShowAd();
                        break;

                }
            } else {
                HandleClick(false);
            }
        } catch (NullPointerException e) {
            HandleClick(false);
        }

    }

    private void ShowAd() {
        AdConstant.isInterstitialShown = true;
        if (MyApplication.sharedPreferencesHelper.getAdShowingPopup()) {
            AdUtils.showAdLoading(activity);
        } else {
            AdUtils.showAdProgress(activity);
        }
        if (MyApplication.sharedPreferencesHelper.getAppOpenAd()) {
            if (MyApplication.sharedPreferencesHelper.getOpenAdStatus()) {
                if (MainAlternatives) {
                    MainAlternatives = false;
                    ShowOpenAd();
                } else {
                    MainAlternatives = true;
                    ShowInterstitialsAd();
                }

            } else {
                ShowInterstitialsAd();
            }
        } else {
            ShowInterstitialsAd();
        }
    }


    public void ShowCollapseBanner(Activity activity, ViewGroup container) {
        if (!AdUtils.isNetworkAvailable(activity)) {
            ShowNetworkDialog();
            return;
        }

        Log.e(TAG, "ShowCollapseBanner:===> "+sharedPreferencesHelper.getAdMobAdShow() );
        if (sharedPreferencesHelper.getShowAdinApp()) {
            if (sharedPreferencesHelper.getAdMobAdShow()) {
                container.setVisibility(View.VISIBLE);
                GoogleCollapseBanner.getInstance(activity).ShowCollapseBanner(activity, container);
            } else {
                container.setVisibility(View.GONE);
            }
        } else {
            container.setVisibility(View.GONE);
        }

    }
    public void ShowRewardAd(HandleRewardedAd adShowingActivity) {
        if (!AdUtils.isNetworkAvailable(activity)) {
            ShowNetworkDialog();
            return;
        }
        handleRewardedAd = adShowingActivity;
        try {
            if (MyApplication.sharedPreferencesHelper.getShowAdinApp()) {
                if (MyApplication.sharedPreferencesHelper.getAdMobAdShow()) {
                    AdUtils.showAdLoading(activity);
                    if (MyApplication.sharedPreferencesHelper.getAdShowingPopup()) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.postDelayed(() -> GoogleRewardedAd.getInstance(activity).ShowRewardedAd(this::HandleRewardedClick), AdConstant.adProgressTime);
                    } else {
                        GoogleRewardedAd.getInstance(activity).ShowRewardedAd(this::HandleRewardedClick);
                    }

                } else {
                    HandleRewardedClick(true);
                }
            } else {
                HandleRewardedClick(true);
            }
        } catch (NullPointerException e) {
            HandleRewardedClick(false);
        }
    }

    void HandleRewardedClick(Boolean AdShow) {
        try {
            AdUtils.dismissAdLoading();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (handleRewardedAd != null) {
            handleRewardedAd.Show(AdShow);
            handleRewardedAd = null;
        }
    }

    public void ShowOpenAd() {
        if (MyApplication.sharedPreferencesHelper.getAdShowingPopup()) {
            Handler handler = new Handler(Looper.getMainLooper());
            AdConstant.isInterstitialShown = false;
            handler.postDelayed(() -> MyApplication.appOpenManager.ShowOpenAd(false, AdShow.this::HandleClick), AdConstant.adProgressTime);
        } else {
            MyApplication.appOpenManager.ShowOpenAd(false, this::HandleClick);
        }

    }

    public void ShowInterstitialsAd() {
        if (!MyApplication.sharedPreferencesHelper.getShowAdinApp()) {
            HandleClick(false);
            return;
        }

        if (MyApplication.sharedPreferencesHelper.getAdShowingPopup()) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(this::ShowInterstitialsAdProgress, AdConstant.adProgressTime);
        } else {
            ShowInterstitialsAdProgress();
        }

    }

    private void ShowInterstitialsAdProgress() {
        LogUtils.logW(TAG, "AD_TYPE => " + AD_TYPE);
        switch (AD_TYPE) {
            case ADMOB_FACEBOOK:
                if (!MyApplication.sharedPreferencesHelper.getAlternateAdShow().isEmpty()) {
                    showAdmobFacebookInterstitialAd();
                } else if (!MyApplication.sharedPreferencesHelper.getFixSequence().isEmpty()) {
                    interClickCount++;
                    if (sharedPreferencesHelper.getInterNativeAdSequenceCounter() == 0) {
                        if (interClickCount % 2 == 1) {
                            showAdmobFacebookInterstitialAd();
                        } else {
                            showFacebookAdmobInterstitialAd();
                        }
                    } else {
                        LogUtils.logW(TAG, "interClickCount => " + interClickCount + " ==> " + MyApplication.sharedPreferencesHelper.getInterNativeAdSequenceCounter() + " ======> " + (interClickCount <= MyApplication.sharedPreferencesHelper.getInterNativeAdSequenceCounter()));
                        if (interClickCount <= MyApplication.sharedPreferencesHelper.getInterNativeAdSequenceCounter()) {
                            showAdmobFacebookInterstitialAd();
                        } else {
                            showFacebookAdmobInterstitialAd();
                            interClickCount = 0;
                        }
                    }
                } else {
                    showAdmobFacebookInterstitialAd();
                }
                break;
            case FACEBOOK_ADMOB:
                if (!MyApplication.sharedPreferencesHelper.getAlternateAdShow().isEmpty()) {
                    showFacebookAdmobInterstitialAd();
                } else if (!MyApplication.sharedPreferencesHelper.getFixSequence().isEmpty()) {
                    interClickCount++;
                    if (sharedPreferencesHelper.getInterNativeAdSequenceCounter() == 0) {
                        if (interClickCount % 2 == 1) {
                            showFacebookAdmobInterstitialAd();
                        } else {
                            showAdmobFacebookInterstitialAd();
                        }
                    } else {
                        LogUtils.logW(TAG, "interClickCount => " + interClickCount + " ==> " + MyApplication.sharedPreferencesHelper.getInterNativeAdSequenceCounter() + " ======> " + (interClickCount <= MyApplication.sharedPreferencesHelper.getInterNativeAdSequenceCounter()));

                        if (interClickCount <= MyApplication.sharedPreferencesHelper.getInterNativeAdSequenceCounter()) {
                            showFacebookAdmobInterstitialAd();
                        } else {
                            showAdmobFacebookInterstitialAd();
                            interClickCount = 0;
                        }
                    }
                } else {
                    showFacebookAdmobInterstitialAd();
                }
                break;
            case ADMOB:
                showOnlyAdmobInterstitialAd();
                break;
            case FACEBOOK:
                showOnlyFbInterstitialAd();
                break;
            case OWNADS_QUREKA:
                OwnInterstitialCount++;
                if (OwnInterstitialCount % 2 == 1) {
                    showOwnInterstitialAd();
                } else {
                    showQureka();
                }
                break;
            case OWNADS:
                showOwnInterstitialAd();
                break;
        }
    }

    private void showAdmobFacebookInterstitialAd() {
        GoogleInterstitialAd.getInstance(activity).ShowInterstitialAd(adShow -> {
            if (adShow) {
                HandleClick(true);
            } else {
                FaceMetaInterstitialAd.getInstance(activity).showInterstitialAd(adShow1 -> {
                    if (adShow1) {
                        if (sharedPreferencesHelper.getPreload()) {
                            GoogleInterstitialAd.getInstance(activity).LoadInterstitialAd();
                        }
                        HandleClick(true);
                    } else {
                        showQureka();
                    }
                });
            }
        });
    }

    private void showFacebookAdmobInterstitialAd() {
        FaceMetaInterstitialAd.getInstance(activity).showInterstitialAd(adShow -> {
            if (adShow) {
                HandleClick(true);
            } else {
                GoogleInterstitialAd.getInstance(activity).ShowInterstitialAd(adShow13 -> {
                    if (adShow13) {
                        if (sharedPreferencesHelper.getPreload()) {
                            FaceMetaInterstitialAd.getInstance(activity).LoadInterstitialAd();
                        }
                        HandleClick(true);
                    } else {
                        showQureka();
                    }
                });
            }
        });
    }

    private void showOnlyAdmobInterstitialAd() {
        GoogleInterstitialAd.getInstance(activity).ShowInterstitialAd(adShow1 -> {
            if (adShow1) {
                HandleClick(true);
            } else {
                showQureka();
            }
        });
    }

    private void showOnlyFbInterstitialAd() {
        FaceMetaInterstitialAd.getInstance(activity).showInterstitialAd(adShow1 -> {
            if (adShow1) {
                HandleClick(true);
            } else {
                showQureka();
            }
        });
    }

    private void showOwnInterstitialAd() {
        if (sharedPreferencesHelper.getOwnAdShow()) {
            OwnInterstitailAd.getInstance(activity).ShowOwnInterstialAd(activity, this::HandleClick);
        }
    }

    private void showQureka() {
        if (sharedPreferencesHelper.getPreload()) {
            switch (AD_TYPE) {
                case FACEBOOK_ADMOB:
                case ADMOB_FACEBOOK:
                    GoogleInterstitialAd.getInstance(activity).LoadInterstitialAd();
                    FaceMetaInterstitialAd.getInstance(activity).LoadInterstitialAd();
                    break;
                case ADMOB:
                    GoogleInterstitialAd.getInstance(activity).LoadInterstitialAd();
                    break;
                case FACEBOOK:
                    FaceMetaInterstitialAd.getInstance(activity).LoadInterstitialAd();
                    break;
            }
        }
        if (MyApplication.sharedPreferencesHelper.getQurekaLite()) {
            QurekaInterstitalAd.getInstance(activity).ShowQurekaAd(activity, this::HandleClick);
        } else {
            HandleClick(false);
        }
    }

    public void ShowNativeAd(ViewGroup nativeAdContainer, AdUtils.NativeType nativeType) {
        if (!MyApplication.sharedPreferencesHelper.getAddBgColor().isEmpty()) {
            getOpacityColor(nativeAdContainer.getBackground(), MyApplication.sharedPreferencesHelper.getAdBgColorOpacity(), true);
        }

        if (!MyApplication.sharedPreferencesHelper.getShowAdinApp()) {
            nativeAdContainer.setVisibility(View.GONE);
            return;
        }

        if (!AdUtils.isNetworkAvailable(activity)) {
            nativeAdContainer.setVisibility(View.GONE);
        } else {
            try {
                if (MyApplication.sharedPreferencesHelper.getNativeAdShow()) {
                    LogUtils.logE(TAG, "ShowNativeAd isResume =====> " + isResume + " AD_TYPE => " + AD_TYPE);
                    if (isResume) {
                        if (sharedPreferencesHelper.getOwnAdShow()) {
                            if (!sharedPreferencesHelper.getOwnSequence().isEmpty()) {
                                switch (AD_TYPE) {
                                    case OWNADS_QUREKA:
                                        ShowOwnAndQurekaAds(nativeAdContainer, nativeType);
                                        break;
                                    case OWNADS:
                                        ShowOwnAds(nativeAdContainer, nativeType);
                                        break;
                                }
                            } else {
                                ShowOwnAds(nativeAdContainer, nativeType);
                            }
                        } else if (!MyApplication.sharedPreferencesHelper.getAlternateAdShow().isEmpty()) {
                            switch (AD_TYPE) {
                                case ADMOB_FACEBOOK:
                                    showAdmobFacebookNativeAd(nativeAdContainer, nativeType);
                                    break;
                                case FACEBOOK_ADMOB:
                                    showFacebookAdmobNativeAd(nativeAdContainer, nativeType);
                                    break;
                                case FACEBOOK:
                                    showOnlyFbNativeAd(nativeAdContainer, nativeType);
                                    break;
                                case ADMOB:
                                    showOnlyAdmobNativeAd(nativeAdContainer, nativeType);
                                    break;
                            }
                        } else if (!MyApplication.sharedPreferencesHelper.getFixSequence().isEmpty()) {
                            switch (nativeType) {
                                case NATIVE_BANNER:
                                    nativeBannerCount++;
                                    switch (AD_TYPE) {
                                        case ADMOB_FACEBOOK:
                                            if (sharedPreferencesHelper.getInterNativeAdSequenceCounter() == 0) {
                                                if (nativeBannerCount % 2 == 1) {
                                                    showAdmobFacebookNativeAd(nativeAdContainer, nativeType);
                                                } else {
                                                    showFacebookAdmobNativeAd(nativeAdContainer, nativeType);
                                                }
                                            } else {
                                                if (nativeBannerCount <= MyApplication.sharedPreferencesHelper.getInterNativeAdSequenceCounter()) {
                                                    showAdmobFacebookNativeAd(nativeAdContainer, nativeType);
                                                } else {
                                                    showFacebookAdmobNativeAd(nativeAdContainer, nativeType);
                                                    nativeBannerCount = 0;
                                                }
                                            }
                                            break;
                                        case FACEBOOK_ADMOB:
                                            if (sharedPreferencesHelper.getInterNativeAdSequenceCounter() == 0) {
                                                if (nativeBannerCount % 2 == 1) {
                                                    showFacebookAdmobNativeAd(nativeAdContainer, nativeType);
                                                } else {
                                                    showAdmobFacebookNativeAd(nativeAdContainer, nativeType);
                                                }
                                            } else {
                                                if (nativeBannerCount <= MyApplication.sharedPreferencesHelper.getInterNativeAdSequenceCounter()) {
                                                    showFacebookAdmobNativeAd(nativeAdContainer, nativeType);
                                                } else {
                                                    showAdmobFacebookNativeAd(nativeAdContainer, nativeType);
                                                    nativeBannerCount = 0;
                                                }
                                            }
                                            break;
                                        case ADMOB:
                                            showOnlyAdmobNativeAd(nativeAdContainer, nativeType);
                                            break;
                                        case FACEBOOK:
                                            showOnlyFbNativeAd(nativeAdContainer, nativeType);
                                            break;
                                    }
                                    break;
                                case NATIVE_BIG:
                                    nativeBigCount++;
                                    switch (AD_TYPE) {
                                        case ADMOB_FACEBOOK:
                                            if (sharedPreferencesHelper.getInterNativeAdSequenceCounter() == 0) {
                                                if (nativeBigCount % 2 == 1) {
                                                    showAdmobFacebookNativeAd(nativeAdContainer, nativeType);
                                                } else {
                                                    showFacebookAdmobNativeAd(nativeAdContainer, nativeType);
                                                }
                                            } else {
                                                if (nativeBigCount <= MyApplication.sharedPreferencesHelper.getInterNativeAdSequenceCounter()) {
                                                    showAdmobFacebookNativeAd(nativeAdContainer, nativeType);
                                                } else {
                                                    showFacebookAdmobNativeAd(nativeAdContainer, nativeType);
                                                    nativeBigCount = 0;
                                                }
                                            }
                                            break;
                                        case FACEBOOK_ADMOB:
                                            if (sharedPreferencesHelper.getInterNativeAdSequenceCounter() == 0) {
                                                if (nativeBigCount % 2 == 1) {
                                                    showFacebookAdmobNativeAd(nativeAdContainer, nativeType);
                                                } else {
                                                    showAdmobFacebookNativeAd(nativeAdContainer, nativeType);
                                                }
                                            } else {
                                                if (nativeBigCount <= MyApplication.sharedPreferencesHelper.getInterNativeAdSequenceCounter()) {
                                                    showFacebookAdmobNativeAd(nativeAdContainer, nativeType);
                                                } else {
                                                    showAdmobFacebookNativeAd(nativeAdContainer, nativeType);
                                                    nativeBigCount = 0;
                                                }
                                            }
                                            break;
                                        case ADMOB:
                                            showOnlyAdmobNativeAd(nativeAdContainer, nativeType);
                                            break;
                                        case FACEBOOK:
                                            showOnlyFbNativeAd(nativeAdContainer, nativeType);
                                            break;
                                    }
                                    break;
                                case NATIVE_MEDIUM:
                                    nativeMediumCount++;
                                    switch (AD_TYPE) {
                                        case ADMOB_FACEBOOK:
                                            if (sharedPreferencesHelper.getInterNativeAdSequenceCounter() == 0) {
                                                if (nativeMediumCount % 2 == 1) {
                                                    showAdmobFacebookNativeAd(nativeAdContainer, nativeType);
                                                } else {
                                                    showFacebookAdmobNativeAd(nativeAdContainer, nativeType);
                                                }
                                            } else {
                                                if (nativeMediumCount <= MyApplication.sharedPreferencesHelper.getInterNativeAdSequenceCounter()) {
                                                    showAdmobFacebookNativeAd(nativeAdContainer, nativeType);
                                                } else {
                                                    showFacebookAdmobNativeAd(nativeAdContainer, nativeType);
                                                    nativeMediumCount = 0;
                                                }
                                            }
                                            break;
                                        case FACEBOOK_ADMOB:
                                            if (sharedPreferencesHelper.getInterNativeAdSequenceCounter() == 0) {
                                                if (nativeMediumCount % 2 == 1) {
                                                    showFacebookAdmobNativeAd(nativeAdContainer, nativeType);
                                                } else {
                                                    showAdmobFacebookNativeAd(nativeAdContainer, nativeType);
                                                }
                                            } else {
                                                if (nativeMediumCount <= MyApplication.sharedPreferencesHelper.getInterNativeAdSequenceCounter()) {
                                                    showFacebookAdmobNativeAd(nativeAdContainer, nativeType);
                                                } else {
                                                    showAdmobFacebookNativeAd(nativeAdContainer, nativeType);
                                                    nativeMediumCount = 0;
                                                }
                                            }
                                            break;
                                        case ADMOB:
                                            showOnlyAdmobNativeAd(nativeAdContainer, nativeType);
                                            break;
                                        case FACEBOOK:
                                            showOnlyFbNativeAd(nativeAdContainer, nativeType);
                                            break;
                                    }
                                    break;
                                case NATIVE_BIG_TOP:
                                    nativeBigTopCount++;
                                    switch (AD_TYPE) {
                                        case ADMOB_FACEBOOK:
                                            if (sharedPreferencesHelper.getInterNativeAdSequenceCounter() == 0) {
                                                if (nativeBigTopCount % 2 == 1) {
                                                    showAdmobFacebookNativeAd(nativeAdContainer, nativeType);
                                                } else {
                                                    showFacebookAdmobNativeAd(nativeAdContainer, nativeType);
                                                }
                                            } else {
                                                if (nativeBigTopCount <= MyApplication.sharedPreferencesHelper.getInterNativeAdSequenceCounter()) {
                                                    showAdmobFacebookNativeAd(nativeAdContainer, nativeType);
                                                } else {
                                                    showFacebookAdmobNativeAd(nativeAdContainer, nativeType);
                                                    nativeBigTopCount = 0;
                                                }
                                            }
                                            break;
                                        case FACEBOOK_ADMOB:
                                            if (sharedPreferencesHelper.getInterNativeAdSequenceCounter() == 0) {
                                                if (nativeBigTopCount % 2 == 1) {
                                                    showFacebookAdmobNativeAd(nativeAdContainer, nativeType);
                                                } else {
                                                    showAdmobFacebookNativeAd(nativeAdContainer, nativeType);
                                                }
                                            } else {
                                                if (nativeBigTopCount <= MyApplication.sharedPreferencesHelper.getInterNativeAdSequenceCounter()) {
                                                    showFacebookAdmobNativeAd(nativeAdContainer, nativeType);
                                                } else {
                                                    showAdmobFacebookNativeAd(nativeAdContainer, nativeType);
                                                    nativeBigTopCount = 0;
                                                }
                                            }
                                            break;
                                        case ADMOB:
                                            showOnlyAdmobNativeAd(nativeAdContainer, nativeType);
                                            break;
                                        case FACEBOOK:
                                            showOnlyFbNativeAd(nativeAdContainer, nativeType);
                                            break;
                                    }
                                    break;
                            }
                        } else {
                            showAdmobFacebookNativeAd(nativeAdContainer, nativeType);
                        }
                    }
                } else {
                    nativeAdContainer.setVisibility(View.GONE);
                }
            } catch (NullPointerException e) {
                nativeAdContainer.setVisibility(View.GONE);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }

        }
    }

    private void showAdmobFacebookNativeAd(ViewGroup nativeAdContainer, AdUtils.NativeType nativeType) {
        switch (nativeType) {
            case NATIVE_BANNER:
                if (MyApplication.sharedPreferencesHelper.getNativeBannerAdShow()) {
                    if (MyApplication.sharedPreferencesHelper.getQurekaLite()) {
                        QuerkaNative.getInstance(activity).ShowQurekaBannerAd(nativeAdContainer);
                    }
                    GoogleNativeBannerAd.getInstance(activity).ShowNativeAdSmall(nativeAdContainer, shown -> {
                        if (shown) {
                            nativeAdContainer.setVisibility(View.VISIBLE);
                        } else {
                            FaceMetaNativeBannerAd.getInstance(activity).ShowNativeAdSmall(nativeAdContainer, shown1 -> {
                                if (shown1) {
                                    if (sharedPreferencesHelper.getPreload()) {
                                        GoogleNativeBannerAd.getInstance(activity).loadNativeBannerAds();
                                    }
                                    nativeAdContainer.setVisibility(View.VISIBLE);
                                } else {
                                    if (sharedPreferencesHelper.getPreload()) {
                                        GoogleNativeBannerAd.getInstance(activity).loadNativeBannerAds();
                                        FaceMetaNativeBannerAd.getInstance(activity).loadFacebookNativeBannerAds();
                                    }
                                }
                            });
                        }
                    });
                } else {
                    nativeAdContainer.setVisibility(View.GONE);
                }
                break;
            case NATIVE_MEDIUM:
                if (MyApplication.sharedPreferencesHelper.getNativeBigAdShow()) {
                    if (MyApplication.sharedPreferencesHelper.getQurekaLite()) {
                        QuerkaNative.getInstance(activity).ShowQurekaMediumAd(nativeAdContainer);
                    }
                    GoogleNativeMediumAd.getInstance(activity).ShowMediumNativeAd(nativeAdContainer, shown -> {
                        if (shown) {
                            nativeAdContainer.setVisibility(View.VISIBLE);
                        } else {
                            FaceMetaNativeMediumAd.getInstance(activity).ShowMediumNativeAd(nativeAdContainer, shown1 -> {
                                if (shown1) {
                                    if (sharedPreferencesHelper.getPreload()) {
                                        GoogleNativeMediumAd.getInstance(activity).LoadNativeMediumAds();
                                    }
                                    nativeAdContainer.setVisibility(View.VISIBLE);
                                } else {
                                    if (sharedPreferencesHelper.getPreload()) {
                                        GoogleNativeMediumAd.getInstance(activity).LoadNativeMediumAds();
                                        FaceMetaNativeMediumAd.getInstance(activity).LoadNativeMediumAds();
                                    }
                                }
                            });
                        }
                    });

                } else {
                    nativeAdContainer.setVisibility(View.GONE);
                }
                break;
            case NATIVE_BIG:
                if (MyApplication.sharedPreferencesHelper.getNativeBigAdShow()) {
                    if (MyApplication.sharedPreferencesHelper.getQurekaLite()) {
                        QuerkaNative.getInstance(activity).ShowQurekaBigAd(nativeAdContainer);
                    }
                    GoogleNativeBigAd.getInstance(activity).ShowTypeWiseNativeAd(nativeAdContainer, nativeType, shown -> {
                        if (shown) {
                            nativeAdContainer.setVisibility(View.VISIBLE);
                        } else {
                            FaceMetaNativeBigAd.getInstance(activity).ShowTypeWiseNativeAd(nativeAdContainer, nativeType, shown1 -> {
                                if (shown1) {
                                    if (sharedPreferencesHelper.getPreload()) {
                                        GoogleNativeBigAd.getInstance(activity).LoadNativeBigAds();
                                    }
                                    nativeAdContainer.setVisibility(View.VISIBLE);
                                } else {
                                    if (sharedPreferencesHelper.getPreload()) {
                                        GoogleNativeBigAd.getInstance(activity).LoadNativeBigAds();
                                        FaceMetaNativeBigAd.getInstance(activity).LoadFacebookNativeBigAds();
                                    }
                                }
                            });
                        }
                    });
                } else {
                    nativeAdContainer.setVisibility(View.GONE);
                }
                break;
            case NATIVE_BIG_TOP:
                if (MyApplication.sharedPreferencesHelper.getNativeBigAdShow()) {
                    if (MyApplication.sharedPreferencesHelper.getQurekaLite()) {
                        QuerkaNative.getInstance(activity).ShowQurekaBigBtnTopAd(nativeAdContainer);
                    }
                    GoogleNativeBigAd.getInstance(activity).ShowTypeWiseNativeAd(nativeAdContainer, nativeType, shown -> {
                        if (shown) {
                            nativeAdContainer.setVisibility(View.VISIBLE);
                        } else {
                            if (MyApplication.sharedPreferencesHelper.getFacebookAdShow()) {
                                FaceMetaNativeBigAd.getInstance(activity).ShowTypeWiseNativeAd(nativeAdContainer, nativeType, shown1 -> {
                                    if (shown1) {
                                        if (sharedPreferencesHelper.getPreload()) {
                                            GoogleNativeBigAd.getInstance(activity).LoadNativeBigAds();
                                        }
                                        nativeAdContainer.setVisibility(View.VISIBLE);
                                    } else {
                                        if (sharedPreferencesHelper.getPreload()) {
                                            GoogleNativeBigAd.getInstance(activity).LoadNativeBigAds();
                                            FaceMetaNativeBigAd.getInstance(activity).LoadFacebookNativeBigAds();
                                        }
                                    }
                                });
                            }
                        }
                    });

                } else {
                    nativeAdContainer.setVisibility(View.GONE);
                }
                break;
            default:
                nativeAdContainer.setVisibility(View.GONE);
                break;
        }
    }

    private void showFacebookAdmobNativeAd(ViewGroup nativeAdContainer, AdUtils.NativeType nativeType) {
        switch (nativeType) {
            case NATIVE_BANNER:
                if (MyApplication.sharedPreferencesHelper.getNativeBannerAdShow()) {
                    if (MyApplication.sharedPreferencesHelper.getQurekaLite()) {
                        QuerkaNative.getInstance(activity).ShowQurekaBannerAd(nativeAdContainer);
                    }
                    FaceMetaNativeBannerAd.getInstance(activity).ShowNativeAdSmall(nativeAdContainer, shown -> {
                        if (shown) {
                            nativeAdContainer.setVisibility(View.VISIBLE);
                        } else {
                            GoogleNativeBannerAd.getInstance(activity).ShowNativeAdSmall(nativeAdContainer, shown1 -> {
                                if (shown1) {
                                    if (sharedPreferencesHelper.getPreload()) {
                                        FaceMetaNativeBannerAd.getInstance(activity).loadFacebookNativeBannerAds();
                                    }
                                    nativeAdContainer.setVisibility(View.VISIBLE);
                                } else {
                                    if (sharedPreferencesHelper.getPreload()) {
                                        FaceMetaNativeBannerAd.getInstance(activity).loadFacebookNativeBannerAds();
                                        GoogleNativeBannerAd.getInstance(activity).loadNativeBannerAds();
                                    }
                                }
                            });
                        }
                    });
                } else {
                    nativeAdContainer.setVisibility(View.GONE);
                }
                break;
            case NATIVE_MEDIUM:
                if (MyApplication.sharedPreferencesHelper.getNativeBigAdShow()) {
                    if (MyApplication.sharedPreferencesHelper.getQurekaLite()) {
                        QuerkaNative.getInstance(activity).ShowQurekaMediumAd(nativeAdContainer);
                    }
                    FaceMetaNativeMediumAd.getInstance(activity).ShowMediumNativeAd(nativeAdContainer, shown -> {
                        if (shown) {
                            nativeAdContainer.setVisibility(View.VISIBLE);
                        } else {
                            GoogleNativeMediumAd.getInstance(activity).ShowMediumNativeAd(nativeAdContainer, shown1 -> {
                                if (shown1) {
                                    if (sharedPreferencesHelper.getPreload()) {
                                        FaceMetaNativeMediumAd.getInstance(activity).LoadNativeMediumAds();
                                    }
                                    nativeAdContainer.setVisibility(View.VISIBLE);
                                } else {
                                    if (sharedPreferencesHelper.getPreload()) {
                                        FaceMetaNativeMediumAd.getInstance(activity).LoadNativeMediumAds();
                                        GoogleNativeMediumAd.getInstance(activity).LoadNativeMediumAds();
                                    }
                                }
                            });
                        }
                    });
                } else {
                    nativeAdContainer.setVisibility(View.GONE);
                }
                break;
            case NATIVE_BIG:
                if (MyApplication.sharedPreferencesHelper.getNativeBigAdShow()) {
                    if (MyApplication.sharedPreferencesHelper.getQurekaLite()) {
                        QuerkaNative.getInstance(activity).ShowQurekaBigAd(nativeAdContainer);
                    }
                    FaceMetaNativeBigAd.getInstance(activity).ShowTypeWiseNativeAd(nativeAdContainer, nativeType, shown -> {
                        if (shown) {
                            nativeAdContainer.setVisibility(View.VISIBLE);
                        } else {
                            GoogleNativeBigAd.getInstance(activity).ShowTypeWiseNativeAd(nativeAdContainer, nativeType, shown1 -> {
                                if (shown1) {
                                    if (sharedPreferencesHelper.getPreload()) {
                                        FaceMetaNativeBigAd.getInstance(activity).LoadFacebookNativeBigAds();
                                    }
                                    nativeAdContainer.setVisibility(View.VISIBLE);
                                } else {
                                    if (sharedPreferencesHelper.getPreload()) {
                                        FaceMetaNativeBigAd.getInstance(activity).LoadFacebookNativeBigAds();
                                        GoogleNativeBigAd.getInstance(activity).LoadNativeBigAds();
                                    }
                                }
                            });
                        }
                    });
                } else {
                    nativeAdContainer.setVisibility(View.GONE);
                }
                break;
            case NATIVE_BIG_TOP:
                if (MyApplication.sharedPreferencesHelper.getNativeBigAdShow()) {
                    if (MyApplication.sharedPreferencesHelper.getQurekaLite()) {
                        QuerkaNative.getInstance(activity).ShowQurekaBigBtnTopAd(nativeAdContainer);
                    }
                    FaceMetaNativeBigAd.getInstance(activity).ShowTypeWiseNativeAd(nativeAdContainer, nativeType, shown -> {
                        if (shown) {
                            nativeAdContainer.setVisibility(View.VISIBLE);
                        } else {
                            GoogleNativeBigAd.getInstance(activity).ShowTypeWiseNativeAd(nativeAdContainer, nativeType, shown1 -> {
                                if (shown1) {
                                    if (sharedPreferencesHelper.getPreload()) {
                                        FaceMetaNativeBigAd.getInstance(activity).LoadFacebookNativeBigAds();
                                    }
                                    nativeAdContainer.setVisibility(View.VISIBLE);
                                } else {
                                    if (sharedPreferencesHelper.getPreload()) {
                                        FaceMetaNativeBigAd.getInstance(activity).LoadFacebookNativeBigAds();
                                        GoogleNativeBigAd.getInstance(activity).LoadNativeBigAds();
                                    }
                                }
                            });
                        }
                    });
                } else {
                    nativeAdContainer.setVisibility(View.GONE);
                }
                break;
            default:
                nativeAdContainer.setVisibility(View.GONE);
                break;
        }
    }

    private void showOnlyAdmobNativeAd(ViewGroup nativeAdContainer, AdUtils.NativeType nativeType) {
        switch (nativeType) {
            case NATIVE_BANNER:
                if (MyApplication.sharedPreferencesHelper.getNativeBannerAdShow()) {
                    if (MyApplication.sharedPreferencesHelper.getQurekaLite()) {
                        QuerkaNative.getInstance(activity).ShowQurekaBannerAd(nativeAdContainer);
                    }
                    GoogleNativeBannerAd.getInstance(activity).ShowNativeAdSmall(nativeAdContainer, shown -> {
                        if (shown) {
                            nativeAdContainer.setVisibility(View.VISIBLE);
                        } else {
                            if (sharedPreferencesHelper.getPreload()) {
                                GoogleNativeBannerAd.getInstance(activity).loadNativeBannerAds();
                            }
                        }
                    });
                } else {
                    nativeAdContainer.setVisibility(View.GONE);
                }
                break;
            case NATIVE_MEDIUM:
                if (MyApplication.sharedPreferencesHelper.getNativeBigAdShow()) {
                    if (MyApplication.sharedPreferencesHelper.getQurekaLite()) {
                        QuerkaNative.getInstance(activity).ShowQurekaMediumAd(nativeAdContainer);
                    }
                    GoogleNativeMediumAd.getInstance(activity).ShowMediumNativeAd(nativeAdContainer, shown -> {
                        if (shown) {
                            nativeAdContainer.setVisibility(View.VISIBLE);
                        } else {
                            if (sharedPreferencesHelper.getPreload()) {
                                GoogleNativeMediumAd.getInstance(activity).LoadNativeMediumAds();
                            }
                        }
                    });
                } else {
                    nativeAdContainer.setVisibility(View.GONE);
                }
                break;
            case NATIVE_BIG:
                if (MyApplication.sharedPreferencesHelper.getNativeBigAdShow()) {
                    if (MyApplication.sharedPreferencesHelper.getQurekaLite()) {
                        QuerkaNative.getInstance(activity).ShowQurekaBigAd(nativeAdContainer);
                    }
                    GoogleNativeBigAd.getInstance(activity).ShowTypeWiseNativeAd(nativeAdContainer, nativeType, shown -> {
                        if (shown) {
                            nativeAdContainer.setVisibility(View.VISIBLE);
                        } else {
                            if (sharedPreferencesHelper.getPreload()) {
                                GoogleNativeBigAd.getInstance(activity).LoadNativeBigAds();
                            }
                        }
                    });
                } else {
                    nativeAdContainer.setVisibility(View.GONE);
                }
                break;
            case NATIVE_BIG_TOP:
                if (MyApplication.sharedPreferencesHelper.getNativeBigAdShow()) {
                    if (MyApplication.sharedPreferencesHelper.getQurekaLite()) {
                        QuerkaNative.getInstance(activity).ShowQurekaBigBtnTopAd(nativeAdContainer);
                    }
                    GoogleNativeBigAd.getInstance(activity).ShowTypeWiseNativeAd(nativeAdContainer, nativeType, shown -> {
                        if (shown) {
                            nativeAdContainer.setVisibility(View.VISIBLE);
                        } else {
                            if (sharedPreferencesHelper.getPreload()) {
                                GoogleNativeBigAd.getInstance(activity).LoadNativeBigAds();
                            }
                        }
                    });
                } else {
                    nativeAdContainer.setVisibility(View.GONE);
                }
                break;
            default:
                nativeAdContainer.setVisibility(View.GONE);
                break;
        }
    }

    private void showOnlyFbNativeAd(ViewGroup nativeAdContainer, AdUtils.NativeType nativeType) {
        switch (nativeType) {
            case NATIVE_BANNER:
                if (MyApplication.sharedPreferencesHelper.getNativeBannerAdShow()) {
                    if (MyApplication.sharedPreferencesHelper.getQurekaLite()) {
                        QuerkaNative.getInstance(activity).ShowQurekaBannerAd(nativeAdContainer);
                    }
                    FaceMetaNativeBannerAd.getInstance(activity).ShowNativeAdSmall(nativeAdContainer, shown -> {
                        if (shown) {
                            nativeAdContainer.setVisibility(View.VISIBLE);
                        } else {
                            if (sharedPreferencesHelper.getPreload()) {
                                FaceMetaNativeBannerAd.getInstance(activity).loadFacebookNativeBannerAds();
                            }
                        }
                    });
                } else {
                    nativeAdContainer.setVisibility(View.GONE);
                }
                break;
            case NATIVE_MEDIUM:
                if (MyApplication.sharedPreferencesHelper.getNativeBigAdShow()) {
                    if (MyApplication.sharedPreferencesHelper.getQurekaLite()) {
                        QuerkaNative.getInstance(activity).ShowQurekaMediumAd(nativeAdContainer);
                    }
                    FaceMetaNativeMediumAd.getInstance(activity).ShowMediumNativeAd(nativeAdContainer, shown -> {
                        if (shown) {
                            nativeAdContainer.setVisibility(View.VISIBLE);
                        } else {
                            if (sharedPreferencesHelper.getPreload()) {
                                FaceMetaNativeMediumAd.getInstance(activity).LoadNativeMediumAds();
                            }
                        }
                    });
                } else {
                    nativeAdContainer.setVisibility(View.GONE);
                }
                break;
            case NATIVE_BIG:
                if (MyApplication.sharedPreferencesHelper.getNativeBigAdShow()) {
                    if (MyApplication.sharedPreferencesHelper.getQurekaLite()) {
                        QuerkaNative.getInstance(activity).ShowQurekaBigAd(nativeAdContainer);
                    }
                    FaceMetaNativeBigAd.getInstance(activity).ShowTypeWiseNativeAd(nativeAdContainer, nativeType, shown -> {
                        if (shown) {
                            nativeAdContainer.setVisibility(View.VISIBLE);
                        } else {
                            if (sharedPreferencesHelper.getPreload()) {
                                FaceMetaNativeBigAd.getInstance(activity).LoadFacebookNativeBigAds();
                            }
                        }
                    });
                } else {
                    nativeAdContainer.setVisibility(View.GONE);
                }
                break;
            case NATIVE_BIG_TOP:
                if (MyApplication.sharedPreferencesHelper.getNativeBigAdShow()) {
                    if (MyApplication.sharedPreferencesHelper.getQurekaLite()) {
                        QuerkaNative.getInstance(activity).ShowQurekaBigBtnTopAd(nativeAdContainer);
                    }
                    FaceMetaNativeBigAd.getInstance(activity).ShowTypeWiseNativeAd(nativeAdContainer, nativeType, shown -> {
                        if (shown) {
                            nativeAdContainer.setVisibility(View.VISIBLE);
                        } else {
                            if (sharedPreferencesHelper.getPreload()) {
                                FaceMetaNativeBigAd.getInstance(activity).LoadFacebookNativeBigAds();
                            }
                        }
                    });
                } else {
                    nativeAdContainer.setVisibility(View.GONE);
                }
                break;
        }
    }

    private void ShowOwnAds(ViewGroup nativeAdContainer, AdUtils.NativeType nativeType) {
        switch (nativeType) {
            case NATIVE_BIG:
                OwnNative.getInstance(activity).ShowOwnNative(nativeAdContainer);
                break;
            case NATIVE_MEDIUM:
                OwnNative.getInstance(activity).ShowOwnNativeMedium(nativeAdContainer);
                break;
            case NATIVE_BANNER:
                OwnNative.getInstance(activity).ShowOwnNativeBanner(nativeAdContainer);
                break;
        }

    }

    private void ShowOwnAndQurekaAds(ViewGroup nativeAdContainer, AdUtils.NativeType nativeType) {
        switch (nativeType) {
            case NATIVE_BIG:
                OwnBigCount++;
                if (OwnBigCount % 2 == 1) {
                    firebaseEvent("OwnAdsNativeBig OwnShow " + sharedPreferencesHelper.getOwnNativePosition());
                    LogUtils.logE(TAG, "OWNADS_QUREKA=======>  OWN NATIVE_BIG LOADED");
                    OwnNative.getInstance(activity).ShowOwnNative(nativeAdContainer);
                } else {
                    firebaseEvent("QurekaBigNative Show ");
                    LogUtils.logE(TAG, "OWNADS_QUREKA=======>  QUREKA NATIVE_BIG LOADED");
                    QuerkaNative.getInstance(activity).ShowQurekaBigAd(nativeAdContainer);
                }
                break;

            case NATIVE_BANNER:
                OwnBannerCount++;
                if (OwnBannerCount % 2 == 1) {
                    firebaseEvent("OwnAdsNativeBanner OwnShow " + sharedPreferencesHelper.getOwnBannerPosition());
                    LogUtils.logE(TAG, "OWNADS_QUREKA=======>  OWN NATIVE_BANNER LOADED");
                    OwnNative.getInstance(activity).ShowOwnNativeBanner(nativeAdContainer);
                } else {
                    firebaseEvent("QurekaNativeBanner Show ");
                    LogUtils.logE(TAG, "OWNADS_QUREKA=======> QUREKA NATIVE_BANNER LOADED");
                    QuerkaNative.getInstance(activity).ShowQurekaBannerAd(nativeAdContainer);
                }
                break;
            case NATIVE_MEDIUM:
                OwnMediumCount++;
                if (OwnMediumCount % 2 == 1) {
                    firebaseEvent("OwnAdsNativeMedium OwnShow " + sharedPreferencesHelper.getOwnNativePosition());
                    LogUtils.logE(TAG, "OWNADS_QUREKA=======>  OWN NATIVE_MEDIUM LOADED");
                    OwnNative.getInstance(activity).ShowOwnNativeMedium(nativeAdContainer);
                } else {
                    firebaseEvent("QurekaNativeMedium Show ");
                    LogUtils.logE(TAG, "OWNADS_QUREKA=======>  QUREKA NATIVE_MEDIUM LOADED");
                    QuerkaNative.getInstance(activity).ShowQurekaMediumAd(nativeAdContainer);
                }
                break;
            case NATIVE_BIG_TOP:
                OwnBigTopCount++;
                if (OwnBigTopCount % 2 == 1) {
                    LogUtils.logE(TAG, "OWNADS_QUREKA=======>  OWN NATIVE_BIG_TOP LOADED");
                    OwnNative.getInstance(activity).ShowOwnNative(nativeAdContainer);
                } else {
                    LogUtils.logE(TAG, "OWNADS_QUREKA=======>  QUREKA NATIVE_BIG_TOP LOADED");
                    QuerkaNative.getInstance(activity).ShowQurekaBigAd(nativeAdContainer);
                }
                break;
        }
    }

    void HandleClick(Boolean AdShow) {
        AdConstant.isInterstitialShown = false;
        try {
            if (MyApplication.sharedPreferencesHelper.getAdShowingPopup()) {
                AdUtils.dismissAdLoading();
            } else {
                AdUtils.dismissAdProgress();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (handleClick != null) {
            handleClick.Show(AdShow);
            isAdClick = false;
            handleClick = null;
        }
    }

    private void ShowNetworkDialog() {
        Dialog dialog = new Dialog(activity);
        DialogInternetBinding dialogInternetBinding = DialogInternetBinding.inflate(activity.getLayoutInflater());
        dialog.setContentView(dialogInternetBinding.getRoot());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialogInternetBinding.buttonRetry.setOnClickListener(v -> {
            dialog.dismiss();
            Intent i = new Intent(Settings.ACTION_SETTINGS);
            activity.startActivity(i);
        });
        dialog.show();

    }
}