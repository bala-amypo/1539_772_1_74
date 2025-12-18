package com.example.demo.service;

import java.util.List;

import com.example.demo.model.FraudRule;

public interface FraudRuleService {

    FraudRule addRule(FraudRule rule);

    List<FraudRule> getAllRules();
}
