package com.iten.tenoku.activity;//package com.iten.tenoku.activity;
//
//import static com.google.android.gms.common.util.CollectionUtils.listOf;
//import static com.iten.tenoku.ad.AdMob.GoogleInterstitialAd.adMobInterstitialAds;
//import static com.iten.tenoku.ad.AppOpenManager.adMobOpenAdModels;
//import static com.iten.tenoku.ad.Facebook.FaceMetaInterstitialAd.facebookInterstitialAdModels;
//import static com.iten.tenoku.utils.AdConstant.adMobNativeAdBannerModels;
//
//import static com.iten.tenoku.utils.AdConstant.commonFeatureHideList;
//import static com.iten.tenoku.utils.AdConstant.facebookNativeAdModels;
//import static com.iten.tenoku.utils.AdConstant.facebookNativeBannerAdModels;
//
//import static com.iten.tenoku.utils.AdConstant.googleNativeAdModels;
//import static com.iten.tenoku.utils.MyApplication.sharedPreferencesHelper;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.pm.PackageManager;
//import android.os.Bundle;
//import android.telephony.TelephonyManager;
//import android.util.Log;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.facebook.FacebookSdk;
//import com.facebook.LoggingBehavior;
//import com.facebook.ads.AdSettings;
//import com.facebook.appevents.AppEventsLogger;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.MobileAds;
//import com.google.android.gms.ads.RequestConfiguration;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
//import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
//import com.google.gson.Gson;
//import com.google.gson.JsonArray;
//import com.iten.tenoku.BuildConfig;
//import com.iten.tenoku.ad.AdMob.GoogleInterstitialAd;
//import com.iten.tenoku.ad.AdMob.GoogleNativeBannerAd;
//import com.iten.tenoku.ad.AdMob.GoogleNativeBigAd;
//import com.iten.tenoku.ad.AdMob.GoogleNativeMediumAd;
//import com.iten.tenoku.ad.AppOpenManager;
//import com.iten.tenoku.ad.Facebook.FaceMetaInterstitialAd;
//import com.iten.tenoku.ad.Facebook.FaceMetaNativeBannerAd;
//import com.iten.tenoku.ad.Facebook.FaceMetaNativeBigAd;
//import com.iten.tenoku.ad.Facebook.FaceMetaNativeMediumAd;
//import com.iten.tenoku.listeners.AppSettingListeners;
//
//import com.iten.tenoku.model.FacebookInterstitialAdModel;
//import com.iten.tenoku.model.FacebookNativeAdModel;
//import com.iten.tenoku.model.FacebookNativeBannerAdModel;
//import com.iten.tenoku.model.GoogleInterstitialAdModel;
//import com.iten.tenoku.model.GoogleNativeAdModel;
//import com.iten.tenoku.model.GoogleNativeBannerAdModel;
//import com.iten.tenoku.model.GoogleOpenAdModel;
//import com.iten.tenoku.networking.AdsResponse;
//import com.iten.tenoku.networking.Api;
//import com.iten.tenoku.networking.RetrofitClient;
//import com.iten.tenoku.utils.AdConstant;
//import com.iten.tenoku.utils.AdUtils;
//import com.iten.tenoku.utils.LogUtils;
//import com.iten.tenoku.utils.MyApplication;
//import com.onesignal.OneSignal;
//
//import java.sql.Array;
//import java.util.Arrays;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class AppSettingActivity extends AppCompatActivity {
//
////    public static AdUtils.AdType AD_TYPE = AdUtils.AdType.ADMOB_FACEBOOK;
//
//    public static AdUtils.AdType AD_TYPE = AdUtils.AdType.FACEBOOK;
//
//    String TAG = "AppSettingActivity";
//    Api api;
//    Activity activity;
//    AppSettingListeners appSettingListeners = null;
//    int AppVersion;
//    Boolean debugMode;
//    String userCountry;
//    boolean isImageShow = false;
//    private String selectedCountryShowDifferentData = "";
//    private String selectedCountryAppOpenNativeHide = "";
//    private String selectedCountryNativeBigHide = "";
//    private String selectedCountryNativeBannerHide = "";
//
////    private static void setAdType(String getAlternate) {
////        if (getAlternate.contains("Facebook,Admob")) {
////            if (!sharedPreferencesHelper.getFacebookAdShow()) {
////                AD_TYPE = AdUtils.AdType.ADMOB;
////            } else if (!sharedPreferencesHelper.getAdMobAdShow()) {
////                AD_TYPE = AdUtils.AdType.FACEBOOK;
////            } else {
////                AD_TYPE = AdUtils.AdType.FACEBOOK_ADMOB;
////            }
////        } else if (getAlternate.contains("Admob,Facebook")) {
////            if (!sharedPreferencesHelper.getAdMobAdShow()) {
////                AD_TYPE = AdUtils.AdType.FACEBOOK;
////            } else if (!sharedPreferencesHelper.getFacebookAdShow()) {
////                AD_TYPE = AdUtils.AdType.ADMOB;
////            } else {
////                AD_TYPE = AdUtils.AdType.ADMOB_FACEBOOK;
////            }
////        } else if (getAlternate.contains("Admob")) {
////            AD_TYPE = AdUtils.AdType.ADMOB;
////        } else if (getAlternate.contains("Facebook")) {
////            AD_TYPE = AdUtils.AdType.FACEBOOK;
////        }
////    }
//
//    private static void setAdType(String getAlternate) {
//        if (getAlternate.contains("Facebook,Admob")) {
//            LogUtils.logE("AppSettingActivity"," contains FB GOOGLE ");
//            if (!sharedPreferencesHelper.getFacebookAdShow()) {
//                LogUtils.logE("AppSettingActivity"," FACEBOOK IS FALSE");
//                AD_TYPE = AdUtils.AdType.ADMOB;
//            } else if (!sharedPreferencesHelper.getAdMobAdShow()) {
//                LogUtils.logE("AppSettingActivity"," GOOGLE IS FALSE ");
//                AD_TYPE = AdUtils.AdType.FACEBOOK;
//            } else {
//                LogUtils.logE("AppSettingActivity"," APLLYING FB, GOOGLE ");
//                AD_TYPE = AdUtils.AdType.FACEBOOK_ADMOB;
//            }
//        } else if (getAlternate.contains("Admob,Facebook")) {
//            if (!sharedPreferencesHelper.getAdMobAdShow()) {
//                AD_TYPE = AdUtils.AdType.FACEBOOK;
//            } else if (!sharedPreferencesHelper.getFacebookAdShow()) {
//                AD_TYPE = AdUtils.AdType.ADMOB;
//            } else {
//                AD_TYPE = AdUtils.AdType.ADMOB_FACEBOOK;
//            }
//        } else if (getAlternate.contains("Admob")) {
//            AD_TYPE = AdUtils.AdType.ADMOB;
//        } else if (getAlternate.contains("Facebook")) {
//            AD_TYPE = AdUtils.AdType.FACEBOOK;
//        }
//    }
//
//
//    public static boolean checkInstallation(Context context, String packageName) {
//
//        PackageManager pm = context.getPackageManager();
//        try {
//            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
//            return true;
//        } catch (PackageManager.NameNotFoundException e) {
//            return false;
//        }
//    }
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        AppOpenManager.Splash_Start = true;
//    }
//
//
//    public void AppSettings(Activity activity, String applicationId, AppSettingListeners settingListeners) {
//        this.activity = activity;
//        this.appSettingListeners = settingListeners;
//
//        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
//        userCountry = tm.getNetworkCountryIso();
//
//
//        api = RetrofitClient.getInstance().getApi();
//        Call<AdsResponse> appSettingsCall = api.addapikey(applicationId);
//        appSettingsCall.enqueue(new Callback<AdsResponse>() {
//            @Override
//            public void onResponse(@NonNull Call<AdsResponse> call, @NonNull Response<AdsResponse> response) {
//                LogUtils.logE(TAG, "onResponse------->>>>>>>: ");
//
//                if (response.isSuccessful()) {
//                    sharedPreferencesHelper.setAppInstallStatus(false);
//                    String data = new Gson().toJson(response.body());
//                    AdsResponse appSettings = new Gson().fromJson(data, AdsResponse.class);
//                    if (appSettings.getIsStatus()) {
//                        sharedPreferencesHelper.setResponse(data);
//                        CheckBooleanValue(appSettings);
//                        CheckStringValue(appSettings);
//                        CheckIntegerValue(appSettings);
//                        SetAdColors(appSettings);
//                        SetRecyclerViewAd(sharedPreferencesHelper.getGridViewPerItemAdTwo(), sharedPreferencesHelper.getGridViewPerItemAdThree(), sharedPreferencesHelper.getListViewAd());
//                        SetAdData(appSettings);
//                        SetHideFeature(appSettings);
//                        SetUserData(appSettings);
//                        FacebookAd(appSettings);
//                        preLoadAd();
//                        HandelData(sharedPreferencesHelper.getUnderMaintenance());
//
//                    } else {
//                        if (appSettingListeners != null) {
//                            appSettingListeners.onStatusChange();
//                            appSettingListeners = null;
//                        }
//                    }
//                } else {
//                    if (appSettingListeners != null) {
//                        appSettingListeners.onResponseFail();
//                        appSettingListeners = null;
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<AdsResponse> call, @NonNull Throwable t) {
//                LogUtils.logE(TAG, "onFailure: " + t.getMessage());
//                if (appSettingListeners != null) {
//                    appSettingListeners.onResponseFail();
//                    appSettingListeners = null;
//                }
//            }
//        });
//
//    }
//
//
//    public void CheckBooleanValue(AdsResponse appSettings) {
//        sharedPreferencesHelper.setVersionUpdateDialog(appSettings.data.getObjAPPSETTINGS().getIsVersionUpdateDialog());
//        sharedPreferencesHelper.setRedirectOtherApp(appSettings.data.getObjAPPSETTINGS().getIsRedirecToTherApp());
//        sharedPreferencesHelper.setShowAdinApp(this, appSettings.data.getObjAPPSETTINGS().getIsShowadInApp());
////
////        sharedPreferencesHelper.setShowAdinApp(this, false);
//
//        sharedPreferencesHelper.setAdMobAdShow(appSettings.data.getObjPLACEMENT().getObjAdmob().getIsAdShowAdStatus());
//        sharedPreferencesHelper.setOpenAdStatus(appSettings.data.getObjAPPSETTINGS().getIsAltIntAppOpenStuse());
//        sharedPreferencesHelper.setAMDStatus(appSettings.data.getObjAPPSETTINGS().getIsAMDClickStatus());
//        sharedPreferencesHelper.setAppOpenAd(appSettings.data.getObjAPPSETTINGS().getIsAppopenad());
//        sharedPreferencesHelper.setUnderMaintenance(Boolean.parseBoolean(appSettings.data.getObjPLACEMENT().objCommonSettings.getIsUnderMaintenance()));
//
//        sharedPreferencesHelper.setShowAppInHouse(appSettings.data.getObjAPPSETTINGS().getIsShowAppInHouse());
//        sharedPreferencesHelper.setQurekaLite(appSettings.data.getObjPLACEMENT().getObjQureka().getIsQurekaLiteShow());
////
////        sharedPreferencesHelper.setQurekaLite(true);
//
//        sharedPreferencesHelper.setQurekaClose(appSettings.data.getObjPLACEMENT().getObjQureka().getIsQurekaClose());
//        sharedPreferencesHelper.setExitScreen(Boolean.parseBoolean(appSettings.data.getObjPLACEMENT().getObjCommonSettings().getIsExitScreen()));
//        sharedPreferencesHelper.setOnAdsResume(appSettings.data.getObjPLACEMENT().getObjCommonSettings().getIsOnResume());
//        sharedPreferencesHelper.setPreload(appSettings.data.getObjPLACEMENT().getObjCommonSettings().getIsPreload());
//
//
//        if (!sharedPreferencesHelper.getPreload()) {
//            sharedPreferencesHelper.setAdShowingPopup(true);
//        } else {
//            sharedPreferencesHelper.setAdShowingPopup(appSettings.data.getObjAPPSETTINGS().getIsAdshowingpopup());
//        }
//        if (checkInstallation(this, "com.facebook.katana") || checkInstallation(this, "com.facebook.lite")) {
//
//            LogUtils.logE(TAG, "CheckBooleanValue Facebook :  " );
//            sharedPreferencesHelper.setFacebookAdShow(appSettings.data.getObjPLACEMENT().getObjFacebook().getIsAdShowAdStatus());
////            sharedPreferencesHelper.setFacebookAdShow(true);
//        } else {
//            LogUtils.logE(TAG, "CheckBooleanValue Facebook else : " );
////            sharedPreferencesHelper.setFacebookAdShow(true);
//            sharedPreferencesHelper.setFacebookAdShow(false);
//        }
//
//        AdConstant.isResume = !sharedPreferencesHelper.getAppOpenAd();
//    }
//
//    public void CheckStringValue(AdsResponse appSettings) {
//
//        AdConstant.adcollapsiblekey = appSettings.data.getObjPLACEMENT().getObjAdmob().getiBannerID().trim();
//        sharedPreferencesHelper.setAppAccountLink(appSettings.data.getObjAPPSETTINGS().getVRedirecToTherAppPackage().trim());
//        sharedPreferencesHelper.setFacebookAppId(appSettings.data.getObjPLACEMENT().getObjFacebook().getIFacebookAppID().trim());
//        sharedPreferencesHelper.setFacebookClientToken(appSettings.data.getObjPLACEMENT().getObjFacebook().getVFacebookClientToken().trim());
//        sharedPreferencesHelper.setVersionCode(appSettings.data.getObjAPPSETTINGS().getVVersionCode().trim());
//        sharedPreferencesHelper.setPrivacyPolicy(appSettings.data.getObjAPPSETTINGS().getVPrivacyPolicy());
//        sharedPreferencesHelper.setQurekaLink(appSettings.data.getObjPLACEMENT().getObjQureka().getVQurekaLink().trim());
//        sharedPreferencesHelper.setPredChampLink(appSettings.data.getObjPLACEMENT().getObjQureka().getVPredchampLink().trim());
//
//        sharedPreferencesHelper.setAlternateAdShow(appSettings.data.getObjAPPSETTINGS().getVAlernateAdShow().trim());
////
////        sharedPreferencesHelper.setAlternateAdShow("Facebook,Admob");
//
////        sharedPreferencesHelper.setAlternateAdShow("");
//
//        sharedPreferencesHelper.setFixSequence(appSettings.data.getObjAPPSETTINGS().getVAppSequence().trim());
////
////        sharedPreferencesHelper.setFixSequence("");
//
//        sharedPreferencesHelper.setNotificationId(appSettings.data.getObjAPPSETTINGS().getVNotificationId().trim());
//
//        if (sharedPreferencesHelper.getBoolean("IsNotificationEnable", true)) {
//            MyApplication.getInstance().setOneSignal(sharedPreferencesHelper.getNotificationId());
//            OneSignal.disablePush(false);
//        } else {
//            OneSignal.disablePush(true);
//        }
//
//
//
//        if (!MyApplication.sharedPreferencesHelper.getAlternateAdShow().isEmpty()) {
//            LogUtils.logE(TAG," getAlternateAdShow().isEmpty() NOT ");
//            String getAlternate = MyApplication.sharedPreferencesHelper.getAlternateAdShow();
//            setAdType(getAlternate);
//        } else if (!MyApplication.sharedPreferencesHelper.getFixSequence().isEmpty()) {
//            LogUtils.logE(TAG," getFixSequence().isEmpty() NOT ");
//            String getFixSequence = MyApplication.sharedPreferencesHelper.getFixSequence();
//            setAdType(getFixSequence);
//        } else {
//            LogUtils.logE(TAG," ELSE");
//            if (!sharedPreferencesHelper.getFacebookAdShow()) {
//                AD_TYPE = AdUtils.AdType.ADMOB;
//            } else if (!sharedPreferencesHelper.getAdMobAdShow()) {
//                AD_TYPE = AdUtils.AdType.FACEBOOK;
//            } else {
//                AD_TYPE = AdUtils.AdType.ADMOB_FACEBOOK;
//            }
//        }
//
//
//    }
//
//    public void CheckIntegerValue(AdsResponse appSettings) {
//        if (appSettings.data.getObjAPPSETTINGS().getIIntCounter() != null && !appSettings.data.getObjAPPSETTINGS().getIIntCounter().trim().isEmpty()) {
//            sharedPreferencesHelper.setMainClick(Integer.parseInt(appSettings.data.getObjAPPSETTINGS().getIIntCounter().trim()));
//        }
//        if (appSettings.data.getObjAPPSETTINGS().getIIntBackCounter() != null && !appSettings.data.getObjAPPSETTINGS().getIIntBackCounter().trim().isEmpty()) {
//            sharedPreferencesHelper.setBackClick(Integer.parseInt(appSettings.data.getObjAPPSETTINGS().getIIntBackCounter().trim()));
//        }
////        sharedPreferencesHelper.setInterNativeAdSequenceCounter(Integer.parseInt(appSettings.pLACEMENT.CommonSettings.getInterNativeAdSequenceCounter().trim()));
////        sharedPreferencesHelper.setRefreshCount(Integer.parseInt(appSettings.pLACEMENT.CommonSettings.geRefreshCounter().trim()) * 1000);
//
//        if (appSettings.data.getObjPLACEMENT().getObjCommonSettings().getIShowEntryScreen() != null && !appSettings.data.getObjPLACEMENT().getObjCommonSettings().getIShowEntryScreen().trim().isEmpty()) {
//            sharedPreferencesHelper.setShowEntryScreen(Integer.parseInt(appSettings.data.getObjPLACEMENT().getObjCommonSettings().getIShowEntryScreen().trim()));
//        }
//        if (appSettings.data.getObjPLACEMENT().getObjCommonSettings().getIGridRecyclerviewPerItemTwo() != null && !appSettings.data.getObjPLACEMENT().getObjCommonSettings().getIGridRecyclerviewPerItemTwo().trim().isEmpty()) {
//            sharedPreferencesHelper.setGridViewPerItemAdTwo(Integer.parseInt(appSettings.data.getObjPLACEMENT().getObjCommonSettings().getIGridRecyclerviewPerItemTwo().trim()));
//        }
//        if (appSettings.data.getObjPLACEMENT().getObjCommonSettings().getIGridRecyclerviewPeritemThree() != null && !appSettings.data.getObjPLACEMENT().getObjCommonSettings().getIGridRecyclerviewPeritemThree().trim().isEmpty()) {
//            sharedPreferencesHelper.setGridViewPerItemAdThree(Integer.parseInt(appSettings.data.getObjPLACEMENT().getObjCommonSettings().getIGridRecyclerviewPeritemThree().trim()));
//        }
//        if (appSettings.data.getObjPLACEMENT().getObjCommonSettings().getIListRecyclerviewPerItem() != null && !appSettings.data.getObjPLACEMENT().getObjCommonSettings().getIListRecyclerviewPerItem().trim().isEmpty()) {
//            sharedPreferencesHelper.setListViewAd(Integer.parseInt(appSettings.data.getObjPLACEMENT().getObjCommonSettings().getIListRecyclerviewPerItem().trim()));
//        }
//        if (appSettings.data.getObjPLACEMENT().getObjCommonSettings().getIShowInterstitialClickCount() != null && !appSettings.data.getObjPLACEMENT().getObjCommonSettings().getIShowInterstitialClickCount().trim().isEmpty()) {
//            sharedPreferencesHelper.setShowInterstitialClickCount(Integer.parseInt(appSettings.data.getObjPLACEMENT().getObjCommonSettings().getIShowInterstitialClickCount().trim()));
//        }
//        if (appSettings.data.getObjPLACEMENT().getObjQureka().getIQurekaShowCount() != null && !appSettings.data.getObjPLACEMENT().getObjQureka().getIQurekaShowCount().trim().isEmpty()) {
//            AdConstant.QUREKA_VIEW_PER_AD = Integer.parseInt(appSettings.data.getObjPLACEMENT().getObjQureka().getIQurekaShowCount().trim());
//        } else {
//            AdConstant.QUREKA_VIEW_PER_AD = 0;
//        }
//
//    }
//
//    public void SetAdColors(AdsResponse appSettings) {
//        //title Color
//        if (appSettings.data.getObjPLACEMENT().getObjCommonSettings().getVAdTitleColor() != null) {
//            if (appSettings.data.getObjPLACEMENT().getObjCommonSettings().getVAdTitleColor().trim().isEmpty() || !appSettings.data.getObjPLACEMENT().getObjCommonSettings().getVAdTitleColor().trim().matches("^#([a-fA-F0-9]{6})$")) {
//                sharedPreferencesHelper.setAdTitleColor("");
//            } else {
//                sharedPreferencesHelper.setAdTitleColor(appSettings.data.getObjPLACEMENT().getObjCommonSettings().getVAdTitleColor().trim());
//            }
//        } else {
//            sharedPreferencesHelper.setAdTitleColor("");
//        }
//
//        //Description Color
//        if (appSettings.data.getObjPLACEMENT().getObjCommonSettings().getVAdDiscColor() != null) {
//            if (appSettings.data.getObjPLACEMENT().getObjCommonSettings().getVAdDiscColor().trim().isEmpty() || !appSettings.data.getObjPLACEMENT().getObjCommonSettings().getVAdDiscColor().trim().matches("^#([a-fA-F0-9]{6})$")) {
//                sharedPreferencesHelper.setAdDescriptionColor("");
//            } else {
//                sharedPreferencesHelper.setAdDescriptionColor(appSettings.data.getObjPLACEMENT().getObjCommonSettings().getVAdDiscColor().trim());
//            }
//        } else {
//            sharedPreferencesHelper.setAdDescriptionColor("");
//        }
//
//        //button text Color
//        if (appSettings.data.getObjPLACEMENT().getObjCommonSettings().getVAdButtonTextColor() != null) {
//            if (appSettings.data.getObjPLACEMENT().getObjCommonSettings().getVAdButtonTextColor().trim().isEmpty() || !appSettings.data.getObjPLACEMENT().getObjCommonSettings().getVAdButtonTextColor().trim().matches("^#([a-fA-F0-9]{6})$")) {
//                sharedPreferencesHelper.setAdButtonTextColor("");
//            } else {
//                sharedPreferencesHelper.setAdButtonTextColor(appSettings.data.getObjPLACEMENT().getObjCommonSettings().getVAdButtonTextColor().trim());
//            }
//        } else {
//            sharedPreferencesHelper.setAdButtonTextColor("");
//        }
//
//        //bg Color
//        if (appSettings.data.getObjPLACEMENT().getObjCommonSettings().getVAdBgColor() != null) {
//
//            if (appSettings.data.getObjPLACEMENT().getObjCommonSettings().getVAdBgColor().trim().contains(",") && !appSettings.data.getObjPLACEMENT().getObjCommonSettings().getVAdBgColor().trim().isEmpty()) {
//                String[] adBgColor = appSettings.data.getObjPLACEMENT().getObjCommonSettings().getVAdBgColor().trim().split(",");
//                if (appSettings.data.getObjPLACEMENT().getObjCommonSettings().getVAdBgColor().trim().isEmpty() || !adBgColor[1].matches("^#([a-fA-F0-9]{6})$")) {
//                    sharedPreferencesHelper.setAddBgColor("");
//                    sharedPreferencesHelper.setAdBgColorOpacity(-1);
//                } else {
//                    sharedPreferencesHelper.setAdBgColorOpacity(Integer.parseInt(adBgColor[0].trim()));
//                    sharedPreferencesHelper.setAddBgColor(adBgColor[1].trim());
//                }
//            } else {
//                if (appSettings.data.getObjPLACEMENT().getObjCommonSettings().getVAdBgColor().trim().isEmpty() || !appSettings.data.getObjPLACEMENT().getObjCommonSettings().getVAdBgColor().trim().matches("^#([a-fA-F0-9]{6})$")) {
//                    sharedPreferencesHelper.setAddBgColor("");
//                    sharedPreferencesHelper.setAdBgColorOpacity(-1);
//                } else {
//                    sharedPreferencesHelper.setAdBgColorOpacity(-1);
//                    sharedPreferencesHelper.setAddBgColor(appSettings.data.getObjPLACEMENT().getObjCommonSettings().getVAdBgColor().trim());
//                }
//            }
//
//        } else {
//            sharedPreferencesHelper.setAddBgColor("");
//        }
//
//        //button Color
//        if (appSettings.data.getObjPLACEMENT().getObjCommonSettings().getVAdButtonColor() != null) {
//
//            if (appSettings.data.getObjPLACEMENT().getObjCommonSettings().getVAdButtonColor().trim().contains(",") && !appSettings.data.getObjPLACEMENT().getObjCommonSettings().getVAdButtonColor().trim().isEmpty()) {
//                String[] adBgColor = appSettings.data.getObjPLACEMENT().getObjCommonSettings().getVAdButtonColor().trim().split(",");
//                if (appSettings.data.getObjPLACEMENT().getObjCommonSettings().getVAdButtonColor().trim().isEmpty() || !adBgColor[1].matches("^#([a-fA-F0-9]{6})$")) {
//                    sharedPreferencesHelper.setAdButtonColor("");
//                    sharedPreferencesHelper.setAdButtonColorOpacity(-1);
//                } else {
//                    sharedPreferencesHelper.setAdButtonColorOpacity(Integer.parseInt(adBgColor[0].trim()));
//                    sharedPreferencesHelper.setAdButtonColor(adBgColor[1].trim());
//                }
//            } else {
//                if (appSettings.data.getObjPLACEMENT().getObjCommonSettings().getVAdButtonColor().trim().isEmpty() || !appSettings.data.getObjPLACEMENT().getObjCommonSettings().getVAdButtonColor().trim().matches("^#([a-fA-F0-9]{6})$")) {
//                    sharedPreferencesHelper.setAdButtonColor("");
//                    sharedPreferencesHelper.setAdButtonColorOpacity(-1);
//                } else {
//                    sharedPreferencesHelper.setAdButtonColorOpacity(-1);
//                    sharedPreferencesHelper.setAdButtonColor(appSettings.data.getObjPLACEMENT().getObjCommonSettings().getVAdButtonColor().trim());
//                }
//            }
//
//        } else {
//            sharedPreferencesHelper.setAdButtonColor("");
//        }
//    }
//
//    public void SetRecyclerViewAd(int gridViewPerItemAdTwo, int gridViewPerItemAdThree, int listViewAd) {
//        if (AD_TYPE == AdUtils.AdType.ADMOB_FACEBOOK || AD_TYPE == AdUtils.AdType.FACEBOOK_ADMOB) {
//            if (!sharedPreferencesHelper.getAdMobAdShow() && !sharedPreferencesHelper.getFacebookAdShow() && !sharedPreferencesHelper.getQurekaLite()) {
//                isImageShow = true;
//                sharedPreferencesHelper.setShowAdinApp(this, false);
//            }
//        } else if (AD_TYPE == AdUtils.AdType.ADMOB) {
//            if (!sharedPreferencesHelper.getAdMobAdShow() && !sharedPreferencesHelper.getQurekaLite()) {
//                isImageShow = true;
//                sharedPreferencesHelper.setShowAdinApp(this, false);
//            }
//        } else if (AD_TYPE == AdUtils.AdType.FACEBOOK) {
//            if (!sharedPreferencesHelper.getFacebookAdShow() && !sharedPreferencesHelper.getQurekaLite()) {
//                isImageShow = true;
//                sharedPreferencesHelper.setShowAdinApp(this, false);
//            }
//        }
//
//        if (!isImageShow) {
//            if (gridViewPerItemAdThree != 0) {
//                AdConstant.GRID_VIEW_PER_THREE_ITEM_AD = gridViewPerItemAdThree;
//                AdConstant.GRID_VIEW_PER_THREE_ITEM_AD_MULTIPLY = gridViewPerItemAdThree * 2 - 1;
//            } else {
//                AdConstant.GRID_VIEW_PER_THREE_ITEM_AD = 0;
//                AdConstant.GRID_VIEW_PER_THREE_ITEM_AD_MULTIPLY = 0;
//            }
//            if (gridViewPerItemAdTwo != 0) {
//                AdConstant.GRID_VIEW_PER_TWO_ITEM_AD = gridViewPerItemAdTwo;
//                AdConstant.GRID_VIEW_PER_TWO_ITEM_AD_MULTIPLY = gridViewPerItemAdTwo * 2 - 1;
//            } else {
//                AdConstant.GRID_VIEW_PER_TWO_ITEM_AD = 0;
//                AdConstant.GRID_VIEW_PER_TWO_ITEM_AD_MULTIPLY = 0;
//            }
//            AdConstant.LIST_VIEW_PER_AD = listViewAd;
//        }
//    }
//
//    public void SetAdData(AdsResponse appSettings) {
//        adMobOpenAdModels.clear();
//        facebookNativeAdModels.clear();
//        googleNativeAdModels.clear();
//        adMobInterstitialAds.clear();
//        adMobNativeAdBannerModels.clear();
//        facebookInterstitialAdModels.clear();
//        facebookNativeBannerAdModels.clear();
//
//
//        if (appSettings.data.getObjAPPSETTINGS().isShowadInApp) {
//
////            if (true) {
//            if (appSettings.data.getObjPLACEMENT().getObjFacebook().getIsAdShowAdStatus()) {
//                for (int i = 0; i < appSettings.data.getObjPLACEMENT().getObjFacebook().getArrNative().size(); i++) {
//                    if (!appSettings.data.getObjPLACEMENT().getObjFacebook().getArrNative().get(i).getIId().trim().isEmpty()) {
//                        FacebookNativeAdModel facebookNativeAdModel = new FacebookNativeAdModel(appSettings.data.getObjPLACEMENT().getObjFacebook().getArrNative().get(i).getIId().trim(), null);
//                        facebookNativeAdModels.add(facebookNativeAdModel);
//                    }
//                }
//                for (int i = 0; i < appSettings.data.getObjPLACEMENT().getObjFacebook().getArrNativeBanner().size(); i++) {
//                    if (!appSettings.data.getObjPLACEMENT().getObjFacebook().getArrNativeBanner().get(i).getIId().trim().isEmpty()) {
//                        FacebookNativeBannerAdModel facebookNativeBannerAdModel = new FacebookNativeBannerAdModel(appSettings.data.getObjPLACEMENT().getObjFacebook().getArrNativeBanner().get(i).getIId().trim(), null);
//                        facebookNativeBannerAdModels.add(facebookNativeBannerAdModel);
//                    }
//                }
//
//                for (int i = 0; i < appSettings.data.getObjPLACEMENT().getObjFacebook().getArrInterstitial().size(); i++) {
//                    if (!appSettings.data.getObjPLACEMENT().getObjFacebook().getArrInterstitial().get(i).getIId().trim().isEmpty()) {
//                        FacebookInterstitialAdModel facebookInterstitialAdModel = new FacebookInterstitialAdModel(appSettings.data.getObjPLACEMENT().getObjFacebook().getArrInterstitial().get(i).getIId().trim(), null);
//                        facebookInterstitialAdModels.add(facebookInterstitialAdModel);
//                    }
//                }
//            }
//
//            if (appSettings.data.getObjPLACEMENT().getObjAdmob().getIsAdShowAdStatus()) {
//                for (int i = 0; i < appSettings.data.getObjPLACEMENT().getObjAdmob().getArrAppOpen().size(); i++) {
//                    if (!appSettings.data.getObjPLACEMENT().getObjAdmob().getArrAppOpen().get(i).getIId().trim().isEmpty()) {
//                        GoogleOpenAdModel adMobOpenAdModel = new GoogleOpenAdModel(appSettings.data.getObjPLACEMENT().getObjAdmob().getArrAppOpen().get(i).getIId().trim(), null, 0);
//                        adMobOpenAdModels.add(adMobOpenAdModel);
//                    }
//                }
//                for (int i = 0; i < appSettings.data.getObjPLACEMENT().getObjAdmob().getArrNative().size(); i++) {
//                    if (!appSettings.data.getObjPLACEMENT().getObjAdmob().getArrNative().get(i).getIId().trim().isEmpty()) {
//                        GoogleNativeAdModel adMobNativeAdModel = new GoogleNativeAdModel(appSettings.data.getObjPLACEMENT().getObjAdmob().getArrNative().get(i).getIId().trim(), null);
//                        googleNativeAdModels.add(adMobNativeAdModel);
//                    }
//                }
//
//                for (int i = 0; i < appSettings.data.getObjPLACEMENT().getObjAdmob().getArrNative().size(); i++) {
//                    if (!appSettings.data.getObjPLACEMENT().getObjAdmob().getArrNative().get(i).getIId().trim().isEmpty()) {
//                        GoogleNativeBannerAdModel adMobNativeBannerAdModel = new GoogleNativeBannerAdModel(appSettings.data.getObjPLACEMENT().getObjAdmob().getArrNative().get(i).getIId().trim(), null);
//                        adMobNativeAdBannerModels.add(adMobNativeBannerAdModel);
//                    }
//                }
//
//                for (int i = 0; i < appSettings.data.getObjPLACEMENT().getObjAdmob().getArrInterstitial().size(); i++) {
//                    if (!appSettings.data.getObjPLACEMENT().getObjAdmob().getArrInterstitial().get(i).getIId().trim().isEmpty()) {
//                        GoogleInterstitialAdModel adMobInterstitialAdModel = new GoogleInterstitialAdModel(appSettings.data.getObjPLACEMENT().getObjAdmob().getArrInterstitial().get(i).getIId().trim(), null);
//                        adMobInterstitialAds.add(adMobInterstitialAdModel);
//                    }
//                }
//            }
////            if (appSettings.data.getObjAPPSETTINGS().getIsShowAppInHouse()) {
////                isShowAppInHouse = appSettings.data.getObjAPPSETTINGS().getIsShowAppInHouse();
////            }
////            if (true) {
//            if (appSettings.data.getObjPLACEMENT().getObjQureka().getIsQurekaLiteShow()) {
//                for (int i = 0; i < appSettings.data.getObjPLACEMENT().getObjQureka().getArrIconUrls().size(); i++) {
//                    AdConstant.iconUrlList.add(appSettings.data.getObjPLACEMENT().getObjQureka().getArrIconUrls().get(i).getIId().trim());
//                }
//                for (int i = 0; i < appSettings.data.getObjPLACEMENT().getObjQureka().getArrQurekaLinks().size(); i++) {
//                    AdConstant.qurekaLinksList.add(appSettings.data.getObjPLACEMENT().getObjQureka().getArrQurekaLinks().get(i).getIId().trim());
//                }
//                for (int i = 0; i < appSettings.data.getObjPLACEMENT().getObjQureka().getArrInterstitialImageUrls().size(); i++) {
//                    AdConstant.interstitialImagesUrlList.add(appSettings.data.getObjPLACEMENT().getObjQureka().getArrInterstitialImageUrls().get(i).getIId().trim());
//                }
//                for (int i = 0; i < appSettings.data.getObjPLACEMENT().getObjQureka().getArrNativeImageUrls().size(); i++) {
//                    AdConstant.nativeImagesUrlList.add(appSettings.data.getObjPLACEMENT().getObjQureka().getArrNativeImageUrls().get(i).getIId().trim());
//                }
//                if (AdConstant.iconUrlList.isEmpty()) {
//                    sharedPreferencesHelper.setQurekaLite(false);
//                }
//            }
//        } else {
//            SetRecyclerViewAd(0, 0, 0);
//            sharedPreferencesHelper.setQurekaLite(false);
//        }
//
//    }
//
//    public void SetHideFeature(AdsResponse appSettings) {
//        commonFeatureHideList.clear();
//        if (appSettings.data.getObjPLACEMENT().getObjCommonSettings().getVCommonFeatureHide() != null) {
//            for (String feature : appSettings.data.getObjPLACEMENT().getObjCommonSettings().getVCommonFeatureHide().split(",")) {
//                commonFeatureHideList.add(feature.trim());
//            }
//        }
//    }
//
//    public void SetUserData(AdsResponse appSettings) {
//        if (appSettings.data.getObjPLACEMENT().getObjCommonSettings().getVClickCountryList() != null) {
//            selectedCountryShowDifferentData = "" + appSettings.data.getObjPLACEMENT().getObjCommonSettings().getVClickCountryList().trim();
//        }
//        if (appSettings.data.getObjPLACEMENT().getObjCommonSettings().getVAppOpenNativeHideCountryList() != null) {
//            selectedCountryAppOpenNativeHide = "" + appSettings.data.getObjPLACEMENT().getObjCommonSettings().getVAppOpenNativeHideCountryList().trim();
//        }
//        if (appSettings.data.getObjPLACEMENT().getObjCommonSettings().getVNativeBigHideCountryList() != null) {
//            selectedCountryNativeBigHide = "" + appSettings.data.getObjPLACEMENT().getObjCommonSettings().getVNativeBigHideCountryList().trim();
//        }
//        if (appSettings.data.getObjPLACEMENT().getObjCommonSettings().getVNativeBannerHideCountryList() != null) {
//            selectedCountryNativeBannerHide = "" + appSettings.data.getObjPLACEMENT().getObjCommonSettings().getVNativeBannerHideCountryList().trim();
//        }
//
//
//        //AppOpen
//        if (selectedCountryAppOpenNativeHide.trim().equalsIgnoreCase("ALL")) {
//            sharedPreferencesHelper.setAppOpenAd(false);
//            adMobOpenAdModels.clear();
//        } else {
//            for (String country : selectedCountryAppOpenNativeHide.split(",")) {
//                if (country.trim().equalsIgnoreCase(userCountry)) {
//                    sharedPreferencesHelper.setAppOpenAd(false);
//                    adMobOpenAdModels.clear();
//                    break;
//                }
//                if (appSettings.data.getObjAPPSETTINGS().getIsAppopenad()) {
//                    sharedPreferencesHelper.setAppOpenAd(true);
//                }
//            }
//
//        }
//
//
//        //NativeBig
//        if (selectedCountryNativeBigHide.trim().equalsIgnoreCase("ALL")) {
//            sharedPreferencesHelper.setNativeBigAdShow(false);
//        } else {
//            for (String country : selectedCountryNativeBigHide.split(",")) {
//                if (country.trim().equalsIgnoreCase(userCountry)) {
//                    sharedPreferencesHelper.setNativeBigAdShow(false);
//                    break;
//                }
//                sharedPreferencesHelper.setNativeBigAdShow(true);
//            }
//        }
//
//        //NativeBanner
//        if (selectedCountryNativeBannerHide.trim().equalsIgnoreCase("ALL")) {
//            sharedPreferencesHelper.setNativeBannerAdShow(false);
//        } else {
//            for (String country : selectedCountryNativeBannerHide.split(",")) {
//                if (country.trim().equalsIgnoreCase(userCountry)) {
//                    sharedPreferencesHelper.setNativeBannerAdShow(false);
//                    break;
//                }
//                sharedPreferencesHelper.setNativeBannerAdShow(true);
//            }
//        }
//
//        if (googleNativeAdModels.isEmpty()) {
//            if (sharedPreferencesHelper.getAdMobAdShow()) {
//                sharedPreferencesHelper.setNativeAdShow(false);
//                SetRecyclerViewAd(0, 0, 0);
//            }
//        } else {
//            sharedPreferencesHelper.setNativeAdShow(true);
//        }
//        if (facebookNativeAdModels.isEmpty()) {
//            if (sharedPreferencesHelper.getFacebookAdShow()) {
//                sharedPreferencesHelper.setNativeAdShow(false);
//                SetRecyclerViewAd(0, 0, 0);
//            }
//        } else {
//            sharedPreferencesHelper.setNativeAdShow(true);
//        }
//
//
//        sharedPreferencesHelper.setInterstitialAdShow(!adMobInterstitialAds.isEmpty());
//        sharedPreferencesHelper.setFacebookInterstitialAdShow(!facebookInterstitialAdModels.isEmpty());
//
//        //Click Country List
//        if (selectedCountryShowDifferentData.equalsIgnoreCase("ALL")) {
//            sharedPreferencesHelper.setMainClick(sharedPreferencesHelper.getShowInterstitialClickCount());
//            sharedPreferencesHelper.setBackClick(sharedPreferencesHelper.getShowInterstitialClickCount());
//            sharedPreferencesHelper.setShowCountyClick(true);
//            sharedPreferencesHelper.setShowEntryScreen(0);
//            sharedPreferencesHelper.setCountyClick(true);
//            sharedPreferencesHelper.setQurekaLite(false);
//            SetRecyclerViewAd(0, 0, 0);
//            SetHideFeature(appSettings);
//        } else {
//            for (String country : selectedCountryShowDifferentData.split(",")) {
//                if (country.trim().equalsIgnoreCase(userCountry)) {
//                    sharedPreferencesHelper.setMainClick(sharedPreferencesHelper.getShowInterstitialClickCount());
//                    sharedPreferencesHelper.setBackClick(sharedPreferencesHelper.getShowInterstitialClickCount());
//                    sharedPreferencesHelper.setShowCountyClick(true);
//                    sharedPreferencesHelper.setShowEntryScreen(0);
//                    sharedPreferencesHelper.setCountyClick(true);
//                    sharedPreferencesHelper.setQurekaLite(false);
//                    SetRecyclerViewAd(0, 0, 0);
//                    SetHideFeature(appSettings);
//                    break;
//                }
//                sharedPreferencesHelper.setCountyClick(false);
//                sharedPreferencesHelper.setShowCountyClick(false);
//                commonFeatureHideList.clear();
//            }
//        }
//
//    }
//
//    public void FacebookAd(AdsResponse appSettings) {
//        FacebookSdk.setApplicationId(appSettings.data.getObjPLACEMENT().getObjFacebook().getIFacebookAppID());
//        FacebookSdk.sdkInitialize(activity);
//        FacebookSdk.setClientToken(appSettings.data.getObjPLACEMENT().getObjFacebook().getVFacebookClientToken());
//
//        FacebookSdk.setAutoInitEnabled(true);
//        FacebookSdk.fullyInitialize();
//        FacebookSdk.setAutoLogAppEventsEnabled(true);
//
//        if (BuildConfig.DEBUG) {
//            AdSettings.setTestMode(true);
//            FacebookSdk.setIsDebugEnabled(true);
//        }
//        FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS);
//        AppEventsLogger logger = AppEventsLogger.newLogger(activity);
//        logger.getApplicationId();
//        logger.logEvent(getClass().getSimpleName());
//    }
//
//    public void preLoadAd() {
//        if (sharedPreferencesHelper.getPreload()) {
//            GoogleInterstitialAd.getInstance(activity).LoadInterstitialAd();
//            FaceMetaInterstitialAd.getInstance(activity).LoadInterstitialAd();
//
//            GoogleNativeBigAd.getInstance(activity).LoadNativeBigAds();
//            GoogleNativeMediumAd.getInstance(activity).LoadNativeMediumAds();
//            GoogleNativeBannerAd.getInstance(activity).loadNativeBannerAds();
//
//            FaceMetaNativeBigAd.getInstance(activity).LoadFacebookNativeBigAds();
//            FaceMetaNativeMediumAd.getInstance(activity).LoadNativeMediumAds();
//            FaceMetaNativeBannerAd.getInstance(activity).loadFacebookNativeBannerAds();
//
//            AppOpenManager.getInstance(activity).LoadAd();
//        }
//    }
//
//    public void HandelData(Boolean underMaintenance) {
//        if (sharedPreferencesHelper.getRedirectOtherApp()) {
//            if (appSettingListeners != null) {
//                appSettingListeners.onAppRedirect(sharedPreferencesHelper.getAppAccountLink());
//                appSettingListeners = null;
//            }
//
//        } else if (sharedPreferencesHelper.getVersionUpdateDialog() && AdUtils.checkUpdate(AppVersion)) {
//            if (appSettingListeners != null) {
//                appSettingListeners.onAppUpdate("https://play.google.com/store/apps/details?id=" + activity.getPackageName());
//                appSettingListeners = null;
//            }
//
//        } else if (underMaintenance) {
//            if (appSettingListeners != null) {
//                appSettingListeners.onUnderMaintenance();
//                appSettingListeners = null;
//            }
//        } else {
//            if (appSettingListeners != null) {
//                appSettingListeners.onResponseSuccess();
//                appSettingListeners = null;
//            }
//        }
//
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        AppOpenManager.Splash_Start = false;
//    }
//
//}