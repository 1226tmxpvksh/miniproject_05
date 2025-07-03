package miniproject.domain;

import lombok.*;
import miniproject.infra.AbstractEvent;

@Data
@ToString
@NoArgsConstructor
public class BookViewed extends AbstractEvent {

    private Long bookId;
    private String title;
    private String content;
    private Long writerId;
    private String coverUrl;
    private String status;
    private Integer viewCount; // ✅ 추가: 조회수도 이벤트로 전달 가능

    public BookViewed(Book aggregate) {
        super(aggregate);
        this.bookId = aggregate.getBookId();
        this.title = aggregate.getTitle();
        this.content = aggregate.getContent();
        this.writerId = aggregate.getWriterId();
        this.coverUrl = aggregate.getCoverUrl();
        this.status = aggregate.getStatus();
        this.viewCount = aggregate.getViewCount(); // ✅ 여기서 같이 보냄
    }
}
