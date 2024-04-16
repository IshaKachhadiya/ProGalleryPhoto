package com.iten.tenoku.model;
import java.util.List;

import com.google.gson.annotations.SerializedName;

   
public class ObjQureka {

   @SerializedName("isQurekaLiteShow")
   boolean isQurekaLiteShow;

   @SerializedName("isQurekaClose")
   boolean isQurekaClose;

   @SerializedName("vQurekaLink")
   String vQurekaLink;

   @SerializedName("vPredchampLink")
   String vPredchampLink;

   @SerializedName("arrIconUrls")
   List<ArrIconUrls> arrIconUrls;

   @SerializedName("arrInterstitialImageUrls")
   List<ArrInterstitialImageUrls> arrInterstitialImageUrls;

   @SerializedName("arrNativeImageUrls")
   List<ArrNativeImageUrls> arrNativeImageUrls;

   @SerializedName("iQurekaShowCount")
   String iQurekaShowCount;

   @SerializedName("arrQurekaLinks")
   List<ArrQurekaLinks> arrQurekaLinks;


    public void setIsQurekaLiteShow(boolean isQurekaLiteShow) {
        this.isQurekaLiteShow = isQurekaLiteShow;
    }
    public boolean getIsQurekaLiteShow() {
        return isQurekaLiteShow;
    }
    
    public void setIsQurekaClose(boolean isQurekaClose) {
        this.isQurekaClose = isQurekaClose;
    }
    public boolean getIsQurekaClose() {
        return isQurekaClose;
    }
    
    public void setVQurekaLink(String vQurekaLink) {
        this.vQurekaLink = vQurekaLink;
    }
    public String getVQurekaLink() {
        return vQurekaLink;
    }
    
    public void setVPredchampLink(String vPredchampLink) {
        this.vPredchampLink = vPredchampLink;
    }
    public String getVPredchampLink() {
        return vPredchampLink;
    }
    
    public void setArrIconUrls(List<ArrIconUrls> arrIconUrls) {
        this.arrIconUrls = arrIconUrls;
    }
    public List<ArrIconUrls> getArrIconUrls() {
        return arrIconUrls;
    }
    
    public void setArrInterstitialImageUrls(List<ArrInterstitialImageUrls> arrInterstitialImageUrls) {
        this.arrInterstitialImageUrls = arrInterstitialImageUrls;
    }
    public List<ArrInterstitialImageUrls> getArrInterstitialImageUrls() {
        return arrInterstitialImageUrls;
    }
    
    public void setArrNativeImageUrls(List<ArrNativeImageUrls> arrNativeImageUrls) {
        this.arrNativeImageUrls = arrNativeImageUrls;
    }
    public List<ArrNativeImageUrls> getArrNativeImageUrls() {
        return arrNativeImageUrls;
    }
    
    public void setIQurekaShowCount(String iQurekaShowCount) {
        this.iQurekaShowCount = iQurekaShowCount;
    }
    public String getIQurekaShowCount() {
        return iQurekaShowCount;
    }
    
    public void setArrQurekaLinks(List<ArrQurekaLinks> arrQurekaLinks) {
        this.arrQurekaLinks = arrQurekaLinks;
    }
    public List<ArrQurekaLinks> getArrQurekaLinks() {
        return arrQurekaLinks;
    }
    
}