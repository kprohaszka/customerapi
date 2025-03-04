package com.example.customerapi.service;

import com.example.customerapi.BaseTest;
import com.example.customerapi.model.User;
import com.example.customerapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest
@Testcontainers
class UserServiceTest extends BaseTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void testLoadUserByUsername() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("StrongPassword123!");
        userService.saveUser(user);

        UserDetails userDetails = userService.loadUserByUsername("testuser");
        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("nonexistent"));
    }

    @Test
    void testSaveUser() {
        User user = new User();
        user.setUsername("newuser");
        user.setPassword("StrongPassword123!");

        User savedUser = userService.saveUser(user);
        assertNotNull(savedUser.getId());
        assertTrue(passwordEncoder.matches("StrongPassword123!", savedUser.getPassword()));
    }

    @Test
    void testSaveUser_InvalidPassword() {
        User user = new User();
        user.setUsername("newuser");
        user.setPassword("weak");

        assertThrows(IllegalArgumentException.class, () -> userService.saveUser(user));
    }

    @Test
    void testIsPasswordValid() {
        assertTrue(userService.isPasswordValid("StrongPassword123!"));
        assertFalse(userService.isPasswordValid("weak"));
        assertFalse(userService.isPasswordValid("NoSpecialChar123"));
        assertFalse(userService.isPasswordValid("NoNumber!"));
        assertFalse(userService.isPasswordValid("nouppercase123!"));
        assertFalse(userService.isPasswordValid("NOLOWERCASE123!"));
    }
}