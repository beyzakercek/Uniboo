package com.example.uniboo;

public class Books{
    String bookName, bookAuthor, usedInUniversity, usingStatus, bookImage;
    float bookPrice;

    public Books(String bookName, String bookAuthor, String usedInUniversity, String usingStatus, String bookImage, float bookPrice) {
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.usedInUniversity = usedInUniversity;
        this.usingStatus = usingStatus;
        this.bookImage = bookImage;
        this.bookPrice = bookPrice;
    }
    public Books(){

    }

    public String getBookName() {
        return bookName;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public String getusedInUniversity() {
        return usedInUniversity;
    }

    public String getUsingStatus() {
        return usingStatus;
    }

    public String getBookImage() {
        return bookImage;
    }

    public float getBookPrice() {
        return bookPrice;
    }
}