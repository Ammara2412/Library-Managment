package com.example.Library_Managment.controller;


import com.example.Library_Managment.Services.UserService;
import com.example.Library_Managment.dto.ApiResponse;
import com.example.Library_Managment.dto.*;
import com.example.Library_Managment.dto.UserRegisterRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        ApiResponse response = new ApiResponse("success", "User Logged in Successfully!");
        response.setToken(token); // Set token in response
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
