package com.example.demo.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

public class ClaimDto {

    // Response only
    private Long id;

    // Request / Response
    @NotNull(message = "Policy ID is required")
    private Long policyId;

    @NotNull(message = "Claim date is required")
    @PastOrPresent(message = "Claim date cannot be in the future")
    private LocalDate claimDate;

    @NotNull(message = "Claim amount is required")
    @DecimalMin(value = "0.0", inclusive = true,
            message = "Claim amount must be greater than or equal to 0")
    private Double claimAmount;

    @NotBlank(message = "Description is required")
    private String description;

    // Response only (PENDING / APPROVED / REJECTED)
    private String status;

    public ClaimDto() {
    }

    public ClaimDto(Long id, Long policyId, LocalDate claimDate,
                    Double claimAmount, String description, String status) {
        this.id = id;
        this.policyId = policyId;
        this.claimDate = claimDate;
        this.claimAmount = claimAmount;
        this.description = description;
        this.status = status;
    }

    // -------- Getters & Setters --------

    public Long getId() {
        return id;
    }
 
    public void setId(Long id) {
        this.id = id;
    }

    public Long getPolicyId() {
        return policyId;
    }
 
    public void setPolicyId(Long policyId) {
        this.policyId = policyId;
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
