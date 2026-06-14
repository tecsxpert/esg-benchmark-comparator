package com.internship.backend.controller;

import com.internship.backend.model.ESGDocument;
import com.internship.backend.repository.ESGRepository;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api")
public class ESGController {

    private final ESGRepository repository;

    public ESGController(ESGRepository repository) {
        this.repository = repository;
    }

    @Operation(summary = "Create ESG record")
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ESGDocument data) {
        return ResponseEntity.ok(repository.save(data));
    }

    @Operation(summary = "Get all ESG records")
    @GetMapping("/all")
    public List<ESGDocument> getAll() {
        return repository.findAll();
    }

    @Operation(summary = "Update ESG record")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable String id,
                                    @RequestBody ESGDocument data) {
        data.setId(id);
        return ResponseEntity.ok(repository.save(data));
    }

    @Operation(summary = "Delete ESG record")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        repository.deleteById(id);
        return ResponseEntity.ok("Deleted successfully");
    }

    // ✅ CSV EXPORT
    @Operation(summary = "Export all data as CSV")
    @GetMapping("/export")
    public void exportCSV(HttpServletResponse response) throws IOException {

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=esg_data.csv");

        List<ESGDocument> list = repository.findAll();

        PrintWriter writer = response.getWriter();
        writer.println("ID,Name,Score");

        for (ESGDocument e : list) {
            writer.println(e.getId() + "," + e.getName() + "," + e.getScore());
        }

        writer.flush();
    }

    // ✅ FILE UPLOAD (FIXED)
    @Operation(summary = "Upload file with validation")
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {

        // Empty check
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        // File type check
        String filename = file.getOriginalFilename();
        if (filename == null ||
                !(filename.endsWith(".csv") || filename.endsWith(".txt"))) {
            return ResponseEntity.badRequest().body("Only CSV or TXT files allowed");
        }

        // Size check (2MB)
        if (file.getSize() > 2 * 1024 * 1024) {
            return ResponseEntity.badRequest().body("File too large (Max 2MB)");
        }

        return ResponseEntity.ok("File uploaded successfully: " + filename);
    }
    @Operation(summary = "Get analytics data")
@GetMapping("/analytics")
public List<ESGDocument> getAnalytics() {
    return repository.findAll();
}
}