package miniproject.domain;

import javax.persistence.*;
import lombok.Data;
import miniproject.BookApplication;
import miniproject.infra.BookListRepository;

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

    @Column(length = 1024)
    private String coverUrl;
    private String status;

    private Integer viewCount = 0; // ✅ 조회수 필드

    public static BookRepository repository() {
        return BookApplication.applicationContext.getBean(BookRepository.class);
    }

    @PostPersist
    public void onPostPersist() {
        // DB에 저장된 직후, bookId가 생성된 상태에서 이 메서드가 호출됩니다.
        Written written = new Written(this);
        written.publish(); // Transaction과 무관하게 즉시 발행
    }

    // public void write(WriteCommand writeCommand) {
    //     this.status = "작성됨";
    //     Written written = new Written(this);
    //     written.publishAfterCommit();
    // }

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
        // BookList에 등록된 도서(공개 도서)만 열람/포인트 차감 허용
        BookListRepository bookListRepository = BookApplication.applicationContext.getBean(BookListRepository.class);
        System.out.println("BookListRepository=" + bookListRepository);
        System.out.println("bookId=" + this.bookId);
        System.out.println("BookList exists=" + bookListRepository.findByBookId(this.bookId).isPresent());
        if (!bookListRepository.findByBookId(this.bookId).isPresent()) {
            throw new IllegalStateException("출간 승인(공개)된 도서만 열람 가능합니다.");
        }
        // 1. 구독권한확인 커맨드 발행 (이벤트/커맨드 객체 생성)
        SubscriptionCheckRequested subscriptionCheckRequested = new SubscriptionCheckRequested();
        subscriptionCheckRequested.setUserId(viewBookCommand.getUserId()); // 열람자 id
        subscriptionCheckRequested.setBookId(this.bookId);
        subscriptionCheckRequested.publishAfterCommit();
        // 실제 열람/포인트 차감은 이벤트 핸들러에서 처리
    }

    // 2. 구독권한확인됨 이벤트 핸들러
    public static void onSubscriptionChecked(SubscriptionChecked event) {
        // 구독자 여부에 따라 분기
        if (event.isSubscribed()) {
            // 구독자면 바로 열람 허용(조회수 증가, BookViewed 이벤트 발행)
            repository().findById(event.getBookId()).ifPresent(book -> {
                book.viewCount += 1;
                BookViewed bookViewed = new BookViewed(book);
                bookViewed.publishAfterCommit();
                repository().save(book);
            });
        } else {
            // 비구독자면 포인트차감 커맨드 발행
            PointDeductRequested pointDeductRequested = new PointDeductRequested();
            pointDeductRequested.setUserId(event.getUserId());
            pointDeductRequested.setAmount(100); // 차감할 포인트
            pointDeductRequested.setBookId(event.getBookId());
            pointDeductRequested.publishAfterCommit();
        }
    }

    // 3. 차감성공됨 이벤트 핸들러
    public static void onPointDeducted(PointDeducted event) {
        // 차감 성공 시 열람 허용(조회수 증가, BookViewed 이벤트 발행)
        repository().findById(event.getBookId()).ifPresent(book -> {
            book.viewCount += 1;
            BookViewed bookViewed = new BookViewed(book);
            bookViewed.publishAfterCommit();
            repository().save(book);
        });
    }

    public void selectBookCover(SelectBookCoverCommand selectBookCoverCommand) {
        BookCoverSelected bookCoverSelected = new BookCoverSelected(this);
        bookCoverSelected.publishAfterCommit();
    }

    public void requestCoverGeneration(RequestCoverGenerationCommand requestCoverGenerationCommand) {
        CoverGenerationRequested coverGenerationRequested = new CoverGenerationRequested(this);
        
        // 이벤트에 필요한 데이터를 명시적으로 설정합니다.
        coverGenerationRequested.setBookId(this.getBookId());
        coverGenerationRequested.setTitle(this.getTitle());
        coverGenerationRequested.setContent(this.getContent());
        
        coverGenerationRequested.publishAfterCommit();
    }

    public void update(UpdateCommand updateCommand) {
        Updated updated = new Updated(this);
        updated.publishAfterCommit();
    }

    public static BookListRepository bookListRepository() {
        return BookApplication.applicationContext.getBean(BookListRepository.class);
    }

    public static void publishComplete(PubApproved event) {
        repository().findById(event.getBookId()).ifPresent(book -> {
            book.setStatus("출간완료");
            repository().save(book);

            // BookList에 출간 승인 도서 추가
            BookList bookList = new BookList();
            bookList.setBookId(book.getBookId());
            bookList.setTitle(book.getTitle());
            bookList.setCoverUrl(book.getCoverUrl());
            bookList.setViewCount(book.getViewCount());
            bookList.setWriterId(book.getWriterId());
            bookList.setWriterNickname(book.getWriterNickname());
            bookListRepository().save(bookList);

            PublishCompleted publishCompleted = new PublishCompleted(book);
            publishCompleted.publishAfterCommit();
        });
    }

    public static void coverCandidatesReady(CoverCreated event) {
        repository().findById(event.getBookId()).ifPresent(book -> {
            book.setCoverUrl(event.getCoverUrl());
            book.setStatus("출간완료");
            repository().save(book);

            // BookList에 출간완료 도서 추가
            BookList bookList = new BookList();
            bookList.setBookId(book.getBookId());
            bookList.setTitle(book.getTitle());
            bookList.setCoverUrl(book.getCoverUrl());
            bookList.setViewCount(book.getViewCount());
            bookList.setWriterId(book.getWriterId());
            bookList.setWriterNickname(book.getWriterNickname());
            bookListRepository().save(bookList);

            PublishCompleted publishCompleted = new PublishCompleted(book);
            publishCompleted.publishAfterCommit();
        });
    }
}
