package com.internship.tool.repository;

import com.internship.tool.entity.EsgBenchmark;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class EsgBenchmarkRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EsgBenchmarkRepository repository;

    private EsgBenchmark testRecord1;
    private EsgBenchmark testRecord2;
    private EsgBenchmark testRecord3;

    @BeforeEach
    void setUp() {
        testRecord1 = EsgBenchmark.builder()
                .companyName("Apple Inc")
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
                .companyName("Microsoft Corp")
                .industry("Technology")
                .country("USA")
                .esgScore(82.0)
                .environmentalScore(85.0)
                .socialScore(78.0)
                .governanceScore(83.0)
                .benchmarkRank(2)
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        testRecord3 = EsgBenchmark.builder()
                .companyName("Google LLC")
                .industry("Technology")
                .country("USA")
                .esgScore(88.0)
                .environmentalScore(92.0)
                .socialScore(85.0)
                .governanceScore(87.0)
                .benchmarkRank(3)
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void findAll_Success() {
        // Given
        entityManager.persistAndFlush(testRecord1);
        entityManager.persistAndFlush(testRecord2);

        // When
        List<EsgBenchmark> result = repository.findAll();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void findAllPaginated_Success() {
        // Given
        entityManager.persistAndFlush(testRecord1);
        entityManager.persistAndFlush(testRecord2);
        entityManager.persistAndFlush(testRecord3);

        Pageable pageable = PageRequest.of(0, 2);

        // When
        Page<EsgBenchmark> result = repository.findAll(pageable);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(3, result.getTotalElements());
    }

    @Test
    void findById_Success() {
        // Given
        EsgBenchmark persisted = entityManager.persistAndFlush(testRecord1);

        // When
        Optional<EsgBenchmark> result = repository.findById(persisted.getId());

        // Then
        assertTrue(result.isPresent());
        assertEquals("Apple Inc", result.get().getCompanyName());
    }

    @Test
    void findById_NotFound() {
        // When
        Optional<EsgBenchmark> result = repository.findById(999L);

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    void findByCompanyNameContaining_ExactMatch() {
        // Given
        entityManager.persistAndFlush(testRecord1);
        entityManager.persistAndFlush(testRecord2);

        // When
        List<EsgBenchmark> result = repository.findByCompanyNameContaining("Apple");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Apple Inc", result.get(0).getCompanyName());
    }

    @Test
    void findByCompanyNameContaining_PartialMatch() {
        // Given
        entityManager.persistAndFlush(testRecord1);
        entityManager.persistAndFlush(testRecord2);

        // When
        List<EsgBenchmark> result = repository.findByCompanyNameContaining("Corp");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Microsoft Corp", result.get(0).getCompanyName());
    }

    @Test
    void findByCompanyNameContaining_CaseInsensitive() {
        // Given
        entityManager.persistAndFlush(testRecord1);
        entityManager.persistAndFlush(testRecord2);

        // When
        List<EsgBenchmark> result = repository.findByCompanyNameContaining("apple");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Apple Inc", result.get(0).getCompanyName());
    }

    @Test
    void findByCompanyNameContaining_NoMatch() {
        // Given
        entityManager.persistAndFlush(testRecord1);
        entityManager.persistAndFlush(testRecord2);

        // When
        List<EsgBenchmark> result = repository.findByCompanyNameContaining("NonExistent");

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void existsById_Success() {
        // Given
        EsgBenchmark persisted = entityManager.persistAndFlush(testRecord1);

        // When
        boolean result = repository.existsById(persisted.getId());

        // Then
        assertTrue(result);
    }

    @Test
    void existsById_NotFound() {
        // When
        boolean result = repository.existsById(999L);

        // Then
        assertFalse(result);
    }

    @Test
    void deleteById_Success() {
        // Given
        EsgBenchmark persisted = entityManager.persistAndFlush(testRecord1);

        // When
        repository.deleteById(persisted.getId());
        entityManager.flush();

        // Then
        Optional<EsgBenchmark> result = repository.findById(persisted.getId());
        assertFalse(result.isPresent());
    }

    @Test
    void save_Success() {
        // When
        EsgBenchmark result = repository.save(testRecord1);

        // Then
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("Apple Inc", result.getCompanyName());
    }
}
