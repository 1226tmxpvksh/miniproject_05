package miniproject.domain;

import lombok.*;
import miniproject.infra.AbstractEvent;

@Data
@ToString
public class BookViewIncreased extends AbstractEvent {

    private Long bookId;
    private Integer viewCount;

    public BookViewIncreased(BestSeller aggregate) {
        super(aggregate);
        this.bookId = aggregate.getBookId();       // Long 타입
        this.viewCount = aggregate.getViewCount();
    }

    public BookViewIncreased() {
        super();
    }
}
