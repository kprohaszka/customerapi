package com.example.customerapi.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.TestPropertySource;
import com.example.customerapi.BaseTest;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
        "jwt.secret=testSecretKeyWithAtLeast32CharactersForHmacSha256",
        "jwt.expiration=3600"
})
class JwtUtilTest extends BaseTest{

    @Autowired
    private JwtUtil jwtUtil;

    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        userDetails = new User("testuser", "password", new ArrayList<>());
    }

    @Test
    void testGenerateToken() {
        String token = jwtUtil.generateToken(userDetails);
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testExtractUsername() {
        String token = jwtUtil.generateToken(userDetails);
        String username = jwtUtil.extractUsername(token);
        assertEquals("testuser", username);
    }

    @Test
    void testExtractExpiration() {
        String token = jwtUtil.generateToken(userDetails);
        Date expiration = jwtUtil.extractExpiration(token);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    void testValidateToken_ValidToken() {
        String token = jwtUtil.generateToken(userDetails);
        boolean isValid = jwtUtil.validateToken(token, userDetails);
        assertTrue(isValid);
    }

    @Test
    void testValidateToken_InvalidUsername() {
        String token = jwtUtil.generateToken(userDetails);
        UserDetails differentUser = new User("different", "password", new ArrayList<>());
        boolean isValid = jwtUtil.validateToken(token, differentUser);
        assertFalse(isValid);
    }
}