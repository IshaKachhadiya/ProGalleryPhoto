package hdphoto.galleryimages.gelleryalbum.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import androidx.core.app.ActivityCompat;

import hdphoto.galleryimages.gelleryalbum.R;
import com.google.android.material.snackbar.Snackbar;

public class PermissionHelperActivity extends BseActivity {
    private final String[] permissions = {"android.permission.WRITE_EXTERNAL_STORAGE"};
    protected View view;

    public void hideViews() {
    }

    public void permissionGranted() {
    }


    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.WRITE_EXTERNAL_STORAGE")) {
            showRequestPermissionRationale();
        } else {
            showAppPermissionSettings();
        }
    }

    private void showRequestPermissionRationale() {
        Snackbar.make(view, getString(R.string.permission_info), -2).setAction(getString(R.string.permission_ok), view -> ActivityCompat.requestPermissions(PermissionHelperActivity.this, permissions, 1000)).show();
    }

    private void showAppPermissionSettings() {
        Snackbar.make(view, getString(R.string.permission_force), -2).setAction(getString(R.string.permission_settings), view -> {
            Uri fromParts = Uri.fromParts(getString(R.string.permission_package), PermissionHelperActivity.this.getPackageName(), null);
            Intent intent = new Intent();
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.setData(fromParts);
            startActivityForResult(intent, 1000);
        }).show();
    }

    @Override
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i != 1000 || iArr.length == 0 || iArr[0] == -1) {
            permissionDenied();
        } else {
            permissionGranted();
        }
    }

    private void permissionDenied() {
        hideViews();
        requestPermission();
    }

    public void setView(View view) {
        this.view = view;
    }
}
