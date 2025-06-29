package miniproject.domain;

import java.util.Optional;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(
    collectionResourceRel = "bestSellers",
    path = "bestSellers"
)
public interface BestSellerRepository extends PagingAndSortingRepository<BestSeller, Long> {

    // ✅ 도서 ID 기준 조회 메서드 추가
    Optional<BestSeller> findByBookId(Long bookId);
}
