package com.internship.tool.service;

import com.internship.tool.dto.AuthRequest;
import com.internship.tool.dto.AuthResponse;
import com.internship.tool.dto.RegisterRequest;
import com.internship.tool.entity.User;
import com.internship.tool.repository.UserRepository;
import com.internship.tool.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private AuthService authService;

    private User testUser;
    private RegisterRequest registerRequest;
    private AuthRequest authRequest;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .password("encodedPassword")
                .email("test@example.com")
                .role("USER")
                .build();

        registerRequest = RegisterRequest.builder()
                .username("testuser")
                .password("password123")
                .email("test@example.com")
                .role("USER")
                .build();

        authRequest = AuthRequest.builder()
                .username("testuser")
                .password("password123")
                .build();
    }

    @Test
    void register_Success() {
        // Given
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(userDetailsService.loadUserByUsername("testuser")).thenReturn(
                org.springframework.security.core.userdetails.User.builder()
                        .username("testuser")
                        .password("encodedPassword")
                        .authorities("ROLE_USER")
                        .build()
        );
        when(jwtUtil.generateToken(any(UserDetails.class))).thenReturn("jwt-token");

        // When
        AuthResponse result = authService.register(registerRequest);

        // Then
        assertNotNull(result);
        assertEquals("jwt-token", result.getToken());
        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode("password123");
    }

    @Test
    void register_WithDefaultRole_Success() {
        // Given
        RegisterRequest requestWithoutRole = RegisterRequest.builder()
                .username("testuser")
                .password("password123")
                .email("test@example.com")
                .role(null)
                .build();

        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(userDetailsService.loadUserByUsername("testuser")).thenReturn(
                org.springframework.security.core.userdetails.User.builder()
                        .username("testuser")
                        .password("encodedPassword")
                        .authorities("ROLE_USER")
                        .build()
        );
        when(jwtUtil.generateToken(any(UserDetails.class))).thenReturn("jwt-token");

        // When
        AuthResponse result = authService.register(requestWithoutRole);

        // Then
        assertNotNull(result);
        assertEquals("jwt-token", result.getToken());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void login_Success() {
        // Given
        when(userDetailsService.loadUserByUsername("testuser")).thenReturn(
                org.springframework.security.core.userdetails.User.builder()
                        .username("testuser")
                        .password("encodedPassword")
                        .authorities("ROLE_USER")
                        .build()
        );
        when(jwtUtil.generateToken(any(UserDetails.class))).thenReturn("jwt-token");

        // When
        AuthResponse result = authService.login(authRequest);

        // Then
        assertNotNull(result);
        assertEquals("jwt-token", result.getToken());
        verify(authenticationManager, times(1)).authenticate(any());
        verify(jwtUtil, times(1)).generateToken(any(UserDetails.class));
    }

    @Test
    void login_WithInvalidCredentials_ThrowsException() {
        // Given
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        // When & Then
        assertThrows(BadCredentialsException.class, () -> authService.login(authRequest));
        verify(authenticationManager, times(1)).authenticate(any());
        verify(jwtUtil, never()).generateToken(any());
    }
}
