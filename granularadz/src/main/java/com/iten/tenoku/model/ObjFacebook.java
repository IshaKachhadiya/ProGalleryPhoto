package com.iten.tenoku.model;
import java.util.List;

import com.google.gson.annotations.SerializedName;

   
public class ObjFacebook {

   @SerializedName("isAdShowAdStatus")
   boolean isAdShowAdStatus;

   @SerializedName("arrNativeBanner")
   List<ArrNativeBanner> arrNativeBanner;

   @SerializedName("arrInterstitial")
   List<ArrInterstitial> arrInterstitial;

   @SerializedName("arrNative")
   List<ArrNative> arrNative;

   @SerializedName("arrRewardedVideo")
   List<ArrRewardedVideo> arrRewardedVideo;

   @SerializedName("iFacebookAppID")
   String iFacebookAppID;

   @SerializedName("vFacebookClientToken")
   String vFacebookClientToken;


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
    
    public void setIFacebookAppID(String iFacebookAppID) {
        this.iFacebookAppID = iFacebookAppID;
    }
    public String getIFacebookAppID() {
        return iFacebookAppID;
    }
    
    public void setVFacebookClientToken(String vFacebookClientToken) {
        this.vFacebookClientToken = vFacebookClientToken;
    }
    public String getVFacebookClientToken() {
        return vFacebookClientToken;
    }
    
}