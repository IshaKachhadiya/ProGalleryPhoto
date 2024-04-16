package hdphoto.galleryimages.gelleryalbum.slidechange;

import android.view.View;


public class ZoomOutSlideTransformer extends BaseTransformer {
    static float ZOOM_OUT_MIN_ALPHA = 0.5f;
    static float ZOOM_OUT_MIN_SCALE = 0.85f;

    @Override
    public void onTransform(View view, float f) {
        if (f >= -1.0f || f <= 1.0f) {
            float height = view.getHeight();
            float width = view.getWidth();
            float max = Math.max(ZOOM_OUT_MIN_SCALE, 1.0f - Math.abs(f));
            float f2 = 1.0f - max;
            float f3 = (height * f2) / 2.0f;
            float f4 = (f2 * width) / 2.0f;
            view.setPivotY(height * ZOOM_OUT_MIN_ALPHA);
            view.setPivotX(width * ZOOM_OUT_MIN_ALPHA);
            if (f < 0.0f) {
                view.setTranslationX(f4 - (f3 / 2.0f));
            } else {
                view.setTranslationX((-f4) + (f3 / 2.0f));
            }
            view.setScaleX(max);
            view.setScaleY(max);
            float f5 = ZOOM_OUT_MIN_ALPHA;
            view.setAlpha((((max - ZOOM_OUT_MIN_SCALE) / 0.14999998f) * f5) + f5);
        }
    }
}
