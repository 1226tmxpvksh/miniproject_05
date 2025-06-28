package miniproject.domain;

import lombok.*;
import miniproject.infra.AbstractEvent;

@Data
@ToString
@NoArgsConstructor
public class PublishRequested extends AbstractEvent {

    private Long bookId;
    private String title;
    private String content;
    private Long writerId;
    private String coverUrl;

    public PublishRequested(Book aggregate) {
        super(aggregate);
        this.bookId = aggregate.getBookId();
        this.title = aggregate.getTitle();
        this.content = aggregate.getContent();
        this.writerId = aggregate.getWriterId();
        this.coverUrl = aggregate.getCoverUrl();
    }
}
