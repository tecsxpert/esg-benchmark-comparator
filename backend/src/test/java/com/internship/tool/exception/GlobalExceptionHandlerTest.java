package com.internship.tool.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GlobalExceptionHandler.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void handleResourceNotFoundException_Success() throws Exception {
        // Given
        ResourceNotFoundException exception = new ResourceNotFoundException("Resource not found");

        // When & Then
        mockMvc.perform(get("/test/resource-not-found")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Resource not found"))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void handleInvalidInputException_Success() throws Exception {
        // Given
        InvalidInputException exception = new InvalidInputException("Invalid input provided");

        // When & Then
        mockMvc.perform(post("/test/invalid-input")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(exception)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid input provided"))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void handleValidationException_Success() throws Exception {
        // Given
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        List<FieldError> fieldErrors = List.of(
                new FieldError("dto", "companyName", "must not be blank"),
                new FieldError("dto", "esgScore", "must be between 0 and 100")
        );
        
        when(exception.getBindingResult()).thenReturn(mock(org.springframework.validation.BindingResult.class));
        when(exception.getBindingResult().getFieldErrors()).thenReturn(fieldErrors);

        // When & Then
        mockMvc.perform(post("/test/validation-error")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed: companyName: must not be blank, esgScore: must be between 0 and 100"))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void handleGeneralException_Success() throws Exception {
        // When & Then
        mockMvc.perform(get("/test/general-error")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("An unexpected error occurred"))
                .andExpect(jsonPath("$.status").value(500));
    }
}
