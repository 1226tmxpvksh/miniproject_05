package miniproject.domain;

import lombok.*;
import miniproject.domain.Book;
import miniproject.infra.AbstractEvent;

@Data
@ToString
@NoArgsConstructor
public class BookViewIncreased extends AbstractEvent {

    private Long bookId;
    private Integer viewCount;

    public BookViewIncreased(Book aggregate) {
        super(aggregate);
        this.bookId = aggregate.getBookId();
        // viewCount는 외부에서 직접 set 필요
    }
} 