package hdphoto.galleryimages.gelleryalbum.slidechange;

import android.view.View;


public class RotateDownTransformer extends BaseTransformer {
    static final float ROTATION_DOWN_MODE = -15.0f;

    @Override
    public boolean isPagingEnabled() {
        return true;
    }

    @Override
    public void onTransform(View view, float f) {
        view.setPivotX(view.getWidth() * 0.5f);
        view.setPivotY(view.getHeight());
        view.setRotation(f * ROTATION_DOWN_MODE * (-1.25f));
    }
}
