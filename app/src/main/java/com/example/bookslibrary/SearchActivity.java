package com.example.bookslibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.bookslibrary.databinding.ActivitySearchBinding;

import java.net.URL;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = "SearchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ActivitySearchBinding binding = DataBindingUtil
                .setContentView(this, R.layout.activity_search);


        binding.btnSearch.setOnClickListener(view -> {
            String title = binding.etTitle.getText().toString().trim().toLowerCase();
            String author = binding.etAuthor.getText().toString().trim().toLowerCase();
            // International Standard Book Number
            String isbn = binding.etISBN.getText().toString().trim().toLowerCase();
            if (isbn.contains("-"))
                isbn = isbn.replace("-", "");

            String publisher = binding.etPublisher.getText().toString().trim().toLowerCase();

            if (title.isEmpty() && author.isEmpty() && publisher.isEmpty() && isbn.isEmpty()) {
                String message = getString(R.string.no_search_terms);
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            } else {
                Log.d(TAG, "onCreate: " + isbn);
                String queryURL = ApiUtil.buildUrl(title, author, publisher, isbn).toString();
                Log.d(TAG, "onCreate: " + queryURL);
                Intent intent = new Intent(getApplicationContext(), BookListActivity.class);
                intent.putExtra("QUERY", queryURL);
                startActivity(intent);
                finish();
            }

        });

    }
}