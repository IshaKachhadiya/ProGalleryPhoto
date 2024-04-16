package hdphoto.galleryimages.gelleryalbum.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.iten.tenoku.ad.AdShow;
import com.iten.tenoku.ad.HandleClick.HandleClick;
import com.iten.tenoku.utils.AdUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import hdphoto.galleryimages.gelleryalbum.constant.Common;
import hdphoto.galleryimages.gelleryalbum.databinding.ActivityForgetPasswordBinding;
import hdphoto.galleryimages.gelleryalbum.utils.FolderPath;
import hdphoto.galleryimages.gelleryalbum.utils.PrefClass;
import hdphoto.galleryimages.gelleryalbum.R;

@SuppressWarnings("all")
public class ForgotPasswordActivity extends BseActivity {
    private static final String TAG = "ForgotPasswordActivity";
    public static ForgotPasswordActivity forgotPasswordActivity;
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            unregisterReceiver(broadcastReceiver);
            if (Common.IdentifyActivity.equals("PasswordLockActivity")) {
                Intent intent2 = new Intent(ForgotPasswordActivity.this, PasswordLockActivity.class);
                intent2.putExtra(Common.gActivityname, TAG);
                startActivity(intent2);
                Common.IdentifyActivity = "";
            }
        }
    };
    PrefClass preferenceClass;
    ActivityForgetPasswordBinding binding;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        binding = ActivityForgetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceClass = new PrefClass(getApplicationContext());

        initView();
        clickListener();
    }

    private void initView() {


        binding.toolbar.toolbarTitle.setText("Forgot Password");
        binding.editSecurityQuestion.setText(preferenceClass.getString(Common.gQuestion));
    }

    @Override
    protected void onResume() {
        super.onResume();
        AdShow.getInstance(this).ShowNativeAd(findViewById(R.id.nativeSmall).findViewById(R.id.native_ad_layout), AdUtils.NativeType.NATIVE_BANNER);
    }

    private void clickListener() {
        binding.toolbar.btnBack.setOnClickListener(view -> onBackPressed());

        binding.btnSubmitForgetPassword.setOnClickListener(view -> {
            RecoverSecurityPassword();
        });

        binding.editSecurityAnswer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    binding.editEmail.setText("");
                    binding.editEmail.setEnabled(true);
                    return;
                }
                binding.editEmail.setEnabled(true);
            }
        });

        binding.editEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    binding.editSecurityAnswer.setText("");
                    binding.editSecurityAnswer.setEnabled(false);
                    return;
                }
                binding.editSecurityAnswer.setEnabled(true);
            }
        });

        binding.imgSnap.setOnClickListener(view -> {
            String string = preferenceClass.getString(Common.gQuestionAnswer);
            if (binding.editSecurityAnswer.getText() == null) {
                Toast.makeText(ForgotPasswordActivity.this, getString(R.string.please_enter_answer), Toast.LENGTH_SHORT).show();
            } else if (binding.editSecurityAnswer.getText().toString().trim().equals(string)) {
                binding.editSecurityAnswer.setFocusable(false);
                TakeSnap(binding.rlScreen);
            } else {
                Toast.makeText(ForgotPasswordActivity.this, getString(R.string.invalid_answer_please_enter_correct_answer), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void TakeSnap(RelativeLayout relativeLayout) {
        relativeLayout.setDrawingCacheEnabled(true);
        Bitmap createBitmap = Bitmap.createBitmap(relativeLayout.getDrawingCache());
        relativeLayout.setDrawingCacheEnabled(false);
        File file = new File(FolderPath.SAVE_SECURITY_QA_IMAGE_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
        File file2 = new File(FolderPath.SAVE_SECURITY_QA_IMAGE_PATH + File.separator + "SecurityFile.png");
        StringBuilder sb = new StringBuilder();
        sb.append(" --- ");
        sb.append(file2.getAbsolutePath());
        if (file2.exists()) {
            file2.delete();
        } else {
            try {
                file2.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            createBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        try {
            Uri uriForFile = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", file2);
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("*/*");
            intent.putExtra("android.intent.extra.STREAM", uriForFile);
            startActivity(Intent.createChooser(intent, "Share IMAGE"));
        } catch (Exception e3) {
            e3.printStackTrace();
            Toast.makeText(this, "Something Went Wrong.", Toast.LENGTH_SHORT).show();
        }
    }

    private void RecoverSecurityPassword() {
        if (binding.editSecurityAnswer.isEnabled()) {
            if (binding.editSecurityAnswer.getText().toString().length() == 0) {
                Toast.makeText(this, "Enter Answer.", Toast.LENGTH_SHORT).show();
            } else if (binding.editSecurityAnswer.getText().toString().trim().equals(preferenceClass.getString(Common.gQuestionAnswer))) {
                ArrayList<String> listString = preferenceClass.getListString(Common.gCurrentPassword);
                StringBuilder sb = new StringBuilder();
                Iterator<String> it = listString.iterator();
                while (it.hasNext()) {
                    sb.append(it.next());
                }
                registerReceiver(broadcastReceiver, new IntentFilter("PasswordLockActivity"));
                Common.IdentifyActivity = "PasswordLockActivity";
                activity.sendBroadcast(new Intent(Common.IdentifyActivity));
            } else {
                Toast.makeText(this, getString(R.string.invalid_answer_please_enter_correct_answer), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        AdShow.getInstance(ForgotPasswordActivity.this).ShowAd(new HandleClick() {
            @Override
            public void Show(boolean adShow) {
                ForgotPasswordActivity.super.onBackPressed();
            }
        }, AdUtils.ClickType.MAIN_CLICK);
        super.onBackPressed();
    }
}
