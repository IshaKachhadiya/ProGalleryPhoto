package hdphoto.galleryimages.gelleryalbum.filter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import hdphoto.galleryimages.gelleryalbum.R;
import ja.burhanrashid52.photoeditor.PhotoFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class FilterViewAdapter extends RecyclerView.Adapter<FilterViewAdapter.ViewHolder> {
    FilterListener mFilterListener;
    List<Pair<String, PhotoFilter>> mFilterPairList = new ArrayList();

    public FilterViewAdapter(FilterListener eMFilterListener) {
        this.mFilterListener = eMFilterListener;
        SetFilters();
    }

    @Override 
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_filter_view_adapter, viewGroup, false));
    }

    @Override 
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Pair<String, PhotoFilter> pair = mFilterPairList.get(i);
        viewHolder.imgFilterView.setImageBitmap(GetBitmapFromAsset(viewHolder.itemView.getContext(), (String) pair.first));
        viewHolder.txtFilterName.setText(((PhotoFilter) pair.second).name().replace("_", " "));
    }

    @Override 
    public int getItemCount() {
        return this.mFilterPairList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFilterView;
        TextView txtFilterName;

        ViewHolder(View view) {
            super(view);
            imgFilterView = (ImageView) view.findViewById(R.id.imgFilterView);
            txtFilterName = (TextView) view.findViewById(R.id.txtFilterName);
            view.setOnClickListener(view2 -> mFilterListener.onFilterSelected((PhotoFilter) mFilterPairList.get(getLayoutPosition()).second));
        }
    }

    private Bitmap GetBitmapFromAsset(Context context, String str) {
        try {
            return BitmapFactory.decodeStream(context.getAssets().open(str));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void SetFilters() {
        mFilterPairList.add(new Pair<>("filters/original.jpg", PhotoFilter.NONE));
        mFilterPairList.add(new Pair<>("filters/auto_fix.png", PhotoFilter.AUTO_FIX));
        mFilterPairList.add(new Pair<>("filters/brightness.png", PhotoFilter.BRIGHTNESS));
        mFilterPairList.add(new Pair<>("filters/contrast.png", PhotoFilter.CONTRAST));
        mFilterPairList.add(new Pair<>("filters/documentary.png", PhotoFilter.DOCUMENTARY));
        mFilterPairList.add(new Pair<>("filters/dual_tone.png", PhotoFilter.DUE_TONE));
        mFilterPairList.add(new Pair<>("filters/fill_light.png", PhotoFilter.FILL_LIGHT));
        mFilterPairList.add(new Pair<>("filters/fish_eye.png", PhotoFilter.FISH_EYE));
        mFilterPairList.add(new Pair<>("filters/grain.png", PhotoFilter.GRAIN));
        mFilterPairList.add(new Pair<>("filters/gray_scale.png", PhotoFilter.GRAY_SCALE));
        mFilterPairList.add(new Pair<>("filters/lomish.png", PhotoFilter.LOMISH));
        mFilterPairList.add(new Pair<>("filters/negative.png", PhotoFilter.NEGATIVE));
        mFilterPairList.add(new Pair<>("filters/posterize.png", PhotoFilter.POSTERIZE));
        mFilterPairList.add(new Pair<>("filters/saturate.png", PhotoFilter.SATURATE));
        mFilterPairList.add(new Pair<>("filters/sepia.png", PhotoFilter.SEPIA));
        mFilterPairList.add(new Pair<>("filters/sharpen.png", PhotoFilter.SHARPEN));
        mFilterPairList.add(new Pair<>("filters/temprature.png", PhotoFilter.TEMPERATURE));
        mFilterPairList.add(new Pair<>("filters/tint.png", PhotoFilter.TINT));
        mFilterPairList.add(new Pair<>("filters/vignette.png", PhotoFilter.VIGNETTE));
        mFilterPairList.add(new Pair<>("filters/cross_process.png", PhotoFilter.CROSS_PROCESS));
        mFilterPairList.add(new Pair<>("filters/b_n_w.png", PhotoFilter.BLACK_WHITE));
        mFilterPairList.add(new Pair<>("filters/flip_horizental.png", PhotoFilter.FLIP_HORIZONTAL));
        mFilterPairList.add(new Pair<>("filters/flip_vertical.png", PhotoFilter.FLIP_VERTICAL));
        mFilterPairList.add(new Pair<>("filters/rotate.png", PhotoFilter.ROTATE));
    }
}
