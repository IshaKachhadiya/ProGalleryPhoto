package hdphoto.galleryimages.gelleryalbum.fragment;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import hdphoto.galleryimages.gelleryalbum.getdata.ImageAlbumData;
import hdphoto.galleryimages.gelleryalbum.listeners.AlbumSortingListener;
import hdphoto.galleryimages.gelleryalbum.listeners.OnClickListener;
import hdphoto.galleryimages.gelleryalbum.activity.PrivateActivity;
import hdphoto.galleryimages.gelleryalbum.activity.ImageSelectActivity;
import hdphoto.galleryimages.gelleryalbum.activity.ImagesActivity;
import hdphoto.galleryimages.gelleryalbum.activity.MainActivity;
import hdphoto.galleryimages.gelleryalbum.sorting.LoginPreferenceUtilsFolder;
import hdphoto.galleryimages.gelleryalbum.sorting.SortingDataDialog;
import hdphoto.galleryimages.gelleryalbum.sorting.SortingFolderDialog;
import hdphoto.galleryimages.gelleryalbum.model.DataFileModel;
import hdphoto.galleryimages.gelleryalbum.model.FolderModel;
import hdphoto.galleryimages.gelleryalbum.constant.Common;
import hdphoto.galleryimages.gelleryalbum.constant.ExternalStorageHelper;
import hdphoto.galleryimages.gelleryalbum.constant.WrapperGridlayoutManager;
import hdphoto.galleryimages.gelleryalbum.utils.Utils;
import hdphoto.galleryimages.gelleryalbum.utils.AppUtilsClass;
import hdphoto.galleryimages.gelleryalbum.utils.ConstantArrayClass;
import hdphoto.galleryimages.gelleryalbum.utils.FileUtils;
import hdphoto.galleryimages.gelleryalbum.utils.FolderPath;
import hdphoto.galleryimages.gelleryalbum.utils.PrefClass;
import hdphoto.galleryimages.gelleryalbum.R;

@SuppressWarnings("all")

public class ImageFragment extends BaseFragment {
    public static ImageFolderAdapter imageFolderAdapter;
    Activity activity;
    AlbumSortingListener albumSortingListener;
    int bpos;
    ArrayList<FolderModel> callBackAlbumsList;
    ContentObserver contentObserver;
    Context context;
    Dialog deletAlbumdg;
    Dialog detailAlbumdg;
    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    Handler handler;
    int i2;
    Uri imageToUploadUri;
    ProgressBar loader;
    ProgressDialog progressDelete;
    ProgressDialog progressLock;
    String progressTag;
    int pv;
    RelativeLayout rlPhotoLayout;
    Thread thread;
    TextView tv_ErrorDisplay;
    View view;
    int countSelected = 0;
    String folder_path = "";
    boolean isSelectAll = false;
    boolean isSingleSelection = false;
    boolean isSelectedAll = false;
    SwipeRefreshLayout swipeRefreshLayout;
    String str;

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
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_image, viewGroup, false);
        preferenceClass = new PrefClass(getActivity());

        view = inflate.findViewById(R.id.layout_album_select);

        initView(inflate);

        SortingCallBack();
        clickListener();
        SetAdapter();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            handler.post(() -> {
                try {
                    AppUtilsClass.ScanImageAlbumListData(requireActivity());
                    new ArrayList().addAll(ConstantArrayClass.imageAlbumsList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });

        return inflate;
    }

    private void initView(View inflate) {
        rlPhotoLayout = inflate.findViewById(R.id.hintMainPhotoLayout);
        tv_ErrorDisplay = inflate.findViewById(R.id.text_view_error);
        loader = inflate.findViewById(R.id.loader);
        recyclerView = inflate.findViewById(R.id.grid_view_album_select);

        tv_ErrorDisplay.setVisibility(View.INVISIBLE);
    }

    private void clickListener() {
        new Handler().postDelayed(() -> CheckImageFound(ConstantArrayClass.imageAlbumsList), 1000L);
    }

    @Override
    public void onResume() {
        FolderOrientation(activity.getResources().getConfiguration().orientation);
        if (imageFolderAdapter != null) {
            new Handler().postDelayed(() -> {
//                AppUtilsClass.RefreshImageAlbum(getActivity());
                AppUtilsClass.ScanImageAlbumListData(requireActivity());
                recyclerView.smoothScrollToPosition(0);
            }, 500);
        }
        super.onResume();
    }

    public class RenameAlbumExecute extends AsyncTask<Void, Void, Void> {
        ArrayList<FolderModel> arrayList;
        String newAlbumFolder;
        int pos;
        ProgressDialog progressDialog;

        public RenameAlbumExecute(ArrayList<FolderModel> arrayList, Integer num, String str) {
            this.arrayList = new ArrayList<>();
            progressDialog = new ProgressDialog(context);
            this.arrayList = arrayList;
            this.pos = num.intValue();
            this.newAlbumFolder = str;
        }

        @Override
        public void onPreExecute() {
            activity.runOnUiThread(() -> {
                progressDialog.setMessage("Please wait a while...");
                progressDialog.setProgressStyle(0);
                progressDialog.setIndeterminate(false);
                progressDialog.setCancelable(false);
                progressDialog.show();
            });
        }

        @Override
        public Void doInBackground(Void... voidArr) {
            File parentFile = new File(arrayList.get(pos).getCoverThumb()).getParentFile();
            File file = new File(parentFile.getParent() + File.separator + newAlbumFolder);
            String[] list = parentFile.list();
            if (list != null) {
                for (String str : list) {
                    File file2 = new File(parentFile + File.separator + str);
                    File file3 = new File(file + File.separator + str);
                    if (file2.exists()) {
                        String substring = file2.getPath().substring(file2.getPath().lastIndexOf("/") + 1);
                        if (substring.endsWith(".jpg") || substring.endsWith(".JPG") || substring.endsWith(".jpeg") || substring.endsWith(".JPEG") || substring.endsWith(".png") || substring.endsWith(".PNG") || substring.endsWith(".gif") || substring.endsWith(".GIF")) {
                            file.mkdirs();
                            if (file2.renameTo(file3)) {
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("date_modified", Long.valueOf(System.currentTimeMillis()));
                                contentValues.put("mime_type", "image/*");
                                contentValues.put(Utils.mediaPath, file3.getPath());
                                activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                                activity.getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "_data='" + file2.getPath() + "'", null);
                            }
                        }
                    }
                }
            }
            if (parentFile.list() == null) {
                parentFile.delete();
            }
            return null;
        }

        @Override
        public void onPostExecute(Void r4) {
            super.onPostExecute(r4);
            AppUtilsClass.RefreshImageAlbum(getActivity());
            new Handler().postDelayed(() -> AppUtilsClass.RefreshMoment(getActivity()), 500L);
            progressDialog.dismiss();
        }
    }

    public void DetailDialog(ArrayList<FolderModel> arrayList, Integer num) {
        File file;
        detailAlbumdg = new Dialog(activity, R.style.ThemeWithCorners1);
        detailAlbumdg.requestWindowFeature(1);
        int i = 0;
        detailAlbumdg.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        detailAlbumdg.setContentView(R.layout.dialog_detail_folder);
        detailAlbumdg.setTitle("Details");
        detailAlbumdg.setCancelable(false);
        detailAlbumdg.setCanceledOnTouchOutside(false);
        TextView textView = (TextView) detailAlbumdg.findViewById(R.id.txtAlbumName);
        TextView textView2 = (TextView) detailAlbumdg.findViewById(R.id.txtAlbumPath);
        TextView textView3 = (TextView) detailAlbumdg.findViewById(R.id.txtAlbumSize);
        TextView textView4 = (TextView) detailAlbumdg.findViewById(R.id.txtAlbumItem);
        TextView textView5 = (TextView) detailAlbumdg.findViewById(R.id.txtAlbumCreatedOn);
        RelativeLayout relativeLayout = (RelativeLayout) detailAlbumdg.findViewById(R.id.rl_no);
        File parentFile = new File(arrayList.get(num.intValue()).getCoverThumb()).getParentFile();
        String[] list = parentFile.list();
        long j = 0;
        if (list != null) {
            int length = list.length;
            while (i < length) {
                File file2 = new File(parentFile + File.separator + list[i]);
                if (file2.exists()) {
                    file = parentFile;
                    String substring = file2.getPath().substring(file2.getPath().lastIndexOf("/") + 1);
                    if (substring.endsWith(".jpg") || substring.endsWith(".JPG") || substring.endsWith(".jpeg") || substring.endsWith(".JPEG") || substring.endsWith(".png") || substring.endsWith(".PNG") || substring.endsWith(".gif") || substring.endsWith(".GIF")) {
                        j += file2.length();
                    }
                } else {
                    file = parentFile;
                }
                i++;
                parentFile = file;
            }
        }
        textView.setText(arrayList.get(num.intValue()).foldername + "");
        textView2.setText(new File(arrayList.get(num.intValue()).getCoverThumb()).getParent());
        textView3.setText(FileUtils.convertToHumanReadableSize(activity, j));
        textView4.setText(arrayList.get(num.intValue()).getPathlist().size() + " Files.");
        relativeLayout.setOnClickListener(view -> detailAlbumdg.dismiss());
        detailAlbumdg.show();
    }

    public void DeleteAlbumDialog(ArrayList<FolderModel> arrayList, Integer num) {
        deletAlbumdg = new Dialog(activity, R.style.ThemeWithCorners1);
        deletAlbumdg.requestWindowFeature(1);
        deletAlbumdg.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        deletAlbumdg.setContentView(R.layout.dg_delete_image_folder);
        deletAlbumdg.setTitle("Delete Image");
        deletAlbumdg.setCancelable(false);
        deletAlbumdg.setCanceledOnTouchOutside(false);
        deletAlbumdg.findViewById(R.id.rl_yes).setOnClickListener(view -> {
            if (GetSelectedList().size() <= 0) {
                Toast.makeText(getActivity(), getString(R.string.select_image), Toast.LENGTH_SHORT).show();
            } else if (ExternalStorageHelper.isExternalStorageReadableAndWritable()) {
                progressTag = "FromDelete";
                new DeleteTrashImageExecute(GetSelectedList()).execute(new Void[0]);
            } else {
                Toast.makeText(getActivity(), "not Read Write", Toast.LENGTH_SHORT).show();
            }
            deletAlbumdg.dismiss();
        });
        deletAlbumdg.findViewById(R.id.rl_no).setOnClickListener(view -> deletAlbumdg.dismiss());
        deletAlbumdg.show();
    }

    public class DeleteTrashImageExecute extends AsyncTask<Void, Void, Void> {
        ArrayList<DataFileModel> pathList;

        public DeleteTrashImageExecute(ArrayList<DataFileModel> arrayList) {
            this.pathList = new ArrayList<>();
            progressDelete = new ProgressDialog(getActivity());
            this.pathList = arrayList;
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
            progressDelete.setMessage("Please wait a while...");
            progressDelete.setProgressStyle(0);
            progressDelete.setIndeterminate(false);
            progressDelete.setCancelable(false);
            progressDelete.show();
        }

        @Override
        public Void doInBackground(Void... voidArr) {
            ArrayList arrayList = new ArrayList();
            ArrayList<String> listString = preferenceClass.getListString(Common.gOldPath);
            new File(FolderPath.SDCARD_PATH_DELETE_IMAGE).mkdirs();
            for (int i = 0; i < pathList.size(); i++) {
                File file = new File(pathList.get(i).path);
                File file2 = new File(FolderPath.SDCARD_PATH_DELETE_IMAGE + File.separator + file.getName());
                String parent = file2.getParent();
                try {
                    FileUtils.CopyMoveFile(file, file2);
                    long j = pathList.get(i).id;
                    String str = pathList.get(i).name;
                    String str2 = pathList.get(i).path;
                    listString.add(str2);
                    arrayList.add(new DataFileModel(j, str, str2, file2.getPath(), parent, false));
                    if (pathList.size() - 1 == i) {
                        for (int i2 = 0; i2 < pathList.size(); i2++) {
                            File file3 = new File(pathList.get(i2).path);
                            file3.delete();
                            ContentResolver contentResolver = getActivity().getContentResolver();
                            Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                            contentResolver.delete(uri, "_data='" + file3.getPath() + "'", null);
                            StringBuilder sb = new StringBuilder();
                            sb.append("Remove TO MediaStore Count: ");
                            sb.append(i2);
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
            isSelectedAll = false;
            progressDelete.dismiss();
            new Handler().postDelayed(() -> AppUtilsClass.RefreshImageAlbum(getActivity()), 200L);
            new Handler().postDelayed(() -> AppUtilsClass.RefreshMoment(getActivity()), 500L);
            Toast.makeText(getActivity(), "files moved to trash successfully.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 111 && i2 == -1) {
            if (imageToUploadUri != null) {
                AddImageToGallery(imageToUploadUri.getPath());
            } else {
                Toast.makeText(activity, "Error while capturing Image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public ArrayList<DataFileModel> GetSelectedList() {
        ArrayList<DataFileModel> arrayList = new ArrayList<>();
        int size = ConstantArrayClass.folderAllImageList.size();
        for (int i = 0; i < size; i++) {
            if (((DataFileModel) ConstantArrayClass.folderAllImageList.get(i)).isSelected) {
                arrayList.add((DataFileModel) ConstantArrayClass.folderAllImageList.get(i));
            }
        }
        return arrayList;
    }

    public void SelectAll() {
        int size = ConstantArrayClass.imageAlbumsList.size();
        for (int i = 0; i < size; i++) {
            ConstantArrayClass.imageAlbumsList.get(i).isSelected = true;
        }
        countSelected = ConstantArrayClass.imageAlbumsList.size();
        isSingleSelection = true;
        isSelectAll = true;
        imageFolderAdapter.notifyDataSetChanged();
    }

    public void UnSelectAll() {
        int size = ConstantArrayClass.imageAlbumsList.size();
        for (int i = 0; i < size; i++) {
            ConstantArrayClass.imageAlbumsList.get(i).isSelected = false;
        }
        countSelected = 0;
        isSingleSelection = false;
        isSelectAll = false;
        imageFolderAdapter.notifyDataSetChanged();
    }

    public void SelectFolderImage() {
        int size = ConstantArrayClass.folderAllImageList.size();
        for (int i = 0; i < size; i++) {
            ((DataFileModel) ConstantArrayClass.folderAllImageList.get(i)).isSelected = true;
        }
        countSelected = ConstantArrayClass.folderAllImageList.size();
        isSelectedAll = true;
        isSingleSelection = true;
        imageFolderAdapter.notifyDataSetChanged();
    }

    public class GetFolderAllImageSelectedAsync extends AsyncTask<Void, Void, Void> {
        String BucketID;

        @Override
        protected void onPreExecute() {
        }

        public GetFolderAllImageSelectedAsync(String str) {
            this.BucketID = str;
        }

        @Override
        public Void doInBackground(Void... voidArr) {
            AppUtilsClass.GetFolderAllImageSelectedList(getActivity(), BucketID);
            return null;
        }

        @Override
        public void onPostExecute(Void r1) {
            super.onPostExecute(r1);
            SelectFolderImage();
        }
    }

    public void SecurityDialog(ArrayList<DataFileModel> arrayList) {
        ConstantArrayClass.firstTimeLockDataArray = arrayList;
        final Dialog dialog = new Dialog(getActivity(), R.style.ThemeWithCorners1);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialog_security);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        dialog.findViewById(R.id.rl_yes).setOnClickListener(view -> {
            FolderPath.lock_screen = 1;
            startActivity(new Intent(getActivity(), PrivateActivity.class));
            dialog.dismiss();
        });

        dialog.findViewById(R.id.rl_no).setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }

    public void LockDialog() {
        final Dialog dialog = new Dialog(getActivity(), R.style.ThemeWithCorners1);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialog_lock_image);
        dialog.setTitle(getString(R.string.lock_image));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        dialog.findViewById(R.id.rl_yes).setOnClickListener(view -> {
            LockImages();
            dialog.dismiss();
        });

        dialog.findViewById(R.id.rl_no).setOnClickListener(view -> {
            UnSelectAll();
            dialog.dismiss();
        });
        dialog.show();
    }

    public void LockImages() {
        new ArrayList().clear();
        ArrayList<DataFileModel> GetSelectedList = GetSelectedList();
        if (GetSelectedList.size() <= 0) {
            Toast.makeText(getActivity(), getString(R.string.select_image), Toast.LENGTH_SHORT).show();
        } else if (ExternalStorageHelper.isExternalStorageReadableAndWritable()) {
            progressTag = "FromLock";
            new LockGalleryImageExecute(GetSelectedList).execute(new Void[0]);
        } else {
            Toast.makeText(getActivity(), "not Read Write", Toast.LENGTH_SHORT).show();
        }
    }

    public class LockGalleryImageExecute extends AsyncTask<Void, Void, Void> {
        ArrayList<DataFileModel> pathList;

        public LockGalleryImageExecute(ArrayList<DataFileModel> arrayList) {
            this.pathList = new ArrayList<>();
            this.pathList = arrayList;
            progressLock = new ProgressDialog(getActivity());
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
            new File(FolderPath.SDCARD_PATH_IMAGE).mkdirs();
            for (int i = 0; i < pathList.size(); i++) {
                File file = new File(pathList.get(i).path);
                File file2 = new File(FolderPath.SDCARD_PATH_IMAGE + File.separator + file.getName());
                String parent = file2.getParent();
                try {
                    FileUtils.CopyMoveFile(file, file2);
                    long j = pathList.get(i).id;
                    String str = pathList.get(i).name;
                    String str2 = pathList.get(i).path;
                    listString.add(str2);
                    arrayList.add(new DataFileModel(j, str, str2, file2.getPath(), parent, false));
                    if (pathList.size() - 1 == i) {
                        for (int i2 = 0; i2 < pathList.size(); i2++) {
                            File file3 = new File(pathList.get(i2).path);
                            file3.delete();
                            ContentResolver contentResolver = getActivity().getContentResolver();
                            Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                            contentResolver.delete(uri, "_data='" + file3.getPath() + "'", null);
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
            isSelectedAll = false;
            progressLock.dismiss();

            new Handler().postDelayed(() -> {
                AppUtilsClass.RefreshImageAlbum(getActivity());
                imageFolderAdapter.notifyDataSetChanged();
            }, 200L);

            new Handler().postDelayed(() -> {
                AppUtilsClass.RefreshMoment(getActivity());
                MomentFragment.momentAdapter.notifyDataSetChanged();
            }, 500L);

            Toast.makeText(getActivity(), getString(R.string.files_moved_to_vault_successfully), Toast.LENGTH_SHORT).show();
        }
    }

    private void SetAdapter() {
        new SortingFolderDialog(activity, ConstantArrayClass.imageAlbumsList, albumSortingListener).Sorting(LoginPreferenceUtilsFolder.GetStringData(activity, SortingDataDialog.SortingName), LoginPreferenceUtilsFolder.GetStringData(activity, SortingDataDialog.SortingType));
        imageFolderAdapter = new ImageFolderAdapter(this.activity, getContext(), ConstantArrayClass.imageAlbumsList);
        recyclerView.setAdapter(imageFolderAdapter);
        loader.setVisibility(View.GONE);

        FolderOrientation(activity.getResources().getConfiguration().orientation);

        imageFolderAdapter.setItemClickCallback(new OnClickListener<ArrayList<FolderModel>, Integer, View>() {
            @Override
            public void onLongClickListener(ArrayList<FolderModel> arrayList, Integer num) {
            }

            @Override
            public void onClickListener(final ArrayList<FolderModel> arrayList, final Integer num) {
                callBackAlbumsList = new ArrayList<>();
                callBackAlbumsList = arrayList;
                bpos = num.intValue();
                Common.IdentifyActivity = "ImageFragment";
                Common.strplay = "Photofrag";
                Intent intent = new Intent(getActivity(), ImageSelectActivity.class);
                intent.putExtra(Common.gBucketID, num.intValue());
                intent.putExtra("position", num.intValue());
                intent.putExtra(AppUtilsClass.INTENT_EXTRA_ALBUM, callBackAlbumsList.get(num.intValue()).foldername);
                startActivity(intent);
            }

            @Override
            public void onClickMoreListener(ArrayList<FolderModel> arrayList, Integer num, View view) {
                MoreOptionDialog(arrayList, num, view, getActivity());
            }
        });

    }

    public class ImageFolderAdapter extends RecyclerView.Adapter<ImageFolderAdapter.MyViewHolder> {
        Activity activity;
        ArrayList<FolderModel> folderModels;
        Context context;
        LayoutInflater layoutInflater;
        private OnClickListener<ArrayList<FolderModel>, Integer, View> onClickListener;
        int size;

        @Override
        public long getItemId(int i) {
            return i;
        }

        public ImageFolderAdapter(Activity activity, Context context, ArrayList<FolderModel> arrayList) {
            this.folderModels = arrayList;
            this.context = context;
            this.activity = activity;
            this.layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_album_adapter, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(MyViewHolder myViewHolder, final int i) {
            myViewHolder.imageView.getLayoutParams().width = size;
            myViewHolder.imageView.getLayoutParams().height = size;
            try {
                myViewHolder.tvCount.setText("(" + folderModels.get(i).pathlist.size() + ")");
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                new File(folderModels.get(i).getCoverThumb());
                myViewHolder.textView.setText(folderModels.get(i).getFoldername());
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            try {
                if (folderModels.get(i).getFoldername().equals("Take Photo")) {
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.centerCrop();
                    Glide.with(context).load((Object) folderModels.get(i).getPathlist().get(0)).apply((BaseRequestOptions<?>) requestOptions).into(myViewHolder.imageView);
                } else {
                    Uri fromFile = Uri.fromFile(new File(folderModels.get(i).getPathlist().get(0).path));
                    RequestOptions requestOptions2 = new RequestOptions();
                    requestOptions2.centerCrop();
                    Glide.with(context).load(fromFile).apply((BaseRequestOptions<?>) requestOptions2).into(myViewHolder.imageView);
                }
            } catch (Exception e3) {
                e3.printStackTrace();
            }
            myViewHolder.imageView.setOnClickListener(view -> {
                if (onClickListener != null) {
                    onClickListener.onClickListener(folderModels, Integer.valueOf(i));
                }
            });
            if (ImagePrivateFragment.privateimage == 1) {
                ImagePrivateFragment.privateimage = 0;
                for (int i2 = 0; i2 < folderModels.size(); i2++) {
                    if (folderModels.get(i2).getFoldername().equals("RestoreLockImage")) {
                        myViewHolder.txttag.setText("New");
                        Toast.makeText(activity, "New", Toast.LENGTH_SHORT).show();
                    } else {
                        myViewHolder.txttag.setText("");
                    }
                }
            }
        }

        @Override
        public int getItemCount() {
            return folderModels.size();
        }

        public FolderModel getItem(int i) {
            return folderModels.get(i);
        }

        public void setLayoutParams(int i) {
            this.size = i;
        }

        public void refreshData(ArrayList<FolderModel> arrayList) {
            this.folderModels = arrayList;
            if (activity != null) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        CheckImageFound(ConstantArrayClass.imageAlbumsList);
                        imageFolderAdapter.notifyDataSetChanged();
                    }
                });
            }
        }

        public void setItemClickCallback(OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
        }

        public void RemoveItem(String str) {
            folderModels.remove(str);
            activity.runOnUiThread(this::notifyDataSetChanged);
        }

        public void RemoveItemSingle(String str) {
            for (int i = 0; i < folderModels.size(); i++) {
                if ((folderModels.get(i)).folderPath.contains(str)) {
                    folderModels.remove(i);
                }
            }
            activity.runOnUiThread(this::notifyDataSetChanged);
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            ImageView ivMoreDetail;
            TextView textView;
            TextView tvCount;
            TextView txttag;

            public MyViewHolder(View view) {
                super(view);
                imageView = (ImageView) view.findViewById(R.id.image_view_album_image);
                textView = (TextView) view.findViewById(R.id.text_view_album_name);
                tvCount = (TextView) view.findViewById(R.id.text_view_album_size);
                ivMoreDetail = (ImageView) view.findViewById(R.id.btnMoreDetails);
                txttag = (TextView) view.findViewById(R.id.txttag);
            }
        }
    }

    public void MoreOptionDialog(final ArrayList<FolderModel> arrayList, final Integer num, View view, FragmentActivity fragmentActivity) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(fragmentActivity, R.style.BottomSheetDialogStyle);
        bottomSheetDialog.requestWindowFeature(1);
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        bottomSheetDialog.setContentView(R.layout.dg_folder_more_option);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.findViewById(R.id.rl_rename).setVisibility(View.VISIBLE);
        bottomSheetDialog.findViewById(R.id.rl_rename).setOnClickListener(view2 -> {
            bottomSheetDialog.dismiss();
        });
        bottomSheetDialog.findViewById(R.id.rl_info).setOnClickListener(view2 -> {
            DetailDialog(arrayList, num);
            bottomSheetDialog.dismiss();
        });
        bottomSheetDialog.findViewById(R.id.rl_delete).setOnClickListener(view2 -> {
            DeleteAlbumDialog(arrayList, num);
            bottomSheetDialog.dismiss();
        });
        bottomSheetDialog.findViewById(R.id.rl_lock).setOnClickListener(view2 -> {
            new ArrayList().clear();
            ArrayList<DataFileModel> GetSelectedList = GetSelectedList();
            if (preferenceClass.getInt(Common.gIsLockStatus, 0) == 0) {
                SecurityDialog(GetSelectedList);
            } else if (GetSelectedList.size() >= 1) {
                LockDialog();
            } else {
                Toast.makeText(activity, "Select at least one image.", Toast.LENGTH_SHORT).show();
            }
            bottomSheetDialog.dismiss();
        });
        bottomSheetDialog.findViewById(R.id.txtCancel).setOnClickListener(view2 -> {
            UnSelectAll();
            bottomSheetDialog.dismiss();
        });
        bottomSheetDialog.show();
    }

    public void FolderOrientation(int i) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) activity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
        if (MainActivity.preferenceAlbumUtils.getInt(Common.gAlbumColumns, 0) != 0) {
            int i2 = MainActivity.preferenceAlbumUtils.getInt(Common.gAlbumColumns, 0);
            this.pv = i2;
            if (i2 != 0) {
                this.i2 = i2;
            } else {
                this.i2 = 2;
                MainActivity.albumDivider = 4;
            }
        } else {
            this.i2 = 2;
            MainActivity.albumDivider = 4;
        }
        if (imageFolderAdapter != null) {
            imageFolderAdapter.setLayoutParams(displayMetrics.widthPixels / (i == 1 ? 2 : MainActivity.albumDivider));
        }
        if (i != 1) {
            this.i2 = MainActivity.albumDivider;
        }
        gridLayoutManager = new GridLayoutManager(this.activity, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    public void AddImageToGallery(String str) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("date_modified", Long.valueOf(System.currentTimeMillis()));
        contentValues.put("mime_type", "image/jpg");
        contentValues.put(Utils.mediaPath, str);
        activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
    }

    public void CheckImageFound(ArrayList<FolderModel> arrayList) {
        if (arrayList.size() >= 1) {
            rlPhotoLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            return;
        }
        recyclerView.setVisibility(View.GONE);
        rlPhotoLayout.setVisibility(View.VISIBLE);
    }

    private void SortingCallBack() {
        albumSortingListener = new onCallbackReceive();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (contentObserver != null) {
            stopThread();
            activity.getContentResolver().unregisterContentObserver(contentObserver);
            contentObserver = null;
            Handler handler = this.handler;
            if (handler != null) {
                handler.removeCallbacksAndMessages(null);
                this.handler = null;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ConstantArrayClass.imageAlbumsList = null;
//        if (imageFolderAdapter != null) {
//            imageFolderAdapter.releaseResources();
//        }
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        FolderOrientation(configuration.orientation);
    }

    private void stopThread() {
        Thread thread = this.thread;
        if (thread == null || !thread.isAlive()) {
            return;
        }
        this.thread.interrupt();
        try {
            this.thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public class onCallbackReceive implements AlbumSortingListener {
        onCallbackReceive() {
        }

        @Override
        public void Sorting(ArrayList<FolderModel> arrayList) {
            CheckImageFound(arrayList);
            imageFolderAdapter = new ImageFolderAdapter(activity, getContext(), arrayList);
            recyclerView.setAdapter(imageFolderAdapter);
            loader.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            FolderOrientation(activity.getResources().getConfiguration().orientation);
            imageFolderAdapter.setItemClickCallback(new OnClickListener<ArrayList<FolderModel>, Integer, View>() {
                @Override
                public void onLongClickListener(ArrayList<FolderModel> arrayList2, Integer num) {

                }

                @Override
                public void onClickListener(final ArrayList<FolderModel> arrayList2, final Integer num) {
                    callBackAlbumsList = new ArrayList<>();
                    Common.IdentifyActivity = "ImageFragment";
                    callBackAlbumsList = arrayList2;
                    bpos = num.intValue();
                    Intent intent = new Intent(getActivity(), ImageSelectActivity.class);
                    intent.putExtra(Common.gBucketID, num.intValue());
                    intent.putExtra("position", num.intValue());
                    intent.putExtra(AppUtilsClass.INTENT_EXTRA_ALBUM, callBackAlbumsList.get(num.intValue()).foldername);
//                    activity.startActivityForResult(intent, 2000);
                    startActivity(intent);
                }

                @Override
                public void onClickMoreListener(ArrayList<FolderModel> arrayList2, Integer num, View view) {
                    MoreOptionDialog(arrayList2, num, view, getActivity());
                    new GetFolderAllImageSelectedAsync(arrayList2.get(num.intValue()).getBucket_id()).execute(new Void[0]);
                }
            });
        }
    }
}
