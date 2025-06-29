package miniproject.domain;

import lombok.*;
import miniproject.infra.AbstractEvent;

@Data
@ToString
@NoArgsConstructor
public class BookViewed extends AbstractEvent {

    private Long bookId;
    private Long writerId;

    public BookViewed(Book aggregate) {
        super(aggregate);
        this.bookId = aggregate.getBookId();
        this.writerId = aggregate.getWriterId();
    }
}
