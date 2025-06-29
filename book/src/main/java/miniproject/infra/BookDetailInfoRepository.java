package miniproject.infra;

import java.util.List;
import miniproject.domain.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(
    collectionResourceRel = "bookDetailInfos",
    path = "bookDetailInfos"
)
public interface BookDetailInfoRepository
    extends PagingAndSortingRepository<BookDetailInfo, Long> {

    // ✅ 작가 ID 기준으로 도서 목록 조회 (닉네임 변경에 사용)
    List<BookDetailInfo> findByWriterId(Long writerId);
}
