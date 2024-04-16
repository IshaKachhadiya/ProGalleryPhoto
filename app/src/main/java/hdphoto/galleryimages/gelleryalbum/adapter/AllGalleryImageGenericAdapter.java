package hdphoto.galleryimages.gelleryalbum.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import java.util.ArrayList;


public abstract class AllGalleryImageGenericAdapter<T> extends BaseAdapter {
    Activity activity;
    ArrayList<T> arrayList;
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<T> mStringFilterList;
    int size;

    @Override 
    public long getItemId(int i) {
        return i;
    }

    public AllGalleryImageGenericAdapter(Activity activity, Context context, ArrayList<T> arrayList) {
        this.arrayList = arrayList;
        this.mStringFilterList = arrayList;
        this.context = context;
        this.activity = activity;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override 
    public int getCount() {
        return arrayList.size();
    }

    @Override 
    public T getItem(int i) {
        return arrayList.get(i);
    }

    public void setLayoutParams(int i) {
        size = i;
    }

    public void releaseResources() {
        arrayList = null;
        context = null;
        activity = null;
    }
}
