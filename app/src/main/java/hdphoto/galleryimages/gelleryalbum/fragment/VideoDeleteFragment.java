package hdphoto.galleryimages.gelleryalbum.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.core.content.ContextCompat;
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import hdphoto.galleryimages.gelleryalbum.R;
import hdphoto.galleryimages.gelleryalbum.adapter.VideoDeleteAdapterrrrr;
import hdphoto.galleryimages.gelleryalbum.constant.Common;
import hdphoto.galleryimages.gelleryalbum.constant.MediaScanners;
import hdphoto.galleryimages.gelleryalbum.activity.MainActivity;
import hdphoto.galleryimages.gelleryalbum.activity.PreviewActivity;
import hdphoto.galleryimages.gelleryalbum.listeners.HideVideoFolderDataSortingListener;
import hdphoto.galleryimages.gelleryalbum.listeners.OnClickHideListener;
import hdphoto.galleryimages.gelleryalbum.listeners.RefreshAdapterListener;
import hdphoto.galleryimages.gelleryalbum.model.DataFileModel;
import hdphoto.galleryimages.gelleryalbum.sorting.LoginPreferenceUtilsFolder;
import hdphoto.galleryimages.gelleryalbum.sorting.SortingVideoPrivateDialog;
import hdphoto.galleryimages.gelleryalbum.appclass.GalleryAppClass;
import hdphoto.galleryimages.gelleryalbum.utils.AppUtilsClass;
import hdphoto.galleryimages.gelleryalbum.utils.FileUtils;
import hdphoto.galleryimages.gelleryalbum.utils.FolderPath;
import hdphoto.galleryimages.gelleryalbum.utils.PrefClass;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;


public class VideoDeleteFragment extends BaseFragment {
    public static String TAG = "VideoDeleteFragment";
    public static ActionMode actionMode;
    public static VideoDeleteAdapterrrrr deleteVideoAdapter;
    public static ArrayList<Object> deleteVideoList;
    public static ArrayList<Object> sendDeleteVideoList;
    Activity activity;
    int bpos;
    Context context;
    GridView deleteVideoGridView;
    String folderPath;
    String fromBtn;
    int i2;
    ImageView imgUnlockButton;
    private Menu mMenu;
    PrefClass preferenceClass;
    int pv;
    RefreshAdapterListener refreshAdapterCallBack;
    RelativeLayout rl_delete_notice,rl_no_data_ly;
    HideVideoFolderDataSortingListener sortingHideVideoCallBack;
    int PRIVATE_ALBUM_RESULT = 444;
    int REFRESH_RESULT = 555;
    int countSelected = 0;
    boolean isSelectAll = false;
    boolean isSingleSelection = false;
    private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode actionMode2, Menu menu) {
            mMenu = menu;
            actionMode2.getMenuInflater().inflate(R.menu.delete_data_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode2, Menu menu) {
            if (Build.VERSION.SDK_INT < 21) {
                return true;
            }
            getActivity().getWindow().clearFlags(67108864);
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode2, MenuItem menuItem) {
            if(menuItem.getItemId() == R.id.Delete){
                if (GetSelectedList().size() >= 1) {
                    DeleteDialog(GetSelectedList());
                } else {
                    Toast.makeText(context, "Select Video.", Toast.LENGTH_SHORT).show();
                }
            }else if(menuItem.getItemId() == R.id.Selector){
                if (isSelectAll) {
                    UnSelectAll();
                    mMenu.getItem(0).setIcon(ContextCompat.getDrawable(activity, R.drawable.action_select));
                    actionMode2.finish();
                } else if (VideoDeleteFragment.deleteVideoList.size() >= 1) {
                    fromBtn = "BoxBtn";
                    SelectAll();
                    mMenu.getItem(0).setIcon(ContextCompat.getDrawable(activity, R.drawable.action_unselect));
                } else {
                    Toast.makeText(context, "No Videos Found!!", Toast.LENGTH_SHORT).show();
                }
            }else if(menuItem.getItemId() == R.id.Share){
                if (GetSelectedList().size() >= 1) {
                    ShareVideos(GetSelectedList());
                } else {
                    Toast.makeText(context, "Select Video.", Toast.LENGTH_SHORT).show();
                }
                actionMode2.finish();
            }else if(menuItem.getItemId() == R.id.Unlock){
                if (GetSelectedList().size() > 0) {
                    RestoreDialog();
                } else {
                    Toast.makeText(context, "Select Video.", Toast.LENGTH_SHORT).show();
                }
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode2) {
            actionMode = null;
            UnSelectAll();
            if (Build.VERSION.SDK_INT >= 21) {
                getActivity().getWindow().clearFlags(67108864);
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        if (deleteVideoList.size() > 0) {
            rl_no_data_ly.setVisibility(View.GONE);
            deleteVideoGridView.setVisibility(View.VISIBLE);
            rl_delete_notice.setVisibility(View.VISIBLE);
        } else {
            deleteVideoGridView.setVisibility(View.GONE);
            rl_delete_notice.setVisibility(View.GONE);
            imgUnlockButton.setVisibility(View.GONE);
            rl_no_data_ly.setVisibility(View.VISIBLE);
//            MainActivity.ivSelectAll.setVisibility(View.GONE);
//            MainActivity.iv_unlock.setVisibility(View.GONE);
        }
        DataOrientation(getResources().getConfiguration().orientation);
        if (deleteVideoAdapter != null) {
            deleteVideoAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        DataOrientation(configuration.orientation);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_delete_video, viewGroup, false);
        context = getContext();
        activity = getActivity();
        preferenceClass = new PrefClass(getActivity());
        deleteVideoGridView = inflate.findViewById(R.id.grid_view_private_video_album);
        rl_delete_notice = inflate.findViewById(R.id.rlDeleteNotice);
        rl_no_data_ly = inflate.findViewById(R.id.hintNoHideVideoLayout);
        imgUnlockButton = inflate.findViewById(R.id.btnVideoUnloackButton);
        deleteVideoList = new ArrayList<>();
        try {
            deleteVideoList = GetDeletedVideoFiles(getActivity());
            Collections.reverse(deleteVideoList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SetAdapter();
        SortingCallBack();
        return inflate;
    }

    public void SelectSingleImage(ArrayList<Object> arrayList, int i) {
        isSingleSelection = true;
        ((DataFileModel) arrayList.get(i)).isSelected = !((DataFileModel) arrayList.get(i)).isSelected;
        if (((DataFileModel) arrayList.get(i)).isSelected) {
            countSelected++;
        } else {
            countSelected--;
        }
        if (countSelected <= 0) {
            isSingleSelection = false;
            isSelectAll = false;
            if (actionMode != null) {
                actionMode.finish();
            }
        }
        if (actionMode != null && GetSelectedList().size() != 0) {
            actionMode.setTitle(String.valueOf(GetSelectedList().size()));
        }
        deleteVideoAdapter.notifyDataSetChanged();
    }

    public ArrayList<Object> GetSelectedList() {
        ArrayList<Object> arrayList = new ArrayList<>();
        int size = deleteVideoList.size();
        for (int i = 0; i < size; i++) {
            if (((DataFileModel) deleteVideoList.get(i)).isSelected) {
                arrayList.add((DataFileModel) deleteVideoList.get(i));
            }
        }
        return arrayList;
    }

    public void UnSelectAll() {
        int size = deleteVideoList.size();
        for (int i = 0; i < size; i++) {
            ((DataFileModel) deleteVideoList.get(i)).isSelected = false;
        }
        if (actionMode != null && GetSelectedList().size() != 0) {
            actionMode.setTitle(String.valueOf(GetSelectedList().size()));
        }
        countSelected = 0;
        isSingleSelection = false;
        isSelectAll = false;
        deleteVideoAdapter.notifyDataSetChanged();
    }

    public void SelectAll() {
        int size = deleteVideoList.size();
        for (int i = 0; i < size; i++) {
            if (!((DataFileModel) deleteVideoList.get(i)).isDirectory) {
                ((DataFileModel) deleteVideoList.get(i)).isSelected = true;
            }
        }
        if (actionMode != null && GetSelectedList().size() != 0) {
            actionMode.setTitle(String.valueOf(GetSelectedList().size()));
        }
        countSelected = deleteVideoList.size();
        isSingleSelection = true;
        isSelectAll = true;
        deleteVideoAdapter.notifyDataSetChanged();
    }

    public void RestoreDialog() {
        final Dialog dialog = new Dialog(getActivity(), R.style.ThemeWithCorners1);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialog_restore_video);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        ((TextView) dialog.findViewById(R.id.txtTitle)).setText(getString(R.string.restore_video));
        ((TextView) dialog.findViewById(R.id.textDesc)).setText(getString(R.string.are_you_sure_you_want_to_delete_video));
        ((TextView) dialog.findViewById(R.id.rl_yes)).setOnClickListener(view -> {
            isSelectAll = false;
            isSingleSelection = false;
            new RestoreVideoExecute(GetSelectedList()).execute(new Void[0]);
            dialog.dismiss();
        });
        ((TextView) dialog.findViewById(R.id.rl_no)).setOnClickListener(view -> {
            UnSelectAll();
            if (VideoDeleteFragment.actionMode != null) {
                VideoDeleteFragment.actionMode.finish();
            }
            dialog.dismiss();
        });
        dialog.show();
    }


    public class RestoreVideoExecute extends AsyncTask<Void, Void, Void> {
        ArrayList<Object> pathList;
        ProgressDialog progressLock;

        public RestoreVideoExecute(ArrayList<Object> arrayList) {
            this.pathList = new ArrayList<>();
            progressLock = new ProgressDialog(VideoDeleteFragment.this.getActivity());
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
            ArrayList arrayList = new ArrayList();
            new ArrayList();
            ArrayList<String> listString = preferenceClass.getListString(Common.gOldPath);
            for (int i = 0; i < pathList.size(); i++) {
                File file = new File(((DataFileModel) pathList.get(i)).path);
                String name = file.getName();
                File file2 = new File(FolderPath.SDCARD_PATH_VIDEO_TRASH_BACKUP);
                if (!file2.exists()) {
                    file2.mkdirs();
                }
                File file3 = new File(file2 + File.separator + name);

                String parent = file3.getParent();
                new MediaScanners(GalleryAppClass.getInstance(), file3);
                try {
                    FileUtils.CopyMoveFile(file, file3);
                    AppUtilsClass.insertUri(getActivity(), file3);
                    long j = ((DataFileModel) pathList.get(i)).id;
                    String str = ((DataFileModel) pathList.get(i)).name;
                    String str2 = ((DataFileModel) pathList.get(i)).path;
                    listString.add(str2);
                    arrayList.add(new DataFileModel(j, str, str2, file3.getPath(), parent, false));
                    if (pathList.size() - 1 == i) {
                        for (int i2 = 0; i2 < pathList.size(); i2++) {
                            File file4 = new File(((DataFileModel) pathList.get(i2)).path);
                            file4.delete();
                            ContentResolver contentResolver = getActivity().getContentResolver();
                            Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                            contentResolver.delete(uri, "_data='" + file4.getPath() + "'", null);
                        }
                        preferenceClass.putListString(Common.gOldPath, listString);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        public void onPostExecute(Void r5) {
            super.onPostExecute(r5);
            isSingleSelection = false;
            isSelectAll = false;

            progressLock.dismiss();
            if (actionMode != null) {
                actionMode.finish();
            }
            new Handler().postDelayed(() -> {
                AppUtilsClass.RefreshMoment((Activity) context);
                AppUtilsClass.RefreshVideoAlbum((Activity) context);
            }, 500L);
            if (deleteVideoList.size() > 0) {
                rl_no_data_ly.setVisibility(View.GONE);
                deleteVideoGridView.setVisibility(View.VISIBLE);
            } else {
                rl_no_data_ly.setVisibility(View.VISIBLE);
                deleteVideoGridView.setVisibility(View.GONE);
            }
            RefreshAdapter();
            Toast.makeText(getActivity(), getString(R.string.files_are_restored_successfully), Toast.LENGTH_SHORT).show();
        }
    }

    public void DeleteDialog(final ArrayList<Object> arrayList) {
        final Dialog dialog = new Dialog(getActivity(), R.style.ThemeWithCorners1);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dg_delete_video);
        dialog.setTitle("Delete Video");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        ((TextView) dialog.findViewById(R.id.txtTitle)).setText(getString(R.string.delete_video));
        ((TextView) dialog.findViewById(R.id.textDesc)).setText(getString(R.string.are_you_sure_you_want_to_delete_video));

        ((TextView) dialog.findViewById(R.id.rl_yes)).setOnClickListener(view -> {
            DeleteSelected(arrayList);
            dialog.dismiss();
        });

        ((TextView) dialog.findViewById(R.id.rl_no)).setOnClickListener(view -> {
            if (fromBtn != null) {
                if (fromBtn.equals("TopBtn")) {
                    UnSelectAll();
                } else {
                    fromBtn.equals("BoxBtn");
                }
            }
            if (actionMode != null) {
                actionMode.finish();
            }
            dialog.dismiss();
        });
        dialog.show();
    }

    public void DeleteSelected(ArrayList<Object> arrayList) {
        ArrayList arrayList2 = new ArrayList(arrayList);
        for (int i = 0; i < arrayList2.size(); i++) {
            try {
                new File(((DataFileModel) arrayList2.get(i)).path).delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (int i2 = 0; i2 < arrayList2.size(); i2++) {
            try {
                File file = new File(((DataFileModel) arrayList2.get(i2)).path);
                file.delete();
                ContentResolver contentResolver = activity.getContentResolver();
                Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                contentResolver.delete(uri, "_data='" + file.getPath() + "'", null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (actionMode != null) {
            actionMode.finish();
        }
        FragmentTransaction beginTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        beginTransaction.replace(R.id.delete_frame_layout, new VideoDeleteFragment());
        beginTransaction.commit();
    }

    private void SetAdapter() {
        new SortingVideoPrivateDialog(getActivity(), deleteVideoList, sortingHideVideoCallBack).Sorting(LoginPreferenceUtilsFolder.GetStringData(getActivity(), SortingVideoPrivateDialog.SortingName), LoginPreferenceUtilsFolder.GetStringData(getActivity(), SortingVideoPrivateDialog.SortingType));
        sortingCallBack();
        GetFirstHiddenAlbum();
        deleteVideoAdapter = new VideoDeleteAdapterrrrr(getActivity(), getContext(), deleteVideoList);
        deleteVideoGridView.setAdapter((ListAdapter) deleteVideoAdapter);
        DataOrientation(getResources().getConfiguration().orientation);
        deleteVideoAdapter.setItemClickCallback(new OnClickHideListener<ArrayList<Object>, Integer, Boolean, View>() {
            @Override
            public void onClickMoreListener(ArrayList<Object> arrayList, Integer num, View view, Boolean bool) {
            }

            @Override
            public void onClickListener(ArrayList<Object> arrayList, Integer num, Boolean bool, View view) {
                sendDeleteVideoList = new ArrayList<>();
                sendDeleteVideoList = arrayList;
                bpos = num.intValue();
                if (!isSingleSelection) {
                    String str = ((DataFileModel) sendDeleteVideoList.get(num.intValue())).path;
                    str.substring(str.lastIndexOf("."));
                    if (str.endsWith(".mp4") || str.endsWith(".MP4") || str.endsWith(".3gp") || str.endsWith(".3GP") || str.endsWith(".mkv") || str.endsWith(".MKV")) {
                        Common.IdentifyActivity = "VideoDeleteFragment";
                        Intent intent = new Intent(requireActivity(), PreviewActivity.class);
                        intent.putExtra(Common.gImagePath, ((DataFileModel) sendDeleteVideoList.get(bpos)).path);
                        intent.putExtra(Common.gTotalimage, sendDeleteVideoList.size());
                        intent.putExtra(Common.gCurrenrtPosition, bpos);
                        intent.putExtra(Common.gArrayType, "private");
                        intent.putExtra(Common.gActivityname, "VideoDeleteFragment");
                        intent.putExtra(Common.gMediaType, ExifInterface.GPS_MEASUREMENT_3D);
                        startActivity(intent);
                    }
                } else if (bool.booleanValue()) {
                    Toast.makeText(context, "isPrivateAlbum", Toast.LENGTH_SHORT).show();
                } else {
                    SelectSingleImage(sendDeleteVideoList, num.intValue());
                }
            }

            @Override
            public void onLongClickListener(ArrayList<Object> arrayList, Integer num, Boolean bool, View view) {
                sendDeleteVideoList = new ArrayList<>();
                sendDeleteVideoList = arrayList;
                if (bool.booleanValue()) {
                    Toast.makeText(context, "isPrivateAlbum", Toast.LENGTH_SHORT).show();
                    return;
                }
                folderPath = ((DataFileModel) sendDeleteVideoList.get(num.intValue())).folderPath;
                if (actionMode == null) {
                    actionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(actionModeCallback);
                }
                SelectSingleImage(sendDeleteVideoList, num.intValue());
            }
        });
    }

    public void GetFirstHiddenAlbum() {
        Collections.sort(deleteVideoList, (obj, obj2) -> Boolean.compare(((DataFileModel) obj2).isDirectory, ((DataFileModel) obj).isDirectory));
    }

    public void DataOrientation(int i) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
        if (MainActivity.preferenceDataUtils.getInt(Common.gDataColumns, 0) != 0) {
            int i2 = MainActivity.preferenceDataUtils.getInt(Common.gDataColumns, 0);
            this.pv = i2;
            if (i2 != 0) {
                this.i2 = i2;
            } else {
                this.i2 = 3;
                MainActivity.dataDivider = 9;
            }
        } else {
            this.i2 = 3;
            MainActivity.dataDivider = 9;
        }
        if (deleteVideoAdapter != null) {
            deleteVideoAdapter.setLayoutParams(displayMetrics.widthPixels / (i == 1 ? this.i2 : MainActivity.dataDivider));
        }
        if (i != 1) {
            this.i2 = MainActivity.dataDivider;
        }
        deleteVideoGridView.setNumColumns(this.i2);
    }

    public void RefreshAdapter() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                FragmentTransaction beginTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                beginTransaction.replace(R.id.delete_frame_layout, new VideoDeleteFragment());
                beginTransaction.commit();
                deleteVideoList = new ArrayList<>();
                try {
                    deleteVideoList = GetDeletedVideoFiles(getActivity());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                GetFirstHiddenAlbum();
                deleteVideoAdapter = new VideoDeleteAdapterrrrr(getActivity(), getContext(), deleteVideoList);
                deleteVideoGridView.setAdapter((ListAdapter) deleteVideoAdapter);
                DataOrientation(activity.getResources().getConfiguration().orientation);
                SortingCallBack();
//                MainActivity.ivSelectAll.setImageDrawable(activity.getResources().getDrawable(R.drawable.action_select));
            }
        });
    }

    private void sortingCallBack() {
        this.sortingHideVideoCallBack = new sortCallBack();
    }


    public class sortCallBack implements HideVideoFolderDataSortingListener {
        sortCallBack() {
        }

        @Override
        public void Sorting(ArrayList<Object> arrayList) {
            deleteVideoList = new ArrayList<>();
            deleteVideoList.addAll(arrayList);
            GetFirstHiddenAlbum();
            deleteVideoAdapter = new VideoDeleteAdapterrrrr(getActivity(), getContext(), deleteVideoList);
            deleteVideoGridView.setAdapter((ListAdapter) deleteVideoAdapter);
            DataOrientation(getResources().getConfiguration().orientation);
            deleteVideoAdapter.setItemClickCallback(new OnClickHideListener<ArrayList<Object>, Integer, Boolean, View>() {
                @Override
                public void onClickMoreListener(ArrayList<Object> arrayList2, Integer num, View view, Boolean bool) {
                }

                @Override
                public void onClickListener(ArrayList<Object> arrayList2, Integer num, Boolean bool, View view) {
                    sendDeleteVideoList = new ArrayList<>();
                    sendDeleteVideoList = arrayList2;
                    bpos = num.intValue();
                    if (!isSingleSelection) {
                        String str = ((DataFileModel) sendDeleteVideoList.get(num.intValue())).path;
                        str.substring(str.lastIndexOf("."));
                        if (str.endsWith(".mp4") || str.endsWith(".MP4") || str.endsWith(".3gp") || str.endsWith(".3GP") || str.endsWith(".mkv") || str.endsWith(".MKV")) {
                            Common.IdentifyActivity = "VideoDeleteFragment";
                            Intent intent = new Intent(requireActivity(), PreviewActivity.class);
                            intent.putExtra(Common.gImagePath, ((DataFileModel) sendDeleteVideoList.get(bpos)).path);
                            intent.putExtra(Common.gTotalimage, sendDeleteVideoList.size());
                            intent.putExtra(Common.gCurrenrtPosition, bpos);
                            intent.putExtra(Common.gArrayType, "private");
                            intent.putExtra(Common.gActivityname, "VideoDeleteFragment");
                            intent.putExtra(Common.gMediaType, ExifInterface.GPS_MEASUREMENT_3D);
                            startActivity(intent);
                        }
                    } else if (bool.booleanValue()) {
                        Toast.makeText(context, "isPrivateAlbum", Toast.LENGTH_SHORT).show();
                    } else {
                        SelectSingleImage(sendDeleteVideoList, num.intValue());
                    }
                }

                @Override
                public void onLongClickListener(ArrayList<Object> arrayList2, Integer num, Boolean bool, View view) {
                    sendDeleteVideoList = new ArrayList<>();
                    sendDeleteVideoList = arrayList2;
                    if (bool.booleanValue()) {
                        Toast.makeText(context, "isPrivateAlbum", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    folderPath = ((DataFileModel) sendDeleteVideoList.get(num.intValue())).folderPath;
                    if (actionMode == null) {
                        actionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(actionModeCallback);
                    }
                    SelectSingleImage(sendDeleteVideoList, num.intValue());
                }
            });
        }
    }

    public void SortingCallBack() {
        this.refreshAdapterCallBack = new RefreshData();
    }


    public class RefreshData implements RefreshAdapterListener {
        RefreshData() {
        }

        @Override
        public void Refresh(Boolean bool) {
            deleteVideoAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == -1 && i == PRIVATE_ALBUM_RESULT) {
            RefreshAdapter();
        } else if (i == REFRESH_RESULT) {
            RefreshAdapter();
        }
    }
}
