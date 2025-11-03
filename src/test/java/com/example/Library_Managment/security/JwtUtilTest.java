package com.example.Library_Managment.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JwtUtilTest {

    private JwtUtil jwtUtil;
    private String secretKey = "your_secret_key"; // Use your actual secret key

    private String username = "testUser";
    private Long userId = 1L;
    private String token;

    @BeforeEach
    public void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "SECRET_KEY", "your-secret-key");

        // Mock the secret key for testing
        // Set the secret key directly if you have a setter or constructor
    }

    @Test
    public void testGenerateToken() {
        token = jwtUtil.generateToken(username, userId);
        assertNotNull(token);
    }

    @Test
    public void testExtractUsername() {
        token = jwtUtil.generateToken(username, userId);
        String extractedUsername = jwtUtil.extractUsername(token);
        assertEquals(username, extractedUsername);
    }

    @Test
    public void testExtractUserId() {
        token = jwtUtil.generateToken(username, userId);
        Long extractedUserId = jwtUtil.extractUserId(token);
        assertEquals(userId, extractedUserId);
    }


    @Test
    public void testValidateToken_Valid() {
        token = jwtUtil.generateToken(username, userId);
        assertTrue(jwtUtil.validateToken(token, username));
    }

    @Test
    public void testValidateToken_InvalidUsername() {
        token = jwtUtil.generateToken(username, userId);
        assertFalse(jwtUtil.validateToken(token, "wrongUser"));
    }

    @Test
    public void testIsTokenExpired() {
        // Mock the claims to return an expired token without actually parsing it
        Claims claims = Mockito.mock(Claims.class);
        when(claims.getExpiration()).thenReturn(new Date(System.currentTimeMillis() - 1000)); // Set to 1 second in the past

        // Mock the extractAllClaims method
        JwtUtil jwtUtilSpy = Mockito.spy(jwtUtil);
        doReturn(claims).when(jwtUtilSpy).extractAllClaims(anyString());

        // Call the isTokenExpired method
        assertTrue(jwtUtilSpy.isTokenExpired("dummy-token")); // Any string here, as it's mocked
    }

    @Test
    public void testIsTokenExpired_NotExpired() {
        // Mock the claims to return a valid token
        Claims claims = Mockito.mock(Claims.class);
        when(claims.getExpiration()).thenReturn(new Date(System.currentTimeMillis() + 1000 * 60)); // 1 minute in the future

        // Mock the extractAllClaims method
        JwtUtil jwtUtilSpy = Mockito.spy(jwtUtil);
        doReturn(claims).when(jwtUtilSpy).extractAllClaims(anyString());

        // Call the isTokenExpired method
        assertFalse(jwtUtilSpy.isTokenExpired("dummy-token")); // Any string here, as it's mocked
    }

    @Test
    public void testIsTokenNotExpired() {
        token = jwtUtil.generateToken(username, userId);
        assertFalse(jwtUtil.isTokenExpired(token));
    }
    
}
