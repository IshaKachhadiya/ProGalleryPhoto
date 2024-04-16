package hdphoto.galleryimages.gelleryalbum.activity;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.iten.tenoku.ad.AdShow;
import com.iten.tenoku.ad.HandleClick.HandleClick;
import com.iten.tenoku.utils.AdUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import hdphoto.galleryimages.gelleryalbum.R;
import hdphoto.galleryimages.gelleryalbum.duplicate.LoginPreferenceManager;
import hdphoto.galleryimages.gelleryalbum.fragment.FavouriteVideoFragment;
import hdphoto.galleryimages.gelleryalbum.fragment.VideoListFragment;
import hdphoto.galleryimages.gelleryalbum.model.mVideo;

public class VideoViewActivity extends AppCompatActivity {
    private String Vpos;
     TextView btCancel;
    TextView btDelete;
    private String check;
    private ImageView ivBack,iv_delete,iv_like,iv_play,iv_share;
    private ImageView ivVideo;
    private videoPagerAdpter pagingVideo;
    private int vPosition;
    private ViewPager videoPager;
    ArrayList<mVideo> al_video = new ArrayList<>();
    private int previousSelected = -1;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_video_view);
        new Random().nextInt(2);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(Integer.MIN_VALUE);
            window.setStatusBarColor(ViewCompat.MEASURED_STATE_MASK);
        }
        Intent intent = getIntent();
        this.Vpos = intent.getStringExtra("posV");
        this.check = intent.getStringExtra("From");
        this.vPosition = Integer.parseInt(this.Vpos);
        if (this.check.matches("main")) {
            this.al_video = VideoListFragment.PassVideo;
        } else {
            this.al_video = FavouriteVideoFragment.getFavVideoList;
        }
        init();
        if (LoginPreferenceManager.getFavData(getApplicationContext(), this.al_video.get(this.videoPager.getCurrentItem()).getStr_path())) {
            this.iv_like.setImageDrawable(getDrawable(R.drawable.ic_fill_heart));
        } else {
            this.iv_like.setImageDrawable(getDrawable(R.drawable.ic_unfill_heart));
        }
        this.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        final Dialog dialog = new Dialog(this, R.style.Theme_Dialog);
        dialog.requestWindowFeature(1);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.delete_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setCanceledOnTouchOutside(true);
        this.btCancel = (TextView) dialog.findViewById(R.id.btCancel);
        this.btDelete = (TextView) dialog.findViewById(R.id.btDelete);
        this.videoPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int i) {
            }

            @Override
            public void onPageScrolled(int i, float f, int i2) {
            }

            @Override
            public void onPageSelected(int i) {
                if (previousSelected != -1) {
                    al_video.get(previousSelected).setBoolean_selected(false);
                    previousSelected = i;
                    al_video.get(i).setBoolean_selected(true);
                    if (LoginPreferenceManager.getFavData(getApplicationContext(), al_video.get(videoPager.getCurrentItem()).getStr_path())) {
                        iv_like.setImageDrawable(getDrawable(R.drawable.ic_fill_heart));
                        return;
                    } else {
                        iv_like.setImageDrawable(getDrawable(R.drawable.ic_unfill_heart));
                        return;
                    }
                }
                previousSelected = i;
                al_video.get(i).setBoolean_selected(true);
                if (LoginPreferenceManager.getFavData(getApplicationContext(), al_video.get(videoPager.getCurrentItem()).getStr_path())) {
                    iv_like.setImageDrawable(getDrawable(R.drawable.ic_fill_heart));
                } else {
                    iv_like.setImageDrawable(getDrawable(R.drawable.ic_unfill_heart));
                }
            }
        });
        this.iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VideoViewActivity videoViewActivity = VideoViewActivity.this;
                videoViewActivity.shareVideo(videoViewActivity.al_video.get(videoPager.getCurrentItem()).getStr_path());
            }
        });
        this.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                btDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view2) {
                        File file = new File(al_video.get(videoPager.getCurrentItem()).getStr_path());
                        try {
                            dialog.dismiss();
                            file.delete();
                            scanFile(String.valueOf(file));
                        } catch (Exception unused) {
                        }
                    }
                });
                btCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view2) {
                        dialog.dismiss();
                    }
                });
            }
        });
        this.iv_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (LoginPreferenceManager.getFavData(getApplicationContext(), al_video.get(videoPager.getCurrentItem()).getStr_path())) {
                        LoginPreferenceManager.saveFavData(getApplicationContext(), al_video.get(videoPager.getCurrentItem()).getStr_path(), false);
                        iv_like.setImageDrawable(getDrawable(R.drawable.ic_unfill_heart));
                    } else {
                        LoginPreferenceManager.saveFavData(getApplicationContext(), al_video.get(videoPager.getCurrentItem()).getStr_path(), true);
                        iv_like.setImageDrawable(getDrawable(R.drawable.ic_fill_heart));
                    }
                    invalidateOptionsMenu();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        AdShow.getInstance(VideoViewActivity.this).ShowAd(new HandleClick() {
            @Override
            public void Show(boolean adShow) {
                if (SingleImageActivity.showOncePhoto == 0) {
                    setResult(0, new Intent());
                    finish();
                    return;
                }
                setResult(0, new Intent());
                finish();
            }
        }, AdUtils.ClickType.MAIN_CLICK);

    }

    public void scanFile(String str) {
        MediaScannerConnection.scanFile(this, new String[]{str}, null, new MediaScannerConnection.OnScanCompletedListener() {
            @Override
            public void onScanCompleted(String str2, Uri uri) {
                al_video.remove(al_video.get(videoPager.getCurrentItem()));
                videoPager.getCurrentItem();
                runOnUiThread(new Runnable() {
                    @Override 
                    public void run() {
                        pagingVideo.notifyDataSetChanged();
                        videoPager.setAdapter(pagingVideo);
                        videoPager.setCurrentItem(videoPager.getCurrentItem());
                    }
                });
            }
        });
    }

    public void shareVideo(final String str) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                String videoContentUriFromFilePath = VideoViewActivity.getVideoContentUriFromFilePath(VideoViewActivity.this, str);
                Intent intent = new Intent("android.intent.action.SEND");
                intent.putExtra("android.intent.extra.SUBJECT", "GalleryVideo");
                intent.setType("video/mp4");
                intent.putExtra("android.intent.extra.STREAM", Uri.parse(videoContentUriFromFilePath));
                try {
                    startActivity(Intent.createChooser(intent, "Share Video.."));
                } catch (ActivityNotFoundException unused) {
                }
            }
        });
    }

    public static String getVideoContentUriFromFilePath(Context context, String str) {
        ContentResolver contentResolver = context.getContentResolver();
        Uri contentUri = MediaStore.Video.Media.getContentUri("external");
        String[] strArr = {"_id"};
        Cursor query = contentResolver.query(contentUri, strArr, "_data LIKE ?", new String[]{str}, null);
        query.moveToFirst();
        long j = query.getLong(query.getColumnIndex(strArr[0]));
        query.close();
        if (j != -1) {
            return contentUri.toString() + "/" + j;
        }
        return null;
    }

    private void init() {
        this.ivBack = (ImageView) findViewById(R.id.ic_back);
        this.iv_share = (ImageView) findViewById(R.id.ivShare);
        this.iv_delete = (ImageView) findViewById(R.id.ivDelete);
        this.iv_like = (ImageView) findViewById(R.id.ivLike);
        this.videoPager = (ViewPager) findViewById(R.id.videoPager);
        videoPagerAdpter videopageradpter = new videoPagerAdpter();
        this.pagingVideo = videopageradpter;
        this.videoPager.setAdapter(videopageradpter);
        this.videoPager.setOffscreenPageLimit(3);
        this.videoPager.setCurrentItem(this.vPosition);
    }

    public class videoPagerAdpter extends PagerAdapter {
        private videoPagerAdpter() {
        }

        @Override
        public int getCount() {
            return al_video.size();
        }

        @Override
        public Object instantiateItem(ViewGroup viewGroup, final int i) {
            View inflate = ((LayoutInflater) viewGroup.getContext().getSystemService("layout_inflater")).inflate(R.layout.video_full_view, (ViewGroup) null);
            ivVideo = (ImageView) inflate.findViewById(R.id.ivVideo);
            iv_play = (ImageView) inflate.findViewById(R.id.ivPlay);
            Glide.with(getApplicationContext()).load(al_video.get(i).getStr_path()).apply((BaseRequestOptions<?>) new RequestOptions().fitCenter()).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(ivVideo);
            iv_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(al_video.get(i).getStr_path()));
                    intent.setDataAndType(Uri.parse(al_video.get(i).getStr_path()), "video/mp4");
                    startActivity(intent);
                }
            });
            ((ViewPager) viewGroup).addView(inflate);
            return inflate;
        }

        @Override
        public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
            ((ViewPager) viewGroup).removeView((View) obj);
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == ((View) obj);
        }
    }
}
