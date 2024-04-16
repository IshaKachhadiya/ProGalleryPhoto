package vidgallery.infideap.drawerbehavior;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import java.util.HashMap;


public class AdvanceDrawerLayout extends DrawerLayout {
    private float defaultDrawerElevation;
    private int defaultScrimColor;
    public View drawerView;
    private FrameLayout frameLayout;
    HashMap<Integer, Setting> settings;

    public AdvanceDrawerLayout(Context context) {
        super(context);
        this.settings = new HashMap<>();
        this.defaultScrimColor = -1728053248;
        init(context, null, 0);
    }

    public AdvanceDrawerLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.settings = new HashMap<>();
        this.defaultScrimColor = -1728053248;
        init(context, attributeSet, 0);
    }

    public AdvanceDrawerLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.settings = new HashMap<>();
        this.defaultScrimColor = -1728053248;
        init(context, attributeSet, i);
    }

    private void init(Context context, AttributeSet attributeSet, int i) {
        this.defaultDrawerElevation = getDrawerElevation();
        addDrawerListener(new DrawerLayout.DrawerListener() { // from class: com.infideap.drawerbehavior.AdvanceDrawerLayout.1
            @Override // androidx.drawerlayout.widget.DrawerLayout.DrawerListener
            public void onDrawerClosed(View view) {
            }

            @Override // androidx.drawerlayout.widget.DrawerLayout.DrawerListener
            public void onDrawerOpened(View view) {
            }

            @Override // androidx.drawerlayout.widget.DrawerLayout.DrawerListener
            public void onDrawerStateChanged(int i2) {
            }

            @Override // androidx.drawerlayout.widget.DrawerLayout.DrawerListener
            public void onDrawerSlide(View view, float f) {
                AdvanceDrawerLayout.this.drawerView = view;
                AdvanceDrawerLayout.this.updateSlideOffset(view, f);
            }
        });
        FrameLayout frameLayout = new FrameLayout(context);
        this.frameLayout = frameLayout;
        super.addView(frameLayout);
    }

    @Override // android.view.ViewGroup, android.view.ViewManager
    public void addView(View view, ViewGroup.LayoutParams layoutParams) {
        view.setLayoutParams(layoutParams);
        addView(view);
    }

    @Override // android.view.ViewGroup
    public void addView(View view) {
        if (view instanceof NavigationView) {
            super.addView(view);
            return;
        }
        CardView cardView = new CardView(getContext());
        cardView.setRadius(0.0f);
        cardView.setClipToPadding(false);
        cardView.addView(view);
        cardView.setCardElevation(0.0f);
        this.frameLayout.addView(cardView);
    }

    public void setViewScale(int i, float f) {
        Setting setting;
        int drawerViewAbsoluteGravity = getDrawerViewAbsoluteGravity(i);
        if (!this.settings.containsKey(Integer.valueOf(drawerViewAbsoluteGravity))) {
            setting = createSetting();
            this.settings.put(Integer.valueOf(drawerViewAbsoluteGravity), setting);
        } else {
            setting = this.settings.get(Integer.valueOf(drawerViewAbsoluteGravity));
        }
        setting.percentage = f;
        setting.scrimColor = 0;
        setting.drawerElevation = 0.0f;
    }

    public void setViewElevation(int i, float f) {
        Setting setting;
        int drawerViewAbsoluteGravity = getDrawerViewAbsoluteGravity(i);
        if (!this.settings.containsKey(Integer.valueOf(drawerViewAbsoluteGravity))) {
            setting = createSetting();
            this.settings.put(Integer.valueOf(drawerViewAbsoluteGravity), setting);
        } else {
            setting = this.settings.get(Integer.valueOf(drawerViewAbsoluteGravity));
        }
        setting.scrimColor = 0;
        setting.drawerElevation = 0.0f;
        setting.elevation = f;
    }

    public void setViewScrimColor(int i, int i2) {
        Setting setting;
        int drawerViewAbsoluteGravity = getDrawerViewAbsoluteGravity(i);
        if (!this.settings.containsKey(Integer.valueOf(drawerViewAbsoluteGravity))) {
            setting = createSetting();
            this.settings.put(Integer.valueOf(drawerViewAbsoluteGravity), setting);
        } else {
            setting = this.settings.get(Integer.valueOf(drawerViewAbsoluteGravity));
        }
        setting.scrimColor = i2;
    }

    public void setDrawerElevation(int i, float f) {
        Setting setting;
        int drawerViewAbsoluteGravity = getDrawerViewAbsoluteGravity(i);
        if (!this.settings.containsKey(Integer.valueOf(drawerViewAbsoluteGravity))) {
            setting = createSetting();
            this.settings.put(Integer.valueOf(drawerViewAbsoluteGravity), setting);
        } else {
            setting = this.settings.get(Integer.valueOf(drawerViewAbsoluteGravity));
        }
        setting.elevation = 0.0f;
        setting.drawerElevation = f;
    }

    public void setRadius(int i, float f) {
        Setting setting;
        int drawerViewAbsoluteGravity = getDrawerViewAbsoluteGravity(i);
        if (!this.settings.containsKey(Integer.valueOf(drawerViewAbsoluteGravity))) {
            setting = createSetting();
            this.settings.put(Integer.valueOf(drawerViewAbsoluteGravity), setting);
        } else {
            setting = this.settings.get(Integer.valueOf(drawerViewAbsoluteGravity));
        }
        setting.radius = f;
    }

    public Setting getSetting(int i) {
        return this.settings.get(Integer.valueOf(getDrawerViewAbsoluteGravity(i)));
    }

    @Override // androidx.drawerlayout.widget.DrawerLayout
    public void setDrawerElevation(float f) {
        this.defaultDrawerElevation = f;
        super.setDrawerElevation(f);
    }

    @Override // androidx.drawerlayout.widget.DrawerLayout
    public void setScrimColor(int i) {
        this.defaultScrimColor = i;
        super.setScrimColor(i);
    }

    public void useCustomBehavior(int i) {
        int drawerViewAbsoluteGravity = getDrawerViewAbsoluteGravity(i);
        if (this.settings.containsKey(Integer.valueOf(drawerViewAbsoluteGravity))) {
            return;
        }
        this.settings.put(Integer.valueOf(drawerViewAbsoluteGravity), createSetting());
    }

    public void removeCustomBehavior(int i) {
        int drawerViewAbsoluteGravity = getDrawerViewAbsoluteGravity(i);
        if (this.settings.containsKey(Integer.valueOf(drawerViewAbsoluteGravity))) {
            this.settings.remove(Integer.valueOf(drawerViewAbsoluteGravity));
        }
    }

    @Override // androidx.drawerlayout.widget.DrawerLayout
    public void openDrawer(final View view, boolean z) {
        super.openDrawer(view, z);
        post(new Runnable() { // from class: com.infideap.drawerbehavior.AdvanceDrawerLayout.2
            @Override 
            public void run() {
                AdvanceDrawerLayout advanceDrawerLayout = AdvanceDrawerLayout.this;
                View view2 = view;
                advanceDrawerLayout.updateSlideOffset(view2, advanceDrawerLayout.isDrawerOpen(view2) ? 1.0f : 0.0f);
            }
        });
    }

    public void updateSlideOffset(View view, float f) {
        int drawerViewAbsoluteGravity = getDrawerViewAbsoluteGravity(GravityCompat.START);
        int drawerViewAbsoluteGravity2 = getDrawerViewAbsoluteGravity(view);
        for (int i = 0; i < this.frameLayout.getChildCount(); i++) {
            CardView cardView = (CardView) this.frameLayout.getChildAt(i);
            Setting setting = this.settings.get(Integer.valueOf(drawerViewAbsoluteGravity2));
            if (setting != null) {
                cardView.setRadius((int) (setting.radius * f));
                super.setScrimColor(setting.scrimColor);
                super.setDrawerElevation(setting.drawerElevation);
                float f2 = 1.0f - setting.percentage;
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) cardView.getLayoutParams();
                int height = (int) (((getHeight() * f2) * f) / 2.0f);
                layoutParams.topMargin = height;
                layoutParams.bottomMargin = height;
                cardView.setLayoutParams(layoutParams);
                cardView.setCardElevation(setting.elevation * f);
                float f3 = setting.elevation;
                boolean z = drawerViewAbsoluteGravity2 == drawerViewAbsoluteGravity;
                int width = view.getWidth();
                updateSlideOffset(cardView, setting, z ? width + f3 : (-width) - f3, f, z);
            } else {
                super.setScrimColor(this.defaultScrimColor);
                super.setDrawerElevation(this.defaultDrawerElevation);
            }
        }
    }

    void updateSlideOffset(CardView cardView, Setting setting, float f, float f2, boolean z) {
        ViewCompat.setX(cardView, f * f2);
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        View view = this.drawerView;
        if (view != null) {
            updateSlideOffset(view, isDrawerOpen(view) ? 1.0f : 0.0f);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getDrawerViewAbsoluteGravity(int i) {
        return GravityCompat.getAbsoluteGravity(i, ViewCompat.getLayoutDirection(this)) & 7;
    }

    int getDrawerViewAbsoluteGravity(View view) {
        return getDrawerViewAbsoluteGravity(((DrawerLayout.LayoutParams) view.getLayoutParams()).gravity);
    }

    Setting createSetting() {
        return new Setting();
    }

    
    public class Setting {
        float drawerElevation;
        float radius;
        int scrimColor;
        float percentage = 1.0f;
        float elevation = 0.0f;

        public Setting() {
            this.scrimColor = AdvanceDrawerLayout.this.defaultScrimColor;
            this.drawerElevation = AdvanceDrawerLayout.this.defaultDrawerElevation;
        }

        public float getDrawerElevation() {
            return this.drawerElevation;
        }

        public float getElevation() {
            return this.elevation;
        }

        public float getPercentage() {
            return this.percentage;
        }

        public float getRadius() {
            return this.radius;
        }

        public int getScrimColor() {
            return this.scrimColor;
        }
    }
}
