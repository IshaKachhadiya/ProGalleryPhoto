package com.iten.tenoku.model;


import com.facebook.ads.NativeAd;

public class FacebookNativeAdModel {
    String adUnit;
    NativeAd nativeAd;

    public FacebookNativeAdModel(String adUnit, NativeAd nativeAd) {
        this.adUnit = adUnit;

        this.nativeAd = nativeAd;
    }

    public String getAdUnit() {
        return adUnit;
    }

    public NativeAd getNativeAd() {
        return nativeAd;
    }

}
