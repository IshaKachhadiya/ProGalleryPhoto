package com.iten.tenoku.activity;


import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.iten.tenoku.ad.HandleClick.InterstitialAdListener;
import com.iten.tenoku.databinding.ActivityInterstitialBinding;

public class InterstitialActivity extends AppCompatActivity {

    public static InterstitialAdListener adListener = null;
//    Data.AppInhouse modal;
    private ActivityInterstitialBinding x;
    private int currentApiVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentApiVersion = Build.VERSION.SDK_INT;
        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(flags);
            final View decorView = getWindow().getDecorView();
            decorView.setOnSystemUiVisibilityChangeListener(i -> {
                if ((i & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    decorView.setSystemUiVisibility(flags);
                }
            });

        }
        x = ActivityInterstitialBinding.inflate(getLayoutInflater());
        View view = x.getRoot();
        setContentView(view);
//        modal = (Data.AppInhouse) getIntent().getSerializableExtra("modal");
        if (adListener != null) {
            adListener.onAdShown();
        }

//        Glide.with(this).asBitmap().load(modal.getAppIcon()).into(new CustomTarget<Bitmap>() {
//            @Override
//            public void onResourceReady(@NonNull Bitmap bitmap, Transition<? super Bitmap> transition) {
//                x.adIcon.setImageBitmap(bitmap);
//            }
//
//            @Override
//            public void onLoadCleared(Drawable placeholder) {
//
//
//            }
//        });
//        Glide.with(this).asBitmap().load(modal.getAppHeaderImage()).into(new CustomTarget<Bitmap>() {
//            @Override
//            public void onResourceReady(@NonNull Bitmap bitmap, Transition<? super Bitmap> transition) {
//                x.nativeAdMediaView.setImageBitmap(bitmap);
//            }
//
//            @Override
//            public void onLoadCleared(Drawable placeholder) {
//
//
//            }
//        });
//        x.ratingBarAd.setRating(Float.parseFloat(modal.getAppRating()));
//        x.textAdBody.setText(modal.getAppDesc());
//        x.textAdTitle.setText(modal.getAppTitle());
//        x.textDownload.setText(modal.getAppCtaText());
//        x.textAdDownloads.setText("(" + modal.getAppDownload() + "+)");
//        x.mainLayout.setOnClickListener(view13 -> {
//            if (adListener != null) {
//                adListener.onApplicationLeft();
//                AdUtils.PlayStore(InterstitialActivity.this, modal.getAppUri());
//                finish();
//                adListener = null;
//            }
//        });

//        x.floatingActionButton.setOnClickListener(view12 -> {
//            if (adListener != null) {
//                adListener.onApplicationLeft();
//                AdUtils.PlayStore(InterstitialActivity.this, modal.getAppUri());
//                finish();
//                adListener = null;
//            }
//        });
        x.houseAdsInterstitialButtonClose.setOnClickListener(view1 -> {
            if (adListener != null) {
                adListener.onAdClosed();
                finish();
                adListener = null;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (adListener != null) {
            adListener.onAdClosed();
            finish();
            adListener = null;
        }


    }

    @SuppressLint("NewApi")
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}