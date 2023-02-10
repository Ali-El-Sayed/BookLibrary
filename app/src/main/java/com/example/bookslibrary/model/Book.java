package com.example.bookslibrary.model;

public class Book {
    public String id;
    public String title;
    public String subTitle;
    public String[] authors;
    public String publisher;

    public Book(String id, String title, String subTitle, String[] authors, String publisher, String publishedDate) {
        this.id = id;
        this.title = title;
        this.subTitle = subTitle;
        this.authors = authors;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
    }

    public String publishedDate;
}
