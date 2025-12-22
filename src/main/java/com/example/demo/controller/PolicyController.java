package com.example.demo.controller;
import com.example.demo.model.Policy;
import com.example.demo.service.PolicyService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/policies")
public class PolicyController {

    private final PolicyService policyService;

    public PolicyController(PolicyService policyService) {
        this.policyService = policyService;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Policy> createPolicy(
            @PathVariable Long userId,
            @Valid @RequestBody Policy policy) {

        return ResponseEntity.ok(
                policyService.createPolicy(userId, policy)
        );
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Policy>> getPoliciesByUser(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                policyService.getPoliciesByUser(userId)
        );
    }
}
