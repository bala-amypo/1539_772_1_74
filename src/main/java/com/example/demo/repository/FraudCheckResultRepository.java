package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.FraudCheckResult;

public interface FraudCheckResultRepository extends JpaRepository<FraudCheckResult, Long> {
    // default JpaRepository methods are enough
}
