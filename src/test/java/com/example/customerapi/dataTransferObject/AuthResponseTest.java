package com.example.customerapi.dataTransferObject;

import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class AuthResponseTest {

    @Test
    void testAuthResponse() {
        String token = "test.jwt.token";

        AuthResponse authResponse = new AuthResponse(token);

        assertEquals(token, authResponse.getToken());
    }
}