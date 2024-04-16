package hdphoto.galleryimages.gelleryalbum.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.view.ActionMode;
import androidx.appcompat.view.StandaloneActionMode;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import hdphoto.galleryimages.gelleryalbum.GalleryFolder;
import hdphoto.galleryimages.gelleryalbum.R;
import hdphoto.galleryimages.gelleryalbum.activity.PrivateActivity;
import hdphoto.galleryimages.gelleryalbum.constant.Common;
import hdphoto.galleryimages.gelleryalbum.constant.ExternalStorageHelper;
import hdphoto.galleryimages.gelleryalbum.constant.WrapperGridlayoutManager;
import hdphoto.galleryimages.gelleryalbum.activity.MainActivity;
import hdphoto.galleryimages.gelleryalbum.activity.PreviewActivity;
import hdphoto.galleryimages.gelleryalbum.adapter.AlbumAdapter;
import hdphoto.galleryimages.gelleryalbum.adapter.MomentAdapter;
import hdphoto.galleryimages.gelleryalbum.getdata.MomentData;
import hdphoto.galleryimages.gelleryalbum.listeners.MomentSortingListener;
import hdphoto.galleryimages.gelleryalbum.listeners.OnClickListener;
import hdphoto.galleryimages.gelleryalbum.model.DataFileModel;
import hdphoto.galleryimages.gelleryalbum.appclass.GalleryAppClass;
import hdphoto.galleryimages.gelleryalbum.model.DialogAlbumModel;
import hdphoto.galleryimages.gelleryalbum.utils.Utils;
import hdphoto.galleryimages.gelleryalbum.utils.AppUtilsClass;
import hdphoto.galleryimages.gelleryalbum.utils.ConstantArrayClass;
import hdphoto.galleryimages.gelleryalbum.utils.FileUtils;
import hdphoto.galleryimages.gelleryalbum.utils.FolderPath;
import hdphoto.galleryimages.gelleryalbum.utils.PrefClass;


import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public class MomentFragment extends BaseFragment{
    private RecyclerView recyclerView;

    static int CAMERA_PHOTO = 111;
    public static RecyclerView fragmnetimagegridView;
    public static MomentAdapter momentAdapter;
    public static RelativeLayout rlPhotoLayout;
    private Dialog albumDialog;
    private GridView albumGridView;
    int bpos;
    ArrayList<Object> callBackAlbumsList;
    ContentObserver contentObserver;
    int currentFirstVisibleItem;
    BottomSheetDialog dialog;
    private AlbumAdapter dialogAlbumAdapter;
    WrapperGridlayoutManager gridLayoutManager;
    Handler handler;
    int i2;
    Uri imageToUploadUri;
    ImageView ic_return_top;
    ProgressBar loader;
    ActionMode mActionMode;
    private Menu mMenu;
    MomentSortingListener momentSortingListener;
    ProgressDialog progressDelete;
    ProgressDialog progressLock;
    String progressTag;
    int pv;
    TextView tv_ErrorDisplay;
    Typeface typeface;
    View view1;
    int countSelected = 0;
    boolean isSelectedAll = false;
    boolean isSingleSelection = false;

    int dialog_count = 0;
    private String folder_path = "";
    List<GalleryFolder> folderList = new ArrayList<>();
    public static MomentData getMomentData = null;
    @Override
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        DataOrientation(configuration.orientation);
    }

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_moment, viewGroup, false);

        preferenceClass = new PrefClass(requireActivity());

        view1 = inflate.findViewById(R.id.layout_album_select);

        MainActivity.ivSelectAll.setImageDrawable(getResources().getDrawable(R.drawable.action_select));

        rlPhotoLayout = inflate.findViewById(R.id.hintMainPhotoLayout);
        tv_ErrorDisplay = inflate.findViewById(R.id.text_view_error);
        loader = inflate.findViewById(R.id.loader);
        swipeRefreshLayout = inflate.findViewById(R.id.swipe);
        ic_return_top = inflate.findViewById(R.id.img_return_top);
        swipeRefreshLayout = inflate.findViewById(R.id.swipe);
        fragmnetimagegridView = inflate.findViewById(R.id.grid_view_album_select);
        tv_ErrorDisplay.setVisibility(View.INVISIBLE);

        HiddenView();
        ic_return_top.setVisibility(View.GONE);

        fragmnetimagegridView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                currentFirstVisibleItem = gridLayoutManager.findFirstVisibleItemPosition();
                if (currentFirstVisibleItem == 0) {
                    ic_return_top.setVisibility(View.GONE);
                } else if (i2 > 0) {
                    ic_return_top.setVisibility(View.GONE);
                } else if (i2 < 0) {
                    ic_return_top.setVisibility(View.GONE);
                }
            }
        });

        ic_return_top.setOnClickListener(view -> fragmnetimagegridView.smoothScrollToPosition(0));

        new Handler().postDelayed(() -> isCheckImage(ConstantArrayClass.albumsList), 1000L);

        SetAdapterData();

        recyclerView = inflate.findViewById(R.id.rv_album);
        recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 1, LinearLayoutManager.HORIZONTAL, false));

        return inflate;
    }

    @Override
    public void onResume() {

        if (MainActivity.oriTag.equals("SettingActivity")) {
            DataOrientation(requireActivity().getResources().getConfiguration().orientation);
        }
        super.onResume();
    }

    private void replaceFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_list, fragment)
                .addToBackStack(null)
                .commit();
    }

    private ArrayList<String> allFolderList = new ArrayList<>();

    private ArrayList<GalleryFolder> fetchMedia() {
        ArrayList<GalleryFolder> mediaFilesArrayList = new ArrayList<>();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        try {
            Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToNext()) {
                do {
                    String id = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                    String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                    String displayName = cursor.getString(cursor.getColumnIndexOrThrow("_display_name"));
                    String size = cursor.getString(cursor.getColumnIndexOrThrow("_size"));
                    String duration = cursor.getString(cursor.getColumnIndexOrThrow("duration"));
                    String path = cursor.getString(cursor.getColumnIndexOrThrow("_data"));
                    String dateAdded = cursor.getString(cursor.getColumnIndexOrThrow("date_added"));
                    GalleryFolder mediaFiles = new GalleryFolder(id, title, displayName, size, duration, path, dateAdded);
                    int index = path.lastIndexOf("/");
                    String subString = path.substring(0, index);
                    if (!allFolderList.contains(subString)) {
                        allFolderList.add(subString);
                    }
                    mediaFilesArrayList.add(mediaFiles);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mediaFilesArrayList;
    }

    public void DeleteDialog() {
        final Dialog dialog = new Dialog(getContext(), R.style.ThemeWithCorners1);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dg_delete_file);
        dialog.setTitle("Delete File");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        ((TextView) dialog.findViewById(R.id.rl_yes)).setOnClickListener(view -> {
            progressTag = "FromDelete";
            new DeleteToTrashMomentExecute(GetSelectedList()).execute(new Void[0]);
            dialog.dismiss();
        });

        ((TextView) dialog.findViewById(R.id.rl_no)).setOnClickListener(view -> {
            UnSelectAll();
            mMenu.getItem(0).setIcon(ContextCompat.getDrawable(requireActivity(), R.drawable.action_select));
            if (mActionMode != null) {
                mActionMode.finish();
            }
            dialog.dismiss();
        });

        dialog.show();
    }

    public class DeleteToTrashMomentExecute extends AsyncTask<Void, Void, Void> {
        ArrayList<DataFileModel> pathList;

        public DeleteToTrashMomentExecute(ArrayList<DataFileModel> arrayList) {
            this.pathList = new ArrayList<>();
            progressDelete = new ProgressDialog(requireActivity());
            this.pathList = arrayList;
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
            progressDelete.setMessage(getString(R.string.please_wait_a_while));
            progressDelete.setProgressStyle(0);
            progressDelete.setIndeterminate(false);
            progressDelete.setCancelable(false);
            progressDelete.show();
        }

        @Override
        public Void doInBackground(Void... voidArr) {
            ArrayList arrayList = new ArrayList();
            ArrayList<String> listString = preferenceClass.getListString(Common.gOldPath);
            String str = null;
            int i = 0;
            while (i < pathList.size()) {
                File file2 = new File(pathList.get(i).path);
                String mediaType = pathList.get(i).getMediaType();
                if (mediaType.equals("1")) {
                    str = FolderPath.SDCARD_PATH_DELETE_IMAGE;
                } else if (mediaType.equals(ExifInterface.GPS_MEASUREMENT_3D)) {
                    str = FolderPath.SDCARD_PATH_DELETE_VIDEO;
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
                            File imageFile = new File(pathList.get(i2).path);
                            if (imageFile.exists()) {
                                imageFile.delete();
                            } else {
                                Toast.makeText(requireActivity(), "Image file does not exist.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        preferenceClass.putListString(Common.gOldPath, listString);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                i++;
                str = str2;
            }
            return null;
        }

        @Override
        public void onPostExecute(Void r5) {
            super.onPostExecute(r5);
            UnSelectAll();
            isSingleSelection = false;
            isSelectedAll = false;
            progressDelete.dismiss();
            if (mActionMode != null) {
                mActionMode.finish();
            }
            new Handler().postDelayed(() -> AppUtilsClass.RefreshMoment(requireActivity()), 500L);
            Toast.makeText(requireActivity(), R.string.files_are_deleted_successfully, Toast.LENGTH_SHORT).show();
        }
    }

    public ArrayList<String> GetBothDataPathList(String str) {
        ArrayList<String> arrayList = new ArrayList<>();
        Cursor query = requireActivity().getContentResolver().query(MediaStore.Files.getContentUri("external"), new String[]{Utils.mediaPath, "bucket_display_name"}, "bucket_id = ?", new String[]{str}, null);
        if (query.moveToFirst()) {
            int columnIndexOrThrow = query.getColumnIndexOrThrow(Utils.mediaPath);
            do {
                arrayList.add(query.getString(columnIndexOrThrow));
            } while (query.moveToNext());
            query.close();
            return arrayList;
        }
        query.close();
        return arrayList;
    }

    public void CopyMoveData(String str, boolean z) {
        for (int i = 0; i < GetSelectedList().size(); i++) {
            File file = new File(GetSelectedList().get(i).path);
            File file2 = new File(str + File.separator + file.getName());
            try {
                FileUtils.CopyMoveFile(file, file2);
                AppUtilsClass.insertUri(requireActivity(), file2);
                if (z) {
                    file.delete();
                    ContentResolver contentResolver = requireActivity().getContentResolver();
                    Uri contentUri = MediaStore.Files.getContentUri("external");
                    contentResolver.delete(contentUri, "_data='" + file.getPath() + "'", null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!z) {
            Toast.makeText(GalleryAppClass.getInstance(), getString(R.string.files_copy_successfully), Toast.LENGTH_SHORT).show();
        }
        new Handler().postDelayed(() -> AppUtilsClass.RefreshPhotoVideo(requireActivity()), 500L);
        if (z) {
            Toast.makeText(GalleryAppClass.getInstance(), getString(R.string.files_move_successfully), Toast.LENGTH_SHORT).show();
        }
    }

    private void orientationBasedUIAlbum(int i) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) requireActivity().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
        if (dialogAlbumAdapter != null) {
            int screenWidthPixels = displayMetrics.widthPixels;
            dialogAlbumAdapter.setLayoutParams(i == 1 ? screenWidthPixels / 2 : screenWidthPixels / 4);
        }
        albumGridView.setNumColumns(i != 1 ? 4 : 2);
    }

    public void DataOrientation(int i) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) requireActivity().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
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
        if (momentAdapter != null) {
            momentAdapter.setLayoutParams(displayMetrics.widthPixels / (i == 1 ? this.i2 : MainActivity.dataDivider));
        }
        if (i != 1) {
            this.i2 = MainActivity.dataDivider;
        }
        gridLayoutManager = new WrapperGridlayoutManager(this.requireActivity(), this.i2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int i3) {
                if (momentAdapter == null || momentAdapter.getItemViewType(i3) != 0) {
                    return 1;
                }
                return MomentFragment.this.i2;
            }
        });
        fragmnetimagegridView.setLayoutManager(gridLayoutManager);
    }

    public void SelectSingleFile(ArrayList<Object> arrayList, int i) {
        isSingleSelection = true;
        ((DataFileModel) arrayList.get(i)).isSelected = !((DataFileModel) arrayList.get(i)).isSelected;
        if (((DataFileModel) arrayList.get(i)).isSelected) {
            countSelected++;
        } else {
            countSelected--;
        }
        if (countSelected <= 0) {
            isSingleSelection = false;
            isSelectedAll = false;
            if (mActionMode != null) {
                mActionMode.finish();
            }
        }
        if (GetSelectedList().size() != 0) {
            mActionMode.setTitle(String.valueOf(GetSelectedList().size()));
        }
        momentAdapter.notifyDataSetChanged();
    }

    public ArrayList<DataFileModel> GetSelectedList() {
        ArrayList<DataFileModel> arrayList = new ArrayList<>();
        int size = ConstantArrayClass.albumsList.size();
        for (int i = 0; i < size; i++) {
            if ((ConstantArrayClass.albumsList.get(i) instanceof DataFileModel) && ((DataFileModel) ConstantArrayClass.albumsList.get(i)).isSelected) {
                arrayList.add((DataFileModel) ConstantArrayClass.albumsList.get(i));
            }
        }
        return arrayList;
    }

    public void UnSelectAll() {
        int size = ConstantArrayClass.albumsList.size();
        for (int i = 0; i < size; i++) {
            if (ConstantArrayClass.albumsList.get(i) instanceof DataFileModel) {
                ((DataFileModel) ConstantArrayClass.albumsList.get(i)).isSelected = false;
            }
        }
        if (GetSelectedList().size() != 0) {
            mActionMode.setTitle(String.valueOf(GetSelectedList().size()));
        }
        countSelected = 0;
        isSelectedAll = false;
        isSingleSelection = false;
        momentAdapter.notifyDataSetChanged();
    }

    public void SelectAll() {
        int size = ConstantArrayClass.albumsList.size();
        for (int i = 0; i < size; i++) {
            if (ConstantArrayClass.albumsList.get(i) instanceof DataFileModel) {
                ((DataFileModel) ConstantArrayClass.albumsList.get(i)).isSelected = true;
            }
        }
        if (GetSelectedList().size() != 0) {
            mActionMode.setTitle(String.valueOf(GetSelectedList().size()));
        }
        countSelected = ConstantArrayClass.albumsList.size();
        isSelectedAll = true;
        isSingleSelection = true;
        momentAdapter.notifyDataSetChanged();
    }

    private void SetAdapterData() {
        momentAdapter = new MomentAdapter(requireActivity(), ConstantArrayClass.albumsList);
        fragmnetimagegridView.setAdapter(momentAdapter);
        loader.setVisibility(View.GONE);
        DataOrientation(getResources().getConfiguration().orientation);
        momentAdapter.setItemClickCallback(new OnClickListener<ArrayList<Object>, Integer, View>() {
            @Override
            public void onClickMoreListener(ArrayList<Object> arrayList, Integer num, View view) {
            }

            @Override
            public void onLongClickListener(ArrayList<Object> arrayList, Integer num) {
                callBackAlbumsList = new ArrayList<>();
                callBackAlbumsList = arrayList;
                @SuppressLint("RestrictedApi") StandaloneActionMode standaloneActionMode = (StandaloneActionMode) mActionMode;
                try {
                    Field declaredField = StandaloneActionMode.class.getDeclaredField("mContextView");
                    declaredField.setAccessible(true);
                    Object obj = declaredField.get(standaloneActionMode);
                    if (preferenceClass.getInt("ThemeMode", 0) == 0) {
                        ((View) obj).setBackground(new ColorDrawable(getContext().getResources().getColor(R.color.white)));
                        requireActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.white));
                    } else if (preferenceClass.getInt("ThemeMode", 1) == 1) {
                        ((View) obj).setBackground(new ColorDrawable(getContext().getResources().getColor(R.color.black)));
                        requireActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.black));
                    }
                    SelectSingleFile(callBackAlbumsList, num.intValue());
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }

            @Override
            public void onClickListener(ArrayList<Object> arrayList, Integer num) {
                callBackAlbumsList = new ArrayList<>();
                callBackAlbumsList = arrayList;
                bpos = num.intValue();
                if (isSingleSelection) {
                    SelectSingleFile(callBackAlbumsList, num.intValue());
                    return;
                }
                Common.IdentifyActivity = "MomentFragment";
                Common.strplay = "Photofrag";
                Intent intent = new Intent(requireActivity(), PreviewActivity.class);
                intent.putExtra(Common.gImagePath, ((DataFileModel) callBackAlbumsList.get(bpos)).path);
                intent.putExtra(Common.gTotalimage, callBackAlbumsList.size());
                intent.putExtra(Common.gCurrenrtPosition, bpos);
                intent.putExtra(Common.gArrayType, "open");
                intent.putExtra(Common.gActivityname, "MomentFragment");
                intent.putExtra(Common.gMediaType, ((DataFileModel) callBackAlbumsList.get(bpos)).getMediaType());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == CAMERA_PHOTO && i2 == -1) {
            Uri uri = imageToUploadUri;
            if (uri != null) {
                AddImageToGallery(uri.getPath());
            } else {
                Toast.makeText(requireActivity(), "Error while capturing Image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void AddImageToGallery(String str) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("date_modified", Long.valueOf(System.currentTimeMillis()));
        contentValues.put("mime_type", "image/jpg");
        contentValues.put(Utils.mediaPath, str);
        requireActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
    }

    public void isCheckImage(ArrayList<Object> arrayList) {
        if (arrayList.size() >= 1) {
            rlPhotoLayout.setVisibility(View.GONE);
            fragmnetimagegridView.setVisibility(View.VISIBLE);
            return;
        }
        fragmnetimagegridView.setVisibility(View.GONE);
        rlPhotoLayout.setVisibility(View.VISIBLE);
    }

    private void SortingCallBack() {
        momentSortingListener = new MomentSorting();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (contentObserver != null) {
            requireActivity().getContentResolver().unregisterContentObserver(contentObserver);
            contentObserver = null;
            Handler handler = this.handler;
            if (handler != null) {
                handler.removeCallbacksAndMessages(null);
                this.handler = null;
            }
        }
    }

    public void HiddenView() {
        MainActivity.rl_imageBtn.setVisibility(View.GONE);
        MainActivity.rl_videoBtn.setVisibility(View.GONE);
        MainActivity.ivSelectAll.setVisibility(View.GONE);
        MainActivity.ivAddAlbum.setVisibility(View.GONE);
    }


    public class MomentSorting implements MomentSortingListener {
        MomentSorting() {
        }

        @Override
        public void Sorting(ArrayList<Object> arrayList) {
            isCheckImage(arrayList);
            momentAdapter = new MomentAdapter(requireActivity(), arrayList);
            fragmnetimagegridView.setAdapter(momentAdapter);
            loader.setVisibility(View.GONE);
            fragmnetimagegridView.setVisibility(View.VISIBLE);
            DataOrientation(requireActivity().getResources().getConfiguration().orientation);
            momentAdapter.setItemClickCallback(new OnClickListener<ArrayList<Object>, Integer, View>() {
                @Override
                public void onClickMoreListener(ArrayList<Object> arrayList2, Integer num, View view) {
                }

                @Override
                public void onLongClickListener(ArrayList<Object> arrayList2, Integer num) {
                    callBackAlbumsList = new ArrayList<>();
                    callBackAlbumsList = arrayList2;
                    @SuppressLint("RestrictedApi") StandaloneActionMode standaloneActionMode = (StandaloneActionMode) mActionMode;
                    try {
                        Field declaredField = StandaloneActionMode.class.getDeclaredField("mContextView");
                        declaredField.setAccessible(true);
                        Object obj = declaredField.get(standaloneActionMode);
                        if (preferenceClass.getInt("ThemeMode", 0) == 0) {
                            ((View) obj).setBackground(new ColorDrawable(getContext().getResources().getColor(R.color.white)));
                            requireActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.white));
                        } else if (preferenceClass.getInt("ThemeMode", 1) == 1) {
                            ((View) obj).setBackground(new ColorDrawable(getContext().getResources().getColor(R.color.black)));
                            requireActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.black));
                        }
                        SelectSingleFile(callBackAlbumsList, num.intValue());
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                }

                @Override
                public void onClickListener(ArrayList<Object> arrayList2, Integer num) {
                    callBackAlbumsList = new ArrayList<>();
                    callBackAlbumsList = arrayList2;
                    bpos = num.intValue();
                    if (isSingleSelection) {
                        SelectSingleFile(callBackAlbumsList, num.intValue());
                        return;
                    }
                    Common.IdentifyActivity = "MomentFragment";
                    Common.strplay = "Photofrag";
                    Intent intent = new Intent(requireActivity(), PreviewActivity.class);
                    intent.putExtra(Common.gImagePath, ((DataFileModel) callBackAlbumsList.get(bpos)).path);
                    intent.putExtra(Common.gTotalimage, callBackAlbumsList.size());
                    intent.putExtra(Common.gCurrenrtPosition, bpos);
                    intent.putExtra(Common.gArrayType, "open");
                    intent.putExtra(Common.gActivityname, "MomentFragment");
                    intent.putExtra(Common.gMediaType, ((DataFileModel) callBackAlbumsList.get(bpos)).getMediaType());
                    startActivity(intent);
                }
            });
        }
    }

    public void SecurityDialog(ArrayList<DataFileModel> arrayList) {
        ConstantArrayClass.firstTimeLockDataArray = arrayList;
        final Dialog dialog = new Dialog(requireActivity(), R.style.ThemeWithCorners1);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialog_security);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        ((TextView) dialog.findViewById(R.id.textDesc)).setText("Set your security lock for hide photos & videos!");

        ((RelativeLayout) dialog.findViewById(R.id.rl_yes)).setOnClickListener(view -> {
            FolderPath.lock_screen = 1;
            startActivity(new Intent(requireActivity(), PrivateActivity.class));
            dialog.dismiss();
        });

        ((RelativeLayout) dialog.findViewById(R.id.rl_no)).setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }

    public String GetParentPath(String str) {
        return new File(str).getAbsoluteFile().getParent();
    }

    public void GetAlbumDialog(final boolean z) {
        albumDialog = new Dialog(requireActivity(), R.style.MyDialog);
        albumDialog.requestWindowFeature(1);
        albumDialog.setCancelable(false);
        albumDialog.setContentView(R.layout.dg_folder);
        ImageView imageView = (ImageView) albumDialog.findViewById(R.id.btnBack);
        TextView textView = (TextView) albumDialog.findViewById(R.id.toolbarTitle);
        textView.setText("Select File Folder");
        textView.setVisibility(View.VISIBLE);
        albumGridView = (GridView) albumDialog.findViewById(R.id.albumGridView);
        Cursor query = requireActivity().getContentResolver().query(MediaStore.Files.getContentUri("external"), new String[]{"_id", "bucket_id", "bucket_display_name", Utils.mediaPath}, "media_type=1 OR media_type=3", null, null);
        final ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        if (query != null) {
            while (query.moveToNext()) {
                DialogAlbumModel gMDialogAlbumModel = new DialogAlbumModel();
                gMDialogAlbumModel.bucket_id = query.getString(query.getColumnIndex("bucket_id"));
                if (!arrayList2.contains(gMDialogAlbumModel.bucket_id)) {
                    gMDialogAlbumModel.foldername = query.getString(query.getColumnIndex("bucket_display_name"));
                    gMDialogAlbumModel.coverThumb = query.getString(query.getColumnIndexOrThrow(Utils.mediaPath));
                    gMDialogAlbumModel.folderPath = GetParentPath(query.getString(query.getColumnIndexOrThrow(Utils.mediaPath)));
                    gMDialogAlbumModel.id = query.getString(query.getColumnIndex("_id"));
                    gMDialogAlbumModel.pathlist = GetBothDataPathList("" + gMDialogAlbumModel.bucket_id);
                    arrayList.add(gMDialogAlbumModel);
                    arrayList2.add(gMDialogAlbumModel.bucket_id);
                }
            }
            query.close();
        }
        dialogAlbumAdapter = new AlbumAdapter(requireActivity(), arrayList);
        albumGridView.setAdapter((ListAdapter) dialogAlbumAdapter);
        orientationBasedUIAlbum(getResources().getConfiguration().orientation);

        albumGridView.setOnItemClickListener((adapterView, view, i, j) -> {
            if (arrayList == null || arrayList.isEmpty()) {
                return;
            }
            File file = new File(((DialogAlbumModel) arrayList.get(i)).folderPath);
            String str = ((DialogAlbumModel) arrayList.get(i)).bucket_id;
            String str2 = ((DialogAlbumModel) arrayList.get(i)).foldername;
            CopyMoveData(file.getAbsolutePath(), z);
            if (albumDialog != null || albumDialog.isShowing()) {
                albumDialog.dismiss();
            }
            if (mActionMode != null) {
                mActionMode.finish();
            }
        });

        imageView.setOnClickListener(view -> {
            UnSelectAll();
            mMenu.getItem(0).setIcon(ContextCompat.getDrawable(requireActivity(), R.drawable.action_select));
            if (mActionMode != null) {
                mActionMode.finish();
            }
            albumDialog.dismiss();
        });

        albumDialog.setOnKeyListener((dialogInterface, i, keyEvent) -> {
            if (i == 4 && dialog_count == 0) {
                requireActivity().onBackPressed();
                return true;
            }
            dialog_count = 0;
            return false;
        });
        albumDialog.show();
    }

    public void ShareBoth(ArrayList<DataFileModel> arrayList) {
        ArrayList<Uri> arrayList2 = new ArrayList<>();
        for (int i = 0; i < arrayList.size(); i++) {
            File file = new File(arrayList.get(i).path);
            FragmentActivity activity = getActivity();
            arrayList2.add(FileProvider.getUriForFile(activity, activity.getPackageName() + ".fileprovider", file));
        }
        try {
            Intent intent = new Intent("android.intent.action.SEND_MULTIPLE");
            intent.setType("*/*");
            intent.putParcelableArrayListExtra("android.intent.extra.STREAM", arrayList2);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
