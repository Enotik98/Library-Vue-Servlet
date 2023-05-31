package com.libraryspring.dto;

import com.libraryspring.entity.Book;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookDTO {
    private int id;
    private String name;
    private String author;
    private String genre;
    private int quantity;
    private int year;

    public static BookDTO ConvertToDTO(Book book) {
        BookDTO dto = new BookDTO();
        dto.setId(book.getId());
        dto.setName(book.getName());
        dto.setAuthor(book.getAuthor());
        dto.setGenre(book.getGenre());
        dto.setQuantity(book.getQuantity());
        dto.setYear(book.getYear());
        return dto;
    }
    public Book ConvertToBook() {
        Book book = new Book();
        book.setId(this.id);
        book.setName(this.name);
        book.setAuthor(this.author);
        book.setGenre(this.genre);
        book.setQuantity(this.quantity);
        book.setYear(this.year);
        return book;
    }
}
