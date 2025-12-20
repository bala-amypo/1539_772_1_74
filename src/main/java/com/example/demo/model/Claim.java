package com.example.demo.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
@Entity
@Table(name = "claims")
public class Claim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    @NotNull
    private LocalDate claimDate;

    @NotNull
    @PositiveOrZero(message = "Claim amount must be >= 0")
    private Double claimAmount;

    private String description;

    private String status = "PENDING";
    @ManyToMany
    @JoinTable(
        name = "claim_fraud_rules",
        joinColumns = @JoinColumn(name = "claim_id"),
        inverseJoinColumns = @JoinColumn(name = "fraud_rule_id")
    )
    private Set<FraudRule> suspectedRules = new HashSet<>();

    public Claim() {}

    public Claim(Policy policy, LocalDate claimDate, Double claimAmount, String description) {
        this.policy = policy;
        this.claimDate = claimDate;
        this.claimAmount = claimAmount;
        this.description = description;
        this.status = "PENDING";
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setStatus(String status) {
        this.status = status;
    }
    public Set<FraudRule> getSuspectedRules() {
        return suspectedRules;
    }

    public void setSuspectedRules(Set<FraudRule> suspectedRules) {
        this.suspectedRules = suspectedRules;
    }
}
