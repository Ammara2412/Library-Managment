package com.example.Library_Managment.service;

import com.example.Library_Managment.Services.BookService;
import com.example.Library_Managment.model.Book;
import com.example.Library_Managment.model.BorrowHistory;
import com.example.Library_Managment.model.userregister;
import com.example.Library_Managment.repository.BookRepository;
import com.example.Library_Managment.repository.BorrowHistoryRepository;
import com.example.Library_Managment.repository.UserRepository;
import com.example.Library_Managment.security.JwtUtil;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BorrowHistoryRepository borrowHistoryRepository;
    @Mock
    private JavaMailSender mailSender;
    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private BookService bookService;

    private Book book;
    private userregister user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock book
        book = new Book();
        book.setId(1L);
        book.setBookName("Test Book");
        book.setAuthorName("John Doe");
        book.setGenre("Fiction");
        book.setDescription("Sample description");
        book.setPdfpath("books/test.pdf");
        book.setBorrowed(false);

        // Mock user
        user = new userregister();
        user.setId(100L);
        user.setEmail("user@example.com");
        user.setFirstName("Ammara");
    }

    // ✅ Test for searchBooks()
    @Test
    void testSearchBooks_WithKeyword() {
        when(bookRepository.searchBooks("Test")).thenReturn(List.of(book));

        List<Book> result = bookService.searchBooks("Test");

        assertEquals(1, result.size());
        assertEquals("Test Book", result.get(0).getBookName());
        verify(bookRepository, times(1)).searchBooks("Test");
    }

    // ✅ Test for searchBooks() with empty keyword
    @Test
    void testSearchBooks_EmptyKeyword() {
        when(bookRepository.findAll()).thenReturn(List.of(book));

        List<Book> result = bookService.searchBooks("");

        assertEquals(1, result.size());
        verify(bookRepository, times(1)).findAll();
    }

    // ✅ Test for borrowBook()
    @Test
    void testBorrowBook_Success() {
        String token = "mock.jwt.token";

        when(jwtUtil.extractUsername(token)).thenReturn(user.getEmail());
        when(jwtUtil.extractUserId(token)).thenReturn(user.getId());
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(mailSender.createMimeMessage()).thenReturn(mock(MimeMessage.class));

        String response = bookService.borrowBook(book.getId(), token);

        assertEquals("Book borrowed successfully!", response);
        assertTrue(book.isBorrowed());
        assertNotNull(book.getIssuedDate());
        assertNotNull(book.getDueDate());

        verify(bookRepository, times(1)).save(book);
        verify(borrowHistoryRepository, times(1)).save(any(BorrowHistory.class));
        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    // ✅ Test when book is already borrowed
    @Test
    void testBorrowBook_AlreadyBorrowed() {
        String token = "mock.jwt.token";
        book.setBorrowed(true);

        when(jwtUtil.extractUserId(token)).thenReturn(user.getId());
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));

        String response = bookService.borrowBook(book.getId(), token);

        assertEquals("Book already borrowed!", response);
        verify(bookRepository, never()).save(any());
    }

    // ✅ Test when book not found
    @Test
    void testBorrowBook_BookNotFound() {
        String token = "mock.jwt.token";
        when(bookRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> bookService.borrowBook(999L, token));

        assertEquals("Book not found", exception.getMessage());
    }

    // ✅ Test revokeAccessForOverdueBooks()

}
