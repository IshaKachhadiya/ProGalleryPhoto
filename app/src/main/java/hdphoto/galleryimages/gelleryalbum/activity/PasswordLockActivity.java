package hdphoto.galleryimages.gelleryalbum.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iten.tenoku.ad.AdShow;
import com.iten.tenoku.ad.HandleClick.HandleClick;
import com.iten.tenoku.utils.AdUtils;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;

import hdphoto.galleryimages.gelleryalbum.constant.Common;
import hdphoto.galleryimages.gelleryalbum.databinding.ActivityPasswordLockBinding;
import hdphoto.galleryimages.gelleryalbum.utils.AppUtilsClass;
import hdphoto.galleryimages.gelleryalbum.R;

@SuppressWarnings("all")
public class PasswordLockActivity extends BseActivity {
    String activityName;
    LinearLayout ll_shake_anim;
    ArrayList<String> pinArray;
    boolean pinConfirm = false;
    ActivityPasswordLockBinding binding;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        binding = ActivityPasswordLockBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        if (intent == null) {
            finish();
        }
        activityName = intent.getStringExtra(Common.gActivityname);

        pinArray = new ArrayList<>();

        SecurityQAActivity.securityQuetionScreen = new SecurityQAActivity();

        initView();
        clickListener();
    }

    private void initView() {
        ll_shake_anim = findViewById(R.id.layoutShakeAnimation);

        binding.toolbar.toolbarTitle.setText(R.string.set_password);
        binding.txtHintTextPassword.setText(getString(R.string.add_new_folder));
    }

    private void clickListener() {
        binding.toolbar.btnBack.setOnClickListener(view -> onBackPressed());

        binding.btnNumber0.setOnClickListener(view -> CheckPassword("0"));

        binding.btnNumber1.setOnClickListener(view -> CheckPassword("1"));

        binding.btnNumber2.setOnClickListener(view -> CheckPassword("2"));

        binding.btnNumber3.setOnClickListener(view -> CheckPassword("3"));

        binding.btnNumber4.setOnClickListener(view -> CheckPassword("4"));

        binding.btnNumber5.setOnClickListener(view -> CheckPassword("5"));

        binding.btnNumber6.setOnClickListener(view -> CheckPassword("6"));

        binding.btnNumber7.setOnClickListener(view -> CheckPassword("7"));

        binding.btnNumber8.setOnClickListener(view -> CheckPassword("8"));

        binding.btnNumber9.setOnClickListener(view -> CheckPassword("9"));

        binding.btnClearNumber.setOnClickListener(view -> ClearPassword());
    }

    @Override
    public void onBackPressed() {
        AdShow.getInstance(PasswordLockActivity.this).ShowAd(new HandleClick() {
            @Override
            public void Show(boolean adShow) {
                PasswordLockActivity.super.onBackPressed();
            }
        }, AdUtils.ClickType.BACK_CLICK);
    }

    public void ClearPassword() {
        if (pinArray.size() == 0) {
            binding.txtHintTextPassword.setText(getString(R.string.add_new_folder));
            pinArray.clear();
            pinConfirm = false;
        } else if (pinArray.size() == 1) {
            binding.hidePin1.setBackground(getResources().getDrawable(R.drawable.ic_lock_ring_null));
            pinArray.remove(pinArray.get(0));
        } else if (pinArray.size() == 2) {
            binding.hidePin2.setBackground(getResources().getDrawable(R.drawable.ic_lock_ring_null));
            pinArray.remove(pinArray.get(1));
        } else if (pinArray.size() == 3) {
            binding.hidePin3.setBackground(getResources().getDrawable(R.drawable.ic_lock_ring_null));
            pinArray.remove(pinArray.get(2));
        } else if (pinArray.size() == 4) {
            binding.hidePin4.setBackground(getResources().getDrawable(R.drawable.ic_lock_ring_null));
            pinArray.remove(pinArray.get(3));
        }
        preferenceClass.putListString(Common.gCurrentPassword, pinArray);
    }

    public void CheckPassword(String str) {
        pinArray.add(str);
        if (pinArray.size() == 1) {
            binding.hidePin1.setBackground(getResources().getDrawable(R.drawable.ic_lock_ring_fill));
        } else if (pinArray.size() == 2) {
            binding.hidePin2.setBackground(getResources().getDrawable(R.drawable.ic_lock_ring_fill));
        } else if (pinArray.size() == 3) {
            binding.hidePin3.setBackground(getResources().getDrawable(R.drawable.ic_lock_ring_fill));
        } else if (pinArray.size() == 4) {
            binding.hidePin4.setBackground(getResources().getDrawable(R.drawable.ic_lock_ring_fill));
            if (pinConfirm) {
                if (pinArray.equals(preferenceClass.getListString(Common.gCurrentPassword))) {
                    MainActivity.checkLockForFirstFile = true;
                    Toast.makeText(this, "Password Set Successfully", Toast.LENGTH_SHORT).show();
                    preferenceClass.putInt(Common.gIsLockStatus, 1);
                    if (activityName.equals("SecurityQAActivity")) {
                        SecurityQAActivity.securityQuetionScreen.finish();
                    } else if (activityName.equals("ForgotPasswordActivity")) {
                        ForgotPasswordActivity.forgotPasswordActivity = new ForgotPasswordActivity();
                        ForgotPasswordActivity.forgotPasswordActivity.finish();
                    }
                    StoreData(pinArray);
                    finish();
                    return;
                }
                Toast.makeText(this, "No match Password.", Toast.LENGTH_SHORT).show();
                binding.txtHintTextPassword.setText("Add New Password.");
                pinArray.clear();
                ll_shake_anim.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake_anim));
                ((Vibrator) getSystemService(Context.VIBRATOR_SERVICE)).vibrate(800L);
                binding.hidePin1.setBackground(getResources().getDrawable(R.drawable.ic_lock_ring_null));
                binding.hidePin2.setBackground(getResources().getDrawable(R.drawable.ic_lock_ring_null));
                binding.hidePin3.setBackground(getResources().getDrawable(R.drawable.ic_lock_ring_null));
                binding.hidePin4.setBackground(getResources().getDrawable(R.drawable.ic_lock_ring_null));
                pinConfirm = false;
                return;
            }
            preferenceClass.putListString(Common.gCurrentPassword, pinArray);
            pinArray.clear();
            binding.txtHintTextPassword.setText("Confirm Password.");
            ConfirmPassword();
        }
    }

    private void StoreData(ArrayList<String> arrayList) {
        StringBuilder sb = new StringBuilder();
        Iterator<String> it = arrayList.iterator();
        while (it.hasNext()) {
            sb.append(it.next());
        }
        CreateFile(sb.toString());
    }

    public void CreateFile(String str) {
        try {
            FileWriter fileWriter = new FileWriter(AppUtilsClass.rootMainDir + "/.password.txt");
            fileWriter.write(str);
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ConfirmPassword() {
        binding.hidePin1.setBackground(getResources().getDrawable(R.drawable.ic_lock_ring_null));
        binding.hidePin2.setBackground(getResources().getDrawable(R.drawable.ic_lock_ring_null));
        binding.hidePin3.setBackground(getResources().getDrawable(R.drawable.ic_lock_ring_null));
        binding.hidePin4.setBackground(getResources().getDrawable(R.drawable.ic_lock_ring_null));
        pinConfirm = true;
    }
}
