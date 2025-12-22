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

            if ("claimAmount".equals(rule.getConditionField())) {

                double threshold = Double.parseDouble(rule.getValue());
                double amount = claim.getClaimAmount();

                boolean ruleMatched = false;

                switch (rule.getOperator()) {
                    case ">":
                        ruleMatched = amount > threshold;
                        break;
                    case "<":
                        ruleMatched = amount < threshold;
                        break;
                    case ">=":
                        ruleMatched = amount >= threshold;
                        break;
                    case "<=":
                        ruleMatched = amount <= threshold;
                        break;
                    case "=":
                        ruleMatched = amount == threshold;
                        break;
                }

                if (ruleMatched) {
                    isFraud = true;
                    triggeredRuleName = rule.getRuleName();
                    reason = "Claim rejected due to rule: " + rule.getRuleName();
                    break;
                }
            }
        }
        if (isFraud) {
            claim.setStatus("REJECTED");
        } else {
            claim.setStatus("APPROVED");
        }

        claimRepository.save(claim);

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
