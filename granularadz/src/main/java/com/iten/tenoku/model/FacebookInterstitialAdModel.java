package com.iten.tenoku.model;


import com.facebook.ads.InterstitialAd;

public class FacebookInterstitialAdModel {
    String adUnit;

    InterstitialAd interstitialAd;

    public FacebookInterstitialAdModel(String adUnit, InterstitialAd interstitialAd) {
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
