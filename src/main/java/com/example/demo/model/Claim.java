package com.example.demo.model;
import java.time.LocalDate;
import jakarta.persistence.*;
@Entity
@Table(name = "claims")
public class Claim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    private LocalDate claimDate;

    private Double claimAmount;

    private String description;

    private String status = "PENDING";

    public Claim() {
    }

    public Claim(Policy policy, LocalDate claimDate,
                 Double claimAmount, String description) {
        this.policy = policy;
        this.claimDate = claimDate;
        this.claimAmount = claimAmount;
        this.description = description;
        this.status = "PENDING";
    }

    // -------- Getters & Setters --------

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
}
