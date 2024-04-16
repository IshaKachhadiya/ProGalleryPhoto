package hdphoto.galleryimages.gelleryalbum.slidechange;

import android.view.View;


public class BackgroundToForegroundTransformer extends BaseTransformer {
    @Override
    public void onTransform(View view, float f) {
        float height = view.getHeight();
        float width = view.getWidth();
        float min = min(f >= 0.0f ? Math.abs(1.0f - f) : 1.0f, 0.5f);
        view.setScaleX(min);
        view.setScaleY(min);
        view.setPivotX(width * 0.5f);
        view.setPivotY(height * 0.5f);
        view.setTranslationX(f < 0.0f ? width * f : (-width) * f * 0.25f);
    }
}
