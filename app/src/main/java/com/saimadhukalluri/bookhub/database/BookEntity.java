package com.saimadhukalluri.bookhub.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "books")
public class BookEntity {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "book_id")
    int book_id;

    @ColumnInfo(name = "book_name")
    String book_name;

    @ColumnInfo(name = "book_author_name")
    String book_author_name;

    @ColumnInfo(name = "book_desc")
    String book_desc;

    @ColumnInfo(name = "book_price")
    String book_price;

    @ColumnInfo(name = "book_rating")
    String book_rating;

    @ColumnInfo(name = "book_image")
    String book_image;

    public BookEntity(int book_id, String book_name, String book_author_name, String book_desc, String book_price, String book_rating, String book_image) {
        this.book_id = book_id;
        this.book_name = book_name;
        this.book_author_name = book_author_name;
        this.book_desc = book_desc;
        this.book_price = book_price;
        this.book_rating = book_rating;
        this.book_image = book_image;
    }

    public String getBook_image() {
        return book_image;
    }

    public void setBook_image(String book_image) {
        this.book_image = book_image;
    }

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public String getBook_author_name() {
        return book_author_name;
    }

    public void setBook_author_name(String book_author_name) {
        this.book_author_name = book_author_name;
    }

    public String getBook_desc() {
        return book_desc;
    }

    public void setBook_desc(String book_desc) {
        this.book_desc = book_desc;
    }

    public String getBook_price() {
        return book_price;
    }

    public void setBook_price(String book_price) {
        this.book_price = book_price;
    }

    public String getBook_rating() {
        return book_rating;
    }

    public void setBook_rating(String book_rating) {
        this.book_rating = book_rating;
    }
}
