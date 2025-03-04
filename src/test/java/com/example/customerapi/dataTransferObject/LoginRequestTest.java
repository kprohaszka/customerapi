package com.example.customerapi.dataTransferObject;

import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class LoginRequestTest {

    @Test
    void testLoginRequest() {
        LoginRequest loginRequest = new LoginRequest();

        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");

        assertEquals("testuser", loginRequest.getUsername());
        assertEquals("password123", loginRequest.getPassword());
    }
}