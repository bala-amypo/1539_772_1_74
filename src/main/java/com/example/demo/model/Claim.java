package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "claims")
public class Claim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔗 Claim → Policy
    @ManyToOne
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    @NotNull(message = "Claim date is required")
    private LocalDate claimDate;

    @NotNull(message = "Claim amount is required")
    @PositiveOrZero(message = "Claim amount must be >= 0")
    private Double claimAmount;

    @NotBlank(message = "Description is required")
    private String description;

    private String status = "PENDING";

    // 🔗 Claim ↔ FraudRule (Many-to-Many)
    @ManyToMany
    @JoinTable(
            name = "claim_fraud_rules",
            joinColumns = @JoinColumn(name = "claim_id"),
            inverseJoinColumns = @JoinColumn(name = "fraud_rule_id")
    )
    private Set<FraudRule> fraudRules;

    // 🔗 Claim → FraudCheckResult (One-to-One)
    @OneToOne(mappedBy = "claim", cascade = CascadeType.ALL)
    private FraudCheckResult fraudCheckResult;

    public Claim() {
    }

    public Claim(Policy policy, LocalDate claimDate,
                 Double claimAmount, String description) {
        this.policy = policy;
        this.claimDate = claimDate;
        this.claimAmount = claimAmount;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public Policy getPolicy() {
        return policy;
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
    }

    public LocalDate getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(LocalDate claimDate) {
        this.claimDate = claimDate;
    }

    public Double getClaimAmount() {
        return claimAmount;
    }

    public void setClaimAmount(Double claimAmount) {
        this.claimAmount = claimAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }
}
