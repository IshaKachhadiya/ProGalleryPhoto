package hdphoto.galleryimages.gelleryalbum.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hdphoto.galleryimages.gelleryalbum.R;
import hdphoto.galleryimages.gelleryalbum.model.LangModel;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.MyViewHolder> {

    private List<LangModel> languages;
    Context context;
    private IClickLanguage iClickLanguage;

    public interface IClickLanguage {
        void onClick(LangModel languageModel);
    }

    public LanguageAdapter(Context context, List<LangModel> languages, IClickLanguage iClickLanguage) {
        this.context = context;
        this.languages = languages;
        this.iClickLanguage = iClickLanguage;
    }

    @NonNull
    @Override
    public LanguageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LanguageAdapter.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_language, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull LanguageAdapter.MyViewHolder holder, int position) {
        LangModel langModel = languages.get(position);
        holder.bind(langModel);

        holder.ll_lang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelectLanguage(langModel.isoLanguage);
                iClickLanguage.onClick(langModel);
                notifyDataSetChanged();
            }
        });
        holder.ll_lang.setBackgroundResource(R.drawable.bottomsheetfolderbg);
        if (langModel.getCheck().booleanValue()) {
            holder.ll_lang.setBackgroundResource(R.drawable.lang_bg_select);
        } else {
            holder.ll_lang.setBackgroundResource(R.drawable.bottomsheetfolderbg);
        }
    }

    @Override
    public int getItemCount() {
        return languages.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_lang;
        LinearLayout ll_lang;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_lang = itemView.findViewById(R.id.tv_name);
            ll_lang = itemView.findViewById(R.id.ll_lang);

        }

        public void bind(LangModel languageModel) {
//            this.ivAvatar.setImageDrawable(context.getDrawable(languageModel.getImage()));
            this.tv_lang.setText(languageModel.getLanguageName());
            if (languageModel.getCheck().booleanValue()) {
                ll_lang.setVisibility(View.VISIBLE);
                ll_lang.setBackgroundResource(R.drawable.lang_bg_select);
            } else {
                ll_lang.setBackgroundResource(R.drawable.dbox_btn_bg);
            }
        }
    }

    public void setSelectLanguage(String str) {
        for (LangModel languageModel : this.languages) {
            if (languageModel.getIsoLanguage().equals(str)) {
                languageModel.setCheck(true);
            } else {
                languageModel.setCheck(false);
            }
        }
        notifyDataSetChanged();
    }
}
