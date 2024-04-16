package hdphoto.galleryimages.gelleryalbum.tool;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import hdphoto.galleryimages.gelleryalbum.R;

import java.util.ArrayList;
import java.util.List;


public class EditingToolsAdapter extends RecyclerView.Adapter<EditingToolsAdapter.ViewHolder> {
    OnItemSelected mOnItemSelected;
    List<ToolModel> mToolList;


    public interface OnItemSelected {
        void onToolSelected(ToolType eMToolType);
    }

    public EditingToolsAdapter(OnItemSelected onItemSelected) {
        mToolList = new ArrayList();
        this.mOnItemSelected = onItemSelected;
        mToolList.add(new ToolModel("Shape", R.drawable.ic_oval, ToolType.SHAPE));
        mToolList.add(new ToolModel("Text", R.drawable.ic_text, ToolType.TEXT));
        mToolList.add(new ToolModel("Eraser", R.drawable.ic_eraser, ToolType.ERASER));
        mToolList.add(new ToolModel("Filter", R.drawable.ic_photo_filter, ToolType.FILTER));
        mToolList.add(new ToolModel("Emoji", R.drawable.ic_insert_emoticon, ToolType.EMOJI));
        mToolList.add(new ToolModel("Sticker", R.drawable.ic_sticker, ToolType.STICKER));
    }

    public class ToolModel {
        private int mToolIcon;
        private String mToolName;
        private ToolType mToolType;

        ToolModel(String str, int i, ToolType eMToolType) {
            this.mToolName = str;
            this.mToolIcon = i;
            this.mToolType = eMToolType;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_em_editing_tools_adapter, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        ToolModel toolModel = this.mToolList.get(i);
        viewHolder.txtTool.setText(toolModel.mToolName);
        viewHolder.imgToolIcon.setImageResource(toolModel.mToolIcon);
    }

    @Override
    public int getItemCount() {
        return mToolList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgToolIcon;
        TextView txtTool;

        ViewHolder(View view) {
            super(view);
            imgToolIcon = (ImageView) view.findViewById(R.id.imgToolIcon);
            txtTool = (TextView) view.findViewById(R.id.txtTool);
            view.setOnClickListener(view2 -> mOnItemSelected.onToolSelected(mToolList.get(getLayoutPosition()).mToolType));
        }
    }
}
