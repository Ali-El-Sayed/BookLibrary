package com.example.bookslibrary.util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class SpUtil {
    private SpUtil() {
    }

    public static final String PREF_NAME = "BooksPreferences";
    public static final String POSITION = "position";
    public static final String QUERY = "query";

    public static SharedPreferences getPreference(@NonNull Context context) {
        // MODE_PRIVATE -> Can only be accessed by the application that created them
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static String getPrefString(Context context, String key) {
        return getPreference(context).getString(key, "");
    }

    public static int getPrefInt(Context context, String key) {
        return getPreference(context).getInt(key, 0);
    }

    public static void setPrefString(Context context, String key, String value) {
        SharedPreferences.Editor pref = getPreference(context).edit();
        pref.putString(key, value);
        pref.apply();
    }

    public static void setPrefInt(Context context, String key, int value) {
        SharedPreferences.Editor pref = getPreference(context).edit();
        pref.putInt(key, value);
        pref.apply();
    }

    @NonNull
    public static ArrayList<String> getQueryList(Context context) {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i <= 5; i++) {
            String query = getPrefString(context, QUERY + i);
            if (!query.isEmpty())
                list.add(query.replace(",", " ").trim());
        }
        return list;
    }

}
