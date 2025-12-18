package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.Claim;
import com.example.demo.model.FraudCheckResult;
import com.example.demo.model.FraudRule;
import com.example.demo.repository.ClaimRepository;
import com.example.demo.repository.FraudCheckResultRepository;
import com.example.demo.repository.FraudRuleRepository;
import com.example.demo.exception.ResourceNotFoundException;

@Service
public class FraudDetectionServiceImpl implements FraudDetectionService {

    private final ClaimRepository claimRepository;
    private final FraudRuleRepository fraudRuleRepository;
    private final FraudCheckResultRepository fraudCheckResultRepository;

    // Constructor injection
    public FraudDetectionServiceImpl(ClaimRepository claimRepository,
                                     FraudRuleRepository fraudRuleRepository,
                                     FraudCheckResultRepository fraudCheckResultRepository) {
        this.claimRepository = claimRepository;
        this.fraudRuleRepository = fraudRuleRepository;
        this.fraudCheckResultRepository = fraudCheckResultRepository;
    }

    @Override
    public FraudCheckResult evaluateClaim(Long claimId) {
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found with id: " + claimId));

        List<FraudRule> rules = fraudRuleRepository.findAll();
        FraudCheckResult result = new FraudCheckResult();
        result.setClaim(claim);
        result.setIsFraudulent(false);

        for (FraudRule rule : rules) {
            // For simplicity, only handle claimAmount rules
            if ("claimAmount".equals(rule.getConditionField())) {
                double value = Double.parseDouble(rule.getValue());
                boolean triggered = false;

                switch (rule.getOperator()) {
                    case ">":
                        triggered = claim.getClaimAmount() > value;
                        break;
                    case "<":
                        triggered = claim.getClaimAmount() < value;
                        break;
                    case "=":
                        triggered = claim.getClaimAmount() == value;
                        break;
                    case ">=":
                        triggered = claim.getClaimAmount() >= value;
                        break;
                    case "<=":
                        triggered = claim.getClaimAmount() <= value;
                        break;
                }

                if (triggered) {
                    result.setIsFraudulent(true);
                    result.setTriggeredRuleName(rule.getRuleName());
                    result.setRejectionReason("Claim amount triggered rule");
                    break; // stop at first matching rule
                }
            }
        }

        return fraudCheckResultRepository.save(result);
    }

    @Override
    public FraudCheckResult getResultByClaim(Long claimId) {
        return fraudCheckResultRepository.findAll().stream()
                .filter(r -> r.getClaim().getId().equals(claimId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Fraud check result not found for claim id: " + claimId));
    }
}
