package com.iten.tenoku.ad.AdMob;

import static com.iten.tenoku.utils.MyApplication.sharedPreferencesHelper;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;

import java.util.ArrayList;

public class GoogleCollapseBanner {
    private static final String TAG = "GoogleCollapseBanner";
  public static   ArrayList<String> AdMobCollapseList = new ArrayList<>();
    Activity activity;
    public static GoogleCollapseBanner mInstance;

    AdView adView;
    int CurrentPostion = 0;

    public GoogleCollapseBanner(Activity activity) {
        this.activity = activity;
    }

    public static GoogleCollapseBanner getInstance(Activity activity) {
        if (mInstance == null) {
            mInstance = new GoogleCollapseBanner(activity);
        }
        return mInstance;
    }

    public void ShowCollapseBanner(Activity activity, ViewGroup viewGroup) {
        Log.e(TAG, "ShowCollapseBanner:===>AdMobCollapseList  "+AdMobCollapseList );

        if (AdMobCollapseList != null) {
            if (AdMobCollapseList.size() > 0) {
                if (CurrentPostion == AdMobCollapseList.size()-1) {
                    CurrentPostion = 0;
                } else {
                    CurrentPostion++;
                }
                adView = new AdView(activity);
                adView.setAdUnitId(AdMobCollapseList.get(CurrentPostion));
                Bundle bundle = new Bundle();
                bundle.putString("collapsible", "bottom");
                AdRequest adRequest = new AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, bundle).build();
                adView.setAdSize(getAdSize());
                adView.loadAd(adRequest);

                adView.setAdListener(new AdListener() {
                    @Override
                    public void onAdImpression() {
                        super.onAdImpression();
                    }
                    @Override
                    public void onAdLoaded() {
                        Log.e(TAG, "ShowCollapseBanner:===>onAdLoaded  " );

                        super.onAdLoaded();
                        viewGroup.removeAllViews();
                        if (adView != null) {
                            viewGroup.addView(adView);
                        }
                    }
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        viewGroup.removeAllViews();
                        viewGroup.setVisibility(View.GONE);
                        Log.e(TAG, "ShowCollapseBanner:===>onAdFailedToLoad  " );

                    }
                    @Override
                    public void onAdOpened() {
                        super.onAdOpened();
                    }
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                    }
                    @Override
                    public void onAdClicked() {
                        super.onAdClicked();
                    }


                });
            }
        }
    }

    private AdSize getAdSize() {
        Display defaultDisplay = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(displayMetrics);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this.activity, (int) (displayMetrics.widthPixels / displayMetrics.density));
    }

}
