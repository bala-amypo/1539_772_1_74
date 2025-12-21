package com.example.demo.controller;
import com.example.demo.model.FraudRule;
import com.example.demo.service.FraudRuleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/rules")
public class FraudRuleController {

    private final FraudRuleService fraudRuleService;

    public FraudRuleController(FraudRuleService fraudRuleService) {
        this.fraudRuleService = fraudRuleService;
    }

    // ADD FRAUD RULE
    @PostMapping
    public ResponseEntity<FraudRule> addRule(
            @Valid @RequestBody FraudRule rule) {

        return ResponseEntity.ok(
                fraudRuleService.addRule(rule)
        );
    }

    //GET ALL FRAUD RULES
    @GetMapping
    public ResponseEntity<List<FraudRule>> getAllRules() {

        return ResponseEntity.ok(
                fraudRuleService.getAllRules()
        );
    }
}
