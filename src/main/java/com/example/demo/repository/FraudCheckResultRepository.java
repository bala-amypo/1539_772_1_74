package com.example.demo.repository;

import com.example.demo.model.FraudCheckResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FraudCheckResultRepository extends JpaRepository<FraudCheckResult, Long> {

    Optional<FraudCheckResult> findByClaimId(Long claimId);
}
