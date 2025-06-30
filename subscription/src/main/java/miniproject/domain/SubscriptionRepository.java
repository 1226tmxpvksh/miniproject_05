package miniproject.domain;

import java.util.Optional; // <-- import 추가
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "subscriptions", path = "subscriptions")
public interface SubscriptionRepository
    extends PagingAndSortingRepository<Subscription, Long> {
    
    // 이 메서드를 추가해야 합니다.
    Optional<Subscription> findByUserId(Long userId);

}