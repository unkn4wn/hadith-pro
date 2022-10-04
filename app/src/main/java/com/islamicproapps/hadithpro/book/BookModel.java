package com.islamicproapps.hadithpro.book;

public class BookModel {
    String bookName;
    String displayName;
    String bookScholar;

    int bookIconColor;
    String bookIconText;

    public BookModel(String bookName, String displayName,String bookScholar, int bookIconColor, String bookIconText) {
        this.bookName = bookName;
        this.displayName = displayName;
        this.bookScholar = bookScholar;
        this.bookIconColor = bookIconColor;
        this.bookIconText = bookIconText;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getBookScholar() {
        return bookScholar;
    }

    public void setBookScholar(String bookScholar) {
        this.bookScholar = bookScholar;
    }

    public int getBookIconColor() {
        return bookIconColor;
    }

    public void setBookIconColor(int bookIconColor) {
        this.bookIconColor = bookIconColor;
    }

    public String getBookIconText() {
        return bookIconText;
    }

    public void setBookIconText(String bookIconText) {
        this.bookIconText = bookIconText;
    }
}
