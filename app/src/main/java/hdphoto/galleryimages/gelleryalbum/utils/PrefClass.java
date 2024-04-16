package hdphoto.galleryimages.gelleryalbum.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;


public class PrefClass {
    protected SharedPreferences sharedPreferences;

    public PrefClass(Context context) {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public int getInt(String str, int i) {
        return this.sharedPreferences.getInt(str, i);
    }


    public String getString(String str) {
        return this.sharedPreferences.getString(str, "");
    }

    public ArrayList<String> getListString(String str) {
        return new ArrayList<>(Arrays.asList(TextUtils.split(this.sharedPreferences.getString(str, ""), "‚‗‚")));
    }

    public void putInt(String str, int i) {
        this.sharedPreferences.edit().putInt(str, i).apply();
    }


    public void putString(String str, String str2) {
        this.sharedPreferences.edit().putString(str, str2).apply();
    }

    public void putListString(String str, ArrayList<String> arrayList) {
        this.sharedPreferences.edit().putString(str, TextUtils.join("‚‗‚", (String[]) arrayList.toArray(new String[arrayList.size()]))).apply();
    }

    public void remove(String str) {
        this.sharedPreferences.edit().remove(str).apply();
    }

    public void clear() {
        this.sharedPreferences.edit().clear().apply();
    }

    public Map<String, ?> getAll() {
        return this.sharedPreferences.getAll();
    }
}
