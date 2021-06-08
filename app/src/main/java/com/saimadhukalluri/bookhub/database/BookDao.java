package com.saimadhukalluri.bookhub.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface BookDao {
    //the first function is inserting the book
    @Insert
    void insertBook(BookEntity bookEntity);

    //the next function will be deleting
    @Delete
    void deleteBook(BookEntity bookEntity);

    //getting all the books details
    @Query("select * from books")
    List<BookEntity> getAllBooks();

    //getting the book by ID
    @Query("select * from books where book_id=:bookId")
    BookEntity getBookById(int bookId);
}
