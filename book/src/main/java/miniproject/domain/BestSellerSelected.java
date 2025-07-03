package miniproject.domain;

import lombok.*;
import miniproject.infra.AbstractEvent;

@Data
@ToString
@NoArgsConstructor
public class BestSellerSelected extends AbstractEvent {

    private Long bookId;
    private String title;
    private String coverUrl;
    private Integer viewCount;
    private Long writerId;

    public BestSellerSelected(Book aggregate) {
        super(aggregate);
        this.bookId = aggregate.getBookId();
        this.title = aggregate.getTitle();
        this.coverUrl = aggregate.getCoverUrl();
        this.viewCount = aggregate.getViewCount();
        this.writerId = aggregate.getWriterId();
    }

    // 기본 생성자 사용 시 수동 세팅 가능
}
