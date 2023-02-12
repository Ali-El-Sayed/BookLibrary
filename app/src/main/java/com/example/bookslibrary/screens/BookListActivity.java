package com.example.bookslibrary.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.bookslibrary.R;
import com.example.bookslibrary.adapter.BooksAdapter;
import com.example.bookslibrary.model.Book;
import com.example.bookslibrary.util.ApiUtil;
import com.example.bookslibrary.util.SpUtil;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class BookListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private ProgressBar mProgressBar;
    private TextView mErrorMessage;
    private RecyclerView mRvBooks;
    private URL bookUrl;

    private static final String TAG = "BookListActivity";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = findViewById(R.id.pb_loading);
        mRvBooks = findViewById(R.id.rv_books);
        mErrorMessage = findViewById(R.id.tv_error);

        LinearLayoutManager bookLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRvBooks.setLayoutManager(bookLayoutManager);

        Intent intent = getIntent();
        String query = intent.getStringExtra("QUERY");

        Log.d(TAG, "onCreate: " + query);
        try {
            if (query == null || query.isEmpty())
                bookUrl = ApiUtil.buildUrl("harry potter");
            else
                bookUrl = new URL(query);

            new BooksQueryTask().execute(bookUrl);
        } catch (Exception e) {
            Log.d("MainActivity", "onCreate: " + e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.book_list_menu, menu);
        MenuItem search = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) search.getActionView();
        searchView.setOnQueryTextListener(this);
        ArrayList<String> recentSearch = SpUtil.getQueryList(getApplicationContext());
        int itemsNum = recentSearch.size();
        MenuItem recentMenu;
        for (int i = 0; i < itemsNum; i++)// ID - Text
            recentMenu = menu.add(Menu.NONE, i, Menu.NONE, recentSearch.get(i));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.advanced_search:
                startActivity(new Intent(this, SearchActivity.class));
                return true;

            default:
                int position = item.getItemId() + 1;
                String preferenceName = SpUtil.QUERY + position;
                String query = SpUtil.getPrefString(getApplicationContext(), preferenceName);
                String[] prefParams = query.split(",");
                String[] queryParams = new String[4];

                System.arraycopy(prefParams, 0, queryParams, 0, prefParams.length);

                bookUrl = ApiUtil.buildUrl(
                        (queryParams[0] == null) ? "" : queryParams[0],
                        (queryParams[1] == null) ? "" : queryParams[1],
                        (queryParams[2] == null) ? "" : queryParams[2],
                        (queryParams[3] == null) ? "" : queryParams[3]
                );
                new BooksQueryTask().execute(bookUrl);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        try {
            bookUrl = ApiUtil.buildUrl(query.trim().toLowerCase());
            new BooksQueryTask().execute(bookUrl);
        } catch (Exception e) {
            Log.d("search book error", "onQueryTextSubmit: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    public class BooksQueryTask extends AsyncTask<URL, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
            mErrorMessage.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            String result;
            try {
                result = ApiUtil.getJson(searchUrl);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            mProgressBar.setVisibility(View.GONE);
            if (result == null) mErrorMessage.setVisibility(View.VISIBLE);
            else {
                ArrayList<Book> books = ApiUtil.getBooksFromJson(result);
                BooksAdapter booksAdapter = new BooksAdapter(books);
                mRvBooks.setAdapter(booksAdapter);
            }
        }
    }
}