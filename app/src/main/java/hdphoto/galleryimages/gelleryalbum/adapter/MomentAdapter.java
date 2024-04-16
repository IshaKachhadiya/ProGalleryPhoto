package hdphoto.galleryimages.gelleryalbum.adapter;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.exifinterface.media.ExifInterface;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.iten.tenoku.ad.AdShow;
import com.iten.tenoku.ad.HandleClick.HandleClick;
import com.iten.tenoku.utils.AdUtils;

import hdphoto.galleryimages.gelleryalbum.R;
import hdphoto.galleryimages.gelleryalbum.fragment.MomentFragment;
import hdphoto.galleryimages.gelleryalbum.listeners.OnClickListener;
import hdphoto.galleryimages.gelleryalbum.model.DataFileModel;
import hdphoto.galleryimages.gelleryalbum.utils.ConstantArrayClass;



import java.io.File;
import java.util.ArrayList;


public class MomentAdapter extends RecyclerView.Adapter<MomentAdapter.MyViewHolder> {
    Activity activity;
    ArrayList<Object> momentArray;
    private OnClickListener<ArrayList<Object>, Integer, View> onClickListener;
    int size;

    public MomentAdapter(Activity activity, ArrayList<Object> arrayList) {
        this.activity = activity;
        this.momentArray = arrayList;
    }

    public void RefreshData(ArrayList<Object> arrayList) {
        CheckImageFound(ConstantArrayClass.albumsList);
        this.momentArray = arrayList;
        notifyDataSetChanged();
    }

    private void CheckImageFound(ArrayList<Object> arrayList) {
        if (arrayList.size() >= 1) {
            MomentFragment.rlPhotoLayout.setVisibility(View.GONE);
            MomentFragment.fragmnetimagegridView.setVisibility(View.VISIBLE);
            return;
        }
        MomentFragment.fragmnetimagegridView.setVisibility(View.GONE);
        MomentFragment.rlPhotoLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemViewType(int i) {
        try {
            if (momentArray.get(i) instanceof DataFileModel) {
                return 1;
            }
            return momentArray.get(i) instanceof String ? 0 : -1;
        } catch (IndexOutOfBoundsException unused) {
            return 1;
        }
    }

    public void FilterNewData(ArrayList<Object> arrayList) {
        momentArray.clear();
        momentArray.addAll(arrayList);
        activity.runOnUiThread(this::notifyDataSetChanged);
    }

    public void RemoveItem(ArrayList<DataFileModel> arrayList) {
        momentArray.removeAll(arrayList);
        activity.runOnUiThread(this::notifyDataSetChanged);
    }

    public void RemoveItemSingle(String str) {
        for (int i = 0; i < momentArray.size(); i++) {
            if ((momentArray.get(i) instanceof DataFileModel) && ((DataFileModel) momentArray.get(i)).path.contains(str)) {
                momentArray.remove(i);
            }
        }
        activity.runOnUiThread(this::notifyDataSetChanged);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == 0) {
            return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_date_header_moment_adapter, viewGroup, false));
        }
        if (i != 1) {
            return null;
        }
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_data_moment_adapter, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, final int i) {
        int itemViewType = myViewHolder.getItemViewType();
        if (itemViewType == 0) {
            try {
                myViewHolder.txt_date.setText((String) momentArray.get(i));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (itemViewType == 1) {
            myViewHolder.image_view_album_image.getLayoutParams().width = size;
            myViewHolder.image_view_album_image.getLayoutParams().height = size;
            try {
                if (((DataFileModel) momentArray.get(i)).isSelected) {
                    myViewHolder.view_alpha.setVisibility(View.VISIBLE);
                } else {
                    myViewHolder.view_alpha.setVisibility(View.GONE);
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            try {
                if (((DataFileModel) momentArray.get(i)).getMediaType().equals(ExifInterface.GPS_MEASUREMENT_3D)) {
                    myViewHolder.rel_play.setVisibility(View.VISIBLE);
                } else if (((DataFileModel) momentArray.get(i)).getMediaType().equals("1")) {
                    myViewHolder.rel_play.setVisibility(View.GONE);
                }
            } catch (Exception e3) {
                e3.printStackTrace();
            }
            try {
                myViewHolder.text_view_album_name.setText(String.valueOf(new File(((DataFileModel) momentArray.get(i)).path).getName()));
            } catch (Exception e4) {
                e4.printStackTrace();
            }
            try {
                TextView textView = myViewHolder.text_view_album_size;
                textView.setText("(" + ((DataFileModel) momentArray.get(i)).pathlist.size() + ")");
            } catch (Exception e5) {
                e5.printStackTrace();
            }
            try {
                if (((DataFileModel) momentArray.get(i)).getFolderName().equals("Take Photo")) {
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.centerCrop();
                    Glide.with(activity).load(((DataFileModel) momentArray.get(i)).path).apply((BaseRequestOptions<?>) requestOptions).into(myViewHolder.image_view_album_image);
                } else {
                    Uri fromFile = Uri.fromFile(new File(((DataFileModel) momentArray.get(i)).path));
                    RequestOptions requestOptions2 = new RequestOptions();
                    requestOptions2.centerCrop();
                    Glide.with(activity).load(fromFile).apply((BaseRequestOptions<?>) requestOptions2).into(myViewHolder.image_view_album_image);
                }
            } catch (Exception e6) {
                e6.printStackTrace();
            }
            myViewHolder.card_click.setOnClickListener(view -> {
                AdShow.getInstance(activity).ShowAd(new HandleClick() {
                    @Override
                    public void Show(boolean adShow) {
                        if (onClickListener != null) {
                            onClickListener.onClickListener(momentArray, Integer.valueOf(i));
                        }
                    }
                }, AdUtils.ClickType.MAIN_CLICK);

            });
            myViewHolder.card_click.setOnLongClickListener(view -> {
                if (onClickListener != null) {
                    onClickListener.onLongClickListener(momentArray, Integer.valueOf(i));
                    return false;
                }
                return false;
            });
        }
    }

    @Override
    public int getItemCount() {
        if (momentArray != null) {
            return momentArray.size();
        }
        return 0;
    }

    public void setItemClickCallback(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView btnMoreDetails;
        CardView card_click;
        ImageView image_view_album_image;
        ImageView img_calender;
        ImageView rel_play;
        RelativeLayout rl_date;
        TextView text_view_album_name;
        TextView text_view_album_size;
        TextView txt_date;
        RelativeLayout view_alpha;

        public MyViewHolder(View view) {
            super(view);
            image_view_album_image = (ImageView) view.findViewById(R.id.image_view_album_image);
            text_view_album_name = (TextView) view.findViewById(R.id.text_view_album_name);
            text_view_album_size = (TextView) view.findViewById(R.id.text_view_album_size);
            view_alpha = (RelativeLayout) view.findViewById(R.id.view_alpha);
            btnMoreDetails = (ImageView) view.findViewById(R.id.btnMoreDetails);
            rel_play = (ImageView) view.findViewById(R.id.rel_play);
            txt_date = (TextView) view.findViewById(R.id.txt_date);
            card_click = (CardView) view.findViewById(R.id.card_click);
            rl_date = (RelativeLayout) view.findViewById(R.id.rl_date);
            img_calender = (ImageView) view.findViewById(R.id.img_calender);
        }
    }

    public void setLayoutParams(int i) {
        size = i;
    }
}
