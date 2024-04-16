package com.iten.tenoku.utils;


import static com.iten.tenoku.utils.MyApplication.sharedPreferencesHelper;

import android.app.Activity;
import android.os.Handler;
import android.widget.Toast;

import com.iten.tenoku.listeners.ExitListeners;

public class ExitUtils {

    public static ExitUtils exitUtils;
    public static Activity activity;
    boolean doubleBackToExitPressedOnce = false;

    public ExitUtils(Activity activity) {
        ExitUtils.activity = activity;
    }

    public static ExitUtils getInstance(Activity activity) {
        ExitUtils.activity = activity;
        if (exitUtils == null) {
            exitUtils = new ExitUtils(activity);
        }
        return exitUtils;
    }

    public void Exit(ExitListeners exitListener, int screenPosition) {
        if (sharedPreferencesHelper.getShowEntryScreen() == 0) {
            if (sharedPreferencesHelper.getShowEntryScreen() == screenPosition) {
                if (sharedPreferencesHelper.getExitScreen()) {
                    exitListener.onExitScreen();
                } else {
                    exitListener.onDoubleExit();
                    onDoubleTapExit();
                }
            } else {
                exitListener.onBackScreen();
            }


        } else if (sharedPreferencesHelper.getShowEntryScreen() == 1) {
            if (sharedPreferencesHelper.getShowEntryScreen() == screenPosition) {
                if (sharedPreferencesHelper.getExitScreen()) {
                    exitListener.onExitScreen();
                } else {
                    exitListener.onDoubleExit();
                    onDoubleTapExit();
                }
            } else {
                exitListener.onBackScreen();
            }


        } else if (sharedPreferencesHelper.getShowEntryScreen() == 2) {
            if (sharedPreferencesHelper.getShowEntryScreen() == screenPosition) {
                if (sharedPreferencesHelper.getExitScreen()) {
                    exitListener.onExitScreen();
                } else {
                    exitListener.onDoubleExit();
                    onDoubleTapExit();
                }

            } else {
                exitListener.onBackScreen();
            }

        } else if (sharedPreferencesHelper.getShowEntryScreen() == 3) {

            if (sharedPreferencesHelper.getShowEntryScreen() == screenPosition) {
                if (sharedPreferencesHelper.getExitScreen()) {
                    exitListener.onExitScreen();
                } else {
                    exitListener.onDoubleExit();
                    onDoubleTapExit();
                }

            } else {
                exitListener.onBackScreen();
            }

        } else if (sharedPreferencesHelper.getShowEntryScreen() == 4) {

            if (sharedPreferencesHelper.getShowEntryScreen() == screenPosition) {
                if (sharedPreferencesHelper.getExitScreen()) {
                    exitListener.onExitScreen();
                } else {
                    exitListener.onDoubleExit();
                    onDoubleTapExit();
                }
            } else {
                exitListener.onBackScreen();
            }

        } else if (sharedPreferencesHelper.getShowEntryScreen() == 5) {

            if (sharedPreferencesHelper.getShowEntryScreen() == screenPosition) {
                if (sharedPreferencesHelper.getExitScreen()) {
                    exitListener.onExitScreen();
                } else {
                    exitListener.onDoubleExit();
                    onDoubleTapExit();
                }
            } else {
                exitListener.onBackScreen();
            }

        }
    }

    public void onDoubleTapExit() {
        if (!doubleBackToExitPressedOnce) {
            doubleBackToExitPressedOnce = true;
            Toast.makeText(activity, "Please click BACK again to exit.", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
        } else {
            doubleBackToExitPressedOnce = false;
            activity.finishAffinity();
            System.exit(0);

        }
    }

}
