package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;

import com.example.demo.model.FraudCheckResult;
import com.example.demo.service.FraudDetectionService;

@RestController
@RequestMapping("/api/fraud-check")
public class FraudDetectionController {

    private final FraudDetectionService fraudDetectionService;

    public FraudDetectionController(FraudDetectionService fraudDetectionService) {
        this.fraudDetectionService = fraudDetectionService;
    }

    @GetMapping("/evaluate/{claimId}")
    public FraudCheckResult evaluateClaim(@PathVariable Long claimId) {
        return fraudDetectionService.evaluateClaim(claimId);
    }

    @GetMapping("/result/claim/{claimId}")
    public FraudCheckResult getResultByClaim(@PathVariable Long claimId) {
        return fraudDetectionService.getResultByClaim(claimId);
    }
}
