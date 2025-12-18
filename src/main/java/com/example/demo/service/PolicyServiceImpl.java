@Service
public class PolicyServiceImpl implements PolicyService {

    private final PolicyRepository policyRepository;
    private final UserRepository userRepository;

    public PolicyServiceImpl(PolicyRepository policyRepository,
                             UserRepository userRepository) {
        this.policyRepository = policyRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Policy createPolicy(Policy policy) {

        if (policy.getEndDate().isBefore(policy.getStartDate())
            || policy.getEndDate().isEqual(policy.getStartDate())) {
            throw new IllegalArgumentException("Invalid policy dates");
        }

        Long userId = policy.getUser().getId();

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        policy.setUser(user);

        return policyRepository.save(policy);
    }
}
