package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.Policy;
import com.example.demo.model.User;
import com.example.demo.repository.PolicyRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.exception.ResourceNotFoundException;

@Service
public class PolicyServiceImpl implements PolicyService {

    private final PolicyRepository policyRepository;
    private final UserRepository userRepository;

    // Constructor injection
    public PolicyServiceImpl(PolicyRepository policyRepository, UserRepository userRepository) {
        this.policyRepository = policyRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Policy createPolicy(Policy policy) {
        // Validate user exists
        Long userId = policy.getUser().getId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        policy.setUser(user);
        return policyRepository.save(policy);
    }

    @Override
    public List<Policy> getPoliciesByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return policyRepository.findAll().stream()
                .filter(p -> p.getUser().getId().equals(userId))
                .toList();
    }
}
