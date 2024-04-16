package hdphoto.galleryimages.gelleryalbum.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.material.snackbar.Snackbar;
import com.iten.tenoku.utils.AdConstant;

public class BaseActivity extends AppCompatActivity {
    ProgressDialog mProgressDialog;

    public void isPermissionGranted(boolean z, String str) {
    }

    public boolean requestPermission(String str) {
        boolean z = ContextCompat.checkSelfPermission(this, str) == 0;
        if (!z) {
            ActivityCompat.requestPermissions(this, new String[]{str}, 52);
        }
        return z;
    }

    public void CreateFullScreen() {
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
    }

    @Override
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 52) {
            isPermissionGranted(iArr[0] == 0, strArr[0]);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        AdConstant.isResume = true;
    }

    public void showLoading(String str) {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(str);
        mProgressDialog.setProgressStyle(0);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    public void HideLoading() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    public void ShowSnackBar(String str) {
        @SuppressLint("ResourceType") View findViewById = findViewById(16908290);
        if (findViewById != null) {
            Snackbar.make(findViewById, str, -1).show();
        } else {
            Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
        }
    }
}
