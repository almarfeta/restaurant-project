package com.example.project_server.controllers;

import com.example.project_server.dtos.AuthenticationRequest;
import com.example.project_server.dtos.RegisterRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "test")
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should register a user successfully")
    void register() throws Exception {
        RegisterRequest request = new RegisterRequest(
                "John",
                "Doe",
                "test@email.com",
                "Password1234"
        );
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(201))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Registered successfully"));
    }

    @Test
    @DisplayName("Should login a user successfully")
    void authenticate() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest(
                "test@email.com",
                "Password1234"
        );
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Login successfully"));
    }

    @Test
    @DisplayName("Should logout a user successfully")
    void logout() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest(
                "test@email.com",
                "Password1234"
        );
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}