package com.example.demo.util;

public class HqlQueryHelper {

    private HqlQueryHelper() {
        // Utility class – prevent object creation
    }

    // 🔍 Fetch all claims for a policy
    public static final String FIND_CLAIMS_BY_POLICY_ID =
            "SELECT c FROM Claim c WHERE c.policy.id = :policyId";

    // 🔍 Fetch high value claims
    public static final String FIND_HIGH_VALUE_CLAIMS =
            "SELECT c FROM Claim c WHERE c.claimAmount > :amount";

    // 🔍 Fetch claims by status
    public static final String FIND_CLAIMS_BY_STATUS =
            "SELECT c FROM Claim c WHERE c.status = :status";

    // 🔍 Fetch fraud results for a claim
    public static final String FIND_FRAUD_RESULT_BY_CLAIM_ID =
            "SELECT f FROM FraudCheckResult f WHERE f.claim.id = :claimId";

    // 🔍 Fetch policies for a user
    public static final String FIND_POLICIES_BY_USER_ID =
            "SELECT p FROM Policy p WHERE p.user.id = :userId";
}
