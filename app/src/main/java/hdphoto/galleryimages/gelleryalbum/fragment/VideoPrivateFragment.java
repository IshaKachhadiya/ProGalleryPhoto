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
import androidx.documentfile.provider.DocumentFile;
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import hdphoto.galleryimages.gelleryalbum.R;
import hdphoto.galleryimages.gelleryalbum.activity.MainActivity;
import hdphoto.galleryimages.gelleryalbum.activity.PreviewActivity;
import hdphoto.galleryimages.gelleryalbum.adapter.VideoPrivateAdapter;
import hdphoto.galleryimages.gelleryalbum.appclass.GalleryAppClass;
import hdphoto.galleryimages.gelleryalbum.constant.Common;
import hdphoto.galleryimages.gelleryalbum.constant.MediaScanners;
import hdphoto.galleryimages.gelleryalbum.listeners.HideVideoFolderDataSortingListener;
import hdphoto.galleryimages.gelleryalbum.listeners.OnClickHideListener;
import hdphoto.galleryimages.gelleryalbum.listeners.RefreshAdapterListener;
import hdphoto.galleryimages.gelleryalbum.model.DataFileModel;
import hdphoto.galleryimages.gelleryalbum.model.FolderModel;
import hdphoto.galleryimages.gelleryalbum.sorting.LoginPreferenceUtilsData;
import hdphoto.galleryimages.gelleryalbum.sorting.SortingVideoPrivateDialog;
import hdphoto.galleryimages.gelleryalbum.utils.AppUtilsClass;
import hdphoto.galleryimages.gelleryalbum.utils.FileUtils;
import hdphoto.galleryimages.gelleryalbum.utils.FolderPath;
import hdphoto.galleryimages.gelleryalbum.utils.PrefClass;
import kotlin.jvm.internal.CharCompanionObject;

@SuppressWarnings("all")
public class VideoPrivateFragment extends BaseFragment {
    public static String FName = "";
    static String TAG = "VideoPrivateFragment";
    public static ActionMode mActionMode;
    public static VideoPrivateAdapter privateVideoAdapter;
    public static ArrayList<Object> privateVideoList;
    public static ArrayList<Object> sendPrivateVideoList;
    AccessFolder11 accessFolder11;
    public Activity activity;
    int bpos;
    Context context;
    BottomSheetDialog dialog;
    String folderPath;
    int i2;
    ImageView ivUnlokButton;
    Menu mMenu;
    PrefClass preferenceClass;
    GridView privateVideoGridView;
    int pv;
    RefreshAdapterListener refreshAdapterCallBack;
    RelativeLayout rl_NoDataLayout;
    HideVideoFolderDataSortingListener sortingHideVideoCallBack;
    int PRIVATE_ALBUM_RESULT = 444;
    int REFRESH_RESULT = 555;
    int countSelected = 0;
    boolean isSelectAll = false;
    boolean isSingleSelection = false;
    ActionMode.Callback actionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return true;
        }

        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            mMenu = menu;
            actionMode.getMenuInflater().inflate(R.menu.private_data_menu, menu);
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            if(menuItem.getItemId() == R.id.Delete){
                if (GetSelectedList().size() >= 1) {
                    DeleteDialog(GetSelectedList());
                } else {
                    Toast.makeText(context, "Select Video.", Toast.LENGTH_SHORT).show();
                }
            }else if(menuItem.getItemId() == R.id.Selector){
                int i = preferenceClass.getInt(Common.gIsLockStatus, 0);
                if (i == 0) {
                    Toast.makeText(context, getString(R.string.please_set_security), Toast.LENGTH_SHORT).show();
                } else if (i == 1) {
                    Toast.makeText(context, getString(R.string.please_enter_passcode), Toast.LENGTH_SHORT).show();
                } else if (isSelectAll) {
                    UnSelectAll();
                    mMenu.getItem(0).setIcon(ContextCompat.getDrawable(activity, R.drawable.action_select));
                    actionMode.finish();
                } else if (privateVideoList.size() >= 1) {
                    SelectAll();
                    mMenu.getItem(0).setIcon(ContextCompat.getDrawable(activity, R.drawable.action_unselect));
                } else {
                    Toast.makeText(context, getString(R.string.no_videos_found), Toast.LENGTH_SHORT).show();
                }
            }else if(menuItem.getItemId() == R.id.Share){
                if (GetSelectedList().size() >= 1) {
                    ShareVideos(GetSelectedList());
                } else {
                    Toast.makeText(context, "Select Video.", Toast.LENGTH_SHORT).show();
                }
                actionMode.finish();
            }else if(menuItem.getItemId() == R.id.Unlock){
                if (GetSelectedList().size() > 0) {
                    UnlockDialog();
                } else {
                    Toast.makeText(context, "Select Video.", Toast.LENGTH_SHORT).show();
                }
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            mActionMode = null;
            UnSelectAll();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        DataOrientation(configuration.orientation);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (privateVideoList.size() > 0) {
            rl_NoDataLayout.setVisibility(View.GONE);
            privateVideoGridView.setVisibility(View.VISIBLE);
        } else {
            privateVideoGridView.setVisibility(View.GONE);
            ivUnlokButton.setVisibility(View.GONE);
            rl_NoDataLayout.setVisibility(View.VISIBLE);
//            MainActivity.ivSelectAll.setVisibility(View.GONE);
//            MainActivity.iv_unlock.setVisibility(View.GONE);
        }
        DataOrientation(getResources().getConfiguration().orientation);
        if (privateVideoAdapter != null) {
            privateVideoAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_private_video, viewGroup, false);
        activity = getActivity();
        context = getContext();
        preferenceClass = new PrefClass(getActivity());
        privateVideoGridView = inflate.findViewById(R.id.grid_view_private_video_album);
        rl_NoDataLayout = inflate.findViewById(R.id.hintNoHideVideoLayout);
        ivUnlokButton = inflate.findViewById(R.id.btnVideoUnloackButton);
        privateVideoList = new ArrayList<>();
        try {
            privateVideoList = GetPrivateVideoFiles(getActivity());
            Collections.reverse(privateVideoList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SetAdapter();

//        MainActivity.iv_unlock.setOnClickListener(view -> {
//            if (GetSelectedList().size() > 0) {
//                UnlockDialog();
//            } else {
//                Toast.makeText(context, "Select Video.", Toast.LENGTH_SHORT).show();
//            }
//        });

//        MainActivity.ivSelectAll.setOnClickListener(view -> {
//            int i = preferenceClass.getInt(Common.gIsLockStatus, 0);
//            if (i == 0) {
//                Toast.makeText(context, "getString(R.string.please_set_security)!!", Toast.LENGTH_SHORT).show();
//            } else if (i == 1) {
//                Toast.makeText(context, getString(R.string.please_enter_passcode), Toast.LENGTH_SHORT).show();
//            } else if (isSelectAll) {
//                UnSelectAll();
//                MainActivity.ivSelectAll.setImageDrawable(getResources().getDrawable(R.drawable.action_select));
//            } else if (VideoPrivateFragment.privateVideoList.size() >= 1) {
//                SelectAll();
//                MainActivity.ivSelectAll.setImageDrawable(getResources().getDrawable(R.drawable.action_unselect));
//            } else {
//                Toast.makeText(context, "No Videos Found!!", Toast.LENGTH_SHORT).show();
//            }
//        });

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
            if (mActionMode != null) {
                mActionMode.finish();
            }
        }
        if (GetSelectedList().size() != 0) {
            mActionMode.setTitle(String.valueOf(GetSelectedList().size()));
        }
        privateVideoAdapter.notifyDataSetChanged();
    }

    public ArrayList<Object> GetSelectedList() {
        ArrayList<Object> arrayList = new ArrayList<>();
        int size = privateVideoList.size();
        for (int i = 0; i < size; i++) {
            if (((DataFileModel) privateVideoList.get(i)).isSelected) {
                arrayList.add((DataFileModel) privateVideoList.get(i));
            }
        }
        return arrayList;
    }

    public void UnSelectAll() {
        int size = privateVideoList.size();
        for (int i = 0; i < size; i++) {
            ((DataFileModel) privateVideoList.get(i)).isSelected = false;
        }
        if (GetSelectedList().size() != 0) {
            mActionMode.setTitle(String.valueOf(GetSelectedList().size()));
        }
        countSelected = 0;
        isSingleSelection = false;
        isSelectAll = false;
        privateVideoAdapter.notifyDataSetChanged();
    }

    public void SelectAll() {
        int size = privateVideoList.size();
        for (int i = 0; i < size; i++) {
            if (!((DataFileModel) privateVideoList.get(i)).isDirectory) {
                ((DataFileModel) privateVideoList.get(i)).isSelected = true;
            }
        }
        if (GetSelectedList().size() != 0) {
            mActionMode.setTitle(String.valueOf(GetSelectedList().size()));
        }
        countSelected = privateVideoList.size();
        isSingleSelection = true;
        isSelectAll = true;
        privateVideoAdapter.notifyDataSetChanged();
    }

    public void UnlockDialog() {
        final Dialog dialog = new Dialog(getActivity(), R.style.ThemeWithCorners1);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialog_unlock_video);
        dialog.setTitle("Unlock Video");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        ((TextView) dialog.findViewById(R.id.rl_yes)).setOnClickListener(view -> {
            isSelectAll = false;
            isSingleSelection = false;
            new UnLockVideoExecute(GetSelectedList()).execute(new Void[0]);
            dialog.dismiss();
        });

        ((TextView) dialog.findViewById(R.id.rl_no)).setOnClickListener(view -> {
            UnSelectAll();
            if (mActionMode != null) {
                mActionMode.finish();
            }
            dialog.dismiss();
        });

        dialog.show();
    }


    public class UnLockVideoExecute extends AsyncTask<Void, Void, Void> {
        ArrayList<Object> pathList;
        ProgressDialog progressLock;

        public UnLockVideoExecute(ArrayList<Object> arrayList) {
            this.pathList = new ArrayList<>();
            progressLock = new ProgressDialog(VideoPrivateFragment.this.getActivity());
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
            ArrayList<String> listString = preferenceClass.getListString(Common.gOldPath);
            for (int i = 0; i < pathList.size(); i++) {
                File file = new File(((DataFileModel) pathList.get(i)).path);
                String name = file.getName();
                File file2 = new File(FolderPath.SDCARD_PATH_VIDEO_LOCK_BACKUP);
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
            if (mActionMode != null) {
                mActionMode.finish();
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    AppUtilsClass.RefreshPhotoVideo((Activity) context);
                }
            }, 500L);
            if (privateVideoList.size() > 0) {
                rl_NoDataLayout.setVisibility(View.GONE);
                privateVideoGridView.setVisibility(View.VISIBLE);
            } else {
                rl_NoDataLayout.setVisibility(View.VISIBLE);
                privateVideoGridView.setVisibility(View.GONE);
            }
            RefreshAdapter();
            Toast.makeText(getActivity(), R.string.files_are_unlocked_successfully, Toast.LENGTH_SHORT).show();
        }
    }

    public void DeleteDialog(final ArrayList<Object> arrayList) {
        final Dialog dialog = new Dialog(getActivity(), R.style.ThemeWithCorners1);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dg_delete_file);
        dialog.setTitle("Delete Video");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        ((TextView) dialog.findViewById(R.id.txtTitle)).setText("Delete Video");
        ((TextView) dialog.findViewById(R.id.textDesc)).setText("Are you sure you want to delete video?");

        ((TextView) dialog.findViewById(R.id.rl_yes)).setOnClickListener(view -> {
            DeleteSelected(arrayList);
            dialog.dismiss();
        });

        ((TextView) dialog.findViewById(R.id.rl_no)).setOnClickListener(view -> {
            UnSelectAll();
            if (mActionMode != null) {
                mActionMode.finish();
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
            File file = new File(((DataFileModel) arrayList2.get(i2)).path);
            ContentResolver contentResolver = activity.getContentResolver();
            Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            contentResolver.delete(uri, "_data='" + file.getPath() + "'", null);
        }
        if (mActionMode != null) {
            mActionMode.finish();
        }
        FragmentTransaction beginTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        beginTransaction.replace(R.id.private_frame_layout, new VideoPrivateFragment());
        beginTransaction.commit();
    }

    private void SetAdapter() {
        new SortingVideoPrivateDialog(getActivity(), privateVideoList, sortingHideVideoCallBack).Sorting(LoginPreferenceUtilsData.GetStringData(getActivity(), SortingVideoPrivateDialog.SortingName), LoginPreferenceUtilsData.GetStringData(getActivity(), SortingVideoPrivateDialog.SortingType));
        sortingCallBack();
        getFirstHiddenAlbum();
        privateVideoAdapter = new VideoPrivateAdapter(getActivity(), getContext(), privateVideoList);
        privateVideoGridView.setAdapter((ListAdapter) privateVideoAdapter);
        DataOrientation(getResources().getConfiguration().orientation);

        privateVideoAdapter.setItemClickCallback(new OnClickHideListener<ArrayList<Object>, Integer, Boolean, View>() {
            @Override
            public void onClickMoreListener(ArrayList<Object> arrayList, Integer num, View view, Boolean bool) {
            }

            @Override
            public void onLongClickListener(ArrayList<Object> arrayList, Integer num, Boolean bool, View view) {
            }

            @Override
            public void onClickListener(ArrayList<Object> arrayList, Integer num, Boolean bool, View view) {
                sendPrivateVideoList = new ArrayList<>();
                sendPrivateVideoList = arrayList;
                bpos = num.intValue();
                if (!isSingleSelection) {
                    String str = ((DataFileModel) sendPrivateVideoList.get(num.intValue())).path;
                    str.substring(str.lastIndexOf("."));
                    if (str.endsWith(".mp4") || str.endsWith(".MP4") || str.endsWith(".3gp") || str.endsWith(".3GP") || str.endsWith(".mkv") || str.endsWith(".MKV")) {
                        Common.IdentifyActivity = "VideoPrivateFragment";
                        Intent intent = new Intent(activity, PreviewActivity.class);
                        intent.putExtra(Common.gImagePath, ((DataFileModel) sendPrivateVideoList.get(bpos)).path);
                        intent.putExtra(Common.gTotalimage, sendPrivateVideoList.size());
                        intent.putExtra(Common.gCurrenrtPosition, bpos);
                        intent.putExtra(Common.gArrayType, "private");
                        intent.putExtra(Common.gActivityname, "VideoPrivateFragment");
                        intent.putExtra(Common.gMediaType, ExifInterface.GPS_MEASUREMENT_3D);
                        activity.startActivity(intent);
                    }
                } else if (bool.booleanValue()) {
                    Toast.makeText(context, "isPrivateAlbum", Toast.LENGTH_SHORT).show();
                } else {
                    SelectSingleImage(VideoPrivateFragment.sendPrivateVideoList, num.intValue());
                }
            }
        });
    }

    public void getFirstHiddenAlbum() {
        Collections.sort(privateVideoList, (obj, obj2) -> Boolean.compare(((DataFileModel) obj2).isDirectory, ((DataFileModel) obj).isDirectory));
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
        if (privateVideoAdapter != null) {
            privateVideoAdapter.setLayoutParams(displayMetrics.widthPixels / (i == 1 ? this.i2 : MainActivity.dataDivider));
        }
        if (i != 1) {
            this.i2 = MainActivity.dataDivider;
        }
        privateVideoGridView.setNumColumns(this.i2);
    }

    public void RefreshAdapter() {
        getActivity().runOnUiThread(() -> {
            FragmentTransaction beginTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            beginTransaction.replace(R.id.private_frame_layout, new VideoPrivateFragment());
            beginTransaction.commit();
            privateVideoList = new ArrayList<>();
            try {
                privateVideoList = GetPrivateVideoFiles(getActivity());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            getFirstHiddenAlbum();
            privateVideoAdapter = new VideoPrivateAdapter(getActivity(), getContext(), privateVideoList);
            privateVideoGridView.setAdapter((ListAdapter) privateVideoAdapter);
            DataOrientation(activity.getResources().getConfiguration().orientation);
            SortingCallBack();
//            MainActivity.ivSelectAll.setImageDrawable(activity.getResources().getDrawable(R.drawable.action_select));
        });
    }

    private void sortingCallBack() {
        this.sortingHideVideoCallBack = new sortCallBack();
    }

    public void SortingCallBack() {
        this.refreshAdapterCallBack = new RefreshData();
    }


    public class RefreshData implements RefreshAdapterListener {
        RefreshData() {
        }

        @Override
        public void Refresh(Boolean bool) {
            privateVideoAdapter.notifyDataSetChanged();
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
        this.activity = activity;
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
    }


    public class sortCallBack implements HideVideoFolderDataSortingListener {
        sortCallBack() {
        }

        @Override
        public void Sorting(ArrayList<Object> arrayList) {
            privateVideoList = new ArrayList<>();
            privateVideoList.addAll(arrayList);
            getFirstHiddenAlbum();
            privateVideoAdapter = new VideoPrivateAdapter(getActivity(), getContext(), privateVideoList);
            privateVideoGridView.setAdapter((ListAdapter) privateVideoAdapter);
            DataOrientation(getResources().getConfiguration().orientation);
            privateVideoAdapter.setItemClickCallback(new OnClickHideListener<ArrayList<Object>, Integer, Boolean, View>() {
                @Override
                public void onClickMoreListener(ArrayList<Object> arrayList2, Integer num, View view, Boolean bool) {
                }

                @Override
                public void onClickListener(ArrayList<Object> arrayList2, Integer num, Boolean bool, View view) {
                    VideoPrivateFragment.sendPrivateVideoList = new ArrayList<>();
                    VideoPrivateFragment.sendPrivateVideoList = arrayList2;
                    bpos = num.intValue();
                    if (!isSingleSelection) {
                        String str = ((DataFileModel) sendPrivateVideoList.get(num.intValue())).path;
                        str.substring(str.lastIndexOf("."));
                        if (str.endsWith(".mp4") || str.endsWith(".MP4") || str.endsWith(".3gp") || str.endsWith(".3GP") || str.endsWith(".mkv") || str.endsWith(".MKV")) {
                            Common.IdentifyActivity = "VideoPrivateFragment";
                            Intent intent = new Intent(activity, PreviewActivity.class);
                            intent.putExtra(Common.gImagePath, ((DataFileModel) sendPrivateVideoList.get(bpos)).path);
                            intent.putExtra(Common.gTotalimage, sendPrivateVideoList.size());
                            intent.putExtra(Common.gCurrenrtPosition, bpos);
                            intent.putExtra(Common.gArrayType, "private");
                            intent.putExtra(Common.gActivityname, "VideoPrivateFragment");
                            intent.putExtra(Common.gMediaType, ExifInterface.GPS_MEASUREMENT_3D);
                            activity.startActivity(intent);
                        }
                    } else if (bool.booleanValue()) {
                        Toast.makeText(context, "isPrivateAlbum", Toast.LENGTH_SHORT).show();
                    } else {
                        SelectSingleImage(sendPrivateVideoList, num.intValue());
                    }
                }

                @Override
                public void onLongClickListener(ArrayList<Object> arrayList2, Integer num, Boolean bool, View view) {
//                    sendPrivateVideoList = new ArrayList<>();
//                    sendPrivateVideoList = arrayList2;
//                    if (bool.booleanValue()) {
//                        Toast.makeText(context, "isPrivateAlbum", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    folderPath = ((DataFileModel) sendPrivateVideoList.get(num.intValue())).folderPath;
//                    if (mActionMode != null) {
//                        mActionMode = null;
//                    }
//                    if (mActionMode == null) {
//                        mActionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(actionModeCallback);
//                    }
//                    SelectSingleImage(sendPrivateVideoList, num.intValue());
                }
            });
        }
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


    public class AccessFolder11 extends RecyclerView.Adapter<AccessFolder11.MyViewHolder> {
        boolean abool;
        Context context;
        ArrayList<FolderModel> folderList;
        ArrayList<Object> unlockArray = new ArrayList<>();

        public AccessFolder11(Context context, ArrayList<FolderModel> arrayList, boolean z) {
            this.folderList = new ArrayList<>();
            this.context = context;
            this.folderList = arrayList;
            this.abool = z;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_select_folder, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(MyViewHolder myViewHolder, final int i) {
            myViewHolder.fileName.setText(folderList.get(i).foldername);
            myViewHolder.fullPath.setText(folderList.get(i).folderPath);
            myViewHolder.fullPath.setSelected(true);
            myViewHolder.itemView.setOnClickListener(view -> {
                dialog.dismiss();
                String folderPath = folderList.get(i).getFolderPath();
                folderPath.substring(folderPath.lastIndexOf("/") + 1);
                FName = folderPath;
                unlockArray.clear();
                unlockArray = GetSelectedList();
                new Unlock11Execute(unlockArray).execute(new Void[0]);
            });
        }

        @Override
        public int getItemCount() {
            if (folderList != null) {
                return folderList.size();
            }
            return 0;
        }

        public void refreshData(ArrayList<FolderModel> arrayList) {
            this.folderList = arrayList;
            notifyDataSetChanged();
        }


        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView fileName;
            TextView fullPath;

            public MyViewHolder(View view) {
                super(view);
                fileName = (TextView) view.findViewById(R.id.fileName);
                fullPath = (TextView) view.findViewById(R.id.fullPath);
            }
        }
    }

    public boolean DownloadForUnlock(Context context, String str) {
        return copyFileInSavedDirForUnlock(context, str);
    }

    static boolean copyFileInSavedDirForUnlock(Context context, String str) {
        Uri fromFile = Uri.fromFile(new File(FName + File.separator + new File(str).getName()));
        try {
            InputStream openInputStream = context.getContentResolver().openInputStream(Uri.parse(str));
            OutputStream openOutputStream = context.getContentResolver().openOutputStream(fromFile, "w");
            byte[] bArr = new byte[1024];
            while (true) {
                int read = openInputStream.read(bArr);
                if (read > 0) {
                    openOutputStream.write(bArr, 0, read);
                } else {
                    openInputStream.close();
                    openOutputStream.flush();
                    openOutputStream.close();
                    Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                    intent.setData(fromFile);
                    context.sendBroadcast(intent);
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void TempDeleteVideo(ArrayList<Object> arrayList) {
        ArrayList arrayList2 = new ArrayList();
        Iterator<Object> it = arrayList.iterator();
        char c = CharCompanionObject.MAX_VALUE;
        while (it.hasNext()) {
            Object next = it.next();
            DocumentFile fromSingleUri = DocumentFile.fromSingleUri(requireActivity(), Uri.parse(((DataFileModel) next).path));
            if (fromSingleUri.exists() && fromSingleUri.delete()) {
                arrayList2.add(next);
                if (c == 0) {
                    return;
                }
                c = 1;
            } else {
                c = 0;
            }
        }
        arrayList.clear();
        Iterator it2 = arrayList2.iterator();
        while (it2.hasNext()) {
            privateVideoList.remove(it2.next());
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                privateVideoAdapter.notifyDataSetChanged();
            }
        });
    }


    public class Unlock11Execute extends AsyncTask<Void, Void, Void> {
        ArrayList<Object> arrayDataList;
        ArrayList arrayList = new ArrayList();
        char c = CharCompanionObject.MAX_VALUE;
        ProgressDialog progressDialog;

        public Unlock11Execute(ArrayList<Object> arrayList) {
            this.arrayDataList = new ArrayList<>();
            this.arrayDataList = arrayList;
            progressDialog = new ProgressDialog(VideoPrivateFragment.this.context);
        }

        @Override
        public void onPreExecute() {
            progressDialog.setMessage(getString(R.string.please_wait_a_while));
            progressDialog.setProgressStyle(0);
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        public Void doInBackground(Void... voidArr) {
            if (!arrayDataList.isEmpty()) {
                Iterator<Object> it = arrayDataList.iterator();
                while (it.hasNext()) {
                    Object next = it.next();
                    DataFileModel gMDataFileModel = (DataFileModel) next;
                    if (DocumentFile.fromSingleUri(requireActivity(), Uri.parse(gMDataFileModel.path)).exists()) {
                        if (DownloadForUnlock(getActivity(), gMDataFileModel.path)) {
                            this.arrayList.add(next);
                            if (this.c == 0) {
                                return null;
                            }
                            this.c = (char) 1;
                        } else {
                            continue;
                        }
                    } else {
                        this.c = (char) 0;
                    }
                }
                TempDeleteVideo(arrayDataList);
                try {
                    arrayDataList.clear();
                    Iterator it2 = arrayList.iterator();
                    while (it2.hasNext()) {
                        ArrayList<Object> arrayList = VideoPrivateFragment.privateVideoList;
                        ((DataFileModel) it2.next()).isSelected = false;
                        arrayList.contains(false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        public void onPostExecute(Void r3) {
            super.onPostExecute(r3);
            progressDialog.dismiss();
            isSingleSelection = false;
            UnSelectAll();
            if (mActionMode != null) {
                mActionMode.finish();
            }
            AppUtilsClass.RefreshVideoAlbum(getActivity());
            AppUtilsClass.RefreshMoment(getActivity());
            MomentFragment.momentAdapter.notifyDataSetChanged();
            privateVideoAdapter.notifyDataSetChanged();
            char c = this.c;
            if (c == 0) {
                Toast.makeText(context, "Couldn't Unlock Video", Toast.LENGTH_SHORT).show();
            } else if (c == 1) {
                Toast.makeText(context, getString(R.string.files_are_unlocked_successfully), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
