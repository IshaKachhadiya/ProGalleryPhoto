package hdphoto.galleryimages.gelleryalbum.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import java.util.Locale;

public class SystemUtil {
    private static Locale myLocale;

    public static void saveLocale(Context context, String str) {
        setPreLanguage(context, str);
    }

    public static void setLocale(Context context) {
        String preLanguage = getPreLanguage(context);
        if (preLanguage.equals("")) {
            Configuration configuration = new Configuration();
            Locale locale = Locale.getDefault();
            Locale.setDefault(locale);
            configuration.locale = locale;
            context.getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());
            return;
        }
        changeLang(preLanguage, context);
    }

    public static void changeLang(String str, Context context) {
        if (str.equalsIgnoreCase("")) {
            return;
        }
        myLocale = new Locale(str);
        saveLocale(context, str);
        Locale.setDefault(myLocale);
        Configuration configuration = new Configuration();
        configuration.locale = myLocale;
        context.getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());
    }

    public static String getPreLanguage(Context context) {
        return context.getSharedPreferences("data", 0).getString("KEY_LANGUAGE", "en");
    }

    public static void setPreLanguage(Context context, String str) {
        if (str == null || str == "") {
            return;
        }
        SharedPreferences.Editor edit = context.getSharedPreferences("data", 0).edit();
        edit.putString("KEY_LANGUAGE", str);
        edit.apply();
    }
}
