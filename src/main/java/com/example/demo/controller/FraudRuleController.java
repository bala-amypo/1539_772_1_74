package com.example.demo.controller;

import com.example.demo.dto.FraudRuleDto;
import com.example.demo.model.FraudRule;
import com.example.demo.service.FraudRuleService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rules")
public class FraudRuleController {

    private final FraudRuleService fraudRuleService;

    public FraudRuleController(FraudRuleService fraudRuleService) {
        this.fraudRuleService = fraudRuleService;
    }

    @PostMapping
    public ResponseEntity<FraudRuleDto> addRule(
            @Valid @RequestBody FraudRuleDto dto) {

        FraudRule rule = new FraudRule(
                dto.getRuleName(),
                dto.getConditionField(),
                dto.getOperator(),
                dto.getValue(),
                dto.getSeverity()
        );

        FraudRule saved = fraudRuleService.addRule(rule);
        dto.setId(saved.getId());

        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<FraudRuleDto>> getAllRules() {
        return ResponseEntity.ok(
                fraudRuleService.getAllRules()
                        .stream()
                        .map(rule -> new FraudRuleDto(
                                rule.getId(),
                                rule.getRuleName(),
                                rule.getConditionField(),
                                rule.getOperator(),
                                rule.getValue(),
                                rule.getSeverity()
                        ))
                        .collect(Collectors.toList())
        );
    }
}
