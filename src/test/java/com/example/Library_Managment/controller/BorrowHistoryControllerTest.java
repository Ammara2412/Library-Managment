package com.example.Library_Managment.controller;

import com.example.Library_Managment.Services.BorrowHistoryService;
import com.example.Library_Managment.dto.BorrowHistoryDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BorrowHistoryControllerTest {

    @Mock
    private BorrowHistoryService borrowHistoryService;

    @InjectMocks
    private BorrowHistoryController borrowHistoryController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // -------------------- TEST 1: getUserHistory - returns list --------------------
    @Test
    void testGetUserHistory_ReturnsBorrowHistory() {
        // Arrange
        Long userId = 1L;

        BorrowHistoryDTO history1 = new BorrowHistoryDTO();
        history1.setBookName("Diary By Young Girl Anne Frank");
        history1.setBorrowDate(LocalDate.of(2025, 1, 10));
        history1.setReturnDate(LocalDate.of(2025, 1, 20));

        BorrowHistoryDTO history2 = new BorrowHistoryDTO();
        history2.setBookName("Effective Java");
        history2.setBorrowDate(LocalDate.of(2025, 2, 1));
        history2.setReturnDate(LocalDate.of(2025, 2, 15));

        when(borrowHistoryService.getUserBorrowHistory(userId))
                .thenReturn(List.of(history1, history2));

        // Act
        List<BorrowHistoryDTO> result = borrowHistoryController.getUserHistory(userId);

        // Assert
        assertEquals(2, result.size());
        assertEquals("Diary By Young Girl Anne Frank", result.get(0).getBookName());
        assertEquals(LocalDate.of(2025, 1, 10), result.get(0).getBorrowDate());
        verify(borrowHistoryService, times(1)).getUserBorrowHistory(userId);
    }

    // -------------------- TEST 2: getUserHistory - empty list --------------------
    @Test
    void testGetUserHistory_EmptyList() {
        // Arrange
        Long userId = 2L;
        when(borrowHistoryService.getUserBorrowHistory(userId)).thenReturn(List.of());

        // Act
        List<BorrowHistoryDTO> result = borrowHistoryController.getUserHistory(userId);

        // Assert
        assertTrue(result.isEmpty());
        verify(borrowHistoryService, times(1)).getUserBorrowHistory(userId);
    }
}
