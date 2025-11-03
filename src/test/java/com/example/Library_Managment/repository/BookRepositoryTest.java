package com.example.Library_Managment.repository;

import com.example.Library_Managment.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest  // Uses in-memory H2 DB for repository testing
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();

        Book book1 = new Book();
        book1.setBookName("Clean Code");
        book1.setAuthorName("Robert Martin");
        book1.setGenre("Programming");

        Book book2 = new Book();
        book2.setBookName("The Alchemist");
        book2.setAuthorName("Paulo Coelho");
        book2.setGenre("Fiction");

        Book book3 = new Book();
        book3.setBookName("Effective Java");
        book3.setAuthorName("Joshua Bloch");
        book3.setGenre("Programming");

        bookRepository.saveAll(List.of(book1, book2, book3));
    }

    // -------------------- TEST 1: searchBooks by book name --------------------
    @Test
    void testSearchBooks_ByBookName() {
        List<Book> results = bookRepository.searchBooks("Clean");
        assertEquals(1, results.size());
        assertEquals("Clean Code", results.get(0).getBookName());
    }

    // -------------------- TEST 2: searchBooks by author name --------------------
    @Test
    void testSearchBooks_ByAuthorName() {
        List<Book> results = bookRepository.searchBooks("Bloch");
        assertEquals(1, results.size());
        assertEquals("Effective Java", results.get(0).getBookName());
    }

    // -------------------- TEST 3: searchBooks by genre --------------------
    @Test
    void testSearchBooks_ByGenre() {
        List<Book> results = bookRepository.searchBooks("programming");
        assertEquals(2, results.size()); // Clean Code + Effective Java
    }

    // -------------------- TEST 4: searchBooks - no matches --------------------
    @Test
    void testSearchBooks_NoMatch() {
        List<Book> results = bookRepository.searchBooks("Cooking");
        assertTrue(results.isEmpty());
    }

    // -------------------- TEST 5: searchBooks - case insensitive --------------------
    @Test
    void testSearchBooks_CaseInsensitive() {
        List<Book> results = bookRepository.searchBooks("aLcHeMiSt");
        assertEquals(1, results.size());
        assertEquals("The Alchemist", results.get(0).getBookName());
    }
}
