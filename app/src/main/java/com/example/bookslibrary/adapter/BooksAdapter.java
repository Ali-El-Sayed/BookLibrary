package com.example.bookslibrary.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookslibrary.screens.BookDetails;
import com.example.bookslibrary.R;
import com.example.bookslibrary.model.Book;

import java.util.ArrayList;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BookViewHolder> {
    final ArrayList<Book> books;

    public BooksAdapter(ArrayList<Book> books) {
        this.books = books;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.book_list_item, parent, false);
        return new BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = books.get(position);
        holder.bind(book);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }


    public class BookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mTvTitle;
        TextView mTvAuthors;
        TextView mTvPublisher;
        TextView mTvPublishedDate;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvTitle = itemView.findViewById(R.id.tv_title);
            mTvAuthors = itemView.findViewById(R.id.tv_authors);
            mTvPublishedDate = itemView.findViewById(R.id.tv_published_date);
            mTvPublisher = itemView.findViewById(R.id.tv_publisher);
            itemView.setOnClickListener(this);
        }

        public void bind(@NonNull Book books) {
            mTvTitle.setText(books.title);
            mTvPublisher.setText(books.publisher);
            mTvPublishedDate.setText(books.publishedDate);
            mTvAuthors.setText(books.authors);
        }

        @Override
        public void onClick(@NonNull View view) {
            int position = getAdapterPosition();
            Book selectedBook = books.get(position);
            Intent intent = new Intent(view.getContext(), BookDetails.class);
            intent.putExtra("Book", selectedBook);
            view.getContext().startActivity(intent);
        }
    }
}
