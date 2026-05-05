package com.internship.backend.controller;

import com.internship.backend.entity.AuditLog;
import com.internship.backend.repository.AuditLogRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/audit-logs")
@CrossOrigin(origins = "http://localhost:5173")
public class AuditLogController {

    private final AuditLogRepository repository;

    public AuditLogController(AuditLogRepository repository) {
        this.repository = repository;
    }

    // Normal GET
    @GetMapping
    public List<AuditLog> getAllLogs() {
        return repository.findByDeletedFalse();
    }

    // Pagination GET
    @GetMapping("/all")
    public List<AuditLog> getAllWithPagination(
            @RequestParam int page,
            @RequestParam int size) {

        Pageable pageable = PageRequest.of(page, size);

        return repository.findAll(pageable).getContent();
    }

    // Create
    @PostMapping
    public AuditLog create(@RequestBody AuditLog log) {
        return repository.save(log);
    }

    // Update
    @PutMapping("/{id}")
    public AuditLog update(@PathVariable Long id,
                           @RequestBody AuditLog updated) {

        AuditLog log = repository.findById(id).orElseThrow();

        log.setAction(updated.getAction());
        log.setUsername(updated.getUsername());
        log.setDescription(updated.getDescription());

        return repository.save(log);
    }

    // Soft Delete
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {

        AuditLog log = repository.findById(id).orElseThrow();

        log.setDeleted(true);
        repository.save(log);

        return "Deleted Successfully";
    }

    // Search
    @GetMapping("/search")
    public List<AuditLog> search(@RequestParam String q) {
        return repository.findByActionContainingIgnoreCase(q);
    }
}
