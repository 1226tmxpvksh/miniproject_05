package miniproject.infra;

import java.util.Optional;
import miniproject.domain.SubscriptionStatusCheck;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(
    collectionResourceRel = "subscriptionStatusChecks",
    path = "subscriptionStatusChecks"
)
public interface SubscriptionStatusCheckRepository
        extends PagingAndSortingRepository<SubscriptionStatusCheck, Long> {

    //구독 현황(리드 모델) userId로 조회
    Optional<SubscriptionStatusCheck> findByUserId(Long userId);
}
