package miniproject.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import javax.persistence.*;
import lombok.Data;
import miniproject.BookApplication;

@Entity
@Table(name = "Book_table")
@Data
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long bookId;

    private String title;
    private String content;
    private String writerNickname;
    private Long writerId;
    private String coverUrl;
    private String status;

    public static BookRepository repository() {
        return BookApplication.applicationContext.getBean(BookRepository.class);
    }

    // 도서 작성
    public void write(WriteCommand writeCommand) {
        this.status = "작성됨";
        Written written = new Written(this);
        written.publishAfterCommit();
    }

    // 도서 삭제
    public void delete(DeleteCommand deleteCommand) {
        this.status = "삭제됨";
        Deleted deleted = new Deleted(this);
        deleted.publishAfterCommit();
    }

    // ✅ 출간 요청
    public void publishRequest(PublishRequestCommand publishRequestCommand) {
        this.status = "출간요청됨";
        PublishRequested publishRequested = new PublishRequested(this);
        publishRequested.publishAfterCommit();
    }

    // 도서 열람
    public void viewBook(ViewBookCommand viewBookCommand) {
        BookViewed bookViewed = new BookViewed(this);
        bookViewed.publishAfterCommit();
    }

    // 표지 선택
    public void selectBookCover(SelectBookCoverCommand selectBookCoverCommand) {
        BookCoverSelected bookCoverSelected = new BookCoverSelected(this);
        bookCoverSelected.publishAfterCommit();
    }

    // 표지 생성 요청
    public void requestCoverGeneration(RequestCoverGenerationCommand requestCoverGenerationCommand) {
        CoverGenerationRequested coverGenerationRequested = new CoverGenerationRequested(this);
        coverGenerationRequested.publishAfterCommit();
    }

    // 도서 수정
    public void update(UpdateCommand updateCommand) {
        Updated updated = new Updated(this);
        updated.publishAfterCommit();
    }

    // ✅ 표지 생성 완료 → 출간 완료 처리
    public static void coverCandidatesReady(CoverCreated coverCreated) {
        repository().findById(coverCreated.getBookId()).ifPresent(book -> {
            book.setCoverUrl(coverCreated.getCoverUrl());
            book.setStatus("출간완료");

            repository().save(book);

            PublishCompleted event = new PublishCompleted(book);
            event.publishAfterCommit();
        });
    }
}
