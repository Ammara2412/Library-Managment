package com.example.Library_Managment.repository;

import com.example.Library_Managment.model.userregister;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<userregister, Long> {
     Optional<userregister> findByEmail(String email);
}