package com.example.customerapi.exceptions;

import com.example.customerapi.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GlobalExceptionHandlerTest extends BaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testHandleMissingParams() throws Exception {
        mockMvc.perform(get("/api/customers/age-range")
                        .param("minAge", "20")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title", is("Missing Parameter")))
                .andExpect(jsonPath("$.detail", containsString("Required parameter 'maxAge' is missing")));
    }

    @Test
    void testHandleTypeMismatch() throws Exception {
        mockMvc.perform(get("/api/customers/age-range")
                        .param("minAge", "twenty")
                        .param("maxAge", "40")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title", is("Type Mismatch")))
                .andExpect(jsonPath("$.detail", containsString("should be of type")));
    }

    @Test
    void testHandleCustomerNotFound() throws Exception {
        mockMvc.perform(get("/api/customers/" + java.util.UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title", is("Customer Not Found")));
    }
}