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

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import hdphoto.galleryimages.gelleryalbum.listeners.AlbumSortingListener;
import hdphoto.galleryimages.gelleryalbum.listeners.OnClickListener;
import hdphoto.galleryimages.gelleryalbum.activity.PrivateActivity;
import hdphoto.galleryimages.gelleryalbum.activity.MainActivity;
import hdphoto.galleryimages.gelleryalbum.activity.VideoSelectActivity;
import hdphoto.galleryimages.gelleryalbum.sorting.LoginPreferenceUtilsFolder;
import hdphoto.galleryimages.gelleryalbum.sorting.SortingDataDialog;
import hdphoto.galleryimages.gelleryalbum.sorting.SortingFolderDialog;
import hdphoto.galleryimages.gelleryalbum.model.DataFileModel;
import hdphoto.galleryimages.gelleryalbum.model.FolderModel;
import hdphoto.galleryimages.gelleryalbum.constant.Common;
import hdphoto.galleryimages.gelleryalbum.constant.WrapperGridlayoutManager;
import hdphoto.galleryimages.gelleryalbum.utils.Utils;
import hdphoto.galleryimages.gelleryalbum.utils.AppUtilsClass;
import hdphoto.galleryimages.gelleryalbum.utils.ConstantArrayClass;
import hdphoto.galleryimages.gelleryalbum.utils.FileUtils;
import hdphoto.galleryimages.gelleryalbum.utils.FolderPath;
import hdphoto.galleryimages.gelleryalbum.utils.PrefClass;
import hdphoto.galleryimages.gelleryalbum.R;


public class VideoFragment extends BaseFragment {
    static String TAG = "VideoFragment";
    public static VideoFolderAdapter videoFolderAdapter;
    Activity activity;
    AlbumSortingListener albumSortingListener;
    int bpos;
    ArrayList<FolderModel> callBackAlbumsList;
    ContentObserver contentObserver;
    Dialog detailAlbumDialog;
    WrapperGridlayoutManager gridLayoutManager;
    RecyclerView gridView;
    Handler handler;
    int i2;
    Uri imageToUploadUri;
    ProgressBar loader;
    ProgressDialog progressDelete, progressLock;
    String progressTag;
    int pv;
    RelativeLayout rl_NoDataLayout;
    Thread thread;
    TextView txtErrorDisplay;
    View view1;
    int countSelected = 0;
    String folder_path = "";
    boolean isSelectAll = false;
    boolean isSingleSelection = false;
    boolean isSelectedAll = false;


    @Override
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        FolderOrientation(configuration.orientation);
    }

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_video, viewGroup, false);

        activity = getActivity();

        preferenceClass = new PrefClass(getActivity());

        initView(inflate);

        HiddenView();
        SortingCallBack();

        clickListener();
        SetVideoAdapterData();


        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            handler.post(() -> {
                try {
                    AppUtilsClass.ScanVideoAlbumListData(requireActivity());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new ArrayList().addAll(ConstantArrayClass.videoAlbumsList);
            });
        });


        return inflate;
    }

    private void initView(View inflate) {
        view1 = inflate.findViewById(R.id.layout_album_select);
        txtErrorDisplay = inflate.findViewById(R.id.text_view_error);
        loader = inflate.findViewById(R.id.loader);
        gridView = inflate.findViewById(R.id.grid_view_album_select);
        rl_NoDataLayout = inflate.findViewById(R.id.hintMainVideoListLayout);

        txtErrorDisplay.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onResume() {
        FolderOrientation(getResources().getConfiguration().orientation);

        if (videoFolderAdapter != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
//                    AppUtilsClass.RefreshVideoAlbum(requireActivity());
                    AppUtilsClass.ScanVideoAlbumListData(requireActivity());
                    gridView.smoothScrollToPosition(0);
                }
            }, 500);
        }
        Common.strplay = "VideoFragment";
        super.onResume();

    }

    private void clickListener() {
        new Handler().postDelayed(() -> CheckVideoFound(ConstantArrayClass.videoAlbumsList), 1000L);

    }
    public void RenameDialog(final ArrayList<FolderModel> arrayList, final Integer num) {
        final Dialog dialog = new Dialog(getActivity(), R.style.ThemeWithCorners1);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialog_rename_folder);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        ((TextView) dialog.findViewById(R.id.txtTitle)).setText("Rename Folder");
        final EditText editText = (EditText) dialog.findViewById(R.id.edit);
        editText.setText(arrayList.get(num.intValue()).foldername);
        final String obj = editText.getText().toString();

        dialog.findViewById(R.id.rl_yes).setOnClickListener(view -> {
            String trim = editText.getText().toString().trim();
            if (!trim.equals(obj)) {
                new RenameVideoAlbumExecute(arrayList, num, trim).execute(new Void[0]);
                dialog.dismiss();
                return;
            }
            editText.setError("Please Enter Folder Name");
        });

        dialog.findViewById(R.id.rl_no).setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    public class RenameVideoAlbumExecute extends AsyncTask<Void, Void, Void> {
        ArrayList<FolderModel> arrayList;
        String newAlbumFolder;
        int pos;
        ProgressDialog progressDialog;

        public RenameVideoAlbumExecute(ArrayList<FolderModel> arrayList, Integer num, String str) {
            this.arrayList = new ArrayList<>();
            progressDialog = new ProgressDialog(VideoFragment.this.getActivity());
            this.arrayList = arrayList;
            this.pos = num.intValue();
            this.newAlbumFolder = str;
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage(getString(R.string.please_wait_a_while));
            progressDialog.setProgressStyle(0);
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
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
                        String fileExtension = FileUtils.getFileExtension(file2.getPath());
                        if (fileExtension.endsWith("mp4") || fileExtension.endsWith("MP4") || fileExtension.endsWith("3gp") || fileExtension.endsWith("3GP") || fileExtension.endsWith("mkv") || fileExtension.endsWith("MKV")) {
                            file.mkdirs();
                            if (file2.renameTo(file3)) {
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("date_modified", Long.valueOf(System.currentTimeMillis()));
                                contentValues.put("mime_type", "video");
                                contentValues.put(Utils.mediaPath, file3.getPath());
                                activity.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues);
                                activity.getContentResolver().delete(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, "_data='" + file2.getPath() + "'", null);
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
            AppUtilsClass.RefreshVideoAlbum(getActivity());
            new Handler().postDelayed(() -> AppUtilsClass.RefreshMoment(getActivity()), 500L);
            progressDialog.dismiss();
        }
    }

    public void DetailDialog(ArrayList<FolderModel> arrayList, Integer num) {
        detailAlbumDialog = new Dialog(getActivity(), R.style.ThemeWithCorners1);
        detailAlbumDialog.requestWindowFeature(1);
        detailAlbumDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        detailAlbumDialog.setContentView(R.layout.dialog_detail_folder);
        detailAlbumDialog.setTitle("Details");
        detailAlbumDialog.setCancelable(false);
        detailAlbumDialog.setCanceledOnTouchOutside(false);

        TextView textView = (TextView) detailAlbumDialog.findViewById(R.id.txtAlbumName);
        TextView textView2 = (TextView) detailAlbumDialog.findViewById(R.id.txtAlbumPath);
        TextView textView3 = (TextView) detailAlbumDialog.findViewById(R.id.txtAlbumSize);
        TextView textView4 = (TextView) detailAlbumDialog.findViewById(R.id.txtAlbumItem);
        TextView textView5 = (TextView) detailAlbumDialog.findViewById(R.id.txtAlbumCreatedOn);
        RelativeLayout relativeLayout = (RelativeLayout) detailAlbumDialog.findViewById(R.id.rl_no);
        File parentFile = new File(arrayList.get(num.intValue()).getCoverThumb()).getParentFile();
        String[] list = parentFile.list();
        long j = 0;
        if (list != null) {
            int length = list.length;
            for (int i = 0; i < length; i++) {
                File file = new File(parentFile + File.separator + list[i]);
                if (file.exists()) {
                    String fileExtension = FileUtils.getFileExtension(file.getPath());
                    if (fileExtension.endsWith("mp4") || fileExtension.endsWith("MP4") || fileExtension.endsWith("3gp") || fileExtension.endsWith("3GP") || fileExtension.endsWith("mkv") || fileExtension.endsWith("MKV")) {
                        j += file.length();
                    }
                }
            }
        }
        textView.setText(arrayList.get(num.intValue()).foldername + "");
        textView2.setText(new File(arrayList.get(num.intValue()).getCoverThumb()).getParent());
        textView3.setText(FileUtils.convertToHumanReadableSize(getActivity(), j));
        textView4.setText(arrayList.get(num.intValue()).getPathlist().size() + " Files.");

        relativeLayout.setOnClickListener(view -> detailAlbumDialog.dismiss());

        detailAlbumDialog.show();
    }

    public void DeleteVideoAlbumDialog(ArrayList<FolderModel> arrayList, Integer num) {
        final Dialog dialog = new Dialog(getActivity(), R.style.ThemeWithCorners1);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialog_gm_delete_video_folder);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        dialog.findViewById(R.id.rl_yes).setOnClickListener(view -> {
            if (GetSelectedList().size() > 0) {
                progressTag = "FromDelete";
                new DeleteTrashVideoExecute(GetSelectedList()).execute(new Void[0]);
            } else {
                Toast.makeText(getActivity(), "Select Video.", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        });

        dialog.findViewById(R.id.rl_no).setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }

    public class DeleteTrashVideoExecute extends AsyncTask<Void, Void, Void> {
        ArrayList<DataFileModel> pathList;

        public DeleteTrashVideoExecute(ArrayList<DataFileModel> arrayList) {
            this.pathList = new ArrayList<>();
            progressDelete = new ProgressDialog(getActivity());
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
            new File(FolderPath.SDCARD_PATH_DELETE_VIDEO).mkdirs();
            for (int i = 0; i < pathList.size(); i++) {
                File file = new File(pathList.get(i).path);
                File file2 = new File(FolderPath.SDCARD_PATH_DELETE_VIDEO + File.separator + file.getName());
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
                            Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
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
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    AppUtilsClass.RefreshVideoAlbum(getActivity());
                }
            }, 300L);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    AppUtilsClass.RefreshMoment(getActivity());
                }
            }, 500L);
            Toast.makeText(getActivity(), "Files moved to trash successfully.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 111 && i2 == -1) {
            Uri uri = imageToUploadUri;
            if (uri != null) {
                AddImageToGallery(uri.getPath());
            } else {
                Toast.makeText(getActivity(), "Error while capturing Image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void SelectSingleImage(ArrayList<FolderModel> arrayList, int i) {
        isSingleSelection = true;
        arrayList.get(i).isSelected = !arrayList.get(i).isSelected;
        if (arrayList.get(i).isSelected) {
            countSelected++;
        } else {
            countSelected--;
        }
        if (countSelected <= 0) {
            isSingleSelection = false;
            isSelectAll = false;
            HiddenView();
        }
        videoFolderAdapter.notifyDataSetChanged();
    }

    public ArrayList<DataFileModel> GetSelectedList() {
        ArrayList<DataFileModel> arrayList = new ArrayList<>();
        int size = ConstantArrayClass.folderAllVideoList.size();
        for (int i = 0; i < size; i++) {
            if (((DataFileModel) ConstantArrayClass.folderAllVideoList.get(i)).isSelected) {
                arrayList.add((DataFileModel) ConstantArrayClass.folderAllVideoList.get(i));
            }
        }
        return arrayList;
    }

    public void SelectAll() {
        int size = ConstantArrayClass.videoAlbumsList.size();
        for (int i = 0; i < size; i++) {
            ConstantArrayClass.videoAlbumsList.get(i).isSelected = true;
        }
        countSelected = ConstantArrayClass.videoAlbumsList.size();
        isSingleSelection = true;
        isSelectAll = true;
        VisibleView();
        videoFolderAdapter.notifyDataSetChanged();
    }

    public void UnSelectAll() {
        int size = ConstantArrayClass.videoAlbumsList.size();
        for (int i = 0; i < size; i++) {
            ConstantArrayClass.videoAlbumsList.get(i).isSelected = false;
        }
        countSelected = 0;
        isSingleSelection = false;
        isSelectAll = false;
        videoFolderAdapter.notifyDataSetChanged();
        HiddenView();
    }

    public void SelectAllVideo() {
        int size = ConstantArrayClass.folderAllVideoList.size();
        for (int i = 0; i < size; i++) {
            ((DataFileModel) ConstantArrayClass.folderAllVideoList.get(i)).isSelected = true;
        }
        countSelected = ConstantArrayClass.folderAllVideoList.size();
        isSelectedAll = true;
        isSingleSelection = true;
        videoFolderAdapter.notifyDataSetChanged();
    }


    public class GetFolderAllVideoSelectedAsync extends AsyncTask<Void, Void, Void> {
        String bbid;

        @Override
        protected void onPreExecute() {
        }

        public GetFolderAllVideoSelectedAsync(String str) {
            this.bbid = str;
        }

        @Override
        public Void doInBackground(Void... voidArr) {
            AppUtilsClass.GetFolderAllVideoSelectedList(getActivity(), bbid);
            return null;
        }

        @Override
        public void onPostExecute(Void r1) {
            super.onPostExecute(r1);
            SelectAllVideo();
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
        dialog.setContentView(R.layout.dialog_lock_video);
        dialog.setTitle(getString(R.string.lock_video));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        dialog.findViewById(R.id.rl_yes).setOnClickListener(view -> {
            LockVideos();
            dialog.dismiss();
        });

        dialog.findViewById(R.id.rl_no).setOnClickListener(view -> {
            UnSelectAll();
            dialog.dismiss();
        });

        dialog.show();
    }

    public void LockVideos() {
        new ArrayList().clear();
        ArrayList<DataFileModel> GetSelectedList = GetSelectedList();
        if (GetSelectedList.size() > 0) {
            progressTag = "FromLock";
            new LockVideoExecute(GetSelectedList).execute(new Void[0]);
            return;
        }
        Toast.makeText(getActivity(), "Select Video.", Toast.LENGTH_SHORT).show();
    }


    public class LockVideoExecute extends AsyncTask<Void, Void, Void> {
        ArrayList<DataFileModel> pathList;

        public LockVideoExecute(ArrayList<DataFileModel> arrayList) {
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
            ArrayList arrayList = new ArrayList();
            ArrayList<String> listString = preferenceClass.getListString(Common.gOldPath);
            new File(FolderPath.SDCARD_PATH_VIDEO).mkdirs();
            for (int i = 0; i < pathList.size(); i++) {
                File file = new File(pathList.get(i).path);
                File file2 = new File(FolderPath.SDCARD_PATH_VIDEO + File.separator + file.getName());
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
                            Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
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

            new Handler().postDelayed(() -> AppUtilsClass.RefreshVideoAlbum(getActivity()), 300L);

            VideoFragment.videoFolderAdapter.notifyDataSetChanged();

            new Handler().postDelayed(() -> AppUtilsClass.RefreshMoment(getActivity()), 500L);

            MomentFragment.momentAdapter.notifyDataSetChanged();

            Toast.makeText(getActivity(), R.string.files_moved_to_vault_successfully, Toast.LENGTH_SHORT).show();
        }
    }

    private void SetVideoAdapterData() {
        new SortingFolderDialog(getActivity(), ConstantArrayClass.videoAlbumsList, albumSortingListener).Sorting(LoginPreferenceUtilsFolder.GetStringData(getActivity(), SortingDataDialog.SortingName), LoginPreferenceUtilsFolder.GetStringData(getActivity(), SortingDataDialog.SortingType));
        videoFolderAdapter = new VideoFolderAdapter(getActivity(), getContext(), ConstantArrayClass.videoAlbumsList);
        gridView.setAdapter(videoFolderAdapter);
        gridView.setVisibility(View.VISIBLE);
        loader.setVisibility(View.GONE);

        FolderOrientation(getResources().getConfiguration().orientation);

        videoFolderAdapter.setItemClickCallback(new OnClickListener<ArrayList<FolderModel>, Integer, View>() {
            @Override
            public void onLongClickListener(ArrayList<FolderModel> arrayList, Integer num) {
            }

            @Override
            public void onClickListener(final ArrayList<FolderModel> arrayList, final Integer num) {
                callBackAlbumsList = new ArrayList<>();
                callBackAlbumsList = arrayList;
                bpos = num.intValue();
                Common.strplay = "VideoFragment";
                Common.IdentifyActivity = TAG;
                Intent intent = new Intent(getActivity(), VideoSelectActivity.class);
                intent.putExtra(Common.gBucketID, num.intValue());
                intent.putExtra("position", num.intValue());
                intent.putExtra(AppUtilsClass.INTENT_EXTRA_ALBUM, callBackAlbumsList.get(num.intValue()).foldername);
//                activity.startActivityForResult(intent, 2000);
                startActivity(intent);
            }

            @Override
            public void onClickMoreListener(ArrayList<FolderModel> arrayList, Integer num, View view) {
                MoreOptionDialog(arrayList, num, view, getActivity());
//                new GetFolderAllVideoSelectedAsync(arrayList.get(num.intValue()).getBucket_id()).execute(new Void[0]);
            }
        });
    }

    public class VideoFolderAdapter extends RecyclerView.Adapter<VideoFolderAdapter.MyViewHolder> {
        Activity activity;
        ArrayList<FolderModel> folderModels;
        Context context;
        LayoutInflater layoutInflater;
        OnClickListener<ArrayList<FolderModel>, Integer, View> onClickListener;
        int size;

        @Override
        public long getItemId(int i) {
            return i;
        }

        public VideoFolderAdapter(Activity activity, Context context, ArrayList<FolderModel> arrayList) {
            this.folderModels = arrayList;
            this.context = context;
            this.activity = activity;
            this.layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new MyViewHolder(layoutInflater.inflate(R.layout.layout_album_adapter, (ViewGroup) null));
        }

        @Override
        public void onBindViewHolder(MyViewHolder myViewHolder, final int i) {
            myViewHolder.imageView.getLayoutParams().width = this.size;
            myViewHolder.imageView.getLayoutParams().height = this.size;
            try {
                myViewHolder.tvCount.setText("(" + folderModels.get(i).pathlist.size() + ")");
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                myViewHolder.textView.setText(folderModels.get(i).getFoldername());
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            try {
                Uri fromFile = Uri.fromFile(new File(folderModels.get(i).getPathlist().get(0).path));
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.override(200, 200);
                requestOptions.centerCrop();
                Glide.with(context).load(fromFile).apply((BaseRequestOptions<?>) requestOptions).into(myViewHolder.imageView);
            } catch (Exception e3) {
                e3.printStackTrace();
            }

            if (Common.strplay.equals("VideoFragment")) {
                myViewHolder.rl_play.setVisibility(View.VISIBLE);
            }

            myViewHolder.imageView.setOnClickListener(view -> {
                if (onClickListener != null) {
                    onClickListener.onClickListener(folderModels, Integer.valueOf(i));
                }

            });
        }

        @Override
        public int getItemCount() {
                return folderModels.size();
        }

        public FolderModel getItem(int i) {
            return folderModels.get(i);
        }

        public void setLayoutParams(int i) {
            size = i;
        }
        public void refreshData(ArrayList<FolderModel> arrayList) {
            this.folderModels = arrayList;
            if (activity != null) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        CheckVideoFound(ConstantArrayClass.videoAlbumsList);
                        videoFolderAdapter.notifyDataSetChanged();
                    }
                });
            }
        }

        public void setItemClickCallback(OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
        }


        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            ImageView ivMoreDetail;
            RelativeLayout rl_play;
            TextView textView;
            TextView tvCount;

            public MyViewHolder(View view) {
                super(view);
                imageView = (ImageView) view.findViewById(R.id.image_view_album_image);
                textView = (TextView) view.findViewById(R.id.text_view_album_name);
                tvCount = (TextView) view.findViewById(R.id.text_view_album_size);
                ivMoreDetail = (ImageView) view.findViewById(R.id.btnMoreDetails);
                rl_play = (RelativeLayout) view.findViewById(R.id.rel_play);
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
            RenameDialog(arrayList, num);
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.findViewById(R.id.rl_info).setOnClickListener(view2 -> {
            DetailDialog(arrayList, num);
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.findViewById(R.id.rl_delete).setOnClickListener(view2 -> {
            DeleteVideoAlbumDialog(arrayList, num);
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
                Toast.makeText(activity, "Select at least one video.", Toast.LENGTH_SHORT).show();
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
        ((WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
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
        if (videoFolderAdapter != null) {
            videoFolderAdapter.setLayoutParams(displayMetrics.widthPixels / (i == 1 ? this.i2 : MainActivity.albumDivider));
        }
        if (i != 1) {
            this.i2 = MainActivity.albumDivider;
        }
        gridLayoutManager = new WrapperGridlayoutManager(this.activity, this.i2);
        this.gridView.setLayoutManager(gridLayoutManager);
    }

    public void AddImageToGallery(String str) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("date_modified", Long.valueOf(System.currentTimeMillis()));
        contentValues.put("mime_type", "video/*");
        contentValues.put(Utils.mediaPath, str);
        activity.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues);
    }

    public void CheckVideoFound(ArrayList<FolderModel> arrayList) {
        if (arrayList.size() >= 1) {
            rl_NoDataLayout.setVisibility(View.GONE);
            gridView.setVisibility(View.VISIBLE);
            return;
        }
        gridView.setVisibility(View.GONE);
        rl_NoDataLayout.setVisibility(View.VISIBLE);
    }

    private void SortingCallBack() {
        albumSortingListener = new videoAlbumSorting();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (contentObserver != null) {
            StopThread();
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
        ConstantArrayClass.videoAlbumsList = null;
//        if (videoFolderAdapter != null) {
//            videoFolderAdapter.releaseResources();
//        }
    }

    private void StopThread() {
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

    private void HiddenView() {
//        MainActivity.ic_sort.setVisibility(View.GONE);
        MainActivity.ivSelectAll.setVisibility(View.GONE);
        MainActivity.ivAddAlbum.setVisibility(View.GONE);
    }

    private void VisibleView() {
        MainActivity.ivSelectAll.setVisibility(View.GONE);
        MainActivity.ivAddAlbum.setVisibility(View.GONE);
//        MainActivity.ic_sort.setVisibility(View.GONE);
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

    public class videoAlbumSorting implements AlbumSortingListener {
        videoAlbumSorting() {
        }

        @Override
        public void Sorting(ArrayList<FolderModel> arrayList) {
//            CheckVideoFound(arrayList);
            videoFolderAdapter = new VideoFolderAdapter(getActivity(), getContext(), arrayList);
            gridView.setAdapter(videoFolderAdapter);
            loader.setVisibility(View.GONE);
            gridView.setVisibility(View.VISIBLE);
            FolderOrientation(activity.getResources().getConfiguration().orientation);
            videoFolderAdapter.setItemClickCallback(new OnClickListener<ArrayList<FolderModel>, Integer, View>() {
                @Override
                public void onLongClickListener(ArrayList<FolderModel> arrayList2, Integer num) {
                }

                @Override
                public void onClickListener(ArrayList<FolderModel> arrayList2, Integer num) {
                    callBackAlbumsList = new ArrayList<>();
                    Common.IdentifyActivity = TAG;
                    callBackAlbumsList = arrayList2;
                    bpos = num.intValue();
                    if (isSingleSelection) {
                        SelectSingleImage(callBackAlbumsList, num.intValue());
                        return;
                    }
                    Intent intent = new Intent(getActivity(), VideoSelectActivity.class);
                    intent.putExtra(Common.gBucketID, num.intValue());
                    intent.putExtra("position", num.intValue());
                    intent.putExtra(AppUtilsClass.INTENT_EXTRA_ALBUM, callBackAlbumsList.get(num.intValue()).foldername);
                    activity.startActivityForResult(intent, 2000);

                }

                @Override
                public void onClickMoreListener(ArrayList<FolderModel> arrayList2, Integer num, View view) {
                    MoreOptionDialog(arrayList2, num, view, getActivity());
                    new GetFolderAllVideoSelectedAsync(arrayList2.get(num.intValue()).getBucket_id()).execute(new Void[0]);
                }
            });
        }
    }
}
