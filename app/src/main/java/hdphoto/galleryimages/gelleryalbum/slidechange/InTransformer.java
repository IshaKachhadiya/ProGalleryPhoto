package hdphoto.galleryimages.gelleryalbum.slidechange;

import android.view.View;


public class InTransformer extends BaseTransformer {
    @Override
    public boolean isPagingEnabled() {
        return true;
    }

    @Override
    public void onTransform(View view, float f) {
        view.setPivotX(f > 0.0f ? 0.0f : view.getWidth());
        view.setPivotY(0.0f);
        view.setRotationY(f * (-90.0f));
    }
}
