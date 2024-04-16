package hdphoto.galleryimages.gelleryalbum.activity;

import static com.iten.tenoku.ad.AdShow.getInstance;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iten.tenoku.ad.AdShow;
import com.iten.tenoku.ad.HandleClick.HandleClick;
import com.iten.tenoku.utils.AdUtils;

import hdphoto.galleryimages.gelleryalbum.constant.Common;
import hdphoto.galleryimages.gelleryalbum.utils.FolderPath;
import hdphoto.galleryimages.gelleryalbum.utils.PrefClass;
import hdphoto.galleryimages.gelleryalbum.R;

public class SettingActivity extends BseActivity {
    ImageView img2;
    ImageView img3;
    ImageView img4;
    ImageView img_back;
    LinearLayout ll_albumList;
    LinearLayout ll_changePassword;
    LinearLayout ll_changeQuestion;
    LinearLayout ll_dataList;
    LinearLayout ll_option_dialog;
    PrefClass preferenceClass;
    RelativeLayout rel_image_main;
    RelativeLayout rl_column2,rl_column3,rl_column4,rl_column5,rl_column6,rl_column7,rl_column8;
    String toastString;
    TextView txt_columnTitle;
    Typeface typeface;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_setting);

        preferenceClass = new PrefClass(this);

        initVIew();
        clickListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getInstance(SettingActivity.this).ShowNativeAd(findViewById(R.id.native_ad_layout), AdUtils.NativeType.NATIVE_MEDIUM);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initVIew() {
        img_back = findViewById(R.id.img_back);
        ll_changePassword = findViewById(R.id.ll_changePassword);
        ll_albumList = findViewById(R.id.ll_albumList);
        ll_dataList = findViewById(R.id.ll_dataList);
        ll_option_dialog = findViewById(R.id.option_dialog);
        ll_changeQuestion = findViewById(R.id.ll_changeQuestion);
        rel_image_main = findViewById(R.id.rel_image_main);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        img4 = findViewById(R.id.img4);
        rl_column2 = findViewById(R.id.rl_column2);
        rl_column3 = findViewById(R.id.rl_column3);
        rl_column4 = findViewById(R.id.rl_column4);
        rl_column5 = findViewById(R.id.rl_column5);
        rl_column6 = findViewById(R.id.rl_column6);
        rl_column7 = findViewById(R.id.rl_column7);
        rl_column8 = findViewById(R.id.rl_column8);
        txt_columnTitle = findViewById(R.id.txt_columnTitle);

        typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Medium.ttf");
    }

    private void clickListener() {
        img_back.setOnClickListener(view -> {
            HideLayout();
            onBackPressed();
        });

        ll_changePassword.setOnClickListener(view -> {
            HideLayout();
            if (preferenceClass.getInt(Common.gIsLockStatus, 0) == 0) {
                SecurityDialog();
            } else {
                Intent intent = new Intent(SettingActivity.this, PasswordChangeActivity.class);
                intent.putExtra(Common.gActivityname, "MainActivity");
                startActivity(intent);
            }
        });

        ll_changeQuestion.setOnClickListener(view -> {
            HideLayout();
            startActivity(new Intent(SettingActivity.this, SecurityQAActivity.class).putExtra("toActivity", "SettingActivity"));
        });

        rel_image_main.setOnClickListener(view -> HideLayout());

        ll_albumList.setOnClickListener(view -> {
            toastString = "";
            toastString = "Album";
            MainActivity.parameter = 0;
            VisibleLayout();
            img2.setImageDrawable(getResources().getDrawable(R.drawable.fld_column_2));
            img3.setImageDrawable(getResources().getDrawable(R.drawable.fld_column_3));
            img4.setImageDrawable(getResources().getDrawable(R.drawable.fld_column_4));
            txt_columnTitle.setText("Folder Column : ");
            rl_column5.setVisibility(View.GONE);
            rl_column6.setVisibility(View.GONE);
            rl_column7.setVisibility(View.GONE);
            rl_column8.setVisibility(View.GONE);
        });

        ll_dataList.setOnClickListener(view -> {
            SettingActivity.this.toastString = "";
            toastString = "Data";
            MainActivity.parameter = 1;
            VisibleLayout();
            img2.setImageDrawable(getResources().getDrawable(R.drawable.img_column_2));
            img3.setImageDrawable(getResources().getDrawable(R.drawable.img_column_3));
            img4.setImageDrawable(getResources().getDrawable(R.drawable.img_column_4));
            txt_columnTitle.setText("Data Column : ");
            rl_column5.setVisibility(View.VISIBLE);
            rl_column6.setVisibility(View.VISIBLE);
            rl_column7.setVisibility(View.VISIBLE);
            rl_column8.setVisibility(View.VISIBLE);
        });

        rl_column2.setOnClickListener(view -> {
            SetToast(2);
            if (MainActivity.parameter == 0) {
                MainActivity.preferenceAlbumUtils.putInt(Common.gAlbumColumns, 2);
                MainActivity.albumDivider = 4;
                PassTag();
            } else if (MainActivity.parameter == 1) {
                MainActivity.preferenceDataUtils.putInt(Common.gDataColumns, 2);
                MainActivity.dataDivider = 4;
                PassTag();
            }
            HideLayout();
        });

        rl_column3.setOnClickListener(view -> {
            SetToast(3);
            if (MainActivity.parameter == 0) {
                MainActivity.preferenceAlbumUtils.putInt(Common.gAlbumColumns, 3);
                MainActivity.albumDivider = 6;
                PassTag();
            } else if (MainActivity.parameter == 1) {
                MainActivity.preferenceDataUtils.putInt(Common.gDataColumns, 3);
                MainActivity.dataDivider = 6;
                PassTag();
            }
            HideLayout();
        });

        rl_column4.setOnClickListener(view -> {
            SetToast(4);
            if (MainActivity.parameter == 0) {
                MainActivity.preferenceAlbumUtils.putInt(Common.gAlbumColumns, 4);
                MainActivity.albumDivider = 8;
                PassTag();
            } else if (MainActivity.parameter == 1) {
                MainActivity.preferenceDataUtils.putInt(Common.gDataColumns, 4);
                MainActivity.dataDivider = 8;
                PassTag();
            }
            HideLayout();
        });

        rl_column5.setOnClickListener(view -> {
            SetToast(5);
            if (MainActivity.parameter == 0) {
                MainActivity.preferenceAlbumUtils.putInt(Common.gAlbumColumns, 5);
                MainActivity.albumDivider = 10;
                PassTag();
            } else if (MainActivity.parameter == 1) {
                MainActivity.preferenceDataUtils.putInt(Common.gDataColumns, 5);
                MainActivity.dataDivider = 10;
                PassTag();
            }
            HideLayout();
        });

        rl_column6.setOnClickListener(view -> {
            SetToast(6);
            if (MainActivity.parameter == 0) {
                MainActivity.preferenceAlbumUtils.putInt(Common.gAlbumColumns, 6);
                MainActivity.albumDivider = 12;
                PassTag();
            } else if (MainActivity.parameter == 1) {
                MainActivity.preferenceDataUtils.putInt(Common.gDataColumns, 6);
                MainActivity.dataDivider = 12;
                PassTag();
            }
            HideLayout();
        });

        rl_column7.setOnClickListener(view -> {
            SetToast(7);
            if (MainActivity.parameter == 0) {
                MainActivity.preferenceAlbumUtils.putInt(Common.gAlbumColumns, 7);
                MainActivity.albumDivider = 14;
                PassTag();
            } else if (MainActivity.parameter == 1) {
                MainActivity.preferenceDataUtils.putInt(Common.gDataColumns, 7);
                MainActivity.dataDivider = 14;
                PassTag();
            }
            HideLayout();
        });

        rl_column8.setOnClickListener(view -> {
            SetToast(8);
            if (MainActivity.parameter == 0) {
                MainActivity.preferenceAlbumUtils.putInt(Common.gAlbumColumns, 8);
                MainActivity.albumDivider = 16;
                PassTag();
            } else if (MainActivity.parameter == 1) {
                MainActivity.preferenceDataUtils.putInt(Common.gDataColumns, 8);
                MainActivity.dataDivider = 16;
                PassTag();
            }
            HideLayout();
        });
    }

    public void SetToast(int i) {
        String str;
        if (i == 0 || (str = toastString) == null) {
            return;
        }
        if (str.equals("Album")) {
            Toast.makeText(this, "Set Album Column " + i + " Successfully", Toast.LENGTH_SHORT).show();
        } else if (toastString.equals("Data")) {
            Toast.makeText(this, "Set Data Column " + i + " Successfully", Toast.LENGTH_SHORT).show();
        }
    }

    public void HideLayout() {
        rel_image_main.setVisibility(View.GONE);
        ll_option_dialog.setVisibility(View.GONE);
    }

    public void VisibleLayout() {
        rel_image_main.setVisibility(View.VISIBLE);
        ll_option_dialog.setVisibility(View.VISIBLE);
    }

    public void PassTag() {
        MainActivity.oriTag = "SettingActivity";
    }

    public void SecurityDialog() {
        final Dialog dialog = new Dialog(this, R.style.ThemeWithCorners1);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialog_security);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        ((TextView) dialog.findViewById(R.id.textDesc)).setText("Set your security lock for hide photos & videos!");

        ((TextView) dialog.findViewById(R.id.rl_yes)).setOnClickListener(view -> {
            FolderPath.lock_screen = 1;
            dialog.dismiss();
        });

        ((TextView) dialog.findViewById(R.id.rl_no)).setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }

    @Override
    public void onBackPressed() {
        AdShow.getInstance(SettingActivity.this).ShowAd(new HandleClick() {
            @Override
            public void Show(boolean adShow) {
                SettingActivity.super.onBackPressed();
            }
        }, AdUtils.ClickType.BACK_CLICK);
    }
}
