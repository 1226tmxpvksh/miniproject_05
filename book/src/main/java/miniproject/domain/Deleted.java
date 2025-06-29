package miniproject.domain;

import lombok.*;
import miniproject.infra.AbstractEvent;

@Data
@ToString
@NoArgsConstructor
public class Deleted extends AbstractEvent {

    private Long bookId;
    private String title;
    private String content;
    private String coverUrl;

    public Deleted(Book aggregate) {
        super(aggregate);
        this.bookId = aggregate.getBookId();
        this.title = aggregate.getTitle();
        this.content = aggregate.getContent();
        this.coverUrl = aggregate.getCoverUrl();
    }
}
