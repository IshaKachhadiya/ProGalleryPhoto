package com.iten.tenoku.utils;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;

//import com.google.firebase.analytics.FirebaseAnalytics;
//import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.GsonBuilder;
import com.iten.tenoku.R;
import com.iten.tenoku.activity.AppSettingActivity;
import com.iten.tenoku.listeners.OnAdSizeHandel;
import com.iten.tenoku.networking.Api;
import com.iten.tenoku.networking.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdUtils {
    private static final String TAG = "AdUtils";
    public static AdLoading adLoading = null;
    public static AdProgress adProgress = null;
    public static AdFetchData adFetchData = null;
//    public static FirebaseAnalytics mFirebaseAnalytics;
//    public static FirebaseCrashlytics mFirebaseCrashlytics;
    static Activity activity;
    static AdUtils mInstance;
    public Api api;

    public AdUtils(Activity activity) {
        AdUtils.activity = activity;
    }

    public static void initFireBase(Context currentActivity) {
//        mFirebaseAnalytics = FirebaseAnalytics.getInstance(currentActivity);
//        mFirebaseCrashlytics = FirebaseCrashlytics.getInstance();
    }
    public static void firebaseEvent(String name) {
        Bundle bundle = new Bundle();
        String mName = name.replace(" ", "_").replace(".", "_");
        String item_Name = "Wizard" + mName;
//        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, item_Name);
        LogUtils.logD("TAG", "item_Name -> " + item_Name);
//        mFirebaseAnalytics.logEvent(item_Name, bundle);
    }

    public void sendRequestData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(AdConstant.vAdFbCounter, String.valueOf(MyApplication.sharedPreferencesHelper.getFbFailedCount()));
            jsonObject.put(AdConstant.vAdAdmobCounter,String.valueOf(MyApplication.sharedPreferencesHelper.getAdmobFailedCount()));
            jsonObject.put(AdConstant.vAppVersion, findVersionKey(activity));
            jsonObject.put(AdConstant.vDeviceName, Build.MANUFACTURER);
            jsonObject.put(AdConstant.vDeviceId,findDeviceId(activity));
            jsonObject.put(AdConstant.vAndroidVesion, String.valueOf(Build.VERSION.RELEASE));
            jsonObject.put(AdConstant.vAppPackageName, activity.getPackageName());

//            Log.e(TAG, "vAdFbCounter: --------->" + String.valueOf(MyApplication.sharedPreferencesHelper.getFbFailedCount()));
//            Log.e(TAG, "vAdAdmobCounter: --------->" + String.valueOf(MyApplication.sharedPreferencesHelper.getAdmobFailedCount()));
//            Log.e(TAG, "vDeviceName: --------->" + Build.MANUFACTURER);
//            Log.e(TAG, "vAndroidVesion: --------->" + Build.VERSION.RELEASE);
//            Log.e(TAG, "vAppPackageName: --------->" + activity.getPackageName());
//            Log.e(TAG, "vAppVersion: --------->" + findVersionKey(activity));
//            Log.e(TAG, "DeviceID: --------->" + findDeviceId(activity));

            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
            api = RetrofitClient.getInstance().getApi();
            Call<ResponseBody> sendCall = api.requestCheck(requestBody);
            sendCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        LogUtils.logE(TAG, " Success--> " + new GsonBuilder().setPrettyPrinting().create().toJson(response.body()));
                    } else {
                        try {
                            LogUtils.logE(TAG, " Error--> " + response.errorBody().string());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }

                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    LogUtils.logE(TAG, "Response Failed");
                    LogUtils.logE(TAG, "Response Failed------> " + t.getMessage());
                }
            });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

    public static AdUtils getInstance(Activity activity) {
        ExitUtils.activity = activity;
        if (mInstance == null) {
            mInstance = new AdUtils(activity);
        }
        return mInstance;
    }

    public static void PlayStore(Context context, String url) {
        try {
            if (url.startsWith("https://play.google.com/store/apps/details?id=")) {
                String remove = url.replace("https://play.google.com/store/apps/details?id=", "");
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + remove)));
            } else if (url.startsWith("http://play.google.com/store/apps/details?id=")) {
                String remove = url.replace("http://play.google.com/store/apps/details?id=", "");
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + remove)));
            } else {
                Intent packageIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                packageIntent.setPackage("com.android.chrome");
                PackageManager packageManager = context.getPackageManager();
                if (packageIntent.resolveActivity(packageManager) != null)
                    context.startActivity(packageIntent);
                else
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isNetworkAvailable(Activity activity) {
        try {
            ConnectivityManager manager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        } catch (Exception e) {
            return false;
        }

    }
    public static String findDeviceId(Activity activity){
        return Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String findVersionKey(Activity activity){
        String versionKey = "";
        try {
            PackageInfo pInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
            versionKey =  pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            versionKey = "";
        }
        return versionKey;
    }
    @SuppressLint("PackageManagerGetSignatures")
    public static String getKeyHash(Activity activity, String libraryPackageName) {
        PackageInfo info;
        try {
            info = activity.getPackageManager().getPackageInfo(libraryPackageName, PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = (Base64.encodeToString(md.digest(), Base64.NO_WRAP));
                return something.replace("+", "*");
            }
        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();

        } catch (Exception ignored) {


        }
        return null;
    }


    public static void showAdLoading(Context context) {
        try {
            if (adLoading != null && adLoading.isShowing()) {
                adLoading.dismiss();
            }

            if (adLoading == null) {
                adLoading = new AdLoading(context);
            }

            adLoading.setCancelable(false);
            adLoading.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void dismissAdLoading() {
        try {
            if (adLoading != null && adLoading.isShowing())
                adLoading.dismiss();
            adLoading = null;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void showAdProgress(Context context) {
        try {
            if (adProgress != null && adProgress.isShowing())
                adProgress.dismiss();

            if (adProgress == null)
                adProgress = new AdProgress(context);

            adProgress.setCancelable(false);
            adProgress.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showFetchingProgress(Context context) {
        try {
            if (adFetchData != null && adFetchData.isShowing())
                adFetchData.dismiss();

            if (adFetchData == null)
                adFetchData = new AdFetchData(context);

            adFetchData.setCancelable(false);
            adFetchData.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void dismissAdProgress() {
        if (adProgress != null && adProgress.isShowing())
            adProgress.dismiss();
        adProgress = null;
    }

    public static void dismissFetchingProgress() {
        if (adFetchData != null && adFetchData.isShowing())
            adFetchData.dismiss();
        adFetchData = null;
    }

    public static boolean checkUpdate(int version) {
        if (MyApplication.sharedPreferencesHelper.getVersionUpdateDialog()) {
            return (float) version < Float.parseFloat(MyApplication.sharedPreferencesHelper.getVersionCode());
        }

        return false;
    }

    public static int getCurrentVersionCode(Activity activity) {
        PackageManager manager = activity.getPackageManager();
        PackageInfo info;
        try {
            info = manager.getPackageInfo(
                    activity.getPackageName(), 0);
            return info.versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static void getOpacityColor(Drawable drawable, int opacity, boolean isBg) {
        String colorOpacity = null;
        switch (opacity) {
            case 100:
                colorOpacity = "FF";
                break;
            case 99:
                colorOpacity = "FC";
                break;
            case 98:
                colorOpacity = "FA";
                break;
            case 97:
                colorOpacity = "F7";
                break;
            case 96:
                colorOpacity = "F5";
                break;
            case 95:
                colorOpacity = "F2";
                break;
            case 94:
                colorOpacity = "F0";
                break;
            case 93:
                colorOpacity = "ED";
                break;
            case 92:
                colorOpacity = "EB";
                break;
            case 91:
                colorOpacity = "E8";
                break;
            case 90:
                colorOpacity = "E6";
                break;
            case 89:
                colorOpacity = "E3";
                break;
            case 88:
                colorOpacity = "E0";
                break;
            case 87:
                colorOpacity = "DE";
                break;
            case 86:
                colorOpacity = "DB";
                break;
            case 85:
                colorOpacity = "D9";
                break;
            case 84:
                colorOpacity = "D6";
                break;
            case 83:
                colorOpacity = "D4";
                break;
            case 82:
                colorOpacity = "D1";
                break;
            case 81:
                colorOpacity = "CF";
                break;
            case 80:
                colorOpacity = "CC";
                break;
            case 79:
                colorOpacity = "C9";
                break;
            case 78:
                colorOpacity = "C7";
                break;
            case 77:
                colorOpacity = "C4";
                break;
            case 76:
                colorOpacity = "C2";
                break;
            case 75:
                colorOpacity = "BF";
                break;
            case 74:
                colorOpacity = "BD";
                break;
            case 73:
                colorOpacity = "BA";
                break;
            case 72:
                colorOpacity = "B8";
                break;
            case 71:
                colorOpacity = "B5";
                break;
            case 70:
                colorOpacity = "B3";
                break;
            case 69:
                colorOpacity = "B0";
                break;
            case 68:
                colorOpacity = "AD";
                break;
            case 67:
                colorOpacity = "AB";
                break;
            case 66:
                colorOpacity = "A8";
                break;
            case 65:
                colorOpacity = "A6";
                break;
            case 64:
                colorOpacity = "A3";
                break;
            case 63:
                colorOpacity = "A1";
                break;
            case 62:
                colorOpacity = "9E";
                break;
            case 61:
                colorOpacity = "9C";
                break;
            case 60:
                colorOpacity = "99";
                break;
            case 59:
                colorOpacity = "96";
                break;
            case 58:
                colorOpacity = "94";
                break;
            case 57:
                colorOpacity = "91";
                break;
            case 56:
                colorOpacity = "8F";
                break;
            case 55:
                colorOpacity = "8C";
                break;
            case 54:
                colorOpacity = "8A";
                break;
            case 53:
                colorOpacity = "87";
                break;
            case 52:
                colorOpacity = "85";
                break;
            case 51:
                colorOpacity = "82";
                break;
            case 50:
                colorOpacity = "80";
                break;
            case 49:
                colorOpacity = "7D";
                break;
            case 48:
                colorOpacity = "7A";
                break;
            case 47:
                colorOpacity = "78";
                break;
            case 46:
                colorOpacity = "75";
                break;
            case 45:
                colorOpacity = "73";
                break;
            case 44:
                colorOpacity = "70";
                break;
            case 43:
                colorOpacity = "6E";
                break;
            case 42:
                colorOpacity = "6B";
                break;
            case 41:
                colorOpacity = "69";
                break;
            case 40:
                colorOpacity = "66";
                break;
            case 39:
                colorOpacity = "63";
                break;
            case 38:
                colorOpacity = "61";
                break;
            case 37:
                colorOpacity = "5E";
                break;
            case 36:
                colorOpacity = "5C";
                break;
            case 35:
                colorOpacity = "59";
                break;
            case 34:
                colorOpacity = "57";
                break;
            case 33:
                colorOpacity = "54";
                break;
            case 32:
                colorOpacity = "52";
                break;
            case 31:
                colorOpacity = "4F";
                break;
            case 30:
                colorOpacity = "4D";
                break;
            case 29:
                colorOpacity = "4A";
                break;
            case 28:
                colorOpacity = "47";
                break;
            case 27:
                colorOpacity = "45";
                break;
            case 26:
                colorOpacity = "42";
                break;
            case 25:
                colorOpacity = "40";
                break;
            case 24:
                colorOpacity = "3D";
                break;
            case 23:
                colorOpacity = "3B";
                break;
            case 22:
                colorOpacity = "38";
                break;
            case 21:
                colorOpacity = "36";
                break;
            case 20:
                colorOpacity = "33";
                break;
            case 19:
                colorOpacity = "30";
                break;
            case 18:
                colorOpacity = "2E";
                break;
            case 17:
                colorOpacity = "2B";
                break;
            case 16:
                colorOpacity = "29";
                break;
            case 15:
                colorOpacity = "26";
                break;
            case 14:
                colorOpacity = "24";
                break;
            case 13:
                colorOpacity = "21";
                break;
            case 12:
                colorOpacity = "1F";
                break;
            case 11:
                colorOpacity = "1C";
                break;
            case 10:
                colorOpacity = "1A";
                break;
            case 9:
                colorOpacity = "17";
                break;
            case 8:
                colorOpacity = "14";
                break;
            case 7:
                colorOpacity = "12";
                break;
            case 6:
                colorOpacity = "0F";
                break;
            case 5:
                colorOpacity = "0D";
                break;
            case 4:
                colorOpacity = "0A";
                break;
            case 3:
                colorOpacity = "08";
                break;
            case 2:
                colorOpacity = "05";
                break;
            case 1:
                colorOpacity = "03";
                break;
            case 0:
                colorOpacity = "00";
                break;
            default:
                colorOpacity = "0D";
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);

        if (isBg) {
            if (MyApplication.sharedPreferencesHelper.getAdBgColorOpacity() == -1) {
                stringBuilder.append(MyApplication.sharedPreferencesHelper.getAddBgColor().trim());
            } else {
                stringBuilder.append("#");
                stringBuilder.append(colorOpacity);
                stringBuilder.append(MyApplication.sharedPreferencesHelper.getAddBgColor().trim().replace("#", ""));
            }

            if (drawable instanceof ShapeDrawable) {
                ((ShapeDrawable) drawable).getPaint().setColor(Color.parseColor(stringBuilder.toString()));
            } else if (drawable instanceof GradientDrawable) {
                ((GradientDrawable) drawable).setColor(Color.parseColor(stringBuilder.toString()));
            } else if (drawable instanceof ColorDrawable) {
                ((ColorDrawable) drawable).setColor(Color.parseColor(stringBuilder.toString()));
            }
        } else {
            if (MyApplication.sharedPreferencesHelper.getAdButtonColorOpacity() == -1) {
                stringBuilder.append(MyApplication.sharedPreferencesHelper.getAdButtonColor().trim());
            } else {
                stringBuilder.append("#");
                stringBuilder.append(colorOpacity);
                stringBuilder.append(MyApplication.sharedPreferencesHelper.getAdButtonColor().trim().replace("#", ""));
            }

            if (drawable instanceof ShapeDrawable) {
                ((ShapeDrawable) drawable).getPaint().setColor(Color.parseColor(stringBuilder.toString()));
            } else if (drawable instanceof GradientDrawable) {
                ((GradientDrawable) drawable).setColor(Color.parseColor(stringBuilder.toString()));
            } else if (drawable instanceof ColorDrawable) {
                ((ColorDrawable) drawable).setColor(Color.parseColor(stringBuilder.toString()));
            }
        }

    }

    public void getAdsSize(ViewGroup viewGroup, OnAdSizeHandel onAdSizeHandel) {
        new Handler().postDelayed(() -> {
            final DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
            final int screenHeightInDp = (int) (viewGroup.getHeight() / displayMetrics.density);
            if (screenHeightInDp > 70 && screenHeightInDp < 259) {
                onAdSizeHandel.onSmallAd();
            } else if (screenHeightInDp > 259 && screenHeightInDp < 310) {
                onAdSizeHandel.onMediumAd();
            } else if (screenHeightInDp > 310) {
                onAdSizeHandel.onBigAd();
            }
        }, 100);

    }

    public enum NativeType {
        NATIVE_BANNER,
        NATIVE_MEDIUM,
        NATIVE_BIG,
        NATIVE_BIG_TOP,
    }

    public enum ClickType {
        MAIN_CLICK,
        BACK_CLICK,
        EVERY_CLICK

    }

    public enum AdType {
        ADMOB_FACEBOOK,
        FACEBOOK_ADMOB,
        ADMOB,
        FACEBOOK,
        OWNADS,
        OWNADS_QUREKA,
        OWNADS_ADMOB,
        OWNADS_FACEBOOK,
        QUREKA
    }

    public enum QurekaType {
        WIN_QUREKA,
        GAME_PREDCHAMP
    }

}
