package com.internship.tool.controller;

import com.internship.tool.dto.EsgBenchmarkDto;
import com.internship.tool.entity.EsgBenchmark;
import com.internship.tool.service.EsgBenchmarkService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/esg")
@RequiredArgsConstructor
public class EsgBenchmarkController {

    private final EsgBenchmarkService service;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<EsgBenchmarkDto>> getAllRecords() {
        List<EsgBenchmarkDto> list = service.getAllRecords()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<EsgBenchmarkDto> getRecordById(@PathVariable Long id) {
        EsgBenchmark record = service.getRecordById(id);
        return ResponseEntity.ok(convertToDto(record));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EsgBenchmarkDto> createRecord(@Valid @RequestBody EsgBenchmarkDto dto) {
        EsgBenchmark record = convertToEntity(dto);
        EsgBenchmark savedRecord = service.createRecord(record);
        return new ResponseEntity<>(convertToDto(savedRecord), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EsgBenchmarkDto> updateRecord(@PathVariable Long id, @Valid @RequestBody EsgBenchmarkDto dto) {
        EsgBenchmark record = convertToEntity(dto);
        EsgBenchmark updatedRecord = service.updateRecord(id, record);
        return ResponseEntity.ok(convertToDto(updatedRecord));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        service.deleteRecord(id);
        return ResponseEntity.noContent().build();
    }

    private EsgBenchmarkDto convertToDto(EsgBenchmark entity) {
        return EsgBenchmarkDto.builder()
                .id(entity.getId())
                .companyName(entity.getCompanyName())
                .industry(entity.getIndustry())
                .country(entity.getCountry())
                .esgScore(entity.getEsgScore())
                .environmentalScore(entity.getEnvironmentalScore())
                .socialScore(entity.getSocialScore())
                .governanceScore(entity.getGovernanceScore())
                .benchmarkRank(entity.getBenchmarkRank())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    private EsgBenchmark convertToEntity(EsgBenchmarkDto dto) {
        return EsgBenchmark.builder()
                .id(dto.getId())
                .companyName(dto.getCompanyName())
                .industry(dto.getIndustry())
                .country(dto.getCountry())
                .esgScore(dto.getEsgScore())
                .environmentalScore(dto.getEnvironmentalScore())
                .socialScore(dto.getSocialScore())
                .governanceScore(dto.getGovernanceScore())
                .benchmarkRank(dto.getBenchmarkRank())
                .status(dto.getStatus())
                .build();
    }
}
