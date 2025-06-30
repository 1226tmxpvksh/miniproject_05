package miniproject.infra;

import java.util.Optional;
import miniproject.domain.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "subscriptionStatusChecks", path = "subscriptionStatusChecks")
public interface SubscriptionStatusCheckRepository
    extends PagingAndSortingRepository<SubscriptionStatusCheck, Long> {

    // findByUserId 메서드 선언 추가
    Optional<SubscriptionStatusCheck> findByUserId(Long userId);
}