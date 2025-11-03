package com.example.Library_Managment.service;


import com.example.Library_Managment.Services.UserService;
import com.example.Library_Managment.dto.UserRegisterRequest;
import com.example.Library_Managment.dto.UserRequest;
import com.example.Library_Managment.exception.InvalidPasswordException;
import com.example.Library_Managment.exception.UserNotFoundException;
import com.example.Library_Managment.model.userregister;
import com.example.Library_Managment.repository.UserRepository;
import com.example.Library_Managment.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test case for registering a user
    @Test
    void testRegisterSuccess() {
        // Given
        UserRegisterRequest request = new UserRegisterRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("john@example.com");
        request.setPassword("password123");

        userregister user = new userregister();
        when(userRepository.save(any(userregister.class))).thenReturn(user);

        // When
        userregister savedUser = userService.register(request);

        // Then
        assertNotNull(savedUser);
        verify(userRepository, times(1)).save(any(userregister.class));
    }

    // Test case for successful login
    @Test
    void testLogin_Successful() {
        // Arrange
        UserRequest loginRequest = new UserRequest();
        loginRequest.setEmail("john.doe@example.com");
        loginRequest.setPassword("password@123");

        userregister mockUser = new userregister();
        mockUser.setEmail("john.doe@example.com");
        mockUser.setPassword("password@123");
        mockUser.setId(1L); // Set a sample user ID

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(mockUser));
        when(jwtUtil.generateToken(mockUser.getEmail(), mockUser.getId())).thenReturn("some.jwt.token");

        // Act
        String token = userService.login(loginRequest);

        // Assert
        assertEquals("some.jwt.token", token); // Verify the token returned
        verify(userRepository, times(1)).findByEmail(any(String.class));
        verify(jwtUtil, times(1)).generateToken(mockUser.getEmail(), mockUser.getId());
    }

    @Test
    void testLogin_UserNotFound() {
        // Arrange
        UserRequest loginRequest = new UserRequest();
        loginRequest.setEmail("nonexistent@example.com");
        loginRequest.setPassword("password@123");

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(UserNotFoundException.class, () -> {
            userService.login(loginRequest);
        });

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findByEmail(any(String.class));
        verify(jwtUtil, never()).generateToken(anyString(), anyLong()); // Ensure token generation is not called
    }

    @Test
    void testLogin_InvalidPassword() {
        // Arrange
        UserRequest loginRequest = new UserRequest();
        loginRequest.setEmail("john.doe@example.com");
        loginRequest.setPassword("wrongpassword");

        userregister mockUser = new userregister();
        mockUser.setEmail("john.doe@example.com");
        mockUser.setPassword("password@123"); // Correct password
        mockUser.setId(1L); // Set a sample user ID

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(mockUser));

        // Act & Assert
        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(InvalidPasswordException.class, () -> {
            userService.login(loginRequest);
        });

        assertEquals("Incorrect password", exception.getMessage());
        verify(userRepository, times(1)).findByEmail(any(String.class));
        verify(jwtUtil, never()).generateToken(anyString(), anyLong()); // Ensure token generation is not called
    }

    // Test case for changing password successfully





}
