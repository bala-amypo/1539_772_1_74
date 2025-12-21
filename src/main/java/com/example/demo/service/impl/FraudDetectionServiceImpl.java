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

        // 1️⃣ Load claim
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found"));

        // 2️⃣ Load all rules
        List<FraudRule> rules = fraudRuleRepository.findAll();

        boolean isFraud = false;
        String triggeredRuleName = null;
        String reason = null;

        // 3️⃣ Evaluate rules
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

        // 🔥 4️⃣ UPDATE CLAIM STATUS (THIS WAS MISSING)
        if (isFraud) {
            claim.setStatus("REJECTED");
        } else {
            claim.setStatus("APPROVED");
        }

        // 🔥 5️⃣ SAVE UPDATED CLAIM
        claimRepository.save(claim);

        // 6️⃣ Create fraud result
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
