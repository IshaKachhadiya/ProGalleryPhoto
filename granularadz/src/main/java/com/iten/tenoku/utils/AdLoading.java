package com.iten.tenoku.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.iten.tenoku.R;


public class AdLoading extends Dialog {

    private static AdLoading mInstance;
    Context mContext;

    public AdLoading(Context context) {
        super(context);
        mContext = context;

    }

    public static AdLoading getInstance(Activity adShowingActivity) {
        if (mInstance == null) {
            mInstance = new AdLoading(adShowingActivity);
        }
        return mInstance;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ad_loading);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


    }

}