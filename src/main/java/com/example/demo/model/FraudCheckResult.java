package com.example.demo.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "fraud_check_results")
public class FraudCheckResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "claim_id", nullable = false)
    private Claim claim;

    private Boolean isFraudulent;

    private String triggeredRuleName;

    private String rejectionReason;

    private LocalDateTime checkedAt;

    public FraudCheckResult() {
    }

    @PrePersist
    public void onCreate() {
        this.checkedAt = LocalDateTime.now();
    }

    // -------- Getters & Setters --------

    public Long getId() {
        return id;
    }
 
    public void setId(Long id) {
        this.id = id;
    }

    public Claim getClaim() {
        return claim;
    }
 
    public void setClaim(Claim claim) {
        this.claim = claim;
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
}
