package com.rcpl.digilib;

/**
 * Created by vartika on 19/02/2018.
 */

public class Players {
    private String bookId,bookName,bookAuthor,bookGenre;

    public Players(String bookId, String bookName, String bookAuthor,String bookGenre) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.bookGenre=bookGenre;
    }

    public String getBookId() {
        return bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }
    public String getBookGenre() {
        return bookGenre;
    }
}