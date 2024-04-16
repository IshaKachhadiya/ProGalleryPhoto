package com.iten.tenoku.model;

import com.google.android.gms.ads.nativead.NativeAd;

public class GoogleNativeBannerAdModel {
    String adUnit;
    NativeAd nativeAd;

    public GoogleNativeBannerAdModel(String adUnit, NativeAd nativeAd) {
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
