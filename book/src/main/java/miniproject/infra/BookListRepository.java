package miniproject.infra;

import java.util.List;
import java.util.Optional;
import miniproject.domain.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "bookLists", path = "bookLists")
public interface BookListRepository
    extends PagingAndSortingRepository<BookList, Long> {

    // ✅ 작가 ID로 도서 목록 조회
    List<BookList> findByWriterId(Long writerId);

    // ✅ 도서 ID로 단일 도서 조회 (조회수 증가 시 필요)
    Optional<BookList> findByBookId(Long bookId);
}
