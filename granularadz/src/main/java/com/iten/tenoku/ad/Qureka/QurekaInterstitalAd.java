package com.iten.tenoku.ad.Qureka;

import static com.iten.tenoku.utils.AdConstant.qurekaLinksList;
import static com.iten.tenoku.utils.AdUtils.getOpacityColor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
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
import com.iten.tenoku.ad.HandleClick.HandleQurekaInterstitialAd;
import com.iten.tenoku.databinding.LayoutQurekaInterstitialAdBinding;
import com.iten.tenoku.model.QurekaInterstitialAdModel;
import com.iten.tenoku.utils.AdConstant;
import com.iten.tenoku.utils.MyApplication;

import java.util.Random;

public class QurekaInterstitalAd {

    @SuppressLint("StaticFieldLeak")
    private static LayoutQurekaInterstitialAdBinding x;
    @SuppressLint("StaticFieldLeak")
    private static QurekaInterstitalAd mInstance;
    @SuppressLint("StaticFieldLeak")
    private static Activity activity;
    String TAG = getClass().getSimpleName();
    HandleQurekaInterstitialAd handleQurekaInterstitialAd = null;
    QurekaInterstitialAdModel qurekaInterstitialAdModel;
    boolean show = false;

    public QurekaInterstitalAd(Activity activity) {
        QurekaInterstitalAd.activity = activity;

    }

    public static QurekaInterstitalAd getInstance(Activity adShowingActivity) {
        if (mInstance == null) {
            mInstance = new QurekaInterstitalAd(adShowingActivity);
        }
        return mInstance;
    }

    private static void countdown() {
        new CountDownTimer(1000, 1000) {
            public void onTick(long millisUntilFinished) {
                long time = millisUntilFinished / 1000;
                x.texViewTimer.setText("" + (time + 1));
            }

            public void onFinish() {
                x.texViewTimer.setVisibility(View.GONE);
                x.imageViewClose.setVisibility(View.VISIBLE);
            }
        }.start();

    }

    void HandleQuerkaInterstitialAd() {
        if (handleQurekaInterstitialAd != null) {
            handleQurekaInterstitialAd.Show(true);
            handleQurekaInterstitialAd = null;
        }
    }

    public void ShowQurekaAd(Activity activity, HandleQurekaInterstitialAd handleQurekaInterstitialAd) {
        this.handleQurekaInterstitialAd = handleQurekaInterstitialAd;
        qurekaInterstitialAdModel = AdConstant.getRandomQurekaInterstitalAd();
        int random = new Random().nextInt((2 - 1) + 1) + 1;


        Dialog dialog = new Dialog(activity, R.style.DialogTheme);
        x = LayoutQurekaInterstitialAdBinding.inflate(activity.getLayoutInflater());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(x.getRoot());
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setFlags(1024, 1024);
        dialog.onWindowFocusChanged(true);
        dialog.setCancelable(false);
        int currentApiVersion;
        currentApiVersion = Build.VERSION.SDK_INT;

        if (!MyApplication.sharedPreferencesHelper.getAdButtonTextColor().isEmpty()) {
            x.textViewAd.setTextColor(Color.parseColor(MyApplication.sharedPreferencesHelper.getAdButtonTextColor()));
        }

        x.layoutTop.setVisibility(View.GONE);

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


        switch (random) {
            case 1:
                x.progress.setVisibility(View.VISIBLE);
                x.layoutQurka.setVisibility(View.VISIBLE);

                x.textViewTitle.setText(qurekaInterstitialAdModel.getTitle());
                x.textViewDescription.setText(qurekaInterstitialAdModel.getDescription());
                x.textViewButton.setText(qurekaInterstitialAdModel.getButton());

                if (!MyApplication.sharedPreferencesHelper.getAdButtonTextColor().isEmpty()) {
                    x.textViewAdTitle.setTextColor(Color.parseColor(MyApplication.sharedPreferencesHelper.getAdButtonTextColor()));
                }

                if (!MyApplication.sharedPreferencesHelper.getAdButtonColor().isEmpty()) {
                    getOpacityColor(x.textViewAdTitle.getBackground(), MyApplication.sharedPreferencesHelper.getAdButtonColorOpacity(), false);
                }

                Glide.with(activity).load(AdConstant.getRandomLogo()).error(R.drawable.creative_1).into(x.imageViewIcon);
                Glide.with(activity).load(AdConstant.getInterstitialRandomImage()).error(R.drawable.ic_qureka_interstital).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        x.progress.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        x.progress.setVisibility(View.GONE);
                        return false;
                    }
                }).into(x.imageViewAd);
                Glide.with(activity).asDrawable().load(AdConstant.getInterstitialRandomImage()).error(R.drawable.ic_interstitalad_qureka).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        x.layoutQurka.setBackgroundDrawable(resource);
                        return false;
                    }
                });

                Animation bottomTop;
                bottomTop = AnimationUtils.loadAnimation(activity, R.anim.bottom_to_top);
                x.layoutQurka.setAnimation(bottomTop);
                bottomTop.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                        x.layoutTop.setVisibility(View.VISIBLE);
                        Animation topBottom = AnimationUtils.loadAnimation(activity, R.anim.top_to_bottom);
                        x.layoutTop.setAnimation(topBottom);
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

                break;
            case 2:
                x.progress2.setVisibility(View.VISIBLE);
                x.qureka2.setVisibility(View.VISIBLE);

                x.textViewTitle2.setText(qurekaInterstitialAdModel.getTitle());
                x.textViewDescription2.setText(qurekaInterstitialAdModel.getDescription());
                x.textViewButton2.setText(qurekaInterstitialAdModel.getButton());

                if (!MyApplication.sharedPreferencesHelper.getAdButtonTextColor().isEmpty()) {
                    x.textViewAd.setTextColor(Color.parseColor(MyApplication.sharedPreferencesHelper.getAdButtonTextColor()));
                }

                if (!MyApplication.sharedPreferencesHelper.getAdButtonColor().isEmpty()) {
                    getOpacityColor(x.textViewAd.getBackground(), MyApplication.sharedPreferencesHelper.getAdButtonColorOpacity(), false);
                }
                Glide.with(activity).load(AdConstant.getRandomLogo()).error(R.drawable.predchamp_1).into(x.imageViewIcon2);
                Glide.with(activity).load(AdConstant.getInterstitialRandomImage()).error(R.drawable.ic_interstitalad_qureka).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        x.progress2.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        x.progress2.setVisibility(View.GONE);
                        return false;
                    }
                }).into(x.imageViewAd2);
                Glide.with(activity).asDrawable().load(AdConstant.getInterstitialRandomImage()).error(R.drawable.ic_qureka_interstital).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        x.qureka2.setBackgroundDrawable(resource);
                        return false;
                    }
                });

                Animation bottomTop2;
                bottomTop2 = AnimationUtils.loadAnimation(activity, R.anim.bottom_to_top);
                x.qureka2.setAnimation(bottomTop2);
                bottomTop2.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });

                break;
        }


        x.imageViewClose.setOnClickListener(view -> {
            if (MyApplication.sharedPreferencesHelper.getQurekaClose()) {
                HandleQuerkaInterstitialAd();
                dialog.dismiss();
            } else {
                HandleQuerkaInterstitialAd();
                ShowQuerkaAd();
                dialog.dismiss();
            }


        });

        x.getRoot().setOnClickListener(view -> {
            HandleQuerkaInterstitialAd();
            ShowQuerkaAd();
            dialog.dismiss();
        });

        x.cardViewPlayButton.setOnClickListener(view -> {
            HandleQuerkaInterstitialAd();
            ShowQuerkaAd();
            dialog.dismiss();
        });
        x.imageViewClose2.setOnClickListener(view -> {
            if (MyApplication.sharedPreferencesHelper.getQurekaClose()) {
                HandleQuerkaInterstitialAd();
                dialog.dismiss();
            } else {
                HandleQuerkaInterstitialAd();
                ShowQuerkaAd();
                dialog.dismiss();
            }
        });

        x.cardViewPlayButton2.setOnClickListener(view -> {
            HandleQuerkaInterstitialAd();
            ShowQuerkaAd();
            dialog.dismiss();
        });
        dialog.show();
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

}
