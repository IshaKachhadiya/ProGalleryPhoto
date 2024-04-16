package hdphoto.galleryimages.gelleryalbum.slidechange;

import android.view.View;


public class DrawerTransformer extends BaseTransformer {
    @Override
    public void onTransform(View view, float f) {
        if (f <= 0.0f) {
            view.setTranslationX(0.0f);
        } else if (f <= 0.0f || f > 1.0f) {
        } else {
            view.setTranslationX(((-view.getWidth()) / 2) * f);
        }
    }
}
