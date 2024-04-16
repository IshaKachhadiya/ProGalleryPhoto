package hdphoto.galleryimages.gelleryalbum.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.exifinterface.media.ExifInterface;


import com.iten.tenoku.ad.AdShow;
import com.iten.tenoku.ad.HandleClick.HandleClick;
import com.iten.tenoku.utils.AdUtils;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;

import hdphoto.galleryimages.gelleryalbum.constant.Common;
import hdphoto.galleryimages.gelleryalbum.databinding.ActivityPasswordChangeBinding;
import hdphoto.galleryimages.gelleryalbum.utils.AppUtilsClass;
import hdphoto.galleryimages.gelleryalbum.utils.PrefClass;
import hdphoto.galleryimages.gelleryalbum.R;

@SuppressWarnings("all")
public class PasswordChangeActivity extends BseActivity {
    int enterPin = 0;
    LinearLayout lyPinLockView;
    LinearLayout lyShakeAnimationgallery;
    ArrayList<String> origanalPin;
    ArrayList<String> pinArray;
    ActivityPasswordChangeBinding binding;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        binding = ActivityPasswordChangeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        pinArray = new ArrayList<>();
        preferenceClass = new PrefClass(getApplicationContext());

        initView();
        clickListener();
    }

    private void initView() {
        binding.toolbar.toolbarTitle.setText(getString(R.string.change_password));
        binding.txtHintTextPassword.setText(getString(R.string.enter_old_password));
    }

    private void clickListener() {
        binding.toolbar.btnBack.setOnClickListener(view -> onBackPressed());

        binding.btnNumber8.setOnClickListener(view -> CheckSecurityPassword("0"));

        binding.btnNumber1.setOnClickListener(view -> CheckSecurityPassword("1"));

        binding.btnNumber2.setOnClickListener(view -> CheckSecurityPassword("2"));

        binding.btnNumber3.setOnClickListener(view -> CheckSecurityPassword("3"));

        binding.btnNumber4.setOnClickListener(view -> CheckSecurityPassword("4"));

        binding.btnNumber5.setOnClickListener(view -> CheckSecurityPassword("5"));

        binding.btnNumber6.setOnClickListener(view -> CheckSecurityPassword("6"));

        binding.btnNumber7.setOnClickListener(view -> CheckSecurityPassword("7"));

        binding.btnNumber8.setOnClickListener(view -> CheckSecurityPassword("8"));

        binding.btnNumber9.setOnClickListener(view -> CheckSecurityPassword("9"));

        binding.btnClearNumber.setOnClickListener(view -> ClearPassWord());
    }

    @Override
    public void onBackPressed() {
        AdShow.getInstance(PasswordChangeActivity.this).ShowAd(new HandleClick() {
            @Override
            public void Show(boolean adShow) {
                PasswordChangeActivity.super.onBackPressed();
            }
        }, AdUtils.ClickType.BACK_CLICK);
    }

    public void ClearPassWord() {
        if (pinArray.size() == 0) {
            binding.txtHintTextPassword.setText("Enter Password.");
            pinArray.clear();
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
    }

    public void CheckSecurityPassword(String str) {
        pinArray.add(str);
        if (pinArray.size() == 1) {
            binding.hidePin1.setBackground(getResources().getDrawable(R.drawable.ic_lock_ring_fill));
        } else if (pinArray.size() == 2) {
            binding.hidePin2.setBackground(getResources().getDrawable(R.drawable.ic_lock_ring_fill));
        } else if (pinArray.size() == 3) {
            binding.hidePin3.setBackground(getResources().getDrawable(R.drawable.ic_lock_ring_fill));
        } else if (pinArray.size() == 4) {
            binding.hidePin4.setBackground(getResources().getDrawable(R.drawable.ic_lock_ring_fill));
            UnlockPassword();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        AdShow.getInstance(this).ShowNativeAd(findViewById(R.id.nativeSmall).findViewById(R.id.native_ad_layout), AdUtils.NativeType.NATIVE_BANNER);
    }

    private void UnlockPassword() {
        if (!pinArray.equals(preferenceClass.getListString(Common.gCurrentPassword))) {
            if (enterPin == 1) {
                preferenceClass.putListString(Common.gTempPassword, pinArray);
                pinArray.clear();
                binding.txtHintTextPassword.setText(getString(R.string.confirm_password));
                binding.hidePin1.setBackground(getResources().getDrawable(R.drawable.ic_lock_ring_null));
                binding.hidePin2.setBackground(getResources().getDrawable(R.drawable.ic_lock_ring_null));
                binding.hidePin3.setBackground(getResources().getDrawable(R.drawable.ic_lock_ring_null));
                binding.hidePin4.setBackground(getResources().getDrawable(R.drawable.ic_lock_ring_null));
                enterPin = 2;
            } else if (enterPin != 2) {
                Toast.makeText(getApplicationContext(), getString(R.string.invalid_password), Toast.LENGTH_SHORT).show();
                binding.txtHintTextPassword.setText(getString(R.string.enter_old_password));
                pinArray.clear();
                lyShakeAnimationgallery.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake_anim));
                ((Vibrator) getSystemService(Context.VIBRATOR_SERVICE)).vibrate(800L);
                binding.hidePin1.setBackground(getResources().getDrawable(R.drawable.ic_lock_ring_null));
                binding.hidePin2.setBackground(getResources().getDrawable(R.drawable.ic_lock_ring_null));
                binding.hidePin3.setBackground(getResources().getDrawable(R.drawable.ic_lock_ring_null));
                binding.hidePin4.setBackground(getResources().getDrawable(R.drawable.ic_lock_ring_null));
            } else if (getStingListToString(preferenceClass.getListString(Common.gTempPassword)).equals(getStingListToString(this.pinArray))) {
                preferenceClass.putListString(Common.gCurrentPassword, pinArray);
                preferenceClass.putInt(Common.gIsLockStatus, 2);
                StoreData(pinArray);
                finish();
            } else {
                if (enterPin == 2) {
                    enterPin = 1;
                    Toast.makeText(getApplicationContext(), "Invalid Confirm Password.", Toast.LENGTH_SHORT).show();
                    binding.txtHintTextPassword.setText(R.string.enter_new_password);
                } else {
                    enterPin = 0;
                }
                ResetAll();
            }
        } else if (enterPin == 0) {
            origanalPin = pinArray;
            pinArray.clear();
            binding.txtHintTextPassword.setText(getString(R.string.enter_new_password));
            binding.hidePin1.setBackground(getResources().getDrawable(R.drawable.ic_lock_ring_null));
            binding.hidePin2.setBackground(getResources().getDrawable(R.drawable.ic_lock_ring_null));
            binding.hidePin3.setBackground(getResources().getDrawable(R.drawable.ic_lock_ring_null));
            binding.hidePin4.setBackground(getResources().getDrawable(R.drawable.ic_lock_ring_null));
            enterPin = 1;
        } else {
            enterPin = 1;
            Toast.makeText(this, getString(R.string.you_can_t_use_old_password_as_current_password), Toast.LENGTH_SHORT).show();
            Toast.makeText(this, getString(R.string.set_diffrent_password), Toast.LENGTH_SHORT).show();
            ResetAll();
        }
    }

    private void ResetAll() {
        pinArray.clear();
        lyShakeAnimationgallery.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake_anim));
        ((Vibrator) getSystemService(Context.VIBRATOR_SERVICE)).vibrate(800L);
        binding.hidePin1.setBackground(getResources().getDrawable(R.drawable.ic_lock_ring_null));
        binding.hidePin2.setBackground(getResources().getDrawable(R.drawable.ic_lock_ring_null));
        binding.hidePin3.setBackground(getResources().getDrawable(R.drawable.ic_lock_ring_null));
        binding.hidePin4.setBackground(getResources().getDrawable(R.drawable.ic_lock_ring_null));
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
}
