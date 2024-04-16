package hdphoto.galleryimages.gelleryalbum.location_media.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.iten.tenoku.ad.AdShow;
import com.iten.tenoku.ad.HandleClick.HandleClick;
import com.iten.tenoku.utils.AdUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;

import hdphoto.galleryimages.gelleryalbum.R;
import hdphoto.galleryimages.gelleryalbum.activity.BaseActivity;
import hdphoto.galleryimages.gelleryalbum.databinding.ActivityMediaColorBinding;
import hdphoto.galleryimages.gelleryalbum.location_media.adapter.FavoriteAdapter;
import hdphoto.galleryimages.gelleryalbum.location_media.common.Constant;
import hdphoto.galleryimages.gelleryalbum.location_media.event.DeleteEvent;
import hdphoto.galleryimages.gelleryalbum.location_media.event.DisplayDeleteEvent;
import hdphoto.galleryimages.gelleryalbum.location_media.model.AlbumData;
import hdphoto.galleryimages.gelleryalbum.location_media.model.PictureData;
import hdphoto.galleryimages.gelleryalbum.location_media.rx.RxBus;
import hdphoto.galleryimages.gelleryalbum.location_media.util.PreferencesManager;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MediaColorActivity extends BaseActivity {
    ActivityMediaColorBinding binding;
    private FavoriteAdapter favoriteAdapter;
    ArrayList<Object> photoList = new ArrayList<>();
    ArrayList<Object> photoMainList = new ArrayList<>();
    private int selectColor = -1;

    public static void DeleteEvent(Throwable th) {
    }

    public static void DisplayDeleteEvent(Throwable th) {
    }


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        binding = ActivityMediaColorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        intView();
        DisplayDeleteEvent();
        DeleteEvent();
    }

    private void intView() {
        binding.progressBar.setVisibility(View.VISIBLE);
        selectColor = ContextCompat.getColor(this, R.color.white);
        new Thread(new Runnable() {
            @Override
            public void run() {
                getColorImages();
            }
        }).start();
        initListener();
    }


    public void getColorImages() {
        Constant.IMAGES_COLOR_GET.booleanValue();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setDataList();
            }
        });
    }

    public void setDataList() {
        ArrayList arrayList;
        ArrayList arrayList2 = new ArrayList(Constant.photoColorList);
        if (Constant.deleteList != null && Constant.deleteList.size() != 0) {
            Iterator<String> it = Constant.deleteList.iterator();
            while (it.hasNext()) {
                String next = it.next();
                Iterator it2 = arrayList2.iterator();
                while (true) {
                    if (it2.hasNext()) {
                        PictureData pictureData = (PictureData) it2.next();
                        if (pictureData.getFilePath().equals(next)) {
                            arrayList2.remove(pictureData);
                            break;
                        }
                    }
                }
            }
        }
        if (Constant.copyMoveImageList != null && Constant.copyMoveImageList.size() != 0) {
            arrayList2.addAll(Constant.copyMoveImageList);
            Collections.sort(arrayList2, new Comparator() {
                @Override
                public int compare(Object obj, Object obj2) {
                    int compare;
                    compare = Long.compare(((PictureData) obj2).getDate(), ((PictureData) obj).getDate());
                    return compare;
                }
            });
        }
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance();
        String format = simpleDateFormat.format(Long.valueOf(calendar.getTimeInMillis()));
        calendar.add(5, -1);
        String format2 = simpleDateFormat.format(Long.valueOf(calendar.getTimeInMillis()));
        Iterator it3 = arrayList2.iterator();
        while (it3.hasNext()) {
            PictureData pictureData2 = (PictureData) it3.next();
            String format3 = simpleDateFormat.format(Long.valueOf(pictureData2.getDate()));
            if (format3.equals(format)) {
                format3 = "Today";
            } else if (format3.equals(format2)) {
                format3 = "Yesterday";
            }
            if (linkedHashMap.containsKey(format3)) {
                arrayList = (ArrayList) linkedHashMap.get(format3);
                if (arrayList == null) {
                    arrayList = new ArrayList();
                }
            } else {
                arrayList = new ArrayList();
            }
            arrayList.add(pictureData2);
            linkedHashMap.put(format3, arrayList);
        }
        ArrayList arrayList3 = new ArrayList(linkedHashMap.keySet());
        for (int i = 0; i < arrayList3.size(); i++) {
            ArrayList<PictureData> arrayList4 = (ArrayList) linkedHashMap.get(arrayList3.get(i));
            if (arrayList4 != null && arrayList4.size() != 0) {
                AlbumData albumData = new AlbumData();
                albumData.setTitle((String) arrayList3.get(i));
                albumData.setPictureData(arrayList4);
                photoList.add(albumData);
                photoMainList.add(albumData);
                photoList.addAll(arrayList4);
                photoMainList.addAll(arrayList4);
            }
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                binding.progressBar.setVisibility(View.GONE);
                setAdapter();            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        AdShow.getInstance(this).ShowNativeAd(findViewById(R.id.nativeSmall).findViewById(R.id.native_ad_layout), AdUtils.NativeType.NATIVE_BANNER);
    }

    private void initListener() {
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.icColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ColorPickerDialogBuilder.with(MediaColorActivity.this).setTitle(getString(R.string.ChooseColor)).initialColor(selectColor).wheelType(ColorPickerView.WHEEL_TYPE.FLOWER).density(12).setOnColorSelectedListener(new OnColorSelectedListener() { // from class: com.gallery.photos.pro.activity.MediaColorActivity$$ExternalSyntheticLambda6
                    @Override
                    public void onColorSelected(int i) {
                        Log.d("Data", "onClick: ====>" + i);
                    }
                }).setPositiveButton(getString(R.string.OK), new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, Integer[] numArr) {
                        selectColor = i;
                        getColorImageList();
                    }
                }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).build().show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        AdShow.getInstance(MediaColorActivity.this).ShowAd(new HandleClick() {
            @Override
            public void Show(boolean adShow) {
                MediaColorActivity.super.onBackPressed();
            }
        }, AdUtils.ClickType.BACK_CLICK);
    }

    public void getColorImageList() {
        photoList.clear();
        if (favoriteAdapter != null) {
            favoriteAdapter.notifyDataSetChanged();
        }
        binding.progressBar.setVisibility(View.VISIBLE);
        AlbumData albumData = null;
        ArrayList arrayList = new ArrayList();
        String substring = Integer.toHexString(selectColor).substring(2);
        int red = Color.red(selectColor);
        int green = Color.green(selectColor);
        int blue = Color.blue(selectColor);
        int alpha = Color.alpha(selectColor);
        String substring2 = substring.length() > 3 ? substring.substring(0, 3) : substring;
        for (int i = 0; i < photoMainList.size(); i++) {
            if (photoMainList.get(i) != null) {
                if (photoMainList.get(i) instanceof AlbumData) {
                    if (albumData != null && arrayList.size() != 0) {
                        photoList.add(albumData);
                        photoList.addAll(arrayList);
                        arrayList.clear();
                    }
                    albumData = (AlbumData) photoMainList.get(i);
                } else if (photoMainList.get(i) instanceof PictureData) {
                    PictureData pictureData = (PictureData) photoMainList.get(i);
                    ArrayList arrayList2 = new ArrayList();
                    arrayList2.addAll(pictureData.getColorList());
                    Iterator it = arrayList2.iterator();
                    while (true) {
                        if (it.hasNext()) {
                            if (isSimilar((Integer) it.next(), red, green, blue, alpha)) {
                                arrayList.add(pictureData);
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                }
            }
        }
        if (albumData != null && arrayList.size() != 0) {
            photoList.add(albumData);
            photoList.addAll(arrayList);
        }
        binding.progressBar.setVisibility(View.GONE);
        if (favoriteAdapter != null) {
            favoriteAdapter.notifyDataSetChanged();
        } else {
            setAdapter();
        }
        setEmpty();
    }

    private boolean isSimilar(Integer num, int i, int i2, int i3, int i4) {
        return ((((((float) Math.abs(i - Color.red(num.intValue()))) / 255.0f) + (((float) Math.abs(i2 - Color.green(num.intValue()))) / 255.0f)) + (((float) Math.abs(i3 - Color.blue(num.intValue()))) / 255.0f)) / 3.0f) * 100.0f < 15.0f;
    }

    private void setAdapter() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager((Context) this, PreferencesManager.getImageGrid(this), RecyclerView.VERTICAL, false);
        binding.favRecycler.setLayoutManager(gridLayoutManager);
        favoriteAdapter = new FavoriteAdapter(this, photoList, new FavoriteAdapter.OnSelectPicture() { // from class: com.gallery.photos.pro.activity.MediaColorActivity.3
            @Override
            public void onSelectPicture(int i) {
                openImageShow(i);
            }

            @Override
            public void onLongClickPicture(int i) {
                openImageShow(i);
            }
        });
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() { // from class: com.gallery.photos.pro.activity.MediaColorActivity.4
            @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
            public int getSpanSize(int i) {
                if (favoriteAdapter.getItemViewType(i) == 1) {
                    return PreferencesManager.getImageGrid(MediaColorActivity.this);
                }
                return 1;
            }
        });
        binding.favRecycler.setAdapter(favoriteAdapter);
        setEmpty();
    }


    public void openImageShow(int i) {
        if (photoList.get(i) instanceof PictureData) {
            int i2 = -1;
            ArrayList arrayList = new ArrayList();
            for (int i3 = 0; i3 < photoList.size(); i3++) {
                if (photoList.get(i3) instanceof PictureData) {
                    arrayList.add((PictureData) photoList.get(i3));
                    if (i == i3) {
                        i2 = arrayList.size() - 1;
                    }
                }
            }
            Constant.displayImageList = new ArrayList<>();
            Constant.displayImageList.addAll(arrayList);

            Intent intent = new Intent(this, ImageShowActivity.class);
            intent.putExtra("pos", i2);
            startActivity(intent);
        }
    }

    private void setEmpty() {
        ArrayList<Object> arrayList = photoList;
        if (arrayList != null && arrayList.size() != 0) {
            binding.favRecycler.setVisibility(View.VISIBLE);
            binding.loutNoData.setVisibility(View.GONE);
            return;
        }
        binding.favRecycler.setVisibility(View.GONE);
        binding.loutNoData.setVisibility(View.VISIBLE);
    }

    private void DisplayDeleteEvent() {
        RxBus.getInstance().addSubscription(this, RxBus.getInstance().toObservable(DisplayDeleteEvent.class).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).distinctUntilChanged().subscribe(new Action1() { // from class: com.gallery.photos.pro.activity.MediaColorActivity$$ExternalSyntheticLambda8
            @Override
            public void call(Object obj) {
                disevent((DisplayDeleteEvent) obj);
            }
        }, new Action1() {
            @Override
            public void call(Object obj) {
                MediaColorActivity.DisplayDeleteEvent((Throwable) obj);
            }
        }));
    }


    public void disevent(DisplayDeleteEvent displayDeleteEvent) {
        if (displayDeleteEvent.getDeleteList() == null || displayDeleteEvent.getDeleteList().size() == 0) {
            return;
        }
        deleteDataInList(displayDeleteEvent.getDeleteList());
    }

    private void DeleteEvent() {
        RxBus.getInstance().addSubscription(this, RxBus.getInstance().toObservable(DeleteEvent.class).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).distinctUntilChanged().subscribe(new Action1() { // from class: com.gallery.photos.pro.activity.MediaColorActivity$$ExternalSyntheticLambda0
            @Override
            public void call(Object obj) {
                deleteevent((DeleteEvent) obj);
            }
        }, new Action1() {
            @Override
            public  void call(Object obj) {
                MediaColorActivity.DeleteEvent((Throwable) obj);
            }
        }));
    }


    public void deleteevent(DeleteEvent deleteEvent) {
        if (deleteEvent.getPos() == -1 || deleteEvent.getDeleteList() == null || deleteEvent.getDeleteList().size() == 0) {
            return;
        }
        deleteDataInList(deleteEvent.getDeleteList());
    }

    private void deleteDataInList(ArrayList<String> arrayList) {
        ArrayList<Object> arrayList2 = photoList;
        if (arrayList2 != null && arrayList2.size() != 0) {
            for (int i = 0; i < arrayList.size(); i++) {
                int i2 = 0;
                while (i2 < photoList.size()) {
                    if ((photoList.get(i2) instanceof PictureData) && ((PictureData) photoList.get(i2)).getFilePath().equalsIgnoreCase(arrayList.get(i))) {
                        boolean z = i2 != 0 ? photoList.get(i2 - 1) instanceof AlbumData : false;
                        boolean z2 = i2 < photoList.size() + (-2) ? photoList.get(i2 + 1) instanceof AlbumData : false;
                        if (z && z2) {
                            photoList.remove(i2);
                            photoList.remove(i2 - 1);
                        } else if (i2 == photoList.size() - 1) {
                            photoList.remove(i2);
                            if (z) {
                                photoList.remove(i2 - 1);
                            }
                        } else {
                            photoList.remove(i2);
                        }
                        if (i2 != 0) {
                            i2--;
                        }
                        if (i == arrayList.size() - 1) {
                            break;
                        }
                    }
                    i2++;
                }
            }
            if (favoriteAdapter != null) {
                favoriteAdapter.notifyDataSetChanged();
            }
            ArrayList<Object> arrayList3 = photoList;
            if (arrayList3 != null && arrayList3.size() != 0) {
                binding.favRecycler.setVisibility(View.VISIBLE);
                binding.loutNoData.setVisibility(View.GONE);
            } else {
                binding.favRecycler.setVisibility(View.GONE);
                binding.loutNoData.setVisibility(View.VISIBLE);
            }
        }
        ArrayList<Object> arrayList4 = photoMainList;
        if (arrayList4 == null || arrayList4.size() == 0) {
            return;
        }
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            int i4 = 0;
            while (i4 < photoMainList.size()) {
                if ((photoMainList.get(i4) instanceof PictureData) && ((PictureData) photoMainList.get(i4)).getFilePath().equalsIgnoreCase(arrayList.get(i3))) {
                    boolean z3 = i4 != 0 ? photoMainList.get(i4 - 1) instanceof AlbumData : false;
                    boolean z4 = i4 < photoMainList.size() + (-2) ? photoMainList.get(i4 + 1) instanceof AlbumData : false;
                    if (z3 && z4) {
                        photoMainList.remove(i4);
                        photoMainList.remove(i4 - 1);
                    } else if (i4 == photoMainList.size() - 1) {
                        photoMainList.remove(i4);
                        if (z3) {
                            photoMainList.remove(i4 - 1);
                        }
                    } else {
                        photoMainList.remove(i4);
                    }
                    if (i4 != 0) {
                        i4--;
                    }
                    if (i3 == arrayList.size() - 1) {
                        break;
                    }
                }
                i4++;
            }
        }
    }
}
