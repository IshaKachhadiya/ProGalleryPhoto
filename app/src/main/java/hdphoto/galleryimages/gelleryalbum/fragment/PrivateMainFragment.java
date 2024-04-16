package hdphoto.galleryimages.gelleryalbum.fragment;

import static android.content.Context.RECEIVER_EXPORTED;
import static androidx.core.content.ContextCompat.registerReceiver;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.google.android.material.tabs.TabLayout;
import com.iten.tenoku.ad.AdShow;
import com.iten.tenoku.ad.HandleClick.HandleClick;
import com.iten.tenoku.utils.AdUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import hdphoto.galleryimages.gelleryalbum.activity.ForgotPasswordActivity;
import hdphoto.galleryimages.gelleryalbum.activity.MainActivity;
import hdphoto.galleryimages.gelleryalbum.activity.PreviewActivity;
import hdphoto.galleryimages.gelleryalbum.activity.SecurityQAActivity;
import hdphoto.galleryimages.gelleryalbum.model.DataFileModel;
import hdphoto.galleryimages.gelleryalbum.constant.Common;
import hdphoto.galleryimages.gelleryalbum.utils.AppUtilsClass;
import hdphoto.galleryimages.gelleryalbum.utils.ConstantArrayClass;
import hdphoto.galleryimages.gelleryalbum.utils.FileUtils;
import hdphoto.galleryimages.gelleryalbum.utils.FolderPath;
import hdphoto.galleryimages.gelleryalbum.utils.PrefClass;
import hdphoto.galleryimages.gelleryalbum.utils.PrivateMasterPassword;
import hdphoto.galleryimages.gelleryalbum.R;

@SuppressWarnings("all")
public class PrivateMainFragment extends BaseFragment {
    static final String TAG = "PrivateMainFragment";
    ImagePrivateFragment imagePrivateFragment;
    ImageView iv_clear_num,iv_hide_pin_1, iv_hide_pin_2, iv_hide_pin_3;
    ImageView iv_hide_pin_4,iv_num_0, iv_num_1, iv_num_2, iv_num_3, iv_num_4, iv_num_5, iv_num_6, iv_num_7, iv_num_8, iv_num_9;
    View img_photo;
    View img_video;
    LinearLayout ll_Photos,ll_Videos,ll_HintLockView,ll_ShakeAnimation,ll_pinLockView;
    PrivateMasterPassword masterPin;
    ArrayList<String> originalPin;
    ArrayList<String> passwordArray;
    PrefClass preferenceClass;
    ProgressDialog progressLock;
    String progressTag;
    RelativeLayout rl_tabLayout;
    TabLayout tb_tabs;
    TextView txtHintTextPassword,txt_photo,txt_set_sequ,txtVideos,txt_confirm,txt_forgotPin;
    Typeface typeface;
    VideoPrivateFragment videoPrivateFragment;
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            requireContext().unregisterReceiver(broadcastReceiver);
            if (Common.IdentifyActivity.equals("SecurityQuestionActivity")) {
                requireContext().startActivity(new Intent(getActivity(), SecurityQAActivity.class));
            } else if (Common.IdentifyActivity.equals("ForgotPasswordActivity")) {
                requireContext().startActivity(new Intent(getActivity(), ForgotPasswordActivity.class));
            }
        }
    };
    int enterPin = 0;
    int isLockStatus = 0;
    boolean checkPass = false;
    int digit = 0;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_private_main, viewGroup, false);
        preferenceClass = new PrefClass(getActivity());
        isLockStatus = preferenceClass.getInt(Common.gIsLockStatus, 0);
        originalPin = new ArrayList<>();
        masterPin = new PrivateMasterPassword();

        ll_Photos = (LinearLayout) inflate.findViewById(R.id.layPhotos);
        ll_Videos = (LinearLayout) inflate.findViewById(R.id.layVideos);
        txt_photo = (TextView) inflate.findViewById(R.id.txtPhotos);
        txtVideos = (TextView) inflate.findViewById(R.id.txtVideos);
        img_photo = (View) inflate.findViewById(R.id.img_photo);
        img_video = (View) inflate.findViewById(R.id.img_video);

        initView(inflate);
        clickListener();

        passwordArray = new ArrayList<>();

        HiddenView();
        LayoutView();

        return inflate;
    }

    public void SetFragment() {
        imagePrivateFragment = new ImagePrivateFragment();
        videoPrivateFragment = new VideoPrivateFragment();
        MainActivity.privateclick = 0;
        changeFragment(new ImagePrivateFragment());

        ll_Photos.setOnClickListener(view -> {
            txt_photo.setTextColor(getResources().getColor(R.color.tab_txt));
            txtVideos.setTextColor(getResources().getColor(R.color.gray_73));
            img_photo.setVisibility(View.VISIBLE);
            img_video.setVisibility(View.INVISIBLE);
            MainActivity.privateclick = 0;
            changeFragment(new ImagePrivateFragment());
        });

        ll_Videos.setOnClickListener(view -> {
            txt_photo.setTextColor(getResources().getColor(R.color.gray_73));
            txtVideos.setTextColor(getResources().getColor(R.color.gray_73));
            img_photo.setVisibility(View.INVISIBLE);
            img_video.setVisibility(View.VISIBLE);
            MainActivity.privateclick = 1;
            changeFragment(new VideoPrivateFragment());
        });
    }

    @Override
    public void onHiddenChanged(boolean z) {
        super.onHiddenChanged(z);
        if (!z) {
            changeFragment(new ImagePrivateFragment());
            txt_photo.setTextColor(getResources().getColor(R.color.tab_txt));
            txtVideos.setTextColor(getResources().getColor(R.color.gray_73));
            img_photo.setVisibility(View.VISIBLE);
            img_video.setVisibility(View.INVISIBLE);
            MainActivity.privateclick = 0;
            return;
        }
        changeFragment(new VideoPrivateFragment());
    }

    public void changeFragment(Fragment fragment) {
//        MainActivity.ivSelectAll.setImageDrawable(getResources().getDrawable(R.drawable.action_select));
        FragmentTransaction beginTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        beginTransaction.replace(R.id.private_frame_layout, fragment);
        beginTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        beginTransaction.commit();
    }

    public void LayoutView() {
        int isLockStatus = preferenceClass.getInt(Common.gIsLockStatus, 0);
        if (isLockStatus == 0) {
            try {
                rl_tabLayout.setVisibility(View.GONE);
                ll_pinLockView.setVisibility(View.GONE);
                ll_HintLockView.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (isLockStatus == 1) {
            rl_tabLayout.setVisibility(View.GONE);
            ll_HintLockView.setVisibility(View.GONE);
            ll_pinLockView.setVisibility(View.VISIBLE);
        } else if (isLockStatus == 2) {
            ll_HintLockView.setVisibility(View.GONE);
            ll_pinLockView.setVisibility(View.GONE);
            rl_tabLayout.setVisibility(View.VISIBLE);
        }
    }

    private void initView(View view) {
        ll_ShakeAnimation = view.findViewById(R.id.layoutShakeAnimation);
        ll_HintLockView = view.findViewById(R.id.layoutHintLockView);
        ll_pinLockView = view.findViewById(R.id.pinLockViewLayout);
        rl_tabLayout = view.findViewById(R.id.tabLayout);
        tb_tabs = view.findViewById(R.id.tab);
        ll_pinLockView = view.findViewById(R.id.pinLockViewLayout);
        txt_confirm = view.findViewById(R.id.txt_confirm);
        txtHintTextPassword = view.findViewById(R.id.txtHintTextPassword);
        txt_set_sequ = view.findViewById(R.id.btnSetSecurity);
        iv_hide_pin_1 = view.findViewById(R.id.hidePin1);
        iv_hide_pin_2 = view.findViewById(R.id.hidePin2);
        iv_hide_pin_3 = view.findViewById(R.id.hidePin3);
        iv_hide_pin_4 = view.findViewById(R.id.hidePin4);
        iv_num_1 = view.findViewById(R.id.btnNumber1);
        iv_num_2 = view.findViewById(R.id.btnNumber2);
        iv_num_3 = view.findViewById(R.id.btnNumber3);
        iv_num_4 = view.findViewById(R.id.btnNumber4);
        iv_num_5 = view.findViewById(R.id.btnNumber5);
        iv_num_6 = view.findViewById(R.id.btnNumber6);
        iv_num_7 = view.findViewById(R.id.btnNumber7);
        iv_num_8 = view.findViewById(R.id.btnNumber8);
        iv_num_9 = view.findViewById(R.id.btnNumber9);
        iv_num_0 = view.findViewById(R.id.btnNumber0);
        iv_clear_num = view.findViewById(R.id.btnClearNumber);
        txt_forgotPin = view.findViewById(R.id.btnForgetPassword);
    }

    private void clickListener() {
        txt_set_sequ.setOnClickListener(view -> {
            AdShow.getInstance(requireActivity()).ShowAd(new HandleClick() {
                @Override
                public void Show(boolean adShow) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        requireContext().registerReceiver(broadcastReceiver, new IntentFilter("SecurityQuestionActivity"),RECEIVER_EXPORTED);
                        Common.IdentifyActivity = "SecurityQuestionActivity";
                        requireContext().sendBroadcast(new Intent(Common.IdentifyActivity));
                    }else {
                        requireContext().registerReceiver(broadcastReceiver, new IntentFilter("SecurityQuestionActivity"),RECEIVER_EXPORTED);
                        Common.IdentifyActivity = "SecurityQuestionActivity";
                        requireContext().sendBroadcast(new Intent(Common.IdentifyActivity));
                    }
                }
            }, AdUtils.ClickType.MAIN_CLICK);
        });

        iv_num_0.setOnClickListener(view -> {
            if (digit < 4) {
                CheckPassword("0");
            }
        });

        iv_num_1.setOnClickListener(view -> {
            if (digit < 4) {
                CheckPassword("1");
            }
        });

        iv_num_2.setOnClickListener(view -> {
            if (digit < 4) {
                CheckPassword(ExifInterface.GPS_MEASUREMENT_2D);
            }
        });

        iv_num_3.setOnClickListener(view -> {
            if (digit < 4) {
                CheckPassword(ExifInterface.GPS_MEASUREMENT_3D);
            }
        });

        iv_num_4.setOnClickListener(view -> {
            if (digit < 4) {
                CheckPassword("4");
            }
        });

        iv_num_5.setOnClickListener(view -> {
            if (digit < 4) {
                CheckPassword("5");
            }
        });

        iv_num_6.setOnClickListener(view -> {
            if (digit < 4) {
                CheckPassword("6");
            }
        });

        iv_num_7.setOnClickListener(view -> {
            if (digit < 4) {
                CheckPassword("7");
            }
        });

        iv_num_8.setOnClickListener(view -> {
            if (digit < 4) {
                CheckPassword("8");
            }
        });

        iv_num_9.setOnClickListener(view -> {
            if (digit < 4) {
                CheckPassword("9");
            }
        });

        iv_clear_num.setOnClickListener(view -> ClearPassword());

        txt_forgotPin.setOnClickListener(view -> ForgetPassword());
    }

    private void HiddenView() {
//        MainActivity.ivSelectAll.setVisibility(View.GONE);
//        MainActivity.ivAddAlbum.setVisibility(View.GONE);
//        MainActivity.ivUnLock.setVisibility(View.GONE);
//        MainActivity.ivShare.setVisibility(View.GONE);
//        MainActivity.ivDelete.setVisibility(View.GONE);
//        MainActivity.ivSort.setVisibility(View.GONE);
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
    }

    public void ClearPassword() {
        if (passwordArray.size() == 0) {
            txtHintTextPassword.setText("Enter Password.");
            passwordArray.clear();
        } else if (passwordArray.size() == 1) {
            iv_hide_pin_1.setBackground(getResources().getDrawable(R.drawable.ic_lock_ring_null));
            passwordArray.remove(passwordArray.get(0));
            digit--;
        } else if (passwordArray.size() == 2) {
            iv_hide_pin_2.setBackground(getResources().getDrawable(R.drawable.ic_lock_ring_null));
            passwordArray.remove(passwordArray.get(1));
            digit--;
        } else if (passwordArray.size() == 3) {
            iv_hide_pin_3.setBackground(getResources().getDrawable(R.drawable.ic_lock_ring_null));
            passwordArray.remove(passwordArray.get(2));
            digit--;
        } else if (passwordArray.size() == 4) {
            iv_hide_pin_4.setBackground(getResources().getDrawable(R.drawable.ic_lock_ring_null));
            passwordArray.remove(passwordArray.get(3));
            digit--;
        }
    }

    public void ForgetPassword() {
        requireActivity().registerReceiver(broadcastReceiver, new IntentFilter("ForgotPasswordActivity"));
        Common.IdentifyActivity = "ForgotPasswordActivity";
        requireActivity().sendBroadcast(new Intent(Common.IdentifyActivity));
    }

    public void CheckPassword(String str) {
        if (digit <= 4) {
            passwordArray.add(str);
            if (passwordArray.size() == 1) {
                digit++;
                iv_hide_pin_1.setBackground(getResources().getDrawable(R.drawable.ic_lock_ring_fill));
            } else if (passwordArray.size() == 2) {
                digit++;
                iv_hide_pin_2.setBackground(getResources().getDrawable(R.drawable.ic_lock_ring_fill));
            } else if (passwordArray.size() == 3) {
                digit++;
                iv_hide_pin_3.setBackground(getResources().getDrawable(R.drawable.ic_lock_ring_fill));
            } else if (passwordArray.size() == 4) {
                digit++;
                iv_hide_pin_4.setBackground(getResources().getDrawable(R.drawable.ic_lock_ring_fill));
                UnlockPassword();
            }
        }
    }

    private void UnlockPassword() {
        final ArrayList<String> listString = preferenceClass.getListString(Common.gCurrentPassword);
        if (preferenceClass.getInt(Common.gUseMasterPassword, 0) == 0) {
            txt_confirm.setOnClickListener(view -> {
                if (passwordArray.equals(listString)) {
                    preferenceClass.putInt(Common.gIsLockStatus, 2);
                    SetFragment();
                    LayoutView();
                    return;
                }
                Toast.makeText(getActivity(), "Invalid Password.", Toast.LENGTH_SHORT).show();
                digit = 0;
                txtHintTextPassword.setText("Enter Password.");
                ResetAll();
            });
        } else if (preferenceClass.getInt(Common.gUseMasterPassword, 0) == 1) {
            if (passwordArray.equals(masterPin.GetMSArrayA())) {
                EnterNewPassword();
            } else if (passwordArray.equals(masterPin.GetMSArrayB())) {
                EnterNewPassword();
            } else if (passwordArray.equals(masterPin.GetMSArrayC())) {
                EnterNewPassword();
            } else {
                ConfirmPassword();
            }
        }
    }

    private void ConfirmPassword() {
        if (enterPin == 1) {
            preferenceClass.putListString(Common.gTempPassword, this.passwordArray);
            passwordArray.clear();
            txtHintTextPassword.setText("Confirm Password.");
            iv_hide_pin_1.setBackground(getResources().getDrawable(R.drawable.ic_lock_ring_null));
            iv_hide_pin_2.setBackground(getResources().getDrawable(R.drawable.ic_lock_ring_null));
            iv_hide_pin_3.setBackground(getResources().getDrawable(R.drawable.ic_lock_ring_null));
            iv_hide_pin_4.setBackground(getResources().getDrawable(R.drawable.ic_lock_ring_null));
            enterPin = 2;
        } else if (enterPin != 2) {
            new ArrayList();
            if (passwordArray.equals(preferenceClass.getListString(Common.gCurrentPassword))) {
                preferenceClass.putInt(Common.gIsLockStatus, 2);
                LayoutView();
                return;
            }
            Toast.makeText(getActivity(), "Invalid Password.", Toast.LENGTH_SHORT).show();
            txtHintTextPassword.setText("Enter Password.");
            ResetAll();
        } else if (GetlistStingToString(preferenceClass.getListString(Common.gTempPassword)).equals(GetlistStingToString(passwordArray))) {
            preferenceClass.putListString(Common.gCurrentPassword, passwordArray);
            preferenceClass.putInt(Common.gIsLockStatus, 2);
            preferenceClass.putInt(Common.gUseMasterPassword, 0);
            LayoutView();
        } else {
            if (enterPin == 2) {
                enterPin = 1;
                Toast.makeText(getActivity(), "Invalid Confirm Password.", Toast.LENGTH_SHORT).show();
                txtHintTextPassword.setText("Enter New Password.");
            } else {
                enterPin = 0;
            }
            ResetAll();
        }
    }

    public void EnterNewPassword() {
        if (enterPin == 0) {
            originalPin = passwordArray;
            passwordArray.clear();
            txtHintTextPassword.setText("Enter New Password.");
            iv_hide_pin_1.setBackground(getResources().getDrawable(R.drawable.ic_lock_ring_null));
            iv_hide_pin_2.setBackground(getResources().getDrawable(R.drawable.ic_lock_ring_null));
            iv_hide_pin_3.setBackground(getResources().getDrawable(R.drawable.ic_lock_ring_null));
            iv_hide_pin_4.setBackground(getResources().getDrawable(R.drawable.ic_lock_ring_null));
            enterPin = 1;
        } else if (passwordArray.equals(masterPin.GetMSArrayA())) {
            Toast.makeText(getActivity(), "You can't use this pin, Please try another pin.", Toast.LENGTH_SHORT).show();
            UseDifferentPassword();
        } else if (passwordArray.equals(masterPin.GetMSArrayB())) {
            Toast.makeText(getActivity(), "You can't use this pin, Please try another pin.", Toast.LENGTH_SHORT).show();
            UseDifferentPassword();
        } else if (passwordArray.equals(masterPin.GetMSArrayC())) {
            Toast.makeText(getActivity(), "You can't use this pin, Please try another pin.", Toast.LENGTH_SHORT).show();
            UseDifferentPassword();
        }
    }

    private void UseDifferentPassword() {
        txtHintTextPassword.setText("Enter New Password.");
        ll_ShakeAnimation.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.shake_anim));
        ((Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE)).vibrate(800L);
        passwordArray.clear();
        iv_hide_pin_1.setBackground(getResources().getDrawable(R.drawable.ic_lock_ring_null));
        iv_hide_pin_2.setBackground(getResources().getDrawable(R.drawable.ic_lock_ring_null));
        iv_hide_pin_3.setBackground(getResources().getDrawable(R.drawable.ic_lock_ring_null));
        iv_hide_pin_4.setBackground(getResources().getDrawable(R.drawable.ic_lock_ring_null));
    }

    public void ResetAll() {
        ll_ShakeAnimation.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.shake_anim));
        ((Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE)).vibrate(800L);
        passwordArray.clear();
        iv_hide_pin_1.setBackground(getResources().getDrawable(R.drawable.ic_lock_ring_null));
        iv_hide_pin_2.setBackground(getResources().getDrawable(R.drawable.ic_lock_ring_null));
        iv_hide_pin_3.setBackground(getResources().getDrawable(R.drawable.ic_lock_ring_null));
        iv_hide_pin_4.setBackground(getResources().getDrawable(R.drawable.ic_lock_ring_null));
    }

    public String GetlistStingToString(ArrayList<String> arrayList) {
        StringBuilder sb = new StringBuilder();
        Iterator<String> it = arrayList.iterator();
        while (it.hasNext()) {
            sb.append(it.next());
        }
        return sb.toString();
    }

    @Override
    public void onResume() {
        super.onResume();
        LayoutView();
        if (!MainActivity.checkLockForFirstFile || ConstantArrayClass.firstTimeLockDataArray.size() == 0) {
            return;
        }
        progressTag = "FromLock";
        new LockFilesExecute(ConstantArrayClass.firstTimeLockDataArray).execute(new Void[0]);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }


    public class LockFilesExecute extends AsyncTask<Void, Void, Void> {
        ArrayList<DataFileModel> pathList;

        public LockFilesExecute(ArrayList<DataFileModel> arrayList) {
            this.pathList = new ArrayList<>();
            progressLock = new ProgressDialog(getActivity());
            this.pathList = arrayList;
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
            progressLock.setMessage(getString(R.string.please_wait_a_while));
            progressLock.setProgressStyle(0);
            progressLock.setIndeterminate(false);
            progressLock.setCancelable(false);
            progressLock.show();
        }

        @Override
        public Void doInBackground(Void... voidArr) {
            File file = null;
            Uri uri;
            ArrayList arrayList = new ArrayList();
            ArrayList<String> listString = preferenceClass.getListString(Common.gOldPath);
            new File(FolderPath.SDCARD_PATH_IMAGE).mkdirs();
            new File(FolderPath.SDCARD_PATH_VIDEO).mkdirs();
            new File(FolderPath.SDCARD_PATH_FOR11).mkdirs();
            new ArrayList();
            String str = null;
            int i = 0;
            while (i < pathList.size()) {
                File file2 = new File(pathList.get(i).path);
                String mediaType = pathList.get(i).getMediaType();
                if (mediaType.equals("1")) {
                    str = FolderPath.SDCARD_PATH_IMAGE;
                } else if (mediaType.equals(ExifInterface.GPS_MEASUREMENT_3D)) {
                    str = FolderPath.SDCARD_PATH_VIDEO;
                }
                String str2 = str;
                File file3 = new File(str2 + File.separator + file2.getName());
                String parent = file3.getParent();
                try {
                    FileUtils.CopyMoveFile(file2, file3);
                    long j = pathList.get(i).id;
                    String str3 = pathList.get(i).name;
                    String str4 = pathList.get(i).path;
                    listString.add(str4);
                    arrayList.add(new DataFileModel(j, str3, str4, file3.getPath(), parent, false));
                    if (pathList.size() - 1 == i) {
                        for (int i2 = 0; i2 < pathList.size(); i2++) {
                            new File(pathList.get(i2).path).delete();
                            ContentResolver contentResolver = getActivity().getContentResolver();
                            if (mediaType.equals("1")) {
                                uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                            } else {
                                uri = mediaType.equals(ExifInterface.GPS_MEASUREMENT_3D) ? MediaStore.Video.Media.EXTERNAL_CONTENT_URI : null;
                            }
                            try {
                                contentResolver.delete(uri, "_data='" + file.getPath() + "'", null);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        MomentFragment.momentAdapter.RemoveItem(pathList);

                        preferenceClass.putListString(Common.gOldPath, listString);
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                i++;
                str = str2;
            }
            return null;
        }

        @Override
        public void onPostExecute(Void r3) {
            super.onPostExecute(r3);
            progressLock.dismiss();
            MainActivity.checkLockForFirstFile = false;
            if (Common.checkPreview) {
                PreviewActivity.allImagesList.remove(PreviewActivity.viewPager.getCurrentItem());
                AppUtilsClass.RefreshPhotoVideo(getActivity());
            } else {
                AppUtilsClass.RefreshImageAlbum(getActivity());
                AppUtilsClass.RefreshVideoAlbum(getActivity());
            }
            Toast.makeText(getActivity(), getString(R.string.files_moved_to_vault_successfully), Toast.LENGTH_SHORT).show();
        }
    }
}
