package com.iten.tenoku.ad.Qureka;


import static com.iten.tenoku.utils.AdConstant.qurekaLinksList;
import static com.iten.tenoku.utils.AdUtils.getOpacityColor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.browser.customtabs.CustomTabsIntent;

import com.bumptech.glide.Glide;
import com.iten.tenoku.R;
import com.iten.tenoku.databinding.CustomNativeBannerAdLayoutBinding;
import com.iten.tenoku.databinding.CustomNativeBigAdBtnTopLayoutBinding;
import com.iten.tenoku.databinding.CustomNativeBigAdLayoutBinding;
import com.iten.tenoku.databinding.CustomNativeMeduimAdLayoutBinding;
import com.iten.tenoku.model.QurekaInterstitialAdModel;
import com.iten.tenoku.utils.AdConstant;
import com.iten.tenoku.utils.MyApplication;

import java.util.Random;


public class QuerkaNative {

    public static QurekaInterstitialAdModel qurekaInterstitialAdModel;
    @SuppressLint("StaticFieldLeak")
    private static QuerkaNative mInstance;
    @SuppressLint("StaticFieldLeak")
    private static Activity activity;
    String TAG = getClass().getSimpleName();
    boolean show = false;

    public QuerkaNative(Activity activity) {
        QuerkaNative.activity = activity;

    }

    public static QuerkaNative getInstance(Activity adShowingActivity) {
        QuerkaNative.activity = adShowingActivity;
        if (mInstance == null) {
            mInstance = new QuerkaNative(adShowingActivity);
        }
        return mInstance;
    }

    public void ShowQuerkaAd() {
        if (show) {
            show = false;
            try {
                int randomNum = new Random().nextInt(qurekaLinksList.size());
                String adLink = qurekaLinksList.get(randomNum);
                final CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().setToolbarColor(activity.getResources().getColor(R.color.ad_end_color)).build();
                customTabsIntent.launchUrl(activity, Uri.parse(adLink));

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            show = true;
            try {
                int randomNum = new Random().nextInt(qurekaLinksList.size());
                String adLink = qurekaLinksList.get(randomNum);
                final CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().setToolbarColor(activity.getResources().getColor(R.color.ad_end_color)).build();
                customTabsIntent.launchUrl(activity, Uri.parse(adLink));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void QuerkaAd(ImageView qureka1, ImageView qureka2, TextView title1, TextView title2, View root) {
        if (AdConstant.iconUrlList.isEmpty()) {
            root.setVisibility(View.GONE);
        } else {
            if (!MyApplication.sharedPreferencesHelper.getQurekaLite()) {
                root.setVisibility(View.GONE);
            }

            if (title1 != null && !MyApplication.sharedPreferencesHelper.getAdButtonColor().isEmpty()) {
                title1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(MyApplication.sharedPreferencesHelper.getAdButtonColor())));
            }
            if (title2 != null && !MyApplication.sharedPreferencesHelper.getAdButtonColor().isEmpty()) {
                title2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(MyApplication.sharedPreferencesHelper.getAdButtonColor())));
            }
            if (title1 != null && !MyApplication.sharedPreferencesHelper.getAdButtonTextColor().isEmpty()) {
                title1.setTextColor(Color.parseColor(MyApplication.sharedPreferencesHelper.getAdButtonTextColor()));
            }
            if (title2 != null && !MyApplication.sharedPreferencesHelper.getAdButtonTextColor().isEmpty()) {
                title2.setTextColor(Color.parseColor(MyApplication.sharedPreferencesHelper.getAdButtonTextColor()));
            }

            //Set Qureka Icon
            if (qureka1 != null) {
                Glide.with(activity).load(AdConstant.getRandomLogo()).error(R.drawable.predchamp_1).into(qureka1);
            }
            if (qureka2 != null) {
                Glide.with(activity).load(AdConstant.getRandomLogo()).error(R.drawable.predchamp_1).into(qureka2);
            }
            root.setOnClickListener(view -> {
                ShowQuerkaAd();

            });
        }
    }

    public void ShowQurekaBannerAd(ViewGroup nativeAdContainer) {
        ShowQuerkaNormalBannerAd(nativeAdContainer);
    }

    private void ShowQuerkaNormalBannerAd(ViewGroup nativeAdContainer) {
        nativeAdContainer.setVisibility(View.VISIBLE);
        qurekaInterstitialAdModel = AdConstant.getRandomQurekaInterstitalAd();
        CustomNativeBannerAdLayoutBinding smallNativeAdLayoutBinding = CustomNativeBannerAdLayoutBinding.inflate(LayoutInflater.from(activity));
        smallNativeAdLayoutBinding.textAdTitle.setText(qurekaInterstitialAdModel.getTitle());
        smallNativeAdLayoutBinding.textAdBody.setText(qurekaInterstitialAdModel.getDescription());
        smallNativeAdLayoutBinding.buttonCallToAction.setText(R.string.play_game);
        Glide.with(activity).load(AdConstant.getRandomLogo()).error(R.drawable.predchamp_1).into(smallNativeAdLayoutBinding.imageAdIcon);
        nativeAdContainer.removeAllViews();
        nativeAdContainer.addView(smallNativeAdLayoutBinding.getRoot());
        if (!MyApplication.sharedPreferencesHelper.getAdTitleColor().isEmpty()) {
            smallNativeAdLayoutBinding.textAdTitle.setTextColor(Color.parseColor(MyApplication.sharedPreferencesHelper.getAdTitleColor()));
        }
        if (!MyApplication.sharedPreferencesHelper.getAdDescriptionColor().isEmpty()) {
            smallNativeAdLayoutBinding.textAdBody.setTextColor(Color.parseColor(MyApplication.sharedPreferencesHelper.getAdDescriptionColor()));
        }
        if (!MyApplication.sharedPreferencesHelper.getAdButtonTextColor().isEmpty()) {
            smallNativeAdLayoutBinding.buttonCallToAction.setTextColor(Color.parseColor(MyApplication.sharedPreferencesHelper.getAdButtonTextColor()));
            smallNativeAdLayoutBinding.textTitle.setTextColor(Color.parseColor(MyApplication.sharedPreferencesHelper.getAdButtonTextColor()));
        }
        if (!MyApplication.sharedPreferencesHelper.getAdButtonColor().isEmpty()) {
            getOpacityColor(smallNativeAdLayoutBinding.buttonCallToAction.getBackground(), MyApplication.sharedPreferencesHelper.getAdButtonColorOpacity(), false);
            getOpacityColor(smallNativeAdLayoutBinding.textTitle.getBackground(), MyApplication.sharedPreferencesHelper.getAdButtonColorOpacity(), false);
        }
        if (!MyApplication.sharedPreferencesHelper.getAddBgColor().isEmpty()) {
            getOpacityColor(smallNativeAdLayoutBinding.smallNativeView.getBackground(), MyApplication.sharedPreferencesHelper.getAdBgColorOpacity(), true);
        }

        smallNativeAdLayoutBinding.buttonCallToAction.setOnClickListener(V -> QuerkaNative.getInstance(activity).ShowQuerkaAd());
        nativeAdContainer.setOnClickListener(V -> QuerkaNative.getInstance(activity).ShowQuerkaAd());
    }

    public void ShowQurekaMediumAd(ViewGroup nativeAdContainer) {
        nativeAdContainer.setVisibility(View.VISIBLE);

        qurekaInterstitialAdModel = AdConstant.getRandomQurekaInterstitalAd();
        CustomNativeMeduimAdLayoutBinding mediumNativeAdLayoutBinding = CustomNativeMeduimAdLayoutBinding.inflate(LayoutInflater.from(activity));
        Glide.with(activity).load(AdConstant.getNativeRandomImage()).error(R.drawable.ic_qureka_native_error_2).into(mediumNativeAdLayoutBinding.nativeAdMediaView);
        Glide.with(activity).load(AdConstant.getRandomLogo()).error(R.drawable.predchamp_1).into(mediumNativeAdLayoutBinding.imageAdIcon);
        mediumNativeAdLayoutBinding.textAdTitle.setText(qurekaInterstitialAdModel.getTitle());
        mediumNativeAdLayoutBinding.textAdBody.setText(qurekaInterstitialAdModel.getDescription());
        mediumNativeAdLayoutBinding.buttonCallToAction.setText(qurekaInterstitialAdModel.getButton());

        nativeAdContainer.removeAllViews();
        nativeAdContainer.addView(mediumNativeAdLayoutBinding.getRoot());

        if (!MyApplication.sharedPreferencesHelper.getAdTitleColor().isEmpty()) {
            mediumNativeAdLayoutBinding.textAdTitle.setTextColor(Color.parseColor(MyApplication.sharedPreferencesHelper.getAdTitleColor()));
        }
        if (!MyApplication.sharedPreferencesHelper.getAdDescriptionColor().isEmpty()) {
            mediumNativeAdLayoutBinding.textAdBody.setTextColor(Color.parseColor(MyApplication.sharedPreferencesHelper.getAdDescriptionColor()));
        }
        if (!MyApplication.sharedPreferencesHelper.getAdButtonTextColor().isEmpty()) {
            mediumNativeAdLayoutBinding.buttonCallToAction.setTextColor(Color.parseColor(MyApplication.sharedPreferencesHelper.getAdButtonTextColor()));
            mediumNativeAdLayoutBinding.textTitle.setTextColor(Color.parseColor(MyApplication.sharedPreferencesHelper.getAdButtonTextColor()));
        }
        if (!MyApplication.sharedPreferencesHelper.getAdButtonColor().isEmpty()) {
            getOpacityColor(mediumNativeAdLayoutBinding.buttonCallToAction.getBackground(), MyApplication.sharedPreferencesHelper.getAdButtonColorOpacity(), false);
            getOpacityColor(mediumNativeAdLayoutBinding.textTitle.getBackground(), MyApplication.sharedPreferencesHelper.getAdButtonColorOpacity(), false);
        }
        if (!MyApplication.sharedPreferencesHelper.getAddBgColor().isEmpty()) {
            getOpacityColor(mediumNativeAdLayoutBinding.bigNative.getBackground(), MyApplication.sharedPreferencesHelper.getAdBgColorOpacity(), true);
        }

        mediumNativeAdLayoutBinding.buttonCallToAction.setOnClickListener(V -> QuerkaNative.getInstance(activity).ShowQuerkaAd());
        nativeAdContainer.setOnClickListener(V -> QuerkaNative.getInstance(activity).ShowQuerkaAd());

    }

    public void ShowQurekaBigAd(ViewGroup nativeAdContainer) {
        nativeAdContainer.setVisibility(View.VISIBLE);
        qurekaInterstitialAdModel = AdConstant.getRandomQurekaInterstitalAd();
        displayNativeBig(nativeAdContainer);
    }

    private void displayNativeBig(ViewGroup nativeAdContainer) {
        CustomNativeBigAdLayoutBinding bigNativeAdLayoutBinding = CustomNativeBigAdLayoutBinding.inflate(LayoutInflater.from(activity));
        bigNativeAdLayoutBinding.textAdTitle.setText(qurekaInterstitialAdModel.getTitle());
        bigNativeAdLayoutBinding.textAdBody.setText(qurekaInterstitialAdModel.getDescription());
        bigNativeAdLayoutBinding.buttonCallToAction.setText(qurekaInterstitialAdModel.getButton());
        Glide.with(activity).load(AdConstant.getNativeRandomImage()).error(R.drawable.ic_qureka_native_error).into(bigNativeAdLayoutBinding.nativeAdMediaView);
        Glide.with(activity).load(AdConstant.getRandomLogo()).error(R.drawable.predchamp_1).into(bigNativeAdLayoutBinding.imageAdIcon);
        nativeAdContainer.removeAllViews();
        nativeAdContainer.addView(bigNativeAdLayoutBinding.getRoot());


        if (!MyApplication.sharedPreferencesHelper.getAdTitleColor().isEmpty()) {
            bigNativeAdLayoutBinding.textAdTitle.setTextColor(Color.parseColor(MyApplication.sharedPreferencesHelper.getAdTitleColor()));
        }
        if (!MyApplication.sharedPreferencesHelper.getAdDescriptionColor().isEmpty()) {
            bigNativeAdLayoutBinding.textAdBody.setTextColor(Color.parseColor(MyApplication.sharedPreferencesHelper.getAdDescriptionColor()));
        }
        if (!MyApplication.sharedPreferencesHelper.getAdButtonTextColor().isEmpty()) {
            bigNativeAdLayoutBinding.buttonCallToAction.setTextColor(Color.parseColor(MyApplication.sharedPreferencesHelper.getAdButtonTextColor()));
            bigNativeAdLayoutBinding.textTitle.setTextColor(Color.parseColor(MyApplication.sharedPreferencesHelper.getAdButtonTextColor()));
        }
        if (!MyApplication.sharedPreferencesHelper.getAdButtonColor().isEmpty()) {
            getOpacityColor(bigNativeAdLayoutBinding.buttonCallToAction.getBackground(), MyApplication.sharedPreferencesHelper.getAdButtonColorOpacity(), false);
            getOpacityColor(bigNativeAdLayoutBinding.textTitle.getBackground(), MyApplication.sharedPreferencesHelper.getAdButtonColorOpacity(), false);
        }
        if (!MyApplication.sharedPreferencesHelper.getAddBgColor().isEmpty()) {
            getOpacityColor(bigNativeAdLayoutBinding.bigNative.getBackground(), MyApplication.sharedPreferencesHelper.getAdBgColorOpacity(), true);
        }

        bigNativeAdLayoutBinding.buttonCallToAction.setOnClickListener(V -> QuerkaNative.getInstance(activity).ShowQuerkaAd());
        nativeAdContainer.setOnClickListener(V -> QuerkaNative.getInstance(activity).ShowQuerkaAd());
    }

    public void ShowQurekaBigBtnTopAd(ViewGroup nativeAdContainer) {
        nativeAdContainer.setVisibility(View.VISIBLE);

        qurekaInterstitialAdModel = AdConstant.getRandomQurekaInterstitalAd();
        CustomNativeBigAdBtnTopLayoutBinding bigNativeAdLayoutBinding = CustomNativeBigAdBtnTopLayoutBinding.inflate(LayoutInflater.from(activity));

        bigNativeAdLayoutBinding.textAdTitle.setText(qurekaInterstitialAdModel.getTitle());
        bigNativeAdLayoutBinding.textAdBody.setText(qurekaInterstitialAdModel.getDescription());
        bigNativeAdLayoutBinding.buttonCallToAction.setText(qurekaInterstitialAdModel.getButton());
        Glide.with(activity).load(AdConstant.getNativeRandomImage()).error(R.drawable.ic_qureka_native_error).into(bigNativeAdLayoutBinding.nativeAdMediaView);
        Glide.with(activity).load(AdConstant.getRandomLogo()).error(R.drawable.predchamp_1).into(bigNativeAdLayoutBinding.imageAdIcon);
        nativeAdContainer.removeAllViews();
        nativeAdContainer.addView(bigNativeAdLayoutBinding.getRoot());


        if (!MyApplication.sharedPreferencesHelper.getAdTitleColor().isEmpty()) {
            bigNativeAdLayoutBinding.textAdTitle.setTextColor(Color.parseColor(MyApplication.sharedPreferencesHelper.getAdTitleColor()));
        }
        if (!MyApplication.sharedPreferencesHelper.getAdDescriptionColor().isEmpty()) {
            bigNativeAdLayoutBinding.textAdBody.setTextColor(Color.parseColor(MyApplication.sharedPreferencesHelper.getAdDescriptionColor()));
        }
        if (!MyApplication.sharedPreferencesHelper.getAdButtonTextColor().isEmpty()) {
            bigNativeAdLayoutBinding.buttonCallToAction.setTextColor(Color.parseColor(MyApplication.sharedPreferencesHelper.getAdButtonTextColor()));
            bigNativeAdLayoutBinding.textTitle.setTextColor(Color.parseColor(MyApplication.sharedPreferencesHelper.getAdButtonTextColor()));
        }
        if (!MyApplication.sharedPreferencesHelper.getAdButtonColor().isEmpty()) {
            getOpacityColor(bigNativeAdLayoutBinding.buttonCallToAction.getBackground(), MyApplication.sharedPreferencesHelper.getAdButtonColorOpacity(), false);
            getOpacityColor(bigNativeAdLayoutBinding.textTitle.getBackground(), MyApplication.sharedPreferencesHelper.getAdButtonColorOpacity(), false);
        }
        if (!MyApplication.sharedPreferencesHelper.getAddBgColor().isEmpty()) {
            getOpacityColor(bigNativeAdLayoutBinding.bigNative.getBackground(), MyApplication.sharedPreferencesHelper.getAdBgColorOpacity(), true);
        }

        bigNativeAdLayoutBinding.buttonCallToAction.setOnClickListener(V -> QuerkaNative.getInstance(activity).ShowQuerkaAd());
        nativeAdContainer.setOnClickListener(V -> QuerkaNative.getInstance(activity).ShowQuerkaAd());

    }
}
