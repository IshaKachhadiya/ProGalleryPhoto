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
import hdphoto.galleryimages.gelleryalbum.listeners.SortingListener;
import hdphoto.galleryimages.gelleryalbum.model.DataFileModel;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class SortingDataDialog {
    public static String SortingName = "NameofSorting";
    public static String SortingType = "TypeofSorting";
    Activity activity;
    ArrayList<Object> imgPathList;
    SortingListener sortingCallBack;
    Typeface typeface;
    String NameStr = "Name";
    String LastModifyDateStr = "Last modifyDate";
    String AscendingStr = "Ascending";
    String DescendingStr = "Descending";


    public class AscendingSorting implements Comparator<Object> {
        AscendingSorting() {
        }

        @Override 
        public int compare(Object obj, Object obj2) {
            StringBuilder sb = new StringBuilder();
            sb.append(" - compare n asc  image - ");
            DataFileModel gMDataFileModel = (DataFileModel) obj2;
            sb.append(new File(gMDataFileModel.path).getName());
            sb.append(" // image2 - ");
            sb.append(new File(gMDataFileModel.path).getName());
            return new File(((DataFileModel) obj).path).getName().compareToIgnoreCase(new File(gMDataFileModel.path).getName());
        }
    }


    public class DescendingSorting implements Comparator<Object> {
        DescendingSorting() {
        }

        @Override 
        public int compare(Object obj, Object obj2) {
            return new File(((DataFileModel) obj2).path).getName().compareToIgnoreCase(new File(((DataFileModel) obj).path).getName());
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

    public SortingDataDialog(Activity activity, ArrayList<Object> arrayList, SortingListener sortingListener) {
        this.imgPathList = new ArrayList<>();
        this.activity = activity;
        this.imgPathList = arrayList;
        this.sortingCallBack = sortingListener;
        BindPreference();
    }

    public void BindPreference() {
        if (LoginPreferenceUtilsData.GetStringData(activity, SortingName) == null) {
            LoginPreferenceUtilsData.SaveStringData(activity, SortingName, LastModifyDateStr);
        }
        if (LoginPreferenceUtilsData.GetStringData(activity, SortingType) == null) {
            LoginPreferenceUtilsData.SaveStringData(activity, SortingType, DescendingStr);
        }
    }

    public void ShowSortingDialogue() {
        final Dialog dialog = new Dialog(this.activity);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialog_sorting);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
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
            RadioGroup radioGroup3 = radioGroup;
            String charSequence = ((RadioButton) radioGroup3.findViewById(radioGroup3.getCheckedRadioButtonId())).getText().toString();
            RadioGroup radioGroup4 = radioGroup2;
            String charSequence2 = ((RadioButton) radioGroup4.findViewById(radioGroup4.getCheckedRadioButtonId())).getText().toString();
            Sorting(charSequence, charSequence2);
        });
        ((TextView) dialog.findViewById(R.id.rl_no)).setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    public void Sorting(String str, String str2) {
        try {
            if (str.equals(LastModifyDateStr) && str2.equals(DescendingStr) && imgPathList.size() > 0 && imgPathList != null) {
                Collections.sort(imgPathList, new DescendingDateSorting());
                sortingCallBack.Sorting(imgPathList);
            } else if (str.equals(LastModifyDateStr) && str2.equals(AscendingStr) && imgPathList.size() > 0 && imgPathList != null) {
                Collections.sort(imgPathList, new AscendingDateSorting());
                sortingCallBack.Sorting(imgPathList);
            } else if (str.equals(NameStr) && str2.equals(DescendingStr) && imgPathList.size() > 0 && imgPathList != null) {
                Collections.sort(imgPathList, new DescendingSorting());
                sortingCallBack.Sorting(imgPathList);
            } else if (str.equals(NameStr) && str2.equals(AscendingStr) && imgPathList.size() > 0 && imgPathList != null) {
                Collections.sort(imgPathList, new AscendingSorting());
                sortingCallBack.Sorting(imgPathList);
            }
            LoginPreferenceUtilsData.SaveStringData(activity, SortingName, str);
            LoginPreferenceUtilsData.SaveStringData(activity, SortingType, str2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
