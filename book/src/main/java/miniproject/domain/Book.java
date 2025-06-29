package miniproject.domain;

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

    private Integer viewCount = 0; // ✅ 조회수 필드

    public static BookRepository repository() {
        return BookApplication.applicationContext.getBean(BookRepository.class);
    }

    public void write(WriteCommand writeCommand) {
        this.status = "작성됨";
        Written written = new Written(this);
        written.publishAfterCommit();
    }

    public void delete(DeleteCommand deleteCommand) {
        this.status = "삭제됨";
        Deleted deleted = new Deleted(this);
        deleted.publishAfterCommit();
    }

    public void publishRequest(PublishRequestCommand publishRequestCommand) {
        this.status = "출간요청됨";
        PublishRequested publishRequested = new PublishRequested(this);
        publishRequested.publishAfterCommit();
    }

    public void viewBook(ViewBookCommand viewBookCommand) {
        this.viewCount += 1;
        BookViewed bookViewed = new BookViewed(this); // 이벤트 발행
        bookViewed.publishAfterCommit();
    }

    public void selectBookCover(SelectBookCoverCommand selectBookCoverCommand) {
        BookCoverSelected bookCoverSelected = new BookCoverSelected(this);
        bookCoverSelected.publishAfterCommit();
    }

    public void requestCoverGeneration(RequestCoverGenerationCommand requestCoverGenerationCommand) {
        CoverGenerationRequested coverGenerationRequested = new CoverGenerationRequested(this);
        coverGenerationRequested.publishAfterCommit();
    }

    public void update(UpdateCommand updateCommand) {
        Updated updated = new Updated(this);
        updated.publishAfterCommit();
    }

    public static void publishComplete(PubApproved event) {
        repository().findById(event.getBookId()).ifPresent(book -> {
            book.setStatus("출간완료");
            repository().save(book);

            PublishCompleted publishCompleted = new PublishCompleted(book);
            publishCompleted.publishAfterCommit();
        });
    }

    public static void coverCandidatesReady(CoverCreated event) {
        repository().findById(event.getBookId()).ifPresent(book -> {
            book.setCoverUrl(event.getCoverUrl());
            book.setStatus("출간완료");
            repository().save(book);

            PublishCompleted publishCompleted = new PublishCompleted(book);
            publishCompleted.publishAfterCommit();
        });
    }
}
