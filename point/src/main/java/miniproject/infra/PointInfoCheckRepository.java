package miniproject.infra;

import java.util.Optional;
import miniproject.domain.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(
    collectionResourceRel = "pointInfoChecks",
    path = "pointInfoChecks"
)
public interface PointInfoCheckRepository
    extends PagingAndSortingRepository<PointInfoCheck, Long> {

    // ✅ 사용자 ID 기준 조회
    Optional<PointInfoCheck> findByUserId(Long userId);
}
