package com.example.bookslibrary;

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

import com.example.bookslibrary.adapter.BooksAdapter;
import com.example.bookslibrary.model.Book;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class BookListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private ProgressBar mProgressBar;
    private TextView mErrorMessage;
    private RecyclerView mRvBooks;

    private static final String TAG = "BookListActivity";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = findViewById(R.id.pb_loading);
        mRvBooks = findViewById(R.id.rv_books);
        mErrorMessage = findViewById(R.id.tv_error);

        LinearLayoutManager bookLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRvBooks.setLayoutManager(bookLayoutManager);

        URL bookUrl;
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.advanced_search) {
            startActivity(new Intent(this, SearchActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        try {
            URL bookUrl = ApiUtil.buildUrl(query.trim().toLowerCase());
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