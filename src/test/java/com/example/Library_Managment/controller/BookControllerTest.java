package com.example.Library_Managment.controller;

import com.example.Library_Managment.Services.BookService;
import com.example.Library_Managment.Services.BorrowHistoryService;
import com.example.Library_Managment.model.Book;
import com.example.Library_Managment.repository.BookRepository;
import com.example.Library_Managment.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class BookControllerTest {

    @Mock
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BorrowHistoryService borrowHistoryService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private BookController bookController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // -------------------- TEST 1: getAllBooks --------------------
    @Test
    void testGetAllBooks() {
        // Arrange
        Book book1 = new Book();
        book1.setId(1L);
        book1.setBookName("Book One");

        Book book2 = new Book();
        book2.setId(2L);
        book2.setBookName("Book Two");

        when(bookService.getAllBooks()).thenReturn(List.of(book1, book2));

        // Act
        ResponseEntity<List<Book>> response = bookController.getAllBooks();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        assertEquals("Book One", response.getBody().get(0).getBookName());
        verify(bookService, times(1)).getAllBooks();
    }

    // -------------------- TEST 2: searchBooks - Books found --------------------
    @Test
    void testSearchBooks_Found() {
        // Arrange
        Book book = new Book();
        book.setBookName("Java");
        when(bookService.searchBooks("Java")).thenReturn(List.of(book));

        // Act
        ResponseEntity<?> response = bookController.searchBooks("Java");

        // Assert
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals("Books found", responseBody.get("message"));
        assertEquals(1, ((List<?>) responseBody.get("data")).size());
        verify(bookService, times(1)).searchBooks("Java");
    }

    // -------------------- TEST 3: searchBooks - No results --------------------
    @Test
    void testSearchBooks_NoResults() {
        // Arrange
        when(bookService.searchBooks("Python")).thenReturn(List.of());

        // Act
        ResponseEntity<?> response = bookController.searchBooks("Python");

        // Assert
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals("No results found", responseBody.get("message"));
        assertTrue(((List<?>) responseBody.get("data")).isEmpty());
        verify(bookService, times(1)).searchBooks("Python");
    }

    // -------------------- TEST 4: borrowBook --------------------
    @Test
    void testBorrowBook() {
        // Arrange
        Long bookId = 1L;
        String token = "jwt.token";
        String authHeader = "Bearer " + token;
        String expectedMessage = "Book borrowed successfully";

        when(bookService.borrowBook(bookId, token)).thenReturn(expectedMessage);

        // Act
        ResponseEntity<?> response = bookController.borrowBook(bookId, authHeader);

        // Assert
        Map<String, String> body = (Map<String, String>) response.getBody();
        assertEquals(expectedMessage, body.get("message"));
        verify(bookService, times(1)).borrowBook(bookId, token);
    }

    // -------------------- TEST 5: borrowBook - Missing Bearer prefix --------------------

}
