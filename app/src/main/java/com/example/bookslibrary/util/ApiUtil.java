package com.example.bookslibrary.util;

import com.example.bookslibrary.model.Book;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class ApiUtil {
    private static final String TAG = "ApiUtil";
    private static final String QUERY_PARAMETER_KEY = "q";
    public static final String KEY = "key";
    public static final String API_KEY = "AIzaSyAu5wT4RLMWNyUKkYu2Ilu4ZaDAFMXgK-o";


    public static final String BASE_API_URL = "https://www.googleapis.com/books/v1/volumes";

    private static final String TITLE = "intitle:";
    private static final String AUTHOR = "inauthor:";
    private static final String PUBLISHER = "inpublisher:";
    private static final String ISBN = "isbn:";


    private ApiUtil() {
    }


    public static URL buildUrl(String title) {
        URL url = null;
        Uri uri = Uri.parse(BASE_API_URL)
                .buildUpon()
                .appendQueryParameter(KEY, API_KEY)
                .appendQueryParameter(QUERY_PARAMETER_KEY, title)
                .build();
        try {
            url = new URL(uri.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildUrl(@NonNull String title, String author, String publisher, String isbn) {
        URL url = null;
        StringBuilder sb = new StringBuilder();

        if (!title.isEmpty()) sb.append(TITLE).append(title).append("+");
        if (!author.isEmpty()) sb.append(AUTHOR).append(author).append("+");
        if (!publisher.isEmpty()) sb.append(PUBLISHER).append(publisher).append("+");
        if (!isbn.isEmpty()) sb.append(ISBN).append(isbn).append("+");
        sb.setLength(sb.length() - 1);
        String query = sb.toString();

        Uri uri = Uri.parse(BASE_API_URL)
                .buildUpon()
                .appendQueryParameter(KEY, API_KEY)
                .appendQueryParameter(QUERY_PARAMETER_KEY, query)
                .build();
        try {
            url = new URL(uri.toString());
            Log.d(TAG, "buildUrl: " + uri);
            Log.d(TAG, "buildUrl: " + url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    @Nullable
    public static String getJson(@NonNull URL url) throws IOException {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream stream = connection.getInputStream();
                Scanner scanner = new Scanner(stream);
                scanner.useDelimiter("\\A");
                boolean hasData = scanner.hasNext();
                if (hasData) return scanner.next();
                else return null;
            } else Log.d(TAG, "Response Code = " + connection.getResponseCode());
        } catch (Exception e) {
            Log.d("Error", "getJson:" + e);
            return null;
        } finally {
            if (connection != null) connection.disconnect();
        }
        return null;
    }

    @NonNull
    public static ArrayList<Book> getBooksFromJson(String json) {
        final String ID = "id";
        final String TITLE = "title";
        final String SUBTITLE = "subtitle";
        final String AUTHORS = "authors";
        final String PUBLISHER = "publisher";
        final String PUBLISHED_DATE = "publishedDate";
        final String ITEMS = "items";
        final String VOLUME_INFO = "volumeInfo";
        final String DESCRIPTION = "description";
        final String IMAGELINKS = "imageLinks";
        final String THUMBNAIL = "thumbnail";


        ArrayList<Book> books = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray arrayBooks = jsonObject.getJSONArray(ITEMS);
            int count = arrayBooks.length();
            for (int i = 0; i < count; i++) {
                JSONObject bookJSON = arrayBooks.getJSONObject(i);
                JSONObject volumeInfoJSON = bookJSON.getJSONObject(VOLUME_INFO);
                JSONObject imageLinks = null;
                if (volumeInfoJSON.has(IMAGELINKS))
                    imageLinks = volumeInfoJSON.getJSONObject(IMAGELINKS);

                int authorNum = 0;
                try {
                    authorNum = volumeInfoJSON.getJSONArray(AUTHORS).length();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String[] authors = new String[authorNum];
                for (int j = 0; j < authorNum; j++)
                    authors[j] = volumeInfoJSON.getJSONArray(AUTHORS).get(j).toString();
                Book book = new Book(bookJSON.getString(ID), volumeInfoJSON.getString(TITLE), (volumeInfoJSON.isNull(SUBTITLE) ? "" : volumeInfoJSON.getString(SUBTITLE)), authors, volumeInfoJSON.getString(PUBLISHER), (volumeInfoJSON.isNull(PUBLISHED_DATE) ? "" : volumeInfoJSON.getString(PUBLISHED_DATE)), (volumeInfoJSON.isNull(DESCRIPTION) ? "" : volumeInfoJSON.getString(DESCRIPTION)), (imageLinks == null ? "" : imageLinks.getString(THUMBNAIL)));
                books.add(book);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return books;
    }

}
