package com.example.bookslibrary;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class ApiUtil {
    private static final String QUERY_PARAMETER_KEY = "q";
    public static final String KEY = "key";
    public static final String API_KEY = "AIzaSyAu5wT4RLMWNyUKkYu2Ilu4ZaDAFMXgK-o";

    private ApiUtil() {
    }

    public static final String BASE_API_URL = "https://www.googleapis.com/books/v1/volumes";


    public static URL buildUrl(String title) {
//        StringBuilder fullUrl = new StringBuilder();
//        fullUrl.append(BASE_API_URL).append("?q=").append(title);
        URL url = null;
        Uri uri = Uri.parse(BASE_API_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAMETER_KEY, title)
                .build();
        try {
            url = new URL(uri.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    @Nullable
    public static String getJson(@NonNull URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            InputStream stream = connection.getInputStream();
            Scanner scanner = new Scanner(stream);
            scanner.useDelimiter("\\A");
            boolean hasData = scanner.hasNext();
            if (hasData) return scanner.next();
            else return null;
        } catch (Exception e) {
            Log.d("Error", "getJson:" + e);
            return null;
        } finally {
            connection.disconnect();
        }
    }


}
