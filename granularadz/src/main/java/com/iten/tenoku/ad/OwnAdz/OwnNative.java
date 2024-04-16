package com.iten.tenoku.ad.OwnAdz;

import static com.iten.tenoku.utils.AdConstant.festumNativeAdModels;
import static com.iten.tenoku.utils.AdConstant.festumNativeBannerAdModels;
import static com.iten.tenoku.utils.AdUtils.firebaseEvent;
import static com.iten.tenoku.utils.AdUtils.getOpacityColor;
import static com.iten.tenoku.utils.MyApplication.sharedPreferencesHelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.browser.customtabs.CustomTabsIntent;

import com.bumptech.glide.Glide;
import com.iten.tenoku.R;
import com.iten.tenoku.databinding.OwnNativeBannerAdLayoutBinding;
import com.iten.tenoku.databinding.OwnNativeBigAdLayoutBinding;
import com.iten.tenoku.databinding.OwnNativeMeduimAdLayoutBinding;
import com.iten.tenoku.utils.MyApplication;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OwnNative {
    @SuppressLint("StaticFieldLeak")
    private static OwnNative mInstance;
    @SuppressLint("StaticFieldLeak")
    private static Activity activity;
    Handler executors = new Handler(Looper.getMainLooper());

    public OwnNative(Activity activity) {
        OwnNative.activity = activity;
    }

    public static synchronized OwnNative getInstance(Activity adShowingActivity) {
        OwnNative.activity = adShowingActivity;
        if (mInstance == null) {
            mInstance = new OwnNative(adShowingActivity);
        }
        return mInstance;
    }


    public void ShowOwnNative(ViewGroup nativeAdContainer) {
        firebaseEvent("OwnAdsNativeBig OwnShow " + sharedPreferencesHelper.getOwnNativePosition());
        nativeAdContainer.setVisibility(View.VISIBLE);
        OwnNativeBigAdLayoutBinding bigNativeLayout = OwnNativeBigAdLayoutBinding.inflate(LayoutInflater.from(activity));
        bigNativeLayout.nativeAdMediaView.setVisibility(View.INVISIBLE);
        bigNativeLayout.nativeAdVideoView.setVisibility(View.VISIBLE);
        executors.post(() -> {
            bigNativeLayout.nativeAdVideoView.setVideoURI(Uri.parse(festumNativeAdModels.get(sharedPreferencesHelper.getOwnNativePosition()).getvVideoUrl()));
            bigNativeLayout.nativeAdVideoView.requestFocus();
            bigNativeLayout.nativeAdVideoView.setOnPreparedListener(mediaPlayer -> bigNativeLayout.nativeAdVideoView.start());
            bigNativeLayout.nativeAdVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    bigNativeLayout.nativeAdMediaView.setVisibility(View.VISIBLE);
                    bigNativeLayout.nativeAdVideoView.setVisibility(View.INVISIBLE);
                    Glide.with(activity).load(festumNativeAdModels.get(sharedPreferencesHelper.getOwnNativePosition()).getvNativeBannerUrl()).error(R.drawable.predchamp_1).into(bigNativeLayout.nativeAdMediaView);
                }
            });
            bigNativeLayout.nativeAdVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    return true;
                }
            });
        });

        bigNativeLayout.textAdTitle.setText(festumNativeAdModels.get(sharedPreferencesHelper.getOwnNativePosition()).getvTittle());
        bigNativeLayout.textAdBody.setText(festumNativeAdModels.get(sharedPreferencesHelper.getOwnNativePosition()).getvDescipation());
        bigNativeLayout.buttonCallToAction.setText(R.string.install);
        Glide.with(activity).load(festumNativeAdModels.get(sharedPreferencesHelper.getOwnNativePosition()).getvLogoUrl()).error(R.drawable.predchamp_1).into(bigNativeLayout.imageAdIcon);
        nativeAdContainer.removeAllViews();
        nativeAdContainer.addView(bigNativeLayout.getRoot());

        if (!sharedPreferencesHelper.getAdTitleColor().isEmpty()) {
            bigNativeLayout.textAdTitle.setTextColor(Color.parseColor(MyApplication.sharedPreferencesHelper.getAdTitleColor()));
        }
        if (!MyApplication.sharedPreferencesHelper.getAdDescriptionColor().isEmpty()) {
            bigNativeLayout.textAdBody.setTextColor(Color.parseColor(MyApplication.sharedPreferencesHelper.getAdDescriptionColor()));
        }
        if (!MyApplication.sharedPreferencesHelper.getAdButtonTextColor().isEmpty()) {
            bigNativeLayout.buttonCallToAction.setTextColor(Color.parseColor(MyApplication.sharedPreferencesHelper.getAdButtonTextColor()));
            bigNativeLayout.textTitle.setTextColor(Color.parseColor(MyApplication.sharedPreferencesHelper.getAdButtonTextColor()));
        }
        if (!MyApplication.sharedPreferencesHelper.getAdButtonColor().isEmpty()) {
            getOpacityColor(bigNativeLayout.buttonCallToAction.getBackground(), MyApplication.sharedPreferencesHelper.getAdButtonColorOpacity(), false);
            getOpacityColor(bigNativeLayout.textTitle.getBackground(), MyApplication.sharedPreferencesHelper.getAdButtonColorOpacity(), false);
        }
        if (!MyApplication.sharedPreferencesHelper.getAddBgColor().isEmpty()) {
            getOpacityColor(bigNativeLayout.bigNative.getBackground(), MyApplication.sharedPreferencesHelper.getAdBgColorOpacity(), true);
        }

        bigNativeLayout.buttonCallToAction.setOnClickListener(view -> ShowNativeRedirectPage());
        nativeAdContainer.setOnClickListener(view -> ShowNativeRedirectPage());
    }


    public void ShowOwnNativeMedium(ViewGroup nativeAdContainer) {
        firebaseEvent("OwnAdsNativeMedium OwnShow " + sharedPreferencesHelper.getOwnNativePosition());
        nativeAdContainer.setVisibility(View.VISIBLE);
        OwnNativeMeduimAdLayoutBinding mediumNativeLayout = OwnNativeMeduimAdLayoutBinding.inflate(LayoutInflater.from(activity));
        mediumNativeLayout.textAdTitle.setText(festumNativeAdModels.get(sharedPreferencesHelper.getOwnNativePosition()).getvTittle());
        mediumNativeLayout.textAdBody.setText(festumNativeAdModels.get(sharedPreferencesHelper.getOwnNativePosition()).getvDescipation());
        mediumNativeLayout.buttonCallToAction.setText(R.string.install);
        Glide.with(activity).load(festumNativeAdModels.get(sharedPreferencesHelper.getOwnNativePosition()).getvNativeBannerUrl()).error(R.drawable.predchamp_1).into(mediumNativeLayout.imageAdIcon);
        Glide.with(activity).load(festumNativeAdModels.get(sharedPreferencesHelper.getOwnNativePosition()).getvLogoUrl()).error(R.drawable.predchamp_1).into(mediumNativeLayout.nativeAdMediaView);
        nativeAdContainer.removeAllViews();
        nativeAdContainer.addView(mediumNativeLayout.getRoot());
        if (!sharedPreferencesHelper.getAdTitleColor().isEmpty()) {
            mediumNativeLayout.textAdTitle.setTextColor(Color.parseColor(MyApplication.sharedPreferencesHelper.getAdTitleColor()));
        }
        if (!sharedPreferencesHelper.getAdDescriptionColor().isEmpty()) {
            mediumNativeLayout.textAdBody.setTextColor(Color.parseColor(sharedPreferencesHelper.getAdDescriptionColor()));
        }
        if (!MyApplication.sharedPreferencesHelper.getAdButtonTextColor().isEmpty()) {
            mediumNativeLayout.buttonCallToAction.setTextColor(Color.parseColor(MyApplication.sharedPreferencesHelper.getAdButtonTextColor()));
            mediumNativeLayout.textTitle.setTextColor(Color.parseColor(MyApplication.sharedPreferencesHelper.getAdButtonTextColor()));
        }
        if (!MyApplication.sharedPreferencesHelper.getAdButtonColor().isEmpty()) {
            getOpacityColor(mediumNativeLayout.buttonCallToAction.getBackground(), MyApplication.sharedPreferencesHelper.getAdButtonColorOpacity(), false);
            getOpacityColor(mediumNativeLayout.textTitle.getBackground(), MyApplication.sharedPreferencesHelper.getAdButtonColorOpacity(), false);
        }
        if (!MyApplication.sharedPreferencesHelper.getAddBgColor().isEmpty()) {
            getOpacityColor(mediumNativeLayout.bigNative.getBackground(), MyApplication.sharedPreferencesHelper.getAdBgColorOpacity(), true);
        }
        mediumNativeLayout.buttonCallToAction.setOnClickListener(view -> ShowNativeRedirectPage());
        nativeAdContainer.setOnClickListener(view -> ShowNativeRedirectPage());
    }

    public void ShowOwnNativeBanner(ViewGroup nativeAdContainer) {
        firebaseEvent("OwnAdsNativeBanner OwnShow " + sharedPreferencesHelper.getOwnBannerPosition());
        nativeAdContainer.setVisibility(View.VISIBLE);
        OwnNativeBannerAdLayoutBinding smallNativeLayout = OwnNativeBannerAdLayoutBinding.inflate(LayoutInflater.from(activity));
        smallNativeLayout.textAdTitle.setText(festumNativeBannerAdModels.get(sharedPreferencesHelper.getOwnBannerPosition()).getvTittle());
        smallNativeLayout.textAdBody.setText(festumNativeBannerAdModels.get(sharedPreferencesHelper.getOwnBannerPosition()).getvDescipation());
        smallNativeLayout.buttonCallToAction.setText(R.string.install);
        Glide.with(activity).load(festumNativeBannerAdModels.get(sharedPreferencesHelper.getOwnBannerPosition()).getvLogoUrl()).error(R.drawable.predchamp_1).into(smallNativeLayout.imageAdIcon);
        nativeAdContainer.removeAllViews();
        nativeAdContainer.addView(smallNativeLayout.getRoot());


        if (!sharedPreferencesHelper.getAdTitleColor().isEmpty()) {
            smallNativeLayout.textAdTitle.setTextColor(Color.parseColor(MyApplication.sharedPreferencesHelper.getAdTitleColor()));
        }
        if (!MyApplication.sharedPreferencesHelper.getAdDescriptionColor().isEmpty()) {
            smallNativeLayout.textAdBody.setTextColor(Color.parseColor(MyApplication.sharedPreferencesHelper.getAdDescriptionColor()));
        }
        if (!MyApplication.sharedPreferencesHelper.getAdButtonTextColor().isEmpty()) {
            smallNativeLayout.buttonCallToAction.setTextColor(Color.parseColor(MyApplication.sharedPreferencesHelper.getAdButtonTextColor()));
            smallNativeLayout.textTitle.setTextColor(Color.parseColor(MyApplication.sharedPreferencesHelper.getAdButtonTextColor()));
        }
        if (!MyApplication.sharedPreferencesHelper.getAdButtonColor().isEmpty()) {
            getOpacityColor(smallNativeLayout.buttonCallToAction.getBackground(), MyApplication.sharedPreferencesHelper.getAdButtonColorOpacity(), false);
            getOpacityColor(smallNativeLayout.textTitle.getBackground(), MyApplication.sharedPreferencesHelper.getAdButtonColorOpacity(), false);
        }
        if (!MyApplication.sharedPreferencesHelper.getAddBgColor().isEmpty()) {
            getOpacityColor(smallNativeLayout.smallNativeView.getBackground(), MyApplication.sharedPreferencesHelper.getAdBgColorOpacity(), true);
        }
        smallNativeLayout.buttonCallToAction.setOnClickListener(view -> ShowNativeBannerredirectPage());
        nativeAdContainer.setOnClickListener(view -> ShowNativeBannerredirectPage());
    }


    public void ShowNativeRedirectPage() {
        final CustomTabsIntent customTabsIntent;
        try {
            customTabsIntent = new CustomTabsIntent.Builder().setToolbarColor(activity.getResources().getColor(R.color.ad_end_color)).build();
            customTabsIntent.launchUrl(activity, Uri.parse(festumNativeAdModels.get(sharedPreferencesHelper.getOwnNativePosition()).getvRedirectUrl()));
        } catch (Resources.NotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public void ShowNativeBannerredirectPage() {
        final CustomTabsIntent customTabsIntent;
        try {
            customTabsIntent = new CustomTabsIntent.Builder().setToolbarColor(activity.getResources().getColor(R.color.ad_end_color)).build();
            customTabsIntent.launchUrl(activity, Uri.parse(festumNativeBannerAdModels.get(sharedPreferencesHelper.getOwnBannerPosition()).getvRedirectUrl()));
        } catch (Resources.NotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}