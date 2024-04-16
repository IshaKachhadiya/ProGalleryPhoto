package hdphoto.galleryimages.gelleryalbum.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.iten.tenoku.ad.AdShow;
import com.iten.tenoku.ad.HandleClick.HandleClick;
import com.iten.tenoku.utils.AdUtils;

import java.util.ArrayList;

import hdphoto.galleryimages.gelleryalbum.listeners.OnClickListener;
import hdphoto.galleryimages.gelleryalbum.model.DataFileModel;
import hdphoto.galleryimages.gelleryalbum.constant.Common;
import hdphoto.galleryimages.gelleryalbum.R;

public class BothAlbumSelectAdapter extends BaseAdapter implements Filterable {
    Activity activity;
    ArrayList<Object> arrayList;
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<Object> mStringFilterList;
    OnClickListener<ArrayList<Object>, Integer, View> onClickListener;
    int size;
    ValueFilter valueFilter;

    @Override 
    public long getItemId(int i) {
        return i;
    }

    public BothAlbumSelectAdapter(Activity activity, Context context, ArrayList<Object> arrayList) {
        this.arrayList = arrayList;
        this.mStringFilterList = arrayList;
        this.context = context;
        this.activity = activity;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override 
    public int getCount() {
        if (arrayList == null) {
            return 0;
        }
        return arrayList.size();
    }

    @Override 
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override 
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(activity).inflate(R.layout.layout_inner_both_album_select_adapter, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) view.findViewById(R.id.image_view_image_select);
            viewHolder.cardimg =  view.findViewById(R.id.cardimg);
            viewHolder.rl_view = (RelativeLayout) view.findViewById(R.id.view_alpha);
            viewHolder.rl_play = (RelativeLayout) view.findViewById(R.id.rel_play);
            viewHolder.txt_size = (TextView) view.findViewById(R.id.txt_size);
            viewHolder.ll_size = (LinearLayout) view.findViewById(R.id.lin_size);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.imageView.getLayoutParams().width = size;
        viewHolder.imageView.getLayoutParams().height = size;
        viewHolder.rl_view.getLayoutParams().width = size;
        viewHolder.rl_view.getLayoutParams().height = size;
        if (Common.strplay.equals("InnerVideoSelectActivity")) {
            viewHolder.rl_play.setVisibility(View.VISIBLE);
            viewHolder.ll_size.setVisibility(View.GONE);
        }
        try {
            if (((DataFileModel) arrayList.get(i)).isSelected) {
                viewHolder.rl_view.setVisibility(View.VISIBLE);
            } else {
                viewHolder.rl_view.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.centerCrop();
            Glide.with(context).load(((DataFileModel) arrayList.get(i)).path).apply((BaseRequestOptions<?>) requestOptions).into(viewHolder.imageView);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        if (((arrayList.size() <= 0 || ((DataFileModel) arrayList.get(i)).duration == 0) ? 0L : ((DataFileModel) arrayList.get(i)).duration) != 0) {
            String milliSecToTimer = milliSecToTimer(((DataFileModel) arrayList.get(i)).duration);
            TextView textView = viewHolder.txt_size;
            textView.setText(milliSecToTimer + "");
        }
        viewHolder.rl_play.setOnClickListener(view2 -> {
            AdShow.getInstance(activity).ShowAd(new HandleClick() {
                @Override
                public void Show(boolean adShow) {
                    try {
                        onClickListener.onClickListener(arrayList, Integer.valueOf(i));
                    } catch (Exception e3) {
                        e3.printStackTrace();
                    }
                }
            }, AdUtils.ClickType.MAIN_CLICK);

        });
        viewHolder.imageView.setOnClickListener(view2 -> {
            AdShow.getInstance(activity).ShowAd(new HandleClick() {
                @Override
                public void Show(boolean adShow) {
                    try {
                        onClickListener.onClickListener(arrayList, Integer.valueOf(i));
                    } catch (Exception e3) {
                        e3.printStackTrace();
                    }
                }
            }, AdUtils.ClickType.MAIN_CLICK);
        });
        viewHolder.imageView.setOnLongClickListener(view2 -> false);
        return view;
    }

    public static String milliSecToTimer(long j) {
        String str;
        String str2;
        String str3;
        int i = (int) (j / 3600000);
        long j2 = j % 3600000;
        int i2 = ((int) j2) / 60000;
        int i3 = (int) ((j2 % 60_000) / 1000);
        if (i != 0) {
            str = i + " ";
        } else {
            str = "";
        }
        if (i2 < 10) {
            str2 = "0" + i2;
        } else {
            str2 = "" + i2;
        }
        if (i3 < 10) {
            str3 = "0" + i3;
        } else {
            str3 = "" + i3;
        }
        return str + str2 + " " + str3;
    }

    public void setItemClickCallback(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    
    private static class ViewHolder {
        public ImageView imageView;
        public CardView cardimg;
        public LinearLayout ll_size;
        public RelativeLayout rl_play;
        public RelativeLayout rl_view;
        public TextView txt_size;

        private ViewHolder() {
        }
    }

    public void setLayoutParams(int i) {
        this.size = i;
    }

    public void releaseResources() {
        arrayList = null;
        context = null;
        activity = null;
    }

    
    public class ValueFilter extends Filter {
        private ValueFilter() {
        }

        @Override
        public Filter.FilterResults performFiltering(CharSequence charSequence) {
            Filter.FilterResults filterResults = new Filter.FilterResults();
            if (charSequence == null || charSequence.length() <= 0) {
                filterResults.count = mStringFilterList.size();
                filterResults.values = mStringFilterList;
            } else {
                ArrayList arrayList = new ArrayList();
                for (int i = 0; i < mStringFilterList.size(); i++) {
                    if (((DataFileModel) mStringFilterList.get(i)).name != null && ((DataFileModel) mStringFilterList.get(i)).name.toUpperCase().contains(charSequence.toString().toUpperCase())) {
                        arrayList.add(new DataFileModel(((DataFileModel) mStringFilterList.get(i)).id, ((DataFileModel) mStringFilterList.get(i)).name, ((DataFileModel) mStringFilterList.get(i)).path, ((DataFileModel) mStringFilterList.get(i)).oldpath, ((DataFileModel) mStringFilterList.get(i)).directory, ((DataFileModel) mStringFilterList.get(i)).isSelected));
                    }
                }
                filterResults.count = arrayList.size();
                filterResults.values = arrayList;
            }
            return filterResults;
        }

        @Override
        public void publishResults(CharSequence charSequence, Filter.FilterResults filterResults) {
            arrayList = (ArrayList) filterResults.values;
            notifyDataSetChanged();
        }
    }

    public void refreshData(ArrayList<Object> arrayList) {
        this.arrayList = arrayList;
        activity.runOnUiThread(new Runnable() {
            @Override 
            public void run() {
                notifyDataSetChanged();
            }
        });
    }
}
