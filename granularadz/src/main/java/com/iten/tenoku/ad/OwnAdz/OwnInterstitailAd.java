package com.iten.tenoku.ad.OwnAdz;

import static com.iten.tenoku.utils.AdConstant.festumInterstitialAdModels;
import static com.iten.tenoku.utils.AdUtils.firebaseEvent;
import static com.iten.tenoku.utils.AdUtils.getOpacityColor;
import static com.iten.tenoku.utils.MyApplication.sharedPreferencesHelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.Nullable;
import androidx.browser.customtabs.CustomTabsIntent;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.iten.tenoku.R;
import com.iten.tenoku.ad.HandleClick.HandleOwnInterstitialAd;
import com.iten.tenoku.databinding.LayoutFestumInterstitialAdBinding;
import com.iten.tenoku.utils.AdConstant;
import com.iten.tenoku.utils.LogUtils;

public class OwnInterstitailAd {
    private static final String TAG = "FestumInterstitailAd";
    @SuppressLint("StaticFieldLeak")
    private static LayoutFestumInterstitialAdBinding binding;
    @SuppressLint("StaticFieldLeak")
    private static OwnInterstitailAd mInstance;

    @SuppressLint("StaticFieldLeak")
    private static Activity activity;
    HandleOwnInterstitialAd handleOwnInterstitialAd = null;

    public OwnInterstitailAd(Activity activity) {
        OwnInterstitailAd.activity = activity;
    }

    public static OwnInterstitailAd getInstance(Activity adShowingactivity) {
        if (mInstance == null) {
            mInstance = new OwnInterstitailAd(adShowingactivity);
        }
        return mInstance;
    }

    private static void countdown() {
        Log.e(TAG, "countdown: ----> " + sharedPreferencesHelper.getOwnInterTimer());
        new CountDownTimer(sharedPreferencesHelper.getOwnInterTimer() * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                long time = millisUntilFinished / 1000;
                binding.texViewTimer.setText(String.valueOf(time + 1));
            }

            public void onFinish() {
                binding.texViewTimer.setVisibility(View.GONE);
                binding.imageViewClose.setVisibility(View.VISIBLE);
            }
        }.start();

    }

    private void HandleOwnInterstitalAd() {
        if (handleOwnInterstitialAd != null) {
            handleOwnInterstitialAd.Show(true);
            handleOwnInterstitialAd = null;
        }
    }

    @SuppressLint("CheckResult")
    public void ShowOwnInterstialAd(Activity activity, HandleOwnInterstitialAd handleOwnInterstitialAd) {
        firebaseEvent("OwnInterstitial AdShow " + sharedPreferencesHelper.getOwnInterstitialPosition());
        this.handleOwnInterstitialAd = handleOwnInterstitialAd;
        Dialog dialog = new Dialog(activity, R.style.DialogTheme);
        binding = LayoutFestumInterstitialAdBinding.inflate(activity.getLayoutInflater());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(binding.getRoot());
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setFlags(1024, 1024);
        dialog.onWindowFocusChanged(true);
        dialog.setCancelable(false);
        int currentApiVersion;
        currentApiVersion = Build.VERSION.SDK_INT;

        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {
            dialog.getWindow().getDecorView().setSystemUiVisibility(flags);
            final View decorView = dialog.getWindow().getDecorView();
            decorView.setOnSystemUiVisibilityChangeListener(i -> {
                if ((i & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    decorView.setSystemUiVisibility(flags);
                }
            });
        }

        binding.progress.setVisibility(View.VISIBLE);
        binding.layoutOwnInter.setVisibility(View.VISIBLE);
        binding.textViewTitle.setText(festumInterstitialAdModels.get(sharedPreferencesHelper.getOwnInterstitialPosition()).getvTittle());
        binding.textViewDescription.setText(festumInterstitialAdModels.get(sharedPreferencesHelper.getOwnInterstitialPosition()).getvDescipation());
        binding.textViewButton.setText(festumInterstitialAdModels.get(sharedPreferencesHelper.getOwnInterstitialPosition()).getvTittle());

        if (!sharedPreferencesHelper.getAdButtonTextColor().isEmpty()) {
            binding.textViewAdTitle.setTextColor(Color.parseColor(sharedPreferencesHelper.getAdButtonTextColor()));
        }

        if (!sharedPreferencesHelper.getAdButtonColor().isEmpty()) {
            getOpacityColor(binding.textViewAdTitle.getBackground(), sharedPreferencesHelper.getAdButtonColorOpacity(), false);
        }
        Glide.with(activity).load(festumInterstitialAdModels.get(sharedPreferencesHelper.getOwnInterstitialPosition()).getvNativeBannerUrl()).error(R.drawable.creative_1).into(binding.imageViewIcon);
        Glide.with(activity).load(festumInterstitialAdModels.get(sharedPreferencesHelper.getOwnInterstitialPosition()).getvNativeBannerUrl()).error(R.drawable.ic_qureka_interstital).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                binding.progress.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                binding.progress.setVisibility(View.GONE);
                return false;
            }
        }).into(binding.imageViewAd);
        Glide.with(activity).asDrawable().load(AdConstant.getInterstitialRandomImage()).error(R.drawable.ic_interstitalad_qureka).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                binding.layoutOwnInter.setBackgroundDrawable(resource);
                return false;
            }
        });

        binding.nativeAdVideoView.setVideoURI(Uri.parse(festumInterstitialAdModels.get(sharedPreferencesHelper.getOwnInterstitialPosition()).getvVideoUrl()));
        binding.nativeAdVideoView.requestFocus();
        binding.nativeAdVideoView.setOnPreparedListener(mediaPlayer -> binding.nativeAdVideoView.start());
        binding.nativeAdVideoView.setOnCompletionListener(mediaPlayer -> {
                    LogUtils.logE(TAG, "onCompletion");
                    binding.nativeAdVideoView.setVisibility(View.INVISIBLE);
                    binding.imageViewAd.setVisibility(View.VISIBLE);
                }
        );
        binding.nativeAdVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                LogUtils.logE(TAG, "Video Error to Play");
                binding.nativeAdVideoView.setVisibility(View.INVISIBLE);
                binding.imageViewAd.setVisibility(View.VISIBLE);
                return true;
            }
        });
        Animation bottomTop;
        bottomTop = AnimationUtils.loadAnimation(activity, R.anim.bottom_to_top);
        binding.layoutOwnInter.setAnimation(bottomTop);
        bottomTop.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.layoutTop.setVisibility(View.VISIBLE);
                Animation topBottom = AnimationUtils.loadAnimation(activity, R.anim.top_to_bottom);
                binding.layoutTop.setAnimation(topBottom);
                topBottom.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        countdown();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        binding.imageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedPreferencesHelper.getOwnInterClose()) {
                    HandleOwnInterstitalAd();
                    ShowRedirectPage();
                    dialog.dismiss();
                } else {
                    HandleOwnInterstitalAd();
                    dialog.dismiss();
                }
            }
        });
        binding.getRoot().setOnClickListener(view -> {
            HandleOwnInterstitalAd();
            ShowRedirectPage();
            dialog.dismiss();
        });
        binding.cardViewPlayButton.setOnClickListener(view -> {
            HandleOwnInterstitalAd();
            ShowRedirectPage();
            dialog.dismiss();
        });
        dialog.show();
    }

    public void ShowRedirectPage() {
        final CustomTabsIntent customTabsIntent;
        try {
            customTabsIntent = new CustomTabsIntent.Builder().setToolbarColor(activity.getResources().getColor(R.color.ad_end_color)).build();
            customTabsIntent.launchUrl(activity, Uri.parse(festumInterstitialAdModels.get(sharedPreferencesHelper.getOwnInterstitialPosition()).getvRedirectUrl()));
        } catch (Resources.NotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
