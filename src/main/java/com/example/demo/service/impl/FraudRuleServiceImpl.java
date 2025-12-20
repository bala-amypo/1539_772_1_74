package com.example.demo.service.impl;

import com.example.demo.model.FraudRule;
import com.example.demo.repository.FraudRuleRepository;
import com.example.demo.service.FraudRuleService;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FraudRuleServiceImpl implements FraudRuleService {

    private final FraudRuleRepository fraudRuleRepository;

    public FraudRuleServiceImpl(FraudRuleRepository fraudRuleRepository) {
        this.fraudRuleRepository = fraudRuleRepository;
    }

    @Override
    public FraudRule addRule(FraudRule rule) {

        fraudRuleRepository.findByRuleName(rule.getRuleName())
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("Invalid or duplicate rule name");
                });

        if (!rule.getSeverity().equals("LOW") &&
            !rule.getSeverity().equals("MEDIUM") &&
            !rule.getSeverity().equals("HIGH")) {
            throw new IllegalArgumentException("Invalid severity level");
        }

        return fraudRuleRepository.save(rule);
    }

    @Override
    public List<FraudRule> getAllRules() {
        return fraudRuleRepository.findAll();
    }
}
