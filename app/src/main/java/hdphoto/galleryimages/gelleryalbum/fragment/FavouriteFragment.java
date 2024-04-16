package hdphoto.galleryimages.gelleryalbum.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import hdphoto.galleryimages.gelleryalbum.R;
import hdphoto.galleryimages.gelleryalbum.activity.FavouriteImageFragment;

public class FavouriteFragment extends Fragment {
    ViewPagerAdapter adapter;
    private TextView txt_album, txt_image;
    View albumView;
    FrameLayout container;
    View photoView;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_favourite, container, false);
        this.viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        this.container = (FrameLayout) view.findViewById(R.id.container);
        this.tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        View inflate = LayoutInflater.from(requireActivity()).inflate(R.layout.layout_tab, (ViewGroup) null);
        this.photoView = inflate;
        txt_image = (TextView) inflate.findViewById(R.id.tabIcon);
        txt_image.setText(getString(R.string.photo));
        this.txt_image.setTextColor(getResources().getColor(R.color.txt_orange));
        TabLayout tabLayout = this.tabLayout;
        tabLayout.addTab(tabLayout.newTab().setCustomView(this.photoView));
        View inflate2 = LayoutInflater.from(requireActivity()).inflate(R.layout.layout_tab, (ViewGroup) null);
        this.albumView = inflate2;
        txt_album =  inflate2.findViewById(R.id.tabIcon);
        txt_album.setText(getString(R.string.Videos));
        TabLayout tabLayout2 = this.tabLayout;
        tabLayout2.addTab(tabLayout2.newTab().setCustomView(this.albumView));
        adapter = new ViewPagerAdapter(requireActivity().getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setCurrentItem(0);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0) {
                    txt_image.setTextColor(getResources().getColor(R.color.txt_orange));
                } else if (tab.getPosition() == 1) {
                    txt_album.setTextColor(getResources().getColor(R.color.txt_orange));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    txt_image.setTextColor(getResources().getColor(R.color.black));
                } else if (tab.getPosition() == 1) {
                    txt_album.setTextColor(getResources().getColor(R.color.black));
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
        });
        this.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int i) {
            }

            @Override
            public void onPageScrolled(int i, float f, int i2) {
            }

            @Override
            public void onPageSelected(int i) {
                if (i == 0) {
                    txt_image.setTextColor(getResources().getColor(R.color.txt_orange));
                    txt_album.setTextColor(getResources().getColor(R.color.black));

                } else if (i == 1) {
                    txt_image.setTextColor(getResources().getColor(R.color.black));
                    txt_album.setTextColor(getResources().getColor(R.color.txt_orange));
                }
            }
        });

        return view;
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {

        @Override
        public int getCount() {
            return 2;
        }

        public ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @NonNull
        @Override
        public Fragment getItem(int i) {
            if(i == 0){
                return new FavouriteImageFragment();
            } else if (i == 1) {
                return new FavouriteVideoFragment();
            }
            return null;
        }
    }
}