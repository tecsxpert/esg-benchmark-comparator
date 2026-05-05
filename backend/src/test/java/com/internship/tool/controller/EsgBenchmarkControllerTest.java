package com.internship.tool.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.internship.tool.dto.EsgBenchmarkDto;
import com.internship.tool.entity.EsgBenchmark;
import com.internship.tool.service.EsgBenchmarkService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EsgBenchmarkController.class)
class EsgBenchmarkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EsgBenchmarkService service;

    @Autowired
    private ObjectMapper objectMapper;

    private EsgBenchmark testRecord;
    private EsgBenchmarkDto testDto;

    @BeforeEach
    void setUp() {
        testRecord = EsgBenchmark.builder()
                .id(1L)
                .companyName("Test Company")
                .industry("Technology")
                .country("USA")
                .esgScore(85.5)
                .environmentalScore(90.0)
                .socialScore(80.0)
                .governanceScore(86.5)
                .benchmarkRank(1)
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        testDto = EsgBenchmarkDto.builder()
                .id(1L)
                .companyName("Test Company")
                .industry("Technology")
                .country("USA")
                .esgScore(85.5)
                .environmentalScore(90.0)
                .socialScore(80.0)
                .governanceScore(86.5)
                .benchmarkRank(1)
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void getAllRecords_Success() throws Exception {
        // Given
        Page<EsgBenchmark> page = new PageImpl<>(Arrays.asList(testRecord));
        when(service.getAllRecords(any())).thenReturn(page);

        // When & Then
        mockMvc.perform(get("/api/esg")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].companyName").value("Test Company"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void getRecordById_Success() throws Exception {
        // Given
        when(service.getRecordById(1L)).thenReturn(testRecord);

        // When & Then
        mockMvc.perform(get("/api/esg/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.companyName").value("Test Company"))
                .andExpect(jsonPath("$.esgScore").value(85.5));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void getRecordById_NotFound() throws Exception {
        // Given
        when(service.getRecordById(999L))
                .thenThrow(new RuntimeException("ESG record not found with id: 999"));

        // When & Then
        mockMvc.perform(get("/api/esg/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void createRecord_Success() throws Exception {
        // Given
        when(service.createRecord(any(EsgBenchmark.class))).thenReturn(testRecord);

        // When & Then
        mockMvc.perform(post("/api/esg")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.companyName").value("Test Company"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void createRecord_Forbidden() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/esg")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void createRecord_ValidationFailed() throws Exception {
        // Given
        EsgBenchmarkDto invalidDto = new EsgBenchmarkDto(); // No company name

        // When & Then
        mockMvc.perform(post("/api/esg")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void updateRecord_Success() throws Exception {
        // Given
        when(service.updateRecord(eq(1L), any(EsgBenchmark.class))).thenReturn(testRecord);

        // When & Then
        mockMvc.perform(put("/api/esg/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.companyName").value("Test Company"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void updateRecord_Forbidden() throws Exception {
        // When & Then
        mockMvc.perform(put("/api/esg/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void deleteRecord_Success() throws Exception {
        // Given
        doNothing().when(service).deleteRecord(1L);

        // When & Then
        mockMvc.perform(delete("/api/esg/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void deleteRecord_Forbidden() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/esg/1")
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllRecords_Unauthorized() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/esg"))
                .andExpect(status().isUnauthorized());
    }
}
