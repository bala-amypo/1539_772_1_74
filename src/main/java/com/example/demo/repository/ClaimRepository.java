package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Claim;

public interface ClaimRepository extends JpaRepository<Claim, Long> {

    List<Claim> findByPolicyId(Long policyId);
}
