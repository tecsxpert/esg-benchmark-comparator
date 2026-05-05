package com.internship.tool.service;

import com.internship.tool.entity.EsgBenchmark;
import com.internship.tool.exception.InvalidInputException;
import com.internship.tool.exception.ResourceNotFoundException;
import com.internship.tool.repository.EsgBenchmarkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EsgBenchmarkServiceTest {

    @Mock
    private EsgBenchmarkRepository repository;

    @InjectMocks
    private EsgBenchmarkService service;

    private EsgBenchmark testRecord;
    private EsgBenchmark testRecord2;

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

        testRecord2 = EsgBenchmark.builder()
                .id(2L)
                .companyName("Test Company 2")
                .industry("Finance")
                .country("UK")
                .esgScore(75.0)
                .environmentalScore(70.0)
                .socialScore(80.0)
                .governanceScore(75.0)
                .benchmarkRank(2)
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void createRecord_Success() {
        // Given
        when(repository.save(any(EsgBenchmark.class))).thenReturn(testRecord);

        // When
        EsgBenchmark result = service.createRecord(testRecord);

        // Then
        assertNotNull(result);
        assertEquals("Test Company", result.getCompanyName());
        assertEquals("ACTIVE", result.getStatus());
        verify(repository, times(1)).save(testRecord);
    }

    @Test
    void createRecord_WithNullCompanyName_ThrowsInvalidInputException() {
        // Given
        EsgBenchmark invalidRecord = EsgBenchmark.builder()
                .companyName(null)
                .build();

        // When & Then
        assertThrows(InvalidInputException.class, () -> service.createRecord(invalidRecord));
        verify(repository, never()).save(any());
    }

    @Test
    void createRecord_WithEmptyCompanyName_ThrowsInvalidInputException() {
        // Given
        EsgBenchmark invalidRecord = EsgBenchmark.builder()
                .companyName("")
                .build();

        // When & Then
        assertThrows(InvalidInputException.class, () -> service.createRecord(invalidRecord));
        verify(repository, never()).save(any());
    }

    @Test
    void createRecord_WithInvalidEsgScore_ThrowsInvalidInputException() {
        // Given
        EsgBenchmark invalidRecord = EsgBenchmark.builder()
                .companyName("Test Company")
                .esgScore(150.0)
                .build();

        // When & Then
        assertThrows(InvalidInputException.class, () -> service.createRecord(invalidRecord));
        verify(repository, never()).save(any());
    }

    @Test
    void getAllRecords_Success() {
        // Given
        List<EsgBenchmark> records = Arrays.asList(testRecord, testRecord2);
        when(repository.findAll()).thenReturn(records);

        // When
        List<EsgBenchmark> result = service.getAllRecords();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Test Company", result.get(0).getCompanyName());
        verify(repository, times(1)).findAll();
    }

    @Test
    void getAllRecordsPaginated_Success() {
        // Given
        Page<EsgBenchmark> page = new PageImpl<>(Arrays.asList(testRecord));
        when(repository.findAll(any(Pageable.class))).thenReturn(page);

        // When
        Page<EsgBenchmark> result = service.getAllRecords(org.springframework.data.domain.PageRequest.of(0, 10));

        // Then
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("Test Company", result.getContent().get(0).getCompanyName());
        verify(repository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void getRecordById_Success() {
        // Given
        when(repository.findById(1L)).thenReturn(Optional.of(testRecord));

        // When
        EsgBenchmark result = service.getRecordById(1L);

        // Then
        assertNotNull(result);
        assertEquals("Test Company", result.getCompanyName());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void getRecordById_NotFound_ThrowsResourceNotFoundException() {
        // Given
        when(repository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> service.getRecordById(999L));
        verify(repository, times(1)).findById(999L);
    }

    @Test
    void updateRecord_Success() {
        // Given
        EsgBenchmark updatedRecord = EsgBenchmark.builder()
                .companyName("Updated Company")
                .esgScore(90.0)
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(testRecord));
        when(repository.save(any(EsgBenchmark.class))).thenReturn(testRecord);

        // When
        EsgBenchmark result = service.updateRecord(1L, updatedRecord);

        // Then
        assertNotNull(result);
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(any(EsgBenchmark.class));
    }

    @Test
    void updateRecord_NotFound_ThrowsResourceNotFoundException() {
        // Given
        EsgBenchmark updatedRecord = EsgBenchmark.builder()
                .companyName("Updated Company")
                .build();

        when(repository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> service.updateRecord(999L, updatedRecord));
        verify(repository, times(1)).findById(999L);
        verify(repository, never()).save(any());
    }

    @Test
    void deleteRecord_Success() {
        // Given
        when(repository.existsById(1L)).thenReturn(true);

        // When
        service.deleteRecord(1L);

        // Then
        verify(repository, times(1)).existsById(1L);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void deleteRecord_NotFound_ThrowsResourceNotFoundException() {
        // Given
        when(repository.existsById(999L)).thenReturn(false);

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> service.deleteRecord(999L));
        verify(repository, times(1)).existsById(999L);
        verify(repository, never()).deleteById(any());
    }
}
