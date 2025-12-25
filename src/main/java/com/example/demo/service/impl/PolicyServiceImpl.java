package com.example.demo.service.impl;

import com.example.demo.model.Policy;
import com.example.demo.model.User;
import com.example.demo.repository.PolicyRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.PolicyService;
import com.example.demo.exception.ResourceNotFoundException;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class PolicyServiceImpl implements PolicyService {

    private final PolicyRepository policyRepository;
    private final UserRepository userRepository;

    public PolicyServiceImpl(PolicyRepository policyRepository,
                             UserRepository userRepository) {
        this.policyRepository = policyRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Policy createPolicy(Long userId, Policy policy) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        if (policyRepository.existsByPolicyNumber(policy.getPolicyNumber())) {
            throw new IllegalArgumentException("Policy number already exists");
        }

        if (!policy.getEndDate().isAfter(policy.getStartDate())) {
            throw new IllegalArgumentException("End date must be after start date");
        }

        policy.setUser(user);
        return policyRepository.save(policy);
    }

    @Override
    public List<Policy> getPoliciesByUser(Long userId) {

        // ⭐ IMPORTANT CHANGE: do NOT throw if user doesn't exist
        if (!userRepository.existsById(userId)) {
            return Collections.emptyList();
        }

        return policyRepository.findByUserId(userId);
    }

    @Override
    public Policy getPolicy(Long id) {
        return policyRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Policy not found"));
    }
}
