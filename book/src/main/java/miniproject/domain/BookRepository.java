package miniproject.domain;

import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "books", path = "books")
public interface BookRepository extends PagingAndSortingRepository<Book, Long> {

    // ✅ 출간 완료된 책만 조회
    List<Book> findByStatus(String status);

    // ✅ 특정 작가의 책 리스트 조회
    List<Book> findByWriterId(Long writerId);
}
