package vidgallery.infideap.drawerbehavior;

import android.content.Context;
import android.util.AttributeSet;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;


public class Advance3DDrawerLayout extends AdvanceDrawerLayout {
    private static final String TAG = "Advance3DDrawerLayout";

    public Advance3DDrawerLayout(Context context) {
        super(context);
    }

    public Advance3DDrawerLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public Advance3DDrawerLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @Override // com.infideap.drawerbehavior.AdvanceDrawerLayout
    void updateSlideOffset(CardView cardView, AdvanceDrawerLayout.Setting setting, float f, float f2, boolean z) {
        updateSlideOffset(cardView, (Setting) setting, f, f2, z);
    }

    void updateSlideOffset(CardView cardView, Setting setting, float f, float f2, boolean z) {
        if (setting.degree > 0.0f) {
            cardView.setRotationY((z ? -1 : 1) * setting.degree * f2);
        }
        ViewCompat.setX(cardView, f * f2);
    }

    @Override // com.infideap.drawerbehavior.AdvanceDrawerLayout
    AdvanceDrawerLayout.Setting createSetting() {
        return new Setting();
    }

    public void setViewRotation(int i, float f) {
        Setting setting;
        int drawerViewAbsoluteGravity = getDrawerViewAbsoluteGravity(i);
        if (!this.settings.containsKey(Integer.valueOf(drawerViewAbsoluteGravity))) {
            setting = (Setting) createSetting();
            this.settings.put(Integer.valueOf(drawerViewAbsoluteGravity), setting);
        } else {
            setting = (Setting) this.settings.get(Integer.valueOf(drawerViewAbsoluteGravity));
        }
        setting.degree = f;
        setting.scrimColor = 0;
        setting.drawerElevation = 0.0f;
    }

    
    public class Setting extends AdvanceDrawerLayout.Setting {
        float degree;

        Setting() {
            super();
        }
    }
}
