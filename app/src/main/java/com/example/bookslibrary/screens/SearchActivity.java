package com.example.bookslibrary.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.bookslibrary.R;
import com.example.bookslibrary.databinding.ActivitySearchBinding;
import com.example.bookslibrary.util.ApiUtil;
import com.example.bookslibrary.util.SpUtil;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = "SearchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ActivitySearchBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_search);


        binding.btnSearch.setOnClickListener(view -> {
            String title = binding.etTitle.getText().toString().trim().toLowerCase();
            String author = binding.etAuthor.getText().toString().trim().toLowerCase();
            // International Standard Book Number
            String isbn = binding.etISBN.getText().toString().trim().toLowerCase();
            if (isbn.contains("-")) isbn = isbn.replace("-", "");

            String publisher = binding.etPublisher.getText().toString().trim().toLowerCase();

            if (title.isEmpty() && author.isEmpty() && publisher.isEmpty() && isbn.isEmpty()) {
                String message = getString(R.string.no_search_terms);
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            } else {
                String queryURL = ApiUtil.buildUrl(title, author, publisher, isbn).toString();
                Intent intent = new Intent(getApplicationContext(), BookListActivity.class);
                Context context = getApplicationContext();
                int position = SpUtil.getPrefInt(context, SpUtil.POSITION);
                if (position == 0 || position == 5) position = 1;
                else ++position;

                String key = SpUtil.QUERY + position;
                String value = title + "," + author + publisher + "," + isbn;

                SpUtil.setPrefString(context, key, value);
                SpUtil.setPrefInt(context, SpUtil.POSITION, position);
                intent.putExtra("QUERY", queryURL);
                startActivity(intent);
                finish();
            }

        });

    }
}