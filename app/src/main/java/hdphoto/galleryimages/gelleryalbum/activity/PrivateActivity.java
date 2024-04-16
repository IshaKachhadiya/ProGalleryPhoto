package hdphoto.galleryimages.gelleryalbum.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.iten.tenoku.ad.AdShow;
import com.iten.tenoku.ad.HandleClick.HandleClick;
import com.iten.tenoku.utils.AdUtils;

import java.util.ArrayList;

import hdphoto.galleryimages.gelleryalbum.fragment.ImagePrivateFragment;
import hdphoto.galleryimages.gelleryalbum.fragment.PrivateMainFragment;
import hdphoto.galleryimages.gelleryalbum.fragment.VideoPrivateFragment;
import hdphoto.galleryimages.gelleryalbum.listeners.HideImageFolderDataSortingListener;
import hdphoto.galleryimages.gelleryalbum.listeners.HideVideoFolderDataSortingListener;
import hdphoto.galleryimages.gelleryalbum.sorting.SortingPrivateImageDialog;
import hdphoto.galleryimages.gelleryalbum.sorting.SortingVideoPrivateDialog;
import hdphoto.galleryimages.gelleryalbum.constant.Common;
import hdphoto.galleryimages.gelleryalbum.R;

public class PrivateActivity extends BseActivity {
    private HideImageFolderDataSortingListener hideImageFolderDataSortingListener;
    HideVideoFolderDataSortingListener hideVideoFolderDataSortingListener;
    ImageView iv_back;
    ImageView imgSortImage;
    String str;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_gm_private);

        if (preferenceClass.getInt(Common.gIsLockStatus, 0) != 0) {
            preferenceClass.putInt(Common.gIsLockStatus, 1);
        }
        changeFragment(new PrivateMainFragment());


        intiView();
        clickListener();

        sortingPrivateImageCallBack();
        sortingPrivateVideoCallBack();
    }

    private void intiView() {
        iv_back = findViewById(R.id.btnBack);
        imgSortImage = findViewById(R.id.btnSortImage);
    }

    private void clickListener() {
        iv_back.setOnClickListener(view -> onBackPressed());

        imgSortImage.setOnClickListener(view -> SortingData());
    }

    public void changeFragment(Fragment fragment) {
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        beginTransaction.replace(R.id.fram_container, fragment);
        beginTransaction.commit();
    }

    public void SortingData() {
        if (MainActivity.privateclick == 0) {
            int lockStatus  = preferenceClass.getInt(Common.gIsLockStatus, 0);
            if (lockStatus  == 0) {
                Toast.makeText(this, getString(R.string.please_set_security), Toast.LENGTH_SHORT).show();
            } else if (lockStatus  == 1) {
                Toast.makeText(this, "Please Enter Passcode!", Toast.LENGTH_SHORT).show();
            } else if (ImagePrivateFragment.privateImageList.size() >= 1) {
                new SortingPrivateImageDialog(this, ImagePrivateFragment.privateImageList, hideImageFolderDataSortingListener).ShowSortingDialogue();
            } else {
                Toast.makeText(this, "No Photos Found!", Toast.LENGTH_SHORT).show();
            }
        } else if (MainActivity.privateclick == 1) {
            int lockStatus = preferenceClass.getInt(Common.gIsLockStatus, 0);
            if (lockStatus == 0) {
                Toast.makeText(this, getString(R.string.please_set_security), Toast.LENGTH_SHORT).show();
            } else if (lockStatus == 1) {
                Toast.makeText(this, "Please Enter Passcode!", Toast.LENGTH_SHORT).show();
            } else if (VideoPrivateFragment.privateVideoList.size() >= 1) {
                new SortingVideoPrivateDialog(this, VideoPrivateFragment.privateVideoList, hideVideoFolderDataSortingListener).ShowSortingDialogue();
            } else {
                Toast.makeText(this, "No Videos Found!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sortingPrivateImageCallBack() {
        hideImageFolderDataSortingListener = new sortListenerHideImageFolderData();
    }


    public class sortListenerHideImageFolderData implements HideImageFolderDataSortingListener {
        sortListenerHideImageFolderData() {
        }

        @Override
        public void Sorting(ArrayList<Object> arrayList) {
            ImagePrivateFragment.privateImageAdapter.notifyDataSetChanged();
            Toast.makeText(PrivateActivity.this, "Sorting Successfully", Toast.LENGTH_SHORT).show();
        }
    }

    private void sortingPrivateVideoCallBack() {
        hideVideoFolderDataSortingListener = new sortCallBack();
    }


    public class sortCallBack implements HideVideoFolderDataSortingListener {
        sortCallBack() {
        }

        @Override
        public void Sorting(ArrayList<Object> arrayList) {
            VideoPrivateFragment.privateVideoAdapter.notifyDataSetChanged();
            Toast.makeText(PrivateActivity.this, "Sorting Successfully", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(i, i2, intent);
        }
    }

    @Override
    public void onBackPressed() {
        AdShow.getInstance(PrivateActivity.this).ShowAd(new HandleClick() {
            @Override
            public void Show(boolean adShow) {
                PrivateActivity.super.onBackPressed();
            }
        }, AdUtils.ClickType.BACK_CLICK);
    }
}
