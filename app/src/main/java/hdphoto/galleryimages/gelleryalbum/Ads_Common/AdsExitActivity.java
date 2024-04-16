package hdphoto.galleryimages.gelleryalbum.Ads_Common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.iten.tenoku.ad.AdShow;
import com.iten.tenoku.utils.AdUtils;

import hdphoto.galleryimages.gelleryalbum.activity.BaseActivity;
import hdphoto.galleryimages.gelleryalbum.databinding.ActivityExitBinding;


public class AdsExitActivity extends BaseActivity {
    ActivityExitBinding x;
    Activity activity;
    Boolean aBoolean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x = ActivityExitBinding.inflate(getLayoutInflater());
        setContentView(x.getRoot());
        activity = this;
        todo();
    }

    private void todo() {
        aBoolean = getIntent().getBooleanExtra("Under", false);
        x.tvYes.setOnClickListener(view -> {
                finishAffinity();
                System.exit(0);
        });

        x.tvNo.setOnClickListener(view -> {
            if (Boolean.TRUE.equals(aBoolean)) {
                startActivity(new Intent(activity, UnderMaintenanceActivity.class));
            } else{
                super.onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AdShow.getInstance(activity).ShowNativeAd(x.nativeBigAdLayout.nativeAdLayout, AdUtils.NativeType.NATIVE_BIG);
    }
}