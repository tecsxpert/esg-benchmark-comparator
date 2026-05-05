package com.internship.tool.service;

import com.internship.tool.entity.EsgBenchmark;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Async
    public void sendEsgRecordCreatedNotification(EsgBenchmark record) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo("admin@esg-platform.com");
            message.setSubject("New ESG Record Created: " + record.getCompanyName());
            message.setText(String.format(
                "A new ESG record has been created:\n\n" +
                "Company: %s\n" +
                "Industry: %s\n" +
                "Country: %s\n" +
                "ESG Score: %.1f\n" +
                "Environmental Score: %.1f\n" +
                "Social Score: %.1f\n" +
                "Governance Score: %.1f\n" +
                "Status: %s\n\n" +
                "Please review the record in the ESG platform.",
                record.getCompanyName(),
                record.getIndustry(),
                record.getCountry(),
                record.getEsgScore(),
                record.getEnvironmentalScore(),
                record.getSocialScore(),
                record.getGovernanceScore(),
                record.getStatus()
            ));

            mailSender.send(message);
            log.info("ESG record creation notification sent for company: {}", record.getCompanyName());
        } catch (Exception e) {
            log.error("Failed to send ESG record creation notification: {}", e.getMessage());
        }
    }

    @Async
    public void sendEsgScoreAlert(EsgBenchmark record) {
        if (record.getEsgScore() != null && record.getEsgScore() < 50) {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom(fromEmail);
                message.setTo("admin@esg-platform.com");
                message.setSubject("Low ESG Score Alert: " + record.getCompanyName());
                message.setText(String.format(
                    "ALERT: Low ESG Score detected\n\n" +
                    "Company: %s\n" +
                    "Current ESG Score: %.1f\n" +
                    "Industry: %s\n\n" +
                    "This score is below the acceptable threshold. " +
                    "Please review and take appropriate action.",
                    record.getCompanyName(),
                    record.getEsgScore(),
                    record.getIndustry()
                ));

                mailSender.send(message);
                log.info("Low ESG score alert sent for company: {}", record.getCompanyName());
            } catch (Exception e) {
                log.error("Failed to send low ESG score alert: {}", e.getMessage());
            }
        }
    }

    @Async
    public void sendWeeklyEsgReport(long totalRecords, double averageScore) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo("admin@esg-platform.com");
            message.setSubject("Weekly ESG Report");
            message.setText(String.format(
                "Weekly ESG Platform Report\n\n" +
                "Total Records: %d\n" +
                "Average ESG Score: %.2f\n" +
                "Report Generated: %s\n\n" +
                "Please review the detailed analytics in the platform.",
                totalRecords,
                averageScore,
                java.time.LocalDateTime.now()
            ));

            mailSender.send(message);
            log.info("Weekly ESG report sent successfully");
        } catch (Exception e) {
            log.error("Failed to send weekly ESG report: {}", e.getMessage());
        }
    }
}
