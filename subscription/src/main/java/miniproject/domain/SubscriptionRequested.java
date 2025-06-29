package miniproject.domain;

import lombok.*;
import miniproject.infra.AbstractEvent;

@Data
@ToString
@NoArgsConstructor
public class SubscriptionRequested extends AbstractEvent {

    private Long userId;
    private String email;
    private String nickname;
    private Boolean subscribed;
    private Boolean writerRequested;

    public SubscriptionRequested(User aggregate) {
        super(aggregate);
        this.userId = aggregate.getUserId();
        this.email = aggregate.getEmail();
        this.nickname = aggregate.getNickname();
        this.subscribed = true; // 기본값 설정
        this.writerRequested = false; // 기본값 설정
    }
}
