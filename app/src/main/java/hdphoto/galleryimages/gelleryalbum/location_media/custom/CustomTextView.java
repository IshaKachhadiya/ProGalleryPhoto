package hdphoto.galleryimages.gelleryalbum.location_media.custom;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import hdphoto.galleryimages.gelleryalbum.R;


public class CustomTextView extends AppCompatTextView {
    public CustomTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(attributeSet);
    }

    public CustomTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(attributeSet);
    }

    public CustomTextView(Context context) {
        super(context);
        init(null);
    }

    private void init(AttributeSet attributeSet) {
        if (attributeSet != null) {
            getContext().obtainStyledAttributes(attributeSet, R.styleable.CustomTextView).recycle();
        }
    }
}
