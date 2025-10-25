

package com.example.Library_Managment.Services;

import com.example.Library_Managment.dto.BorrowHistoryDTO;
import com.example.Library_Managment.model.BorrowHistory;
import com.example.Library_Managment.model.userregister;
import com.example.Library_Managment.repository.BorrowHistoryRepository;
import com.example.Library_Managment.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BorrowHistoryService {

    private final BorrowHistoryRepository borrowHistoryRepository;
    private final UserRepository userRepository;

    public BorrowHistoryService(BorrowHistoryRepository borrowHistoryRepository, UserRepository userRepository) {
        this.borrowHistoryRepository = borrowHistoryRepository;
        this.userRepository = userRepository;
    }

    public List<BorrowHistoryDTO> getUserBorrowHistory(Long userId) {
        userregister user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return borrowHistoryRepository.findByUser(user)
                .stream()
                .map(h -> {
                    BorrowHistoryDTO dto = new BorrowHistoryDTO();
                    dto.setBookName(h.getBook().getBookName());
                    dto.setAuthorName(h.getBook().getAuthorName());
                    dto.setGenre(h.getBook().getGenre());
                    dto.setImagePath(h.getBook().getImagePath()); // ✅ Set image path
                    dto.setBorrowDate(h.getBorrowDate());
                    dto.setDueDate(h.getDueDate());
                    dto.setReturnDate(h.getReturnDate());
                    dto.setStatus(h.getStatus().name());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}








/*

package com.example.Library_Managment.Services;

import com.example.Library_Managment.model.*;
import com.example.Library_Managment.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BorrowHistoryService {

    private final BorrowHistoryRepository borrowHistoryRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public BorrowHistoryService(BorrowHistoryRepository borrowHistoryRepository,
                                UserRepository userRepository,
                                BookRepository bookRepository) {
        this.borrowHistoryRepository = borrowHistoryRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    // Record borrow action
    public void recordBorrow(Long userId, Long bookId) {
        userregister user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (book.isBorrowed()) {
            throw new RuntimeException("Book is already borrowed");
        }

        BorrowHistory history = new BorrowHistory();
        history.setUser(user);
        history.setBook(book);
        history.setBorrowDate(LocalDate.now());
        history.setDueDate(LocalDate.now().plusDays(14)); // 2 weeks
        history.setStatus(BorrowHistory.Status.BORROWED);

        book.setBorrowed(true);
        book.setBorrowedByUserId(userId);
        book.setIssuedDate(LocalDate.now());
        book.setDueDate(LocalDate.now().plusDays(14));

        bookRepository.save(book);
        borrowHistoryRepository.save(history);
    }

    // Record return action
    public void recordReturn(Long userId, Long bookId) {
        userregister user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        BorrowHistory history = borrowHistoryRepository.findByUser(user).stream()
                .filter(h -> h.getBook().getId().equals(bookId) && h.getStatus() == BorrowHistory.Status.BORROWED)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No active borrow record found"));

        history.setReturnDate(LocalDate.now());
        history.setStatus(BorrowHistory.Status.RETURNED);

        book.setBorrowed(false);
        book.setBorrowedByUserId(null);
        book.setAccessRevoked(false);

        bookRepository.save(book);
        borrowHistoryRepository.save(history);
    }

    // Get borrow history for a user
    public List<BorrowHistory> getUserHistory(Long userId) {
        userregister user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return borrowHistoryRepository.findByUser(user);
    }
}
*/
/*

package com.example.Library_Managment.Services;

import com.example.Library_Managment.model.*;
import com.example.Library_Managment.repository.*;
import org.springframework.stereotype.Service;
import com.example.Library_Managment.dto.BorrowHistoryDTO;


import java.time.LocalDate;
import java.util.List;

@Service
public class BorrowHistoryService {

    private final BorrowHistoryRepository borrowHistoryRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public BorrowHistoryService(BorrowHistoryRepository borrowHistoryRepository,
                                UserRepository userRepository,
                                BookRepository bookRepository) {
        this.borrowHistoryRepository = borrowHistoryRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    // Record borrow action
    public void recordBorrow(Long userId, Long bookId) {
        userregister user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (book.isBorrowed()) {
            throw new RuntimeException("Book is already borrowed");
        }

        BorrowHistory history = new BorrowHistory();
        history.setUser(user);
        history.setBook(book);
        history.setBorrowDate(LocalDate.now());
        history.setDueDate(LocalDate.now().plusDays(14)); // 2 weeks
        history.setStatus(BorrowHistory.Status.BORROWED);

        book.setBorrowed(true);
        book.setBorrowedByUserId(userId);
        book.setIssuedDate(LocalDate.now());
        book.setDueDate(LocalDate.now().plusDays(14));

        bookRepository.save(book);
        borrowHistoryRepository.save(history);
    }

    // Record return action
    public void recordReturn(Long userId, Long bookId) {
        userregister user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        BorrowHistory history = borrowHistoryRepository.findByUser(user).stream()
                .filter(h -> h.getBook().getId().equals(bookId) && h.getStatus() == BorrowHistory.Status.BORROWED)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No active borrow record found"));

        history.setReturnDate(LocalDate.now());
        history.setStatus(BorrowHistory.Status.RETURNED);

        book.setBorrowed(false);
        book.setBorrowedByUserId(null);
        book.setAccessRevoked(false);

        bookRepository.save(book);
        borrowHistoryRepository.save(history);
    }

    // Get borrow history for a user (raw entities)
    public List<BorrowHistory> getUserHistory(Long userId) {
        userregister user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return borrowHistoryRepository.findByUser(user);
    }

    // ✅ Get borrow history as DTO (for API response)
    public List<BorrowHistoryDTO> getUserHistoryDTO(Long userId) {
        userregister user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return borrowHistoryRepository.findByUser(user)
                .stream()
                .map(h -> {
                    BorrowHistoryDTO dto = new BorrowHistoryDTO();
                    dto.setBookName(h.getBook().getBookName());
                    dto.setAuthorName(h.getBook().getAuthorName());
                    dto.setGenre(h.getBook().getGenre());
                    dto.setBorrowDate(h.getBorrowDate());
                    dto.setDueDate(h.getDueDate());
                    dto.setReturnDate(h.getReturnDate());
                    dto.setStatus(h.getStatus().name());
                    return dto;
                })
                .toList();
    }

    // ✅ Get all borrow history (for debug)
    public List<BorrowHistory> getAllHistory() {
        return borrowHistoryRepository.findAll();
    }
}
*/
