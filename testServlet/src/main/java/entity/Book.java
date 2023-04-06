package entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class Book {
    private int id;
    private String name;
    private String author;
    private String genre;
    private int quantity;
    private int year;

    public Book(String name, String author, String genre, int quantity, int year) {
        this.name = name;
        this.author = author;
        this.genre = genre;
        this.quantity = quantity;
        this.year = year;
    }

    public Book(String name, String author, String genre, int quantity) {
        this.name = name;
        this.author = author;
        this.genre = genre;
        this.quantity = quantity;
    }
}
