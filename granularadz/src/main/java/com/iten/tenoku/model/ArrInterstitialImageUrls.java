package com.iten.tenoku.model;

import com.google.gson.annotations.SerializedName;

   
public class ArrInterstitialImageUrls {

   @SerializedName("iId")
   String iId;


    public void setIId(String iId) {
        this.iId = iId;
    }
    public String getIId() {
        return iId;
    }
    
}