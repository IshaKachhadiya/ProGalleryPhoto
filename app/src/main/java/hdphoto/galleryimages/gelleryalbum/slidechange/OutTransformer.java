package hdphoto.galleryimages.gelleryalbum.slidechange;

import android.view.View;


public class OutTransformer extends BaseTransformer {
    @Override
    public boolean isPagingEnabled() {
        return true;
    }

    @Override
    public void onTransform(View view, float f) {
        view.setPivotX(f < 0.0f ? view.getWidth() : 0.0f);
        view.setPivotY(view.getHeight() * 0.5f);
        view.setRotationY(f * 90.0f);
    }
}
