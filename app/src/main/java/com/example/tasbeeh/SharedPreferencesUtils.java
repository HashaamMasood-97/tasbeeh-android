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
    private static final String SELECTED_ITEM_POSITION_KEY = "selected_item_position";
    private static final String IS_UPDATE_MODE_KEY = "is_update_mode";


    private SharedPreferences sharedPreferences;
    private Gson gson = new Gson();

    public SharedPreferencesUtils(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public List<TasbeehItem> getSavedTasbeehItems() {
        String json = sharedPreferences.getString(TASBEEH_LIST_KEY, "");
        Type type = new TypeToken<List<TasbeehItem>>() {}.getType();
        List<TasbeehItem> tasbeehItems = gson.fromJson(json, type);

        // Handle null or old data where isUpdateMode is not available
        if (tasbeehItems != null) {
            for (TasbeehItem item : tasbeehItems) {
                if (item.getUpdateMode() == null) {
                    item.setUpdateMode(false);
                }
            }
        } else {
            tasbeehItems = new ArrayList<>();
        }

        return tasbeehItems;
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

    public int getSelectedItemPosition() {
        return sharedPreferences.getInt(SELECTED_ITEM_POSITION_KEY, -1);
    }

    public void setSelectedItemPosition(int position) {
        sharedPreferences.edit().putInt(SELECTED_ITEM_POSITION_KEY, position).apply();
    }

    public boolean getIsUpdateMode() {
        return sharedPreferences.getBoolean(IS_UPDATE_MODE_KEY, false);
    }

    public void setIsUpdateMode(boolean isUpdateMode) {
        sharedPreferences.edit().putBoolean(IS_UPDATE_MODE_KEY, isUpdateMode).apply();
    }



}

