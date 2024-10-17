package com.rcpl.digilib;

/**
 * Created by HP on 02-Apr-18.
 */

public class Favourites {
    private String bookName,authorName,bookID,bookUrl;

    public String getBookUrl() {
        return bookUrl;
    }


    public Favourites(String bookName, String authorName, String bookID, String bookUrl) {
        this.bookName = bookName;
        this.authorName = authorName;
        this.bookID=bookID;
        this.bookUrl=bookUrl;

    }

    public String getBookID() {
        return bookID;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
}
