package com.internship.tool.repository;

import com.internship.tool.entity.EsgBenchmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EsgBenchmarkRepository extends JpaRepository<EsgBenchmark, Long> {
    
    List<EsgBenchmark> findByCompanyNameContaining(String name);
}
