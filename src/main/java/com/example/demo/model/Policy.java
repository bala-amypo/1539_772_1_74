package com.example.demo.model;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "policies", uniqueConstraints = {
        @UniqueConstraint(columnNames = "policyNumber")
})
public class Policy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔗 Policy → User
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "User is required")
    private User user;

    @NotBlank(message = "Policy number is required")
    @Column(nullable = false, unique = true)
    private String policyNumber;

    @NotBlank(message = "Policy type is required")
    private String policyType;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    // 🔗 Policy → Claims
    @OneToMany(mappedBy = "policy", cascade = CascadeType.ALL)
    private List<Claim> claims;

    public Policy() {
    }

    public Policy(User user, String policyNumber, String policyType,
                  LocalDate startDate, LocalDate endDate) {
        this.user = user;
        this.policyNumber = policyNumber;
        this.policyType = policyType;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getId() {
        return id;
    }
 
    public void setId(Long id) {
        this.id = id;
    }
 
    public User getUser() {
        return user;
    }
 
    public void setUser(User user) {
        this.user = user;
    }
 
    public String getPolicyNumber() {
        return policyNumber;
    }
 
    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }
 
    public String getPolicyType() {
        return policyType;
    }
 
    public void setPolicyType(String policyType) {
        this.policyType = policyType;
    }
 
    public LocalDate getStartDate() {
        return startDate;
    }
 
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
 
    public LocalDate getEndDate() {
        return endDate;
    }
 
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
