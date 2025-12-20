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

                // 🔥 ADD RULE TO CLAIM
                claim.getSuspectedRules().add(rule);

                break;
            }
        }
    }

    // 🔥 UPDATE CLAIM STATUS
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
