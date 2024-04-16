package hdphoto.galleryimages.gelleryalbum.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import hdphoto.galleryimages.gelleryalbum.R;
import java.util.ArrayList;
import java.util.List;


public class EdColorPickerAdapter extends RecyclerView.Adapter<EdColorPickerAdapter.ViewHolder> {
    List<Integer> colorPickerColors;
    Context context;
    LayoutInflater inflater;
    OnColorPickerClickListener onColorPickerClickListener;


    public interface OnColorPickerClickListener {
        void onColorPickerClickListener(int i);
    }

    EdColorPickerAdapter(Context context, List<Integer> list) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.colorPickerColors = list;
    }

    public EdColorPickerAdapter(Context context) {
        this(context, getDefaultColors(context));
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override 
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(inflater.inflate(R.layout.layout_colorpicker_adapter, viewGroup, false));
    }

    @Override 
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.colorPickerView.setBackgroundColor(colorPickerColors.get(i).intValue());
    }

    @Override 
    public int getItemCount() {
        return colorPickerColors.size();
    }

    public void setOnColorPickerClickListener(OnColorPickerClickListener onColorPickerClickListener) {
        this.onColorPickerClickListener = onColorPickerClickListener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        View colorPickerView;

        public ViewHolder(View view) {
            super(view);
            this.colorPickerView = view.findViewById(R.id.color_picker_view);
            colorPickerView.setOnClickListener(view2 -> {
                if (onColorPickerClickListener != null) {
                    onColorPickerClickListener.onColorPickerClickListener(colorPickerColors.get(getAdapterPosition()).intValue());
                }
            });
        }
    }

    public static List<Integer> getDefaultColors(Context context) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(Integer.valueOf(ContextCompat.getColor(context, R.color.blue_color_picker)));
        arrayList.add(Integer.valueOf(ContextCompat.getColor(context, R.color.brown_color_picker)));
        arrayList.add(Integer.valueOf(ContextCompat.getColor(context, R.color.green_color_picker)));
        arrayList.add(Integer.valueOf(ContextCompat.getColor(context, R.color.orange_color_picker)));
        arrayList.add(Integer.valueOf(ContextCompat.getColor(context, R.color.red_color_picker)));
        arrayList.add(Integer.valueOf(ContextCompat.getColor(context, R.color.black)));
        arrayList.add(Integer.valueOf(ContextCompat.getColor(context, R.color.red_orange_color_picker)));
        arrayList.add(Integer.valueOf(ContextCompat.getColor(context, R.color.sky_blue_color_picker)));
        arrayList.add(Integer.valueOf(ContextCompat.getColor(context, R.color.violet_color_picker)));
        arrayList.add(Integer.valueOf(ContextCompat.getColor(context, R.color.white)));
        arrayList.add(Integer.valueOf(ContextCompat.getColor(context, R.color.yellow_color_picker)));
        arrayList.add(Integer.valueOf(ContextCompat.getColor(context, R.color.yellow_green_color_picker)));
        return arrayList;
    }
}
