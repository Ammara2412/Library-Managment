package com.example.Library_Managment.repository;

import com.example.Library_Managment.model.BorrowHistory;
import com.example.Library_Managment.model.userregister;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface BorrowHistoryRepository extends JpaRepository<BorrowHistory, Long> {
    List<BorrowHistory> findByUser(userregister user);
}

