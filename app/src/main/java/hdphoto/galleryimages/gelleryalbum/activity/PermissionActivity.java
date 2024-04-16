package hdphoto.galleryimages.gelleryalbum.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.iten.tenoku.ad.AdShow;
import com.iten.tenoku.ad.HandleClick.HandleClick;
import com.iten.tenoku.utils.AdUtils;
import com.permissionx.guolindev.PermissionX;

import hdphoto.galleryimages.gelleryalbum.databinding.ActivityPermissionBinding;
import hdphoto.galleryimages.gelleryalbum.utils.Preference;
import hdphoto.galleryimages.gelleryalbum.R;


public class PermissionActivity extends AppCompatActivity {
    ActivityPermissionBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setFlags(1024, 1024);
        binding = ActivityPermissionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.tvAllow.setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                PermissionX.init(PermissionActivity.this)
                        .permissions(Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO, Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.MANAGE_EXTERNAL_STORAGE)
                        .explainReasonBeforeRequest()
                        .onExplainRequestReason((scope, deniedList, beforeRequest) -> {
                            scope.showRequestReasonDialog(deniedList, getString(R.string.permissionx_needs_following_permissions_to_continue), "Allow");
                        })
                        .onForwardToSettings((scope, deniedList) -> {
                            scope.showForwardToSettingsDialog(deniedList, getString(R.string.please_allow_following_permissions_in_settings), "Allow");
                        })
                        .request((allGranted, grantedList, deniedList) -> {
                            if (!allGranted) {
                                Toast.makeText(PermissionActivity.this, getString(R.string.the_following_permissions_are_denied) + deniedList, Toast.LENGTH_SHORT).show();
                            } else {
                                Preference.setFirstLaunchDone(PermissionActivity.this);

                                AdShow.getInstance(PermissionActivity.this).ShowAd(new HandleClick() {
                                    @Override
                                    public void Show(boolean adShow) {
                                        startActivity(new Intent(PermissionActivity.this, MainActivity.class));
                                        finish();
                                    }
                                }, AdUtils.ClickType.MAIN_CLICK);

                            }
                        });
            } else {
                PermissionX.init((FragmentActivity) PermissionActivity.this)
                        .permissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.MANAGE_EXTERNAL_STORAGE)
                        .explainReasonBeforeRequest()
                        .onExplainRequestReason((scope, deniedList, beforeRequest) -> {
                            scope.showRequestReasonDialog(deniedList, getString(R.string.permissionx_needs_following_permissions_to_continue), "Allow");
                        })
                        .onForwardToSettings((scope, deniedList) -> {
                            scope.showForwardToSettingsDialog(deniedList, getString(R.string.please_allow_following_permissions_in_settings), "Allow");
                        })
                        .request((allGranted, grantedList, deniedList) -> {
                            if (!allGranted) {
                                Toast.makeText(PermissionActivity.this, getString(R.string.the_following_permissions_are_denied) + deniedList, Toast.LENGTH_SHORT).show();
                            } else {
                                Preference.setFirstLaunchDone(PermissionActivity.this);

                                AdShow.getInstance(PermissionActivity.this).ShowAd(new HandleClick() {
                                    @Override
                                    public void Show(boolean adShow) {
                                        startActivity(new Intent(PermissionActivity.this, MainActivity.class));
                                        finish();
                                    }
                                }, AdUtils.ClickType.MAIN_CLICK);
                            }
                        });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
