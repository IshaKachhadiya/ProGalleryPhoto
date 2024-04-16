package hdphoto.galleryimages.gelleryalbum.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import hdphoto.galleryimages.gelleryalbum.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class StickerFragment extends BottomSheetDialogFragment {
    StickerListener gStickerListener;
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onSlide(View view, float f) {
        }

        @Override
        public void onStateChanged(View view, int i) {
            if (i == 5) {
                dismiss();
            }
        }
    };
    
    public interface StickerListener {
        void onStickerClick(Bitmap bitmap);
    }

    public void setStickerListener(StickerListener stickerListener) {
        this.gStickerListener = stickerListener;
    }

    @SuppressLint({"RestrictedApi", "ResourceType"})
    @Override
    public void setupDialog(Dialog dialog, int i) {
        super.setupDialog(dialog, i);
        View inflate = View.inflate(getContext(), R.layout.dg_fragment_emoji_bs, null);
        dialog.setContentView(inflate);
        CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) ((View) inflate.getParent()).getLayoutParams()).getBehavior();
        if (behavior != null && (behavior instanceof BottomSheetBehavior)) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
        ((View) inflate.getParent()).setBackgroundColor(getResources().getColor(17170445));
        RecyclerView recyclerView = (RecyclerView) inflate.findViewById(R.id.rvEmoji);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.setAdapter(new StickerAdapter());
    }

    @Override 
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
    }

    
    public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.ViewHolder> {
        int[] stickerList = {R.drawable.b1, R.drawable.b2, R.drawable.b3, R.drawable.b4, R.drawable.b5, R.drawable.b6, R.drawable.b7, R.drawable.b8, R.drawable.b9, R.drawable.b10, R.drawable.b11, R.drawable.b12, R.drawable.b13, R.drawable.b14, R.drawable.b15, R.drawable.b16, R.drawable.b17, R.drawable.b18, R.drawable.b19, R.drawable.c1, R.drawable.c2, R.drawable.c3, R.drawable.c4, R.drawable.c5, R.drawable.g1, R.drawable.g2, R.drawable.g3, R.drawable.g4, R.drawable.g5, R.drawable.g6, R.drawable.g7, R.drawable.g8, R.drawable.g9, R.drawable.g10, R.drawable.g11, R.drawable.g12, R.drawable.g13, R.drawable.g14, R.drawable.hc1, R.drawable.hc2, R.drawable.hc3, R.drawable.hc4, R.drawable.hc5, R.drawable.hc6, R.drawable.n1, R.drawable.n2, R.drawable.n3, R.drawable.n4, R.drawable.n5, R.drawable.t1, R.drawable.t2, R.drawable.t3, R.drawable.t4, R.drawable.t5, R.drawable.t6, R.drawable.t7, R.drawable.t8, R.drawable.t9, R.drawable.t10, R.drawable.t11, R.drawable.t12, R.drawable.t13, R.drawable.t14, R.drawable.t15, R.drawable.t16, R.drawable.t17, R.drawable.t18, R.drawable.t19, R.drawable.t20, R.drawable.t21, R.drawable.t22};

        public StickerAdapter() {
        }

        @Override 
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_sticker_bs, viewGroup, false));
        }

        @Override 
        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            viewHolder.imgSticker.setImageResource(stickerList[i]);
        }

        @Override 
        public int getItemCount() {
            return stickerList.length;
        }

        
        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imgSticker;

            ViewHolder(View view) {
                super(view);
                imgSticker = (ImageView) view.findViewById(R.id.imgSticker);
                view.setOnClickListener(view2 -> {
                    if (gStickerListener != null) {
                        gStickerListener.onStickerClick(BitmapFactory.decodeResource(getResources(), stickerList[getLayoutPosition()]));
                    }
                    dismiss();
                });
            }
        }
    }

    private String GetEmojiByUnicode(int i) {
        return new String(Character.toChars(i));
    }
}
