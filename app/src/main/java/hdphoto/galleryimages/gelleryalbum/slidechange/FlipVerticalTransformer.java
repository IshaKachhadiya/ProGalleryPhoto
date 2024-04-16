package hdphoto.galleryimages.gelleryalbum.slidechange;

import android.view.View;


public class FlipVerticalTransformer extends BaseTransformer {
    @Override
    public void onTransform(View view, float f) {
        float f2 = f * (-180.0f);
        view.setAlpha((f2 > 90.0f || f2 < -90.0f) ? 0.0f : 1.0f);
        view.setPivotX(view.getWidth() * 0.5f);
        view.setPivotY(view.getHeight() * 0.5f);
        view.setRotationX(f2);
    }

    @Override
    public void onPostTransform(View view, float f) {
        super.onPostTransform(view, f);
        if (f <= -0.5f || f >= 0.5f) {
            view.setVisibility(View.INVISIBLE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }
}
