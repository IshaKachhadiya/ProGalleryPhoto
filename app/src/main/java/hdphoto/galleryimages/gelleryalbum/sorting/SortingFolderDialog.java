package hdphoto.galleryimages.gelleryalbum.sorting;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import hdphoto.galleryimages.gelleryalbum.R;
import hdphoto.galleryimages.gelleryalbum.fragment.ImageFragment;
import hdphoto.galleryimages.gelleryalbum.fragment.VideoFragment;
import hdphoto.galleryimages.gelleryalbum.listeners.AlbumSortingListener;
import hdphoto.galleryimages.gelleryalbum.model.FolderModel;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class SortingFolderDialog {
    public static String SortingName = "NameofSorting";
    public static String SortingType = "TypeofSorting";
    Activity activity;
    ArrayList<FolderModel> albumPathList;
    AlbumSortingListener albumSortingCallBack;
    Typeface typeface;
    String NameStr = "Name";
    String LastModifyDateStr = "Size";
    String AscendingStr = "Ascending";
    String DescendingStr = "Descending";


    public class DescendingSorting implements Comparator<FolderModel> {
        DescendingSorting() {
        }

        @Override 
        public int compare(FolderModel gMFolderModel, FolderModel gMFolderModel2) {
            return new File(gMFolderModel2.getFoldername()).getName().compareToIgnoreCase(new File(gMFolderModel.getFoldername()).getName());
        }
    }


    public class AscendingSorting implements Comparator<FolderModel> {
        AscendingSorting() {
        }

        @Override 
        public int compare(FolderModel gMFolderModel, FolderModel gMFolderModel2) {
            return new File(gMFolderModel.getFoldername()).getName().compareToIgnoreCase(new File(gMFolderModel2.getFoldername()).getName());
        }
    }


    public class AscendingDateSorting implements Comparator<FolderModel> {
        AscendingDateSorting() {
        }

        @Override 
        public int compare(FolderModel gMFolderModel, FolderModel gMFolderModel2) {
            return Long.compare(gMFolderModel.getPathlist().size(), gMFolderModel2.getPathlist().size());
        }
    }


    public class DescendingDateSorting implements Comparator<FolderModel> {
        DescendingDateSorting() {
        }

        @Override 
        public int compare(FolderModel gMFolderModel, FolderModel gMFolderModel2) {
            return Long.compare(gMFolderModel2.getPathlist().size(), gMFolderModel.getPathlist().size());
        }
    }

    public SortingFolderDialog(Activity activity, ArrayList<FolderModel> arrayList, AlbumSortingListener albumSortingListener) {
        this.albumPathList = new ArrayList<>();
        this.activity = activity;
        this.albumPathList = arrayList;
        this.albumSortingCallBack = albumSortingListener;
        BindPreference();
    }

    public void BindPreference() {
        if (LoginPreferenceUtilsFolder.GetStringData(activity, SortingName) == null) {
            LoginPreferenceUtilsFolder.SaveStringData(activity, SortingName, NameStr);
        }
        if (LoginPreferenceUtilsFolder.GetStringData(activity, SortingType) == null) {
            LoginPreferenceUtilsFolder.SaveStringData(activity, SortingType, AscendingStr);
        }
    }

    public void ShowSortingDialogue() {
        final Dialog dialog = new Dialog(activity, R.style.ThemeWithCorners1);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialogfolder_gm_sorting);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        final RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.myRadioGroup);
        RadioButton radioButton = (RadioButton) dialog.findViewById(R.id.lastmodified);
        RadioButton radioButton2 = (RadioButton) dialog.findViewById(R.id.name);
        if (LoginPreferenceUtilsFolder.GetStringData(activity, SortingName).equalsIgnoreCase(NameStr)) {
            radioButton2.setChecked(true);
        } else if (LoginPreferenceUtilsFolder.GetStringData(activity, SortingName).equalsIgnoreCase(LastModifyDateStr)) {
            radioButton.setChecked(true);
        }
        final RadioGroup radioGroup2 = (RadioGroup) dialog.findViewById(R.id.mySortGroup);
        RadioButton radioButton3 = (RadioButton) dialog.findViewById(R.id.ascending);
        RadioButton radioButton4 = (RadioButton) dialog.findViewById(R.id.descending);
        if (LoginPreferenceUtilsFolder.GetStringData(activity, SortingType).equalsIgnoreCase(AscendingStr)) {
            radioButton3.setChecked(true);
        } else if (LoginPreferenceUtilsFolder.GetStringData(activity, SortingType).equalsIgnoreCase(DescendingStr)) {
            radioButton4.setChecked(true);
        }
        ((TextView) dialog.findViewById(R.id.rl_yes)).setOnClickListener(view -> {
            dialog.dismiss();
            String charSequence = ((RadioButton) radioGroup.findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString();
            SortingFolderDialog gMSortingFolderDialog = SortingFolderDialog.this;
            gMSortingFolderDialog.Sorting(charSequence, ((RadioButton) radioGroup2.findViewById(radioGroup2.getCheckedRadioButtonId())).getText().toString());
        });
        ((TextView) dialog.findViewById(R.id.rl_no)).setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    public void Sorting(String str, String str2) {
        ArrayList<FolderModel> arrayList;
        ArrayList<FolderModel> arrayList2;
        ArrayList<FolderModel> arrayList3;
        ArrayList<FolderModel> arrayList4;
        try {
            if (str.equalsIgnoreCase(NameStr) && str2.equalsIgnoreCase(AscendingStr)) {
                if (albumPathList.size() > 0 && (arrayList4 = albumPathList) != null) {
                    try {
                        Collections.sort(arrayList4, new AscendingSorting());
                        albumSortingCallBack.Sorting(albumPathList);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                LoginPreferenceUtilsFolder.SaveStringData(activity, SortingName, str);
                LoginPreferenceUtilsFolder.SaveStringData(activity, SortingType, str2);
                if (ImageFragment.imageFolderAdapter != null) {
                    ImageFragment.imageFolderAdapter.notifyDataSetChanged();
                }
                if (VideoFragment.videoFolderAdapter == null) {
                    VideoFragment.videoFolderAdapter.notifyDataSetChanged();
                    return;
                }
                return;
            }
            if (str.equalsIgnoreCase(NameStr) && str2.equalsIgnoreCase(DescendingStr)) {
                if (albumPathList.size() > 0 && (arrayList3 = albumPathList) != null) {
                    Collections.sort(arrayList3, new DescendingSorting());
                    albumSortingCallBack.Sorting(albumPathList);
                }
                LoginPreferenceUtilsFolder.SaveStringData(activity, SortingName, str);
                LoginPreferenceUtilsFolder.SaveStringData(activity, SortingType, str2);
                ImageFragment.ImageFolderAdapter imageFolderAdapter = ImageFragment.imageFolderAdapter;
                VideoFragment.VideoFolderAdapter videoFolderAdapter = VideoFragment.videoFolderAdapter;
            }
            if (str.equalsIgnoreCase(LastModifyDateStr) && str2.equalsIgnoreCase(AscendingStr)) {
                if (albumPathList.size() > 0 && (arrayList2 = albumPathList) != null) {
                    Collections.sort(arrayList2, new AscendingDateSorting());
                    albumSortingCallBack.Sorting(albumPathList);
                }
                LoginPreferenceUtilsFolder.SaveStringData(activity, SortingName, str);
                LoginPreferenceUtilsFolder.SaveStringData(activity, SortingType, str2);
                ImageFragment.ImageFolderAdapter imageFolderAdapter2 = ImageFragment.imageFolderAdapter;
                VideoFragment.VideoFolderAdapter videoFolderAdapter2 = VideoFragment.videoFolderAdapter;
            }
            if (str.equalsIgnoreCase(LastModifyDateStr) && str2.equalsIgnoreCase(DescendingStr) && albumPathList.size() > 0 && (arrayList = albumPathList) != null) {
                Collections.sort(arrayList, new DescendingDateSorting());
                albumSortingCallBack.Sorting(albumPathList);
            }
            LoginPreferenceUtilsFolder.SaveStringData(activity, SortingName, str);
            LoginPreferenceUtilsFolder.SaveStringData(activity, SortingType, str2);
            ImageFragment.ImageFolderAdapter imageFolderAdapter3 = ImageFragment.imageFolderAdapter;
            VideoFragment.VideoFolderAdapter videoFolderAdapter3 = VideoFragment.videoFolderAdapter;
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
}
