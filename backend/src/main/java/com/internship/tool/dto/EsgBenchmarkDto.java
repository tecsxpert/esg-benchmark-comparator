package com.internship.tool.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EsgBenchmarkDto {

    private Long id;

    @NotBlank(message = "Company name is mandatory")
    @Size(max = 255, message = "Company name cannot exceed 255 characters")
    private String companyName;

    @Size(max = 150, message = "Industry cannot exceed 150 characters")
    private String industry;

    @Size(max = 100, message = "Country cannot exceed 100 characters")
    private String country;

    @Min(value = 0, message = "ESG Score must be at least 0")
    @Max(value = 100, message = "ESG Score cannot exceed 100")
    private Double esgScore;

    @Min(value = 0, message = "Environmental Score must be at least 0")
    @Max(value = 100, message = "Environmental Score cannot exceed 100")
    private Double environmentalScore;

    @Min(value = 0, message = "Social Score must be at least 0")
    @Max(value = 100, message = "Social Score cannot exceed 100")
    private Double socialScore;

    @Min(value = 0, message = "Governance Score must be at least 0")
    @Max(value = 100, message = "Governance Score cannot exceed 100")
    private Double governanceScore;

    @Min(value = 1, message = "Benchmark rank must be positive")
    private Integer benchmarkRank;

    @Size(max = 50, message = "Status cannot exceed 50 characters")
    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
}
