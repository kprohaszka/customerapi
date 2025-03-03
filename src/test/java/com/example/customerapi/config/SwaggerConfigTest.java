package com.example.customerapi.config;

import com.example.customerapi.BaseTest;
import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SwaggerConfigTest extends BaseTest {

    @Autowired
    private SwaggerConfig swaggerConfig;

    @Test
    void customOpenAPI_shouldReturnCorrectConfiguration() {
        OpenAPI openAPI = swaggerConfig.customOpenAPI();

        assertNotNull(openAPI);
        assertNotNull(openAPI.getInfo());
        assertEquals("Customer API", openAPI.getInfo().getTitle());
        assertEquals("1.0", openAPI.getInfo().getVersion());
        assertEquals("API for managing customer data", openAPI.getInfo().getDescription());
    }
}