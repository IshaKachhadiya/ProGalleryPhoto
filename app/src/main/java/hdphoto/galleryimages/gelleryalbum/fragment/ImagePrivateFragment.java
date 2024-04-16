package hdphoto.galleryimages.gelleryalbum.fragment;

import android.annotation.SuppressLint;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;
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
import java.util.Comparator;
import java.util.Iterator;
import hdphoto.galleryimages.gelleryalbum.R;
import hdphoto.galleryimages.gelleryalbum.activity.MainActivity;
import hdphoto.galleryimages.gelleryalbum.activity.PreviewActivity;
import hdphoto.galleryimages.gelleryalbum.adapter.ImagePrivateAdapter;
import hdphoto.galleryimages.gelleryalbum.appclass.GalleryAppClass;
import hdphoto.galleryimages.gelleryalbum.constant.Common;
import hdphoto.galleryimages.gelleryalbum.constant.MediaScanners;
import hdphoto.galleryimages.gelleryalbum.listeners.HideImageFolderDataSortingListener;
import hdphoto.galleryimages.gelleryalbum.listeners.OnClickHideListener;
import hdphoto.galleryimages.gelleryalbum.listeners.RefreshAdapterListener;
import hdphoto.galleryimages.gelleryalbum.model.DataFileModel;
import hdphoto.galleryimages.gelleryalbum.model.FolderModel;
import hdphoto.galleryimages.gelleryalbum.sorting.LoginPreferenceUtilsData;
import hdphoto.galleryimages.gelleryalbum.sorting.SortingPrivateImageDialog;
import hdphoto.galleryimages.gelleryalbum.utils.AppUtilsClass;
import hdphoto.galleryimages.gelleryalbum.utils.FileUtils;
import hdphoto.galleryimages.gelleryalbum.utils.FolderPath;
import hdphoto.galleryimages.gelleryalbum.utils.PrefClass;
import kotlin.jvm.internal.CharCompanionObject;

@SuppressWarnings("all")
public class ImagePrivateFragment extends BaseFragment implements View.OnClickListener {
    public static String FName = "";
    static String TAG = "ImagePrivateFragment";
    public static ActionMode mActionMode;
    public static ImagePrivateAdapter privateImageAdapter;
    public static ArrayList<Object> privateImageList;
    public static int privateimage;
    public static ArrayList<Object> sendPrivateImageList;
    AccessFolder11 accessFolder11;
    Activity activity;
    int bpos;
    Context context;
    BottomSheetDialog dialog;
    String folderPath;
    HideImageFolderDataSortingListener hideImageFolderDataSortingListener;
    int i2;
    ImageView imgUnlokButton;
    LinearLayout ll_bottomControl;
    private Menu mMenu;
    PrefClass preferenceClass;
    GridView privateImageGridView;
    int pv;
    RefreshAdapterListener refreshAdapterListener;
    RelativeLayout rl_NoDataLayout;
    int GRANT_PERMISSION = 333;
    int PRIVATE_ALBUM_RESULT = 444;
    int REFRESH_RESULT = 555;
    int REQUEST_ID_SET_AS_WALLPAPER = 111;
    int countSelected = 0;
    boolean isSelectAll = false;
    boolean isSingleSelection = false;
    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
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
                    DeleteDialog();
                } else {
                    Toast.makeText(context, getString(R.string.select_image), Toast.LENGTH_SHORT).show();
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
                } else if (ImagePrivateFragment.privateImageList.size() >= 1) {
                    SelectAll();
                    mMenu.getItem(0).setIcon(ContextCompat.getDrawable(activity, R.drawable.action_unselect));
                } else {
                    Toast.makeText(context, getString(R.string.no_photos_found), Toast.LENGTH_SHORT).show();
                }
            }else if(menuItem.getItemId() == R.id.Share){
                if (GetSelectedList().size() >= 1) {
                    ShareImages(GetSelectedList());
                } else {
                    Toast.makeText(context, getString(R.string.select_image), Toast.LENGTH_SHORT).show();
                }
                actionMode.finish();
            }else if(menuItem.getItemId() == R.id.Unlock){
                if (GetSelectedList().size() > 0) {
                    UnlockDialog();
                } else {
                    Toast.makeText(context, getString(R.string.select_image), Toast.LENGTH_SHORT).show();
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
    private String folder_path = "";

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (privateImageList.size() > 0) {
            rl_NoDataLayout.setVisibility(View.GONE);
            privateImageGridView.setVisibility(View.VISIBLE);
        } else {
            rl_NoDataLayout.setVisibility(View.VISIBLE);
            privateImageGridView.setVisibility(View.GONE);
        }
        DataOrientation(getResources().getConfiguration().orientation);
        if (privateImageAdapter != null) {
            privateImageAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        DataOrientation(configuration.orientation);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_image_private, viewGroup, false);

        preferenceClass = new PrefClass(getActivity());

        initView(inflate);

        privateImageList = new ArrayList<>();
        try {
            privateImageList = GetPrivateImageFiles(getActivity());
            Collections.reverse(privateImageList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SetAdapter();

        RefreshPrivateAdapter();
        return inflate;
    }

    private void initView(View view) {
        privateImageGridView = view.findViewById(R.id.grid_view_private_photo_album);
        rl_NoDataLayout = view.findViewById(R.id.hintNoHideImageLayout);
        ll_bottomControl = view.findViewById(R.id.bottomControlLayout);
        imgUnlokButton = view.findViewById(R.id.btnImageUnloackButton);
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
            HiddenView();
            if (mActionMode != null) {
                mActionMode.finish();
            }
        }
        if (GetSelectedList().size() != 0) {
            mActionMode.setTitle(String.valueOf(GetSelectedList().size()));
        }
        privateImageAdapter.notifyDataSetChanged();
    }

    public ArrayList<DataFileModel> GetSelectedList() {
        ArrayList<DataFileModel> arrayList = new ArrayList<>();
        int size = privateImageList.size();
        for (int i = 0; i < size; i++) {
            if (((DataFileModel) privateImageList.get(i)).isSelected) {
                arrayList.add((DataFileModel) privateImageList.get(i));
            }
        }
        return arrayList;
    }

    public void UnSelectAll() {
        int size = privateImageList.size();
        for (int i = 0; i < size; i++) {
            ((DataFileModel) privateImageList.get(i)).isSelected = false;
        }
        if (GetSelectedList().size() != 0) {
            mActionMode.setTitle(String.valueOf(GetSelectedList().size()));
        }
        countSelected = 0;
        isSingleSelection = false;
        isSelectAll = false;
        HiddenView();
        privateImageAdapter.notifyDataSetChanged();
    }

    public void SelectAll() {
        int size = privateImageList.size();
        for (int i = 0; i < size; i++) {
            if (!((DataFileModel) privateImageList.get(i)).isDirectory) {
                ((DataFileModel) privateImageList.get(i)).isSelected = true;
            }
        }
        if (GetSelectedList().size() != 0) {
            mActionMode.setTitle(String.valueOf(GetSelectedList().size()));
        }
        countSelected = privateImageList.size();
        isSingleSelection = true;
        isSelectAll = true;
        VisibleView();
        privateImageAdapter.notifyDataSetChanged();
    }

    public void UnlockDialog() {
        final Dialog dialog = new Dialog(getActivity(), R.style.ThemeWithCorners1);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialog_unlock_image);
        dialog.setTitle("Unlock Image");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        ((TextView) dialog.findViewById(R.id.rl_yes)).setOnClickListener(view -> {
            isSelectAll = false;
            isSingleSelection = false;
            new UnLockImageExecute(GetSelectedList()).execute(new Void[0]);
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


    public class UnLockImageExecute extends AsyncTask<Void, Void, Void> {
        ArrayList<DataFileModel> pathList;
        ProgressDialog progressLock;

        public UnLockImageExecute(ArrayList<DataFileModel> arrayList) {
            this.pathList = new ArrayList<>();
            progressLock = new ProgressDialog(ImagePrivateFragment.this.getActivity());
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
                File file = new File(pathList.get(i).path);
                String name = file.getName();
                File file2 = new File(FolderPath.SDCARD_PATH_IMAGE_LOCK_BACKUP);
                if (!file2.exists()) {
                    file2.mkdirs();
                }
                File file3 = new File(file2 + File.separator + name);
                if (file3.exists()) {
                    Log.e(ImagePrivateFragment.TAG, "doInBackground: ");
                }
                String parent = file3.getParent();
                new MediaScanners(GalleryAppClass.getInstance(), file3);
                try {
                    FileUtils.CopyMoveFile(file, file3);
                    AppUtilsClass.insertUri(getActivity(), file3);
                    long j = pathList.get(i).id;
                    String str = pathList.get(i).name;
                    String str2 = pathList.get(i).path;
                    listString.add(str2);
                    arrayList.add(new DataFileModel(j, str, str2, file3.getPath(), parent, false));
                    if (pathList.size() - 1 == i) {
                        for (int i2 = 0; i2 < pathList.size(); i2++) {
                            File file4 = new File(pathList.get(i2).path);
                            file4.delete();
                            ContentResolver contentResolver = getActivity().getContentResolver();
                            Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
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
            privateimage = 1;
            isSingleSelection = false;
            isSelectAll = false;
            progressLock.dismiss();
            if (mActionMode != null) {
                mActionMode.finish();
            }
            new Handler().postDelayed(() -> AppUtilsClass.RefreshPhotoVideo(getActivity()), 500L);

            if (privateImageList.size() > 0) {
                rl_NoDataLayout.setVisibility(View.GONE);
                privateImageGridView.setVisibility(View.VISIBLE);
            } else {
                rl_NoDataLayout.setVisibility(View.VISIBLE);
                privateImageGridView.setVisibility(View.GONE);
            }
            RefreshAdapter();
            HiddenView();
            Toast.makeText(getActivity(), getString(R.string.files_are_unlocked_successfully), Toast.LENGTH_SHORT).show();
        }
    }

    public void DeleteDialog() {
        final Dialog dialog = new Dialog(getActivity(), R.style.ThemeWithCorners1);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dg_delete_file);
        dialog.setTitle("Delete Image");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        ((TextView) dialog.findViewById(R.id.rl_yes)).setOnClickListener(view -> {
            DeleteSelectedItem();
            HiddenView();
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

    public void DeleteSelectedItem() {
        ArrayList arrayList = new ArrayList(GetSelectedList());
        for (int i = 0; i < arrayList.size(); i++) {
            try {
                new File(((DataFileModel) arrayList.get(i)).path).delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            File file = new File(((DataFileModel) arrayList.get(i2)).path);
            ContentResolver contentResolver = activity.getContentResolver();
            Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            contentResolver.delete(uri, "_data='" + file.getPath() + "'", null);
        }
        if (mActionMode != null) {
            mActionMode.finish();
        }
        FragmentTransaction beginTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        beginTransaction.replace(R.id.private_frame_layout, new ImagePrivateFragment());
        beginTransaction.commit();
    }

    public void SetAdapter() {
        new SortingPrivateImageDialog(getActivity(), privateImageList, hideImageFolderDataSortingListener).Sorting(LoginPreferenceUtilsData.GetStringData(getActivity(), SortingPrivateImageDialog.SortingName), LoginPreferenceUtilsData.GetStringData(getActivity(), SortingPrivateImageDialog.SortingType));
        SortingCallBack();
        getFirstHiddenAlbum();
        privateImageAdapter = new ImagePrivateAdapter(getActivity(), getContext(), privateImageList);
        privateImageGridView.setAdapter((ListAdapter) privateImageAdapter);
        DataOrientation(getResources().getConfiguration().orientation);
        privateImageAdapter.setItemClickCallback(new OnClickHideListener<ArrayList<Object>, Integer, Boolean, View>() {
            @Override
            public void onClickMoreListener(ArrayList<Object> arrayList, Integer num, View view, Boolean bool) {
            }

            @Override
            public void onLongClickListener(ArrayList<Object> arrayList, Integer num, Boolean bool, View view) {
            }

            @Override
            public void onClickListener(ArrayList<Object> arrayList, Integer num, Boolean bool, View view) {
                sendPrivateImageList = new ArrayList<>();
                sendPrivateImageList = arrayList;
                bpos = num.intValue();
                if (!isSingleSelection) {
                    String str = ((DataFileModel) sendPrivateImageList.get(num.intValue())).path;
                    str.substring(str.lastIndexOf("."));
                    if (str.endsWith(".jpg") || str.endsWith(".JPG") || str.endsWith(".jpeg") || str.endsWith(".JPEG") || str.endsWith(".png") || str.endsWith(".PNG") || str.endsWith(".gif") || str.endsWith(".GIF")) {
                        Common.IdentifyActivity = "ImagePrivateFragment";
                        Intent intent = new Intent(activity, PreviewActivity.class);
                        intent.putExtra(Common.gImagePath, ((DataFileModel) sendPrivateImageList.get(bpos)).path);
                        intent.putExtra(Common.gTotalimage, sendPrivateImageList.size());
                        intent.putExtra(Common.gCurrenrtPosition, bpos);
                        intent.putExtra(Common.gArrayType, "private");
                        intent.putExtra(Common.gActivityname, "ImagePrivateFragment");
                        intent.putExtra(Common.gMediaType, ((DataFileModel) sendPrivateImageList.get(bpos)).mediaType);
                        activity.startActivity(intent);
                    }
                } else if (bool.booleanValue()) {
                    Toast.makeText(context, "isPrivateAlbum", Toast.LENGTH_SHORT).show();
                } else {
                    SelectSingleImage(ImagePrivateFragment.sendPrivateImageList, num.intValue());
                }
            }
        });
    }

    public void getFirstHiddenAlbum() {
        Collections.sort(privateImageList, new Comparator<Object>() {
            @Override 
            public int compare(Object obj, Object obj2) {
                return Boolean.compare(((DataFileModel) obj2).isDirectory, ((DataFileModel) obj).isDirectory);
            }
        });
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
        if (privateImageAdapter != null) {
            privateImageAdapter.setLayoutParams(displayMetrics.widthPixels / (i == 1 ? this.i2 : MainActivity.dataDivider));
        }
        if (i != 1) {
            this.i2 = MainActivity.dataDivider;
        }
        privateImageGridView.setNumColumns(this.i2);
    }

    public void RefreshAdapter() {
        getActivity().runOnUiThread(() -> {
            FragmentTransaction beginTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            beginTransaction.replace(R.id.private_frame_layout, new ImagePrivateFragment());
            beginTransaction.commit();
            try {
                privateImageList = new ArrayList<>();
                privateImageList = GetPrivateImageFiles(getActivity());
            } catch (Exception e) {
                e.printStackTrace();
            }
            getFirstHiddenAlbum();
            privateImageAdapter = new ImagePrivateAdapter(getActivity(), getContext(), privateImageList);
            privateImageGridView.setAdapter((ListAdapter) privateImageAdapter);
            DataOrientation(activity.getResources().getConfiguration().orientation);
//            MainActivity.ivSelectAll.setImageDrawable(activity.getResources().getDrawable(R.drawable.action_select));
        });
    }

    @Override 
    public void onClick(View view) {
        int id = view.getId();
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == -1 && i == GRANT_PERMISSION) {
            if (Build.VERSION.SDK_INT >= 19) {
                activity.getContentResolver().takePersistableUriPermission(intent.getData(), intent.getFlags());
            }
            if (GetSelectedList().size() > 0) {
                UnlockDialog();
            } else {
                Toast.makeText(context, getString(R.string.select_image), Toast.LENGTH_SHORT).show();
            }
        } else if (i2 == -1 && i == PRIVATE_ALBUM_RESULT) {
            RefreshAdapter();
        } else if (i == REFRESH_RESULT) {
            RefreshAdapter();
        }
    }

    public void VisibleView() {
        ll_bottomControl.setVisibility(View.GONE);
    }

    public void HiddenView() {
        ll_bottomControl.setVisibility(View.GONE);
    }

    private void RefreshPrivateAdapter() {
        refreshAdapterListener = new RefreshDataExecute();
    }


    public class RefreshDataExecute implements RefreshAdapterListener {
        RefreshDataExecute() {
        }

        @Override
        public void Refresh(Boolean bool) {
            privateImageAdapter.notifyDataSetChanged();
        }
    }

    private void SortingCallBack() {
        hideImageFolderDataSortingListener = new sortListenerHideImageFolderData();
    }


    public class sortListenerHideImageFolderData implements HideImageFolderDataSortingListener {
        sortListenerHideImageFolderData() {
        }

        @Override
        public void Sorting(ArrayList<Object> arrayList) {
            privateImageList = new ArrayList<>();
            privateImageList.addAll(arrayList);
            getFirstHiddenAlbum();
            privateImageAdapter = new ImagePrivateAdapter(getActivity(), getContext(), privateImageList);
            privateImageGridView.setAdapter((ListAdapter) privateImageAdapter);
            DataOrientation(getResources().getConfiguration().orientation);
            privateImageAdapter.setItemClickCallback(new OnClickHideListener<ArrayList<Object>, Integer, Boolean, View>() {
                @Override
                public void onClickMoreListener(ArrayList<Object> arrayList2, Integer num, View view, Boolean bool) {
                }

                @Override
                public void onClickListener(ArrayList<Object> arrayList2, Integer num, Boolean bool, View view) {
                    sendPrivateImageList = new ArrayList<>();
                    sendPrivateImageList = arrayList2;
                    bpos = num.intValue();
                    if (!isSingleSelection) {
                        String str = ((DataFileModel) sendPrivateImageList.get(num.intValue())).path;
                        str.substring(str.lastIndexOf("."));
                        if (str.endsWith(".jpg") || str.endsWith(".JPG") || str.endsWith(".jpeg") || str.endsWith(".JPEG") || str.endsWith(".png") || str.endsWith(".PNG") || str.endsWith(".gif") || str.endsWith(".GIF")) {
                            Common.IdentifyActivity = "ImagePrivateFragment";
                            Intent intent = new Intent(activity, PreviewActivity.class);
                            intent.putExtra(Common.gImagePath, ((DataFileModel) sendPrivateImageList.get(bpos)).path);
                            intent.putExtra(Common.gTotalimage, sendPrivateImageList.size());
                            intent.putExtra(Common.gCurrenrtPosition, bpos);
                            intent.putExtra(Common.gArrayType, "private");
                            intent.putExtra(Common.gActivityname, "ImagePrivateFragment");
                            intent.putExtra(Common.gMediaType, ((DataFileModel) sendPrivateImageList.get(bpos)).mediaType);
                            activity.startActivity(intent);
                        }
                    } else if (bool.booleanValue()) {
                        Toast.makeText(context, "isPrivateAlbum", Toast.LENGTH_SHORT).show();
                    } else {
                        SelectSingleImage(sendPrivateImageList, num.intValue());
                    }
                }

                @Override
                public void onLongClickListener(ArrayList<Object> arrayList2, Integer num, Boolean bool, View view) {
                    sendPrivateImageList = new ArrayList<>();
                    sendPrivateImageList = arrayList2;
                    if (bool.booleanValue()) {
                        return;
                    }
                    folderPath = ((DataFileModel) sendPrivateImageList.get(num.intValue())).folderPath;
                    VisibleView();
                    if (mActionMode != null) {
                        mActionMode = null;
                    }
                    if (mActionMode == null) {
                        mActionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(mActionModeCallback);
                    }
                    SelectSingleImage(sendPrivateImageList, num.intValue());
                }
            });
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

    public class AccessFolder11 extends RecyclerView.Adapter<AccessFolder11.MyViewHolder> {
        boolean abool;
        Context context;
        ArrayList<FolderModel> folderList;
        ArrayList<DataFileModel> unlockArray = new ArrayList<>();

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
                ImagePrivateFragment.FName = folderPath;
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

    public void TempDeleteImage(ArrayList<DataFileModel> arrayList) {
        ArrayList arrayList2 = new ArrayList();
        Iterator<DataFileModel> it = arrayList.iterator();
        char c = CharCompanionObject.MAX_VALUE;
        while (it.hasNext()) {
            DataFileModel next = it.next();
            DocumentFile fromSingleUri = DocumentFile.fromSingleUri(requireActivity(), Uri.parse(next.path));
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
            privateImageList.remove(it2.next());
        }
        getActivity().runOnUiThread(() -> privateImageAdapter.notifyDataSetChanged());
    }

    public class Unlock11Execute extends AsyncTask<Void, Void, Void> {
        ArrayList<DataFileModel> arrayDataList;
        ArrayList arrayList = new ArrayList();
        char c = CharCompanionObject.MAX_VALUE;
        ProgressDialog progressDialog;

        public Unlock11Execute(ArrayList<DataFileModel> arrayList) {
            this.arrayDataList = new ArrayList<>();
            this.arrayDataList = arrayList;
            progressDialog = new ProgressDialog(context);
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
                try {
                    Iterator<DataFileModel> it = arrayDataList.iterator();
                    while (it.hasNext()) {
                        DataFileModel next = it.next();
                        if (DocumentFile.fromSingleUri(requireActivity(), Uri.parse(next.path)).exists()) {
                            if (DownloadForUnlock(getActivity(), next.path)) {
                                arrayList.add(next);
                                if (c == 0) {
                                    return null;
                                }
                                c = (char) 1;
                            }
                        } else {
                            c = (char) 0;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                TempDeleteImage(arrayDataList);
                try {
                    arrayDataList.clear();
                    Iterator it2 = arrayList.iterator();
                    while (it2.hasNext()) {
                        ArrayList<Object> arrayList = privateImageList;
                        ((DataFileModel) it2.next()).isSelected = false;
                        arrayList.contains(false);
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
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
            AppUtilsClass.RefreshImageAlbum(getActivity());
            AppUtilsClass.RefreshMoment(getActivity());
            MomentFragment.momentAdapter.notifyDataSetChanged();
            privateImageAdapter.notifyDataSetChanged();
            char c = this.c;
            if (c == 0) {
                Toast.makeText(context, "Couldn't Unlock Image", Toast.LENGTH_SHORT).show();
            } else if (c == 1) {
                Toast.makeText(context, getString(R.string.files_are_unlocked_successfully), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
