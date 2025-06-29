package miniproject.domain;

import lombok.*;
import javax.persistence.*;
import java.util.Date;
import miniproject.BestsellerApplication;

@Entity
@Table(name = "BestSeller_table")
@Data
public class BestSeller {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long bestsellerId;

    private Long bookId;
    private Integer viewCount = 0;
    private String selectedStatus = "일반도서";
    private Date selectedAt;

    public static BestSellerRepository repository() {
        return BestsellerApplication.applicationContext.getBean(BestSellerRepository.class);
    }

    public void increaseBookView(IncreaseBookViewCommand cmd) {
        this.viewCount += 1;

        BookViewIncreased bookViewIncreased = new BookViewIncreased(this);
        bookViewIncreased.publishAfterCommit();

        if (this.viewCount >= 100 && !"베스트셀러".equals(this.selectedStatus)) {
            this.selectBestSeller(new SelectBestSellerCommand(this.bookId));
        }
    }

    public void selectBestSeller(SelectBestSellerCommand cmd) {
        this.selectedStatus = "베스트셀러";
        this.selectedAt = new Date();

        BestsellerSelected event = new BestsellerSelected(this);
        event.publishAfterCommit();
    }

    // 📘 이벤트 1: BookAccessGranted 발생 시 호출
    public static void viewCount(BookAccessGranted event) {
        repository().findByBookId(event.getBookId()).ifPresentOrElse(bestSeller -> {
            bestSeller.increaseBookView(new IncreaseBookViewCommand(event.getBookId()));
            repository().save(bestSeller);
        }, () -> {
            BestSeller newBook = new BestSeller();
            newBook.setBookId(event.getBookId());
            newBook.setViewCount(1);
            repository().save(newBook);

            BookViewIncreased event1 = new BookViewIncreased(newBook);
            event1.publishAfterCommit();
        });
    }

    // 📘 이벤트 2: PointDeducted 발생 시 (현재는 아무 로직 없음)
    public static void viewCount(PointDeducted event) {
        // no-op
    }
}
