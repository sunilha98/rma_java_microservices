package com.resourcemgmt.resourceallocations.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String VALID_TOKEN = "valid-test-token";





    @Test
    void testProtectedEndpoints_WithoutToken() throws Exception {
        mockMvc.perform(get("/api/secure"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testProtectedEndpoints_WithInvalidToken() throws Exception {
        mockMvc.perform(get("/api/secure")
                        .header("X-Bearer-Token", "invalid-token"))
                .andExpect(status().isForbidden());
    }


}