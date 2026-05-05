package com.internship.backend.repository;

import com.internship.backend.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByActionContainingIgnoreCase(String action);

    List<AuditLog> findByDeletedFalse();
}
