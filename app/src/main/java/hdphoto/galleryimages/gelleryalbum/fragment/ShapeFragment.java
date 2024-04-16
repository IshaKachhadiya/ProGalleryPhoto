package hdphoto.galleryimages.gelleryalbum.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import hdphoto.galleryimages.gelleryalbum.R;
import hdphoto.galleryimages.gelleryalbum.adapter.EdColorPickerAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;



public class ShapeFragment extends BottomSheetDialogFragment implements SeekBar.OnSeekBarChangeListener {
    Properties gProperties;

    public interface Properties {
        void onColorChanged(int i);
        void onOpacityChanged(int i);
        void onShapeSizeChanged(int i);
    }

    @Override 
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override 
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @Override 
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.dg_fragment_shapes_bs, viewGroup, false);
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.shapeColors);

        ((SeekBar) view.findViewById(R.id.shapeOpacity)).setOnSeekBarChangeListener(this);
        ((SeekBar) view.findViewById(R.id.shapeSize)).setOnSeekBarChangeListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);
        EdColorPickerAdapter eMColorPickerAdapter = new EdColorPickerAdapter(getActivity());
        eMColorPickerAdapter.setOnColorPickerClickListener(i -> {
            if (gProperties != null) {
                dismiss();
                gProperties.onColorChanged(i);
            }
        });
        recyclerView.setAdapter(eMColorPickerAdapter);
    }

    public void setPropertiesChangeListener(Properties properties) {
        this.gProperties = properties;
    }

    @Override 
    public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
        Properties properties;
        int id = seekBar.getId();
        if (id == R.id.shapeOpacity) {
            Properties properties2 = this.gProperties;
            if (properties2 != null) {
                properties2.onOpacityChanged(i);
            }
        } else if (id != R.id.shapeSize || (properties = this.gProperties) == null) {
        } else {
            properties.onShapeSizeChanged(i);
        }
    }
}
