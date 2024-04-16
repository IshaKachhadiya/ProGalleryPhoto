package hdphoto.galleryimages.gelleryalbum.location_media.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iten.tenoku.ad.AdShow;
import com.iten.tenoku.ad.HandleClick.HandleClick;
import com.iten.tenoku.utils.AdUtils;

import java.util.ArrayList;

import hdphoto.galleryimages.gelleryalbum.R;
import hdphoto.galleryimages.gelleryalbum.activity.BaseActivity;
import hdphoto.galleryimages.gelleryalbum.databinding.ActivityLocationImageBinding;
import hdphoto.galleryimages.gelleryalbum.location_media.adapter.LocationAdapter;
import hdphoto.galleryimages.gelleryalbum.location_media.common.Constant;
import hdphoto.galleryimages.gelleryalbum.location_media.event.DeleteEvent;
import hdphoto.galleryimages.gelleryalbum.location_media.event.DisplayDeleteEvent;
import hdphoto.galleryimages.gelleryalbum.location_media.event.LocationEvent;
import hdphoto.galleryimages.gelleryalbum.location_media.model.LocationImageData;
import hdphoto.galleryimages.gelleryalbum.location_media.model.PictureData;
import hdphoto.galleryimages.gelleryalbum.location_media.rx.RxBus;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class LocationImageActivity extends BaseActivity {
    private ActivityLocationImageBinding binding;
    private LocationAdapter locationAdapter;
    ArrayList<LocationImageData> locationImageLists = new ArrayList<>();

    public static  void DeleteEventClick(Throwable th) {
    }

    public static  void DisplayDeleteEventclick(Throwable th) {
    }

    public static  void LocationEventClick(Throwable th) {
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        binding = ActivityLocationImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
        DisplayDeleteEvent();
        DeleteEvent();
        LocationEvent();
    }
    private void LocationEvent() {
        RxBus.getInstance().addSubscription(this, RxBus.getInstance().toObservable(LocationEvent.class).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).distinctUntilChanged().subscribe(new Action1() { // from class: com.gallery.photos.pro.activity.LocationImageActivity$$ExternalSyntheticLambda3
            @Override
            public void call(Object obj) {
                locationImageLists.clear();
                locationImageLists.addAll(Constant.locationImageDatas);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        binding.loadProgress.setVisibility(View.GONE);
                        initAdapter();
                    }
                });
            }
        }, new Action1() {
            @Override
            public void call(Object obj) {
                LocationEventClick((Throwable) obj);
            }
        }));
    }

    private void DisplayDeleteEvent() {
        RxBus.getInstance().addSubscription(this, RxBus.getInstance().toObservable(DisplayDeleteEvent.class).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).distinctUntilChanged().subscribe(new Action1() { // from class: com.gallery.photos.pro.activity.LocationImageActivity$$ExternalSyntheticLambda6
            @Override
            public  void call(Object obj) {
                disevent((DisplayDeleteEvent) obj);
            }
        }, new Action1() {
            @Override
            public  void call(Object obj) {
                DisplayDeleteEventclick((Throwable) obj);
            }
        }));
    }

    public  void disevent(DisplayDeleteEvent displayDeleteEvent) {
        if (displayDeleteEvent.getDeleteList() == null || displayDeleteEvent.getDeleteList().size() == 0) {
            return;
        }
        updateDeleteImageData(displayDeleteEvent.getDeleteList());
    }

    private void DeleteEvent() {
        RxBus.getInstance().addSubscription(this, RxBus.getInstance().toObservable(DeleteEvent.class).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).distinctUntilChanged().subscribe(new Action1() { // from class: com.gallery.photos.pro.activity.LocationImageActivity$$ExternalSyntheticLambda1
            @Override
            public void call(Object obj) {
               mainclick((DeleteEvent) obj);
            }
        }, new Action1() {
            @Override
            public void call(Object obj) {
                DeleteEventClick((Throwable) obj);
            }
        }));
    }

    @Override
    protected void onResume() {
        super.onResume();
        AdShow.getInstance(this).ShowNativeAd(findViewById(R.id.nativeSmall).findViewById(R.id.native_ad_layout), AdUtils.NativeType.NATIVE_BANNER);
    }
    public  void mainclick(DeleteEvent deleteEvent) {
        LocationImageData locationImageData;
        ArrayList<PictureData> pictureData;
        if (deleteEvent.getDeleteList() == null || deleteEvent.getDeleteList().size() == 0) {
            return;
        }
        ArrayList<String> deleteList = deleteEvent.getDeleteList();
        ArrayList<LocationImageData> arrayList = this.locationImageLists;
        if (arrayList == null || arrayList.size() == 0) {
            return;
        }
        for (int i = 0; i < deleteList.size(); i++) {
            int i2 = 0;
            while (i2 < this.locationImageLists.size()) {
                if (this.locationImageLists.get(i2) != null && (pictureData = (locationImageData = this.locationImageLists.get(i2)).getPictureData()) != null && pictureData.size() != 0) {
                    int i3 = 0;
                    while (true) {
                        if (i3 >= pictureData.size()) {
                            break;
                        } else if (deleteList.get(i).equalsIgnoreCase(pictureData.get(i3).getFilePath())) {
                            pictureData.remove(i3);
                            if (pictureData.size() == 0) {
                                this.locationImageLists.remove(i2);
                                if (i2 != 0) {
                                    i2--;
                                }
                            } else {
                                locationImageData.setPictureData(pictureData);
                                this.locationImageLists.set(i2, locationImageData);
                            }
                        } else {
                            i3++;
                        }
                    }
                }
                i2++;
            }
        }
        LocationAdapter locationAdapter = this.locationAdapter;
        if (locationAdapter != null) {
            locationAdapter.notifyDataSetChanged();
        }
        ArrayList<LocationImageData> arrayList2 = this.locationImageLists;
        if (arrayList2 != null && arrayList2.size() != 0) {
            binding.locationRecycler.setVisibility(View.VISIBLE);
            binding.location.setVisibility(View.GONE);
            binding.loutNoData.setVisibility(View.GONE);
            return;
        }
        binding.locationRecycler.setVisibility(View.GONE);
        binding.location.setVisibility(View.GONE);
        binding.loutNoData.setVisibility(View.VISIBLE);
    }

    private void updateDeleteImageData(ArrayList<String> arrayList) {
        LocationImageData locationImageData;
        ArrayList<PictureData> pictureData;
        ArrayList<LocationImageData> arrayList2 = this.locationImageLists;
        if (arrayList2 == null || arrayList2.size() == 0) {
            return;
        }
        for (int i = 0; i < arrayList.size(); i++) {
            int i2 = 0;
            while (i2 < this.locationImageLists.size()) {
                if (this.locationImageLists.get(i2) != null && (pictureData = (locationImageData = this.locationImageLists.get(i2)).getPictureData()) != null && pictureData.size() != 0) {
                    int i3 = 0;
                    while (true) {
                        if (i3 >= pictureData.size()) {
                            break;
                        } else if (arrayList.get(i).equalsIgnoreCase(pictureData.get(i3).getFilePath())) {
                            pictureData.remove(i3);
                            if (pictureData.size() == 0) {
                                this.locationImageLists.remove(i2);
                                if (i2 != 0) {
                                    i2--;
                                }
                            } else {
                                locationImageData.setPictureData(pictureData);
                                this.locationImageLists.set(i2, locationImageData);
                            }
                        } else {
                            i3++;
                        }
                    }
                }
                i2++;
            }
        }
        if (locationAdapter != null) {
            locationAdapter.notifyDataSetChanged();
        }
        ArrayList<LocationImageData> arrayList3 = this.locationImageLists;
        if (arrayList3 != null && arrayList3.size() != 0) {
            binding.locationRecycler.setVisibility(View.VISIBLE);
            binding.location.setVisibility(View.VISIBLE);
            binding.loutNoData.setVisibility(View.GONE);
            return;
        }
        binding.locationRecycler.setVisibility(View.GONE);
        binding.location.setVisibility(View.GONE);
        binding.loutNoData.setVisibility(View.VISIBLE);
    }

    private void initView() {
        if (Constant.LOCATION_GET.booleanValue()) {
            locationImageLists.clear();
            locationImageLists.addAll(Constant.locationImageDatas);
            binding.loadProgress.setVisibility(View.GONE);
            initAdapter();
            return;
        }
        this.binding.loadProgress.setVisibility(View.VISIBLE);

        binding.ivBack.setOnClickListener(v->{
            onBackPressed();
        });
    }

    private void initAdapter() {
        this.locationAdapter = new LocationAdapter(this, this.locationImageLists, new LocationAdapter.OnSelectLocation() {
            @Override
            public void onClickLocation(int i) {
                startActivity(new Intent(LocationImageActivity.this, LocationImageListActivity.class).putExtra("data", locationImageLists.get(i)));
            }
        });
        binding.locationRecycler.setLayoutManager(new GridLayoutManager((Context) this, 2, RecyclerView.VERTICAL, false));
        binding.locationRecycler.setAdapter(this.locationAdapter);
        ArrayList<LocationImageData> arrayList = this.locationImageLists;
        if (arrayList != null && arrayList.size() != 0) {
            binding.locationRecycler.setVisibility(View.VISIBLE);
            binding.location.setVisibility(View.GONE);
            binding.loutNoData.setVisibility(View.GONE);
            return;
        }
        binding.locationRecycler.setVisibility(View.GONE);
        binding.location.setVisibility(View.GONE);
        binding.loutNoData.setVisibility(View.VISIBLE);
    }

    private void initListener() {

    }

    @Override
    public void onBackPressed() {
        AdShow.getInstance(LocationImageActivity.this).ShowAd(new HandleClick() {
            @Override
            public void Show(boolean adShow) {
                LocationImageActivity.super.onBackPressed();
            }
        }, AdUtils.ClickType.BACK_CLICK);
    }
}