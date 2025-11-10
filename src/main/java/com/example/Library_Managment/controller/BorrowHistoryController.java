package com.example.Library_Managment.controller;

import com.example.Library_Managment.dto.BorrowHistoryDTO;
import com.example.Library_Managment.Services.BorrowHistoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/history")
@CrossOrigin
public class BorrowHistoryController {

    private final BorrowHistoryService borrowHistoryService;
    public BorrowHistoryController(BorrowHistoryService borrowHistoryService) {
        this.borrowHistoryService = borrowHistoryService;
    }
    @GetMapping("/{userId}")
    public List<BorrowHistoryDTO> getUserHistory(@PathVariable Long userId) {
        return borrowHistoryService.getUserBorrowHistory(userId);
    }
}
