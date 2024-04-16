package hdphoto.galleryimages.gelleryalbum.location_media.activity;

import static com.iten.tenoku.utils.MyApplication.sharedPreferencesHelper;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.gson.Gson;
import com.iten.tenoku.activity.AppSettingActivity;
import com.iten.tenoku.databinding.DialogAppUpdateBinding;
import com.iten.tenoku.databinding.DialogInternetBinding;
import com.iten.tenoku.listeners.AppSettingListeners;
import com.iten.tenoku.networking.AdsResponse;
import com.iten.tenoku.utils.AdConstant;
import com.iten.tenoku.utils.AdUtils;
import com.iten.tenoku.utils.LogUtils;
import com.iten.tenoku.utils.MyApplication;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import hdphoto.galleryimages.gelleryalbum.Ads_Common.SharePref;
import hdphoto.galleryimages.gelleryalbum.Ads_Common.UnderMaintenanceActivity;
import hdphoto.galleryimages.gelleryalbum.BuildConfig;
import hdphoto.galleryimages.gelleryalbum.R;
import hdphoto.galleryimages.gelleryalbum.activity.LanguageActivity;
import hdphoto.galleryimages.gelleryalbum.activity.MainActivity;
import hdphoto.galleryimages.gelleryalbum.activity.PermissionActivity;
import hdphoto.galleryimages.gelleryalbum.appclass.GalleryAppClass;
import hdphoto.galleryimages.gelleryalbum.getdata.ImageAlbumData;
import hdphoto.galleryimages.gelleryalbum.getdata.MomentData;
import hdphoto.galleryimages.gelleryalbum.getdata.VideoAlbumData;
import hdphoto.galleryimages.gelleryalbum.location_media.common.Constant;
import hdphoto.galleryimages.gelleryalbum.location_media.util.PreferencesManager;
import hdphoto.galleryimages.gelleryalbum.utils.Preference;
import hdphoto.galleryimages.gelleryalbum.utils.SharePrefUtil;


public class SplashScreenActivity extends AppSettingActivity {
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;

    public static Activity activity;
    public static MomentData getMomentData = null;
    String currentDate;
    int LAUNCH_INTERNET_ACTIVITY = 1;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.iuc_splashactivity);
        SharedPreferences sharedPreferences2 = getSharedPreferences(MyPREFERENCES, 0);
        this.sharedPreferences = sharedPreferences2;
        this.editor = sharedPreferences2.edit();
        PreferencesManager.saveImageShowADs(this, false);
        startImmidiateActivity();

        getData(SplashScreenActivity.this);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/YYYY");
        currentDate = simpleDateFormat.format(Calendar.getInstance().getTime());

        setAdRequestStatus();

        checkNotificationPermission();

        SharePref.putPrefCntOpenApp(SplashScreenActivity.this, SharePref.getPrefCntOpenApp(SplashScreenActivity.this) + 1);
    }

    private void startImmidiateActivity() {
        Constant.photoList.clear();
        Constant.folderList.clear();
        Constant.storyList.clear();
        Constant.videoList.clear();
        GalleryAppClass.putTabPos(0);
    }

    public static void getData(Activity activity) {
        getMomentData = new MomentData(activity);
        getMomentData.getMomentAllAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);

//        getImageAlbumData = new ImageAlbumData(activity);
//        getImageAlbumData.getImageAlbumAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);

//        getVideoAlbumData = new VideoAlbumData(activity);
//        getVideoAlbumData.getVideoAlbumAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    private void checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= 33) {
            if (ActivityCompat.checkSelfPermission(SplashScreenActivity.this, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                intAction();
            } else {
                checkNotificationPermissionAbove33();
            }
        } else {
            intAction();
        }
    }

    public void checkNotificationPermissionAbove33() {
        Dexter.withContext(this).withPermission(Manifest.permission.POST_NOTIFICATIONS).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                LogUtils.logV("TAG", "MyPermission onPermissionGranted");
                intAction();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                intAction();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }

        }).onSameThread().check();
    }

    private void intAction() {
        Clap_checkConnection();
    }

    private void Clap_checkConnection() {
        if (AdUtils.isNetworkAvailable(SplashScreenActivity.this)) {
            Clap_loadSetting();
        } else {
            ShowNetworkDialog();
        }
    }

    private void ShowNetworkDialog() {
        Dialog dialog = new Dialog(SplashScreenActivity.this);
        DialogInternetBinding dialogInternetBinding = DialogInternetBinding.inflate(getLayoutInflater());
        dialog.setContentView(dialogInternetBinding.getRoot());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialogInternetBinding.buttonRetry.setOnClickListener(v -> {
            dialog.dismiss();
            if (AdUtils.isNetworkAvailable(SplashScreenActivity.this)) {
                Clap_loadSetting();
            } else {
                Intent i = new Intent(Settings.ACTION_SETTINGS);
                startActivityForResult(i, LAUNCH_INTERNET_ACTIVITY);
            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LAUNCH_INTERNET_ACTIVITY) {
            Clap_checkConnection();
        }
    }


    private void Clap_loadSetting() {
        AppSettings(SplashScreenActivity.this, BuildConfig.VERSION_CODE, getPackageName(), new AppSettingListeners() {
            @Override
            public void onResponseSuccess() {
                LogUtils.logE("TAG", "onResponseSuccess: ");
                if (sharedPreferencesHelper.getAppOpenAd()) {
                    new Handler().postDelayed(() -> {
                        LogUtils.logD("TAG", "AppOpenManager PassActivity after 5 sec: ");
                        openWelcome();
                    }, 4000);

                } else {
                    new Handler().postDelayed(() -> {
                        new Handler().postDelayed(() -> {
                            if (SharePrefUtil.isTutorial(SplashScreenActivity.this)) {
                                if (Preference.isFirstLaunch(SplashScreenActivity.this)) {
                                    startActivity(new Intent(SplashScreenActivity.this, PermissionActivity.class));
                                } else {
                                    startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                                }
                            } else {
                                startActivity(new Intent(SplashScreenActivity.this, LanguageActivity.class));
                            }
                        }, 1500);
                    }, 3000);
                }
            }

            @Override
            public void onUnderMaintenance() {
                com.iten.tenoku.utils.MyApplication.appOpenManager.ShowOpenAd(true, adShow -> {
                    startActivity(new Intent(SplashScreenActivity.this, UnderMaintenanceActivity.class));
                    finish();
                });
            }

            @Override
            public void onResponseFail() {
                LogUtils.logE("TAG", "onResponseFail  ===> ");
                if (!sharedPreferencesHelper.sharedPreferences.getAll().isEmpty()) {
                    try {
                        AdsResponse appSettings = new Gson().fromJson(sharedPreferencesHelper.getResponse(), AdsResponse.class);
                        if (appSettings.getIsStatus()) {

                            Log.e("TAG", "onResponseFail: " + appSettings.getIsStatus());
                            CheckBooleanValue(appSettings);
                            CheckStringValue(appSettings);
                            CheckIntegerValue(appSettings);
                            SetAdColors(appSettings);
                            SetRecyclerViewAd(sharedPreferencesHelper.getGridViewPerItemAdTwo(), sharedPreferencesHelper.getGridViewPerItemAdThree(), sharedPreferencesHelper.getListViewAd());
                            SetAdData(appSettings);
                            SetHideFeature(appSettings);
                            SetUserData(appSettings);
                            FacebookAd(appSettings);
                            preLoadAd();
                            HandelData(sharedPreferencesHelper.getUnderMaintenance());
                        }
                    } catch (Exception e) {
                    }
                    openWelcome();
                } else {
                    recreate();
                }
            }

            @Override
            public void onAppUpdate(String url) {
                LogUtils.logE("TAG", "onAppUpdate: ");
                AppDialogShow(url, 0);
            }

            @Override
            public void onAppRedirect(String url) {
                LogUtils.logE("TAG", "onAppRedirect: ");
                AppDialogShow(url, 1);
            }

            @Override
            public void onStatusChange() {
                LogUtils.logE("TAG", "onStatusChange: ");

            }
        });

    }

    private void setAdRequestStatus() {
        if (!MyApplication.sharedPreferencesHelper.getUserSavedDate().equals(currentDate)) {
            MyApplication.sharedPreferencesHelper.setTotalFailedCount(AdConstant.ResetTotalCount);
            MyApplication.sharedPreferencesHelper.setAdmobFailedCount(AdConstant.ResetTotalCount);
            MyApplication.sharedPreferencesHelper.setFbFailedCount(AdConstant.ResetTotalCount);
            MyApplication.sharedPreferencesHelper.setUserSavedDate(currentDate);
            sharedPreferencesHelper.setRequestDataSend(false);
        }
    }


    public final void openWelcome() {
        com.iten.tenoku.utils.MyApplication.appOpenManager.ShowOpenAd(true, adShow -> {
            new Handler().postDelayed(() -> {
                if (SharePrefUtil.isTutorial(SplashScreenActivity.this)) {
                    if (Preference.isFirstLaunch(SplashScreenActivity.this)) {
                        startActivity(new Intent(SplashScreenActivity.this, PermissionActivity.class));
                    } else {
                        startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                    }
                }else {
                    startActivity(new Intent(SplashScreenActivity.this, LanguageActivity.class));
                }
            }, 1500);
        });

    }

    private void AppDialogShow(String url, int i) {
        Dialog dialog = new Dialog(SplashScreenActivity.this);
        DialogAppUpdateBinding dialogAppUpdateBinding = DialogAppUpdateBinding.inflate(getLayoutInflater());
        dialog.setContentView(dialogAppUpdateBinding.getRoot());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        if (i == 0) {
            dialogAppUpdateBinding.buttonRetry.setText(com.iten.tenoku.R.string.update_title);
            dialogAppUpdateBinding.txtTitle.setText(com.iten.tenoku.R.string.update_sub_title);
            dialogAppUpdateBinding.txtDecription.setText("");
            dialogAppUpdateBinding.txtDecription.setVisibility(View.GONE);
        } else if (i == 1) {
            dialogAppUpdateBinding.buttonRetry.setText(com.iten.tenoku.R.string.install_title);
            dialogAppUpdateBinding.txtTitle.setText(com.iten.tenoku.R.string.install_sub_title);
            dialogAppUpdateBinding.txtDecription.setVisibility(View.VISIBLE);
            dialogAppUpdateBinding.txtDecription.setText(com.iten.tenoku.R.string.install_descrption);
        } else {
            dialog.dismiss();
        }
        dialogAppUpdateBinding.buttonRetry.setOnClickListener(v -> {
            dialog.dismiss();
            try {
                Uri marketUri = Uri.parse(url);
                Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                startActivity(marketIntent);
            } catch (ActivityNotFoundException ignored) {
            }
        });
        dialog.show();
    }
}
