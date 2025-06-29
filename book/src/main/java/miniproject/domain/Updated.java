package miniproject.domain;

import lombok.*;
import miniproject.infra.AbstractEvent;

@Data
@ToString
@NoArgsConstructor
public class Updated extends AbstractEvent {

    private Long bookId;
    private String title;
    private String content;
    private Long writerId;
    private String status;

    public Updated(Book aggregate) {
        super(aggregate);
        this.bookId = aggregate.getBookId();
        this.title = aggregate.getTitle();
        this.content = aggregate.getContent();
        this.writerId = aggregate.getWriterId();
        this.status = aggregate.getStatus();
    }
}
