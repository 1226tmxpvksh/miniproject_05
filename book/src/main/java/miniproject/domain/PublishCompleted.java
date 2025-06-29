package miniproject.domain;

import lombok.*;
import miniproject.infra.AbstractEvent;

@Data
@ToString
@NoArgsConstructor
public class PublishCompleted extends AbstractEvent {

    private Long bookId;
    private String title;
    private String content;
    private Long writerId;
    private String coverUrl;
    private String writerNickname;
    private String status;

    public PublishCompleted(Book aggregate) {
        super(aggregate);
        this.bookId = aggregate.getBookId();
        this.title = aggregate.getTitle();
        this.content = aggregate.getContent();
        this.writerId = aggregate.getWriterId();
        this.coverUrl = aggregate.getCoverUrl();
        this.writerNickname = aggregate.getWriterNickname();
        this.status = aggregate.getStatus();
    }
}
