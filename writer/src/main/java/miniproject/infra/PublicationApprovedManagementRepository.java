package miniproject.infra;

import java.util.Optional;
import miniproject.domain.PublicationApprovedManagement;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(
    collectionResourceRel = "publicationApprovedManagements",
    path = "publicationApprovedManagements"
)
public interface PublicationApprovedManagementRepository
    extends PagingAndSortingRepository<PublicationApprovedManagement, Long> {

    Optional<PublicationApprovedManagement> findByBookId(Long bookId);

}
