package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.Claim;
import com.example.demo.model.Policy;
import com.example.demo.repository.ClaimRepository;
import com.example.demo.repository.PolicyRepository;
import com.example.demo.exception.ResourceNotFoundException;

@Service
public class ClaimServiceImpl implements ClaimService {

    private final ClaimRepository claimRepository;
    private final PolicyRepository policyRepository;

    // Constructor injection
    public ClaimServiceImpl(ClaimRepository claimRepository, PolicyRepository policyRepository) {
        this.claimRepository = claimRepository;
        this.policyRepository = policyRepository;
    }

    @Override
    public Claim createClaim(Claim claim) {
        Long policyId = claim.getPolicy().getId();
        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new ResourceNotFoundException("Policy not found with id: " + policyId));

        // Validate claim amount
        if (claim.getClaimAmount() == null || claim.getClaimAmount() <= 0) {
            throw new IllegalArgumentException("Invalid claim amount");
        }

        // Validate claim date
        if (claim.getClaimDate() == null || claim.getClaimDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Invalid claim date");
        }

        claim.setPolicy(policy);
        return claimRepository.save(claim);
    }

    @Override
    public Claim getClaim(Long claimId) {
        return claimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found with id: " + claimId));
    }

    @Override
    public List<Claim> getAllClaims() {
        return claimRepository.findAll();
    }
}
