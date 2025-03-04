package com.example.customerapi.dataTransferObjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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