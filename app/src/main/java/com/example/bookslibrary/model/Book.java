package com.example.bookslibrary.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;

import com.example.bookslibrary.R;
import com.squareup.picasso.Picasso;

public class Book implements Parcelable {
    public String id;
    public String title;
    public String subTitle;
    public String description;
    public String authors;
    public String publisher;
    public String thumbnail;

    public Book(String id, String title, String subTitle, String[] authors, String publisher, String publishedDate, String description, String thumbnail) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.subTitle = subTitle;
        this.authors = TextUtils.join(", ", authors);
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.thumbnail = thumbnail;
        this.changeProtocol();
    }

    public String publishedDate;

    protected Book(Parcel in) {
        id = in.readString();
        title = in.readString();
        subTitle = in.readString();
        authors = in.readString();
        publisher = in.readString();
        publishedDate = in.readString();
        description = in.readString();
        thumbnail = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(subTitle);
        parcel.writeString(authors);
        parcel.writeString(publisher);
        parcel.writeString(publishedDate);
        parcel.writeString(description);
        parcel.writeString(thumbnail);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @BindingAdapter({"app:imageUrl"})
    public static void loadImage(ImageView imageView, String imageUrl) {
        if (!imageUrl.isEmpty())
            Picasso.get().load(imageUrl).placeholder(R.drawable.book_open_variant).into(imageView);
        else imageView.setBackgroundResource(R.drawable.book_open_variant);
        Log.d("thumbnail", "loadImage: " + imageUrl);
    }

    private void changeProtocol() {
        this.thumbnail = this.thumbnail.replaceFirst("http", "https");
    }
}
