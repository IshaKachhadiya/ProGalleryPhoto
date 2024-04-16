package com.iten.tenoku.model;

import com.facebook.ads.NativeBannerAd;

public class FacebookNativeBannerAdModel {
    String adUnit;
    NativeBannerAd nativebannerAd;

    public FacebookNativeBannerAdModel(String adUnit, NativeBannerAd nativebannerAd) {
        this.adUnit = adUnit;

        this.nativebannerAd = nativebannerAd;
    }

    public String getAdUnit() {
        return adUnit;
    }

    public NativeBannerAd getNativeAd() {
        return nativebannerAd;
    }

}
