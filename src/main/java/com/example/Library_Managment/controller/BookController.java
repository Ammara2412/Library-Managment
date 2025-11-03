
// BookController.java

package com.example.Library_Managment.controller;

import com.example.Library_Managment.dto.BorrowHistoryDTO;
import com.example.Library_Managment.model.Book;
import com.example.Library_Managment.model.BorrowHistory;
import com.example.Library_Managment.repository.BorrowHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.Library_Managment.model.BorrowHistory;
import com.example.Library_Managment.Services.BorrowHistoryService;
import java.io.File;
import java.time.LocalDate;
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
    private BookRepository bookRepository;
    @Autowired
    private  BorrowHistoryService borrowHistoryService;
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }


    @GetMapping("/search")
    public ResponseEntity<?> searchBooks(@RequestParam(required = false) String keyword) {
        List<Book> results = bookService.searchBooks(keyword);

        if (results.isEmpty()) {
            return ResponseEntity.ok(Map.of("message", "No results found", "data", results));
        }

        return ResponseEntity.ok(Map.of("message", "Books found", "data", results));
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













//Currntly not needed,

 /* @GetMapping("/download/{bookId}")
    public ResponseEntity<?> downloadBook(@PathVariable Long bookId, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        Long userId = jwtUtil.extractUserId(token);

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        // 🔒 Restrict access if overdue or revoked
        if (book.isAccessRevoked() || LocalDate.now().isAfter(book.getDueDate())) {
            return ResponseEntity.status(403)
                    .body(Map.of("message", "Access denied: Book is overdue. Please renew or contact library."));
        }

        // Optional: Verify that the requesting user is the one who borrowed the book
        if (!book.getBorrowedByUserId().equals(userId)) {
            return ResponseEntity.status(403)
                    .body(Map.of("message", "You didn’t borrow this book."));
        }

        // ✅ Proceed with serving the file
        try {
            File file = new File("src/main/resources/static/" + book.getPdfPath());
            if (!file.exists()) {
                return ResponseEntity.status(404).body(Map.of("message", "File not found"));
            }

            org.springframework.core.io.InputStreamResource resource =
                    new org.springframework.core.io.InputStreamResource(new java.io.FileInputStream(file));

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=" + file.getName())
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("message", "Error while downloading file"));
        }
    }
*/






