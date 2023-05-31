package com.libraryspring.controller;

import com.libraryspring.dto.BookDTO;
import com.libraryspring.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RestController
@RequestMapping("/book")
@CrossOrigin(origins = "http://localhost:8081")
@RequiredArgsConstructor
public class BookController {

    @Autowired
    private final BookService bookService;

    @GetMapping("")
    public ResponseEntity<?> getAllBook() {
        return ResponseEntity.ok(bookService.findAllBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBook(@PathVariable Integer id) {
        return ResponseEntity.ok(bookService.findBook(id));
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addBook(@RequestBody BookDTO bookDTO) {
        if (bookService.addBook(bookDTO)) {
            return ResponseEntity.ok("add successful");
        }else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateBook(@RequestBody BookDTO bookDTO) {
        if (bookService.updateBook(bookDTO)) {
            return ResponseEntity.ok("update successful");
        }else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteBook(@RequestBody Map<String, String> body) {
        int id = Integer.parseInt(body.get("id"));
        if (bookService.deleteBook(id)){
            return ResponseEntity.ok("delete successful");
        }else {
            return ResponseEntity.badRequest().build();
        }
    }
}
