
// BookRepository.java
package com.example.Library_Managment.repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import com.example.Library_Managment.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // Search by book name, author name, or genre (case-insensitive)
    @Query("SELECT b FROM Book b WHERE " +
            "LOWER(b.bookName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(b.authorName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(b.genre) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Book> searchBooks(@Param("keyword") String keyword);
}




