package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.FraudRule;
import com.example.demo.repository.FraudRuleRepository;

@Service
public class FraudRuleServiceImpl implements FraudRuleService {

    private final FraudRuleRepository fraudRuleRepository;

    // Constructor injection
    public FraudRuleServiceImpl(FraudRuleRepository fraudRuleRepository) {
        this.fraudRuleRepository = fraudRuleRepository;
    }

    @Override
    public FraudRule addRule(FraudRule rule) {
        return fraudRuleRepository.save(rule);
    }

    @Override
    public List<FraudRule> getAllRules() {
        return fraudRuleRepository.findAll();
    }
}
