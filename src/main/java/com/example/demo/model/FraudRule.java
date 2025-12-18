package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "fraud_rules", uniqueConstraints = {
        @UniqueConstraint(columnNames = "ruleName")
})
public class FraudRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String ruleName;

    // example: "claimAmount"
    private String conditionField;

    // >, <, =, >=, <=
    private String operator;

    // value stored as String for flexibility
    private String value;

    // LOW / MEDIUM / HIGH
    private String severity;

    public FraudRule() {
    }

    public FraudRule(String ruleName, String conditionField,
                     String operator, String value, String severity) {
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
