package com.example.tasbeeh;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SharedPreferencesUtils {
    private static final String TASBEEH_LIST_KEY = "tasbeeh_list";
    private static final String CURRENT_COUNT_KEY = "current_count";

    private SharedPreferences sharedPreferences;
    private Gson gson = new Gson();

    public SharedPreferencesUtils(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public List<TasbeehItem> getSavedTasbeehItems() {
        String json = sharedPreferences.getString(TASBEEH_LIST_KEY, "");
        Type type = new TypeToken<List<TasbeehItem>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public void saveTasbeehItems(List<TasbeehItem> tasbeehItems) {
        String json = gson.toJson(tasbeehItems);
        sharedPreferences.edit().putString(TASBEEH_LIST_KEY, json).apply();
    }

    public int getCurrentCount() {
        return sharedPreferences.getInt(CURRENT_COUNT_KEY, 0);
    }

    public void setCurrentCount(int count) {
        sharedPreferences.edit().putInt(CURRENT_COUNT_KEY, count).apply();
    }
}
