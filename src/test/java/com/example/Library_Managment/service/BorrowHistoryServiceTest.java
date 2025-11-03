package com.example.Library_Managment.service;

import com.example.Library_Managment.Services.BorrowHistoryService;
import com.example.Library_Managment.dto.BorrowHistoryDTO;
import com.example.Library_Managment.model.Book;
import com.example.Library_Managment.model.BorrowHistory;
import com.example.Library_Managment.model.userregister;
import com.example.Library_Managment.repository.BorrowHistoryRepository;
import com.example.Library_Managment.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class BorrowHistoryServiceTest {

    @Mock
    private BorrowHistoryRepository borrowHistoryRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BorrowHistoryService borrowHistoryService;

    private userregister user;
    private Book book;
    private BorrowHistory history;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new userregister();
        user.setId(1L);
        user.setFirstName("Alice");
        user.setEmail("alice@example.com");

        book = new Book();
        book.setId(10L);
        book.setBookName("Clean Code");
        book.setAuthorName("Robert C. Martin");
        book.setGenre("Programming");
        book.setImagePath("clean-code.jpg");

        history = new BorrowHistory();
        history.setBook(book);
        history.setUser(user);
        history.setBorrowDate(LocalDate.of(2024, 10, 1));
        history.setDueDate(LocalDate.of(2024, 10, 15));
        history.setReturnDate(LocalDate.of(2024, 10, 10));
        history.setStatus(BorrowHistory.Status.RETURNED);
    }

    @Test
    void getUserBorrowHistory_ShouldReturnBorrowHistoryDTOList_WhenUserExists() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(borrowHistoryRepository.findByUser(user)).thenReturn(List.of(history));

        // Act
        List<BorrowHistoryDTO> result = borrowHistoryService.getUserBorrowHistory(1L);

        // Assert
        assertThat(result).hasSize(1);
        BorrowHistoryDTO dto = result.get(0);
        assertThat(dto.getBookName()).isEqualTo("Clean Code");
        assertThat(dto.getAuthorName()).isEqualTo("Robert C. Martin");
        assertThat(dto.getGenre()).isEqualTo("Programming");
        assertThat(dto.getImagePath()).isEqualTo("clean-code.jpg");
        assertThat(dto.getStatus()).isEqualTo("RETURNED");
        verify(borrowHistoryRepository, times(1)).findByUser(user);
    }

    @Test
    void getUserBorrowHistory_ShouldThrowException_WhenUserNotFound() {
        // Arrange
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // Act + Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                borrowHistoryService.getUserBorrowHistory(99L)
        );

        assertThat(exception.getMessage()).isEqualTo("User not found");
        verify(borrowHistoryRepository, never()).findByUser(any());
    }

    @Test
    void getUserBorrowHistory_ShouldReturnEmptyList_WhenUserHasNoHistory() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(borrowHistoryRepository.findByUser(user)).thenReturn(List.of());

        // Act
        List<BorrowHistoryDTO> result = borrowHistoryService.getUserBorrowHistory(1L);

        // Assert
        assertThat(result).isEmpty();
        verify(borrowHistoryRepository, times(1)).findByUser(user);
    }
}
