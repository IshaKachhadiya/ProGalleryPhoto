package hdphoto.galleryimages.gelleryalbum.slidechange;

import android.view.View;


public class AccordionTransformer extends BaseTransformer {
    @Override
    public void onTransform(View view, float f) {
        int i = (f > 0.0f ? 1 : (f == 0.0f ? 0 : -1));
        char c = i > 0 ? (char) 1 : i == 0 ? (char) 0 : (char) 65535;
        view.setPivotX(c >= 0 ? view.getWidth() : 0.0f);
        view.setScaleX(c < 0 ? f + 1.0f : 1.0f - f);
    }
}
