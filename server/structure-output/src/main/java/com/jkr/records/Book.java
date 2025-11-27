package com.jkr.records;

import lombok.Data;

@Data
public class Book {

    private int id;

    private String bookName;

    public Book() {
    }

    public Book(int id, String bookName) {
        this.id = id;
        this.bookName = bookName;
    }
}
