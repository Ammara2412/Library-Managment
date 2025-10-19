
// BookController.java

package com.example.Library_Managment.controller;

import com.example.Library_Managment.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.example.Library_Managment.Services.BookService;
import org.springframework.http.ResponseEntity;
import java.util.List;
import com.example.Library_Managment.repository.BookRepository;

import com.example.Library_Managment.security.JwtUtil;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
@CrossOrigin
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }


    @GetMapping("/search")
    public List<Book> searchBooks(@RequestParam("keyword") String keyword) {
        return bookService.searchBooks(keyword);
    }

    @PostMapping("/borrow/{bookId}")
    public ResponseEntity<?> borrowBook(
            @PathVariable Long bookId,
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHeader.substring(7); // remove "Bearer "
        String message = bookService.borrowBook(bookId, token);
        return ResponseEntity.ok(Map.of("message", message));


    }


}




