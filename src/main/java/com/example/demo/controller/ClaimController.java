package com.example.demo.controller;

import com.example.demo.model.Claim;
import com.example.demo.service.ClaimService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/claims")
public class ClaimController {

    private final ClaimService claimService;

    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }

    // ✅ CREATE CLAIM
    @PostMapping("/{policyId}")
    public ResponseEntity<Claim> createClaim(
            @PathVariable Long policyId,
            @Valid @RequestBody Claim claim) {

        return ResponseEntity.ok(
                claimService.createClaim(policyId, claim)
        );
    }

    // ✅ GET CLAIM BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Claim> getClaim(@PathVariable Long id) {
        return ResponseEntity.ok(
                claimService.getClaim(id)
        );
    }

    // ✅ GET ALL CLAIMS (ADMIN VIEW)
    @GetMapping
    public ResponseEntity<List<Claim>> getAllClaims() {
        return ResponseEntity.ok(
                claimService.getAllClaims()
        );
    }
}
