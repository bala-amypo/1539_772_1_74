package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class FraudRuleDto {

    // Response only
    private Long id;

    @NotBlank(message = "Rule name is required")
    private String ruleName;

    @NotBlank(message = "Condition field is required")
    private String conditionField;

    @NotBlank(message = "Operator is required")
    @Pattern(
        regexp = ">|<|>=|<=|=",
        message = "Operator must be one of >, <, >=, <=, ="
    )
    private String operator;

    @NotBlank(message = "Threshold value is required")
    private String value;

    @NotBlank(message = "Severity is required")
    @Pattern(
        regexp = "LOW|MEDIUM|HIGH",
        message = "Severity must be LOW, MEDIUM, or HIGH"
    )
    private String severity;

    public FraudRuleDto() {
    }

    public FraudRuleDto(Long id, String ruleName, String conditionField,
                        String operator, String value, String severity) {
        this.id = id;
        this.ruleName = ruleName;
        this.conditionField = conditionField;
        this.operator = operator;
        this.value = value;
        this.severity = severity;
    }

    // -------- Getters & Setters --------

    public Long getId() {
        return id;
    }
 
    public void setId(Long id) {
        this.id = id;
    }

    public String getRuleName() {
        return ruleName;
    }
 
    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getConditionField() {
        return conditionField;
    }
 
    public void setConditionField(String conditionField) {
        this.conditionField = conditionField;
    }

    public String getOperator() {
        return operator;
    }
 
    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }
 
    public void setValue(String value) {
        this.value = value;
    }

    public String getSeverity() {
        return severity;
    }
 
    public void setSeverity(String severity) {
        this.severity = severity;
    }
}
