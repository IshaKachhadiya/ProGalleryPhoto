package com.iten.tenoku.model;

import com.google.gson.annotations.SerializedName;

   
public class ObjAPPSETTINGS {

   @SerializedName("iId")
   String iId;

   @SerializedName("vAppId")
   String vAppId;

   @SerializedName("iAccId")
   String iAccId;

   @SerializedName("iCatId")
   String iCatId;

   @SerializedName("vAppName")
   String vAppName;

   @SerializedName("vPackName")
   String vPackName;

   @SerializedName("vLogo")
   String vLogo;

   @SerializedName("isVersionUpdateDialog")
  public boolean isVersionUpdateDialog;

   @SerializedName("vVersionCode")
   String vVersionCode;

   @SerializedName("isRedirecToTherApp")
   boolean isRedirecToTherApp;

   @SerializedName("vRedirecToTherAppPackage")
   String vRedirecToTherAppPackage;

   @SerializedName("isMoreApp")
   boolean isMoreApp;

   @SerializedName("isNeedInternet")
   boolean isNeedInternet;

   @SerializedName("vPolicylink")
   String vPolicylink;

   @SerializedName("vPrivacyPolicy")
   String vPrivacyPolicy;

   @SerializedName("vAppAccountLink")
   String vAppAccountLink;

   @SerializedName("vNotificationId")
   String vNotificationId;

   @SerializedName("vNotificationKey")
   String vNotificationKey;

   @SerializedName("isOwnAdShow")
   public boolean isOwnAdShow;
   @SerializedName("isShowAppInHouse")
   public boolean isShowAppInHouse;

   @SerializedName("isShowadInApp")
   public boolean isShowadInApp;

   @SerializedName("vAllAddFormat")
   String vAllAddFormat;

   @SerializedName("isHowShowAddEquence")
   boolean isHowShowAddEquence;

    @SerializedName("isOwnSequence")
    String isOwnSequence;
   @SerializedName("vAppSequence")
   String vAppSequence;

   @SerializedName("vAlernateAdShow")
   String vAlernateAdShow;

   @SerializedName("isHowShowInterstailadSequence")
   boolean isHowShowInterstailadSequence;

   @SerializedName("vAdPlatFormSequenceInterstitial")
   String vAdPlatFormSequenceInterstitial;

   @SerializedName("vAlernateAdShowInterstitial")
   String vAlernateAdShowInterstitial;

   @SerializedName("isHowShowRewardvideoAdSequence")
   boolean isHowShowRewardvideoAdSequence;

   @SerializedName("vAdPlatformSequenceRewardVideo")
   String vAdPlatformSequenceRewardVideo;

   @SerializedName("vAlernateAdShowRewardVideo")
   String vAlernateAdShowRewardVideo;

   @SerializedName("isHowShownAtiveadSequence")
   boolean isHowShownAtiveadSequence;

   @SerializedName("vAdPlatformSequenceNativead")
   String vAdPlatformSequenceNativead;

   @SerializedName("vAlernateAdShowNativead")
   String vAlernateAdShowNativead;

   @SerializedName("isShowTestAd")
   boolean isShowTestAd;

   @SerializedName("iIntCounter")
   String iIntCounter;

   @SerializedName("iIntBackCounter")
   String iIntBackCounter;
   @SerializedName("iFailedCounter")
   String iFailedCounter;

   @SerializedName("isAMDClickStatus")
   boolean isAMDClickStatus;

   @SerializedName("isAltIntAppOpenStuse")
   boolean isAltIntAppOpenStuse;

   @SerializedName("isAppopenad")
   boolean isAppopenad;

   @SerializedName("isAdshowingpopup")
   boolean isAdshowingpopup;

    public boolean getIsOwnAdShow() {
        return isOwnAdShow;
    }

    public void setIId(String iId) {
        this.iId = iId;
    }
    public String getIId() {
        return iId;
    }
    
    public void setVAppId(String vAppId) {
        this.vAppId = vAppId;
    }
    public String getVAppId() {
        return vAppId;
    }
    
    public void setIAccId(String iAccId) {
        this.iAccId = iAccId;
    }
    public String getIAccId() {
        return iAccId;
    }
    
    public void setICatId(String iCatId) {
        this.iCatId = iCatId;
    }
    public String getICatId() {
        return iCatId;
    }
    
    public void setVAppName(String vAppName) {
        this.vAppName = vAppName;
    }
    public String getVAppName() {
        return vAppName;
    }
    
    public void setVPackName(String vPackName) {
        this.vPackName = vPackName;
    }
    public String getVPackName() {
        return vPackName;
    }
    
    public void setVLogo(String vLogo) {
        this.vLogo = vLogo;
    }
    public String getVLogo() {
        return vLogo;
    }
    
    public void setIsVersionUpdateDialog(boolean isVersionUpdateDialog) {
        this.isVersionUpdateDialog = isVersionUpdateDialog;
    }
    public boolean getIsVersionUpdateDialog() {
        return isVersionUpdateDialog;
    }
    
    public void setVVersionCode(String vVersionCode) {
        this.vVersionCode = vVersionCode;
    }
    public String getVVersionCode() {
        return vVersionCode;
    }
    
    public void setIsRedirecToTherApp(boolean isRedirecToTherApp) {
        this.isRedirecToTherApp = isRedirecToTherApp;
    }
    public boolean getIsRedirecToTherApp() {
        return isRedirecToTherApp;
    }
    
    public void setVRedirecToTherAppPackage(String vRedirecToTherAppPackage) {
        this.vRedirecToTherAppPackage = vRedirecToTherAppPackage;
    }
    public String getVRedirecToTherAppPackage() {
        return vRedirecToTherAppPackage;
    }
    
    public void setIsMoreApp(boolean isMoreApp) {
        this.isMoreApp = isMoreApp;
    }
    public boolean getIsMoreApp() {
        return isMoreApp;
    }
    
    public void setIsNeedInternet(boolean isNeedInternet) {
        this.isNeedInternet = isNeedInternet;
    }
    public boolean getIsNeedInternet() {
        return isNeedInternet;
    }
    
    public void setVPolicylink(String vPolicylink) {
        this.vPolicylink = vPolicylink;
    }
    public String getVPolicylink() {
        return vPolicylink;
    }
    
    public void setVPrivacyPolicy(String vPrivacyPolicy) {
        this.vPrivacyPolicy = vPrivacyPolicy;
    }
    public String getVPrivacyPolicy() {
        return vPrivacyPolicy;
    }
    
    public void setVAppAccountLink(String vAppAccountLink) {
        this.vAppAccountLink = vAppAccountLink;
    }
    public String getVAppAccountLink() {
        return vAppAccountLink;
    }
    
    public void setVNotificationId(String vNotificationId) {
        this.vNotificationId = vNotificationId;
    }
    public String getVNotificationId() {
        return vNotificationId;
    }
    
    public void setVNotificationKey(String vNotificationKey) {
        this.vNotificationKey = vNotificationKey;
    }
    public String getVNotificationKey() {
        return vNotificationKey;
    }
    
    public void setIsShowAppInHouse(boolean isShowAppInHouse) {
        this.isShowAppInHouse = isShowAppInHouse;
    }
    public boolean getIsShowAppInHouse() {
        return isShowAppInHouse;
    }
    
    public void setIsShowadInApp(boolean isShowadInApp) {
        this.isShowadInApp = isShowadInApp;
    }
    public boolean getIsShowadInApp() {
        return isShowadInApp;
    }
    
    public void setVAllAddFormat(String vAllAddFormat) {
        this.vAllAddFormat = vAllAddFormat;
    }
    public String getVAllAddFormat() {
        return vAllAddFormat;
    }
    
    public void setIsHowShowAddEquence(boolean isHowShowAddEquence) {
        this.isHowShowAddEquence = isHowShowAddEquence;
    }
    public boolean getIsHowShowAddEquence() {
        return isHowShowAddEquence;
    }

    public String getIsOwnSequence() {
        return isOwnSequence;
    }

    public void setVAppSequence(String vAppSequence) {
        this.vAppSequence = vAppSequence;
    }
    public String getVAppSequence() {
        return vAppSequence;
    }
    
    public void setVAlernateAdShow(String vAlernateAdShow) {
        this.vAlernateAdShow = vAlernateAdShow;
    }
    public String getVAlernateAdShow() {
        return vAlernateAdShow;
    }
    
    public void setIsHowShowInterstailadSequence(boolean isHowShowInterstailadSequence) {
        this.isHowShowInterstailadSequence = isHowShowInterstailadSequence;
    }
    public boolean getIsHowShowInterstailadSequence() {
        return isHowShowInterstailadSequence;
    }
    
    public void setVAdPlatFormSequenceInterstitial(String vAdPlatFormSequenceInterstitial) {
        this.vAdPlatFormSequenceInterstitial = vAdPlatFormSequenceInterstitial;
    }
    public String getVAdPlatFormSequenceInterstitial() {
        return vAdPlatFormSequenceInterstitial;
    }
    
    public void setVAlernateAdShowInterstitial(String vAlernateAdShowInterstitial) {
        this.vAlernateAdShowInterstitial = vAlernateAdShowInterstitial;
    }
    public String getVAlernateAdShowInterstitial() {
        return vAlernateAdShowInterstitial;
    }
    
    public void setIsHowShowRewardvideoAdSequence(boolean isHowShowRewardvideoAdSequence) {
        this.isHowShowRewardvideoAdSequence = isHowShowRewardvideoAdSequence;
    }
    public boolean getIsHowShowRewardvideoAdSequence() {
        return isHowShowRewardvideoAdSequence;
    }
    
    public void setVAdPlatformSequenceRewardVideo(String vAdPlatformSequenceRewardVideo) {
        this.vAdPlatformSequenceRewardVideo = vAdPlatformSequenceRewardVideo;
    }
    public String getVAdPlatformSequenceRewardVideo() {
        return vAdPlatformSequenceRewardVideo;
    }
    
    public void setVAlernateAdShowRewardVideo(String vAlernateAdShowRewardVideo) {
        this.vAlernateAdShowRewardVideo = vAlernateAdShowRewardVideo;
    }
    public String getVAlernateAdShowRewardVideo() {
        return vAlernateAdShowRewardVideo;
    }
    
    public void setIsHowShownAtiveadSequence(boolean isHowShownAtiveadSequence) {
        this.isHowShownAtiveadSequence = isHowShownAtiveadSequence;
    }
    public boolean getIsHowShownAtiveadSequence() {
        return isHowShownAtiveadSequence;
    }
    
    public void setVAdPlatformSequenceNativead(String vAdPlatformSequenceNativead) {
        this.vAdPlatformSequenceNativead = vAdPlatformSequenceNativead;
    }
    public String getVAdPlatformSequenceNativead() {
        return vAdPlatformSequenceNativead;
    }
    
    public void setVAlernateAdShowNativead(String vAlernateAdShowNativead) {
        this.vAlernateAdShowNativead = vAlernateAdShowNativead;
    }
    public String getVAlernateAdShowNativead() {
        return vAlernateAdShowNativead;
    }
    
    public void setIsShowTestAd(boolean isShowTestAd) {
        this.isShowTestAd = isShowTestAd;
    }
    public boolean getIsShowTestAd() {
        return isShowTestAd;
    }
    
    public void setIIntCounter(String iIntCounter) {
        this.iIntCounter = iIntCounter;
    }
    public String getIIntCounter() {
        return iIntCounter;
    }
    
    public void setIIntBackCounter(String iIntBackCounter) {
        this.iIntBackCounter = iIntBackCounter;
    }
    public String getIIntBackCounter() {
        return iIntBackCounter;
    }

    public String getIFailedCounter() {
        return iFailedCounter;
    }

    public void setIsAMDClickStatus(boolean isAMDClickStatus) {
        this.isAMDClickStatus = isAMDClickStatus;
    }
    public boolean getIsAMDClickStatus() {
        return isAMDClickStatus;
    }
    
    public void setIsAltIntAppOpenStuse(boolean isAltIntAppOpenStuse) {
        this.isAltIntAppOpenStuse = isAltIntAppOpenStuse;
    }
    public boolean getIsAltIntAppOpenStuse() {
        return isAltIntAppOpenStuse;
    }
    
    public void setIsAppopenad(boolean isAppopenad) {
        this.isAppopenad = isAppopenad;
    }
    public boolean getIsAppopenad() {
        return isAppopenad;
    }
    
    public void setIsAdshowingpopup(boolean isAdshowingpopup) {
        this.isAdshowingpopup = isAdshowingpopup;
    }
    public boolean getIsAdshowingpopup() {
        return isAdshowingpopup;
    }
    
}