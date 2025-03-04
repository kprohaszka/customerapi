package com.example.customerapi.controller;

import com.example.customerapi.BaseTest;
import com.example.customerapi.exception.CustomerNotFoundException;
import com.example.customerapi.exception.InvalidCustomerDataException;
import com.example.customerapi.model.Customer;
import com.example.customerapi.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerTest extends BaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CustomerService customerService;

    private Customer testCustomer;
    private UUID testId;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        testCustomer = new Customer();
        testCustomer.setId(testId);
        testCustomer.setFirstName("John");
        testCustomer.setLastName("Doe");
        testCustomer.setEmail("john.doe@example.com");
        testCustomer.setDateOfBirth(LocalDate.of(1990, 1, 1));
        testCustomer.setPhoneNumber("+1234567890");
    }

    @Test
    @WithMockUser
    void testCreateCustomer() throws Exception {
        when(customerService.createCustomer(any(Customer.class))).thenReturn(testCustomer);

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCustomer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(testId.toString())))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")));

        verify(customerService).createCustomer(any(Customer.class));
    }

    @Test
    @WithMockUser
    void testCreateCustomer_InvalidData() throws Exception {
        when(customerService.createCustomer(any(Customer.class)))
                .thenThrow(new InvalidCustomerDataException("Invalid customer data"));

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCustomer)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Invalid customer data")));

        verify(customerService).createCustomer(any(Customer.class));
    }

    @Test
    @WithMockUser
    void testGetCustomerById() throws Exception {
        when(customerService.getCustomerById(testId)).thenReturn(testCustomer);

        mockMvc.perform(get("/api/customers/" + testId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testId.toString())))
                .andExpect(jsonPath("$.firstName", is("John")));

        verify(customerService).getCustomerById(testId);
    }

    @Test
    @WithMockUser
    void testGetCustomerById_NotFound() throws Exception {
        when(customerService.getCustomerById(testId))
                .thenThrow(new CustomerNotFoundException("Customer not found"));

        mockMvc.perform(get("/api/customers/" + testId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("Customer not found")));

        verify(customerService).getCustomerById(testId);
    }

    @Test
    @WithMockUser
    void testGetAllCustomers() throws Exception {
        List<Customer> customers = Collections.singletonList(testCustomer);
        when(customerService.getAllCustomers()).thenReturn(customers);

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(testId.toString())));

        verify(customerService).getAllCustomers();
    }

    @Test
    @WithMockUser
    void testUpdateCustomer() throws Exception {
        when(customerService.updateCustomer(eq(testId), any(Customer.class))).thenReturn(testCustomer);

        mockMvc.perform(put("/api/customers/" + testId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCustomer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testId.toString())));

        verify(customerService).updateCustomer(eq(testId), any(Customer.class));
    }

    @Test
    @WithMockUser
    void testUpdateCustomer_NotFound() throws Exception {
        when(customerService.updateCustomer(eq(testId), any(Customer.class)))
                .thenThrow(new CustomerNotFoundException("Customer not found"));

        mockMvc.perform(put("/api/customers/" + testId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCustomer)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("Customer not found")));

        verify(customerService).updateCustomer(eq(testId), any(Customer.class));
    }

    @Test
    @WithMockUser
    void testDeleteCustomer() throws Exception {
        doNothing().when(customerService).deleteCustomer(testId);

        mockMvc.perform(delete("/api/customers/" + testId))
                .andExpect(status().isNoContent());

        verify(customerService).deleteCustomer(testId);
    }

    @Test
    @WithMockUser
    void testDeleteCustomer_NotFound() throws Exception {
        doThrow(new CustomerNotFoundException("Customer not found"))
                .when(customerService).deleteCustomer(testId);

        mockMvc.perform(delete("/api/customers/" + testId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("Customer not found")));

        verify(customerService).deleteCustomer(testId);
    }

    @Test
    @WithMockUser
    void testGetAverageAge() throws Exception {
        when(customerService.getAverageAge()).thenReturn(35.5);

        mockMvc.perform(get("/api/customers/average-age"))
                .andExpect(status().isOk())
                .andExpect(content().string("35.5"));

        verify(customerService).getAverageAge();
    }

    @Test
    @WithMockUser
    void testGetCustomersBetweenAges() throws Exception {
        List<Customer> customers = Arrays.asList(testCustomer);
        when(customerService.getCustomersBetweenAges(20, 40)).thenReturn(customers);

        mockMvc.perform(get("/api/customers/age-range")
                        .param("minAge", "20")
                        .param("maxAge", "40"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(testId.toString())));

        verify(customerService).getCustomersBetweenAges(20, 40);
    }

    @Test
    @WithMockUser
    void testGetCustomersBetweenAges_MissingParams() throws Exception {
        mockMvc.perform(get("/api/customers/age-range")
                        .param("minAge", "20"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(customerService);
    }

    @Test
    @WithMockUser
    void testGetCustomersBetweenAges_InvalidRange() throws Exception {
        mockMvc.perform(get("/api/customers/age-range")
                        .param("minAge", "40")
                        .param("maxAge", "20"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Invalid age range")));

        verifyNoInteractions(customerService);
    }
}