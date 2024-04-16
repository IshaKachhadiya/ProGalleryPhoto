package com.iten.tenoku.utils;

import static com.iten.tenoku.utils.MyApplication.sharedPreferencesHelper;

import com.iten.tenoku.model.FacebookNativeAdModel;
import com.iten.tenoku.model.FacebookNativeBannerAdModel;
import com.iten.tenoku.model.FestumresponseModel;
import com.iten.tenoku.model.GoogleNativeAdModel;
import com.iten.tenoku.model.GoogleNativeBannerAdModel;
import com.iten.tenoku.model.QurekaInterstitialAdModel;

import java.util.ArrayList;
import java.util.Collections;

public class AdConstant {
    public static ArrayList<GoogleNativeAdModel> googleNativeAdModels = new ArrayList<>();
    public static ArrayList<GoogleNativeBannerAdModel> adMobNativeAdBannerModels = new ArrayList<>();
    public static ArrayList<FacebookNativeAdModel> facebookNativeAdModels = new ArrayList<>();
    public static ArrayList<FacebookNativeBannerAdModel> facebookNativeBannerAdModels = new ArrayList<>();
    public static String interNativeAdSequenceCounter = "internativeadsequencecounter";
    public static String isRequestDataSend = "isRequestDataSend";
    public static String totalFailedCount = "totalFailedCount";
    public static String FailedCount = "FailedCount";
    public static String AdmobFailedCount = "AdmobFailedCount";
    public static String FbFailedCount = "FbFailedCount";
    public static String userSavedDate = "userSavedDate";
    //Response
    public static String facebookAppId = "facebookappid";
    public static String facebookClientToken = "facebookclienttoken";

    public static String id = "id";
    public static String languageFirstTime = "languagesFirstTime";
    public static String versionupdatedialog = "versionupdatedialog";
    public static String versioncode = "versioncode";
    public static String redirectotherapp = "redirectotherapp";
    public static String privacypolicy = "privacypolicy";
    public static String appaccountlink = "appaccountlink";
    public static String showadinapp = "showadinapp";
    public static String isownadshow = "isownadshow";
    public static String isOwnInterClose = "isowninterclose";
    public static String AdMobAdShow = "admobadshow";
    public static String FacebookAdShow = "facebookadshow";
    public static String NativeAdShow = "NativeAdShow";
    public static String NativeBigAdShow = "NativeBigAdShow";
    public static String NativeBannerAdShow = "NativeBannerAdShow";
    public static String InterstitialAdShow = "InterstitialAdShow";

    public static String FacebookInterstitialAdShow = "FacebookInterstitialAdShow";
    public static String IntCounter = "intcounter";
    public static String OwnAppOpenNo = "ownappopenno";
    public static String OwnNativeNo = "ownnativeno";
    public static String OwnBannerNo = "ownbannerno";
    public static String OwnInterNo = "owninterno";
    public static String OwnInterTimer = "ownintertimer";
    public static String IntBackCounter = "intbackcounter";
    public static String AltIntAppOpenStuse = "altintappopenstuse";
    public static String AMDClickStatus = "amdclickstatus";
    public static String CountryClick = "CountryClick";
    public static String appopenad = "appopenad";
    public static String festumopenad = "festumopenad";
    public static String UnderMaintenance = "underMaintenance";
    public static String adshowingpopup = "adshowingpopup";
    public static boolean isResume = true;
    public static String OnAdsResume = "onadsresume";
    public static String OnPreload = "onpreload";
    public static String NeedInternet = "NeedInternet";
    public static String IsNeedToShowNativeBanner = "isneedtoshownativebanner";
    public static String refreshCount = "refreshcount";

    public static int adProgressTime = 1000;
    public static String showappinhouse = "showappinhouse";

    public static String adcollapsiblekey = "adcollapsiblekey";

    public static String adbgcolor = "adbgcolor";
    public static String adtitlecolor = "adtitlecolor";
    public static String addescriptioncolor = "addescriptioncolor";
    public static String adbuttontextcolor = "adbuttontextcolor";
    public static String notificationid = "notificationid";
    public static String adbgcoloropacity = "adbgcoloropacity";
    public static String adbuttoncoloropacity = "adbuttoncoloropacity";
    public static String QurekaLite = "qurekalite";
    public static String QurekaClose = "qurekaclose";
    public static String showEntryScreen = "showEntryScreen";
    public static String QurekaLink = "QurekaLink";
    public static String PredchampLink = "PredchampLink";
    public static String alernateAdShow = "alternateAdShow";
    public static String appsequence = "appsequence";
    public static String ownsequnce = "ownsequnce";
    public static String OWN = "Own";
    public static String OWN_QUREKA = "Own,Qureka";
    public static String ProFeaturesPurchaseCoin = "ProFeaturesPurchaseCoin";
    public static String WidgetRecycler = "WidgetRecycler";
    public static String ShowGame = "ShowGame";

    public static String ShowIPTV = "ShowIPTV";
    public static String IPTVUrl = "IPTVUrl";

    public static String setShowInterstitialClickCount = "setShowInterstitialClickCount";
    public static String showCountyClick = "showCountyClick";
    public static String exitScreen = "exitScreen";
    public static String TimerSecond = "TimerSecond";
    public static String GridViewAdPerItemTwo = "GridViewAdPerItemTwo";
    public static String GridViewAdPerItemThree = "GridViewAdPerItemThree";
    public static String ListViewAd = "ListViewAd";
    public static String AdButtonColor = "AdButtonColor";

    public static String TopImageSliderImageTopic = "TopImageSliderImageTopic";
    public static ArrayList<String> topImageSliderImageList = new ArrayList<>();

    public static ArrayList<String> commonFeatureHideList = new ArrayList<>();
    public static int GRID_VIEW_PER_THREE_ITEM_AD = 0;
    public static int GRID_VIEW_PER_TWO_ITEM_AD = 0;
    public static int GRID_VIEW_PER_THREE_ITEM_AD_MULTIPLY = 0;
    public static int GRID_VIEW_PER_TWO_ITEM_AD_MULTIPLY = 0;
    public static int LIST_VIEW_PER_AD = 0;
    public static int QUREKA_VIEW_PER_AD = 0;
    public static String first_time_app_install = "first_time_app_install";

    public static String response = "appresponse";

    public static String recentSound = "recntSound";
    public static String language = "language";
    public static String vAdFbCounter = "vAdFbCounter";
    public static String vAdAdmobCounter = "vAdAdmobCounter";
    public static String dtDate = "dtDate";
    public static String vDeviceName = "vDeviceName";
    public static String vAndroidId = "vAndroidId";
    public static String vDeviceId = "vDeviceId";
    public static String vAndroidVesion = "vAndroidVesion";
    public static String vAppName = "vAppName";
    public static String vAppPackageName = "vAppPackageName";
    public static String vAppVersion = "vAppVersion";
    public static String vAdPlatform = "vAdPlatform";

    //    public static List<Data.AppInhouse> appInhouses = new ArrayList<>();
    public static ArrayList<String> qurekaLinksList = new ArrayList<>();
    public static ArrayList<String> iconUrlList = new ArrayList<>();
    public static ArrayList<String> interstitialImagesUrlList = new ArrayList<>();
    public static ArrayList<String> nativeImagesUrlList = new ArrayList<>();
    public static boolean isInterstitialShown = false;

    /*Festum*/

    public static ArrayList<FestumresponseModel> festumNativeAdModels = new ArrayList<>();
    public static ArrayList<FestumresponseModel> festumNativeBannerAdModels = new ArrayList<>();
    public static ArrayList<FestumresponseModel> festumInterstitialAdModels = new ArrayList<>();
    public static ArrayList<FestumresponseModel> festumAppOpenAdModels = new ArrayList<>();
    public static final int ResetTotalCount = 0;


    public static ArrayList<QurekaInterstitialAdModel> qurekaInterstitialAdModelArrayList() {
        ArrayList<QurekaInterstitialAdModel> interstitialAdModels = new ArrayList<>();
        interstitialAdModels.add(new QurekaInterstitialAdModel("Play GK Quiz", "Play and earn upto 50,000 coins", "Play and win"));
        interstitialAdModels.add(new QurekaInterstitialAdModel("Play Game", "Now play your favorite games on one click", "Play and enjoy"));
        interstitialAdModels.add(new QurekaInterstitialAdModel("Play Cricket", "Play cricket quiz and win upto 50,000coins now", "Play and win"));
        interstitialAdModels.add(new QurekaInterstitialAdModel("Mega Quiz", "Mega Quiz for 5,00,000 coins is live now", "Play & win now"));
        return interstitialAdModels;

    }

    public static QurekaInterstitialAdModel getRandomQurekaInterstitalAd() {

        int random = (int) (Math.random() * qurekaInterstitialAdModelArrayList().size());
        return qurekaInterstitialAdModelArrayList().get(random);
    }


    public static String getInterstitialRandomImage() {
        try {
            int random = (int) (Math.random() * interstitialImagesUrlList.size());
            return interstitialImagesUrlList.get(random);
        } catch (Exception e) {
            return "";
        }

    }

    public static String getRandomLogo() {
        try {
            int random = (int) (Math.random() * iconUrlList.size());
            return iconUrlList.get(random);
        } catch (Exception e) {
            return "";
        }

    }

    public static String getNativeRandomImage() {
        try {
            int random = (int) (Math.random() * nativeImagesUrlList.size());
            return nativeImagesUrlList.get(random);
        } catch (Exception e) {
            return "";
        }
    }

    public static String getRandomTopImageSliderImageTopic() {
        Collections.addAll(topImageSliderImageList, sharedPreferencesHelper.getTopImageSliderImageTopic().split(","));
        int random = (int) (Math.random() * topImageSliderImageList.size());
        return topImageSliderImageList.get(random);
    }


    public enum APP_FUNCTION {
        PRANK_AIR_HORN,
        PRANK_SOUND_CREATE,
        PRANK_CALL,
        TRENDING_HORN,
        AIR_HORN,
        VEHI_HORN,
        EMER_HORN,
        SIREN_HORN,
        ALERT_HORN,
        BELL_HORN,
        BURP_HORN,
        FART_HORN,
        GUN_SOUND,
        LAUGH_SOUND,
        MAN_SOUND,
        MANS_SOUND,
        WOMAN_SOUND,
        WOMANS_SOUND

    }

    public enum LOTTIE_AD_TYPE {
        STATIC,
        DYNAMIC
    }


}

