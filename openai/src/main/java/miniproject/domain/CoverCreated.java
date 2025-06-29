package miniproject.domain;

import lombok.*;
import miniproject.infra.AbstractEvent;

@Data
@ToString
@NoArgsConstructor
public class CoverCreated extends AbstractEvent {

    private Long requestId;
    private Long bookId;
    private String prompt;
    private String coverUrl;

    public CoverCreated(OpenAi aggregate) {
        super(aggregate);
        this.requestId = aggregate.getRequestId();
        this.bookId = aggregate.getBookId();
        this.prompt = aggregate.getPrompt();
        this.coverUrl = aggregate.getCoverUrl();
    }
}