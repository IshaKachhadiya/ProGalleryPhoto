package com.iten.tenoku.ad.HandleClick;

public interface InterstitialAdListener {
    void onAdLoaded();

    void onAdClosed();

    void onAdShown();

    void onApplicationLeft();

    void onAdFailedToLoad(Exception exception);
}
