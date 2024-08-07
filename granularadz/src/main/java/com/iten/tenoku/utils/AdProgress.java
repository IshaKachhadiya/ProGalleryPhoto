package com.iten.tenoku.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;

import com.iten.tenoku.R;


public class AdProgress extends Dialog {

    Context mContext;

    public AdProgress(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ad_progress);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().getAttributes().gravity = Gravity.CENTER;

        findViewById(R.id.custProgressBar).setClickable(false);
        findViewById(R.id.custProgressBar).setEnabled(false);
    }

}