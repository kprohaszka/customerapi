package com.example.customerapi.config;

import com.example.customerapi.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigIntegrationTest extends BaseTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @Test
    public void testPublicEndpointsAccessible() throws Exception {

        String credentialsJson = "{"
                + "\"username\": \"testUser\","
                + "\"password\": \"\\\"4]C54j,ni$839or\""
                + "}";

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(credentialsJson))
                .andExpect(status().isOk());
    }

    @Test
    public void testProtectedEndpointsRequireAuthentication() throws Exception {
        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    public void testAuthenticatedUserCanAccessProtectedEndpoints() throws Exception {
        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk());
    }

    @Test
    public void testCsrfIsDisabled() throws Exception {
        mockMvc.perform(post("/api/customers"))
                .andExpect(status().isForbidden());
    }
}