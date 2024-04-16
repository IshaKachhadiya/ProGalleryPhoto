package hdphoto.galleryimages.gelleryalbum.slidechange;

import android.view.View;


public class StackTransformer extends BaseTransformer {
    @Override
    public void onTransform(View view, float f) {
        view.setTranslationX(f >= 0.0f ? (-view.getWidth()) * f : 0.0f);
    }
}
