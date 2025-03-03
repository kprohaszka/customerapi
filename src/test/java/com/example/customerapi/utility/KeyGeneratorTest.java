package com.example.customerapi.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KeyGeneratorTest {

    @Test
    void testGenerateSecretKey() {
        String key1 = KeyGenerator.generateSecretKey();
        String key2 = KeyGenerator.generateSecretKey();

        assertNotNull(key1);
        assertNotNull(key2);
        
        assertFalse(key1.isEmpty());
        assertFalse(key2.isEmpty());

        assertNotEquals(key1, key2);

        assertTrue(key1.length() >= 43);
    }
}