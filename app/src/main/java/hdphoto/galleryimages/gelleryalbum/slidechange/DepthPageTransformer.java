package hdphoto.galleryimages.gelleryalbum.slidechange;

import android.view.View;


public class DepthPageTransformer extends BaseTransformer {
    private static final float MIN_SCALE = 0.75f;

    @Override
    public boolean isPagingEnabled() {
        return true;
    }

    @Override
    public void onTransform(View view, float f) {
        if (f <= 0.0f) {
            view.setTranslationX(0.0f);
            view.setScaleX(1.0f);
            view.setScaleY(1.0f);
        } else if (f <= 1.0f) {
            float abs = ((1.0f - Math.abs(f)) * 0.25f) + 0.75f;
            view.setAlpha(1.0f - f);
            view.setPivotY(view.getHeight() * 0.5f);
            view.setTranslationX(view.getWidth() * (-f));
            view.setScaleX(abs);
            view.setScaleY(abs);
        }
    }
}
