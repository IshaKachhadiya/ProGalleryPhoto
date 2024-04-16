package com.iten.tenoku.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.daimajia.androidanimations.library.BuildConfig;
import com.google.gson.Gson;


public class SharedPreferencesHelper {
    public SharedPreferences sharedPreferences;

    public SharedPreferencesHelper(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public static SharedPreferencesHelper init(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(BuildConfig.LIBRARY_PACKAGE_NAME, Context.MODE_PRIVATE);
        return new SharedPreferencesHelper(sharedPreferences);
    }

    public void setRequestDataSend(Boolean isReqestDataSend) {
        sharedPreferences.edit().putBoolean(AdConstant.isRequestDataSend, isReqestDataSend).apply();
    }

    public Boolean getRequestDataSend() {
        return sharedPreferences.getBoolean(AdConstant.isRequestDataSend, false);
    }

    public int getInterNativeAdSequenceCounter() {
        return sharedPreferences.getInt(AdConstant.interNativeAdSequenceCounter, 0);
    }

    public void setInterNativeAdSequenceCounter(int interNativeAdSequenceCounter) {
        sharedPreferences.edit().putInt(AdConstant.interNativeAdSequenceCounter, interNativeAdSequenceCounter).apply();
    }

    public String getUserSavedDate() {
        return sharedPreferences.getString(AdConstant.userSavedDate, "");
    }

    public void setUserSavedDate(String response) {
        sharedPreferences.edit().putString(AdConstant.userSavedDate, response).apply();
    }

    public int getTotalFailedCount() {
        return sharedPreferences.getInt(AdConstant.totalFailedCount, 0);
    }

    public void setTotalFailedCount(int userCount) {
        sharedPreferences.edit().putInt(AdConstant.totalFailedCount, userCount).apply();
    }

    public String getResponse() {
        return sharedPreferences.getString(AdConstant.response, "");
    }

    public void setResponse(String response) {
        sharedPreferences.edit().putString(AdConstant.response, response).apply();
    }

    public String getRecent() {
        return sharedPreferences.getString(AdConstant.recentSound, "null");
    }

    public void setRecent(Object response) {
        sharedPreferences.edit().putString(AdConstant.recentSound, new Gson().toJson(response)).apply();
    }

    public String getLanguage() {
        return sharedPreferences.getString(AdConstant.language, "English");
    }

    public void setLanguage(String response) {
        sharedPreferences.edit().putString(AdConstant.language, response).apply();
    }

    public Boolean getAppInstallStatus() {
        return sharedPreferences.getBoolean(AdConstant.first_time_app_install, true);
    }

    public void setAppInstallStatus(Boolean appInstallStatus) {
        sharedPreferences.edit().putBoolean(AdConstant.first_time_app_install, appInstallStatus).apply();
    }

    public boolean getBoolean(String str, boolean z) {
        if (sharedPreferences != null) {
            return sharedPreferences.getBoolean(str, z);
        }
        return false;
    }

    public void putBoolean(String str, boolean z) {
        sharedPreferences.edit().putBoolean(str, z).apply();
    }


    public String getSaveString(String key, String value) {
        return sharedPreferences.getString(key, value);
    }

    public void setSaveString(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }


    public Boolean getVersionUpdateDialog() {
        return sharedPreferences.getBoolean(AdConstant.versionupdatedialog, false);
    }

    public void setVersionUpdateDialog(Boolean versionUpdateDialog) {
        sharedPreferences.edit().putBoolean(AdConstant.versionupdatedialog, versionUpdateDialog).apply();
    }


    public Boolean getOwnInterClose() {
        return sharedPreferences.getBoolean(AdConstant.isOwnInterClose, false);
    }

    public void setOwnInterClose(Boolean isClose) {
        sharedPreferences.edit().putBoolean(AdConstant.isOwnInterClose, isClose).apply();
    }

    public Boolean getOwnAdShow() {
        return sharedPreferences.getBoolean(AdConstant.isownadshow, false);
    }

    public void setOwnAdShow(Boolean onAdsResume) {
        sharedPreferences.edit().putBoolean(AdConstant.isownadshow, onAdsResume).apply();
    }

    public int getOwnAppOpenPosition() {
        return sharedPreferences.getInt(AdConstant.OwnAppOpenNo, 0);
    }

    public void setOwnAppOpenPosition(int shownPosition) {
        sharedPreferences.edit().putInt(AdConstant.OwnAppOpenNo, shownPosition).apply();
    }

    public int getOwnNativePosition() {
        return sharedPreferences.getInt(AdConstant.OwnNativeNo, 0);
    }

    public void setOwnNativePosition(int shownPosition) {
        sharedPreferences.edit().putInt(AdConstant.OwnNativeNo, shownPosition).apply();
    }

    public int getOwnBannerPosition() {
        return sharedPreferences.getInt(AdConstant.OwnBannerNo, 0);
    }

    public void setOwnBannerPosition(int shownPosition) {
        sharedPreferences.edit().putInt(AdConstant.OwnBannerNo, shownPosition).apply();
    }

    public int getOwnInterTimer() {
        return sharedPreferences.getInt(AdConstant.OwnInterTimer, 5);
    }

    public void setOwnInterTimer(int interTimer) {
        sharedPreferences.edit().putInt(AdConstant.OwnInterTimer, interTimer).apply();
    }

    public int getOwnInterstitialPosition() {
        return sharedPreferences.getInt(AdConstant.OwnInterNo, 0);
    }

    public void setOwnInterstitialPosition(int shownPosition) {
        sharedPreferences.edit().putInt(AdConstant.OwnInterNo, shownPosition).apply();
    }

    public Boolean getOnAdsResume() {
        return sharedPreferences.getBoolean(AdConstant.OnAdsResume, false);
    }

    public void setOnAdsResume(Boolean onAdsResume) {
        sharedPreferences.edit().putBoolean(AdConstant.OnAdsResume, onAdsResume).apply();
    }

    public Boolean getPreload() {
        return sharedPreferences.getBoolean(AdConstant.OnPreload, false);
    }

    public void setPreload(Boolean onPreload) {
        sharedPreferences.edit().putBoolean(AdConstant.OnPreload, onPreload).apply();
    }

    public Boolean getNeedInternet() {
        return sharedPreferences.getBoolean(AdConstant.NeedInternet, false);
    }

    public void setNeedInternet(Boolean needInternet) {
        sharedPreferences.edit().putBoolean(AdConstant.NeedInternet, needInternet).apply();
    }

    public Boolean getIsNeedToShowNativeBanner() {
        return sharedPreferences.getBoolean(AdConstant.IsNeedToShowNativeBanner, false);
    }

    public void setIsNeedToShowNativeBanner(Boolean onPreload) {
        sharedPreferences.edit().putBoolean(AdConstant.IsNeedToShowNativeBanner, onPreload).apply();
    }

    public int getRefreshCount() {
        return sharedPreferences.getInt(AdConstant.refreshCount, 0);
    }

    public void setRefreshCount(int refreshCount) {
        sharedPreferences.edit().putInt(AdConstant.refreshCount, refreshCount).apply();
    }

    public Boolean getRedirectOtherApp() {
        return sharedPreferences.getBoolean(AdConstant.redirectotherapp, false);
    }

    public void setRedirectOtherApp(Boolean redirectOtherApp) {
        sharedPreferences.edit().putBoolean(AdConstant.redirectotherapp, redirectOtherApp).apply();
    }

    public String getAppAccountLink() {
        return sharedPreferences.getString(AdConstant.appaccountlink, "1");
    }

    public void setAppAccountLink(String appAccountLink) {
        sharedPreferences.edit().putString(AdConstant.appaccountlink, appAccountLink).apply();
    }

    public String getVersionCode() {
        return sharedPreferences.getString(AdConstant.versioncode, "1");
    }

    public void setVersionCode(String versioncode) {
        sharedPreferences.edit().putString(AdConstant.versioncode, versioncode).apply();

    }

    public Boolean getShowAdinApp() {
        return sharedPreferences.getBoolean(AdConstant.showadinapp, false);
    }

    public void setShowAdinApp(Context context, Boolean showadinapp) {
        sharedPreferences.edit().putBoolean(AdConstant.showadinapp, showadinapp).apply();
    }

    public Boolean getAdMobAdShow() {
        return sharedPreferences.getBoolean(AdConstant.AdMobAdShow, false);
    }

    public void setAdMobAdShow(Boolean adMobAdShow) {
        sharedPreferences.edit().putBoolean(AdConstant.AdMobAdShow, adMobAdShow).apply();
    }

    public Boolean getFacebookAdShow() {
        return sharedPreferences.getBoolean(AdConstant.FacebookAdShow, false);
    }

    public void setFacebookAdShow(Boolean adMobAdShow) {
        sharedPreferences.edit().putBoolean(AdConstant.FacebookAdShow, adMobAdShow).apply();
    }

    public Boolean getNativeAdShow() {
        return sharedPreferences.getBoolean(AdConstant.NativeAdShow, true);
    }

    public void setNativeAdShow(Boolean nativeAdShow) {
        sharedPreferences.edit().putBoolean(AdConstant.NativeAdShow, nativeAdShow).apply();
    }

    public Boolean getNativeBigAdShow() {
        return sharedPreferences.getBoolean(AdConstant.NativeBigAdShow, true);
    }

    public void setNativeBigAdShow(Boolean NativeBigAdShow) {
        sharedPreferences.edit().putBoolean(AdConstant.NativeBigAdShow, NativeBigAdShow).apply();
    }

    public Boolean getNativeBannerAdShow() {
        return sharedPreferences.getBoolean(AdConstant.NativeBannerAdShow, true);
    }

    public void setNativeBannerAdShow(Boolean NativeBannerAdShow) {
        sharedPreferences.edit().putBoolean(AdConstant.NativeBannerAdShow, NativeBannerAdShow).apply();
    }

    public Boolean getInterstitialAdShow() {
        return sharedPreferences.getBoolean(AdConstant.InterstitialAdShow, true);
    }

    public void setInterstitialAdShow(Boolean InterstitialAdShow) {
        sharedPreferences.edit().putBoolean(AdConstant.InterstitialAdShow, InterstitialAdShow).apply();
    }

    public Boolean getFacebookInterstitialAdShow() {
        return sharedPreferences.getBoolean(AdConstant.FacebookInterstitialAdShow, true);
    }

    public void setFacebookInterstitialAdShow(Boolean InterstitialAdShow) {
        sharedPreferences.edit().putBoolean(AdConstant.FacebookInterstitialAdShow, InterstitialAdShow).apply();
    }

    public int getFailedCount() {
        return sharedPreferences.getInt(AdConstant.FailedCount, 0);
    }

    public void setFailedCount(int failedCount) {
        sharedPreferences.edit().putInt(AdConstant.FailedCount, failedCount).apply();
    }

    public int getAdmobFailedCount() {
        return sharedPreferences.getInt(AdConstant.AdmobFailedCount, 0);
    }

    public void setAdmobFailedCount(int admobFailedCount) {
        sharedPreferences.edit().putInt(AdConstant.AdmobFailedCount, admobFailedCount).apply();
    }

    public int getFbFailedCount() {
        return sharedPreferences.getInt(AdConstant.FbFailedCount, 0);
    }

    public void setFbFailedCount(int fbFailedCount) {
        sharedPreferences.edit().putInt(AdConstant.FbFailedCount, fbFailedCount).apply();
    }

    public int getMainClick() {
        return sharedPreferences.getInt(AdConstant.IntCounter, 0);
    }

    public void setMainClick(int mainClick) {
        sharedPreferences.edit().putInt(AdConstant.IntCounter, mainClick).apply();
    }

    public int getBackClick() {
        return sharedPreferences.getInt(AdConstant.IntBackCounter, 0);
    }

    public void setBackClick(int backClick) {
        sharedPreferences.edit().putInt(AdConstant.IntBackCounter, backClick).apply();
    }

    public Boolean getOpenAdStatus() {
        return sharedPreferences.getBoolean(AdConstant.AltIntAppOpenStuse, false);
    }

    public void setOpenAdStatus(boolean altIntAppOpenStatus) {
        sharedPreferences.edit().putBoolean(AdConstant.AltIntAppOpenStuse, altIntAppOpenStatus).apply();
    }

    public Boolean getAMDStatus() {
        return sharedPreferences.getBoolean(AdConstant.AMDClickStatus, false);
    }

    public void setAMDStatus(boolean isaMDClickStatus) {
        sharedPreferences.edit().putBoolean(AdConstant.AMDClickStatus, isaMDClickStatus).apply();
    }

    public Boolean getCountyClick() {
        return sharedPreferences.getBoolean(AdConstant.CountryClick, false);
    }

    public void setCountyClick(boolean countyClick) {
        sharedPreferences.edit().putBoolean(AdConstant.CountryClick, countyClick).apply();
    }

    public Boolean getAppOpenAd() {
        return sharedPreferences.getBoolean(AdConstant.appopenad, false);
    }

    public void setAppOpenAd(boolean appOpenAd) {
        sharedPreferences.edit().putBoolean(AdConstant.appopenad, appOpenAd).apply();
    }

    public Boolean getFestumOpenAd() {
        return sharedPreferences.getBoolean(AdConstant.festumopenad, false);
    }

    public void setFestumOpenAd(boolean appOpenAd) {
        sharedPreferences.edit().putBoolean(AdConstant.festumopenad, appOpenAd).apply();
    }

    public Boolean getUnderMaintenance() {
        return sharedPreferences.getBoolean(AdConstant.UnderMaintenance, true);
    }

    public void setUnderMaintenance(boolean underMaintenance) {
        sharedPreferences.edit().putBoolean(AdConstant.UnderMaintenance, underMaintenance).apply();
    }

    public Boolean getAdShowingPopup() {
        return sharedPreferences.getBoolean(AdConstant.adshowingpopup, true);
    }

    public void setAdShowingPopup(boolean adShowingPopup) {
        sharedPreferences.edit().putBoolean(AdConstant.adshowingpopup, adShowingPopup).apply();
    }

    public String getPrivacyPolicy() {
        return sharedPreferences.getString(AdConstant.privacypolicy, "");
    }

    public void setPrivacyPolicy(String privacyPolicy) {
        sharedPreferences.edit().putString(AdConstant.privacypolicy, privacyPolicy).apply();
    }

    public Boolean getShowAppInHouse() {
        return sharedPreferences.getBoolean(AdConstant.showappinhouse, false);
    }

    public void setShowAppInHouse(boolean appOpenAd) {
        sharedPreferences.edit().putBoolean(AdConstant.showappinhouse, appOpenAd).apply();
    }

    public String getAddBgColor() {
        return sharedPreferences.getString(AdConstant.adbgcolor, "");
    }

    public void setAddBgColor(String adBgColor) {
        sharedPreferences.edit().putString(AdConstant.adbgcolor, adBgColor).apply();
    }

    public String getAdTitleColor() {
        return sharedPreferences.getString(AdConstant.adtitlecolor, "");
    }

    public void setAdTitleColor(String adTitleColor) {
        sharedPreferences.edit().putString(AdConstant.adtitlecolor, adTitleColor).apply();
    }

    public String getAdDescriptionColor() {
        return sharedPreferences.getString(AdConstant.addescriptioncolor, "");
    }

    public void setAdDescriptionColor(String adDescriptionColor) {
        sharedPreferences.edit().putString(AdConstant.addescriptioncolor, adDescriptionColor).apply();
    }

    public String getAdButtonTextColor() {
        return sharedPreferences.getString(AdConstant.adbuttontextcolor, "");
    }

    public void setAdButtonTextColor(String adButtonTextColor) {
        sharedPreferences.edit().putString(AdConstant.adbuttontextcolor, adButtonTextColor).apply();
    }

    public String getNotificationId() {
        return sharedPreferences.getString(AdConstant.notificationid, "");
    }

    public void setNotificationId(String notificationId) {
        sharedPreferences.edit().putString(AdConstant.notificationid, notificationId).apply();
    }


    public int getAdBgColorOpacity() {
        return sharedPreferences.getInt(AdConstant.adbgcoloropacity, -1);
    }

    public void setAdBgColorOpacity(int adBgColorOpacity) {
        sharedPreferences.edit().putInt(AdConstant.adbgcoloropacity, adBgColorOpacity).apply();
    }

    public int getAdButtonColorOpacity() {
        return sharedPreferences.getInt(AdConstant.adbuttoncoloropacity, -1);
    }

    public void setAdButtonColorOpacity(int adButtonColorOpacity) {
        sharedPreferences.edit().putInt(AdConstant.adbuttoncoloropacity, adButtonColorOpacity).apply();
    }

    public Boolean getQurekaLite() {
        return sharedPreferences.getBoolean(AdConstant.QurekaLite, false);
    }

    public void setQurekaLite(Boolean valueOf) {
        sharedPreferences.edit().putBoolean(AdConstant.QurekaLite, valueOf).apply();
    }


    public void setLanguagaTime(Boolean valueOf) {
        sharedPreferences.edit().putBoolean(AdConstant.languageFirstTime, valueOf).apply();
    }

    public Boolean getLanguageTime() {
        return sharedPreferences.getBoolean(AdConstant.languageFirstTime, false);
    }

    public Boolean getQurekaClose() {
        return sharedPreferences.getBoolean(AdConstant.QurekaClose, true);
    }

    public void setQurekaClose(Boolean valueOf) {
        sharedPreferences.edit().putBoolean(AdConstant.QurekaClose, valueOf).apply();
    }

    public int getShowEntryScreen() {
        return sharedPreferences.getInt(AdConstant.showEntryScreen, 0);
    }

    public void setShowEntryScreen(int showEntryScreen) {
        sharedPreferences.edit().putInt(AdConstant.showEntryScreen, showEntryScreen).apply();
    }

    public String getQurekaLink() {
        return sharedPreferences.getString(AdConstant.QurekaLink, "http://871.win.qureka.com/");
    }

    public void setQurekaLink(String qurekaLink) {
        sharedPreferences.edit().putString(AdConstant.QurekaLink, qurekaLink).apply();
    }

    public String getPredChampLink() {
        return sharedPreferences.getString(AdConstant.PredchampLink, "https://472.game.predchamp.com/");
    }

    public void setPredChampLink(String predChampLink) {
        sharedPreferences.edit().putString(AdConstant.PredchampLink, predChampLink).apply();
    }

    public String getAlternateAdShow() {
        return sharedPreferences.getString(AdConstant.alernateAdShow, "");
    }

    public void setAlternateAdShow(String alternateAdShow) {
        sharedPreferences.edit().putString(AdConstant.alernateAdShow, alternateAdShow).apply();
    }

    public String getFixSequence() {
        return sharedPreferences.getString(AdConstant.appsequence, "");
    }

    public void setFixSequence(String fixSequence) {
        sharedPreferences.edit().putString(AdConstant.appsequence, fixSequence).apply();
    }

    public String getOwnSequence() {
        return sharedPreferences.getString(AdConstant.ownsequnce, "");
    }

    public void setOwnSequence(String ownSequence) {
        sharedPreferences.edit().putString(AdConstant.ownsequnce, ownSequence).apply();
    }

    public int getShowInterstitialClickCount() {
        return sharedPreferences.getInt(AdConstant.setShowInterstitialClickCount, 0);
    }

    public void setShowInterstitialClickCount(int clickCount) {
        sharedPreferences.edit().putInt(AdConstant.setShowInterstitialClickCount, clickCount).apply();
    }

    public Boolean getShowCountyClick() {
        return sharedPreferences.getBoolean(AdConstant.showCountyClick, false);
    }

    public void setShowCountyClick(boolean showCountyClick) {
        sharedPreferences.edit().putBoolean(AdConstant.showCountyClick, showCountyClick).apply();
    }

    public Boolean getExitScreen() {
        return sharedPreferences.getBoolean(AdConstant.exitScreen, false);
    }

    public void setExitScreen(boolean exitScreen) {
        sharedPreferences.edit().putBoolean(AdConstant.exitScreen, exitScreen).apply();
    }

    public int getGridViewPerItemAdTwo() {
        return sharedPreferences.getInt(AdConstant.GridViewAdPerItemTwo, 0);
    }

    public void setGridViewPerItemAdTwo(int gridViewAd) {
        sharedPreferences.edit().putInt(AdConstant.GridViewAdPerItemTwo, gridViewAd).apply();
    }

    public int getGridViewPerItemAdThree() {
        return sharedPreferences.getInt(AdConstant.GridViewAdPerItemThree, 0);
    }

    public void setGridViewPerItemAdThree(int gridViewAd) {
        sharedPreferences.edit().putInt(AdConstant.GridViewAdPerItemThree, gridViewAd).apply();
    }

    public int getListViewAd() {
        return sharedPreferences.getInt(AdConstant.ListViewAd, 0);
    }

    public void setListViewAd(int listViewAd) {
        sharedPreferences.edit().putInt(AdConstant.ListViewAd, listViewAd).apply();
    }

    public String getAdButtonColor() {
        return sharedPreferences.getString(AdConstant.AdButtonColor, "");
    }

    public void setAdButtonColor(String adButtonColor) {
        sharedPreferences.edit().putString(AdConstant.AdButtonColor, adButtonColor).apply();
    }

    public String getTopImageSliderImageTopic() {
        return sharedPreferences.getString(AdConstant.TopImageSliderImageTopic, "pre wedding photoshoot");
    }

    public void setTopImageSliderImageTopic(String unSplashKey) {
        sharedPreferences.edit().putString(AdConstant.TopImageSliderImageTopic, unSplashKey).apply();
    }

    public String getFacebookAppId() {
        return sharedPreferences.getString(AdConstant.facebookAppId, "1");
    }

    public void setFacebookAppId(String facebookAppId) {
        sharedPreferences.edit().putString(AdConstant.facebookAppId, facebookAppId).apply();
    }

    public String getFacebookClientToken() {
        return sharedPreferences.getString(AdConstant.facebookClientToken, "1");
    }

    public void setFacebookClientToken(String facebookClientToken) {
        sharedPreferences.edit().putString(AdConstant.facebookClientToken, facebookClientToken).apply();
    }

}
