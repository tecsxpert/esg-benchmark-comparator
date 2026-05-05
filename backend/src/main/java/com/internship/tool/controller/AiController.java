package com.internship.tool.controller;

import com.internship.tool.dto.EsgBenchmarkDto;
import com.internship.tool.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @PostMapping("/analyze-esg")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> analyzeEsg(@RequestBody EsgBenchmarkDto esgData) {
        Map<String, Object> analysis = aiService.analyzeEsg(esgData);
        return ResponseEntity.ok(analysis);
    }

    @PostMapping("/benchmark-comparison")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> benchmarkComparison(@RequestBody List<EsgBenchmarkDto> companies) {
        Map<String, Object> comparison = aiService.benchmarkComparison(companies);
        return ResponseEntity.ok(comparison);
    }
}
