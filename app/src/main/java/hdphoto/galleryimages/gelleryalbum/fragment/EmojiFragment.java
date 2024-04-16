package hdphoto.galleryimages.gelleryalbum.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import hdphoto.galleryimages.gelleryalbum.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import java.util.ArrayList;


public class EmojiFragment extends BottomSheetDialogFragment {
    private EmojiListener gEmojiListener;
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

    
    public interface EmojiListener {
        void onEmojiClick(String str);
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
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 5));
        recyclerView.setAdapter(new EmojiAdapter());
    }

    public void setEmojiListener(EmojiListener emojiListener) {
        this.gEmojiListener = emojiListener;
    }

    
    public class EmojiAdapter extends RecyclerView.Adapter<EmojiAdapter.ViewHolder> {
        ArrayList<String> emojisList;

        public EmojiAdapter() {
            emojisList = EmojiFragment.GetEmojiList(getActivity());
        }

        @Override 
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_em_emoji, viewGroup, false));
        }

        @Override 
        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            viewHolder.txtEmoji.setText(emojisList.get(i));
        }

        @Override 
        public int getItemCount() {
            return emojisList.size();
        }

        
        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView txtEmoji;

            ViewHolder(View view) {
                super(view);
                txtEmoji = (TextView) view.findViewById(R.id.txtEmoji);
                view.setOnClickListener(view2 -> {
                    if (gEmojiListener != null) {
                        gEmojiListener.onEmojiClick(emojisList.get(getLayoutPosition()));
                    }
                    dismiss();
                });
            }
        }
    }

    public static ArrayList<String> GetEmojiList(Context context) {
        ArrayList<String> arrayList = new ArrayList<>();
        for (String str : context.getResources().getStringArray(R.array.photo_editor_emoji)) {
            arrayList.add(ConvertEmoji(str));
        }
        return arrayList;
    }

    private static String ConvertEmoji(String str) {
        try {
            return new String(Character.toChars(Integer.parseInt(str.substring(2), 16)));
        } catch (NumberFormatException unused) {
            return "";
        }
    }
}
