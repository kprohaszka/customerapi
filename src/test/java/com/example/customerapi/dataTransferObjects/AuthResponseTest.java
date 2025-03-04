package com.example.customerapi.dataTransferObjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthResponseTest {

    @Test
    void testAuthResponse() {
        String token = "test.jwt.token";

        AuthResponse authResponse = new AuthResponse(token);

        assertEquals(token, authResponse.getToken());
    }
}