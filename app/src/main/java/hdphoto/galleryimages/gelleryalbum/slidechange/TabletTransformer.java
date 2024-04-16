package hdphoto.galleryimages.gelleryalbum.slidechange;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.View;


public class TabletTransformer extends BaseTransformer {
    static Camera GOFFSET_CAMERA = new Camera();
    static Matrix GOFFSET_MATRIX = new Matrix();
    static float[] GOFFSET_TEMP_FLOAT = new float[2];

    @Override
    public void onTransform(View view, float f) {
        float abs = (f < 0.0f ? 30.0f : -30.0f) * Math.abs(f);
        view.setTranslationX(GetOffSetXForRotation(abs, view.getWidth(), view.getHeight()));
        view.setPivotX(view.getWidth() * 0.5f);
        view.setPivotY(0.0f);
        view.setRotationY(abs);
    }

    protected static float GetOffSetXForRotation(float f, int i, int i2) {
        Matrix matrix = GOFFSET_MATRIX;
        matrix.reset();
        Camera camera = GOFFSET_CAMERA;
        camera.save();
        camera.rotateY(Math.abs(f));
        camera.getMatrix(matrix);
        camera.restore();
        matrix.preTranslate((-i) * 0.5f, (-i2) * 0.5f);
        float f2 = i;
        float f3 = i2;
        matrix.postTranslate(f2 * 0.5f, 0.5f * f3);
        float[] fArr = GOFFSET_TEMP_FLOAT;
        fArr[0] = f2;
        fArr[1] = f3;
        matrix.mapPoints(fArr);
        return (f2 - fArr[0]) * (f > 0.0f ? 1.0f : -1.0f);
    }
}
