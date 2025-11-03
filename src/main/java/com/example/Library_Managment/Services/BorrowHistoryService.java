

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
                    dto.setImagePath(h.getBook().getImagePath());
                    dto.setBorrowDate(h.getBorrowDate());
                    dto.setDueDate(h.getDueDate());
                    dto.setReturnDate(h.getReturnDate());
                    dto.setStatus(h.getStatus().name());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}




