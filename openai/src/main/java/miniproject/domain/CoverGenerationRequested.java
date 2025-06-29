package miniproject.domain;

import lombok.*;
import miniproject.infra.AbstractEvent;

@Data
@ToString
@NoArgsConstructor
public class CoverGenerationRequested extends AbstractEvent {

    private Long bookId;
    private String title;
    private String content;

    public CoverGenerationRequested(Book aggregate) {
        super(aggregate);
        this.bookId = aggregate.getBookId();
        this.title = aggregate.getTitle();
        this.content = aggregate.getContent();
    }
}