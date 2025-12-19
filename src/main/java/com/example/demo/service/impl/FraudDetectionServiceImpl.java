package com.example.demo.service.impl;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.FraudDetectionService;
import com.example.demo.exception.ResourceNotFoundException;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FraudDetectionServiceImpl implements FraudDetectionService {

    private final ClaimRepository claimRepository;
    private final FraudRuleRepository fraudRuleRepository;
    private final FraudCheckResultRepository resultRepository;

    public FraudDetectionServiceImpl(ClaimRepository claimRepository,
                                     FraudRuleRepository fraudRuleRepository,
                                     FraudCheckResultRepository resultRepository) {
        this.claimRepository = claimRepository;
        this.fraudRuleRepository = fraudRuleRepository;
        this.resultRepository = resultRepository;
    }

    @Override
    public FraudCheckResult evaluateClaim(Long claimId) {

        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found"));

        List<FraudRule> rules = fraudRuleRepository.findAll();

        boolean isFraud = false;
        String triggeredRuleName = null;
        String reason = null;

        for (FraudRule rule : rules) {
            // Simple example: only checking claimAmount
            if ("claimAmount".equals(rule.getConditionField())) {
                double threshold = Double.parseDouble(rule.getValue());
                switch (rule.getOperator()) {
                    case ">":
                        if (claim.getClaimAmount() > threshold) {
                            isFraud = true;
                        }
                        break;
                    case "<":
                        if (claim.getClaimAmount() < threshold) {
                            isFraud = true;
                        }
                        break;
                    case ">=":
                        if (claim.getClaimAmount() >= threshold) {
                            isFraud = true;
                        }
                        break;
                    case "<=":
                        if (claim.getClaimAmount() <= threshold) {
                            isFraud = true;
                        }
                        break;
                    case "=":
                        if (claim.getClaimAmount().equals(threshold)) {
                            isFraud = true;
                        }
                        break;
                }

                if (isFraud) {
                    triggeredRuleName = rule.getRuleName();
                    reason = "Claim amount triggered fraud threshold: " + rule.getOperator() + " " + threshold;
                    break;
                }
            }
        }

        FraudCheckResult result = new FraudCheckResult(
                claim,
                isFraud,
                triggeredRuleName,
                reason,
                LocalDateTime.now()
        );

        return resultRepository.save(result);
    }

    @Override
    public FraudCheckResult getResultByClaim(Long claimId) {
        return resultRepository.findByClaimId(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Result not found"));
    }
}
