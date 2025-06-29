package miniproject.domain;

import java.util.Optional;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(
    collectionResourceRel = "bestSellers",
    path = "bestSellers"
)
public interface BestSellerRepository extends PagingAndSortingRepository<BestSeller, Long> {

    // 🔥 이 메서드 꼭 추가해줘야 컴파일 성공함
    Optional<BestSeller> findByBookId(Long bookId);
}
