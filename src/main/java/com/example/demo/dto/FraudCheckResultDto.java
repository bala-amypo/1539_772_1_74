package com.example.demo.dto;

import java.time.LocalDateTime;

public class FraudCheckResultDto {

    private Long claimId;

    private Boolean isFraudulent;

    private String triggeredRuleName;

    private String rejectionReason;

    private LocalDateTime checkedAt;

    public FraudCheckResultDto() {
    }

    public FraudCheckResultDto(Long claimId,
                               Boolean isFraudulent,
                               String triggeredRuleName,
                               String rejectionReason,
                               LocalDateTime checkedAt) {
        this.claimId = claimId;
        this.isFraudulent = isFraudulent;
        this.triggeredRuleName = triggeredRuleName;
        this.rejectionReason = rejectionReason;
        this.checkedAt = checkedAt;
    }

    // -------- Getters & Setters --------

    public Long getClaimId() {
        return claimId;
    }

    public void setClaimId(Long claimId) {
        this.claimId = claimId;
    }

    public Boolean getIsFraudulent() {
        return isFraudulent;
    }

    public void setIsFraudulent(Boolean isFraudulent) {
        this.isFraudulent = isFraudulent;
    }

    public String getTriggeredRuleName() {
        return triggeredRuleName;
    }

    public void setTriggeredRuleName(String triggeredRuleName) {
        this.triggeredRuleName = triggeredRuleName;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public LocalDateTime getCheckedAt() {
        return checkedAt;
    }

    public void setCheckedAt(LocalDateTime checkedAt) {
        this.checkedAt = checkedAt;
    }
}
