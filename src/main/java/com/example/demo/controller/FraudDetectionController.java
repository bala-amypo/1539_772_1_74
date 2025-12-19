package com.example.demo.controller;

import com.example.demo.model.FraudCheckResult;
import com.example.demo.service.FraudDetectionService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fraud-check")
public class FraudDetectionController {

    private final FraudDetectionService fraudDetectionService;

    public FraudDetectionController(FraudDetectionService fraudDetectionService) {
        this.fraudDetectionService = fraudDetectionService;
    }

    // ✅ EVALUATE CLAIM
    @PostMapping("/evaluate/{claimId}")
    public ResponseEntity<FraudCheckResult> evaluateClaim(@PathVariable Long claimId) {
        return ResponseEntity.ok(
                fraudDetectionService.evaluateClaim(claimId)
        );
    }

    // ✅ GET FRAUD RESULT BY CLAIM
    @GetMapping("/result/claim/{claimId}")
    public ResponseEntity<FraudCheckResult> getResultByClaim(@PathVariable Long claimId) {
        return ResponseEntity.ok(
                fraudDetectionService.getResultByClaim(claimId)
        );
    }
}
