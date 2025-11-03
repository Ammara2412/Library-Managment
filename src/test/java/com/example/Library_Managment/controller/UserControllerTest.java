package com.example.Library_Managment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.Library_Managment.dto.*;
import com.example.Library_Managment.model.userregister;
import com.example.Library_Managment.Services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class UserControllerTest {
    private MockMvc mockMvc;

    @InjectMocks
    private UserController userController;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

    }

    @Test
    public void testRegister() {
        // Arrange
        UserRegisterRequest registerRequest = new UserRegisterRequest();
        registerRequest.setFirstName("John");
        registerRequest.setLastName("Doe");
        registerRequest.setEmail("john.doe@example.com");
        registerRequest.setPassword("password@123");

        userregister user = new userregister();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password@123");

        when(userService.register(any(UserRegisterRequest.class))).thenReturn(user);

        // Act
        ResponseEntity<ApiResponse> responseEntity = userController.register(registerRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("User Registered Successfully!", responseEntity.getBody().getMessage());
        verify(userService, times(1)).register(any(UserRegisterRequest.class));
    }
    // Test for User Login
    @Test
    void testLogin() {
        // Arrange
        UserRequest loginRequest = new UserRequest();
        loginRequest.setEmail("john.doe@example.com");
        loginRequest.setPassword("password@123");

        String token = "some.jwt.token";
        userregister mockUser = new userregister();
        mockUser.setId(1L); // example user ID

        when(userService.login(any(UserRequest.class))).thenReturn(token);
        when(userService.getUserByEmail(loginRequest.getEmail())).thenReturn(mockUser);

        // Act
        ResponseEntity<?> response = userController.login(loginRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());

        ApiResponse apiResponse = (ApiResponse) response.getBody();
        assertNotNull(apiResponse);
        assertEquals("User Logged in Successfully!", apiResponse.getMessage());
        assertEquals(token, apiResponse.getToken());
        assertEquals(mockUser.getId(), apiResponse.getId());

        // Verify service calls
        verify(userService, times(1)).login(any(UserRequest.class));
        verify(userService, times(1)).getUserByEmail(loginRequest.getEmail());
    }




}
