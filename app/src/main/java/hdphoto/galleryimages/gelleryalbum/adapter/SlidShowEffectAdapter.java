package hdphoto.galleryimages.gelleryalbum.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import hdphoto.galleryimages.gelleryalbum.R;
import hdphoto.galleryimages.gelleryalbum.constant.Common;


public class SlidShowEffectAdapter extends ArrayAdapter<String> {
    final Activity activity;
    final String[] maintitle;

    public SlidShowEffectAdapter(Activity activity, String[] strArr) {
        super(activity, (int) R.layout.layout_slide_show_effect, strArr);
        this.activity = activity;
        this.maintitle = strArr;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View inflate = LayoutInflater.from(activity).inflate(R.layout.layout_slide_show_effect, viewGroup, false);
        ImageView imageView = (ImageView) inflate.findViewById(R.id.icon);
        ((TextView) inflate.findViewById(R.id.text1)).setText(maintitle[i]);
        if (Common.gEff_Pos == i) {
            imageView.setImageResource(R.drawable.ic_lock_ring_fill);
        } else {
            imageView.setImageResource(R.drawable.ic_lock_ring_null);
        }
        return inflate;
    }
}
