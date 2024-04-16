package com.iten.tenoku.ad.AdMob;

import static com.iten.tenoku.utils.MyApplication.sharedPreferencesHelper;

import android.annotation.SuppressLint;
import android.app.Activity;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.iten.tenoku.ad.HandleClick.HandleRewardedAd;
import com.iten.tenoku.model.AdMobRewadedAdModel;
import com.iten.tenoku.utils.AdUtils;

import java.util.ArrayList;

public class GoogleRewardedAd {

    private static final String TAG = GoogleRewardedAd.class.getSimpleName();
    public static ArrayList<AdMobRewadedAdModel> adMobRewardedAds = new ArrayList<>();
    public static int CurrentRewardAdPosition = 0;
    @SuppressLint("StaticFieldLeak")
    private static Activity activity;
    @SuppressLint("StaticFieldLeak")
    private static GoogleRewardedAd mInstance;
    Boolean userRewarded = false;
    HandleRewardedAd handleRewardedAd = null;

    public GoogleRewardedAd(Activity activity) {
        GoogleRewardedAd.activity = activity;

    }

    public static GoogleRewardedAd getInstance(Activity adShowingActivity) {
        if (mInstance == null) {
            mInstance = new GoogleRewardedAd(adShowingActivity);
        }
        return mInstance;
    }

    public void LoadAd() {
        if (!adMobRewardedAds.isEmpty()) {
            if (CurrentRewardAdPosition == adMobRewardedAds.size() - 1) CurrentRewardAdPosition = 0;
            else CurrentRewardAdPosition++;

            if (adMobRewardedAds.get(CurrentRewardAdPosition).getRewardedAd() == null) {
                AdRequest adRequest = new AdRequest.Builder().build();
                RewardedAd.load(activity, adMobRewardedAds.get(CurrentRewardAdPosition).getAdUnit(),
                        adRequest, new RewardedAdLoadCallback() {
                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {

                            }
                            @Override
                            public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                                AdMobRewadedAdModel adMobRewadedAdModel = new AdMobRewadedAdModel(adMobRewardedAds.get(CurrentRewardAdPosition).getAdUnit(), rewardedAd);
                                adMobRewardedAds.set(CurrentRewardAdPosition, adMobRewadedAdModel);
                            }
                        });
            }

        }


    }

    public void ShowRewardedAd(HandleRewardedAd handleRewardedAdActivity) {
        this.handleRewardedAd = handleRewardedAdActivity;
        if (sharedPreferencesHelper.getShowAdinApp()) {
            if (!adMobRewardedAds.isEmpty()) {
                for (int i = 0; i < adMobRewardedAds.size(); i++) {
                    if (adMobRewardedAds.get(i).getRewardedAd() != null) {
                        ShowAd(adMobRewardedAds.get(i).getRewardedAd(), i);
                        return;
                    }
                }
                ShowRetryRewardedAd();
            } else {
                HandleRewardAd(false);
            }
        } else {
            HandleRewardAd(true);
        }
    }

    private void ShowRetryRewardedAd() {
        if (!adMobRewardedAds.isEmpty()) {
            if (CurrentRewardAdPosition == adMobRewardedAds.size() - 1) CurrentRewardAdPosition = 0;
            else CurrentRewardAdPosition++;
            AdRequest adRequest = new AdRequest.Builder().build();
            RewardedAd.load(activity, adMobRewardedAds.get(CurrentRewardAdPosition).getAdUnit(),
                    adRequest, new RewardedAdLoadCallback() {
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            LoadAd();
                            HandleRewardAd(false);

                        }
                        @Override
                        public void onAdLoaded(@NonNull final RewardedAd rewardedAd) {
                            AdUtils.dismissAdLoading();
                            rewardedAd.show(activity, new OnUserEarnedRewardListener() {
                                @Override
                                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                    userRewarded = true;
                                }
                            });
                            rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                    LoadAd();
                                    HandleRewardAd(false);
                                }

                                @Override
                                public void onAdShowedFullScreenContent() {
                                    LoadAd();

                                }

                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    if (userRewarded) {
                                        userRewarded = false;
                                        HandleRewardAd(true);
                                    }

                                }
                            });
                        }
                    });
        } else {
            HandleRewardAd(false);
        }

    }

    private void ShowAd(RewardedAd rewardedAd, final int i) {
        if (rewardedAd != null) {
            AdUtils.dismissAdLoading();
            rewardedAd.show(activity, rewardItem -> userRewarded = true);
            rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    AdMobRewadedAdModel adMobRewadedAdModel = new AdMobRewadedAdModel(adMobRewardedAds.get(i).getAdUnit(), null);
                    adMobRewardedAds.set(i, adMobRewadedAdModel);
                    ShowRetryRewardedAd();
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    AdMobRewadedAdModel adMobRewadedAdModel = new AdMobRewadedAdModel(adMobRewardedAds.get(i).getAdUnit(), null);
                    adMobRewardedAds.set(i, adMobRewadedAdModel);
                    LoadAd();
                }
                @Override
                public void onAdDismissedFullScreenContent() {
                    if (userRewarded) {
                        userRewarded = false;
                        HandleRewardAd(true);
                    }
                }
            });
        } else {
            ShowRetryRewardedAd();
        }
    }

    void HandleRewardAd(Boolean AdShowed) {
        if (handleRewardedAd != null) {
            handleRewardedAd.Show(AdShowed);
            handleRewardedAd = null;
        }

    }
}
