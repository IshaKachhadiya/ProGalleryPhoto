package com.iten.tenoku.model;

import com.google.android.gms.ads.interstitial.InterstitialAd;

public class GoogleInterstitialAdModel {
    String adUnit;

    InterstitialAd interstitialAd;

    public GoogleInterstitialAdModel(String adUnit, InterstitialAd interstitialAd) {
        this.adUnit = adUnit;

        this.interstitialAd = interstitialAd;
    }

    public String getAdUnit() {
        return adUnit;
    }

    public InterstitialAd getInterstitialAd() {
        return interstitialAd;
    }

}
