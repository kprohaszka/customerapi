package com.example.customerapi.utils;

import java.security.SecureRandom;
import java.util.Base64;

public class KeyGenerator {
    public static String generateSecretKey() {
        SecureRandom random = new SecureRandom();
        byte[] keyBytes = new byte[32];
        random.nextBytes(keyBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(keyBytes);
    }
}