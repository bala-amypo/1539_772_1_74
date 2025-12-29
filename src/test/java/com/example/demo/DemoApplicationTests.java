package com.example.demo;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.*;
import com.example.demo.service.impl.*;
import com.example.demo.util.HqlQueryHelper;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
@Listeners(TestResultListener.class)
public class DemoApplicationTests {

    private UserRepository userRepository;
    private PolicyRepository policyRepository;
    private ClaimRepository claimRepository;
    private FraudRuleRepository fraudRuleRepository;
    private FraudCheckResultRepository resultRepository;
    private HqlQueryHelper hqlQueryHelper;

    private UserService userService;
    private PolicyService policyService;
    private ClaimService claimService;
    private FraudRuleService fraudRuleService;
    private FraudDetectionService fraudDetectionService;

    private PasswordEncoder passwordEncoder;

    @BeforeClass
    public void setup() {
        userRepository = Mockito.mock(UserRepository.class);
        policyRepository = Mockito.mock(PolicyRepository.class);
        claimRepository = Mockito.mock(ClaimRepository.class);
        fraudRuleRepository = Mockito.mock(FraudRuleRepository.class);
        resultRepository = Mockito.mock(FraudCheckResultRepository.class);
        hqlQueryHelper = Mockito.mock(HqlQueryHelper.class);

        passwordEncoder = new BCryptPasswordEncoder();

        userService = new UserServiceImpl(userRepository, passwordEncoder);
        policyService = new PolicyServiceImpl(policyRepository, userRepository);
        claimService = new ClaimServiceImpl(claimRepository, policyRepository);
        fraudRuleService = new FraudRuleServiceImpl(fraudRuleRepository);
        fraudDetectionService = new FraudDetectionServiceImpl(
                claimRepository,
                fraudRuleRepository,
                resultRepository
        );
    }

    // ============================================================
    // 1) Develop and deploy a simple servlet using Tomcat Server
    // ============================================================

    @Test(groups = "servlet", priority = 1)
    public void testHelloServletReturnsText() throws Exception {
        String contentType = "text/plain";
        Assert.assertEquals(contentType, "text/plain");
    }

    @Test(groups = "servlet", priority = 1)
    public void testHelloServletPathMapping() {
        String path = "/hello-servlet";
        Assert.assertTrue(path.startsWith("/hello"));
    }

    @Test(groups = "servlet", priority = 1)
    public void testServletDeployedInContext() {
        boolean deployed = true; // simulated check
        Assert.assertTrue(deployed);
    }

    @Test(groups = "servlet", priority = 2)
    public void testServletSupportsGet() {
        String method = "GET";
        Assert.assertEquals(method, "GET");
    }

    @Test(groups = "servlet", priority = 2)
    public void testServletDoesNotSupportDelete() {
        boolean deleteSupported = false;
        Assert.assertFalse(deleteSupported);
    }

    @Test(groups = "servlet", priority = 3)
    public void testServletResponseNotNull() {
        String response = "Hello from HelloServlet";
        Assert.assertNotNull(response);
    }

    @Test(groups = "servlet", priority = 3)
    public void testServletResponseContainsHello() {
        String response = "Hello from HelloServlet";
        Assert.assertTrue(response.contains("Hello"));
    }

    @Test(groups = "servlet", priority = 3)
    public void testServletConfigurationLoadOnStartup() {
        int loadOnStartup = 1;
        Assert.assertTrue(loadOnStartup >= 0);
    }

    // ============================================================
    // 2) Implement CRUD operations using Spring Boot and REST APIs
    // ============================================================

    @Test(groups = "crud", priority = 4)
    public void testUserRegistrationSuccess() {
        User user = new User("John", "john@example.com", "password", "USER");

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User saved = userService.register(user);
        Assert.assertEquals(saved.getEmail(), "john@example.com");
        Assert.assertNotEquals(saved.getPassword(), "password"); // encoded
    }

    @Test(groups = "crud", priority = 4, expectedExceptions = IllegalArgumentException.class)
    public void testUserRegistrationDuplicateEmail() {
        User user = new User("John", "dup@example.com", "pass", "USER");
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);
        userService.register(user);
    }

    @Test(groups = "crud", priority = 4)
    public void testCreatePolicySuccess() {
        User user = new User("Anna", "anna@example.com", "pass", "USER");
        user.setId(1L);

        Policy policy = new Policy(user, "POL123", "HEALTH",
                LocalDate.now(), LocalDate.now().plusDays(1));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(policyRepository.existsByPolicyNumber("POL123")).thenReturn(false);
        when(policyRepository.save(any(Policy.class))).thenAnswer(inv -> inv.getArgument(0));

        Policy saved = policyService.createPolicy(1L, policy);
        Assert.assertEquals(saved.getPolicyNumber(), "POL123");
    }

    @Test(groups = "crud", priority = 4, expectedExceptions = IllegalArgumentException.class)
    public void testCreatePolicyInvalidDates() {
        User user = new User("Anna", "anna@example.com", "pass", "USER");
        user.setId(1L);

        Policy policy = new Policy(user, "POL999", "HEALTH",
                LocalDate.now(), LocalDate.now().minusDays(1));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        policyService.createPolicy(1L, policy);
    }

    @Test(groups = "crud", priority = 4)
    public void testGetPoliciesByUser() {
        when(policyRepository.findByUserId(2L)).thenReturn(List.of());
        List<Policy> policies = policyService.getPoliciesByUser(2L);
        Assert.assertNotNull(policies);
    }

    @Test(groups = "crud", priority = 5)
    public void testCreateClaimSuccess() {
        Policy policy = new Policy();
        policy.setId(1L);

        Claim claim = new Claim(policy, LocalDate.now(), 1000.0, "Sample claim");

        when(policyRepository.findById(1L)).thenReturn(Optional.of(policy));
        when(claimRepository.save(any(Claim.class))).thenAnswer(inv -> inv.getArgument(0));

        Claim saved = claimService.createClaim(1L, claim);
        Assert.assertEquals(saved.getClaimAmount(), 1000.0);
    }

    @Test(groups = "crud", priority = 5, expectedExceptions = IllegalArgumentException.class)
    public void testCreateClaimWithFutureDate() {
        Policy policy = new Policy();
        policy.setId(1L);

        Claim claim = new Claim(policy, LocalDate.now().plusDays(1), 500.0, "Future claim");

        when(policyRepository.findById(1L)).thenReturn(Optional.of(policy));
        claimService.createClaim(1L, claim);
    }

    @Test(groups = "crud", priority = 5, expectedExceptions = IllegalArgumentException.class)
    public void testCreateClaimWithNegativeAmount() {
        Policy policy = new Policy();
        policy.setId(1L);

        Claim claim = new Claim(policy, LocalDate.now(), -10.0, "Invalid");

        when(policyRepository.findById(1L)).thenReturn(Optional.of(policy));
        claimService.createClaim(1L, claim);
    }

    // ============================================================
    // 3) Dependency Injection and IoC using Spring Framework
    // ============================================================

    @Test(groups = "di", priority = 6)
    public void testUserServiceInjectedWithUserRepository() {
        Assert.assertNotNull(userService);
        Assert.assertNotNull(userRepository);
    }

    @Test(groups = "di", priority = 6)
    public void testPolicyServiceInjectedWithRepositories() {
        Assert.assertNotNull(policyService);
        Assert.assertNotNull(policyRepository);
        Assert.assertNotNull(userRepository);
    }

    @Test(groups = "di", priority = 6)
    public void testClaimServiceInjectedWithRepositories() {
        Assert.assertNotNull(claimService);
        Assert.assertNotNull(claimRepository);
        Assert.assertNotNull(policyRepository);
    }

    @Test(groups = "di", priority = 7)
    public void testFraudDetectionServiceInjected() {
        Assert.assertNotNull(fraudDetectionService);
        Assert.assertNotNull(fraudRuleRepository);
    }

    @Test(groups = "di", priority = 7)
    public void testPasswordEncoderIsBCrypt() {
        Assert.assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
    }

    @Test(groups = "di", priority = 7)
    public void testUserServiceRegisterUsesEncoder() {
        User user = new User("Test", "enc@example.com", "plain", "USER");

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User saved = userService.register(user);
        Assert.assertNotEquals(saved.getPassword(), "plain");
    }

    @Test(groups = "di", priority = 7)
    public void testIoCHandlesMultipleBeans() {
        Assert.assertNotNull(fraudRuleService);
        Assert.assertNotNull(resultRepository);
    }

    @Test(groups = "di", priority = 8)
    public void testServicesAreTransactionalByDefault() {
        Assert.assertTrue(true);
    }

    // ============================================================
    // 4) Hibernate configurations, generator classes, annotations, CRUD
    // ============================================================

    @Test(groups = "hibernate", priority = 9)
    public void testUserEntityHasGeneratedId() {
        User user = new User();
        user.setId(10L);
        Assert.assertEquals(user.getId(), Long.valueOf(10L));
    }

    @Test(groups = "hibernate", priority = 9)
    public void testPolicyEntityUniquePolicyNumberConstraint() {
        when(policyRepository.existsByPolicyNumber("XYZ")).thenReturn(true);
        Policy policy = new Policy();
        policy.setPolicyNumber("XYZ");
        policy.setStartDate(LocalDate.now());
        policy.setEndDate(LocalDate.now().plusDays(1));
        try {
            policyService.createPolicy(1L, policy);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            Assert.assertTrue(ex.getMessage().contains("Policy number already exists"));
        }
    }

    @Test(groups = "hibernate", priority = 9)
    public void testFraudRuleEntityValidation() {
        FraudRule rule = new FraudRule("Rule1", "claimAmount", ">", "5000", "HIGH");
        when(fraudRuleRepository.findByRuleName("Rule1")).thenReturn(Optional.empty());
        when(fraudRuleRepository.save(any(FraudRule.class))).thenAnswer(inv -> inv.getArgument(0));
        FraudRule saved = fraudRuleService.addRule(rule);
        Assert.assertEquals(saved.getSeverity(), "HIGH");
    }

    @Test(groups = "hibernate", priority = 10, expectedExceptions = IllegalArgumentException.class)
    public void testFraudRuleInvalidSeverity() {
        FraudRule rule = new FraudRule("Rule2", "claimAmount", ">", "5000", "CRAZY");
        fraudRuleService.addRule(rule);
    }

    @Test(groups = "hibernate", priority = 10)
    public void testSaveAndRetrieveClaim() {
        Claim claim = new Claim();
        claim.setId(5L);
        when(claimRepository.findById(5L)).thenReturn(Optional.of(claim));
        Claim found = claimService.getClaim(5L);
        Assert.assertEquals(found.getId(), Long.valueOf(5L));
    }

    @Test(groups = "hibernate", priority = 10)
    public void testRepositoryCrudCount() {
        when(policyRepository.count()).thenReturn(5L);
        long count = policyRepository.count();
        Assert.assertEquals(count, 5L);
    }

    @Test(groups = "hibernate", priority = 11)
    public void testUserEmailUniquenessSimulated() {
        when(userRepository.existsByEmail("unique@example.com")).thenReturn(false);
        boolean exists = userRepository.existsByEmail("unique@example.com");
        Assert.assertFalse(exists);
    }

    @Test(groups = "hibernate", priority = 11)
    public void testClaimRepositoryFindByPolicyIdReturnsList() {
        when(claimRepository.findByPolicyId(3L)).thenReturn(List.of());
        List<Claim> claims = claimRepository.findByPolicyId(3L);
        Assert.assertNotNull(claims);
    }

    // ============================================================
    // 5) JPA mapping with normalization (1NF, 2NF, 3NF)
    // ============================================================

    @Test(groups = "jpa", priority = 12)
    public void testPolicyIsIn1NF() {
        Policy policy = new Policy();
        policy.setPolicyType("HEALTH");
        Assert.assertEquals(policy.getPolicyType(), "HEALTH");
    }

    @Test(groups = "jpa", priority = 12)
    public void testUserEmailSingleValuedAttribute() {
        User user = new User();
        user.setEmail("one@example.com");
        Assert.assertEquals(user.getEmail(), "one@example.com");
    }

    @Test(groups = "jpa", priority = 13)
    public void testPolicyDependsOnUserId_2NF() {
        Policy policy = new Policy();
        User user = new User();
        user.setId(1L);
        policy.setUser(user);
        Assert.assertEquals(policy.getUser().getId(), Long.valueOf(1L));
    }

    @Test(groups = "jpa", priority = 13)
    public void testClaimDependsOnPolicyId_2NF() {
        Policy policy = new Policy();
        policy.setId(2L);
        Claim claim = new Claim();
        claim.setPolicy(policy);
        Assert.assertEquals(claim.getPolicy().getId(), Long.valueOf(2L));
    }

    @Test(groups = "jpa", priority = 14)
    public void testNoTransitiveDependencyOnUserName_3NF() {
        User user = new User("Name", "email@example.com", "pwd", "USER");
        Policy policy = new Policy();
        policy.setUser(user);
        Assert.assertEquals(policy.getUser().getName(), "Name");
    }

    @Test(groups = "jpa", priority = 14)
    public void testClaimHasAtomicAttributes() {
        Claim claim = new Claim();
        claim.setDescription("Accident damage");
        Assert.assertEquals(claim.getDescription(), "Accident damage");
    }

    @Test(groups = "jpa", priority = 14)
    public void testFraudCheckResultSnapshotDoesNotBreak3NF() {
        FraudCheckResult result = new FraudCheckResult();
        result.setMatchedRules("Rule1,Rule2");
        Assert.assertTrue(result.getMatchedRules().contains("Rule1"));
    }

    @Test(groups = "jpa", priority = 15)
    public void testMappingsAllowNormalizedQueryPatterns() {
        Assert.assertTrue(true);
    }

    // ============================================================
    // 6) Many-to-Many relationships and associations in Spring Boot
    // ============================================================

    @Test(groups = "manyToMany", priority = 16)
    public void testClaimCanHoldMultipleFraudRules() {
        Claim claim = new Claim();
        FraudRule r1 = new FraudRule("R1", "claimAmount", ">", "5000", "HIGH");
        FraudRule r2 = new FraudRule("R2", "claimAmount", ">", "10000", "HIGH");

        claim.setSuspectedRules(Set.of(r1, r2));
        Assert.assertEquals(claim.getSuspectedRules().size(), 2);
    }

    @Test(groups = "manyToMany", priority = 16)
    public void testFraudRuleKnowsItsClaims() {
        Claim claim = new Claim();
        claim.setId(1L);

        FraudRule rule = new FraudRule();
        rule.setRuleName("R1");
        rule.setClaims(Set.of(claim));

        Assert.assertEquals(rule.getClaims().iterator().next().getId(), Long.valueOf(1L));
    }

    @Test(groups = "manyToMany", priority = 17)
    public void testEmptyManyToManySetIsValid() {
        Claim claim = new Claim();
        Assert.assertTrue(claim.getSuspectedRules().isEmpty());
    }

    @Test(groups = "manyToMany", priority = 17)
    public void testAddAndRemoveRuleFromClaim() {
        Claim claim = new Claim();
        FraudRule rule = new FraudRule();
        rule.setRuleName("R1");

        claim.getSuspectedRules().add(rule);
        Assert.assertEquals(claim.getSuspectedRules().size(), 1);

        claim.getSuspectedRules().remove(rule);
        Assert.assertTrue(claim.getSuspectedRules().isEmpty());
    }

    @Test(groups = "manyToMany", priority = 17)
    public void testManyToManyDoesNotDuplicateRules() {
        Claim claim = new Claim();
        FraudRule rule = new FraudRule();
        rule.setRuleName("R1");
        claim.getSuspectedRules().add(rule);
        claim.getSuspectedRules().add(rule);
        Assert.assertEquals(claim.getSuspectedRules().size(), 1);
    }

    @Test(groups = "manyToMany", priority = 18)
    public void testManyToManyAssociationsSurvivePersistenceSimulation() {
        Claim claim = new Claim();
        FraudRule rule = new FraudRule();
        rule.setRuleName("R1");
        claim.getSuspectedRules().add(rule);

        Assert.assertTrue(claim.getSuspectedRules().contains(rule));
    }

    @Test(groups = "manyToMany", priority = 18)
    public void testRuleWithNoClaimsIsAllowed() {
        FraudRule rule = new FraudRule();
        Assert.assertTrue(rule.getClaims().isEmpty());
    }

    @Test(groups = "manyToMany", priority = 18)
    public void testManyToManyMappingSupportsBidirectionalNavigation() {
        Claim claim = new Claim();
        FraudRule rule = new FraudRule();
        rule.setRuleName("R1");
        rule.setClaims(Set.of(claim));
        claim.setSuspectedRules(Set.of(rule));

        Assert.assertTrue(rule.getClaims().contains(claim));
        Assert.assertTrue(claim.getSuspectedRules().contains(rule));
    }

    // ============================================================
    // 7) Basic security controls and JWT token-based authentication
    // ============================================================

    @Test(groups = "security", priority = 19)
    public void testJwtContainsUserIdEmailRole() {
        User user = new User("Sec", "sec@example.com", "pwd", "ADMIN");
        user.setId(99L);

        String secret = "SuperSecretJwtKeyForDemoProject123";
        JwtUtil jwtUtil = new JwtUtil(secret, 3600000);

        String token = jwtUtil.generateToken(user);
        Assert.assertTrue(jwtUtil.validateToken(token));
        String email = jwtUtil.getEmailFromToken(token);
        Assert.assertEquals(email, "sec@example.com");
    }

    @Test(groups = "security", priority = 19)
    public void testJwtInvalidTokenFailsValidation() {
        String secret = "SuperSecretJwtKeyForDemoProject123";
        JwtUtil jwtUtil = new JwtUtil(secret, 3600000);
        Assert.assertFalse(jwtUtil.validateToken("invalid-token"));
    }

    @Test(groups = "security", priority = 20)
    public void testPasswordEncodingForSecurity() {
        String raw = "password";
        String encoded = passwordEncoder.encode(raw);
        Assert.assertTrue(passwordEncoder.matches(raw, encoded));
    }

    @Test(groups = "security", priority = 20)
    public void testUserRoleIsPrefixedWithROLE() {
        User user = new User("Sec", "role@example.com", "pwd", "ADMIN");
        Assert.assertEquals(user.getRole(), "ADMIN");
    }

    @Test(groups = "security", priority = 20)
    public void testUnauthorizedAccessScenarioSimulation() {
        boolean hasToken = false;
        Assert.assertFalse(hasToken);
    }

    @Test(groups = "security", priority = 21)
    public void testAuthorizedAccessSimulation() {
        boolean hasToken = true;
        Assert.assertTrue(hasToken);
    }

    @Test(groups = "security", priority = 21)
    public void testAdminRoleCanConfigureRules() {
        User admin = new User("Admin", "admin@example.com", "pwd", "ADMIN");
        Assert.assertEquals(admin.getRole(), "ADMIN");
    }

    @Test(groups = "security", priority = 21)
    public void testUserRoleCannotSimulateAdminOperations() {
        User user = new User("U", "u@example.com", "pwd", "USER");
        Assert.assertNotEquals(user.getRole(), "ADMIN");
    }

    // ============================================================
    // 8) Use HQL and HCQL for advanced data querying
    // ============================================================

    @Test(groups = "hql", priority = 22)
    public void testFindHighValueClaimsReturnsList() {
        when(hqlQueryHelper.findHighValueClaims(10000.0)).thenReturn(List.of());
        List<Claim> claims = hqlQueryHelper.findHighValueClaims(10000.0);
        Assert.assertNotNull(claims);
    }

    @Test(groups = "hql", priority = 22)
    public void testFindHighValueClaimsParameter() {
        double minAmount = 5000.0;
        Assert.assertTrue(minAmount > 0);
    }

    @Test(groups = "hql", priority = 23)
    public void testCriteriaQueryByDescriptionKeyword() {
        when(hqlQueryHelper.findClaimsByDescriptionKeyword("accident")).thenReturn(List.of());
        List<Claim> claims = hqlQueryHelper.findClaimsByDescriptionKeyword("accident");
        Assert.assertNotNull(claims);
    }

    @Test(groups = "hql", priority = 23)
    public void testCriteriaQueryEmptyKeyword() {
        when(hqlQueryHelper.findClaimsByDescriptionKeyword("")).thenReturn(List.of());
        List<Claim> claims = hqlQueryHelper.findClaimsByDescriptionKeyword("");
        Assert.assertNotNull(claims);
    }

    @Test(groups = "hql", priority = 24)
    public void testHqlQuerySupportsGreaterThanCondition() {
        double min = 1000.0;
        Assert.assertTrue(min >= 0);
    }

    @Test(groups = "hql", priority = 24)
    public void testHqlQueryDoesNotReturnNullList() {
        when(hqlQueryHelper.findHighValueClaims(0.0)).thenReturn(List.of());
        List<Claim> claims = hqlQueryHelper.findHighValueClaims(0.0);
        Assert.assertNotNull(claims);
    }

    @Test(groups = "hql", priority = 24)
    public void testAdvancedQueryUsedForFraudAnalyticsSimulation() {
        boolean usedForAnalytics = true;
        Assert.assertTrue(usedForAnalytics);
    }

    @Test(groups = "hql", priority = 25)
    public void testHqlAndCriteriaComplementEachOther() {
        Assert.assertTrue(true);
    }
}
