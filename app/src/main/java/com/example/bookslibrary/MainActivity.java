package com.example.bookslibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private ProgressBar mProgressBar;
    private TextView mTvBooks;
    private TextView mErrorMessage;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = findViewById(R.id.pb_loading);
        mTvBooks = findViewById(R.id.tv_books);
        mErrorMessage = findViewById(R.id.tv_error);

        try {
            URL bookUrl = ApiUtil.buildUrl("android");
            new BooksQueryTask().execute(bookUrl);
        } catch (Exception e) {
            Log.d("MainActivity", "onCreate: " + e);
        }
    }

    public class BooksQueryTask extends AsyncTask<URL, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
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
            Log.d("RESULT", "onPostExecute: "+result);
            mProgressBar.setVisibility(View.GONE);
            if (result == null) mErrorMessage.setVisibility(View.VISIBLE);
            else mTvBooks.setText(result);
        }
    }
}