package com.iten.tenoku.model;

import com.google.gson.annotations.SerializedName;

public class CommonSettings {

    @SerializedName("Under Maintenance")
    public String underMaintenance;
    @SerializedName("Show Entry Screen")
    public String showEntryScreen;
    @SerializedName("Exit Screen")
    public String exitScreen;
    @SerializedName("List Recyclerview Per Item")
    public String listRecyclerviewPerItem;
    @SerializedName("Grid Recyclerview Per Item Two")
    public String gridRecyclerviewPerItemTwo;
    @SerializedName("Grid Recyclerview Per item Three")
    public String gridRecyclerviewPerItemThree;
    @SerializedName("Click Country List")
    public String clickCountryList;
    @SerializedName("Show Interstitial Click Count")
    public String showInterstitialClickCount;
    @SerializedName("Common Feature Hide")
    public String commonFeatureHide;
    @SerializedName("App Open Native Hide Country List")
    public String appOpenNativeHideCountry;
    @SerializedName("Ad Bg Color")
    public String AdBgColor;
    @SerializedName("Ad Button Color")
    public String adButtonColor;
    @SerializedName("Ad Button Text Color")
    public String AdButtonTextColor;
    @SerializedName("Ad Title Color")
    public String AdTitleColor;
    @SerializedName("Ad Disc Color")
    public String AdDescriptionColor;
    @SerializedName("Native Big Hide Country List")
    public String NativeBigHideCountryList;
    @SerializedName("Native Banner Hide Country List")
    public String NativeBannerHideCountryList;
    @SerializedName("OnResume")
    public String OnAdsResume;
    @SerializedName("Preload")
    public String Preload;
    @SerializedName("Native Refresh Second")
    public String refreshCounter;
    @SerializedName("Inter Native Ad Sequence Counter ")
    public String interNativeAdSequenceCounter;

    public String getInterNativeAdSequenceCounter() {
        return interNativeAdSequenceCounter;
    }

    public String geRefreshCounter() {
        return refreshCounter;
    }

    public String getIsOnAdsResume() {
        return OnAdsResume;
    }

    public String getPreLoad() {
        return Preload;
    }

    public String getNativeBigHideCountryList() {
        return NativeBigHideCountryList;
    }

    public String getNativeBannerHideCountryList() {
        return NativeBannerHideCountryList;
    }

    public String getUnderMaintenance() {
        return underMaintenance;
    }

    public String getShowEntryScreen() {
        return showEntryScreen;
    }

    public String getExitScreen() {
        return exitScreen;
    }

    public String getListRecyclerviewPerItem() {
        return listRecyclerviewPerItem;
    }

    public String getGridRecyclerviewPerItemTwo() {
        return gridRecyclerviewPerItemTwo;
    }

    public String getGridRecyclerviewPerItemThree() {
        return gridRecyclerviewPerItemThree;
    }

    public String getClickCountryList() {
        return clickCountryList;
    }

    public String getShowInterstitialClickCount() {
        return showInterstitialClickCount;
    }

    public String getCommonFeatureHide() {
        return commonFeatureHide;
    }

    public String getAppOpenNativeHideCountry() {
        return appOpenNativeHideCountry;
    }

    public String getAdBgColor() {
        return AdBgColor;
    }

    public String getAdButtonColor() {
        return adButtonColor;
    }

    public String getAdButtonTextColor() {
        return AdButtonTextColor;
    }

    public String getAdTitleColor() {
        return AdTitleColor;
    }

    public String getAdDescriptionColor() {
        return AdDescriptionColor;
    }
}