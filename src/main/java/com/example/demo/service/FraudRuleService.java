package com.example.demo.service;

import com.example.demo.model.FraudRule;

import java.util.List;

public interface FraudRuleService {

    FraudRule addRule(FraudRule rule);

    List<FraudRule> getAllRules();
}
