package com.iten.tenoku.model;

import com.google.gson.annotations.SerializedName;

   
public class ObjUnity {

   @SerializedName("isAdShowAdStatus")
   boolean isAdShowAdStatus;

   @SerializedName("iAppID")
   String iAppID;

   @SerializedName("vInterstitial")
   String vInterstitial;

   @SerializedName("vRewardedVideo")
   String vRewardedVideo;


    public void setIsAdShowAdStatus(boolean isAdShowAdStatus) {
        this.isAdShowAdStatus = isAdShowAdStatus;
    }
    public boolean getIsAdShowAdStatus() {
        return isAdShowAdStatus;
    }
    
    public void setIAppID(String iAppID) {
        this.iAppID = iAppID;
    }
    public String getIAppID() {
        return iAppID;
    }
    
    public void setVInterstitial(String vInterstitial) {
        this.vInterstitial = vInterstitial;
    }
    public String getVInterstitial() {
        return vInterstitial;
    }
    
    public void setVRewardedVideo(String vRewardedVideo) {
        this.vRewardedVideo = vRewardedVideo;
    }
    public String getVRewardedVideo() {
        return vRewardedVideo;
    }
    
}