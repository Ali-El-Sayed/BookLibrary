package com.example.bookslibrary.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;

import com.example.bookslibrary.R;
import com.example.bookslibrary.databinding.ActivityBookDetailsBinding;
import com.example.bookslibrary.model.Book;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class BookDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        ActivityBookDetailsBinding binding = DataBindingUtil.setContentView(this,
                R.layout.activity_book_details);

        Book book = getIntent().getParcelableExtra("Book");
        binding.setBook(book);

    }
}