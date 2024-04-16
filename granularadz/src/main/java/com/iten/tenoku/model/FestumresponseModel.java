package com.iten.tenoku.model;

import com.google.gson.annotations.SerializedName;

public class FestumresponseModel {
    @SerializedName("vRedirectUrl")
    String vRedirectUrl;

    @SerializedName("vNativeBannerUrl")
    String vNativeBannerUrl;
    @SerializedName("vVideoUrl")
    String vVideoUrl;

    @SerializedName("vLogoUrl")
    String vLogoUrl;
    @SerializedName("vTittle")
    String vTittle;
    @SerializedName("vDescipation")
    String vDescipation;
    @SerializedName("iShowTimer")
    String iShowTimer;
    @SerializedName("isSkip")
    boolean isSkip;

    public FestumresponseModel(String vRedirectUrl, String vNativeBannerUrl, String vVideoUrl, String vLogoUrl, String vTittle, String vDescipation, String iShowTimer, boolean isSkip) {
        this.vRedirectUrl = vRedirectUrl;
        this.vNativeBannerUrl = vNativeBannerUrl;
        this.vVideoUrl = vVideoUrl;
        this.vLogoUrl = vLogoUrl;
        this.vTittle = vTittle;
        this.vDescipation = vDescipation;
        this.iShowTimer = iShowTimer;
        this.isSkip = isSkip;
    }

    public String getvRedirectUrl() {
        return vRedirectUrl;
    }

    public String getvNativeBannerUrl() {
        return vNativeBannerUrl;
    }

    public String getvVideoUrl() {
        return vVideoUrl;
    }

    public String getvLogoUrl() {
        return vLogoUrl;
    }

    public String getvTittle() {
        return vTittle;
    }

    public String getvDescipation() {
        return vDescipation;
    }

    public String getiShowTimer() {
        return iShowTimer;
    }

    public boolean isSkip() {
        return isSkip;
    }
}
