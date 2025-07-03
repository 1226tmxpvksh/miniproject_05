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

    public BestSellerSelected(Object aggregate) {
        super(aggregate);
    }

    // 기본 생성자 사용 시 수동 세팅 가능
}
