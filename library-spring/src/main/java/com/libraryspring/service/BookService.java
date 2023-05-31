package com.libraryspring.service;

import com.libraryspring.dto.BookDTO;
import com.libraryspring.entity.Book;
import com.libraryspring.repository.BookRepository;
import com.libraryspring.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class BookService {
    @Autowired
    BookRepository bookRepository;
    @Autowired
    OrderRepository orderRepository;

    public Map<String, Object> findAllBooks(){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("books", bookRepository.findAll());
        return resultMap;
    }
    public  Map<String, Object> findBook(int id){
        Map<String, Object> resultMap = new HashMap<>();
        Book book = bookRepository.findById(id);
        int count = orderRepository.contTakenBook(book);
        resultMap.put("book", BookDTO.ConvertToDTO(book));
        resultMap.put("count", count);
        return resultMap;
    }
    public boolean addBook(BookDTO bookDTO){
        Book exBook = bookRepository.findByName(bookDTO.getName());
        if (exBook != null){
            return false;
        }
        Book book = bookDTO.ConvertToBook();
        bookRepository.save(book);
        return true;
    }

    public boolean updateBook(BookDTO bookDTO){
        Book exBook = bookRepository.findByName(bookDTO.getName());
        if (exBook == null){
            return false;
        }
        Book book = bookDTO.ConvertToBook();
        book.setId(exBook.getId());
        bookRepository.save(book);
        return true;
    }
    public boolean deleteBook(int id){
        bookRepository.deleteById(id);
        return true;
    }
}
