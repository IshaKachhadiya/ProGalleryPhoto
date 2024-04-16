package hdphoto.galleryimages.gelleryalbum.Ads_Common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.iten.tenoku.utils.MyApplication;

import java.util.Random;

import hdphoto.galleryimages.gelleryalbum.R;
import hdphoto.galleryimages.gelleryalbum.activity.BaseActivity;
import hdphoto.galleryimages.gelleryalbum.databinding.ActivityUnderMaintenanceBinding;

public class UnderMaintenanceActivity extends BaseActivity {

    String TAG = getClass().getSimpleName();
    boolean doubleBackToExitPressedOnce = false;
    ActivityUnderMaintenanceBinding x;
    Activity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x = ActivityUnderMaintenanceBinding.inflate(getLayoutInflater());
        setContentView(x.getRoot());
        activity = this;

        int i = new Random().nextInt(3);
        Log.e(TAG, "onCreate: " + i);

        if (i == 1) {
            x.animationView.setAnimation(com.iten.tenoku.R.raw.app_update1);
            x.textTitle.setTextColor(getResources().getColor(R.color.app_update_1));
            x.textTitle.setText(getResources().getString(R.string.clap_under_maintenance_1));
        } else if (i == 2) {
            x.animationView.setAnimation(com.iten.tenoku.R.raw.app_update2);
            x.textTitle.setTextColor(getResources().getColor(R.color.app_update_2));
            x.textTitle.setText(getResources().getString(R.string.clap_under_maintenance_2));
        } else {
            x.animationView.setAnimation(com.iten.tenoku.R.raw.app_update3);
            x.textTitle.setTextColor(getResources().getColor(R.color.app_update_3));
            x.textTitle.setText(getResources().getString(R.string.clap_under_maintenance_3));
        }
    }

    @Override
    public void onBackPressed() {
        if (MyApplication.sharedPreferencesHelper.getExitScreen()) {
            startActivity(new Intent(activity, AdsExitActivity.class).putExtra("Under", true));
            finish();
        } else {
            promises_DoubleExit();
        }
    }

    public void promises_DoubleExit() {
        if (!doubleBackToExitPressedOnce) {
            doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit.", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);

        } else {
            doubleBackToExitPressedOnce = false;
            finishAffinity();
        }
    }

}