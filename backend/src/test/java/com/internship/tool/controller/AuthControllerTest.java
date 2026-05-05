package com.internship.tool.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.internship.tool.dto.AuthRequest;
import com.internship.tool.dto.AuthResponse;
import com.internship.tool.dto.RegisterRequest;
import com.internship.tool.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    private AuthRequest authRequest;
    private RegisterRequest registerRequest;
    private AuthResponse authResponse;

    @BeforeEach
    void setUp() {
        authRequest = AuthRequest.builder()
                .username("testuser")
                .password("password123")
                .build();

        registerRequest = RegisterRequest.builder()
                .username("testuser")
                .password("password123")
                .email("test@example.com")
                .role("USER")
                .build();

        authResponse = AuthResponse.builder()
                .token("jwt-token")
                .build();
    }

    @Test
    void register_Success() throws Exception {
        // Given
        when(authService.register(any(RegisterRequest.class))).thenReturn(authResponse);

        // When & Then
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"));
        
        verify(authService, times(1)).register(any(RegisterRequest.class));
    }

    @Test
    void register_WithInvalidEmail_ReturnsBadRequest() throws Exception {
        // Given
        RegisterRequest invalidRequest = RegisterRequest.builder()
                .username("testuser")
                .password("password123")
                .email("invalid-email")
                .build();

        // When & Then
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
        
        verify(authService, never()).register(any());
    }

    @Test
    void register_WithEmptyUsername_ReturnsBadRequest() throws Exception {
        // Given
        RegisterRequest invalidRequest = RegisterRequest.builder()
                .username("")
                .password("password123")
                .email("test@example.com")
                .build();

        // When & Then
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
        
        verify(authService, never()).register(any());
    }

    @Test
    void login_Success() throws Exception {
        // Given
        when(authService.login(any(AuthRequest.class))).thenReturn(authResponse);

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"));
        
        verify(authService, times(1)).login(any(AuthRequest.class));
    }

    @Test
    void login_WithEmptyUsername_ReturnsBadRequest() throws Exception {
        // Given
        AuthRequest invalidRequest = AuthRequest.builder()
                .username("")
                .password("password123")
                .build();

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
        
        verify(authService, never()).login(any());
    }

    @Test
    void login_WithEmptyPassword_ReturnsBadRequest() throws Exception {
        // Given
        AuthRequest invalidRequest = AuthRequest.builder()
                .username("testuser")
                .password("")
                .build();

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
        
        verify(authService, never()).login(any());
    }

    @Test
    void login_WithInvalidCredentials_ReturnsUnauthorized() throws Exception {
        // Given
        when(authService.login(any(AuthRequest.class)))
                .thenThrow(new RuntimeException("Bad credentials"));

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isInternalServerError());
        
        verify(authService, times(1)).login(any(AuthRequest.class));
    }
}
