package hdphoto.galleryimages.gelleryalbum.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import com.iten.tenoku.ad.AdShow;
import com.iten.tenoku.ad.HandleClick.HandleClick;
import com.iten.tenoku.utils.AdUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import hdphoto.galleryimages.gelleryalbum.adapter.LanguageAdapter;
import hdphoto.galleryimages.gelleryalbum.databinding.ActivityLanguageBinding;
import hdphoto.galleryimages.gelleryalbum.model.LangModel;
import hdphoto.galleryimages.gelleryalbum.utils.AppLangSessionManager;
import hdphoto.galleryimages.gelleryalbum.utils.Preference;
import hdphoto.galleryimages.gelleryalbum.utils.SharePrefUtil;
import hdphoto.galleryimages.gelleryalbum.utils.SystemUtil;

public class LanguageActivity extends AppCompatActivity {

    AppLangSessionManager appLangSessionManager;
    ActivityLanguageBinding binding;
    List<LangModel> languageList = new ArrayList<>();
    String codeLang = "en";
    String langDevice = "en";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SystemUtil.setLocale(this);
        super.onCreate(savedInstanceState);
        binding = ActivityLanguageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        appLangSessionManager = new AppLangSessionManager(this);


        Configuration configuration = new Configuration();
        Locale locale = Locale.getDefault();
        this.langDevice = locale.getLanguage();
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
        Locale.setDefault(locale);
        configuration.locale = locale;

        final SharedPreferences sharedPreferences = getBaseContext().getSharedPreferences("LANGUAGE", 0);

        setLang();
        setSelect();

        binding.toolbar.btnBack.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.rvLanguage.setLayoutManager(new LinearLayoutManager(this));
        binding.rvLanguage.setAdapter(new LanguageAdapter(this, languageList, new LanguageAdapter.IClickLanguage() {
            @Override
            public void onClick(LangModel languageModel) {
                codeLang = languageModel.getIsoLanguage();
            }
        }));

        binding.tvNext.setOnClickListener(v -> {
            AdShow.getInstance(LanguageActivity.this).ShowAd(new HandleClick() {
                @Override
                public void Show(boolean adShow) {
                    SystemUtil.saveLocale(getBaseContext(), codeLang);
                    sharedPreferences.edit().putBoolean("language", true).apply();
                    SharePrefUtil.forceTutorial(LanguageActivity.this);

                    if (Preference.isFirstLaunch(LanguageActivity.this)) {
                        startActivity(new Intent(LanguageActivity.this, PermissionActivity.class));
                        recreate();
                    } else {
                        startActivity(new Intent(LanguageActivity.this, MainActivity.class));
                        recreate();
                    }
                }
            }, AdUtils.ClickType.MAIN_CLICK);
        });
    }

    private void setSelect() {
        for (int i = 1; i < this.languageList.size(); i++) {
            if (languageList.get(i).isoLanguage.contains(this.langDevice)) {
                languageList.get(i).setCheck(true);
                languageList.get(0).setCheck(false);
                codeLang = this.languageList.get(i).getIsoLanguage();
            }
        }
    }

    private void setLang() {
        languageList = new ArrayList();
        languageList.add(new LangModel("English", "en", true));
        languageList.add(new LangModel("Hindi", "hi", false));
        languageList.add(new LangModel("French", "fr", false));
        languageList.add(new LangModel("Spanish", "es", false));
        languageList.add(new LangModel("German", "de", false));
        languageList.add(new LangModel("Bengali", "bn", false));
        languageList.add(new LangModel("Arabic", "ar", false));
        languageList.add(new LangModel("Vietnam", "vi", false));
        languageList.add(new LangModel("japanese", "ja", false));
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}