package com.example.bookslibrary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookslibrary.R;
import com.example.bookslibrary.model.Book;

import java.util.ArrayList;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BookViewHolder> {
    private ArrayList<Book> books = new ArrayList<>();

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

    public static class BookViewHolder extends RecyclerView.ViewHolder {
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
        }

        public void bind(@NonNull Book books) {
            mTvTitle.setText(books.title);
            mTvPublisher.setText(books.publisher);
            mTvPublishedDate.setText(books.publishedDate);
            StringBuilder author = new StringBuilder();
            for (int i = 0; i < books.authors.length; i++) {
                author.append(books.authors[i]);
                if (i + 1 < books.authors.length) author.append(", ");
            }
            mTvAuthors.setText(author.toString());
        }
    }
}
