package com.saimadhukalluri.bookhub.model;

public class Book {
    String book_id;
    String name;
    String authorName;
    String prise;
    String rating;
    String bookImage;

    public Book(String book_id, String name, String authorName, String rating, String prise, String bookImage) {
        this.book_id = book_id;
        this.name = name;
        this.authorName = authorName;
        this.prise = prise;
        this.rating = rating;
        this.bookImage = bookImage;
    }

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getPrise() {
        return prise;
    }

    public void setPrise(String prise) {
        this.prise = prise;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getBookImage() {
        return bookImage;
    }

    public void setBookImage(String bookImage) {
        this.bookImage = bookImage;
    }
}
