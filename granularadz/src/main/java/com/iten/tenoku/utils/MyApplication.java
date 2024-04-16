package com.iten.tenoku.utils;


import static com.iten.tenoku.utils.AdUtils.initFireBase;

import android.app.Application;
import android.util.Log;

import androidx.multidex.MultiDexApplication;

import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.MobileAds;
import com.google.gson.Gson;
import com.iten.tenoku.R;
import com.iten.tenoku.ad.AppOpenManager;
import com.onesignal.OSNotificationOpenedResult;
import com.onesignal.OneSignal;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class MyApplication extends Application {

    public static final String TAG = "MyApplication";
    public static SharedPreferencesHelper sharedPreferencesHelper;
    public static AppOpenManager appOpenManager;
    public static boolean isFromOneSignal = false;
    private static MyApplication mInstance;

    public static synchronized MyApplication getInstance() {
        MyApplication appController;
        synchronized (MyApplication.class) {
            appController = mInstance;
        }
        return appController;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        sharedPreferencesHelper = SharedPreferencesHelper.init(this);
        appOpenManager = new AppOpenManager(this);
        AudienceNetworkAds.initialize(this);

        MobileAds.initialize(this, initializationStatus -> {
        });
        initFireBase(this);
        if (sharedPreferencesHelper.getBoolean("IsNotificationEnable", true)) {
            setOneSignal(getString(R.string.one_signal_app_id));
            OneSignal.disablePush(false);
        } else {
            OneSignal.disablePush(true);
        }
    }

    public void setOneSignal(String id) {
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        OneSignal.initWithContext(getApplicationContext());
        OneSignal.setAppId(id);
        LogUtils.logE(TAG, "setNotificationId -> setOneSignalId " + id);
        OneSignal.promptForPushNotifications();
        OneSignal.setNotificationOpenedHandler(new ExampleNotificationOpenedHandler());
        String userId = OneSignal.getDeviceState().getUserId();
        LogUtils.logE(TAG, "setNotificationId -> userId -> " + userId);
    }


    private static class ExampleNotificationOpenedHandler implements OneSignal.OSNotificationOpenedHandler {
        @Override
        public void notificationOpened(OSNotificationOpenedResult result) {
            LogUtils.logD(TAG, "Response " + new Gson().toJson(result.getNotification().getLaunchURL()));
            LogUtils.logD(TAG, "Response " + new Gson().toJson(result.getNotification()));
            isFromOneSignal = true;
        }
    }
}