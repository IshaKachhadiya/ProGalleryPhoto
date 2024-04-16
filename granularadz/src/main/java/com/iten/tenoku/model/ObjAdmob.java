package com.iten.tenoku.model;
import java.util.List;

import com.google.gson.annotations.SerializedName;


public class ObjAdmob {

   @SerializedName("iAppID")
   String iAppID;

    @SerializedName("iBannerID")
    String iBannerID;

   @SerializedName("arrBanner")
   List<ArrBanner> arrBanner;

   @SerializedName("arrInterstitial")
   List<ArrInterstitial> arrInterstitial;

   @SerializedName("arrNative")
   List<ArrNative> arrNative;

   @SerializedName("arrRewardedVideo")
   List<ArrRewardedVideo> arrRewardedVideo;

   @SerializedName("arrAppOpen")
   List<ArrAppOpen> arrAppOpen;

   @SerializedName("isAdShowAdStatus")
   boolean isAdShowAdStatus;

   @SerializedName("arrNativeBanner")
   List<ArrNativeBanner> arrNativeBanner;

    public String getiBannerID() {
        return iBannerID;
    }

    public void setiBannerID(String iBannerID) {
        this.iBannerID = iBannerID;
    }

    public void setIAppID(String iAppID) {
        this.iAppID = iAppID;
    }
    public String getIAppID() {
        return iAppID;
    }

    public void setArrBanner(List<ArrBanner> arrBanner) {
        this.arrBanner = arrBanner;
    }
    public List<ArrBanner> getArrBanner() {
        return arrBanner;
    }

    public void setArrInterstitial(List<ArrInterstitial> arrInterstitial) {
        this.arrInterstitial = arrInterstitial;
    }
    public List<ArrInterstitial> getArrInterstitial() {
        return arrInterstitial;
    }

    public void setArrNative(List<ArrNative> arrNative) {
        this.arrNative = arrNative;
    }
    public List<ArrNative> getArrNative() {
        return arrNative;
    }

    public void setArrRewardedVideo(List<ArrRewardedVideo> arrRewardedVideo) {
        this.arrRewardedVideo = arrRewardedVideo;
    }
    public List<ArrRewardedVideo> getArrRewardedVideo() {
        return arrRewardedVideo;
    }

    public void setArrAppOpen(List<ArrAppOpen> arrAppOpen) {
        this.arrAppOpen = arrAppOpen;
    }
    public List<ArrAppOpen> getArrAppOpen() {
        return arrAppOpen;
    }

    public void setIsAdShowAdStatus(boolean isAdShowAdStatus) {
        this.isAdShowAdStatus = isAdShowAdStatus;
    }
    public boolean getIsAdShowAdStatus() {
        return isAdShowAdStatus;
    }

    public void setArrNativeBanner(List<ArrNativeBanner> arrNativeBanner) {
        this.arrNativeBanner = arrNativeBanner;
    }
    public List<ArrNativeBanner> getArrNativeBanner() {
        return arrNativeBanner;
    }

}