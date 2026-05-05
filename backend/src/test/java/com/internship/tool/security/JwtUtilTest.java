package com.internship.tool.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", "testSecretKeyThatIsAtLeast32CharactersLongForTesting");
        ReflectionTestUtils.setField(jwtUtil, "expiration", 86400000L); // 24 hours

        userDetails = org.springframework.security.core.userdetails.User.builder()
                .username("testuser")
                .password("password")
                .authorities("ROLE_USER")
                .build();
    }

    @Test
    void generateToken_Success() {
        // When
        String token = jwtUtil.generateToken(userDetails);

        // Then
        assertNotNull(token);
        assertTrue(token.startsWith("eyJ"));
    }

    @Test
    void extractUsername_Success() {
        // Given
        String token = jwtUtil.generateToken(userDetails);

        // When
        String username = jwtUtil.extractUsername(token);

        // Then
        assertEquals("testuser", username);
    }

    @Test
    void extractExpiration_Success() {
        // Given
        String token = jwtUtil.generateToken(userDetails);

        // When
        Date expiration = jwtUtil.extractExpiration(token);

        // Then
        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    void validateToken_ValidToken_ReturnsTrue() {
        // Given
        String token = jwtUtil.generateToken(userDetails);

        // When
        boolean isValid = jwtUtil.validateToken(token, userDetails);

        // Then
        assertTrue(isValid);
    }

    @Test
    void validateToken_InvalidUsername_ReturnsFalse() {
        // Given
        String token = jwtUtil.generateToken(userDetails);
        UserDetails differentUser = org.springframework.security.core.userdetails.User.builder()
                .username("differentuser")
                .password("password")
                .authorities("ROLE_USER")
                .build();

        // When
        boolean isValid = jwtUtil.validateToken(token, differentUser);

        // Then
        assertFalse(isValid);
    }

    @Test
    void extractClaim_Success() {
        // Given
        String token = jwtUtil.generateToken(userDetails);

        // When
        String subject = jwtUtil.extractClaim(token, claims -> claims.getSubject());

        // Then
        assertEquals("testuser", subject);
    }
}
