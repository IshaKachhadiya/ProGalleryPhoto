package hdphoto.galleryimages.gelleryalbum.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.iten.tenoku.ad.AdShow;
import com.iten.tenoku.ad.HandleClick.HandleClick;
import com.iten.tenoku.utils.AdUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import hdphoto.galleryimages.gelleryalbum.constant.Common;
import hdphoto.galleryimages.gelleryalbum.utils.FolderPath;
import hdphoto.galleryimages.gelleryalbum.utils.PrefClass;
import hdphoto.galleryimages.gelleryalbum.R;

@SuppressWarnings("all")
public class SecurityQAActivity extends BseActivity {
    public static SecurityQAActivity securityQuetionScreen;
    EditText edtAnswer;
    EditText edtEmail;
    String fromActivity;
    ImageView imgBack;
    ImageView img_Snap;
    PrefClass preferenceClass;
    RelativeLayout rl_screen;
    Toolbar toolbar;
    TextView txtSubmit;
    TextView txtToolbarTitle;
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            unregisterReceiver(broadcastReceiver);
            if (Common.IdentifyActivity.equals("PasswordLockActivity")) {
                Intent intent2 = new Intent(SecurityQAActivity.this, PasswordLockActivity.class);
                intent2.putExtra(Common.gActivityname, "SecurityQAActivity");
                startActivity(intent2);
                Common.IdentifyActivity = "";
                finish();
            }
        }
    };
    String question = "null";

    @Override
    public void onPointerCaptureChanged(boolean z) {
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_security_question);

        initView();

        preferenceClass = new PrefClass(getApplicationContext());
        fromActivity = getIntent().getStringExtra("toActivity");

        SetSpinner();
        clickListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AdShow.getInstance(this).ShowCollapseBanner(SecurityQAActivity.this, findViewById(R.id.layout_ads));

    }


    private void initView() {
        imgBack = findViewById(R.id.btnBack);
        txtToolbarTitle = findViewById(R.id.toolbarTitle);
        txtSubmit = findViewById(R.id.btnSubmit);
        edtAnswer = findViewById(R.id.editAnswer);
        edtEmail = findViewById(R.id.editEmail);
        img_Snap = findViewById(R.id.img_Snap);
        rl_screen = findViewById(R.id.rl_screen);

        txtToolbarTitle.setText("Security Question");
    }

    private void clickListener() {
        imgBack.setOnClickListener(view -> onBackPressed());

        txtSubmit.setOnClickListener(view -> {
            if (question.equals("Select Question")) {
                Toast.makeText(SecurityQAActivity.this, "Please select question.", Toast.LENGTH_SHORT).show();
            } else if (edtAnswer.getText() == null) {
                Toast.makeText(SecurityQAActivity.this, "Please enter answer.", Toast.LENGTH_SHORT).show();
            } else if (edtAnswer.getText().length() >= 4) {
                AdShow.getInstance(SecurityQAActivity.this).ShowAd(new HandleClick() {
                    @Override
                    public void Show(boolean adShow) {
                        preferenceClass.putString(Common.gQuestion, question);
                        preferenceClass.putString(Common.gQuestionAnswer, edtAnswer.getText().toString().trim());
                        preferenceClass.putString("email", edtEmail.getText().toString().trim());
                        if (fromActivity == null) {
                            registerReceiver(broadcastReceiver, new IntentFilter("PasswordLockActivity"),RECEIVER_EXPORTED);
                            Common.IdentifyActivity = "PasswordLockActivity";
                            BseActivity.activity.sendBroadcast(new Intent(Common.IdentifyActivity));
                        } else if (fromActivity.equals("SettingActivity")) {
                            finish();
                        } else {
                            registerReceiver(broadcastReceiver, new IntentFilter("PasswordLockActivity"),RECEIVER_EXPORTED);
                            Common.IdentifyActivity = "PasswordLockActivity";
                            BseActivity.activity.sendBroadcast(new Intent(Common.IdentifyActivity));
                        }
                    }
                }, AdUtils.ClickType.MAIN_CLICK);
            } else {
                Toast.makeText(SecurityQAActivity.this, "atleast enter 4 character in security answer", Toast.LENGTH_SHORT).show();
            }
        });

        img_Snap.setOnClickListener(view -> {
            if (question.equals("Select Question")) {
                Toast.makeText(SecurityQAActivity.this, "Please select question.", Toast.LENGTH_SHORT).show();
            } else if (edtAnswer.getText() == null) {
                Toast.makeText(SecurityQAActivity.this, "Please enter answer.", Toast.LENGTH_SHORT).show();
            } else if (edtAnswer.getText().length() >= 4) {
                edtAnswer.setFocusable(false);
                TakeSnap(rl_screen);
            } else {
                Toast.makeText(SecurityQAActivity.this, "At least enter 4 character in security answer.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        AdShow.getInstance(SecurityQAActivity.this).ShowAd(new HandleClick() {
            @Override
            public void Show(boolean adShow) {
                SecurityQAActivity.super.onBackPressed();
            }
        }, AdUtils.ClickType.BACK_CLICK);
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

    public void SetSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.spinnerCustom);
        ArrayList arrayList = new ArrayList();
        arrayList.add("Select Question");
        arrayList.add("What was your childhood nickname?");
        arrayList.add("In what city or town did your parent meet?");
        arrayList.add("What is your favorite movie?");
        arrayList.add("What was your favorite food as a child?");
        arrayList.add("Who is your childhood sports hero?");
        arrayList.add("In what town was your first job?");
        spinner.setAdapter((SpinnerAdapter) new CustomSpinnerAdapter(this, arrayList));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                question = adapterView.getItemAtPosition(i).toString();
            }
        });
    }

    public class CustomSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {
        protected ArrayList<String> arrayList;

        @Override
        public long getItemId(int i) {
            return i;
        }

        public CustomSpinnerAdapter(Context context, ArrayList<String> arrayList) {
            this.arrayList = arrayList;
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return arrayList.get(i);
        }

        @Override
        public View getDropDownView(int i, View view, ViewGroup viewGroup) {
            TextView textView = new TextView(SecurityQAActivity.this);
            textView.setPadding(18, 18, 18, 18);
            textView.setTextSize(15.0f);
            textView.setGravity(16);
            textView.setText(arrayList.get(i));
            textView.setTextColor(getResources().getColor(R.color.black));
            textView.setBackgroundColor(getResources().getColor(R.color.white));
            return textView;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            TextView textView = new TextView(SecurityQAActivity.this);
            textView.setGravity(16);
            textView.setPadding(35, 18, 35, 18);
            textView.setTextSize(15.0f);
            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.dropdownarrow, 0);
            textView.setText(arrayList.get(i));
            return textView;
        }
    }
}
