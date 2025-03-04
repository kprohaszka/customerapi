package com.example.customerapi.repositories;

import com.example.customerapi.BaseTest;
import com.example.customerapi.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest extends BaseTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void testSaveUser() {
        User user = createUser("testuser");
        User savedUser = userRepository.save(user);

        assertNotNull(savedUser.getId());
        assertEquals(user.getUsername(), savedUser.getUsername());
    }

    @Test
    void testFindByUsername() {
        User user = createUser("testuser");
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByUsername("testuser");
        assertTrue(foundUser.isPresent());
        assertEquals("testuser", foundUser.get().getUsername());
    }

    @Test
    void testFindByUsername_NotFound() {
        Optional<User> foundUser = userRepository.findByUsername("nonexistent");
        assertFalse(foundUser.isPresent());
    }

    @Test
    void testUsernameUniqueness() {
        User user1 = createUser("sameusername");
        userRepository.save(user1);

        User user2 = createUser("sameusername");

        assertThrows(Exception.class, () -> userRepository.saveAndFlush(user2));
    }

    private User createUser(String username) {
        User user = new User();
        user.setUsername(username);
        user.setPassword("encodedPassword");
        user.setRole("USER");
        return user;
    }
}