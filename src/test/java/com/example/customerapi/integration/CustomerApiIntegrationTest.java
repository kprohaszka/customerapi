package com.example.customerapi.integration;

import com.example.customerapi.BaseTest;
import com.example.customerapi.dataTransferObject.AuthResponse;
import com.example.customerapi.dataTransferObject.LoginRequest;
import com.example.customerapi.model.Customer;
import com.example.customerapi.model.User;
import com.example.customerapi.repository.CustomerRepository;
import com.example.customerapi.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
class CustomerApiIntegrationTest extends BaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String authToken;

    @BeforeEach
    void setUp() throws Exception {
        userRepository.deleteAll();
        customerRepository.deleteAll();

        User user = new User();
        user.setUsername("integrationtest");
        user.setPassword(passwordEncoder.encode("StrongPassword123!"));
        user.setRole("USER");
        userRepository.save(user);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("integrationtest");
        loginRequest.setPassword("StrongPassword123!");

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        AuthResponse authResponse = objectMapper.readValue(
                result.getResponse().getContentAsString(), AuthResponse.class);
        authToken = authResponse.getToken();

        assertNotNull(authToken);
    }

    @Test
    void testCreateAndRetrieveCustomer() throws Exception {
        Customer customer = new Customer();
        customer.setFirstName("Integration");
        customer.setLastName("Test");
        customer.setEmail("integration.test@example.com");
        customer.setDateOfBirth(LocalDate.of(1990, 1, 1));
        customer.setPhoneNumber("+1234567890");

        MvcResult createResult = mockMvc.perform(post("/api/customers")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated())
                .andReturn();

        Customer createdCustomer = objectMapper.readValue(
                createResult.getResponse().getContentAsString(), Customer.class);

        assertNotNull(createdCustomer.getId());
        assertEquals("Integration", createdCustomer.getFirstName());
        assertEquals("Test", createdCustomer.getLastName());

        assertTrue(customerRepository.findById(createdCustomer.getId()).isPresent());
    }
}