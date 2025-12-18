package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Policy;

public interface PolicyRepository extends JpaRepository<Policy, Long> {

    Optional<Policy> findByPolicyNumber(String policyNumber);
}
