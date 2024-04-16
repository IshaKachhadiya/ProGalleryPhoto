package hdphoto.galleryimages.gelleryalbum.location_media.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;


import hdphoto.galleryimages.gelleryalbum.R;
import hdphoto.galleryimages.gelleryalbum.databinding.ActivityImageShowBinding;
import hdphoto.galleryimages.gelleryalbum.location_media.adapter.ImagePagerAdapter;
import hdphoto.galleryimages.gelleryalbum.location_media.common.Constant;

public class ImageShowActivity extends AppCompatActivity {
    ActivityImageShowBinding binding;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImageShowBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.position = getIntent().getIntExtra("pos", 0);
//        this.str = getIntent().getStringExtra("name");

        binding.toolbar.toolbarTitle.setText(R.string.app_name);

        ImagePagerAdapter adapter = new ImagePagerAdapter(this,Constant.displayImageList);
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.setCurrentItem(position);

        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int i2) {
                position = i2;
            }

            @Override
            public void onPageScrolled(int i2, float f, int i3) {

            }

            @Override
            public void onPageSelected(int i2) {

            }
        });

        binding.toolbar.btnBack.setOnClickListener(v->{
            onBackPressed();
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}