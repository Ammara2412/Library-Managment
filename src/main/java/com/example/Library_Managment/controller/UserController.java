package com.example.Library_Managment.controller;


import com.example.Library_Managment.Services.UserService;
import com.example.Library_Managment.dto.ApiResponse;
import com.example.Library_Managment.dto.*;
import com.example.Library_Managment.dto.UserRegisterRequest;
import com.example.Library_Managment.model.userregister;
import com.example.Library_Managment.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin

public class UserController {
    @Autowired
   private UserService userService;

    // ✅ Registration Endpoint
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody UserRegisterRequest RegisterRequest) {
        userService.register(RegisterRequest);
        ApiResponse response = new ApiResponse("success", "User Registered Successfully!");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ✅ Login Endpoint
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserRequest request) {
        String token = userService.login(request); // Get token
        userregister user = userService.getUserByEmail(request.getEmail()); // create this method in UserService

        ApiResponse response = new ApiResponse("success", "User Logged in Successfully!");
        response.setToken(token); // Set token in response
        response.setId(user.getId()); // set the user id

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(request);
        ApiResponse response = new ApiResponse("success", "Password changed successfully");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //Reset Password Endpoint
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @Validated @RequestBody ResetPasswordRequwst request) {
        userregister nu = userService.resetPassword(request);
        ApiResponse response = new ApiResponse("success","Password reset successfully. New Password is: " + nu.getPassword());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }



}
