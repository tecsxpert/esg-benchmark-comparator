package com.internship.tool.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.internship.tool.dto.EsgBenchmarkDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${ai.service.url:http://ai-service:5000}")
    private String aiServiceUrl;

    public Map<String, Object> analyzeEsg(EsgBenchmarkDto esgData) {
        try {
            String url = aiServiceUrl + "/api/ai/analyze-esg";
            Map<String, Object> response = restTemplate.postForObject(url, esgData, Map.class);
            log.info("ESG analysis completed for company: {}", esgData.getCompanyName());
            return response;
        } catch (Exception e) {
            log.error("Error analyzing ESG data: {}", e.getMessage());
            throw new RuntimeException("Failed to analyze ESG data", e);
        }
    }

    public Map<String, Object> benchmarkComparison(List<EsgBenchmarkDto> companies) {
        try {
            String url = aiServiceUrl + "/api/ai/benchmark-comparison";
            Map<String, Object> request = Map.of("companies", companies);
            Map<String, Object> response = restTemplate.postForObject(url, request, Map.class);
            log.info("Benchmark comparison completed for {} companies", companies.size());
            return response;
        } catch (Exception e) {
            log.error("Error in benchmark comparison: {}", e.getMessage());
            throw new RuntimeException("Failed to perform benchmark comparison", e);
        }
    }
}
