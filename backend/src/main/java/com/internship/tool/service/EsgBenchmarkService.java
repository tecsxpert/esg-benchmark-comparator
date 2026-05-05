package com.internship.tool.service;

import com.internship.tool.entity.EsgBenchmark;
import com.internship.tool.exception.InvalidInputException;
import com.internship.tool.exception.ResourceNotFoundException;
import com.internship.tool.repository.EsgBenchmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EsgBenchmarkService {

    private final EsgBenchmarkRepository repository;
    private final EmailService emailService;

    @Transactional
    @CacheEvict(value = "esg_records", allEntries = true)
    public EsgBenchmark createRecord(EsgBenchmark record) {
        validateRecord(record);
        applyDefaults(record);
        EsgBenchmark savedRecord = repository.save(record);
        
        // Send email notification
        emailService.sendEsgRecordCreatedNotification(savedRecord);
        emailService.sendEsgScoreAlert(savedRecord);
        
        return savedRecord;
    }

    public List<EsgBenchmark> getAllRecords() {
        return repository.findAll();
    }

    @Cacheable(value = "esg_records", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<EsgBenchmark> getAllRecords(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Cacheable(value = "esg_record", key = "#id")
    public EsgBenchmark getRecordById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ESG record not found with id: " + id));
    }

    @Transactional
    @CacheEvict(value = {"esg_records", "esg_record"}, allEntries = true)
    public EsgBenchmark updateRecord(Long id, EsgBenchmark updatedRecord) {
        EsgBenchmark existingRecord = getRecordById(id);
        
        validateRecord(updatedRecord);

        existingRecord.setCompanyName(updatedRecord.getCompanyName());
        existingRecord.setIndustry(updatedRecord.getIndustry());
        existingRecord.setCountry(updatedRecord.getCountry());
        existingRecord.setEsgScore(updatedRecord.getEsgScore());
        existingRecord.setEnvironmentalScore(updatedRecord.getEnvironmentalScore());
        existingRecord.setSocialScore(updatedRecord.getSocialScore());
        existingRecord.setGovernanceScore(updatedRecord.getGovernanceScore());
        existingRecord.setBenchmarkRank(updatedRecord.getBenchmarkRank());
        existingRecord.setStatus(updatedRecord.getStatus() == null ? existingRecord.getStatus() : updatedRecord.getStatus());

        return repository.save(existingRecord);
    }

    @Transactional
    @CacheEvict(value = {"esg_records", "esg_record"}, allEntries = true)
    public void deleteRecord(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("ESG record not found with id: " + id);
        }
        repository.deleteById(id);
    }

    private void validateRecord(EsgBenchmark record) {
        if (record.getCompanyName() == null || record.getCompanyName().trim().isEmpty()) {
            throw new InvalidInputException("Company name cannot be empty");
        }
        if (record.getEsgScore() != null && (record.getEsgScore() < 0 || record.getEsgScore() > 100)) {
            throw new InvalidInputException("ESG Score must be between 0 and 100");
        }
        if (record.getEnvironmentalScore() != null && (record.getEnvironmentalScore() < 0 || record.getEnvironmentalScore() > 100)) {
            throw new InvalidInputException("Environmental Score must be between 0 and 100");
        }
        if (record.getSocialScore() != null && (record.getSocialScore() < 0 || record.getSocialScore() > 100)) {
            throw new InvalidInputException("Social Score must be between 0 and 100");
        }
        if (record.getGovernanceScore() != null && (record.getGovernanceScore() < 0 || record.getGovernanceScore() > 100)) {
            throw new InvalidInputException("Governance Score must be between 0 and 100");
        }
        if (record.getBenchmarkRank() != null && record.getBenchmarkRank() < 1) {
            throw new InvalidInputException("Benchmark rank must be positive");
        }
    }

    private void applyDefaults(EsgBenchmark record) {
        if (record.getStatus() == null || record.getStatus().trim().isEmpty()) {
            record.setStatus("ACTIVE");
        }
    }
}
