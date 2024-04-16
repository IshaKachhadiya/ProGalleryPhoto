package hdphoto.galleryimages.gelleryalbum.slidechange;

import android.view.View;


public class RotateUpTransformer extends BaseTransformer {
    private static final float ROTATION_UP_MOD = -15.0f;

    @Override
    public boolean isPagingEnabled() {
        return true;
    }

    @Override
    public void onTransform(View view, float f) {
        float f2 = f * ROTATION_UP_MOD;
        view.setPivotX(view.getWidth() * 0.5f);
        view.setPivotY(0.0f);
        view.setTranslationX(0.0f);
        view.setRotation(f2);
    }
}
