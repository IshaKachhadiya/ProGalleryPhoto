package hdphoto.galleryimages.gelleryalbum.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.view.PointerIconCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.iten.tenoku.ad.AdShow;
import com.iten.tenoku.ad.HandleClick.HandleClick;
import com.iten.tenoku.utils.AdUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;


import hdphoto.galleryimages.gelleryalbum.R;
import hdphoto.galleryimages.gelleryalbum.duplicate.DuplicateImageActivity;
import hdphoto.galleryimages.gelleryalbum.duplicate.LoginPreferenceManager;
import hdphoto.galleryimages.gelleryalbum.duplicate.pictureFacer;
import hdphoto.galleryimages.gelleryalbum.location_media.imageIndicatorListener;
import it.sephiroth.android.library.imagezoom.ImageViewTouch;

public class SingleImageActivity extends AppCompatActivity implements imageIndicatorListener {
    public static int showOncePhoto;
    private ArrayList<pictureFacer> allImages;
    private TextView tv_Cancel,tv_Delete;
    private String check;
    int i;
    private ImageViewTouch image;
    private ViewPager imagePager;
    RecyclerView indicatorRecycler;
    ImageView iv_back,iv_delete,iv_like,ivMore;
    private ImageView ivShare;
    private ImagesPagerAdapter pagingImages;
    private int position;
    private int previousSelected;
    int requestCode;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_single_image);
        new Random().nextInt(2);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(Integer.MIN_VALUE);
            window.setStatusBarColor(ViewCompat.MEASURED_STATE_MASK);
        }
        Intent intent = getIntent();
        this.position = Integer.parseInt(intent.getStringExtra("Pos"));
//        position = getIntent().getIntExtra("Pos",0);
        String stringExtra = intent.getStringExtra("From");
        this.check = stringExtra;
        if (stringExtra.matches("All")) {
//            this.allImages = ImageFragment.PassData;
//            this.requestCode = 2;
        } else if (this.check.matches("FavImage")) {
            this.allImages = FavouriteImageFragment.getFavimgList;
            this.requestCode = 2;
        } else if (this.check.matches("Duplicate")) {
            this.allImages = DuplicateImageActivity.getimgList;
            this.requestCode = 2;
        } else {
//            this.allImages = ImageDisplay.PassDataAlbum;
//            this.requestCode = 3;
        }
        init();
        if (LoginPreferenceManager.getFavData(getApplicationContext(), this.allImages.get(this.imagePager.getCurrentItem()).getPicturePath())) {
            this.iv_like.setImageDrawable(getResources().getDrawable(R.drawable.ic_fill_heart));
        } else {
            this.iv_like.setImageDrawable(getResources().getDrawable(R.drawable.ic_unfill_heart));
        }
        final Dialog dialog = new Dialog(this, R.style.Theme_Dialog);
        dialog.requestWindowFeature(1);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.delete_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setCanceledOnTouchOutside(true);
        this.tv_Cancel = (TextView) dialog.findViewById(R.id.btCancel);
        this.tv_Delete = (TextView) dialog.findViewById(R.id.btDelete);
        this.imagePager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int i) {
            }

            @Override
            public void onPageScrolled(int i, float f, int i2) {
            }

            @Override
            public void onPageSelected(int i) {
                try {
                    if (SingleImageActivity.this.previousSelected != -1) {
                        ((pictureFacer) SingleImageActivity.this.allImages.get(SingleImageActivity.this.previousSelected)).setSelected(false);
                        SingleImageActivity.this.previousSelected = i;
                        ((pictureFacer) SingleImageActivity.this.allImages.get(i)).setSelected(true);
                        if (LoginPreferenceManager.getFavData(SingleImageActivity.this.getApplicationContext(), ((pictureFacer) SingleImageActivity.this.allImages.get(SingleImageActivity.this.imagePager.getCurrentItem())).getPicturePath())) {
                            SingleImageActivity.this.iv_like.setImageDrawable(SingleImageActivity.this.getDrawable(R.drawable.ic_fill_heart));
                        } else {
                            SingleImageActivity.this.iv_like.setImageDrawable(SingleImageActivity.this.getDrawable(R.drawable.ic_unfill_heart));
                        }
                    } else {
                        SingleImageActivity.this.previousSelected = i;
                        ((pictureFacer) SingleImageActivity.this.allImages.get(i)).setSelected(true);
                        if (LoginPreferenceManager.getFavData(SingleImageActivity.this.getApplicationContext(), ((pictureFacer) SingleImageActivity.this.allImages.get(SingleImageActivity.this.imagePager.getCurrentItem())).getPicturePath())) {
                            SingleImageActivity.this.iv_like.setImageDrawable(SingleImageActivity.this.getDrawable(R.drawable.ic_fill_heart));
                        } else {
                            SingleImageActivity.this.iv_like.setImageDrawable(SingleImageActivity.this.getDrawable(R.drawable.ic_unfill_heart));
                        }
                    }
                } catch (Exception unused) {
                }
            }
        });
        this.iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SingleImageActivity.this.onBackPressed();
            }
        });
        this.ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri fromFile;
                try {
                    File file = new File(allImages.get(SingleImageActivity.this.imagePager.getCurrentItem()).getPicturePath());
                    try {
                        fromFile = FileProvider.getUriForFile(SingleImageActivity.this, getPackageName() + ".fileprovider", file);
                    } catch (Exception unused) {
                        fromFile = Uri.fromFile(file);
                    }
                    Intent intent2 = new Intent("android.intent.action.SEND");
                    intent2.setType("image/*");
                    intent2.putExtra("android.intent.extra.STREAM", fromFile);
                    intent2.putExtra("android.intent.extra.SUBJECT", SingleImageActivity.this.getString(R.string.app_name));
                    intent2.putExtra("android.intent.extra.TEXT", "https://play.google.com/store/apps/details?id=" + getPackageName());
                    intent2.addFlags(1);
                    intent2.setFlags(268435456);
                    SingleImageActivity.this.startActivityForResult(Intent.createChooser(intent2, "Choose one..."), PointerIconCompat.TYPE_CONTEXT_MENU);
                } catch (Exception unused2) {
                }
            }
        });
        this.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                SingleImageActivity.this.tv_Delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view2) {
                        pictureFacer picturefacer = (pictureFacer) SingleImageActivity.this.allImages.get(SingleImageActivity.this.imagePager.getCurrentItem());
                        File file = new File(picturefacer.getPicturePath());
                        try {
                            dialog.dismiss();
                            if (file.delete()) {
                                Log.e("FileDelete--)", "" + file.getAbsoluteFile());
                                try {
                                    if (Build.VERSION.SDK_INT >= 19) {
                                        Intent intent2 = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                                        intent2.setData(Uri.fromFile(file));
                                        SingleImageActivity.this.sendBroadcast(intent2);
                                    } else {
                                        SingleImageActivity.this.sendBroadcast(new Intent("android.intent.action.MEDIA_MOUNTED", Uri.fromFile(file)));
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (Exception unused) {
                        }
                        SingleImageActivity.this.scanFile(picturefacer.getPicturePath());
                    }
                });
                SingleImageActivity.this.tv_Cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view2) {
                        dialog.dismiss();
                    }
                });
            }
        });

    }

    @Override
    public void onBackPressed() {
        AdShow.getInstance(SingleImageActivity.this).ShowAd(new HandleClick() {
            @Override
            public void Show(boolean adShow) {
                SingleImageActivity.super.onBackPressed();
            }
        }, AdUtils.ClickType.MAIN_CLICK);
    }

    private void deleteImage(String str) {
        if (str.startsWith("content://")) {
            ContentResolver contentResolver = getApplicationContext().getContentResolver();
            contentResolver.delete(Uri.parse(str), null, null);
            try {
                DocumentsContract.deleteDocument(contentResolver, Uri.parse(str));
                return;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return;
            }
        }
        File file = new File(str);
        if (file.exists()) {
            file.delete();
        }
    }

    public void scanFile(String str) {
        MediaScannerConnection.scanFile(this, new String[]{str}, null, new MediaScannerConnection.OnScanCompletedListener() {
            @Override
            public void onScanCompleted(String str2, Uri uri) {
                Log.i("TAG", "Finished scanning Image--) " + str2 + "Pos--) " + SingleImageActivity.this.imagePager.getCurrentItem());
                SingleImageActivity.this.allImages.remove(SingleImageActivity.this.allImages.get(SingleImageActivity.this.imagePager.getCurrentItem()));
                StringBuilder sb = new StringBuilder();
                sb.append("Pos After Delete --) ");
                sb.append(SingleImageActivity.this.imagePager.getCurrentItem() + 1);
                Log.i("TAG", sb.toString());
                SingleImageActivity.this.runOnUiThread(new Runnable() { // from class: com.nk.gallery.Activity.SingleImageActivity.7.1
                    @Override
                    public void run() {
                        SingleImageActivity.this.pagingImages.notifyDataSetChanged();
                        SingleImageActivity.this.imagePager.setAdapter(SingleImageActivity.this.pagingImages);
                        SingleImageActivity.this.imagePager.setCurrentItem(SingleImageActivity.this.imagePager.getCurrentItem());
                    }
                });
            }
        });
    }

    private void init() {
        this.imagePager = (ViewPager) findViewById(R.id.imagePager);
        this.iv_back = (ImageView) findViewById(R.id.ivBack);
        this.ivMore = (ImageView) findViewById(R.id.ivMore);
        this.ivShare = (ImageView) findViewById(R.id.ivShare);
        this.iv_delete = (ImageView) findViewById(R.id.ivDelete);
        this.iv_like = (ImageView) findViewById(R.id.ivLike);
        ImagesPagerAdapter imagesPagerAdapter = new ImagesPagerAdapter();
        pagingImages = imagesPagerAdapter;
        imagePager.setAdapter(imagesPagerAdapter);
        imagePager.setOffscreenPageLimit(3);
        imagePager.setCurrentItem(this.position);
    }

    @Override
    public void onImageIndicatorClicked(int i) {
        int i2 = this.previousSelected;
        if (i2 != -1) {
            allImages.get(i2).setSelected(false);
            previousSelected = i;
            indicatorRecycler.getRecycledViewPool().clear();
            indicatorRecycler.getAdapter().notifyDataSetChanged();
        } else {
            this.previousSelected = i;
        }
        this.imagePager.setCurrentItem(i);
    }

//    public SingleImageActivity(ArrayList<pictureFacer> arrayList, int i, Context context) {
//        this.allImages = new ArrayList<>();
//        this.viewVisibilityController = 0;
//        this.viewVisibilitylooper = 0;
//        this.previousSelected = -1;
//        this.i = 1;
//        this.sdcardPath = "sd_card_path_2";
//        this.allImages = arrayList;
//        this.position = i;
//        this.animeContx = context;
//    }
    public class ImagesPagerAdapter extends PagerAdapter {
        private ImagesPagerAdapter() {
        }

        @Override
        public int getCount() {
            return SingleImageActivity.this.allImages.size();
        }

        @Override
        public Object instantiateItem(ViewGroup viewGroup, int i) {
            View inflate = ((LayoutInflater) viewGroup.getContext().getSystemService("layout_inflater")).inflate(R.layout.image_full_view, (ViewGroup) null);
            SingleImageActivity.this.image = (ImageViewTouch) inflate.findViewById(R.id.image);
            Glide.with(getApplicationContext()).load(((pictureFacer) allImages.get(i)).getPicturePath()).apply((BaseRequestOptions<?>) new RequestOptions().fitCenter()).into(SingleImageActivity.this.image);
            SingleImageActivity.this.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (indicatorRecycler.getVisibility() == 8) {
                            indicatorRecycler.setVisibility(0);
                        } else {
                            indicatorRecycler.setVisibility(8);
                        }
                    } catch (Exception unused) {
                    }
                }
            });
            SingleImageActivity.this.iv_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Log.d("TAG", "onOptionsItemSelected: " + LoginPreferenceManager.getFavData(getApplicationContext(), ((pictureFacer) allImages.get(imagePager.getCurrentItem())).getPicturePath()));
                        if (LoginPreferenceManager.getFavData(getApplicationContext(), ((pictureFacer) allImages.get(imagePager.getCurrentItem())).getPicturePath())) {
                            LoginPreferenceManager.saveFavData(getApplicationContext(), ((pictureFacer) allImages.get(imagePager.getCurrentItem())).getPicturePath(), false);
                            SingleImageActivity.this.iv_like.setImageDrawable(getDrawable(R.drawable.ic_unfill_heart));
                        } else {
                            LoginPreferenceManager.saveFavData(getApplicationContext(), ((pictureFacer) allImages.get(imagePager.getCurrentItem())).getPicturePath(), true);
                            SingleImageActivity.this.iv_like.setImageDrawable(getDrawable(R.drawable.ic_fill_heart));
                        }
                        SingleImageActivity.this.invalidateOptionsMenu();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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


    @SuppressLint("WrongConstant")
    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 3) {
            try {
                getContentResolver().takePersistableUriPermission(i2 == -1 ? intent.getData() : null, intent.getFlags() & 3);
                pictureFacer picturefacer = this.allImages.get(this.imagePager.getCurrentItem());
                new File(picturefacer.getPicturePath());
                String picturePath = picturefacer.getPicturePath();
                deleteImage(picturePath);
                scanFile(picturePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
