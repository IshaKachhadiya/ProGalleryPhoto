package hdphoto.galleryimages.gelleryalbum.sorting;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import hdphoto.galleryimages.gelleryalbum.R;
import hdphoto.galleryimages.gelleryalbum.listeners.HideVideoFolderDataSortingListener;
import hdphoto.galleryimages.gelleryalbum.model.DataFileModel;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class SortingVideoPrivateDialog {
    public static String SortingName = "NameofSorting";
    public static String SortingType = "TypeofSorting";
    static final String TAG = "SortingGalleryHide";
    Activity activity;
    ArrayList<Object> imgPathlist;
    HideVideoFolderDataSortingListener sortingHideVideoCallBack;
    Typeface typeface;
    String NameStr = "Name";
    String LastModifyDateStr = "Last modifyDate";
    String AscendingStr = "Ascending";
    String DescendingStr = "Descending";


    public class DescendingSorting implements Comparator<Object> {
        DescendingSorting() {
        }

        @Override 
        public int compare(Object obj, Object obj2) {
            return new File(((DataFileModel) obj2).path).getName().compareToIgnoreCase(new File(((DataFileModel) obj).path).getName());
        }
    }


    public class AscendingSorting implements Comparator<Object> {
        AscendingSorting() {
        }

        @Override 
        public int compare(Object obj, Object obj2) {
            return new File(((DataFileModel) obj).path).getName().compareToIgnoreCase(new File(((DataFileModel) obj2).path).getName());
        }
    }


    public class AscendingDateSorting implements Comparator<Object> {
        AscendingDateSorting() {
        }

        @Override 
        public int compare(Object obj, Object obj2) {
            if (Build.VERSION.SDK_INT >= 30) {
                return Long.compare(Long.parseLong(((DataFileModel) obj).takenDate), Long.parseLong(((DataFileModel) obj2).takenDate));
            }
            return Long.compare(Long.parseLong(((DataFileModel) obj).modifiedDate), Long.parseLong(((DataFileModel) obj2).modifiedDate));
        }
    }


    public class DescendingDateSorting implements Comparator<Object> {
        DescendingDateSorting() {
        }

        @Override 
        public int compare(Object obj, Object obj2) {
            if (Build.VERSION.SDK_INT >= 30) {
                return Long.compare(Long.parseLong(((DataFileModel) obj2).takenDate), Long.parseLong(((DataFileModel) obj).takenDate));
            }
            return Long.compare(Long.parseLong(((DataFileModel) obj2).modifiedDate), Long.parseLong(((DataFileModel) obj).modifiedDate));
        }
    }

    public SortingVideoPrivateDialog(Activity activity, ArrayList<Object> arrayList, HideVideoFolderDataSortingListener hideVideoFolderDataSortingListener) {
        this.imgPathlist = new ArrayList<>();
        this.activity = activity;
        this.imgPathlist = arrayList;
        this.sortingHideVideoCallBack = hideVideoFolderDataSortingListener;
        BinfPreference();
    }

    public void BinfPreference() {
        if (LoginPreferenceUtilsData.GetStringData(activity, SortingName) == null) {
            LoginPreferenceUtilsData.SaveStringData(activity, SortingName, LastModifyDateStr);
        }
        if (LoginPreferenceUtilsData.GetStringData(activity, SortingType) == null) {
            LoginPreferenceUtilsData.SaveStringData(activity, SortingType, DescendingStr);
        }
    }

    public void ShowSortingDialogue() {
        final Dialog dialog = new Dialog(activity, R.style.ThemeWithCorners1);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialog_sorting);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        ((TextView) dialog.findViewById(R.id.txtTitle)).setTypeface(typeface);
        final RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.myRadioGroup);
        RadioButton radioButton = (RadioButton) dialog.findViewById(R.id.lastmodified);
        RadioButton radioButton2 = (RadioButton) dialog.findViewById(R.id.name);
        if (LoginPreferenceUtilsData.GetStringData(activity, SortingName).equalsIgnoreCase(NameStr)) {
            radioButton2.setChecked(true);
        } else if (LoginPreferenceUtilsData.GetStringData(activity, SortingName).equalsIgnoreCase(LastModifyDateStr)) {
            radioButton.setChecked(true);
        }
        final RadioGroup radioGroup2 = (RadioGroup) dialog.findViewById(R.id.mySortGroup);
        RadioButton radioButton3 = (RadioButton) dialog.findViewById(R.id.ascending);
        RadioButton radioButton4 = (RadioButton) dialog.findViewById(R.id.descending);
        if (LoginPreferenceUtilsData.GetStringData(activity, SortingType).equalsIgnoreCase(AscendingStr)) {
            radioButton3.setChecked(true);
        } else if (LoginPreferenceUtilsData.GetStringData(activity, SortingType).equalsIgnoreCase(DescendingStr)) {
            radioButton4.setChecked(true);
        }
        ((TextView) dialog.findViewById(R.id.rl_yes)).setOnClickListener(view -> {
            dialog.dismiss();
            String charSequence = ((RadioButton) radioGroup.findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString();
            Sorting(charSequence, ((RadioButton) radioGroup2.findViewById(radioGroup2.getCheckedRadioButtonId())).getText().toString());
        });
        ((TextView) dialog.findViewById(R.id.rl_no)).setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    public void Sorting(String str, String str2) {
        try {
            if (str.equals(LastModifyDateStr) && str2.equals(DescendingStr) && imgPathlist.size() > 0 && imgPathlist != null) {
                Collections.sort(imgPathlist, new DescendingDateSorting());
                sortingHideVideoCallBack.Sorting(imgPathlist);
            } else if (str.equals(LastModifyDateStr) && str2.equals(AscendingStr) && imgPathlist.size() > 0 && imgPathlist != null) {
                Collections.sort(imgPathlist, new AscendingDateSorting());
                sortingHideVideoCallBack.Sorting(imgPathlist);
            } else if (str.equals(NameStr) && str2.equals(DescendingStr) && imgPathlist.size() > 0 && imgPathlist != null) {
                Collections.sort(imgPathlist, new DescendingSorting());
                sortingHideVideoCallBack.Sorting(imgPathlist);
            } else if (str.equals(NameStr) && str2.equals(AscendingStr) && imgPathlist.size() > 0 && imgPathlist != null) {
                Collections.sort(imgPathlist, new AscendingSorting());
                sortingHideVideoCallBack.Sorting(imgPathlist);
            }
            LoginPreferenceUtilsData.SaveStringData(activity, SortingName, str);
            LoginPreferenceUtilsData.SaveStringData(activity, SortingType, str2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
