package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Claim;
import com.example.demo.model.FraudCheckResult;
import com.example.demo.model.FraudRule;
import com.example.demo.repository.ClaimRepository;
import com.example.demo.repository.FraudCheckResultRepository;
import com.example.demo.repository.FraudRuleRepository;
import com.example.demo.service.FraudDetectionService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FraudDetectionServiceImpl implements FraudDetectionService {

    private final ClaimRepository claimRepository;
    private final FraudRuleRepository fraudRuleRepository;
    private final FraudCheckResultRepository resultRepository;

    public FraudDetectionServiceImpl(
            ClaimRepository claimRepository,
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
                boolean matched = false;

                switch (rule.getOperator()) {
                    case ">":  matched = claim.getClaimAmount() > threshold; break;
                    case "<":  matched = claim.getClaimAmount() < threshold; break;
                    case ">=": matched = claim.getClaimAmount() >= threshold; break;
                    case "<=": matched = claim.getClaimAmount() <= threshold; break;
                    case "=":  matched = claim.getClaimAmount().doubleValue() == threshold; break;
                }

                if (matched) {
                    isFraud = true;
                    triggeredRuleName = rule.getRuleName();
                    reason = "Claim amount triggered fraud threshold: "
                            + rule.getOperator() + " " + threshold;

                    // add rule to claim
                    claim.getSuspectedRules().add(rule);
                    break;
                }
            }
        }

        // update claim status
        claim.setStatus(isFraud ? "REJECTED" : "APPROVED");
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
