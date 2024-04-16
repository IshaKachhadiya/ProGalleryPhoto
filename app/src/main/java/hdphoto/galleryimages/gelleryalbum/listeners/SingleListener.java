package hdphoto.galleryimages.gelleryalbum.listeners;

import android.view.View;


public interface SingleListener<T, V, P> {
    void onSingleCallback(T t, V v, P p, View view);
}
