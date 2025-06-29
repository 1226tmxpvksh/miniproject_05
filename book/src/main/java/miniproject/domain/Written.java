package miniproject.domain;

import lombok.*;
import miniproject.infra.AbstractEvent;

@Data
@ToString
@NoArgsConstructor
public class Written extends AbstractEvent {

    private Long bookId;
    private String title;
    private String content;
    private Long writerId;
    private String writerNickname;
    private String status;

    public Written(Book aggregate) {
        super(aggregate);
        this.bookId = aggregate.getBookId();
        this.title = aggregate.getTitle();
        this.content = aggregate.getContent();
        this.writerId = aggregate.getWriterId();
        this.writerNickname = aggregate.getWriterNickname();
        this.status = aggregate.getStatus();
    }
}
