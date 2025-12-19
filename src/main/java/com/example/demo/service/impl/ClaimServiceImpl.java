package com.example.demo.service.impl;

import com.example.demo.model.Claim;
import com.example.demo.model.Policy;
import com.example.demo.repository.ClaimRepository;
import com.example.demo.repository.PolicyRepository;
import com.example.demo.service.ClaimService;
import com.example.demo.exception.ResourceNotFoundException;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ClaimServiceImpl implements ClaimService {

    private final ClaimRepository claimRepository;
    private final PolicyRepository policyRepository;

    public ClaimServiceImpl(ClaimRepository claimRepository,
                            PolicyRepository policyRepository) {
        this.claimRepository = claimRepository;
        this.policyRepository = policyRepository;
    }

    @Override
    public Claim createClaim(Long policyId, Claim claim) {

        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Policy not found"));

        if (claim.getClaimAmount() < 0) {
            throw new IllegalArgumentException("Invalid claim amount");
        }

        if (claim.getClaimDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Invalid claim date");
        }

        claim.setPolicy(policy);
        claim.setStatus("PENDING");

        return claimRepository.save(claim);
    }

    @Override
    public Claim getClaim(Long claimId) {
        return claimRepository.findById(claimId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Claim not found"));
    }

    @Override
    public List<Claim> getAllClaims() {
        return claimRepository.findAll();
    }
}
