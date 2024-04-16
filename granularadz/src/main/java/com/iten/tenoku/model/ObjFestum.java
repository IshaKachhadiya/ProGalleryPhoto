package com.iten.tenoku.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ObjFestum {
    @SerializedName("isShowStatus")
    boolean isShowStatus;
    @SerializedName("isOwnInterClose")
    boolean isOwnInterClose;
    @SerializedName("iNativeNo")
    String iNativeNo;
    @SerializedName("iBannerNo")
    String iBannerNo;
    @SerializedName("iInterstiatialsNo")
    String iInterstiatialsNo;
    @SerializedName("iOpenAdNo")
    String iOpenAdNo;

    @SerializedName("arrNative")
    List<FestumresponseModel> arrNative;

    @SerializedName("arrNativeBanner")
    List<FestumresponseModel> arrBanner;

    @SerializedName("arrInterstiatial")
    List<FestumresponseModel> arrInterstitial;

    @SerializedName("arrOpenAd")
    List<FestumresponseModel> arrAppOpen;

    public boolean getisShowStatus() {
        return isShowStatus;
    }

    public boolean getOwnInterClose() {
        return isOwnInterClose;
    }

    public String getiNativeNo() {
        return iNativeNo;
    }

    public String getiBannerNo() {
        return iBannerNo;
    }

    public String getiInterstiatialsNo() {
        return iInterstiatialsNo;
    }

    public String getiOpenAdNo() {
        return iOpenAdNo;
    }

    public List<FestumresponseModel> getArrBanner() {
        return arrBanner;
    }

    public List<FestumresponseModel> getArrInterstitial() {
        return arrInterstitial;
    }

    public List<FestumresponseModel> getArrNative() {
        return arrNative;
    }

    public List<FestumresponseModel> getArrAppOpen() {
        return arrAppOpen;
    }
}
