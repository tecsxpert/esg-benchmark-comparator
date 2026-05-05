package com.internship.tool.service;

import com.internship.tool.entity.EsgBenchmark;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@EnableScheduling
@Slf4j
public class ScheduledTasksService {

    private final EsgBenchmarkService esgBenchmarkService;
    private final EmailService emailService;

    // Run every Monday at 9:00 AM
    @Scheduled(cron = "0 0 9 * * MON")
    public void sendWeeklyEsgReport() {
        try {
            log.info("Starting weekly ESG report generation");
            
            List<EsgBenchmark> allRecords = esgBenchmarkService.getAllRecords();
            long totalRecords = allRecords.size();
            
            double averageScore = allRecords.stream()
                    .filter(record -> record.getEsgScore() != null)
                    .mapToDouble(EsgBenchmark::getEsgScore)
                    .average()
                    .orElse(0.0);
            
            emailService.sendWeeklyEsgReport(totalRecords, averageScore);
            
            log.info("Weekly ESG report completed. Total records: {}, Average score: {}", 
                    totalRecords, averageScore);
        } catch (Exception e) {
            log.error("Error generating weekly ESG report: {}", e.getMessage());
        }
    }

    // Run every day at 8:00 AM to check for low ESG scores
    @Scheduled(cron = "0 0 8 * * *")
    public void checkLowEsgScores() {
        try {
            log.info("Starting daily low ESG score check");
            
            List<EsgBenchmark> allRecords = esgBenchmarkService.getAllRecords();
            
            allRecords.stream()
                    .filter(record -> record.getEsgScore() != null && record.getEsgScore() < 50)
                    .forEach(record -> {
                        log.warn("Low ESG score detected for company: {} with score: {}", 
                                record.getCompanyName(), record.getEsgScore());
                        emailService.sendEsgScoreAlert(record);
                    });
            
            log.info("Daily low ESG score check completed");
        } catch (Exception e) {
            log.error("Error during low ESG score check: {}", e.getMessage());
        }
    }

    // Run every hour to cleanup expired caches (if needed)
    @Scheduled(fixedRate = 3600000) // 1 hour in milliseconds
    public void cleanupExpiredCaches() {
        try {
            log.info("Starting cache cleanup task");
            
            // This is a placeholder for cache cleanup logic
            // In a real application, you might want to cleanup old cache entries
            // or perform other maintenance tasks
            
            log.info("Cache cleanup completed");
        } catch (Exception e) {
            log.error("Error during cache cleanup: {}", e.getMessage());
        }
    }
}
