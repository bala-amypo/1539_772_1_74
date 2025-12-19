package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import com.example.demo.model.FraudRule;
import com.example.demo.service.FraudRuleService;

@RestController
@RequestMapping("/api/rules")
public class FraudRuleController {

    private final FraudRuleService fraudRuleService;

    public FraudRuleController(FraudRuleService fraudRuleService) {
        this.fraudRuleService = fraudRuleService;
    }

    @PostMapping("/")
    public FraudRule addRule(@Valid @RequestBody FraudRule rule) {
        return fraudRuleService.addRule(rule);
    }

    @GetMapping("/")
    public List<FraudRule> getAllRules() {
        return fraudRuleService.getAllRules();
    }
}
